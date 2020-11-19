package com.nukkitx.network.raknet;

import cn.nukkit.Server;
import com.nukkitx.network.raknet.util.RoundRobinIterator;
import com.nukkitx.network.util.Bootstraps;
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.network.util.EventLoops;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import static com.nukkitx.network.raknet.RakNetConstants.UDP_HEADER_SIZE;

@ParametersAreNonnullByDefault
public class RakNetServer extends RakNet {

    private static final InternalLogger log = InternalLoggerFactory.getInstance(RakNetServer.class);
    final ConcurrentMap<InetSocketAddress, RakNetServerSession> sessionsByAddress = new ConcurrentHashMap<>();
    private final ServerDatagramHandler datagramHandler = new ServerDatagramHandler();
    private final ConcurrentMap<InetAddress, Long> blockAddresses = new ConcurrentHashMap<>();
    private final Set<Channel> channels = new HashSet<>();
    private final Iterator<Channel> channelIterator = new RoundRobinIterator<>(channels);
    private volatile RakNetServerListener listener = null;
    private final int bindThreads;
    private int maxConnections = 1024;

    public RakNetServer(InetSocketAddress bindAddress) {
        this(bindAddress, 1);
    }

    public RakNetServer(InetSocketAddress bindAddress, int bindThreads) {
        this(bindAddress, bindThreads, EventLoops.commonGroup());
    }

    public RakNetServer(InetSocketAddress bindAddress, int bindThreads, EventLoopGroup eventLoopGroup) {
        super(bindAddress, eventLoopGroup);
        this.bindThreads = bindThreads;
    }

    @Override
    protected CompletableFuture<Void> bindInternal() {
        int bindThreads = Bootstraps.isReusePortAvailable() ? this.bindThreads : 1;
        ChannelFuture[] channelFutures = new ChannelFuture[bindThreads];

        for (int i = 0; i < bindThreads; i++) {
            channelFutures[i] = this.bootstrap.handler(datagramHandler).bind(this.bindAddress);
        }

        return Bootstraps.allOf(channelFutures);
    }

    public void block(InetAddress address) {
        Objects.requireNonNull(address, "address");
        this.blockAddresses.put(address, -1L);
    }

    public void block(InetAddress address, long timeout, TimeUnit timeUnit) {
        Objects.requireNonNull(address, "address");
        Objects.requireNonNull(address, "timeUnit");
        this.blockAddresses.put(address, System.currentTimeMillis() + timeUnit.toMillis(timeout));
    }

    public boolean unblock(InetAddress address) {
        Objects.requireNonNull(address, "address");
        return this.blockAddresses.remove(address) != null;
    }

    public int getSessionCount() {
        return this.sessionsByAddress.size();
    }

    @Nullable
    public RakNetServerSession getSession(InetSocketAddress address) {
        return this.sessionsByAddress.get(address);
    }

    @Nonnegative
    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(@Nonnegative int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public RakNetServerListener getListener() {
        return listener;
    }

    public void setListener(RakNetServerListener listener) {
        this.listener = listener;
    }

    public void send(InetSocketAddress address, ByteBuf buffer) {
        channelIterator.next().writeAndFlush(new DatagramPacket(buffer, address));
    }

    @Override
    public void close() {
        super.close();
        for (RakNetServerSession session : this.sessionsByAddress.values()) {
            session.disconnect(DisconnectReason.SHUTTING_DOWN);
        }
        for (Channel channel : this.channels) {
            channel.close().syncUninterruptibly();
        }
    }

    @Override
    protected void onTick() {
        final long curTime = System.currentTimeMillis();
        for (RakNetServerSession session : this.sessionsByAddress.values()) {
            session.eventLoop.execute(() -> session.onTick(curTime));
        }
        Iterator<Long> blockedAddresses = this.blockAddresses.values().iterator();
        long timeout;
        while (blockedAddresses.hasNext()) {
            timeout = blockedAddresses.next();
            if (timeout > 0 && timeout < curTime) {
                blockedAddresses.remove();
            }
        }
    }

    private void onOpenConnectionRequest1(ChannelHandlerContext ctx, DatagramPacket packet) {
        if (!packet.content().isReadable(16)) {
            return;
        }
        // We want to do as many checks as possible before creating a session so memory is not wasted.
        ByteBuf buffer = packet.content();
        if (!RakNetUtils.verifyUnconnectedMagic(buffer)) {
            return;
        }
        int protocol = buffer.readUnsignedByte(); // Protocol version
        int mtu = buffer.readableBytes() + 18 + (packet.sender().getAddress() instanceof Inet6Address ? 40 : 20)
                + UDP_HEADER_SIZE; // 1 (Packet ID), 16 (Magic), 1 (Protocol Version), 20/40 (IP Header)

        RakNetServerSession session = this.sessionsByAddress.get(packet.sender());

        if (session != null && session.getState() == RakNetState.CONNECTED) {
            this.sendAlreadyConnected(ctx, packet.sender());
        /*} else if (this.maxConnections >= 0 && this.maxConnections <= getSessionCount()) {
            this.sendNoFreeIncomingConnections(ctx, packet.sender());*/
        } else if (this.listener != null && !this.listener.onConnectionRequest(packet.sender())) {
            this.sendConnectionBanned(ctx, packet.sender());
        } else if (Server.getInstance().strongIPBans && Server.getInstance().getIPBans().isBanned(packet.sender().getHostName())) {
            this.sendConnectionBanned(ctx, packet.sender());
            Server.getInstance().getLogger().info("\u00A7c" + packet.sender().getHostName() + " disconnected due to IP banned");
        } else if (session == null) {
            // Passed all checks. Now create the session and send the first reply.
            session = new RakNetServerSession(this, packet.sender(), ctx.channel(), mtu,
                    this.eventLoopGroup.next(), protocol);
            session.setState(RakNetState.INITIALIZING);
            if (this.sessionsByAddress.putIfAbsent(packet.sender(), session) == null) {
                session.sendOpenConnectionReply1();
                if (listener != null) {
                    listener.onSessionCreation(session);
                } else {
                    Server.getInstance().getLogger().warning("Unable to create session for " + packet.sender().getHostName() + ": listener is null");
                }
            }
        } else {
            session.sendOpenConnectionReply1(); // Probably a packet loss occurred, send the reply again
        }
    }

    private void onUnconnectedPing(ChannelHandlerContext ctx, DatagramPacket packet) {
        if (!packet.content().isReadable(24)) {
            return;
        }
        long pingTime = packet.content().readLong();
        if (!RakNetUtils.verifyUnconnectedMagic(packet.content())) {
            return;
        }

        byte[] userData = null;

        if (this.listener != null) {
            userData = this.listener.onQuery(packet.sender());
        }
        if (userData == null) {
            userData = new byte[0];
        }

        int packetLength = 35 + userData.length;

        ByteBuf buffer = ctx.alloc().ioBuffer(packetLength, packetLength);

        buffer.writeByte(RakNetConstants.ID_UNCONNECTED_PONG);
        buffer.writeLong(pingTime);
        buffer.writeLong(this.guid);
        RakNetUtils.writeUnconnectedMagic(buffer);
        buffer.writeShort(userData.length);
        buffer.writeBytes(userData);

        RakNet.send(ctx, packet.sender(), buffer);
    }

    /*
    Packet Dispatchers
     */

    private void sendAlreadyConnected(ChannelHandlerContext ctx, InetSocketAddress recipient) {
        ByteBuf buffer = ctx.alloc().ioBuffer(25, 25);
        buffer.writeByte(RakNetConstants.ID_ALREADY_CONNECTED);
        RakNetUtils.writeUnconnectedMagic(buffer);
        buffer.writeLong(this.guid);

        RakNet.send(ctx, recipient, buffer);
        Server.getInstance().getLogger().debug("Already connected");
    }

    private void sendConnectionBanned(ChannelHandlerContext ctx, InetSocketAddress recipient) {
        ByteBuf buffer = ctx.alloc().ioBuffer(25, 25);
        buffer.writeByte(RakNetConstants.ID_CONNECTION_BANNED);
        RakNetUtils.writeUnconnectedMagic(buffer);
        buffer.writeLong(this.guid);

        RakNet.send(ctx, recipient, buffer);
        Server.getInstance().getLogger().debug("Connection banned");
    }

    private void sendNoFreeIncomingConnections(ChannelHandlerContext ctx, InetSocketAddress recipient) {
        ByteBuf buffer = ctx.alloc().ioBuffer(25, 25);
        buffer.writeByte(RakNetConstants.ID_NO_FREE_INCOMING_CONNECTIONS);
        RakNetUtils.writeUnconnectedMagic(buffer);
        buffer.writeLong(this.guid);

        RakNet.send(ctx, recipient, buffer);
        Server.getInstance().getLogger().debug("No free incoming connections");
    }

    @ChannelHandler.Sharable
    private class ServerDatagramHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            if (!(msg instanceof DatagramPacket)) {
                return;
            }

            final DatagramPacket packet = (DatagramPacket) msg;

            try {
                if (blockAddresses.containsKey(packet.sender().getAddress())) {
                    // Ignore these addresses altogether.
                    return;
                }

                ByteBuf content = packet.content();
                if (!content.isReadable()) {
                    // We have no use for empty packets.
                    return;
                }
                byte packetId = content.readByte();

                // These packets don't require a session
                switch (packetId) {
                    case RakNetConstants.ID_UNCONNECTED_PING:
                        RakNetServer.this.onUnconnectedPing(ctx, packet);
                        return;
                    case RakNetConstants.ID_OPEN_CONNECTION_REQUEST_1:
                        RakNetServer.this.onOpenConnectionRequest1(ctx, packet);
                        return;
                }
                content.readerIndex(0);

                RakNetServerSession session = RakNetServer.this.sessionsByAddress.get(packet.sender());

                if (session != null) {
                    session.onDatagram(packet);
                }
                if (RakNetServer.this.listener != null) {
                    RakNetServer.this.listener.onUnhandledDatagram(ctx, packet);
                }
            } finally {
                packet.release();
            }
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            if (ctx.channel().isRegistered()) {
                RakNetServer.this.channels.add(ctx.channel());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            log.error("An exception occurred in RakNet", cause);
        }
    }
}

package com.nukkitx.network.raknet;

import cn.nukkit.Server;
import com.nukkitx.network.raknet.pipeline.*;
import com.nukkitx.network.raknet.util.RoundRobinIterator;
import com.nukkitx.network.util.Bootstraps;
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.network.util.EventLoops;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

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

import static com.nukkitx.network.raknet.RakNetConstants.*;

@ParametersAreNonnullByDefault
public class RakNetServer extends RakNet {

    private static final InternalLogger log = InternalLoggerFactory.getInstance(RakNetServer.class);

    private final ConcurrentMap<InetAddress, Long> blockAddresses = new ConcurrentHashMap<>();
    final ConcurrentMap<InetSocketAddress, RakNetServerSession> sessionsByAddress = new ConcurrentHashMap<>();

    private final InetSocketAddress bindAddress;
    private final int bindThreads;

    private final Set<Channel> channels = new HashSet<>();
    private final Iterator<Channel> channelIterator = new RoundRobinIterator<>(channels);

    private final ServerChannelInitializer initializer = new ServerChannelInitializer();
    private final ServerMessageHandler messageHandler = new ServerMessageHandler(this);
    private final ServerDatagramHandler serverDatagramHandler = new ServerDatagramHandler(this);
    private final RakExceptionHandler exceptionHandler = new RakExceptionHandler();

    private volatile RakNetServerListener listener = null;

    public RakNetServer(InetSocketAddress bindAddress) {
        this(bindAddress, 1);
    }

    public RakNetServer(InetSocketAddress bindAddress, int bindThreads) {
        this(bindAddress, bindThreads, EventLoops.commonGroup());
    }

    public RakNetServer(InetSocketAddress bindAddress, int bindThreads, EventLoopGroup eventLoopGroup) {
        super(eventLoopGroup);
        this.bindThreads = bindThreads;
        this.bindAddress = bindAddress;
    }

    @Override
    protected CompletableFuture<Void> bindInternal() {
        int bindThreads = Bootstraps.isReusePortAvailable() ? this.bindThreads : 1;
        ChannelFuture[] channelFutures = new ChannelFuture[bindThreads];

        for (int i = 0; i < bindThreads; i++) {
            channelFutures[i] = this.bootstrap.handler(this.initializer).bind(this.bindAddress);
        }
        return Bootstraps.allOf(channelFutures);
    }

    public void send(InetSocketAddress address, ByteBuf buffer) {
        this.channelIterator.next().writeAndFlush(new DatagramPacket(buffer, address));
    }

    @Override
    public void close(boolean force) {
        super.close(force);
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

    public void onOpenConnectionRequest1(ChannelHandlerContext ctx, DatagramPacket packet) {
        if (!packet.content().isReadable(16)) {
            return;
        }
        // We want to do as many checks as possible before creating a session so memory is not wasted.
        ByteBuf buffer = packet.content();
        if (!RakNetUtils.verifyUnconnectedMagic(buffer)) {
            return;
        }
        int protocolVersion = buffer.readUnsignedByte();
        int mtu = buffer.readableBytes() + 18 + (packet.sender().getAddress() instanceof Inet6Address ? 40 : 20) + UDP_HEADER_SIZE; // 1 (Packet ID), 16 (Magic), 1 (Protocol Version), 20/40 (IP Header)

        RakNetServerSession session = this.sessionsByAddress.get(packet.sender());

        if (session != null && session.getState() == RakNetState.CONNECTED) {
            this.sendAlreadyConnected(ctx, packet.sender());
        } else if (this.listener != null && !this.listener.onConnectionRequest(packet.sender(), packet.sender())) {
            this.sendConnectionBanned(ctx, packet.sender());
        } else if (Server.getInstance().strongIPBans && Server.getInstance().getIPBans().isBanned(packet.sender().getHostName())) {
            this.sendConnectionBanned(ctx, packet.sender());
            Server.getInstance().getLogger().info("\u00A7c" + packet.sender().getHostName() + " disconnected due to IP banned");
        } else if (session == null) {
            // Passed all checks. Now create the session and send the first reply.
            session = new RakNetServerSession(this, packet.sender(), ctx.channel(),
                    ctx.channel().eventLoop().next(), mtu, protocolVersion);
            if (this.sessionsByAddress.putIfAbsent(packet.sender(), session) == null) {
                session.setState(RakNetState.INITIALIZING);
                session.sendOpenConnectionReply1();
                if (listener != null) {
                    listener.onSessionCreation(session);
                } else {
                    Server.getInstance().getLogger().warning("Unable to create session for " + packet.sender().getHostName() + ": listener is null");
                }
            }
        } else {
            if (session.getState() != RakNetState.INITIALIZING) {
                Server.getInstance().getLogger().warning("New connection attempt in state " + session.getState().toString());
            }
            session.setMtu(mtu);
            session.sendOpenConnectionReply1(); // Probably a packet loss occurred, send the reply again
        }
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

    public boolean isBlocked(InetAddress address) {
        return this.blockAddresses.containsKey(address);
    }

    public int getSessionCount() {
        return this.sessionsByAddress.size();
    }

    @Nullable
    public RakNetServerSession getSession(InetSocketAddress address) {
        return this.sessionsByAddress.get(address);
    }

    @Override
    public InetSocketAddress getBindAddress() {
        return this.bindAddress;
    }

    public RakNetServerListener getListener() {
        return listener;
    }

    public void setListener(RakNetServerListener listener) {
        this.listener = listener;
    }

    /*
     * Packet Dispatchers
     */

    private void sendAlreadyConnected(ChannelHandlerContext ctx, InetSocketAddress recipient) {
        ByteBuf buffer = ctx.alloc().ioBuffer(25, 25);
        buffer.writeByte(ID_ALREADY_CONNECTED);
        RakNetUtils.writeUnconnectedMagic(buffer);
        buffer.writeLong(this.guid);
        ctx.writeAndFlush(new DatagramPacket(buffer, recipient));
        Server.getInstance().getLogger().debug("Already connected");
    }

    private void sendConnectionBanned(ChannelHandlerContext ctx, InetSocketAddress recipient) {
        ByteBuf buffer = ctx.alloc().ioBuffer(25, 25);
        buffer.writeByte(ID_CONNECTION_BANNED);
        RakNetUtils.writeUnconnectedMagic(buffer);
        buffer.writeLong(this.guid);
        ctx.writeAndFlush(new DatagramPacket(buffer, recipient));
        Server.getInstance().getLogger().debug("Connection banned");
    }

    @ChannelHandler.Sharable
    private class ServerChannelInitializer extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel channel) throws Exception {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(RakOutboundHandler.NAME, new RakOutboundHandler());
            pipeline.addLast(ServerMessageHandler.NAME, RakNetServer.this.messageHandler);
            pipeline.addLast(ServerDatagramHandler.NAME, RakNetServer.this.serverDatagramHandler);
            pipeline.addLast(RakExceptionHandler.NAME, RakNetServer.this.exceptionHandler);
            RakNetServer.this.channels.add(channel);
        }
    }
}

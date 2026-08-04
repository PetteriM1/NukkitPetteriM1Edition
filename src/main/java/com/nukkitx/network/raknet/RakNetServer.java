package com.nukkitx.network.raknet;

import cn.nukkit.Server;
import com.nukkitx.network.raknet.pipeline.RakExceptionHandler;
import com.nukkitx.network.raknet.pipeline.RakOutboundHandler;
import com.nukkitx.network.raknet.pipeline.ServerDatagramHandler;
import com.nukkitx.network.raknet.pipeline.ServerMessageHandler;
import com.nukkitx.network.util.Bootstraps;
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.network.util.EventLoops;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import static com.nukkitx.network.raknet.RakNetConstants.*;

@ParametersAreNonnullByDefault
public class RakNetServer extends RakNet {

    private final ConcurrentMap<InetAddress, Long> blockAddresses = new ConcurrentHashMap<>();
    final ConcurrentMap<InetSocketAddress, RakNetServerSession> sessionsByAddress = new ConcurrentHashMap<>();

    private final InetSocketAddress bindAddress;
    private final int bindThreads;

    private final ServerChannelInitializer initializer = new ServerChannelInitializer();
    private final ServerMessageHandler messageHandler = new ServerMessageHandler(this);
    private final ServerDatagramHandler serverDatagramHandler = new ServerDatagramHandler(this);
    private final RakExceptionHandler exceptionHandler = new RakExceptionHandler(this);

    private volatile RakNetServerListener listener = null;
    private volatile Channel channel;

    private final boolean disableRakBans = Server.getInstance().getPropertyBoolean("disable-rak-bans", false);
    private int blockListUpdateTick;

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
        if (this.channel != null) {
            this.channel.writeAndFlush(new DatagramPacket(buffer, address));
        }
    }

    @Override
    public void close(boolean force) {
        super.close(force);
        for (RakNetServerSession session : this.sessionsByAddress.values()) {
            session.disconnect(DisconnectReason.SHUTTING_DOWN);
        }
        if (this.channel != null) {
            this.channel.close().syncUninterruptibly();
        }
    }

    @Override
    protected void onTick() {
        final long curTime = RakNet.time();

        for (RakNetServerSession session : this.sessionsByAddress.values()) {
            if (session.isClosed() || session.disconnected) {
                continue;
            }

            if (curTime - session.lastTouched >= SESSION_TIMEOUT_MS) {
                session.close(DisconnectReason.TIMED_OUT);
                continue;
            }

            if (curTime - session.lastUpdate > 1000) {
                session.lastUpdate = curTime;
                session.receivedBytes = 0;
            }

            if (session.getState().ordinal() >= RakNetState.INITIALIZED.ordinal() && curTime - session.lastTick > 50) {
                session.eventLoop.execute(() -> session.tick(curTime));
            }
        }

        if (!disableRakBans && this.blockListUpdateTick++ > 100) {
            this.blockListUpdateTick = 0;

            Iterator<Long> blockedAddresses = this.blockAddresses.values().iterator();
            long timeout;
            while (blockedAddresses.hasNext()) {
                timeout = blockedAddresses.next();
                if (timeout > 0 && timeout < curTime) {
                    blockedAddresses.remove();
                }
            }
        }
    }

    public void onOpenConnectionRequest1(ChannelHandlerContext ctx, DatagramPacket packet) {
        ByteBuf buffer = packet.content();
        if (!buffer.isReadable(16)) {
            return;
        }

        // We want to do as many checks as possible before creating a session so memory is not wasted.
        if (!RakNetUtils.verifyUnconnectedMagic(buffer)) {
            return;
        }

        int protocolVersion = buffer.readUnsignedByte();
        int mtu = buffer.readableBytes() + 18 + (packet.sender().getAddress() instanceof Inet6Address ? 40 : 20) + UDP_HEADER_SIZE; // 1 (Packet ID), 16 (Magic), 1 (Protocol Version), 20/40 (IP Header)

        RakNetServerSession session = this.sessionsByAddress.get(packet.sender());

        if (session != null && session.getState() == RakNetState.CONNECTED) {
            Server.getInstance().getLogger().debug(packet.sender() + " already connected");
        } else if (this.listener != null && !this.listener.onConnectionRequest(packet.sender())) {
            Server.getInstance().getLogger().debug(packet.sender() + " connection banned");
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
                    Server.getInstance().getLogger().warning("Unable to create session due to null listener for " + packet.sender());
                }
            } else {
                Server.getInstance().getLogger().warning("Previous session was still listed for " + packet.sender());
            }
        } else {
            /*if (session.getState() != RakNetState.INITIALIZING) {
                Server.getInstance().getLogger().warning("New connection attempt in state " + session.getState().toString());
            }*/
            session.setMtu(mtu);
            session.sendOpenConnectionReply1(); // Probably a packet loss occurred, send the reply again
        }
    }

    public void block(InetAddress address) {
        this.blockAddresses.put(address, -1L);
    }

    public void block(InetAddress address, long timeout, TimeUnit timeUnit) {
        this.blockAddresses.put(address, RakNet.time() + timeUnit.toMillis(timeout));
    }

    public boolean unblock(InetAddress address) {
        return this.blockAddresses.remove(address) != null;
    }

    public boolean isBlocked(InetAddress address) {
        return this.blockAddresses.containsKey(address);
    }

    @Nullable
    public RakNetServerSession getSession(InetSocketAddress address) {
        return this.sessionsByAddress.get(address);
    }

    public RakNetServerListener getListener() {
        return listener;
    }

    public void setListener(RakNetServerListener listener) {
        this.listener = listener;
    }

    @ChannelHandler.Sharable
    private class ServerChannelInitializer extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel channel) throws Exception {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(RakOutboundHandler.NAME, new RakOutboundHandler(RakNetServer.this));
            if (!disableRakBans) {
                pipeline.addLast(ServerMessageHandler.NAME, RakNetServer.this.messageHandler);
            }
            pipeline.addLast(ServerDatagramHandler.NAME, RakNetServer.this.serverDatagramHandler);
            pipeline.addLast(RakExceptionHandler.NAME, RakNetServer.this.exceptionHandler);
            RakNetServer.this.channel = channel;
        }
    }
}

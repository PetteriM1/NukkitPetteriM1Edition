package com.nukkitx.network.raknet;

import com.nukkitx.network.util.EventLoops;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import lombok.RequiredArgsConstructor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.concurrent.*;

@ParametersAreNonnullByDefault
public class RakNetClient extends RakNet {

    private static final InternalLogger log = InternalLoggerFactory.getInstance(RakNetClient.class);
    private final ClientDatagramHandler handler = new ClientDatagramHandler();
    private final ConcurrentMap<InetSocketAddress, PingEntry> pings = new ConcurrentHashMap<>();
    RakNetClientSession session;
    private Channel channel;

    public RakNetClient(InetSocketAddress bindAddress) {
        this(bindAddress, EventLoops.commonGroup());
    }

    public RakNetClient(InetSocketAddress bindAddress, EventLoopGroup eventLoopGroup) {
        super(bindAddress, eventLoopGroup);
    }

    @Override
    protected CompletableFuture<Void> bindInternal() {
        ChannelFuture channelFuture = this.bootstrap.handler(this.handler).bind(this.bindAddress);

        CompletableFuture<Void> future = new CompletableFuture<>();
        channelFuture.addListener(future1 -> {
            if (future1.cause() != null) {
                future.completeExceptionally(future1.cause());
            }
            future.complete(null);
        });
        return future;
    }

    public RakNetClientSession create(InetSocketAddress address) {
        if (!this.isRunning()) {
            throw new IllegalStateException("RakNet has not been started");
        }
        if (session != null) {
            throw new IllegalStateException("Session has already been created");
        }

        this.session = new RakNetClientSession(this, address, this.channel, RakNetConstants.MAXIMUM_MTU_SIZE,
                this.eventLoopGroup.next());
        return this.session;
    }

    public CompletableFuture<RakNetPong> ping(InetSocketAddress address, long timeout, TimeUnit unit) {
        if (!this.isRunning()) {
            throw new IllegalStateException("RakNet has not been started");
        }

        if (this.session != null && this.session.address.equals(address)) {
            throw new IllegalArgumentException("Cannot ping connected address");
        }
        if (this.pings.containsKey(address)) {
            return this.pings.get(address).future;
        }

        CompletableFuture<RakNetPong> pongFuture = new CompletableFuture<>();

        PingEntry entry = new PingEntry(pongFuture, System.currentTimeMillis() + unit.toMillis(timeout));
        this.pings.put(address, entry);
        this.sendUnconnectedPing(address);

        return pongFuture;
    }

    @Override
    protected void onTick() {
        final long curTime = System.currentTimeMillis();
        if (this.session != null) {
            session.eventLoop.execute(() -> session.onTick(curTime));
        }
        Iterator<PingEntry> iterator = this.pings.values().iterator();
        while (iterator.hasNext()) {
            PingEntry entry = iterator.next();
            if (curTime >= entry.timeout) {
                entry.future.completeExceptionally(new TimeoutException());
                iterator.remove();
            }
        }
    }

    private void onUnconnectedPong(DatagramPacket packet) {
        PingEntry entry = this.pings.get(packet.sender());
        if (entry == null) {
            return;
        }

        ByteBuf content = packet.content();
        long pingTime = content.readLong();
        long guid = content.readLong();
        if (!RakNetUtils.verifyUnconnectedMagic(content)) {
            return;
        }

        byte[] userData = null;
        if (content.isReadable()) {
            userData = new byte[content.readUnsignedShort()];
            content.readBytes(userData);
        }

        entry.future.complete(new RakNetPong(pingTime, System.currentTimeMillis(), guid, userData));
    }

    private void sendUnconnectedPing(InetSocketAddress recipient) {
        ByteBuf buffer = this.channel.alloc().ioBuffer(23);
        buffer.writeByte(RakNetConstants.ID_UNCONNECTED_PING);
        buffer.writeLong(System.currentTimeMillis());
        RakNetUtils.writeUnconnectedMagic(buffer);
        buffer.writeLong(this.guid);

        this.channel.writeAndFlush(new DatagramPacket(buffer, recipient));
    }

    @RequiredArgsConstructor
    private static class PingEntry {
        private final CompletableFuture<RakNetPong> future;
        private final long timeout;
    }

    private class ClientDatagramHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            if (!(msg instanceof DatagramPacket)) {
                return;
            }

            DatagramPacket packet = (DatagramPacket) msg;
            try {
                ByteBuf content = packet.content();
                int packetId = content.readUnsignedByte();

                if (packetId == RakNetConstants.ID_UNCONNECTED_PONG) {
                    RakNetClient.this.onUnconnectedPong(packet);
                } else if (session != null) {
                    content.readerIndex(0);
                    session.onDatagram(packet);
                }
            } finally {
                packet.release();
            }
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            if (ctx.channel().isRegistered()) {
                RakNetClient.this.channel = ctx.channel();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            log.error("An exception occurred in RakNet", cause);
        }
    }
}

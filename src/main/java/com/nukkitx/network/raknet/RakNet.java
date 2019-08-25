package com.nukkitx.network.raknet;

import com.nukkitx.network.util.Bootstraps;
import com.nukkitx.network.util.Preconditions;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.DatagramPacket;

import javax.annotation.Nonnegative;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@ParametersAreNonnullByDefault
public abstract class RakNet implements AutoCloseable {

    final long guid = ThreadLocalRandom.current().nextLong();
    final Bootstrap bootstrap;
    final EventLoopGroup eventLoopGroup;
    final InetSocketAddress bindAddress;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private ScheduledFuture<?> tickFuture;
    int protocolVersion = RakNetConstants.RAKNET_PROTOCOL_VERSION;
    private volatile boolean closed;

    RakNet(InetSocketAddress bindAddress, EventLoopGroup eventLoopGroup) {
        this.bindAddress = bindAddress;
        this.eventLoopGroup = eventLoopGroup;

        this.bootstrap = new Bootstrap().option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT);

        Bootstraps.setupBootstrap(this.bootstrap, true);
        this.bootstrap.group(eventLoopGroup);
    }

    static void send(ChannelHandlerContext ctx, InetSocketAddress recipient, ByteBuf buffer) {
        ctx.writeAndFlush(new DatagramPacket(buffer, recipient), ctx.voidPromise());
    }

    public CompletableFuture<Void> bind() {
        Preconditions.checkState(this.running.compareAndSet(false, true), "RakNet has already been started");

        CompletableFuture<Void> future = bindInternal();

        future.whenComplete((aVoid, throwable) -> {
            if (throwable != null) {
                // Failed to start. Set running to false
                this.running.compareAndSet(true, false);
            } else {
                this.closed = false;
                this.tickFuture = this.eventLoopGroup.scheduleAtFixedRate(this::onTick, 0, 10,
                        TimeUnit.MILLISECONDS);
            }
        });
        return future;
    }

    public void close() {
        this.closed = true;
        if (this.tickFuture != null) {
            this.tickFuture.cancel(false);
        }
    }

    protected abstract CompletableFuture<Void> bindInternal();

    protected abstract void onTick();

    public boolean isRunning() {
        return this.running.get();
    }

    public boolean isClosed() {
        return closed;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    @Nonnegative
    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(@Nonnegative int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public InetSocketAddress getBindAddress() {
        return bindAddress;
    }

    public long getGuid() {
        return guid;
    }
}

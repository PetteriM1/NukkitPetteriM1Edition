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

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@ParametersAreNonnullByDefault
public abstract class RakNet implements AutoCloseable {

    protected final long guid = ThreadLocalRandom.current().nextLong();
    protected final Bootstrap bootstrap;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private ScheduledFuture<?> tickFuture;
    protected final AtomicBoolean closed = new AtomicBoolean(false);

    RakNet(EventLoopGroup eventLoopGroup) {
        this.bootstrap = new Bootstrap().option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT);
        this.bootstrap.group(eventLoopGroup);
        Bootstraps.setupBootstrap(this.bootstrap, true);
    }

    static void send(ChannelHandlerContext ctx, InetSocketAddress recipient, ByteBuf buffer) {
        ctx.writeAndFlush(new DatagramPacket(buffer, recipient), ctx.voidPromise());
    }

    public CompletableFuture<Void> bind() {
        Preconditions.checkState(this.running.compareAndSet(false, true), "RakNet has already been started");
        CompletableFuture<Void> future = this.bindInternal();

        future.whenComplete((aVoid, throwable) -> {
            if (throwable != null) {
                // Failed to start. Set running to false
                this.running.compareAndSet(true, false);
                return;
            }

            this.closed.set(false);
            this.tickFuture = this.getEventLoopGroup().next().scheduleAtFixedRate(this::onTick, 0, 10, TimeUnit.MILLISECONDS);
        });
        return future;
    }

    public void close() {
        this.close(false);
    }

    public void close(boolean force) {
        this.closed.set(true);
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
        return this.closed.get();
    }

    public Bootstrap getBootstrap() {
        return this.bootstrap;
    }

    public abstract InetSocketAddress getBindAddress();

    public long getGuid() {
        return this.guid;
    }

    protected EventLoopGroup getEventLoopGroup() {
        return this.bootstrap.config().group();
    }
}

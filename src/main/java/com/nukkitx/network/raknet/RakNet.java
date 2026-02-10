package com.nukkitx.network.raknet;

import cn.nukkit.Server;
import com.google.common.base.Preconditions;
import com.nukkitx.network.util.Bootstraps;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This RakNet implementation is based on CloudburstMC/Network version develop/6cdcaf5 (1.6.28-SNAPSHOT)
 */
@ParametersAreNonnullByDefault
public abstract class RakNet implements AutoCloseable {

    protected final long guid;
    private final AtomicBoolean running = new AtomicBoolean(false);
    protected final AtomicBoolean closed = new AtomicBoolean(false);
    protected final Bootstrap bootstrap;
    private ScheduledFuture<?> tickFuture;

    RakNet(EventLoopGroup eventLoopGroup) {
        this.guid = Server.getInstance().suomiCraftPEMode() ? ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE) : ThreadLocalRandom.current().nextLong();
        this.bootstrap = new Bootstrap().option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT);
        this.bootstrap.group(eventLoopGroup);
        Bootstraps.setupBootstrap(this.bootstrap, true);
    }

    public abstract InetSocketAddress getBindAddress();

    public Bootstrap getBootstrap() {
        return this.bootstrap;
    }

    protected EventLoopGroup getEventLoopGroup() {
        return this.bootstrap.config().group();
    }

    public long getGuid() {
        return this.guid;
    }

    public boolean isClosed() {
        return this.closed.get();
    }

    public boolean isRunning() {
        return this.running.get();
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
            this.tickFuture = this.nextEventLoop().scheduleAtFixedRate(this::onTick, 0, 10, TimeUnit.MILLISECONDS);
        });
        return future;
    }

    protected abstract CompletableFuture<Void> bindInternal();

    public void close(boolean force) {
        this.closed.set(true);
        if (this.tickFuture != null) {
            this.tickFuture.cancel(false);
        }
    }

    public void close() {
        this.close(false);
    }

    protected EventLoop nextEventLoop() {
        return this.getEventLoopGroup().next();
    }

    protected abstract void onTick();
}

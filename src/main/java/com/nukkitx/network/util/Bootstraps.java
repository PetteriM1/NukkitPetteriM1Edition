package com.nukkitx.network.util;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.epoll.Native;
import io.netty.channel.unix.UnixChannelOption;
import lombok.experimental.UtilityClass;

import java.util.concurrent.CompletableFuture;

import static com.nukkitx.network.util.EventLoops.getChannelType;

@UtilityClass
public final class Bootstraps {

    private static final int[] REUSEPORT_VERSION = {3, 9, 0};
    private static final boolean REUSEPORT_AVAILABLE;

    static {
        String kernelVersion;
        try {
            kernelVersion = Native.KERNEL_VERSION;
        } catch (Throwable e) {
            kernelVersion = null;
        }
        if (kernelVersion != null && kernelVersion.contains("-")) {
            int index = kernelVersion.indexOf('-');
            if (index > -1) {
                kernelVersion = kernelVersion.substring(0, index);
            }
            int[] kernelVer = fromString(kernelVersion);
            REUSEPORT_AVAILABLE = checkVersion(kernelVer, 0);
        } else {
            REUSEPORT_AVAILABLE = false;
        }
    }

    public static boolean isReusePortAvailable() {
        return REUSEPORT_AVAILABLE;
    }

    public static void setupBootstrap(Bootstrap bootstrap, boolean datagram) {
        Class<? extends Channel> channel = datagram ? getChannelType().getDatagramChannel() : getChannelType().getSocketChannel();
        bootstrap.channel(channel);

        setupAbstractBootstrap(bootstrap);
    }

    private static void setupAbstractBootstrap(AbstractBootstrap bootstrap) {
        if (REUSEPORT_AVAILABLE) {
            bootstrap.option(UnixChannelOption.SO_REUSEPORT, true);
        }
    }

    private static int[] fromString(String ver) {
        String[] parts = ver.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("At least 2 version numbers required");
        }

        return new int[]{
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                parts.length == 2 ? 0 : Integer.parseInt(parts[2].split("\\+")[0]) // Nukkit/issues/2178
        };
    }

    private static boolean checkVersion(int[] ver, int i) {
        if (ver[i] > REUSEPORT_VERSION[i]) {
            return true;
        } else if (ver[i] == REUSEPORT_VERSION[i]) {
            if (ver.length == (i + 1)) {
                return true;
            } else {
                return checkVersion(ver, i + 1);
            }
        }
        return false;
    }

    public static CompletableFuture<Void> allOf(ChannelFuture... futures) {
        if (futures == null || futures.length == 0) {
            return CompletableFuture.completedFuture(null);
        }
        @SuppressWarnings("unchecked")
        CompletableFuture<Channel>[] completableFutures = new CompletableFuture[futures.length];
        for (int i = 0; i < futures.length; i++) {
            ChannelFuture channelFuture = futures[i];
            CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
            channelFuture.addListener(future -> {
                if (future.cause() != null) {
                    completableFuture.completeExceptionally(future.cause());
                }
                completableFuture.complete(channelFuture.channel());
            });
            completableFutures[i] = completableFuture;
        }

        return CompletableFuture.allOf(completableFutures);
    }
}

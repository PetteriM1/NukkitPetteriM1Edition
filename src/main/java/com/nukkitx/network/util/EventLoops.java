package com.nukkitx.network.util;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.*;
import io.netty.channel.kqueue.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadFactory;
import java.util.function.Function;

@UtilityClass
public final class EventLoops {

    static final ChannelType CHANNEL_TYPE;
    static final EventLoopGroup EVENT_LOOP_GROUP;
    private static final ThreadFactory EVENT_LOOP_FACTORY = NetworkThreadFactory.builder().format("Network Listener - #%d")
            .daemon(true).build();

    static {
        boolean disableNative = System.getProperties().contains("disableNativeEventLoop");

        if (!disableNative && Epoll.isAvailable()) {
            CHANNEL_TYPE = ChannelType.EPOLL;
        } else if (!disableNative && KQueue.isAvailable()) {
            CHANNEL_TYPE = ChannelType.KQUEUE;
        } else {
            CHANNEL_TYPE = ChannelType.NIO;
        }
        EVENT_LOOP_GROUP = CHANNEL_TYPE.eventLoopGroupFactory.apply(0);
    }

    public static EventLoopGroup commonGroup() {
        return EVENT_LOOP_GROUP;
    }

    public static EventLoopGroup newEventLoopGroup(int threads) {
        return CHANNEL_TYPE.eventLoopGroupFactory.apply(threads);
    }

    public static ChannelType getChannelType() {
        return CHANNEL_TYPE;
    }

    @RequiredArgsConstructor
    public enum ChannelType {
        EPOLL(EpollDatagramChannel.class, EpollSocketChannel.class, EpollServerSocketChannel.class,
                threads -> new EpollEventLoopGroup(threads, EVENT_LOOP_FACTORY)),
        KQUEUE(KQueueDatagramChannel.class, KQueueSocketChannel.class, KQueueServerSocketChannel.class,
                threads -> new KQueueEventLoopGroup(threads, EVENT_LOOP_FACTORY)),
        NIO(NioDatagramChannel.class, NioSocketChannel.class, NioServerSocketChannel.class,
                threads -> new NioEventLoopGroup(threads, EVENT_LOOP_FACTORY));

        final Class<? extends DatagramChannel> datagramChannel;
        final Class<? extends SocketChannel> socketChannel;
        final Class<? extends ServerSocketChannel> serverSocketChannel;
        private final Function<Integer, EventLoopGroup> eventLoopGroupFactory;

        public Class<? extends DatagramChannel> getDatagramChannel() {
            return datagramChannel;
        }

        public Class<? extends ServerSocketChannel> getServerSocketChannel() {
            return serverSocketChannel;
        }

        public Class<? extends SocketChannel> getSocketChannel() {
            return socketChannel;
        }
    }
}

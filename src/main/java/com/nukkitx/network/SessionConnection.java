package com.nukkitx.network;

import com.nukkitx.network.util.DisconnectReason;

import java.net.InetSocketAddress;

public interface SessionConnection<T> {

    void close();

    void close(DisconnectReason reason);

    void disconnect();

    void disconnect(DisconnectReason reason);

    InetSocketAddress getAddress();

    long getPing();

    boolean isClosed();

    void send(T packet);

    void sendImmediate(T packet);
}

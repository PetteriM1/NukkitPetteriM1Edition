package com.nukkitx.network;

import com.nukkitx.network.util.DisconnectReason;

import java.net.InetSocketAddress;

public interface SessionConnection<T> {

    InetSocketAddress getAddress();

    void close(DisconnectReason reason);

    void disconnect(DisconnectReason reason);

    boolean isClosed();

    long getPing();
}

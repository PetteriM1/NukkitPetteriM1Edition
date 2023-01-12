/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.rcon;

import java.nio.channels.SocketChannel;

public class RCONCommand {
    private final SocketChannel b;
    private final int a;
    private final String c;

    public RCONCommand(SocketChannel socketChannel, int n, String string) {
        this.b = socketChannel;
        this.a = n;
        this.c = string;
    }

    public SocketChannel getSender() {
        return this.b;
    }

    public int getId() {
        return this.a;
    }

    public String getCommand() {
        return this.c;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class ClientToServerHandshakePacket
extends DataPacket {
    public static final byte NETWORK_ID = 4;

    @Override
    public byte pid() {
        return 4;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.a();
    }

    public String toString() {
        return "ClientToServerHandshakePacket()";
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class SubClientLoginPacket
extends DataPacket {
    public static final byte NETWORK_ID = 94;

    @Override
    public byte pid() {
        return 94;
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
        return "SubClientLoginPacket()";
    }
}


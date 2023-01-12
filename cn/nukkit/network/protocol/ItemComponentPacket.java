/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class ItemComponentPacket
extends DataPacket {
    public static final byte NETWORK_ID = -94;

    @Override
    public byte pid() {
        return -94;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(0L);
    }

    public String toString() {
        return "ItemComponentPacket()";
    }
}


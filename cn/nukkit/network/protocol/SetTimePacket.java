/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class SetTimePacket
extends DataPacket {
    public static final byte NETWORK_ID = 10;
    public int time;

    @Override
    public byte pid() {
        return 10;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.time);
    }

    public String toString() {
        return "SetTimePacket(time=" + this.time + ")";
    }
}


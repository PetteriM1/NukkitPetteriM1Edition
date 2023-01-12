/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class StructureBlockUpdatePacket
extends DataPacket {
    public static final byte NETWORK_ID = 90;

    @Override
    public byte pid() {
        return 90;
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
        return "StructureBlockUpdatePacket()";
    }
}


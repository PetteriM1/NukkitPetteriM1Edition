/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class RequestChunkRadiusPacket
extends DataPacket {
    public static final byte NETWORK_ID = 69;
    public int radius;

    @Override
    public void decode() {
        this.radius = this.getVarInt();
    }

    @Override
    public void encode() {
        this.a();
    }

    @Override
    public byte pid() {
        return 69;
    }

    public String toString() {
        return "RequestChunkRadiusPacket(radius=" + this.radius + ")";
    }
}


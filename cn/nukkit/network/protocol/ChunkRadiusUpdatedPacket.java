/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class ChunkRadiusUpdatedPacket
extends DataPacket {
    public static final byte NETWORK_ID = 70;
    public int radius;

    @Override
    public void decode() {
        this.radius = this.getVarInt();
    }

    @Override
    public void encode() {
        super.reset();
        this.putVarInt(this.radius);
    }

    @Override
    public byte pid() {
        return 70;
    }

    public String toString() {
        return "ChunkRadiusUpdatedPacket(radius=" + this.radius + ")";
    }
}


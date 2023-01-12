/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import java.util.UUID;

public class ResourcePackChunkRequestPacket
extends DataPacket {
    public static final byte NETWORK_ID = 84;
    public UUID packId;
    public int chunkIndex;

    @Override
    public void decode() {
        this.packId = UUID.fromString(this.getString());
        this.chunkIndex = this.getLInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.packId.toString());
        this.putLInt(this.chunkIndex);
    }

    @Override
    public byte pid() {
        return 84;
    }

    public String toString() {
        return "ResourcePackChunkRequestPacket(packId=" + this.packId + ", chunkIndex=" + this.chunkIndex + ")";
    }
}


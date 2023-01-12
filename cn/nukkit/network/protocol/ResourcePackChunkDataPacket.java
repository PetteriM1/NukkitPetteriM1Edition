/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import java.util.UUID;

public class ResourcePackChunkDataPacket
extends DataPacket {
    public static final byte NETWORK_ID = 83;
    public UUID packId;
    public int chunkIndex;
    public long progress;
    public byte[] data;

    @Override
    public void decode() {
        this.packId = UUID.fromString(this.getString());
        this.chunkIndex = this.getLInt();
        this.progress = this.getLLong();
        this.data = this.protocol < 388 ? this.get(this.getLInt()) : this.getByteArray();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.packId.toString());
        this.putLInt(this.chunkIndex);
        this.putLLong(this.progress);
        if (this.protocol < 388) {
            this.putLInt(this.data.length);
            this.put(this.data);
        } else {
            this.putByteArray(this.data);
        }
    }

    @Override
    public byte pid() {
        return 83;
    }

    public String toString() {
        return "ResourcePackChunkDataPacket(packId=" + this.packId + ", chunkIndex=" + this.chunkIndex + ", progress=" + this.progress + ")";
    }
}


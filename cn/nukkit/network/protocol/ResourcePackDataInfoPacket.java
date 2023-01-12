/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import java.util.UUID;

public class ResourcePackDataInfoPacket
extends DataPacket {
    public static final byte NETWORK_ID = 82;
    public static final int TYPE_INVALID = 0;
    public static final int TYPE_ADDON = 1;
    public static final int TYPE_CACHED = 2;
    public static final int TYPE_COPY_PROTECTED = 3;
    public static final int TYPE_BEHAVIOR = 4;
    public static final int TYPE_PERSONA_PIECE = 5;
    public static final int TYPE_RESOURCE = 6;
    public static final int TYPE_SKINS = 7;
    public static final int TYPE_WORLD_TEMPLATE = 8;
    public static final int TYPE_COUNT = 9;
    public UUID packId;
    public int maxChunkSize;
    public int chunkCount;
    public long compressedPackSize;
    public byte[] sha256;
    public boolean premium;
    public int type;

    public ResourcePackDataInfoPacket() {
        this.type = this.protocol < 388 ? 1 : 6;
    }

    @Override
    public void decode() {
        this.packId = UUID.fromString(this.getString());
        this.maxChunkSize = this.getLInt();
        this.chunkCount = this.getLInt();
        this.compressedPackSize = this.getLLong();
        this.sha256 = this.getByteArray();
        if (this.protocol >= 361) {
            this.premium = this.getBoolean();
            this.type = this.getByte();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.packId.toString());
        this.putLInt(this.maxChunkSize);
        this.putLInt(this.chunkCount);
        this.putLLong(this.compressedPackSize);
        this.putByteArray(this.sha256);
        if (this.protocol >= 361) {
            this.putBoolean(this.premium);
            this.putByte((byte)this.type);
        }
    }

    @Override
    public byte pid() {
        return 82;
    }

    public String toString() {
        return "ResourcePackDataInfoPacket(packId=" + this.packId + ", maxChunkSize=" + this.maxChunkSize + ", chunkCount=" + this.chunkCount + ", compressedPackSize=" + this.compressedPackSize + ", premium=" + this.premium + ", type=" + this.type + ")";
    }
}


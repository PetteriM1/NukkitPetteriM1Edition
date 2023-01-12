/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import java.util.Arrays;

public class LevelChunkPacket
extends DataPacket {
    public static final byte NETWORK_ID = 58;
    public int chunkX;
    public int chunkZ;
    public int subChunkCount;
    public boolean cacheEnabled;
    public boolean requestSubChunks;
    public int subChunkLimit;
    public long[] blobIds;
    public byte[] data;

    @Override
    public byte pid() {
        return 58;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.chunkX);
        this.putVarInt(this.chunkZ);
        if (this.protocol >= 361) {
            if (this.protocol < 486 || !this.requestSubChunks) {
                this.putUnsignedVarInt(this.subChunkCount);
            } else if (this.subChunkLimit < 0) {
                this.putUnsignedVarInt(-1L);
            } else {
                this.putUnsignedVarInt(-2L);
                this.putUnsignedVarInt(this.subChunkLimit);
            }
            this.putBoolean(this.cacheEnabled);
            if (this.cacheEnabled) {
                this.putUnsignedVarInt(this.blobIds.length);
                for (long l : this.blobIds) {
                    this.putLLong(l);
                }
            }
        }
        this.putByteArray(this.data);
    }

    public String toString() {
        return "LevelChunkPacket(chunkX=" + this.chunkX + ", chunkZ=" + this.chunkZ + ", subChunkCount=" + this.subChunkCount + ", cacheEnabled=" + this.cacheEnabled + ", requestSubChunks=" + this.requestSubChunks + ", subChunkLimit=" + this.subChunkLimit + ", blobIds=" + Arrays.toString(this.blobIds) + ")";
    }
}


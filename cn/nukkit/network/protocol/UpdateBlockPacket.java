/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class UpdateBlockPacket
extends DataPacket {
    public static final byte NETWORK_ID = 21;
    public static final int FLAG_NONE = 0;
    public static final int FLAG_NEIGHBORS = 1;
    public static final int FLAG_NETWORK = 2;
    public static final int FLAG_NOGRAPHIC = 4;
    public static final int FLAG_PRIORITY = 8;
    public static final int FLAG_ALL = 3;
    public static final int FLAG_ALL_PRIORITY = 11;
    public int x;
    public int z;
    public int y;
    public int blockId;
    public int blockData;
    public int blockRuntimeId;
    public int flags;
    public int dataLayer = 0;

    @Override
    public byte pid() {
        return 21;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBlockVector3(this.x, this.y, this.z);
        if (this.protocol > 201) {
            this.putUnsignedVarInt(this.blockRuntimeId);
            this.putUnsignedVarInt(this.flags);
        } else {
            this.putUnsignedVarInt(this.blockId);
            this.putUnsignedVarInt(0xB0 | this.blockData & 0xF);
        }
        if (this.protocol > 224) {
            this.putUnsignedVarInt(this.dataLayer);
        }
    }

    public String toString() {
        return "UpdateBlockPacket(x=" + this.x + ", z=" + this.z + ", y=" + this.y + ", blockId=" + this.blockId + ", blockData=" + this.blockData + ", blockRuntimeId=" + this.blockRuntimeId + ", flags=" + this.flags + ", dataLayer=" + this.dataLayer + ")";
    }

    public static class Entry {
        public final int x;
        public final int z;
        public final int y;
        public final int blockId;
        public final int blockData;
        public final int flags;

        public Entry(int n, int n2, int n3, int n4, int n5, int n6) {
            this.x = n;
            this.z = n2;
            this.y = n3;
            this.blockId = n4;
            this.blockData = n5;
            this.flags = n6;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;

public class BlockEntityDataPacket
extends DataPacket {
    public static final byte NETWORK_ID = 56;
    public int x;
    public int y;
    public int z;
    public byte[] namedTag;

    @Override
    public byte pid() {
        return 56;
    }

    @Override
    public void decode() {
        BlockVector3 blockVector3 = this.getBlockVector3();
        this.x = blockVector3.x;
        this.y = blockVector3.y;
        this.z = blockVector3.z;
        this.namedTag = this.get();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBlockVector3(this.x, this.y, this.z);
        this.put(this.namedTag);
    }

    public String toString() {
        return "BlockEntityDataPacket(x=" + this.x + ", y=" + this.y + ", z=" + this.z + ")";
    }
}


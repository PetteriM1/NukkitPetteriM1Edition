/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;

public class ItemFrameDropItemPacket
extends DataPacket {
    public static final byte NETWORK_ID = 71;
    public int x;
    public int y;
    public int z;

    @Override
    public void decode() {
        BlockVector3 blockVector3 = this.getBlockVector3();
        this.z = blockVector3.z;
        this.y = blockVector3.y;
        this.x = blockVector3.x;
    }

    @Override
    public void encode() {
        this.a();
    }

    @Override
    public byte pid() {
        return 71;
    }

    public String toString() {
        return "ItemFrameDropItemPacket(x=" + this.x + ", y=" + this.y + ", z=" + this.z + ")";
    }
}


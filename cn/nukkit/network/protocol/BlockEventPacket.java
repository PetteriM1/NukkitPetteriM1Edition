/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class BlockEventPacket
extends DataPacket {
    public static final byte NETWORK_ID = 26;
    public int x;
    public int y;
    public int z;
    public int case1;
    public int case2;

    @Override
    public byte pid() {
        return 26;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBlockVector3(this.x, this.y, this.z);
        this.putVarInt(this.case1);
        this.putVarInt(this.case2);
    }

    public String toString() {
        return "BlockEventPacket(x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", case1=" + this.case1 + ", case2=" + this.case2 + ")";
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;

public class AnvilDamagePacket
extends DataPacket {
    public static final byte NETWORK_ID = -115;
    public int damage;
    public int x;
    public int y;
    public int z;

    @Override
    public byte pid() {
        return -115;
    }

    @Override
    public void decode() {
        this.damage = this.getByte();
        BlockVector3 blockVector3 = this.getBlockVector3();
        this.x = blockVector3.x;
        this.y = blockVector3.y;
        this.z = blockVector3.z;
    }

    @Override
    public void encode() {
        this.a();
    }

    public String toString() {
        return "AnvilDamagePacket(damage=" + this.damage + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ")";
    }
}


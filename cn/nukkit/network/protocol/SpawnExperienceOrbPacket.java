/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class SpawnExperienceOrbPacket
extends DataPacket {
    public static final byte NETWORK_ID = 66;
    public float x;
    public float y;
    public float z;
    public int amount;

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVector3f(this.x, this.y, this.z);
        this.putUnsignedVarInt(this.amount);
    }

    @Override
    public byte pid() {
        return 66;
    }

    public String toString() {
        return "SpawnExperienceOrbPacket(x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", amount=" + this.amount + ")";
    }
}


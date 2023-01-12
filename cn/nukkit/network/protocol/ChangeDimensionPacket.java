/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class ChangeDimensionPacket
extends DataPacket {
    public static final byte NETWORK_ID = 61;
    public int dimension;
    public float x;
    public float y;
    public float z;
    public boolean respawn;

    @Override
    public void decode() {
        this.dimension = this.getVarInt();
        this.x = this.getVector3f().x;
        this.y = this.getVector3f().y;
        this.z = this.getVector3f().z;
        this.respawn = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.dimension);
        this.putVector3f(this.x, this.y, this.z);
        this.putBoolean(this.respawn);
    }

    @Override
    public byte pid() {
        return 61;
    }

    public String toString() {
        return "ChangeDimensionPacket(dimension=" + this.dimension + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", respawn=" + this.respawn + ")";
    }
}


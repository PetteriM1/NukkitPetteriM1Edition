/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.DataPacket;

public class RespawnPacket
extends DataPacket {
    public static final byte NETWORK_ID = 45;
    public static final int STATE_SEARCHING_FOR_SPAWN = 0;
    public static final int STATE_READY_TO_SPAWN = 1;
    public static final int STATE_CLIENT_READY_TO_SPAWN = 2;
    public float x;
    public float y;
    public float z;
    public int respawnState = 0;
    public long runtimeEntityId;

    @Override
    public void decode() {
        Vector3f vector3f = this.getVector3f();
        this.x = vector3f.x;
        this.y = vector3f.y;
        this.z = vector3f.z;
        if (this.protocol >= 388) {
            this.respawnState = this.getByte();
            this.runtimeEntityId = this.getEntityRuntimeId();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putVector3f(this.x, this.y, this.z);
        if (this.protocol >= 388) {
            this.putByte((byte)this.respawnState);
            this.putEntityRuntimeId(this.runtimeEntityId);
        }
    }

    @Override
    public byte pid() {
        return 45;
    }

    public String toString() {
        return "RespawnPacket(x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", respawnState=" + this.respawnState + ", runtimeEntityId=" + this.runtimeEntityId + ")";
    }
}


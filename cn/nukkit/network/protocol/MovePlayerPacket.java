/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.DataPacket;

public class MovePlayerPacket
extends DataPacket {
    public static final byte NETWORK_ID = 19;
    public static final int MODE_NORMAL = 0;
    public static final int MODE_RESET = 1;
    public static final int MODE_TELEPORT = 2;
    public static final int MODE_PITCH = 3;
    public long eid;
    public float x;
    public float y;
    public float z;
    public float yaw;
    public float headYaw;
    public float pitch;
    public int mode = 0;
    public boolean onGround;
    public long ridingEid;
    public int teleportCause = 0;
    public int teleportItem = 0;
    public long frame;

    @Override
    public void decode() {
        this.eid = this.getEntityRuntimeId();
        Vector3f vector3f = this.getVector3f();
        this.x = vector3f.x;
        this.y = vector3f.y;
        this.z = vector3f.z;
        this.pitch = this.getLFloat();
        this.yaw = this.getLFloat();
        this.headYaw = this.getLFloat();
        this.mode = this.getByte();
        this.onGround = this.getBoolean();
        this.ridingEid = this.getEntityRuntimeId();
        if (this.mode == 2) {
            this.teleportCause = this.getLInt();
            this.teleportItem = this.getLInt();
        }
        if (this.protocol >= 419) {
            this.frame = this.getUnsignedVarLong();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putVector3f(this.x, this.y, this.z);
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw);
        this.putLFloat(this.headYaw);
        this.putByte((byte)this.mode);
        this.putBoolean(this.onGround);
        this.putEntityRuntimeId(this.ridingEid);
        if (this.mode == 2) {
            this.putLInt(this.teleportCause);
            this.putLInt(this.teleportItem);
        }
        if (this.protocol >= 419) {
            this.putUnsignedVarLong(this.frame);
        }
    }

    @Override
    public byte pid() {
        return 19;
    }

    public String toString() {
        return "MovePlayerPacket(eid=" + this.eid + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", yaw=" + this.yaw + ", headYaw=" + this.headYaw + ", pitch=" + this.pitch + ", mode=" + this.mode + ", onGround=" + this.onGround + ", ridingEid=" + this.ridingEid + ", teleportCause=" + this.teleportCause + ", teleportItem=" + this.teleportItem + ", frame=" + this.frame + ")";
    }
}


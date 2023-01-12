/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.DataPacket;

public class MoveEntityAbsolutePacket
extends DataPacket {
    public static final byte NETWORK_ID = 18;
    public long eid;
    public double x;
    public double y;
    public double z;
    public double yaw;
    public double headYaw;
    public double pitch;
    public boolean onGround;
    public boolean teleport;
    public boolean forceMoveLocalEntity;

    @Override
    public byte pid() {
        return 18;
    }

    @Override
    public void decode() {
        this.eid = this.getEntityRuntimeId();
        if (this.protocol >= 274) {
            int n = this.getByte();
            this.onGround = (n & 1) != 0;
            this.teleport = (n & 2) != 0;
            this.forceMoveLocalEntity = (n & 4) != 0;
        }
        Vector3f vector3f = this.getVector3f();
        this.x = vector3f.x;
        this.y = vector3f.y;
        this.z = vector3f.z;
        this.pitch = (double)this.getByte() * 1.40625;
        this.headYaw = (double)this.getByte() * 1.40625;
        this.yaw = (double)this.getByte() * 1.40625;
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        if (this.protocol >= 274) {
            byte by = 0;
            if (this.onGround) {
                by = (byte)(by | 1);
            }
            if (this.teleport) {
                by = (byte)(by | 2);
            }
            if (this.forceMoveLocalEntity) {
                by = (byte)(by | 4);
            }
            this.putByte(by);
        }
        this.putVector3f((float)this.x, (float)this.y, (float)this.z);
        this.putByte((byte)(this.pitch / 1.40625));
        this.putByte((byte)(this.headYaw / 1.40625));
        this.putByte((byte)(this.yaw / 1.40625));
        if (this.protocol <= 261) {
            this.putBoolean(this.onGround);
            this.putBoolean(this.teleport);
        }
    }

    public String toString() {
        return "MoveEntityAbsolutePacket(eid=" + this.eid + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", yaw=" + this.yaw + ", headYaw=" + this.headYaw + ", pitch=" + this.pitch + ", onGround=" + this.onGround + ", teleport=" + this.teleport + ", forceMoveLocalEntity=" + this.forceMoveLocalEntity + ")";
    }
}


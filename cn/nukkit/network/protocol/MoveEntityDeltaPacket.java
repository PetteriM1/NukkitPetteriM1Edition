/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class MoveEntityDeltaPacket
extends DataPacket {
    public static final byte NETWORK_ID = 111;
    public static final int FLAG_HAS_X = 1;
    public static final int FLAG_HAS_Y = 2;
    public static final int FLAG_HAS_Z = 4;
    public static final int FLAG_HAS_YAW = 8;
    public static final int FLAG_HAS_HEAD_YAW = 16;
    public static final int FLAG_HAS_PITCH = 32;
    public long eid;
    public int flags = 0;
    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;
    public double yawDelta = 0.0;
    public double headYawDelta = 0.0;
    public double pitchDelta = 0.0;

    @Override
    public byte pid() {
        return 111;
    }

    @Override
    public void decode() {
        this.getEntityRuntimeId();
        this.flags = this.getByte();
        this.x = this.f(1);
        this.y = this.f(2);
        this.z = this.f(4);
        this.yawDelta = this.g(8);
        this.headYawDelta = this.g(16);
        this.pitchDelta = this.g(32);
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putByte((byte)this.flags);
        this.a(1, this.x);
        this.a(2, this.y);
        this.a(4, this.z);
        this.a(8, this.yawDelta);
        this.a(16, this.headYawDelta);
        this.a(32, this.pitchDelta);
    }

    private float f(int n) {
        if ((this.flags & n) != 0) {
            if (this.protocol < 419) {
                return this.getVarInt();
            }
            return this.getLFloat();
        }
        return 0.0f;
    }

    private double g(int n) {
        if ((this.flags & n) != 0) {
            return (double)this.getByte() * 1.40625;
        }
        return 0.0;
    }

    private void a(int n, float f2) {
        if ((this.flags & n) != 0) {
            if (this.protocol < 419) {
                this.putVarInt((int)f2);
            } else {
                this.putLFloat(f2);
            }
        }
    }

    private void a(int n, double d2) {
        if ((this.flags & n) != 0) {
            this.putByte((byte)(d2 / 1.40625));
        }
    }

    public String toString() {
        return "MoveEntityDeltaPacket(eid=" + this.eid + ", flags=" + this.flags + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", yawDelta=" + this.yawDelta + ", headYawDelta=" + this.headYawDelta + ", pitchDelta=" + this.pitchDelta + ")";
    }
}


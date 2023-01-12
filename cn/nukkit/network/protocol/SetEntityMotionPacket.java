/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class SetEntityMotionPacket
extends DataPacket {
    public static final byte NETWORK_ID = 40;
    public long eid;
    public float motionX;
    public float motionY;
    public float motionZ;

    @Override
    public byte pid() {
        return 40;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putVector3f(this.motionX, this.motionY, this.motionZ);
    }

    public String toString() {
        return "SetEntityMotionPacket(eid=" + this.eid + ", motionX=" + this.motionX + ", motionY=" + this.motionY + ", motionZ=" + this.motionZ + ")";
    }
}


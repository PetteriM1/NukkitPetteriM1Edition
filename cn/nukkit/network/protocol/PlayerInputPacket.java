/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class PlayerInputPacket
extends DataPacket {
    public static final byte NETWORK_ID = 57;
    public float motionX;
    public float motionY;
    public boolean jumping;
    public boolean sneaking;

    @Override
    public void decode() {
        this.motionX = this.getLFloat();
        this.motionY = this.getLFloat();
        this.jumping = this.getBoolean();
        this.sneaking = this.getBoolean();
    }

    @Override
    public void encode() {
        this.a();
    }

    @Override
    public byte pid() {
        return 57;
    }

    public String toString() {
        return "PlayerInputPacket(motionX=" + this.motionX + ", motionY=" + this.motionY + ", jumping=" + this.jumping + ", sneaking=" + this.sneaking + ")";
    }
}


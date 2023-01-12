/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class RiderJumpPacket
extends DataPacket {
    public static final byte NETWORK_ID = 20;
    public int jumpStrength;

    @Override
    public byte pid() {
        return 20;
    }

    @Override
    public void decode() {
        this.jumpStrength = this.getVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.jumpStrength);
    }

    public String toString() {
        return "RiderJumpPacket(jumpStrength=" + this.jumpStrength + ")";
    }
}


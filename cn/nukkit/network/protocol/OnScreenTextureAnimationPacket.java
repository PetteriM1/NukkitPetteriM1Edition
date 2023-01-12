/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class OnScreenTextureAnimationPacket
extends DataPacket {
    public static final byte NETWORK_ID = -126;
    public int effectId;

    @Override
    public byte pid() {
        return -126;
    }

    @Override
    public void decode() {
        this.effectId = this.getLInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putLInt(this.effectId);
    }

    public String toString() {
        return "OnScreenTextureAnimationPacket(effectId=" + this.effectId + ")";
    }
}


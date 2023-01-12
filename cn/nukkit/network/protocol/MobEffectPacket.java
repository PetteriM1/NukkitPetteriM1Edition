/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class MobEffectPacket
extends DataPacket {
    public static final byte NETWORK_ID = 28;
    public static final byte EVENT_ADD = 1;
    public static final byte EVENT_MODIFY = 2;
    public static final byte EVENT_REMOVE = 3;
    public long eid;
    public int eventId;
    public int effectId;
    public int amplifier = 0;
    public boolean particles = true;
    public int duration = 0;

    @Override
    public byte pid() {
        return 28;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putByte((byte)this.eventId);
        this.putVarInt(this.effectId);
        this.putVarInt(this.amplifier);
        this.putBoolean(this.particles);
        this.putVarInt(this.duration);
    }

    public String toString() {
        return "MobEffectPacket(eid=" + this.eid + ", eventId=" + this.eventId + ", effectId=" + this.effectId + ", amplifier=" + this.amplifier + ", particles=" + this.particles + ", duration=" + this.duration + ")";
    }
}


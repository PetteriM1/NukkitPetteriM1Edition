/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class HurtArmorPacket
extends DataPacket {
    public static final byte NETWORK_ID = 38;
    public int cause;
    public int damage;
    public long armorSlots;

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        if (this.protocol >= 407) {
            this.putVarInt(this.cause);
        }
        this.putVarInt(this.damage);
        if (this.protocol >= 465) {
            this.putUnsignedVarLong(this.armorSlots);
        }
    }

    @Override
    public byte pid() {
        return 38;
    }

    public String toString() {
        return "HurtArmorPacket(cause=" + this.cause + ", damage=" + this.damage + ", armorSlots=" + this.armorSlots + ")";
    }
}


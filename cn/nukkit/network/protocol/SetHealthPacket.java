/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class SetHealthPacket
extends DataPacket {
    public static final byte NETWORK_ID = 42;
    public int health;

    @Override
    public byte pid() {
        return 42;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.health);
    }

    public String toString() {
        return "SetHealthPacket(health=" + this.health + ")";
    }
}


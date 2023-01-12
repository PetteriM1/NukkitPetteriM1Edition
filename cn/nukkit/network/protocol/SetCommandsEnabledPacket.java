/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class SetCommandsEnabledPacket
extends DataPacket {
    public static final byte NETWORK_ID = 59;
    public boolean enabled;

    @Override
    public byte pid() {
        return 59;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.enabled);
    }

    public String toString() {
        return "SetCommandsEnabledPacket(enabled=" + this.enabled + ")";
    }
}


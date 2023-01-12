/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class StopSoundPacket
extends DataPacket {
    public static final byte NETWORK_ID = 87;
    public String name;
    public boolean stopAll;

    @Override
    public byte pid() {
        return 87;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.name);
        this.putBoolean(this.stopAll);
    }

    public String toString() {
        return "StopSoundPacket(name=" + this.name + ", stopAll=" + this.stopAll + ")";
    }
}


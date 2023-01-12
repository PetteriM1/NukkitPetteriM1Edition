/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class ClientCacheStatusPacket
extends DataPacket {
    public static final byte NETWORK_ID = -127;
    public boolean supported;

    @Override
    public byte pid() {
        return -127;
    }

    @Override
    public void decode() {
        this.supported = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.supported);
    }

    public String toString() {
        return "ClientCacheStatusPacket(supported=" + this.supported + ")";
    }
}


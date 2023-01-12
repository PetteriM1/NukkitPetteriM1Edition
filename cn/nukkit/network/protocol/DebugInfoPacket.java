/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class DebugInfoPacket
extends DataPacket {
    public static final byte NETWORK_ID = -101;
    public long entityId;
    public String data;

    @Override
    public byte pid() {
        return -101;
    }

    @Override
    public void decode() {
        this.entityId = this.getLong();
        this.data = this.getString();
    }

    @Override
    public void encode() {
        this.reset();
        this.putLong(this.entityId);
        this.putString(this.data);
    }
}


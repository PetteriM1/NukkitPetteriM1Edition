/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class MapCreateLockedCopyPacket
extends DataPacket {
    public static final byte NETWORK_ID = -125;
    public long originalMapId;
    public long newMapId;

    @Override
    public byte pid() {
        return -125;
    }

    @Override
    public void decode() {
        this.originalMapId = this.getVarLong();
        this.newMapId = this.getVarLong();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.originalMapId);
        this.putVarLong(this.newMapId);
    }

    public String toString() {
        return "MapCreateLockedCopyPacket(originalMapId=" + this.originalMapId + ", newMapId=" + this.newMapId + ")";
    }
}


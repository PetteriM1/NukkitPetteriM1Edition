/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class MapInfoRequestPacket
extends DataPacket {
    public static final byte NETWORK_ID = 68;
    public long mapId;

    @Override
    public byte pid() {
        return 68;
    }

    @Override
    public void decode() {
        this.mapId = this.getEntityUniqueId();
    }

    @Override
    public void encode() {
        this.a();
    }

    public String toString() {
        return "MapInfoRequestPacket(mapId=" + this.mapId + ")";
    }
}


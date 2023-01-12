/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class UpdateEquipmentPacket
extends DataPacket {
    public static final byte NETWORK_ID = 81;
    public int windowId;
    public int windowType;
    public int unknown;
    public long eid;
    public byte[] namedtag;

    @Override
    public byte pid() {
        return 81;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte)this.windowId);
        this.putByte((byte)this.windowType);
        this.putEntityUniqueId(this.eid);
        this.put(this.namedtag);
    }

    public String toString() {
        return "UpdateEquipmentPacket(windowId=" + this.windowId + ", windowType=" + this.windowType + ", unknown=" + this.unknown + ", eid=" + this.eid + ")";
    }
}


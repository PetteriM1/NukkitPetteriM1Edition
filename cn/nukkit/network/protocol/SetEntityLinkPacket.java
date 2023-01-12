/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class SetEntityLinkPacket
extends DataPacket {
    public static final byte NETWORK_ID = 41;
    public static final byte TYPE_REMOVE = 0;
    public static final byte TYPE_RIDE = 1;
    public static final byte TYPE_PASSENGER = 2;
    public long vehicleUniqueId;
    public long riderUniqueId;
    public byte type;
    public byte immediate;
    public boolean riderInitiated = false;

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.vehicleUniqueId);
        this.putEntityUniqueId(this.riderUniqueId);
        this.putByte(this.type);
        this.putByte(this.immediate);
        if (this.protocol >= 407) {
            this.putBoolean(this.riderInitiated);
        }
    }

    @Override
    public byte pid() {
        return 41;
    }

    public String toString() {
        return "SetEntityLinkPacket(vehicleUniqueId=" + this.vehicleUniqueId + ", riderUniqueId=" + this.riderUniqueId + ", type=" + this.type + ", immediate=" + this.immediate + ", riderInitiated=" + this.riderInitiated + ")";
    }
}


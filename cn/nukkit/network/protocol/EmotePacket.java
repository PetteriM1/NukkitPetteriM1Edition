/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class EmotePacket
extends DataPacket {
    public static final byte NETWORK_ID = -118;
    public long runtimeId;
    public String emoteID;
    public byte flags;

    @Override
    public byte pid() {
        return -118;
    }

    @Override
    public void decode() {
        this.runtimeId = this.getEntityRuntimeId();
        this.emoteID = this.getString();
        this.flags = (byte)this.getByte();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.runtimeId);
        this.putString(this.emoteID);
        this.putByte(this.flags);
    }

    public String toString() {
        return "EmotePacket(runtimeId=" + this.runtimeId + ", emoteID=" + this.emoteID + ", flags=" + this.flags + ")";
    }
}


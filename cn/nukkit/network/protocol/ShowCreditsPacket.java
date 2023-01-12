/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class ShowCreditsPacket
extends DataPacket {
    public static final byte NETWORK_ID = 75;
    public static final int STATUS_START_CREDITS = 0;
    public static final int STATUS_END_CREDITS = 1;
    public long eid;
    public int status;

    @Override
    public byte pid() {
        return 75;
    }

    @Override
    public void decode() {
        this.eid = this.getEntityRuntimeId();
        this.status = this.getVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putVarInt(this.status);
    }

    public String toString() {
        return "ShowCreditsPacket(eid=" + this.eid + ", status=" + this.status + ")";
    }
}


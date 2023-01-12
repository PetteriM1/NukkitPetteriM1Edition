/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class TakeItemEntityPacket
extends DataPacket {
    public static final byte NETWORK_ID = 17;
    public long entityId;
    public long target;

    @Override
    public void decode() {
        this.target = this.getEntityRuntimeId();
        this.entityId = this.getEntityRuntimeId();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.target);
        this.putEntityRuntimeId(this.entityId);
    }

    @Override
    public byte pid() {
        return 17;
    }

    public String toString() {
        return "TakeItemEntityPacket(entityId=" + this.entityId + ", target=" + this.target + ")";
    }
}


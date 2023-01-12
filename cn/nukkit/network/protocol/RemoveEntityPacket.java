/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class RemoveEntityPacket
extends DataPacket {
    public static final byte NETWORK_ID = 14;
    public long eid;

    @Override
    public byte pid() {
        return 14;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.eid);
    }

    public String toString() {
        return "RemoveEntityPacket(eid=" + this.eid + ")";
    }
}


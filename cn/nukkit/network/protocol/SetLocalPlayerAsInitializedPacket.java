/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class SetLocalPlayerAsInitializedPacket
extends DataPacket {
    public static final byte NETWORK_ID = 113;
    public long eid;

    @Override
    public byte pid() {
        return 113;
    }

    @Override
    public void decode() {
        this.eid = this.getUnsignedVarLong();
    }

    @Override
    public void encode() {
        this.putUnsignedVarLong(this.eid);
    }

    public String toString() {
        return "SetLocalPlayerAsInitializedPacket(eid=" + this.eid + ")";
    }
}


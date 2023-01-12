/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class EntityFallPacket
extends DataPacket {
    public static final byte NETWORK_ID = 37;
    public long eid;
    public float fallDistance;
    public boolean isInVoid;

    @Override
    public void decode() {
        this.eid = this.getEntityRuntimeId();
        this.fallDistance = this.getLFloat();
        this.isInVoid = this.getBoolean();
    }

    @Override
    public void encode() {
        this.a();
    }

    @Override
    public byte pid() {
        return 37;
    }

    public String toString() {
        return "EntityFallPacket(eid=" + this.eid + ", fallDistance=" + this.fallDistance + ", isInVoid=" + this.isInVoid + ")";
    }
}


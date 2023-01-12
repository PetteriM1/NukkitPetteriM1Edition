/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;

public class SetEntityDataPacket
extends DataPacket {
    public static final byte NETWORK_ID = 39;
    public long eid;
    public EntityMetadata metadata;
    public long frame;

    @Override
    public byte pid() {
        return 39;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.put(Binary.writeMetadata(this.protocol, this.metadata));
        if (this.protocol >= 419) {
            if (this.protocol >= 557) {
                this.putUnsignedVarInt(0L);
                this.putUnsignedVarInt(0L);
            }
            this.putUnsignedVarLong(this.frame);
        }
    }

    public String toString() {
        return "SetEntityDataPacket(eid=" + this.eid + ", metadata=" + this.metadata + ", frame=" + this.frame + ")";
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class AddPaintingPacket
extends DataPacket {
    public static final byte NETWORK_ID = 22;
    public long entityUniqueId;
    public long entityRuntimeId;
    public float x;
    public float y;
    public float z;
    public int direction;
    public String title;

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        if (this.protocol < 361) {
            this.putBlockVector3((int)this.x, (int)this.y, (int)this.z);
        } else {
            this.putVector3f(this.x, this.y, this.z);
        }
        this.putVarInt(this.direction);
        this.putString(this.title);
    }

    @Override
    public byte pid() {
        return 22;
    }

    public String toString() {
        return "AddPaintingPacket(entityUniqueId=" + this.entityUniqueId + ", entityRuntimeId=" + this.entityRuntimeId + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", direction=" + this.direction + ", title=" + this.title + ")";
    }
}


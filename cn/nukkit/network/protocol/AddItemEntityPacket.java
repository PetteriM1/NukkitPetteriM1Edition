/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;

public class AddItemEntityPacket
extends DataPacket {
    public static final byte NETWORK_ID = 15;
    public long entityUniqueId;
    public long entityRuntimeId;
    public Item item;
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;
    public EntityMetadata metadata = new EntityMetadata();
    public boolean isFromFishing = false;

    @Override
    public byte pid() {
        return 15;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putSlot(this.protocol, this.item);
        this.putVector3f(this.x, this.y, this.z);
        this.putVector3f(this.speedX, this.speedY, this.speedZ);
        this.put(Binary.writeMetadata(this.protocol, this.metadata));
        if (this.protocol >= 223) {
            this.putBoolean(this.isFromFishing);
        }
    }

    public String toString() {
        return "AddItemEntityPacket(entityUniqueId=" + this.entityUniqueId + ", entityRuntimeId=" + this.entityRuntimeId + ", item=" + this.item + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", speedX=" + this.speedX + ", speedY=" + this.speedY + ", speedZ=" + this.speedZ + ", metadata=" + this.metadata + ", isFromFishing=" + this.isFromFishing + ")";
    }
}


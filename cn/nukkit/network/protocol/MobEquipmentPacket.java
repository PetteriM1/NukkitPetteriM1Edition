/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;

public class MobEquipmentPacket
extends DataPacket {
    public static final byte NETWORK_ID = 31;
    public long eid;
    public Item item;
    public int inventorySlot;
    public int hotbarSlot;
    public int windowId;

    @Override
    public byte pid() {
        return 31;
    }

    @Override
    public void decode() {
        this.eid = this.getEntityRuntimeId();
        this.item = this.getSlot(this.protocol);
        this.inventorySlot = this.getByte();
        this.hotbarSlot = this.getByte();
        this.windowId = this.getByte();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putSlot(this.protocol, this.item);
        this.putByte((byte)this.inventorySlot);
        this.putByte((byte)this.hotbarSlot);
        this.putByte((byte)this.windowId);
    }

    public String toString() {
        return "MobEquipmentPacket(eid=" + this.eid + ", item=" + this.item + ", inventorySlot=" + this.inventorySlot + ", hotbarSlot=" + this.hotbarSlot + ", windowId=" + this.windowId + ")";
    }
}


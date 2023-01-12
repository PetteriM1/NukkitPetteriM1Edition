/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;

public class InventorySlotPacket
extends DataPacket {
    public static final byte NETWORK_ID = 50;
    public int inventoryId;
    public int networkId;
    public int slot;
    public Item item;

    @Override
    public byte pid() {
        return 50;
    }

    @Override
    public void decode() {
        this.inventoryId = (int)this.getUnsignedVarInt();
        this.slot = (int)this.getUnsignedVarInt();
        this.item = this.getSlot(this.protocol);
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.inventoryId);
        this.putUnsignedVarInt(this.slot);
        if (this.protocol >= 407 && this.protocol < 431) {
            this.putVarInt(this.networkId);
        }
        this.putSlot(this.protocol, this.item);
    }

    public String toString() {
        return "InventorySlotPacket(inventoryId=" + this.inventoryId + ", networkId=" + this.networkId + ", slot=" + this.slot + ", item=" + this.item + ")";
    }
}


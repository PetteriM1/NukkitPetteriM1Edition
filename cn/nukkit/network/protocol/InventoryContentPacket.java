/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import java.util.Arrays;

public class InventoryContentPacket
extends DataPacket {
    public static final byte NETWORK_ID = 49;
    public static final int SPECIAL_INVENTORY = 0;
    public static final int SPECIAL_OFFHAND = 119;
    public static final int SPECIAL_ARMOR = 120;
    public static final int SPECIAL_CREATIVE = 121;
    public static final int SPECIAL_HOTBAR = 122;
    public static final int SPECIAL_FIXED_INVENTORY = 123;
    public int inventoryId;
    public int networkId;
    public Item[] slots = new Item[0];

    @Override
    public byte pid() {
        return 49;
    }

    @Override
    public DataPacket clean() {
        this.slots = new Item[0];
        return super.clean();
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.inventoryId);
        this.putUnsignedVarInt(this.slots.length);
        for (Item item : this.slots) {
            if (this.protocol >= 407 && this.protocol < 431) {
                this.putVarInt(this.networkId);
            }
            this.putSlot(this.protocol, item);
        }
    }

    @Override
    public InventoryContentPacket clone() {
        InventoryContentPacket inventoryContentPacket = (InventoryContentPacket)super.clone();
        inventoryContentPacket.slots = (Item[])this.slots.clone();
        return inventoryContentPacket;
    }

    public String toString() {
        return "InventoryContentPacket(inventoryId=" + this.inventoryId + ", networkId=" + this.networkId + ", slots=" + Arrays.deepToString(this.slots) + ")";
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import java.util.Arrays;
import java.util.UUID;

public class CraftingEventPacket
extends DataPacket {
    public static final byte NETWORK_ID = 53;
    public static final int TYPE_SHAPELESS = 0;
    public static final int TYPE_SHAPED = 1;
    public static final int TYPE_FURNACE = 2;
    public static final int TYPE_FURNACE_DATA = 3;
    public static final int TYPE_MULTI = 4;
    public static final int TYPE_SHULKER_BOX = 5;
    public int windowId;
    public int type;
    public UUID id;
    public Item[] input;
    public Item[] output;

    @Override
    public void decode() {
        int n;
        this.windowId = this.getByte();
        this.type = (int)this.getUnsignedVarInt();
        this.id = this.getUUID();
        int n2 = (int)this.getUnsignedVarInt();
        this.input = new Item[Math.min(n2, 128)];
        for (n = 0; n < this.input.length; ++n) {
            this.input[n] = this.getSlot(this.protocol);
        }
        n = (int)this.getUnsignedVarInt();
        this.output = new Item[Math.min(n, 128)];
        for (int k = 0; k < this.output.length; ++k) {
            this.output[k] = this.getSlot(this.protocol);
        }
    }

    @Override
    public void encode() {
        this.a();
    }

    @Override
    public byte pid() {
        return 53;
    }

    public String toString() {
        return "CraftingEventPacket(windowId=" + this.windowId + ", type=" + this.type + ", id=" + this.id + ", input=" + Arrays.deepToString(this.input) + ", output=" + Arrays.deepToString(this.output) + ")";
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import java.util.Arrays;

public class MobArmorEquipmentPacket
extends DataPacket {
    public static final byte NETWORK_ID = 32;
    public long eid;
    public Item[] slots = new Item[4];

    @Override
    public byte pid() {
        return 32;
    }

    @Override
    public void decode() {
        this.eid = this.getEntityRuntimeId();
        this.slots = new Item[4];
        this.slots[0] = this.getSlot(this.protocol);
        this.slots[1] = this.getSlot(this.protocol);
        this.slots[2] = this.getSlot(this.protocol);
        this.slots[3] = this.getSlot(this.protocol);
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putSlot(this.protocol, this.slots[0]);
        this.putSlot(this.protocol, this.slots[1]);
        this.putSlot(this.protocol, this.slots[2]);
        this.putSlot(this.protocol, this.slots[3]);
    }

    public String toString() {
        return "MobArmorEquipmentPacket(eid=" + this.eid + ", slots=" + Arrays.deepToString(this.slots) + ")";
    }
}


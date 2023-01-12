/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import java.util.Arrays;

public class CreativeContentPacket
extends DataPacket {
    public static final byte NETWORK_ID = -111;
    public Item[] entries;

    @Override
    public byte pid() {
        return -111;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.entries.length);
        int n = 1;
        for (Item item : this.entries) {
            this.putUnsignedVarInt(n++);
            this.putSlot(this.protocol, item, this.protocol >= 431);
        }
    }

    public String toString() {
        return "CreativeContentPacket(entries=" + Arrays.deepToString(this.entries) + ")";
    }
}


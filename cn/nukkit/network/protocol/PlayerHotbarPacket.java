/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class PlayerHotbarPacket
extends DataPacket {
    public static final byte NETWORK_ID = 48;
    public int selectedHotbarSlot;
    public int windowId = 0;
    public boolean selectHotbarSlot = true;

    @Override
    public byte pid() {
        return 48;
    }

    @Override
    public void decode() {
        this.selectedHotbarSlot = (int)this.getUnsignedVarInt();
        this.windowId = this.getByte();
        this.selectHotbarSlot = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.selectedHotbarSlot);
        this.putByte((byte)this.windowId);
        if (this.protocol == 201) {
            this.putUnsignedVarInt(0L);
        }
        this.putBoolean(this.selectHotbarSlot);
    }

    public String toString() {
        return "PlayerHotbarPacket(selectedHotbarSlot=" + this.selectedHotbarSlot + ", windowId=" + this.windowId + ", selectHotbarSlot=" + this.selectHotbarSlot + ")";
    }
}


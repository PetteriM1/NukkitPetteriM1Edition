/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class GUIDataPickItemPacket
extends DataPacket {
    public static final byte NETWORK_ID = 54;
    public int hotbarSlot;

    @Override
    public byte pid() {
        return 54;
    }

    @Override
    public void encode() {
        this.reset();
        this.putLInt(this.hotbarSlot);
    }

    @Override
    public void decode() {
        this.hotbarSlot = this.getLInt();
    }

    public String toString() {
        return "GUIDataPickItemPacket(hotbarSlot=" + this.hotbarSlot + ")";
    }
}


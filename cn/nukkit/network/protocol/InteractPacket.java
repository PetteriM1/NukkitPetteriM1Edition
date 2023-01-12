/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class InteractPacket
extends DataPacket {
    public static final byte NETWORK_ID = 33;
    public static final int ACTION_VEHICLE_EXIT = 3;
    public static final int ACTION_MOUSEOVER = 4;
    public static final int ACTION_OPEN_NPC = 5;
    public static final int ACTION_OPEN_INVENTORY = 6;
    public int action;
    public long target;

    @Override
    public void decode() {
        this.action = this.getByte();
        this.target = this.getEntityRuntimeId();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte)this.action);
        this.putEntityRuntimeId(this.target);
    }

    @Override
    public byte pid() {
        return 33;
    }

    public String toString() {
        return "InteractPacket(action=" + this.action + ", target=" + this.target + ")";
    }
}


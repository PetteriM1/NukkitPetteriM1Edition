/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class ContainerClosePacket
extends DataPacket {
    public static final byte NETWORK_ID = 47;
    public int windowId;
    public boolean wasServerInitiated = true;

    @Override
    public byte pid() {
        return 47;
    }

    @Override
    public void decode() {
        this.windowId = (byte)this.getByte();
        if (this.protocol >= 419) {
            this.wasServerInitiated = this.getBoolean();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte)this.windowId);
        if (this.protocol >= 419) {
            this.putBoolean(this.wasServerInitiated);
        }
    }

    public String toString() {
        return "ContainerClosePacket(windowId=" + this.windowId + ", wasServerInitiated=" + this.wasServerInitiated + ")";
    }
}


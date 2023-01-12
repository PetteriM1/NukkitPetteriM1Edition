/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class CameraPacket
extends DataPacket {
    public static final byte NETWORK_ID = 73;
    public long cameraUniqueId;
    public long playerUniqueId;

    @Override
    public byte pid() {
        return 73;
    }

    @Override
    public void decode() {
        this.cameraUniqueId = this.getVarLong();
        this.playerUniqueId = this.getVarLong();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.cameraUniqueId);
        this.putEntityUniqueId(this.playerUniqueId);
    }

    public String toString() {
        return "CameraPacket(cameraUniqueId=" + this.cameraUniqueId + ", playerUniqueId=" + this.playerUniqueId + ")";
    }
}


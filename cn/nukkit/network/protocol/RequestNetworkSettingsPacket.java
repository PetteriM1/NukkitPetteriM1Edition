/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class RequestNetworkSettingsPacket
extends DataPacket {
    public int protocolVersion;

    @Override
    public byte pid() {
        return -63;
    }

    @Override
    public void encode() {
        this.a();
    }

    @Override
    public void decode() {
        this.protocolVersion = this.getInt();
    }

    public String toString() {
        return "RequestNetworkSettingsPacket(protocolVersion=" + this.protocolVersion + ")";
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class ServerSettingsRequestPacket
extends DataPacket {
    public static final byte NETWORK_ID = 102;

    @Override
    public byte pid() {
        return 102;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.a();
    }

    public String toString() {
        return "ServerSettingsRequestPacket()";
    }
}


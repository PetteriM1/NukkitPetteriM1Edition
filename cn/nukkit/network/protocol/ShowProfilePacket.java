/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class ShowProfilePacket
extends DataPacket {
    public static final byte NETWORK_ID = 104;
    public String xuid;

    @Override
    public byte pid() {
        return 104;
    }

    @Override
    public void decode() {
        this.xuid = this.getString();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.xuid);
    }

    public String toString() {
        return "ShowProfilePacket(xuid=" + this.xuid + ")";
    }
}


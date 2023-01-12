/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class SetPlayerGameTypePacket
extends DataPacket {
    public static final byte NETWORK_ID = 62;
    public int gamemode;

    @Override
    public byte pid() {
        return 62;
    }

    @Override
    public void decode() {
        this.gamemode = this.getVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.gamemode);
    }

    public String toString() {
        return "SetPlayerGameTypePacket(gamemode=" + this.gamemode + ")";
    }
}


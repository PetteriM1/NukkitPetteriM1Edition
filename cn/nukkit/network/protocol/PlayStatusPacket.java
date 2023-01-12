/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class PlayStatusPacket
extends DataPacket {
    public static final byte NETWORK_ID = 2;
    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAILED_CLIENT = 1;
    public static final int LOGIN_FAILED_SERVER = 2;
    public static final int PLAYER_SPAWN = 3;
    public static final int LOGIN_FAILED_INVALID_TENANT = 4;
    public static final int LOGIN_FAILED_VANILLA_EDU = 5;
    public static final int LOGIN_FAILED_EDU_VANILLA = 6;
    public static final int LOGIN_FAILED_SERVER_FULL = 7;
    public static final int LOGIN_FAILED_EDITOR_TO_VANILLA_MISMATCH = 8;
    public static final int LOGIN_FAILED_VANILLA_TO_EDITOR_MISMATCH = 9;
    public int status;

    @Override
    public byte pid() {
        return 2;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putInt(this.status);
    }

    public String toString() {
        return "PlayStatusPacket(status=" + this.status + ")";
    }
}


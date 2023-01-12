/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class DisconnectPacket
extends DataPacket {
    public static final byte NETWORK_ID = 5;
    public boolean hideDisconnectionScreen = false;
    public String message;

    @Override
    public byte pid() {
        return 5;
    }

    @Override
    public void decode() {
        this.hideDisconnectionScreen = this.getBoolean();
        this.message = this.getString();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.hideDisconnectionScreen);
        if (!this.hideDisconnectionScreen) {
            this.putString(this.message);
        }
    }

    public String toString() {
        return "DisconnectPacket(hideDisconnectionScreen=" + this.hideDisconnectionScreen + ", message=" + this.message + ")";
    }
}


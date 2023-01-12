/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class SimpleEventPacket
extends DataPacket {
    public static final byte NETWORK_ID = 64;
    public static final int TYPE_ENABLE_COMMANDS = 1;
    public static final int TYPE_DISABLE_COMMANDS = 2;
    public short eventType;

    @Override
    public byte pid() {
        return 64;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putShort(this.eventType);
    }

    public String toString() {
        return "SimpleEventPacket(eventType=" + this.eventType + ")";
    }
}


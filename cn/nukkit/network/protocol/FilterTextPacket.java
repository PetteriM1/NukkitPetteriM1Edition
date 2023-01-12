/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class FilterTextPacket
extends DataPacket {
    public static final byte NETWORK_ID = -93;
    public String text;
    public boolean fromServer;

    @Override
    public byte pid() {
        return -93;
    }

    @Override
    public void decode() {
        this.text = this.getString();
        this.fromServer = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.text);
        this.putBoolean(this.fromServer);
    }

    public String toString() {
        return "FilterTextPacket(text=" + this.text + ", fromServer=" + this.fromServer + ")";
    }
}


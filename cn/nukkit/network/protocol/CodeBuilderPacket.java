/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class CodeBuilderPacket
extends DataPacket {
    public static final byte NETWORK_ID = -106;
    public boolean isOpening;
    public String url = "";

    @Override
    public byte pid() {
        return -106;
    }

    @Override
    public void decode() {
        this.url = this.getString();
        this.isOpening = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.url);
        this.putBoolean(this.isOpening);
    }

    public String toString() {
        return "CodeBuilderPacket(isOpening=" + this.isOpening + ", url=" + this.url + ")";
    }
}


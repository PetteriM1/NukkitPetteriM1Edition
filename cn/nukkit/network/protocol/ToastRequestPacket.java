/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class ToastRequestPacket
extends DataPacket {
    public String title = "";
    public String content = "";

    @Override
    public byte pid() {
        return -70;
    }

    @Override
    public void decode() {
        this.title = this.getString();
        this.content = this.getString();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.title);
        this.putString(this.content);
    }

    public String toString() {
        return "ToastRequestPacket(title=" + this.title + ", content=" + this.content + ")";
    }
}


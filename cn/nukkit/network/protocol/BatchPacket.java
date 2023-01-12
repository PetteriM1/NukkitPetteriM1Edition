/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class BatchPacket
extends DataPacket {
    public static final byte NETWORK_ID = -1;
    public byte[] payload;

    @Override
    public byte pid() {
        return -1;
    }

    @Override
    public void decode() {
        this.payload = this.get();
    }

    @Override
    public void encode() {
        this.a();
    }

    public void trim() {
        this.setBuffer(null);
    }
}


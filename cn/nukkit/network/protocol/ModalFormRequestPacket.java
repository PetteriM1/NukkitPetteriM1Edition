/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class ModalFormRequestPacket
extends DataPacket {
    public static final byte NETWORK_ID = 100;
    public int formId;
    public String data;

    @Override
    public byte pid() {
        return 100;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.formId);
        this.putString(this.data);
    }

    public String toString() {
        return "ModalFormRequestPacket(formId=" + this.formId + ", data=" + this.data + ")";
    }
}


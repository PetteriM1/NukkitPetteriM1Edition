/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class ModalFormResponsePacket
extends DataPacket {
    public static final byte NETWORK_ID = 101;
    public int formId;
    public String data = "null";
    public int cancelReason;

    @Override
    public byte pid() {
        return 101;
    }

    @Override
    public void decode() {
        this.formId = this.getVarInt();
        if (this.protocol >= 544) {
            if (this.getBoolean()) {
                this.data = this.getString();
            }
            if (this.getBoolean()) {
                this.cancelReason = this.getByte();
            }
        } else {
            this.data = this.getString();
        }
    }

    @Override
    public void encode() {
        this.a();
    }

    public String toString() {
        return "ModalFormResponsePacket(formId=" + this.formId + ", data=" + this.data + ", cancelReason=" + this.cancelReason + ")";
    }
}


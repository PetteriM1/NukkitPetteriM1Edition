/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class DeathInfoPacket
extends DataPacket {
    public static final byte NETWORK_ID = -67;
    public String messageTranslationKey;
    public String[] messageParameters = new String[0];

    @Override
    public byte pid() {
        return -67;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.messageTranslationKey);
        this.putUnsignedVarInt(this.messageParameters.length);
        for (String string : this.messageParameters) {
            this.putString(string);
        }
    }
}


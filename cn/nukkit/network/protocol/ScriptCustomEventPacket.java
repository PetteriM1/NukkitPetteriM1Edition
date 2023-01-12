/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import java.util.Arrays;

public class ScriptCustomEventPacket
extends DataPacket {
    public static final byte NETWORK_ID = 117;
    public String eventName;
    public byte[] eventData;

    @Override
    public byte pid() {
        return 117;
    }

    @Override
    public void decode() {
        this.eventName = this.getString();
        this.eventData = this.getByteArray();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.eventName);
        this.putByteArray(this.eventData);
    }

    public String toString() {
        return "ScriptCustomEventPacket(eventName=" + this.eventName + ", eventData=" + Arrays.toString(this.eventData) + ")";
    }
}


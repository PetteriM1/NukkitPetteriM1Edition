/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class NetworkStackLatencyPacket
extends DataPacket {
    public static final byte NETWORK_ID = 115;
    public long timestamp;
    public boolean unknownBool;

    @Override
    public byte pid() {
        return 115;
    }

    @Override
    public void decode() {
        this.timestamp = this.getLLong();
    }

    @Override
    public void encode() {
        this.reset();
        this.putLLong(this.timestamp);
        if (this.protocol >= 332) {
            this.putBoolean(this.unknownBool);
        }
    }

    public String toString() {
        return "NetworkStackLatencyPacket(timestamp=" + this.timestamp + ", unknownBool=" + this.unknownBool + ")";
    }
}


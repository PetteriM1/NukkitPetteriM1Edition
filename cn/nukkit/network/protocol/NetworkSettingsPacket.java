/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.types.PacketCompressionAlgorithm;

public class NetworkSettingsPacket
extends DataPacket {
    public int compressionThreshold;
    public PacketCompressionAlgorithm compressionAlgorithm;
    public boolean clientThrottleEnabled;
    public byte clientThrottleThreshold;
    public float clientThrottleScalar;

    @Override
    public byte pid() {
        return -113;
    }

    @Override
    public void encode() {
        this.reset();
        this.putLShort(this.compressionThreshold);
        this.putLShort(this.compressionAlgorithm.ordinal());
        if (this.protocol >= 554) {
            this.putBoolean(this.clientThrottleEnabled);
            this.putByte(this.clientThrottleThreshold);
            this.putLFloat(this.clientThrottleScalar);
        }
    }

    @Override
    public void decode() {
        this.b();
    }

    public String toString() {
        return "NetworkSettingsPacket(compressionThreshold=" + this.compressionThreshold + ", compressionAlgorithm=" + (Object)((Object)this.compressionAlgorithm) + ", clientThrottleEnabled=" + this.clientThrottleEnabled + ", clientThrottleThreshold=" + this.clientThrottleThreshold + ", clientThrottleScalar=" + this.clientThrottleScalar + ")";
    }
}


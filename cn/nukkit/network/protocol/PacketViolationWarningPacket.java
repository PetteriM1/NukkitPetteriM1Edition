/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class PacketViolationWarningPacket
extends DataPacket {
    public static final byte NETWORK_ID = -100;
    public PacketViolationType type;
    public PacketViolationSeverity severity;
    public int packetId;
    public String context;

    @Override
    public byte pid() {
        return -100;
    }

    @Override
    public void decode() {
        this.type = PacketViolationType.values()[this.getVarInt() + 1];
        this.severity = PacketViolationSeverity.values()[this.getVarInt()];
        this.packetId = this.getVarInt();
        this.context = this.getString();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.type.ordinal() - 1);
        this.putVarInt(this.severity.ordinal());
        this.putVarInt(this.packetId);
        this.putString(this.context);
    }

    public String toString() {
        return "PacketViolationWarningPacket(type=" + (Object)((Object)this.type) + ", severity=" + (Object)((Object)this.severity) + ", packetId=" + this.packetId + ", context=" + this.context + ")";
    }

    public static enum PacketViolationSeverity {
        UNKNOWN,
        WARNING,
        FINAL_WARNING,
        TERMINATING_CONNECTION;

    }

    public static enum PacketViolationType {
        UNKNOWN,
        MALFORMED_PACKET;

    }
}


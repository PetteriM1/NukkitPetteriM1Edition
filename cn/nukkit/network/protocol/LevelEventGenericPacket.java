/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.DataPacket;
import java.io.IOException;
import java.nio.ByteOrder;

public class LevelEventGenericPacket
extends DataPacket {
    public static final byte NETWORK_ID = 124;
    public int eventId;
    public CompoundTag tag;

    @Override
    public byte pid() {
        return 124;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.eventId);
        try {
            this.put(NBTIO.write(this.tag, ByteOrder.LITTLE_ENDIAN, true));
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    public String toString() {
        return "LevelEventGenericPacket(eventId=" + this.eventId + ", tag=" + this.tag + ")";
    }
}


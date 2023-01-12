/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.UUID;

public class EmoteListPacket
extends DataPacket {
    public static final byte NETWORK_ID = -104;
    public long runtimeId;
    public final List<UUID> pieceIds = new ObjectArrayList<UUID>();

    @Override
    public byte pid() {
        return -104;
    }

    @Override
    public void decode() {
        this.runtimeId = this.getEntityRuntimeId();
        int n = (int)this.getUnsignedVarInt();
        if (n > 1000) {
            throw new RuntimeException("Too big EmoteListPacket: " + n);
        }
        for (int k = 0; k < n; ++k) {
            UUID uUID = this.getUUID();
            this.pieceIds.add(uUID);
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.runtimeId);
        this.putUnsignedVarInt(this.pieceIds.size());
        for (UUID uUID : this.pieceIds) {
            this.putUUID(uUID);
        }
    }

    public String toString() {
        return "EmoteListPacket(runtimeId=" + this.runtimeId + ", pieceIds=" + this.pieceIds + ")";
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}


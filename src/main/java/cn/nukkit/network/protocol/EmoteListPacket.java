package cn.nukkit.network.protocol;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.UUID;

public class EmoteListPacket extends DataPacket {

    public long runtimeId;
    public final List<UUID> pieceIds = new ObjectArrayList<>();

    @Override
    public byte pid() {
        return ProtocolInfo.EMOTE_LIST_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
        this.runtimeId = this.getEntityUniqueId();
        int size = (int) this.getUnsignedVarInt();
        if (size > 1000) {
            throw new RuntimeException("Too big EmoteListPacket: " + size);
        }
        for (int i = 0; i < size; i++) {
            UUID id = this.getUUID();
            pieceIds.add(id);
        }
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putEntityUniqueId(runtimeId);
        this.putUnsignedVarInt(pieceIds.size());
        for (UUID id : pieceIds) {
            this.putUUID(id);
        }
    }
}

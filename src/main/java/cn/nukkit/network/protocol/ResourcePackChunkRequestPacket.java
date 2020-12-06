package cn.nukkit.network.protocol;

import lombok.ToString;

import java.util.UUID;

@ToString
public class ResourcePackChunkRequestPacket extends DataPacket {

    public UUID packId;
    public int chunkIndex;

    @Override
    public void decode(int protocolId) {
        this.packId = UUID.fromString(this.getString());
        this.chunkIndex = this.getLInt();
    }

    @Override
    public void encode(int protocolId) {
        this.putString(this.packId.toString());
        this.putLInt(this.chunkIndex);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.RESOURCE_PACK_CHUNK_REQUEST_PACKET;
    }
}

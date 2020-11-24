package cn.nukkit.network.protocol;

import lombok.ToString;

import java.util.UUID;

@ToString(exclude = "data")
public class ResourcePackChunkDataPacket extends DataPacket {

    public UUID packId;
    public int chunkIndex;
    public long progress;
    public byte[] data;

    @Override
    public void decode() {
        this.packId = UUID.fromString(this.getString());
        this.chunkIndex = this.getLInt();
        this.progress = this.getLLong();
        if (protocol < 388) {
            this.data = this.get(this.getLInt());
        } else {
            this.data = this.getByteArray();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.packId.toString());
        this.putLInt(this.chunkIndex);
        this.putLLong(this.progress);
        if (protocol < 388) {
            this.putLInt(this.data.length);
            this.put(this.data);
        } else {
            this.putByteArray(this.data);
        }
    }

    @Override
    public byte pid() {
        return ProtocolInfo.RESOURCE_PACK_CHUNK_DATA_PACKET;
    }
}

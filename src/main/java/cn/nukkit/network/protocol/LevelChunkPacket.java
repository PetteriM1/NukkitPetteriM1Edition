package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString(exclude = "data")
public class LevelChunkPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.FULL_CHUNK_DATA_PACKET;
    }

    public int chunkX;
    public int chunkZ;
    public int subChunkCount;
    public boolean cacheEnabled;
    public long[] blobIds;
    public byte[] data;

    @Override
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putVarInt(this.chunkX);
        this.putVarInt(this.chunkZ);
        if (protocolId >= 361) {
            this.putUnsignedVarInt(this.subChunkCount);
            this.putBoolean(cacheEnabled);
            if (this.cacheEnabled) {
                this.putUnsignedVarInt(blobIds.length);
                for (long blobId : blobIds) {
                    this.putLLong(blobId);
                }
            }
        }
        this.putByteArray(this.data);
    }
}

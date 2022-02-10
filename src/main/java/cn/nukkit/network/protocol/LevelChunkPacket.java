package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString(exclude = "data")
public class LevelChunkPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.FULL_CHUNK_DATA_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int chunkX;
    public int chunkZ;
    public int subChunkCount;
    public boolean cacheEnabled;
    /**
     * v1.18.0?10 and above
     */
    public boolean requestSubChunks;
    /**
     * v1.18.0?10 and above
     */
    public int subChunkLimit;
    public long[] blobIds;
    public byte[] data;

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.chunkX);
        this.putVarInt(this.chunkZ);
        if (protocol >= ProtocolInfo.v1_12_0) {
            if (protocol >= ProtocolInfo.v1_18_0) {
                if (!this.requestSubChunks) {
                    this.putUnsignedVarInt(this.subChunkCount);
                } else if (this.subChunkLimit < 0) {
                    this.putUnsignedVarInt(-1);
                } else {
                    this.putUnsignedVarInt(-2);
                    this.putUnsignedVarInt(this.subChunkLimit);
                }
            }else {
                this.putUnsignedVarInt(this.subChunkCount);
            }
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

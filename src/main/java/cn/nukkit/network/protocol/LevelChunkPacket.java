package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString(exclude = "data")
public class LevelChunkPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.FULL_CHUNK_DATA_PACKET;
    private static final long[] EMPTY_long = new long[0];
    public int chunkX;
    public int chunkZ;
    public int dimension;
    public int subChunkCount;
    public boolean cacheEnabled;
    public boolean requestSubChunks;
    public int subChunkLimit;
    public long[] blobIds = EMPTY_long;
    public byte[] data;

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();

        this.putVarInt(this.chunkX);
        this.putVarInt(this.chunkZ);
        if (protocol >= ProtocolInfo.v1_20_60) {
            this.putVarInt(this.dimension);
        }

        if (protocol >= 361) {
            if (protocol >= ProtocolInfo.v1_26_40 || protocol < ProtocolInfo.v1_18_10 || !this.requestSubChunks) {
                this.putUnsignedVarInt(this.subChunkCount);
            } else if (this.subChunkLimit < 0) {
                this.putUnsignedVarInt(-1);
            } else {
                this.putUnsignedVarInt(-2);

                if (protocol < ProtocolInfo.v1_26_40) {
                    this.putUnsignedVarInt(this.subChunkLimit);
                }
            }

            if (protocol >= ProtocolInfo.v1_26_40) {
                if (this.requestSubChunks) {
                    this.putBoolean(true);
                    this.putVarInt(this.subChunkLimit);
                } else {
                    this.putBoolean(false);
                }
            }

            this.putBoolean(cacheEnabled);

            if (this.cacheEnabled || protocol >= ProtocolInfo.v1_26_40) {
                this.putUnsignedVarInt(blobIds.length);
                for (long blobId : blobIds) {
                    this.putLLong(blobId);
                }
            }
        }
        this.putByteArray(this.data);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

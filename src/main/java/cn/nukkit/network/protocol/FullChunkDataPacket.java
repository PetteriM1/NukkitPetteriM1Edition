package cn.nukkit.network.protocol;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class FullChunkDataPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.FULL_CHUNK_DATA_PACKET;
    }

    public int chunkX;
    public int chunkZ;
    public byte[] data;

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.chunkX);
        this.putVarInt(this.chunkZ);
        this.putByteArray(this.data);
    }
}

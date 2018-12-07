package cn.nukkit.network.protocol;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class ChunkRadiusUpdatedPacket extends DataPacket {

    public int radius;

    @Override
    public void decode() {
        this.radius = this.getVarInt();
    }

    @Override
    public void encode() {
        super.reset();
        this.putVarInt(this.radius);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET;
    }
}

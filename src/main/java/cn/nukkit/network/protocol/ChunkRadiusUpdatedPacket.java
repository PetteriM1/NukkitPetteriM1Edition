package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class ChunkRadiusUpdatedPacket extends DataPacket {

    public int radius;

    @Override
    public void decodePayload(int protocolId) {
        this.radius = this.getVarInt();
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putVarInt(this.radius);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET;
    }
}

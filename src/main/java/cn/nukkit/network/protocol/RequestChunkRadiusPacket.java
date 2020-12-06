package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class RequestChunkRadiusPacket extends DataPacket {

    public int radius;

    @Override
    public void decode(int protocolId) {
        this.radius = this.getVarInt();
    }

    @Override
    public void encode(int protocolId) {
    }

    @Override
    public byte pid() {
        return ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET;
    }
}

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
    public void decodePayload(int protocolId) {
        this.radius = this.getVarInt();
    }

    @Override
    public void encodePayload(int protocolId) {
    }

    @Override
    public byte pid() {
        return ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET;
    }
}

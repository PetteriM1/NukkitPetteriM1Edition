package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import lombok.ToString;

@ToString
public class NetworkChunkPublisherUpdatePacket extends DataPacket {

    public BlockVector3 position;
    public int radius;

    @Override
    public byte pid() {
        return ProtocolInfo.NETWORK_CHUNK_PUBLISHER_UPDATE_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
        this.position = this.getSignedBlockPosition();
        this.radius = (int) this.getUnsignedVarInt();
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putSignedBlockPosition(position);
        this.putUnsignedVarInt(radius);
    }
}

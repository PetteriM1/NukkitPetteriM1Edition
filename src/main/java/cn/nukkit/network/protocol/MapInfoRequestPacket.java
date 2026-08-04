package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * Created by CreeperFace on 5.3.2017.
 */
@ToString
public class MapInfoRequestPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MAP_INFO_REQUEST_PACKET;

    public long mapId;

    @Override
    public void decode() {
        mapId = this.getEntityUniqueId();
        if (protocol > ProtocolInfo.v1_19_20) {
            this.getLInt(); // pixels count
        }
    }

    @Override
    public void encode() {
        this.encodeUnsupported();
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

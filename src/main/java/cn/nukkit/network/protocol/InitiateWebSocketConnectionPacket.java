package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class InitiateWebSocketConnectionPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.INITIATE_WEB_SOCKET_CONNECTION_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
    }
}

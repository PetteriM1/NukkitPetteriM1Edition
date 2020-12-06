package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class ClientToServerHandshakePacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.CLIENT_TO_SERVER_HANDSHAKE_PACKET;
    }

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
    }
}

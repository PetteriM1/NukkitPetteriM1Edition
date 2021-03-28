package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class ServerToClientHandshakePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SERVER_TO_CLIENT_HANDSHAKE_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public String publicKey;
    public String serverToken;
    public String privateKey;

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
    }
}

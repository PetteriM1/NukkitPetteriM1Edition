package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class SubClientLoginPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.SUB_CLIENT_LOGIN_PACKET;
    }

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
    }
}

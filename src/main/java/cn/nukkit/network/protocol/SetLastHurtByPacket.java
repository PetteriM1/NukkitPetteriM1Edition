package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class SetLastHurtByPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.SET_LAST_HURT_BY_PACKET;
    }

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
    }
}

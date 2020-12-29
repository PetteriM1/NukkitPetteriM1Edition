package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class SetHealthPacket extends DataPacket {

    public int health;

    @Override
    public byte pid() {
        return ProtocolInfo.SET_HEALTH_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putUnsignedVarInt(this.health);
    }
}

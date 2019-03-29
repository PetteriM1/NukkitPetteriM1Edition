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
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.health);
    }
}

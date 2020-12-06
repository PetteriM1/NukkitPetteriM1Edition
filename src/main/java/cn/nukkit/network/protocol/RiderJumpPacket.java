package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class RiderJumpPacket extends DataPacket {

    public int jumpStrength;

    @Override
    public byte pid() {
        return ProtocolInfo.RIDER_JUMP_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        this.jumpStrength = this.getVarInt();
    }

    @Override
    public void encode(int protocolId) {
        this.putVarInt(this.jumpStrength);
    }
}

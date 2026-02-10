package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class RiderJumpPacket extends DataPacket { // Called PassengerJumpPacket since 1.21.40

    public static final byte NETWORK_ID = ProtocolInfo.RIDER_JUMP_PACKET;

    public int jumpStrength;

    @Override
    public void decode() {
        this.jumpStrength = this.getVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.jumpStrength);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

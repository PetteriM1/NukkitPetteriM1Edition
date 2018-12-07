package cn.nukkit.network.protocol;

public class RiderJumpPacket extends DataPacket {

    public int jumpStrength;

    @Override
    public byte pid() {
        return ProtocolInfo.RIDER_JUMP_PACKET;
    }

    @Override
    public void decode() {
        this.jumpStrength = this.getVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.jumpStrength);
    }
}

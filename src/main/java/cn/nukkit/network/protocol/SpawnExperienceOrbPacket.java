package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class SpawnExperienceOrbPacket extends DataPacket {

    public float x;
    public float y;
    public float z;
    public int amount;

    @Override
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putVector3f(this.x, this.y, this.z);
        this.putUnsignedVarInt(this.amount);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.SPAWN_EXPERIENCE_ORB_PACKET;
    }
}

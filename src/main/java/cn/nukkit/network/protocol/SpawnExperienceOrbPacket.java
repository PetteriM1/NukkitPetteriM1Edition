package cn.nukkit.network.protocol;

public class SpawnExperienceOrbPacket extends DataPacket {

    public float x;
    public float y;
    public float z;
    public int amount;

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putVector3f(this.x, this.y, this.z);
        this.putUnsignedVarInt(this.amount);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.SPAWN_EXPERIENCE_ORB_PACKET;
    }
}

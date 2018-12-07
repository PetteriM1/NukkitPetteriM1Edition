package cn.nukkit.network.protocol;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class SetEntityMotionPacket extends DataPacket {

    public long eid;
    public float motionX;
    public float motionY;
    public float motionZ;

    @Override
    public byte pid() {
        return ProtocolInfo.SET_ENTITY_MOTION_PACKET;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putVector3f(this.motionX, this.motionY, this.motionZ);
    }
}

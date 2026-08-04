package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class SetEntityMotionPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SET_ENTITY_MOTION_PACKET;

    public long eid;
    public float motionX;
    public float motionY;
    public float motionZ;
    public long tick;

    @Override
    public void decode() {
        this.eid = this.getEntityRuntimeId();
        this.motionX = this.getLFloat();
        this.motionY = this.getLFloat();
        this.motionZ = this.getLFloat();
        if (protocol >= ProtocolInfo.v1_20_70) {
            this.tick = this.getUnsignedVarLong();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.putVector3f(this.motionX, this.motionY, this.motionZ);
        if (protocol >= ProtocolInfo.v1_20_70) {
            this.putUnsignedVarLong(this.tick);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

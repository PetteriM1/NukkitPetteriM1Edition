package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
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
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putEntityRuntimeId(this.eid);
        this.putVector3f(this.motionX, this.motionY, this.motionZ);
    }
}

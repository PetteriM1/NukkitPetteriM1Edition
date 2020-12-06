package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class MoveEntityAbsolutePacket extends DataPacket {

    public long eid;
    public double x;
    public double y;
    public double z;
    public double yaw;
    public double headYaw;
    public double pitch;
    public boolean onGround;
    public boolean teleport;

    @Override
    public byte pid() {
        return ProtocolInfo.MOVE_ENTITY_ABSOLUTE_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        this.eid = this.getEntityRuntimeId();
        if (protocolId >= 274) {
            int flags = this.getByte();
            teleport = (flags & 0x01) != 0;
            onGround = (flags & 0x02) != 0;
        }
        Vector3f v = this.getVector3f();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.pitch = this.getByte() * 1.40625;
        this.headYaw = this.getByte() * 1.40625;
        this.yaw = this.getByte() * 1.40625;
    }

    @Override
    public void encode(int protocolId) {
        this.putEntityRuntimeId(this.eid);
        if (protocolId >= 274) {
            byte flags = 0;
            if (onGround) {
                flags |= 0x01;
            }
            if (teleport) {
                flags |= 0x02;
            }
            this.putByte(flags);
        }
        this.putVector3f((float) this.x, (float) this.y, (float) this.z);
        this.putByte((byte) (this.pitch / 1.40625));
        this.putByte((byte) (this.headYaw / 1.40625));
        this.putByte((byte) (this.yaw / 1.40625));
        if (protocolId <= 261) {
            this.putBoolean(this.onGround);
            this.putBoolean(this.teleport);
        }
    }
}

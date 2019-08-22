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
    public void decode() {
        this.eid = this.getEntityRuntimeId();
        if (protocol >= 274) {
            int flags = this.getByte();
            teleport = (flags & 0x01) != 0;
            onGround = (flags & 0x02) != 0;
        }
        Vector3f v = this.getVector3f();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.pitch = this.getByte() * (360d / 256d);
        this.headYaw = this.getByte() * (360d / 256d);
        this.yaw = this.getByte() * (360d / 256d);
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        if (protocol >= 274) {
            byte flags = 0;
            if (teleport) {
                flags |= 0x01;
            }
            if (onGround) {
                flags |= 0x02;
            }
            this.putByte(flags);
        }
        this.putVector3f((float) this.x, (float) this.y, (float) this.z);
        this.putByte((byte) (this.pitch / (360d / 256d)));
        this.putByte((byte) (this.headYaw / (360d / 256d)));
        this.putByte((byte) (this.yaw / (360d / 256d)));
        if (protocol <= 261) {
            this.putBoolean(this.onGround);
            this.putBoolean(this.teleport);
        }
    }
}
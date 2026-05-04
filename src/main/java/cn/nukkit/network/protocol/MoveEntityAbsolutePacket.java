package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class MoveEntityAbsolutePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MOVE_ENTITY_ABSOLUTE_PACKET;

    public long eid;
    public double x;
    public double y;
    public double z;
    public double yaw;
    public double headYaw;
    public double pitch;
    public boolean onGround;
    public boolean teleport;
    public boolean forceMoveLocalEntity;
    public boolean forceCompletion; // Since 975

    @Override
    public void decode() {
        this.eid = this.getEntityRuntimeId();
        if (protocol >= 274) {
            int flags = this.getByte();
            onGround = (flags & 0x01) != 0;
            teleport = (flags & 0x02) != 0;
            forceMoveLocalEntity = (flags & 0x04) != 0;
            if (protocol >= ProtocolInfo.v1_26_20_26) {
                forceCompletion = (flags & 0x08) != 0;
            }
        }
        Vector3f v = this.getVector3f();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.pitch = this.getByte() * 1.40625;
        this.headYaw = this.getByte() * 1.40625;
        this.yaw = this.getByte() * 1.40625;
        if (protocol <= 261) {
            this.onGround = this.getBoolean();
            this.teleport = this.getBoolean();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        if (protocol >= 274) {
            byte flags = 0;
            if (onGround) {
                flags |= 0x01;
            }
            if (teleport) {
                flags |= 0x02;
            }
            if (forceMoveLocalEntity) {
                flags |= 0x04;
            }
            if (forceCompletion && protocol >= ProtocolInfo.v1_26_20_26) {
                flags |= 0x08;
            }
            this.putByte(flags);
        }
        this.putVector3f((float) this.x, (float) this.y, (float) this.z);
        this.putByte((byte) (this.pitch / 1.40625));
        this.putByte((byte) (this.headYaw / 1.40625));
        this.putByte((byte) (this.yaw / 1.40625));
        if (protocol <= 261) {
            this.putBoolean(this.onGround);
            this.putBoolean(this.teleport);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

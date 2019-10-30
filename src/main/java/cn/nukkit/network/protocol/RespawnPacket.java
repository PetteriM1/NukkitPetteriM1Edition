package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class RespawnPacket extends DataPacket {

    public static final int STATE_SPAWN = 1;
    public static final int STATE_SELECT_SPAWN = 2;
    public static final int STATE_SELECTED_SPAWN = 3;

    public float x;
    public float y;
    public float z;
    public int respawnState = STATE_SPAWN;
    public long unknownEntityId;

    @Override
    public void decode() {
        Vector3f v = this.getVector3f();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        if (protocol >= 388) {
            this.respawnState = this.getByte();
            this.unknownEntityId = this.getEntityRuntimeId();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putVector3f(this.x, this.y, this.z);
        if (protocol >= 388) {
            this.putByte((byte) respawnState);
            this.putEntityRuntimeId(unknownEntityId);
        }
    }

    @Override
    public byte pid() {
        return ProtocolInfo.RESPAWN_PACKET;
    }
}

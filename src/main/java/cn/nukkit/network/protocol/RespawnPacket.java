package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class RespawnPacket extends DataPacket {

    public static final int STATE_SEARCHING_FOR_SPAWN = 0;
    public static final int STATE_READY_TO_SPAWN = 1;
    public static final int STATE_CLIENT_READY_TO_SPAWN = 2;

    public float x;
    public float y;
    public float z;
    public int respawnState = STATE_SEARCHING_FOR_SPAWN;
    public long runtimeEntityId;

    @Override
    public void decodePayload(int protocolId) {
        Vector3f v = this.getVector3f();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        if (protocolId >= 388) {
            this.respawnState = this.getByte();
            this.runtimeEntityId  = this.getEntityRuntimeId();
        }
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putVector3f(this.x, this.y, this.z);
        if (protocolId >= 388) {
            this.putByte((byte) respawnState);
            this.putEntityRuntimeId(runtimeEntityId);
        }
    }

    @Override
    public byte pid() {
        return ProtocolInfo.RESPAWN_PACKET;
    }
}

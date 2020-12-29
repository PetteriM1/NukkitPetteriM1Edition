package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * Created on 2016/1/5 by xtypr.
 * Package cn.nukkit.network.protocol in project nukkit .
 */
@ToString
public class ChangeDimensionPacket extends DataPacket {

    public int dimension;

    public float x;
    public float y;
    public float z;

    public boolean respawn;

    @Override
    public void decodePayload(int protocolId) {
        this.dimension = this.getVarInt();
        this.x = this.getVector3f().x;
        this.y = this.getVector3f().y;
        this.z = this.getVector3f().z;
        this.respawn = this.getBoolean();
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putVarInt(this.dimension);
        this.putVector3f(this.x, this.y, this.z);
        this.putBoolean(this.respawn);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.CHANGE_DIMENSION_PACKET;
    }
}

package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class AddPaintingPacket extends DataPacket {

    public long entityUniqueId;
    public long entityRuntimeId;
    public float x;
    public float y;
    public float z;
    public int direction;
    public String title;

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        if (protocolId < 361) {
            this.putBlockVector3((int) this.x, (int) this.y, (int) this.z);
        } else {
            this.putVector3f(this.x, this.y, this.z);
        }
        this.putVarInt(this.direction);
        this.putString(this.title);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.ADD_PAINTING_PACKET;
    }
}

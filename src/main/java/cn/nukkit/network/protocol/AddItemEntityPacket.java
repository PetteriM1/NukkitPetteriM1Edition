package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Binary;
import lombok.ToString;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class AddItemEntityPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.ADD_ITEM_ENTITY_PACKET;
    }

    public long entityUniqueId;
    public long entityRuntimeId;
    public Item item;
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;
    public EntityMetadata metadata = new EntityMetadata();
    public boolean isFromFishing = false;

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putSlot(protocolId, this.item);
        this.putVector3f(this.x, this.y, this.z);
        this.putVector3f(this.speedX, this.speedY, this.speedZ);
        this.put(Binary.writeMetadata(protocolId, metadata));
        if (protocolId >= 223) {
            this.putBoolean(this.isFromFishing);
        }
    }
}

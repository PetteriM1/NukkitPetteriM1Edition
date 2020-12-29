package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Binary;
import lombok.ToString;

import java.util.UUID;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class AddPlayerPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.ADD_PLAYER_PACKET;
    }

    public UUID uuid;
    public String username;
    public long entityUniqueId;
    public long entityRuntimeId;
    public String platformChatId = "";
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;
    public float pitch;
    public float yaw;
    public Item item;
    public EntityMetadata metadata = new EntityMetadata();
    public String deviceId = "";
    public int buildPlatform = -1;

    @Override
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putUUID(this.uuid);
        this.putString(this.username);
        if (protocolId >= 223 && protocolId <= 282) {
            this.putString("");
            this.putVarInt(0);
        }
        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        if (protocolId >= 223) {
            this.putString(this.platformChatId);
        }
        this.putVector3f(this.x, this.y, this.z);
        this.putVector3f(this.speedX, this.speedY, this.speedZ);
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw);
        this.putLFloat(this.yaw);
        this.putSlot(protocolId, this.item);
        this.put(Binary.writeMetadata(protocolId, this.metadata));
        if (protocolId > 274) {
            this.putUnsignedVarInt(0);
            this.putUnsignedVarInt(0);
            this.putUnsignedVarInt(0);
            this.putUnsignedVarInt(0);
            this.putUnsignedVarInt(0);
            this.putLLong(entityUniqueId);
            this.putUnsignedVarInt(0);
            this.putString(deviceId);
            if (protocolId >= 388) {
                this.putLInt(buildPlatform);
            }
        }
    }
}

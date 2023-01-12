/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.Server;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;
import java.util.UUID;

public class AddPlayerPacket
extends DataPacket {
    public static final byte NETWORK_ID = 12;
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
    public float headYaw = -1.0f;
    public Item item;
    public int gameType = Server.getInstance().getGamemode();
    public EntityMetadata metadata = new EntityMetadata();
    public String deviceId = "";
    public int buildPlatform = -1;

    @Override
    public byte pid() {
        return 12;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUUID(this.uuid);
        this.putString(this.username);
        if (this.protocol >= 223 && this.protocol <= 282) {
            this.putString("");
            this.putVarInt(0);
        }
        if (this.protocol < 534) {
            this.putEntityUniqueId(this.entityUniqueId);
        }
        this.putEntityRuntimeId(this.entityRuntimeId);
        if (this.protocol >= 223) {
            this.putString(this.platformChatId);
        }
        this.putVector3f(this.x, this.y, this.z);
        this.putVector3f(this.speedX, this.speedY, this.speedZ);
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw);
        this.putLFloat(this.headYaw == -1.0f ? this.yaw : this.headYaw);
        this.putSlot(this.protocol, this.item);
        if (this.protocol >= 503) {
            this.putVarInt(this.gameType);
        }
        this.put(Binary.writeMetadata(this.protocol, this.metadata));
        if (this.protocol > 274) {
            if (this.protocol >= 534) {
                if (this.protocol >= 557) {
                    this.putUnsignedVarInt(0L);
                    this.putUnsignedVarInt(0L);
                }
                this.putLLong(this.entityUniqueId);
                this.putUnsignedVarInt(0L);
                this.putUnsignedVarInt(0L);
                this.putUnsignedVarInt(1L);
                this.putLShort(1);
                this.putLInt(262143);
                this.putLInt(63);
                this.putLFloat(0.1f);
                this.putLFloat(0.05f);
            } else {
                this.putUnsignedVarInt(0L);
                this.putUnsignedVarInt(0L);
                this.putUnsignedVarInt(0L);
                this.putUnsignedVarInt(0L);
                this.putUnsignedVarInt(0L);
                this.putLLong(this.entityUniqueId);
            }
            this.putUnsignedVarInt(0L);
            this.putString(this.deviceId);
            if (this.protocol >= 388) {
                this.putLInt(this.buildPlatform);
            }
        }
    }

    public String toString() {
        return "AddPlayerPacket(uuid=" + this.uuid + ", username=" + this.username + ", entityUniqueId=" + this.entityUniqueId + ", entityRuntimeId=" + this.entityRuntimeId + ", platformChatId=" + this.platformChatId + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", speedX=" + this.speedX + ", speedY=" + this.speedY + ", speedZ=" + this.speedZ + ", pitch=" + this.pitch + ", yaw=" + this.yaw + ", headYaw=" + this.headYaw + ", item=" + this.item + ", gameType=" + this.gameType + ", metadata=" + this.metadata + ", deviceId=" + this.deviceId + ", buildPlatform=" + this.buildPlatform + ")";
    }
}


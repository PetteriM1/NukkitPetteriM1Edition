package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.Skin;
import lombok.ToString;

import java.util.UUID;

/**
 * @author Nukkit Project Team
 */
@ToString
public class PlayerListPacket extends DataPacket {

    public static final byte TYPE_ADD = 0;
    public static final byte TYPE_REMOVE = 1;

    public byte type;
    public Entry[] entries = new Entry[0];

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.type);
        this.putUnsignedVarInt(this.entries.length);
        for (Entry entry : this.entries) {
            if (protocol >= 223) {
                this.putUUID(entry.uuid);
            }
            if (type == TYPE_ADD) {
                if (protocol < 223) {
                    this.putUUID(entry.uuid);
                }
                this.putVarLong(entry.entityId);
                this.putString(entry.name);
                if (protocol >= 223 && protocol <= 282) {
                    this.putString("");
                    this.putVarInt(0);
                }
                this.putSkin(entry.skin);
                if (protocol < 223) {
                    this.putByteArray(new byte[0]);
                }
                this.putString(entry.xboxUserId);
                if (protocol >= 223) {
                    this.putString(entry.platformChatId);
                }
            } else if (protocol < 223) {
                this.putUUID(entry.uuid);
            }
        }
    }

    @Override
    public byte pid() {
        return ProtocolInfo.PLAYER_LIST_PACKET;
    }

    @ToString
    public static class Entry {

        public final UUID uuid;
        public long entityId = 0;
        public String name = "";
        public Skin skin;
        public String xboxUserId = "";
        public String platformChatId = "";

        public Entry(UUID uuid) {
            this.uuid = uuid;
        }

        public Entry(UUID uuid, long entityId, String name, Skin skin) {
            this(uuid, entityId, name, skin, "");
        }

        public Entry(UUID uuid, long entityId, String name, Skin skin, String xboxUserId) {
            this.uuid = uuid;
            this.entityId = entityId;
            this.name = name;
            this.skin = skin;
            this.xboxUserId = xboxUserId == null ? "" : xboxUserId;
        }
    }
}

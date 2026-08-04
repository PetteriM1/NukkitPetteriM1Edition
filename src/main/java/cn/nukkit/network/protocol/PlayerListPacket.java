package cn.nukkit.network.protocol;

import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import lombok.ToString;

import java.awt.*;
import java.util.UUID;

/**
 * @author Nukkit Project Team
 */
@ToString
public class PlayerListPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.PLAYER_LIST_PACKET;

    public static final byte TYPE_ADD = 0;
    public static final byte TYPE_REMOVE = 1;

    public byte type = -1; // for legacy reasons allow using this to override type of all entries
    public Entry[] entries = new Entry[0];

    @ToString
    public static class Entry {

        public byte type;
        public final UUID uuid;
        public long entityId = 0;
        public String name = "";
        public Skin skin;
        public String xboxUserId = "";
        public String platformChatId = "";
        public int buildPlatform = -1;
        public boolean isTeacher;
        public boolean isHost;
        public boolean isSubClient;
        public Color color;

        public Entry(UUID uuid) {
            this.uuid = uuid;
        }

        public Entry(UUID uuid, long entityId, String name, Skin skin) {
            this(uuid, entityId, name, skin, "");
        }

        public Entry(UUID uuid, long entityId, String name, Skin skin, String xboxUserId) {
            this(uuid, entityId, name, skin, xboxUserId, Color.WHITE);
        }

        public Entry(UUID uuid, long entityId, String name, Skin skin, String xboxUserId, Color color) {
            this.uuid = uuid;
            this.entityId = entityId;
            this.name = name;
            this.skin = skin;
            this.xboxUserId = xboxUserId == null ? "" : xboxUserId;
            this.color = color;
        }
    }

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();

        if (this.entries.length == 0) {
            if (protocol < ProtocolInfo.v1_26_40) {
                this.putByte(TYPE_ADD);
            }

            this.putUnsignedVarInt(0);
            Server.getInstance().getLogger().debug("PlayerListPacket with no entries");
            return;
        }

        if (protocol >= ProtocolInfo.v1_26_40) {
            this.putUnsignedVarInt(this.entries.length);

            for (Entry entry : this.entries) {
                int entryType = this.type == -1 ? entry.type : this.type;
                this.putUnsignedVarInt(entryType == TYPE_ADD ? 1 : 0);

                switch (entryType) {
                    case TYPE_ADD:
                        this.putByte(this.type);
                        this.putUUID(entry.uuid);
                        this.putVarLong(entry.entityId);
                        this.putString(entry.name);
                        this.putString(entry.xboxUserId);
                        this.putString(entry.platformChatId);
                        this.putLInt(entry.buildPlatform);
                        this.putSkin(protocol, entry.skin);
                        this.putBoolean(entry.isTeacher);
                        this.putBoolean(entry.isHost);
                        this.putBoolean(entry.isSubClient);
                        this.putLInt(entry.color.getRGB());
                        break;
                    case TYPE_REMOVE:
                        this.putByte(this.type);
                        this.putUUID(entry.uuid);
                        break;
                    default:
                        throw new IllegalArgumentException("entryType: " + entryType);
                }
            }

            return;
        }

        byte entryType = this.type == -1 ? this.entries[0].type : this.type;
        this.putByte(entryType);

        this.putUnsignedVarInt(this.entries.length);

        switch (entryType) {
            case TYPE_ADD:
                for (Entry entry : this.entries) {
                    if (protocol >= 223) {
                        this.putUUID(entry.uuid);
                    }
                    this.putVarLong(entry.entityId);
                    this.putString(entry.name);
                    if (protocol >= 223 && protocol <= 282) {
                        this.putString("");
                        this.putVarInt(0);
                    }
                    if (protocol < 388) {
                        this.putSkin(protocol, entry.skin);
                        if (protocol < 223) {
                            this.putByteArray(new byte[0]);
                        }
                    }
                    this.putString(entry.xboxUserId);
                    if (protocol >= 223) {
                        this.putString(entry.platformChatId);
                        if (protocol >= 388) {
                            this.putLInt(entry.buildPlatform);
                            this.putSkin(protocol, entry.skin);
                            this.putBoolean(entry.isTeacher);
                            this.putBoolean(entry.isHost);
                            if (protocol >= ProtocolInfo.v1_20_60) {
                                this.putBoolean(entry.isSubClient);
                                if (protocol >= ProtocolInfo.v1_21_80) {
                                    this.putLInt(entry.color.getRGB());
                                }
                            }
                        }
                    }
                }
                if (protocol >= ProtocolInfo.v1_14_60) {
                    for (Entry entry : this.entries) { // WTF Mojang
                        this.putBoolean(entry.skin.isTrusted());
                    }
                }
                break;
            case TYPE_REMOVE:
                for (Entry entry : this.entries) {
                    if (protocol >= 223) {
                        this.putUUID(entry.uuid);
                    }
                }
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

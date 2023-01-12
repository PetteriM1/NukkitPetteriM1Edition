/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.network.protocol.DataPacket;
import java.util.Arrays;
import java.util.UUID;

public class PlayerListPacket
extends DataPacket {
    public static final byte NETWORK_ID = 63;
    public static final byte TYPE_ADD = 0;
    public static final byte TYPE_REMOVE = 1;
    public byte type;
    public Entry[] entries = new Entry[0];

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.type);
        this.putUnsignedVarInt(this.entries.length);
        switch (this.type) {
            case 0: {
                for (Entry entry : this.entries) {
                    if (this.protocol >= 223) {
                        this.putUUID(entry.uuid);
                    }
                    this.putVarLong(entry.entityId);
                    this.putString(entry.name);
                    if (this.protocol >= 223 && this.protocol <= 282) {
                        this.putString("");
                        this.putVarInt(0);
                    }
                    if (this.protocol < 388) {
                        this.putSkin(this.protocol, entry.skin);
                        if (this.protocol < 223) {
                            this.putByteArray(new byte[0]);
                        }
                    }
                    this.putString(entry.xboxUserId);
                    if (this.protocol < 223) continue;
                    this.putString(entry.platformChatId);
                    if (this.protocol < 388) continue;
                    this.putLInt(entry.buildPlatform);
                    this.putSkin(this.protocol, entry.skin);
                    this.putBoolean(entry.isTeacher);
                    this.putBoolean(entry.isHost);
                }
                if (this.protocol < 390) break;
                for (Entry entry : this.entries) {
                    this.putBoolean(entry.skin.isTrusted());
                }
                break;
            }
            case 1: {
                for (Entry entry : this.entries) {
                    if (this.protocol < 223) continue;
                    this.putUUID(entry.uuid);
                }
                break;
            }
        }
    }

    @Override
    public byte pid() {
        return 63;
    }

    public String toString() {
        return "PlayerListPacket(type=" + this.type + ", entries=" + Arrays.deepToString(this.entries) + ")";
    }

    public static class Entry {
        public final UUID uuid;
        public long entityId = 0L;
        public String name = "";
        public Skin skin;
        public String xboxUserId = "";
        public String platformChatId = "";
        public int buildPlatform = -1;
        public boolean isTeacher;
        public boolean isHost;

        public Entry(UUID uUID) {
            this.uuid = uUID;
        }

        public Entry(UUID uUID, long l, String string, Skin skin) {
            this(uUID, l, string, skin, "");
        }

        public Entry(UUID uUID, long l, String string, Skin skin, String string2) {
            this.uuid = uUID;
            this.entityId = l;
            this.name = string;
            this.skin = skin;
            this.xboxUserId = string2 == null ? "" : string2;
        }

        public String toString() {
            return "PlayerListPacket.Entry(uuid=" + this.uuid + ", entityId=" + this.entityId + ", name=" + this.name + ", skin=" + this.skin + ", xboxUserId=" + this.xboxUserId + ", platformChatId=" + this.platformChatId + ", buildPlatform=" + this.buildPlatform + ", isTeacher=" + this.isTeacher + ", isHost=" + this.isHost + ")";
        }
    }
}


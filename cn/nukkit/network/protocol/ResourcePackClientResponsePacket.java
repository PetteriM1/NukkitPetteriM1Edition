/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import java.util.Arrays;
import java.util.UUID;

public class ResourcePackClientResponsePacket
extends DataPacket {
    public static final byte NETWORK_ID = 8;
    public static final byte STATUS_REFUSED = 1;
    public static final byte STATUS_SEND_PACKS = 2;
    public static final byte STATUS_HAVE_ALL_PACKS = 3;
    public static final byte STATUS_COMPLETED = 4;
    public byte responseStatus;
    public Entry[] packEntries;

    @Override
    public void decode() {
        this.responseStatus = (byte)this.getByte();
        this.packEntries = new Entry[Math.min(this.getLShort(), 1024)];
        for (int k = 0; k < this.packEntries.length; ++k) {
            String[] stringArray = this.getString().split("_");
            this.packEntries[k] = new Entry(UUID.fromString(stringArray[0]), stringArray[1]);
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.responseStatus);
        this.putLShort(this.packEntries.length);
        for (Entry entry : this.packEntries) {
            this.putString(entry.uuid.toString() + '_' + entry.version);
        }
    }

    @Override
    public byte pid() {
        return 8;
    }

    public String toString() {
        return "ResourcePackClientResponsePacket(responseStatus=" + this.responseStatus + ", packEntries=" + Arrays.deepToString(this.packEntries) + ")";
    }

    public static class Entry {
        public final UUID uuid;
        public final String version;

        public Entry(UUID uUID, String string) {
            this.uuid = uUID;
            this.version = string;
        }

        public String toString() {
            return "ResourcePackClientResponsePacket.Entry(uuid=" + this.uuid + ", version=" + this.version + ")";
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.NOBF;
import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.item.RuntimeItems;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RuntimeItemMapping {
    private static final Logger b = LogManager.getLogger(RuntimeItemMapping.class);
    private final int a;
    @NOBF
    private final Int2ObjectMap<LegacyEntry> runtime2Legacy = new Int2ObjectOpenHashMap<LegacyEntry>();
    @NOBF
    private final Int2ObjectMap<RuntimeEntry> legacy2Runtime = new Int2ObjectOpenHashMap<RuntimeEntry>();
    @NOBF
    private final Map<String, LegacyEntry> identifier2Legacy = new HashMap<String, LegacyEntry>();
    @NOBF
    private byte[] itemPalette;

    public RuntimeItemMapping(Map<String, RuntimeItems.MappingEntry> map, int n) {
        this.a = n;
        InputStream inputStream = Server.class.getClassLoader().getResourceAsStream("runtime_item_states_" + n + ".json");
        if (inputStream == null) {
            throw new AssertionError((Object)("Unable to load runtime_item_states_" + n + ".json"));
        }
        JsonArray jsonArray = JsonParser.parseReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            int n2;
            if (!jsonElement.isJsonObject()) {
                throw new IllegalStateException("(" + n + ") Invalid entry");
            }
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String string = jsonObject.get("name").getAsString();
            int n3 = jsonObject.get("id").getAsInt();
            if (this.a < 419) {
                this.a(string, n3);
                continue;
            }
            boolean bl = false;
            int n4 = 0;
            if (map.containsKey(string)) {
                RuntimeItems.MappingEntry mappingEntry = map.get(string);
                n2 = RuntimeItems.getLegacyIdFromLegacyString(mappingEntry.getLegacyName());
                if (n2 == -1) {
                    throw new IllegalStateException("(" + n + ") Unable to match " + mappingEntry + " with legacyId");
                }
                n4 = mappingEntry.getDamage();
                bl = true;
            } else {
                n2 = RuntimeItems.getLegacyIdFromLegacyString(string);
                if (n2 == -1) {
                    b.trace("(" + n + ") Unable to find legacyId for " + string);
                    continue;
                }
            }
            int n5 = this.getFullId(n2, n4);
            LegacyEntry legacyEntry = new LegacyEntry(n2, bl, n4);
            if (Nukkit.DEBUG > 1 && this.runtime2Legacy.containsKey(n3)) {
                Server.getInstance().getLogger().warning("RuntimeItemMapping (Protocol " + n + "): Registering " + string + " but runtime id " + n3 + " is already used");
            }
            this.runtime2Legacy.put(n3, legacyEntry);
            this.identifier2Legacy.put(string, legacyEntry);
            this.legacy2Runtime.put(n5, new RuntimeEntry(string, n3, bl));
        }
        this.a();
    }

    private void a(String string, int n) {
        int n2 = this.getFullId(n, 0);
        LegacyEntry legacyEntry = new LegacyEntry(n, false, 0);
        if (Nukkit.DEBUG > 1 && this.runtime2Legacy.containsKey(n)) {
            Server.getInstance().getLogger().warning("RuntimeItemMapping (Legacy): Registering " + string + " but runtime id " + n + " is already used");
        }
        this.runtime2Legacy.put(n, legacyEntry);
        this.identifier2Legacy.put(string, legacyEntry);
        this.legacy2Runtime.put(n2, new RuntimeEntry(string, n, false));
    }

    private void a() {
        BinaryStream binaryStream = new BinaryStream();
        binaryStream.putUnsignedVarInt(this.legacy2Runtime.size());
        for (RuntimeEntry runtimeEntry : this.legacy2Runtime.values()) {
            binaryStream.putString(runtimeEntry.getIdentifier());
            binaryStream.putLShort(runtimeEntry.getRuntimeId());
            if (this.a < 419) continue;
            binaryStream.putBoolean(false);
        }
        this.itemPalette = binaryStream.getBuffer();
    }

    public LegacyEntry fromRuntime(int n) {
        LegacyEntry legacyEntry = (LegacyEntry)this.runtime2Legacy.get(n);
        if (legacyEntry == null) {
            b.warn("(" + this.a + ") Unknown runtime2Legacy mapping: " + n);
            return new LegacyEntry(0, false, 0);
        }
        return legacyEntry;
    }

    public RuntimeEntry toRuntime(int n, int n2) {
        RuntimeEntry runtimeEntry = (RuntimeEntry)this.legacy2Runtime.get(this.getFullId(n, n2));
        if (runtimeEntry == null) {
            runtimeEntry = (RuntimeEntry)this.legacy2Runtime.get(this.getFullId(n, 0));
        }
        if (runtimeEntry == null) {
            b.warn("(" + this.a + ") Unknown legacy2Runtime mapping: id=" + n + " meta=" + n2);
            runtimeEntry = (RuntimeEntry)this.legacy2Runtime.get(this.getFullId(248, 0));
            if (runtimeEntry == null) {
                throw new RuntimeException("(" + this.a + ") Runtime ID for Item.INFO_UPDATE must exist!");
            }
        }
        return runtimeEntry;
    }

    public Item parseCreativeItem(JsonObject jsonObject, boolean bl) {
        return this.parseCreativeItem(jsonObject, bl, this.a);
    }

    public Item parseCreativeItem(JsonObject jsonObject, boolean bl, int n) {
        int n2;
        String string = jsonObject.get("id").getAsString();
        LegacyEntry legacyEntry = this.fromIdentifier(string);
        if (legacyEntry == null) {
            b.trace("(" + n + ") Can not find legacyEntry for " + string);
            if (!bl) {
                throw new IllegalStateException("(" + n + ") Can not find legacyEntry for " + string);
            }
            return null;
        }
        byte[] byArray = jsonObject.has("nbt_b64") ? Base64.getDecoder().decode(jsonObject.get("nbt_b64").getAsString()) : (jsonObject.has("nbt_hex") ? Utils.parseHexBinary(jsonObject.get("nbt_hex").getAsString()) : new byte[]{});
        int n3 = legacyEntry.getLegacyId();
        int n4 = 0;
        if (jsonObject.has("damage")) {
            n4 = jsonObject.get("damage").getAsInt();
        } else if (legacyEntry.isHasDamage()) {
            n4 = legacyEntry.getDamage();
        } else if (jsonObject.has("blockRuntimeId")) {
            n2 = jsonObject.get("blockRuntimeId").getAsInt();
            int n5 = GlobalBlockPalette.getLegacyFullId(n, n2);
            if (n5 == -1) {
                if (bl) {
                    return null;
                }
                throw new IllegalStateException("(" + n + ") Can not find blockRuntimeId for " + n2);
            }
            n4 = n5 & 0xF;
        }
        n2 = jsonObject.has("count") ? jsonObject.get("count").getAsInt() : 1;
        return Item.get(n3, n4, n2, byArray);
    }

    public LegacyEntry fromIdentifier(String string) {
        return this.identifier2Legacy.get(string);
    }

    public int getFullId(int n, int n2) {
        return (short)n << 16 | (n2 & Short.MAX_VALUE) << 1;
    }

    public byte[] getItemPalette() {
        return this.itemPalette;
    }

    public int getProtocolId() {
        return this.a;
    }

    private static IllegalStateException a(IllegalStateException illegalStateException) {
        return illegalStateException;
    }

    public static class RuntimeEntry {
        private final String b;
        private final int c;
        private final boolean a;

        public RuntimeEntry(String string, int n, boolean bl) {
            this.b = string;
            this.c = n;
            this.a = bl;
        }

        public String getIdentifier() {
            return this.b;
        }

        public int getRuntimeId() {
            return this.c;
        }

        public boolean isHasDamage() {
            return this.a;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof RuntimeEntry)) {
                return false;
            }
            RuntimeEntry runtimeEntry = (RuntimeEntry)object;
            if (!runtimeEntry.canEqual(this)) {
                return false;
            }
            String string = this.getIdentifier();
            String string2 = runtimeEntry.getIdentifier();
            if (string == null ? string2 != null : !string.equals(string2)) {
                return false;
            }
            if (this.getRuntimeId() != runtimeEntry.getRuntimeId()) {
                return false;
            }
            return this.isHasDamage() == runtimeEntry.isHasDamage();
        }

        protected boolean canEqual(Object object) {
            return object instanceof RuntimeEntry;
        }

        public int hashCode() {
            int n = 59;
            int n2 = 1;
            String string = this.getIdentifier();
            n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
            n2 = n2 * 59 + this.getRuntimeId();
            n2 = n2 * 59 + (this.isHasDamage() ? 79 : 97);
            return n2;
        }

        public String toString() {
            return "RuntimeItemMapping.RuntimeEntry(identifier=" + this.getIdentifier() + ", runtimeId=" + this.getRuntimeId() + ", hasDamage=" + this.isHasDamage() + ")";
        }
    }

    public static class LegacyEntry {
        private final int c;
        private final boolean a;
        private final int b;

        public int getDamage() {
            return this.a ? this.b : 0;
        }

        public LegacyEntry(int n, boolean bl, int n2) {
            this.c = n;
            this.a = bl;
            this.b = n2;
        }

        public int getLegacyId() {
            return this.c;
        }

        public boolean isHasDamage() {
            return this.a;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof LegacyEntry)) {
                return false;
            }
            LegacyEntry legacyEntry = (LegacyEntry)object;
            if (!legacyEntry.canEqual(this)) {
                return false;
            }
            if (this.getLegacyId() != legacyEntry.getLegacyId()) {
                return false;
            }
            if (this.isHasDamage() != legacyEntry.isHasDamage()) {
                return false;
            }
            return this.getDamage() == legacyEntry.getDamage();
        }

        protected boolean canEqual(Object object) {
            return object instanceof LegacyEntry;
        }

        public int hashCode() {
            int n = 59;
            int n2 = 1;
            n2 = n2 * 59 + this.getLegacyId();
            n2 = n2 * 59 + (this.isHasDamage() ? 79 : 97);
            n2 = n2 * 59 + this.getDamage();
            return n2;
        }

        public String toString() {
            return "RuntimeItemMapping.LegacyEntry(legacyId=" + this.getLegacyId() + ", hasDamage=" + this.isHasDamage() + ", damage=" + this.getDamage() + ")";
        }
    }
}


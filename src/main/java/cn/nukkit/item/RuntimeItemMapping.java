package cn.nukkit.item;

import cn.nukkit.Server;
import cn.nukkit.item.RuntimeItems.MappingEntry;

import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class RuntimeItemMapping {

    private final int protocolId;

    private final Int2ObjectMap<LegacyEntry> runtime2Legacy = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<RuntimeEntry> legacy2Runtime = new Int2ObjectOpenHashMap<>();
    private final Map<String, LegacyEntry> identifier2Legacy = new HashMap<>();

    private final ArrayList<Integer> customItems = new ArrayList<>();

    private byte[] itemPalette;

    public RuntimeItemMapping(Map<String, MappingEntry> mappings, String itemStatesFile, int protocolId) {
        this.protocolId = protocolId;
        InputStream stream = Server.class.getClassLoader().getResourceAsStream(itemStatesFile);
        if (stream == null) {
            throw new AssertionError("Unable to load " + itemStatesFile);
        }
        JsonArray json = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonArray();

        for (JsonElement element : json) {
            if (!element.isJsonObject()) {
                throw new IllegalStateException("Invalid entry");
            }
            JsonObject entry = element.getAsJsonObject();
            String identifier = entry.get("name").getAsString();
            int runtimeId = entry.get("id").getAsInt();

            if (this.protocolId < ProtocolInfo.v1_16_100) {
                this.registerOldItem(identifier, runtimeId);
                continue;
            }

            boolean hasDamage = false;
            int damage = 0;
            int legacyId;

            if (mappings.containsKey(identifier)) {
                MappingEntry mapping = mappings.get(identifier);
                legacyId = RuntimeItems.getLegacyIdFromLegacyString(mapping.getLegacyName());
                if (legacyId == -1) {
                    throw new IllegalStateException("Unable to match  " + mapping + " with legacyId");
                }
                damage = mapping.getDamage();
                hasDamage = true;
            } else {
                legacyId = RuntimeItems.getLegacyIdFromLegacyString(identifier);
                if (legacyId == -1) {
                    log.trace("Unable to find legacyId for " + identifier);
                    continue;
                }
            }

            int fullId = this.getFullId(legacyId, damage);
            LegacyEntry legacyEntry = new LegacyEntry(legacyId, hasDamage, damage);

            this.runtime2Legacy.put(runtimeId, legacyEntry);
            this.identifier2Legacy.put(identifier, legacyEntry);
            this.legacy2Runtime.put(fullId, new RuntimeEntry(identifier, runtimeId, hasDamage));
        }

        this.generatePalette();
    }

    synchronized boolean registeredCustomItem(ItemCustom itemCustom) {
        if (!Server.getInstance().enableCustomItems || this.customItems.contains(itemCustom.getId())) {
            return false;
        }
        this.customItems.add(itemCustom.getId());

        int fullId = this.getFullId(itemCustom.getId(), 0);

        LegacyEntry legacyEntry = new LegacyEntry(itemCustom.getId(), false, 0);
        this.runtime2Legacy.put(itemCustom.getId(), legacyEntry);
        this.identifier2Legacy.put(itemCustom.getName(), legacyEntry);
        this.legacy2Runtime.put(fullId,
                new RuntimeEntry(itemCustom.getName(), itemCustom.getId(), false, true));

        this.generatePalette();

        return true;
    }

    synchronized boolean deleteCustomItem(ItemCustom itemCustom) {
        if (!Server.getInstance().enableCustomItems && !this.customItems.contains(itemCustom.getId())) {
            return false;
        }

        this.runtime2Legacy.remove(itemCustom.getId());
        this.identifier2Legacy.remove(itemCustom.getName());
        this.legacy2Runtime.remove(this.getFullId(itemCustom.getId(), 0));

        this.generatePalette();

        return true;
    }

    public ArrayList<Integer> getCustomItems() {
        return new ArrayList<>(customItems);
    }

    private void registerOldItem(String identifier, int legacyId) {
        int fullId = this.getFullId(legacyId, 0);
        LegacyEntry legacyEntry = new LegacyEntry(legacyId, false, 0);

        this.runtime2Legacy.put(legacyId, legacyEntry);
        this.identifier2Legacy.put(identifier, legacyEntry);
        this.legacy2Runtime.put(fullId, new RuntimeEntry(identifier, legacyId, false));
    }

    private void generatePalette() {
        BinaryStream paletteBuffer = new BinaryStream();
        paletteBuffer.putUnsignedVarInt(this.legacy2Runtime.size());
        for (RuntimeEntry entry : this.legacy2Runtime.values()) {
            if (entry.isCustomItem()) {
                if (Server.getInstance().enableCustomItems && protocolId >= ProtocolInfo.v1_16_100) {
                    paletteBuffer.putString(("customitem:" + entry.getIdentifier()).toLowerCase());
                    paletteBuffer.putLShort(entry.getRuntimeId());
                    paletteBuffer.putBoolean(true); // Component item
                }
            }else {
                paletteBuffer.putString(entry.getIdentifier());
                paletteBuffer.putLShort(entry.getRuntimeId());
                if (this.protocolId >= ProtocolInfo.v1_16_100) {
                    paletteBuffer.putBoolean(false); // Component item
                }
            }
        }
        this.itemPalette = paletteBuffer.getBuffer();
    }

    public LegacyEntry fromRuntime(int runtimeId) {
        LegacyEntry legacyEntry = this.runtime2Legacy.get(runtimeId);
        if (legacyEntry == null) {
            throw new IllegalArgumentException("Unknown runtime2Legacy mapping: " + runtimeId);
        }
        return legacyEntry;
    }

    public RuntimeEntry toRuntime(int id, int meta) {
        RuntimeEntry runtimeEntry = this.legacy2Runtime.get(this.getFullId(id, meta));
        if (runtimeEntry == null) {
            runtimeEntry = this.legacy2Runtime.get(this.getFullId(id, 0));
        }

        if (runtimeEntry == null) {
            throw new IllegalArgumentException("Unknown legacy2Runtime mapping: id=" + id + " meta=" + meta);
        }
        return runtimeEntry;
    }

    public Item parseCreativeItem(JsonObject json, boolean ignoreUnknown) {
        return this.parseCreativeItem(json, ignoreUnknown, this.protocolId);
    }

    public Item parseCreativeItem(JsonObject json, boolean ignoreUnknown, int protocolId) {
        String identifier = json.get("id").getAsString();
        LegacyEntry legacyEntry = this.fromIdentifier(identifier);
        if (legacyEntry == null) {
            if (!ignoreUnknown) {
                throw new IllegalStateException("Can not find legacyEntry for " + identifier);
            }
            log.trace("Can not find legacyEntry for " + identifier);
            return null;
        }

        byte[] nbtBytes;
        if (json.has("nbt_b64")) {
            nbtBytes = Base64.getDecoder().decode(json.get("nbt_b64").getAsString());
        } else if (json.has("nbt_hex")) {
            nbtBytes = Utils.parseHexBinary(json.get("nbt_hex").getAsString());
        } else {
            nbtBytes = new byte[0];
        }

        int legacyId = legacyEntry.getLegacyId();
        int damage = 0;
        if (json.has("damage")) {
            damage = json.get("damage").getAsInt();
        } else if (legacyEntry.isHasDamage()) {
            damage = legacyEntry.getDamage();
        } else if (json.has("blockRuntimeId")) {
            int runtimeId = json.get("blockRuntimeId").getAsInt();
            int fullId = GlobalBlockPalette.getLegacyFullId(protocolId, runtimeId);
            if (fullId == -1) {
                if (ignoreUnknown) {
                    return null;
                } else {
                    throw new IllegalStateException("Can not find blockRuntimeId for " + runtimeId);
                }
            }

            damage = fullId & 0xf;
        }

        int count = json.has("count") ? json.get("count").getAsInt() : 1;
        return Item.get(legacyId, damage, count, nbtBytes);
    }


    public LegacyEntry fromIdentifier(String identifier) {
        return this.identifier2Legacy.get(identifier);
    }

    public int getFullId(int id, int data) {
        return (((short) id) << 16) | ((data & 0x7fff) << 1);
    }

    public byte[] getItemPalette() {
        return this.itemPalette;
    }

    public int getProtocolId() {
        return this.protocolId;
    }

    @Data
    public static class LegacyEntry {
        private final int legacyId;
        private final boolean hasDamage;
        private final int damage;

        public int getDamage() {
            return this.hasDamage ? this.damage : 0;
        }
    }

    @Data
    public static class RuntimeEntry {
        private final String identifier;
        private final int runtimeId;
        private final boolean hasDamage;
        private final boolean isCustomItem;

        public RuntimeEntry(String identifier, int runtimeId, boolean hasDamage) {
            this(identifier, runtimeId, hasDamage, false);
        }

        public RuntimeEntry(String identifier, int runtimeId, boolean hasDamage, boolean isCustomItem) {
            this.identifier = identifier;
            this.runtimeId = runtimeId;
            this.hasDamage = hasDamage;
            this.isCustomItem = isCustomItem;
        }
    }
}

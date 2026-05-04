package cn.nukkit.item;

import cn.nukkit.Nukkit;
import cn.nukkit.block.BlockID;
import cn.nukkit.item.RuntimeItems.MappingEntry;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.ItemComponentPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Log4j2
public class RuntimeItemMapping {

    private final int protocolId;

    private final Int2ObjectMap<LegacyEntry> runtime2Legacy = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<RuntimeEntry> legacy2Runtime = new Int2ObjectOpenHashMap<>();
    private final Map<String, LegacyEntry> identifier2Legacy = new HashMap<>();

    private byte[] itemPalette;

    private final Map<String, ItemComponentPacket.ItemDefinition> vanillaItems;

    public RuntimeItemMapping(Map<String, MappingEntry> mappings, int protocol) {
        this.protocolId = protocol;

        JsonArray json = Utils.loadJsonResource("runtime_item_states_" + protocol + ".json").getAsJsonArray();
        if (json.isEmpty()) {
            throw new IllegalStateException("Empty array");
        }

        CompoundTag itemComponents = null;
        if (this.protocolId >= ProtocolInfo.v1_21_60) {
            try (InputStream stream = RuntimeItemMapping.class.getClassLoader().getResourceAsStream("item_components_" + this.protocolId + ".nbt")) {
                itemComponents = NBTIO.read(new BufferedInputStream(new GZIPInputStream(stream)), ByteOrder.BIG_ENDIAN, false);
            } catch (Exception e) {
                throw new AssertionError("Error while loading item_components_" + this.protocolId + ".nbt", e);
            }
        }

        this.vanillaItems = new HashMap<>();

        for (JsonElement element : json) {
            if (!element.isJsonObject()) {
                throw new IllegalStateException("(" + protocol + ") Invalid entry");
            }
            JsonObject entry = element.getAsJsonObject();
            String identifier = entry.get("name").getAsString();
            int runtimeId = entry.get("id").getAsInt();

            if (this.protocolId < ProtocolInfo.v1_16_100) {
                this.registerOldItem(identifier, runtimeId);
                continue;
            }

            if (this.protocolId >= ProtocolInfo.v1_21_60) {
                int version = entry.get("version").getAsInt();
                boolean componentBased = entry.get("componentBased").getAsBoolean();
                CompoundTag components = (CompoundTag) itemComponents.get(identifier);
                this.vanillaItems.put(identifier, new ItemComponentPacket.ItemDefinition(identifier, runtimeId, componentBased, version, components));
            }

            boolean hasDamage = false;
            int damage = 0;
            int legacyId;

            MappingEntry mapping;
            if (mappings.containsKey(identifier) && (mapping = mappings.get(identifier)).protocol <= protocol) {
                legacyId = RuntimeItems.getLegacyIdFromLegacyString(mapping.getLegacyName());
                if (legacyId == -1) {
                    throw new IllegalStateException("(" + protocol + ") Unable to match " + mapping + " with legacyId");
                }
                damage = mapping.getDamage();
                hasDamage = true;
            } else {
                legacyId = RuntimeItems.getLegacyIdFromLegacyString(identifier);
                if (legacyId == -1) {
                    if (Nukkit.DEBUG > 2) {
                        log.debug("(" + protocol + ") Unable to find legacyId for " + identifier);
                    }
                    continue;
                }
            }

            this.registerItem(identifier, runtimeId, legacyId, damage, hasDamage);
        }

        this.generatePalette();
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
    }

    public byte[] getItemPalette() {
        return this.itemPalette;
    }

    public int getProtocolId() {
        return this.protocolId;
    }

    public Collection<ItemComponentPacket.ItemDefinition> getVanillaItemDefinitions() {
        return vanillaItems.values();
    }

    public LegacyEntry fromIdentifier(String identifier) {
        return this.identifier2Legacy.get(identifier);
    }

    public LegacyEntry fromRuntime(int runtimeId) {
        LegacyEntry legacyEntry = this.runtime2Legacy.get(runtimeId);
        if (legacyEntry == null) {
            //throw new IllegalArgumentException("(" + protocolId + ") Unknown runtime2Legacy mapping: " + runtimeId);
            if (Nukkit.DEBUG > 1) {
                log.warn("(" + protocolId + ") Unknown runtime2Legacy mapping: " + runtimeId);
            }
            return new LegacyEntry(0, false, 0);
        }
        return legacyEntry;
    }

    public void generatePalette() {
        if (protocolId >= ProtocolInfo.v1_21_60) {
            return; // See getItemDefinitions
        }

        BinaryStream paletteBuffer = new BinaryStream();
        paletteBuffer.putUnsignedVarInt(this.legacy2Runtime.size());
        for (RuntimeEntry entry : this.legacy2Runtime.values()) {
            paletteBuffer.putString(entry.getIdentifier());
            paletteBuffer.putLShort(entry.getRuntimeId());
            if (this.protocolId >= ProtocolInfo.v1_16_100) {
                paletteBuffer.putBoolean(!entry.getIdentifier().startsWith("minecraft:")); // TODO: Component item without bc break
            }
        }
        this.itemPalette = paletteBuffer.getBuffer();
    }

    public int getFullId(int id, int data) {
        return (((short) id) << 16) | ((data & 0x7fff) << 1);
    }

    boolean isSupported(int id, int meta) {
        boolean found = this.legacy2Runtime.containsKey(this.getFullId(id, meta));
        if (!found) {
            found = this.legacy2Runtime.containsKey(this.getFullId(id, 0));
        }
        return found;
    }

    public Item parseCreativeItem(JsonObject json, boolean ignoreUnknown) {
        return this.parseCreativeItem(json, ignoreUnknown, this.protocolId);
    }

    public Item parseCreativeItem(JsonObject json, boolean ignoreUnknown, int protocolId) {
        String identifier = json.get("id").getAsString();
        LegacyEntry legacyEntry = this.fromIdentifier(identifier);
        if (legacyEntry == null) {
            if (!ignoreUnknown) {
                throw new IllegalStateException("(" + protocolId + ") Can not find legacyEntry for " + identifier);
            }
            if (Nukkit.DEBUG > 2) {
                log.debug("(" + protocolId + ") Can not find legacyEntry for " + identifier);
            }
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
        /*} else if (json.has("blockRuntimeId")) {
            int runtimeId = json.get("blockRuntimeId").getAsInt();

            if (runtimeId != 0) {
                int fullId = GlobalBlockPalette.getLegacyFullId(protocolId, runtimeId);
                if (fullId == -1) {
                    if (ignoreUnknown) {
                        return null;
                    } else {
                        throw new IllegalStateException("(" + protocolId + ") Can not find blockRuntimeId for " + identifier + " (" + runtimeId + ")");
                    }
                }

                damage = fullId & Block.DATA_MASK;
            }*/
        }

        // Disabled it for now as it apparently messed up some crafting recipes, mushroom blocks seem to be the only ones requiring manual fix
        if (legacyId == BlockID.RED_MUSHROOM_BLOCK || legacyId == BlockID.BROWN_MUSHROOM_BLOCK) {
            damage = 14;
        }

        int count = json.has("count") ? json.get("count").getAsInt() : 1;
        return Item.get(legacyId, damage, count, nbtBytes);
    }

    public void registerItem(String identifier, int runtimeId, int legacyId, int damage) {
        this.registerItem(identifier, runtimeId, legacyId, damage, false);
    }

    public void registerItem(String identifier, int runtimeId, int legacyId, int damage, boolean hasDamage) {
        int fullId = this.getFullId(legacyId, damage);
        LegacyEntry legacyEntry = new LegacyEntry(legacyId, hasDamage, damage);

        if (Nukkit.DEBUG > 1) {
            if (this.runtime2Legacy.containsKey(runtimeId)) {
                log.warn("RuntimeItemMapping (Protocol " + this.protocolId + "): Registering " + identifier + " but runtime id " + runtimeId + " is already used");
            }
        }

        this.runtime2Legacy.put(runtimeId, legacyEntry);
        this.identifier2Legacy.put(identifier, legacyEntry);
        if (!hasDamage && this.legacy2Runtime.containsKey(fullId)) {
            if (Nukkit.DEBUG > 1) {
                log.debug("RuntimeItemMapping (Protocol " + this.protocolId + ") contains duplicated legacy item state runtimeId=" + runtimeId + " identifier=" + identifier);
            }
        } else {
            this.legacy2Runtime.put(fullId, new RuntimeEntry(identifier, runtimeId, hasDamage));
        }
    }

    private void registerOldItem(String identifier, int legacyId) {
        int fullId = this.getFullId(legacyId, 0);
        LegacyEntry legacyEntry = new LegacyEntry(legacyId, false, 0);

        if (Nukkit.DEBUG > 1) {
            if (this.runtime2Legacy.containsKey(legacyId)) {
                log.warn("RuntimeItemMapping (Legacy): Registering " + identifier + " but runtime id " + legacyId + " is already used");
            }
        }
        this.runtime2Legacy.put(legacyId, legacyEntry);
        this.identifier2Legacy.put(identifier, legacyEntry);
        this.legacy2Runtime.put(fullId, new RuntimeEntry(identifier, legacyId, false));
    }

    public RuntimeEntry toRuntime(int id, int meta) {
        RuntimeEntry runtimeEntry = this.legacy2Runtime.get(this.getFullId(id, meta));
        if (runtimeEntry == null) {
            runtimeEntry = this.legacy2Runtime.get(this.getFullId(id, 0));
        }

        if (runtimeEntry == null) {
            if (Nukkit.DEBUG > 1) {
                log.warn("(" + protocolId + ") Unknown legacy2Runtime mapping: id=" + id + " meta=" + meta);
            }
            runtimeEntry = this.legacy2Runtime.get(this.getFullId(Item.INFO_UPDATE, 0));
            if (runtimeEntry == null)
                throw new RuntimeException("(" + protocolId + ") Runtime ID for Item.INFO_UPDATE must exist!");
            //throw new IllegalArgumentException("(" + protocolId + ") Unknown legacy2Runtime mapping: id=" + id + " meta=" + meta); // Not very multiversion friendly
        }
        return runtimeEntry;
    }
}

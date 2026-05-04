package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@UtilityClass
public class RuntimeItems {

    private static final Map<String, Integer> legacyString2LegacyInt = new HashMap<>();
    private static final Map<Integer, String> legacyInt2LegacyString = new HashMap<>();
    private static final Map<String, MappingEntry> latestIdentifierMapping = new HashMap<>();

    private static RuntimeItemMapping mapping361;
    private static RuntimeItemMapping mapping407;
    private static RuntimeItemMapping mapping419;
    private static RuntimeItemMapping mapping440;
    private static RuntimeItemMapping mapping448;
    private static RuntimeItemMapping mapping475;
    private static RuntimeItemMapping mapping486;
    private static RuntimeItemMapping mapping503;
    private static RuntimeItemMapping mapping527;
    private static RuntimeItemMapping mapping534;
    private static RuntimeItemMapping mapping560;
    private static RuntimeItemMapping mapping567;
    private static RuntimeItemMapping mapping575;
    private static RuntimeItemMapping mapping582;
    private static RuntimeItemMapping mapping589;
    private static RuntimeItemMapping mapping594;
    private static RuntimeItemMapping mapping618;
    private static RuntimeItemMapping mapping630;
    private static RuntimeItemMapping mapping649;
    private static RuntimeItemMapping mapping662;
    private static RuntimeItemMapping mapping671;
    private static RuntimeItemMapping mapping685;
    private static RuntimeItemMapping mapping712;
    private static RuntimeItemMapping mapping729;
    private static RuntimeItemMapping mapping748;
    private static RuntimeItemMapping mapping766;
    private static RuntimeItemMapping mapping776;
    private static RuntimeItemMapping mapping786;
    private static RuntimeItemMapping mapping800;
    private static RuntimeItemMapping mapping818;
    private static RuntimeItemMapping mapping827;
    private static RuntimeItemMapping mapping843;
    private static RuntimeItemMapping mapping859;
    private static RuntimeItemMapping mapping898;
    private static RuntimeItemMapping mapping975;

    // Remember to update item components in CustomItemManager
    // Remember to update recipes in CraftingManager

    public static RuntimeItemMapping[] VALUES;

    private static boolean initialized;

    @Data
    public static class MappingEntry {
        private final String legacyName;
        private final int damage;
        @Getter(value = AccessLevel.NONE)
        @Setter(value = AccessLevel.NONE)
        int protocol;
    }

    public static int getData(int fullId) {
        return ((fullId >> 1) & 0x7fff);
    }

    public static int getFullId(int id, int data) {
        return (((short) id) << 16) | ((data & 0x7fff) << 1);
    }

    public static int getId(int fullId) {
        return (short) (fullId >> 16);
    }

    public static int getLegacyIdFromLegacyString(String identifier) {
        return legacyString2LegacyInt.getOrDefault(identifier, -1);
    }

    public static String getLegacyStringFromLegacyId(int legacyId) {
        return legacyInt2LegacyString.getOrDefault(legacyId, "minecraft:info_update");
    }

    public static String getLegacyStringFromNew(String identifier) {
        MappingEntry entry = latestIdentifierMapping.get(identifier);
        return entry == null ? identifier : entry.getLegacyName();
    }

    public static RuntimeItemMapping getMapping(int protocolId) {
        if (protocolId >= ProtocolInfo.v1_26_20_26) {
            return mapping975;
        } else if (protocolId >= ProtocolInfo.v1_21_130_28) {
            return mapping898;
        } else if (protocolId >= ProtocolInfo.v1_21_120) {
            return mapping859;
        } else if (protocolId >= ProtocolInfo.v1_21_110) {
            return mapping843;
        } else if (protocolId >= ProtocolInfo.v1_21_100) {
            return mapping827;
        } else if (protocolId >= ProtocolInfo.v1_21_90) {
            return mapping818;
        } else if (protocolId >= ProtocolInfo.v1_21_80) {
            return mapping800;
        } else if (protocolId >= ProtocolInfo.v1_21_70_24) {
            return mapping786;
        } else if (protocolId >= ProtocolInfo.v1_21_60) {
            return mapping776;
        } else if (protocolId >= ProtocolInfo.v1_21_50_28) {
            return mapping766;
        } else if (protocolId >= ProtocolInfo.v1_21_40) {
            return mapping748;
        } else if (protocolId >= ProtocolInfo.v1_21_30) {
            return mapping729;
        } else if (protocolId >= ProtocolInfo.v1_21_20) {
            return mapping712;
        } else if (protocolId >= ProtocolInfo.v1_21_0) {
            return mapping685;
        } else if (protocolId >= ProtocolInfo.v1_20_80) {
            return mapping671;
        } else if (protocolId >= ProtocolInfo.v1_20_70) {
            return mapping662;
        } else if (protocolId >= ProtocolInfo.v1_20_60) {
            return mapping649;
        } else if (protocolId >= ProtocolInfo.v1_20_50) {
            return mapping630;
        } else if (protocolId >= ProtocolInfo.v1_20_30) {
            return mapping618;
        } else if (protocolId >= ProtocolInfo.v1_20_10_21) {
            return mapping594;
        } else if (protocolId >= ProtocolInfo.v1_20_0_23) {
            return mapping589;
        } else if (protocolId >= ProtocolInfo.v1_19_80) {
            return mapping582;
        } else if (protocolId >= ProtocolInfo.v1_19_70_24) {
            return mapping575;
        } else if (protocolId >= ProtocolInfo.v1_19_60) {
            return mapping567;
        } else if (protocolId >= ProtocolInfo.v1_19_50) {
            return mapping560;
        } else if (protocolId >= ProtocolInfo.v1_19_10) {
            return mapping534;
        } else if (protocolId >= ProtocolInfo.v1_19_0_29) {
            return mapping527;
        } else if (protocolId >= ProtocolInfo.v1_18_30) {
            return mapping503;
        } else if (protocolId >= ProtocolInfo.v1_18_10_26) {
            return mapping486;
        } else if (protocolId >= ProtocolInfo.v1_18_0) {
            return mapping475;
        } else if (protocolId >= ProtocolInfo.v1_17_10) {
            return mapping448;
        } else if (protocolId >= ProtocolInfo.v1_17_0) {
            return mapping440;
        } else if (protocolId >= ProtocolInfo.v1_16_100) {
            return mapping419;
        } else if (protocolId >= ProtocolInfo.v1_16_0) {
            return mapping407;
        }
        return mapping361;
    }

    public static int getNetworkId(int networkFullId) {
        return networkFullId >> 1;
    }

    public static boolean hasData(int id) {
        return (id & 0x1) != 0;
    }

    public static void init() {
        if (initialized) {
            throw new IllegalStateException("RuntimeItems were already generated!");
        }
        initialized = true;
        log.debug("Loading runtime items...");

        JsonObject json = Utils.loadJsonResource("legacy_item_ids.json").getAsJsonObject();
        for (Map.Entry<String, JsonElement> identifierToId : json.entrySet()) {
            int legacyId = identifierToId.getValue().getAsInt();
            legacyString2LegacyInt.put(identifierToId.getKey(), legacyId);
            legacyInt2LegacyString.put(legacyId, identifierToId.getKey());
        }

        JsonObject itemMapping = Utils.loadJsonResource("item_mappings.json").getAsJsonObject();
        Map<String, MappingEntry> mappingEntries = latestIdentifierMapping; // keep it same with master
        for (String legacyName : itemMapping.keySet()) {
            JsonObject convertData = itemMapping.getAsJsonObject(legacyName);

            int minProtocol = 0;
            JsonElement protocolObj = convertData.get("protocol");
            if (protocolObj != null) {
                minProtocol = protocolObj.getAsInt();
            }

            for (String damageStr : convertData.keySet()) {
                if (damageStr.equals("protocol")) {
                    continue;
                }

                String identifier = convertData.get(damageStr).getAsString();
                int damage = Integer.parseInt(damageStr);
                MappingEntry entry = new MappingEntry(legacyName, damage);
                entry.protocol = minProtocol;
                mappingEntries.put(identifier, entry);
            }
        }

        mapping361 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_12_0);
        mapping407 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_16_0);
        mapping419 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_16_100);
        mapping440 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_17_0);
        mapping448 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_17_10);
        mapping475 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_18_0);
        mapping486 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_18_10);
        mapping503 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_18_30);
        mapping527 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_19_0);
        mapping534 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_19_10);
        mapping560 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_19_50);
        mapping567 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_19_60);
        mapping575 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_19_70);
        mapping582 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_19_80);
        mapping589 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_20_0);
        mapping594 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_20_10);
        mapping618 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_20_30); // Same for 1.20.40 (622)
        mapping630 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_20_50);
        mapping649 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_20_60);
        mapping662 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_20_70);
        mapping671 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_20_80);
        mapping685 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_21_0);
        mapping712 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_21_20);
        mapping729 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_21_30);
        mapping748 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_21_40);
        mapping766 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_21_50);
        mapping776 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_21_60);
        mapping786 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_21_70);
        mapping800 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_21_80);
        mapping818 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_21_90); // Using for 1.21.93 too, update if Record Lava Chicken is needed
        mapping827 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_21_100);
        mapping843 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_21_110);
        mapping859 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_21_120);
        mapping898 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_21_130);
        mapping975 = new RuntimeItemMapping(mappingEntries, ProtocolInfo.v1_26_20);

        // NOTE: Check registerCreativeItems protocol if updating creative_items

        VALUES = new RuntimeItemMapping[]{
                mapping361,
                mapping407,
                mapping419,
                mapping440,
                mapping448,
                mapping475,
                mapping486,
                mapping503,
                mapping527,
                mapping560,
                mapping567,
                mapping575,
                mapping582,
                mapping589,
                mapping594,
                mapping618,
                mapping630,
                mapping649,
                mapping662,
                mapping671,
                mapping685,
                mapping712,
                mapping729,
                mapping748,
                mapping766,
                mapping776,
                mapping786,
                mapping800,
                mapping818,
                mapping827,
                mapping843,
                mapping859,
                mapping898,
                mapping975
        };
    }
}

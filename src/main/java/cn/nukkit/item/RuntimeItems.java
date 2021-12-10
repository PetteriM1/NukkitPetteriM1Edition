package cn.nukkit.item;

import cn.nukkit.Server;
import cn.nukkit.network.protocol.ProtocolInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@UtilityClass
public class RuntimeItems {

    private static final Map<String, Integer> legacyString2LegacyInt = new HashMap<>();

    private static RuntimeItemMapping mapping361;
    private static RuntimeItemMapping mapping419;
    private static RuntimeItemMapping mapping440;
    private static RuntimeItemMapping mapping448;
    private static RuntimeItemMapping mapping475;

    private static boolean initialized;

    public static void init() {
        if (initialized) {
            throw new IllegalStateException("RuntimeItems were already generated!");
        }
        initialized = true;
        log.debug("Loading runtime items...");
        InputStream itemIdsStream = Server.class.getClassLoader().getResourceAsStream("legacy_item_ids.json");
        if (itemIdsStream == null) {
            throw new AssertionError("Unable to load legacy_item_ids.json");
        }

        JsonObject json = JsonParser.parseReader(new InputStreamReader(itemIdsStream)).getAsJsonObject();
        for (String identifier : json.keySet()) {
            legacyString2LegacyInt.put(identifier, json.get(identifier).getAsInt());
        }

        InputStream mappingStream = Server.class.getClassLoader().getResourceAsStream("item_mappings.json");
        if (mappingStream == null) {
            throw new AssertionError("Unable to load item_mappings.json");
        }
        JsonObject itemMapping = JsonParser.parseReader(new InputStreamReader(mappingStream)).getAsJsonObject();

        Map<String, MappingEntry> mappingEntries = new HashMap<>();
        for (String legacyName : itemMapping.keySet()) {
            JsonObject convertData = itemMapping.getAsJsonObject(legacyName);
            for (String damageStr : convertData.keySet()) {
                String identifier = convertData.get(damageStr).getAsString();
                int damage = Integer.parseInt(damageStr);
                mappingEntries.put(identifier, new MappingEntry(legacyName, damage));
            }
        }

        mapping361 = new RuntimeItemMapping(mappingEntries, "runtime_item_states_361.json", ProtocolInfo.v1_12_0);
        mapping419 = new RuntimeItemMapping(mappingEntries, "runtime_item_states_419.json", ProtocolInfo.v1_16_100);
        mapping440 = new RuntimeItemMapping(mappingEntries, "runtime_item_states_440.json", ProtocolInfo.v1_17_0);
        mapping448 = new RuntimeItemMapping(mappingEntries, "runtime_item_states_448.json", ProtocolInfo.v1_17_10);
        mapping475 = new RuntimeItemMapping(mappingEntries, "runtime_item_states_475.json", ProtocolInfo.v1_18_0);
    }

    public static RuntimeItemMapping getMapping(int protocolId) {
        if (protocolId >= ProtocolInfo.v1_18_0) {
            return mapping475;
        } else if (protocolId >= ProtocolInfo.v1_17_10) {
            return mapping448;
        } else if (protocolId >= ProtocolInfo.v1_17_0) {
            return mapping440;
        } else if (protocolId >= ProtocolInfo.v1_16_100) {
            return mapping419;
        }
        return mapping361;
    }

    public static int getLegacyIdFromLegacyString(String identifier) {
        return legacyString2LegacyInt.getOrDefault(identifier, -1);
    }

    @Data
    public static class MappingEntry {
        private final String legacyName;
        private final int damage;
    }

    public static int getId(int fullId) {
        return (short) (fullId >> 16);
    }

    public static int getData(int fullId) {
        return ((fullId >> 1) & 0x7fff);
    }

    public static int getFullId(int id, int data) {
        return (((short) id) << 16) | ((data & 0x7fff) << 1);
    }

    public static int getNetworkId(int networkFullId) {
        return networkFullId >> 1;
    }

    public static boolean hasData(int id) {
        return (id & 0x1) != 0;
    }

    @ToString
    @RequiredArgsConstructor
    static class Entry {
        String name;
        int id;
        Integer oldId;
        Integer oldData;
    }
}

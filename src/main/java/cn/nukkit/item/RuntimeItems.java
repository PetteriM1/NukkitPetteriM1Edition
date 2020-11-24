package cn.nukkit.item;

import cn.nukkit.Server;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

@UtilityClass
public class RuntimeItems {

    private static final Gson GSON = new Gson();
    private static final Type ENTRY_TYPE = new TypeToken<ArrayList<Entry>>() {
    }.getType();

    private static final Int2ObjectMap<RuntimeItemMapping> itemPalettes = new Int2ObjectOpenHashMap<>();

    public static void init() {
        Server.getInstance().getLogger().debug("Loading runtime items...");
        registerMapping(ProtocolInfo.v1_12_0, "runtime_item_ids_361.json");
        registerMapping(ProtocolInfo.v1_16_100, "runtime_item_ids_419.json");
    }

    private static void registerMapping(int protocolId, String fileName) {
        InputStream stream = Server.class.getClassLoader().getResourceAsStream(fileName);
        if (stream == null) {
            throw new AssertionError("Unable to load " + fileName);
        }

        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        Collection<Entry> entries = GSON.fromJson(reader, ENTRY_TYPE);

        BinaryStream paletteBuffer = new BinaryStream();
        paletteBuffer.putUnsignedVarInt(entries.size());

        Int2IntMap legacyNetworkMap = new Int2IntOpenHashMap();
        Int2IntMap networkLegacyMap = new Int2IntOpenHashMap();
        for (Entry entry : entries) {
            paletteBuffer.putString(entry.name);
            paletteBuffer.putLShort(entry.id);

            if (protocolId < ProtocolInfo.v1_16_100) {
                legacyNetworkMap.put(entry.id, entry.id);
                networkLegacyMap.put(entry.id, entry.id);
            } else {
                paletteBuffer.putBoolean(false); // Component item
                if (entry.oldId != null) {
                    boolean hasData = entry.oldData != null;
                    int fullId = getFullId(entry.oldId, hasData ? entry.oldData : 0);
                    legacyNetworkMap.put(fullId, (entry.id << 1) | (hasData ? 1 : 0));
                    networkLegacyMap.put(entry.id, fullId | (hasData ? 1 : 0));
                }
            }
        }

        byte[] itemDataPalette = paletteBuffer.getBuffer();
        itemPalettes.put(protocolId, new RuntimeItemMapping(protocolId, itemDataPalette, legacyNetworkMap, networkLegacyMap));
    }

    public static RuntimeItemMapping getRuntimeMapping(int protocolId) {
        if (protocolId < ProtocolInfo.v1_16_100) {
            return itemPalettes.get(ProtocolInfo.v1_12_0);
        }
        return itemPalettes.get(protocolId);
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

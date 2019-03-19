package cn.nukkit.level;

import cn.nukkit.Server;
import cn.nukkit.utils.BinaryStream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalBlockPalette {

    private static final AtomicInteger runtimeIdAllocator282 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator291 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator313 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator332 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator340 = new AtomicInteger(0);
    private static final Int2IntArrayMap legacyToRuntimeId282 = new Int2IntArrayMap();
    private static final Int2IntArrayMap legacyToRuntimeId291 = new Int2IntArrayMap();
    private static final Int2IntArrayMap legacyToRuntimeId313 = new Int2IntArrayMap();
    private static final Int2IntArrayMap legacyToRuntimeId332 = new Int2IntArrayMap();
    private static final Int2IntArrayMap legacyToRuntimeId340 = new Int2IntArrayMap();
    private static byte[] compiledTable282;
    private static byte[] compiledTable291;
    private static byte[] compiledTable313;
    private static byte[] compiledTable332;
    private static byte[] compiledTable340;

    static {
        legacyToRuntimeId282.defaultReturnValue(-1);
        legacyToRuntimeId291.defaultReturnValue(-1);
        legacyToRuntimeId313.defaultReturnValue(-1);
        legacyToRuntimeId332.defaultReturnValue(-1);
        legacyToRuntimeId340.defaultReturnValue(-1);

        Server.getInstance().getScheduler().scheduleTask(null, () -> {
            // 282
            InputStream stream282 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_282.json");
            if (stream282 == null) throw new AssertionError("Unable to locate RuntimeID table 282");
            Collection<TableEntry> entries282 = new Gson().fromJson(new InputStreamReader(stream282, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
            BinaryStream table282 = new BinaryStream();
            table282.putUnsignedVarInt(entries282.size());
            for (TableEntry entry282 : entries282) {
                registerMapping(282, (entry282.id << 4) | entry282.data);
                table282.putString(entry282.name);
                table282.putLShort(entry282.data);
            }
            compiledTable282 = table282.getBuffer();
            // 291
            InputStream stream291 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_291.json");
            if (stream291 == null) throw new AssertionError("Unable to locate RuntimeID table 291");
            Collection<TableEntry> entries291 = new Gson().fromJson(new InputStreamReader(stream291, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
            BinaryStream table291 = new BinaryStream();
            table291.putUnsignedVarInt(entries291.size());
            for (TableEntry entry291 : entries291) {
                registerMapping(291, (entry291.id << 4) | entry291.data);
                table291.putString(entry291.name);
                table291.putLShort(entry291.data);
            }
            compiledTable291 = table291.getBuffer();
            // 313
            InputStream stream313 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_313.json");
            if (stream313 == null) throw new AssertionError("Unable to locate RuntimeID table 313");
            Collection<TableEntry> entries313 = new Gson().fromJson(new InputStreamReader(stream313, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
            BinaryStream table313 = new BinaryStream();
            table313.putUnsignedVarInt(entries313.size());
            for (TableEntry entry313 : entries313) {
                registerMapping(313, (entry313.id << 4) | entry313.data);
                table313.putString(entry313.name);
                table313.putLShort(entry313.data);
            }
            compiledTable313 = table313.getBuffer();
            // 332
            InputStream stream332 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_332.json");
            if (stream332 == null) throw new AssertionError("Unable to locate RuntimeID table 332");
            Collection<TableEntry> entries332 = new Gson().fromJson(new InputStreamReader(stream332, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
            BinaryStream table332 = new BinaryStream();
            table332.putUnsignedVarInt(entries332.size());
            for (TableEntry entry332 : entries332) {
                registerMapping(332, (entry332.id << 4) | entry332.data);
                table332.putString(entry332.name);
                table332.putLShort(entry332.data);
            }
            compiledTable332 = table332.getBuffer();
            // 340
            InputStream stream340 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_340.json");
            if (stream340 == null) throw new AssertionError("Unable to locate RuntimeID table 340");
            Collection<TableEntry> entries340 = new Gson().fromJson(new InputStreamReader(stream340, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
            BinaryStream table340 = new BinaryStream();
            table340.putUnsignedVarInt(entries340.size());
            for (TableEntry entry340 : entries340) {
                registerMapping(340, (entry340.id << 4) | entry340.data);
                table340.putString(entry340.name);
                table340.putLShort(entry340.data);
            }
            compiledTable340 = table340.getBuffer();
        }, true);
    }

    public static int getOrCreateRuntimeId(int protocol, int id, int meta) {
        return getOrCreateRuntimeId(protocol, (id << 4) | meta);
    }

    public static int getOrCreateRuntimeId(int protocol, int legacyId) {
        switch (protocol) {
            case 281:
            case 282:
                return legacyToRuntimeId282.get(legacyId);
            case 291:
                return legacyToRuntimeId291.get(legacyId);
            case 313:
                return legacyToRuntimeId313.get(legacyId);
            case 332:
                return legacyToRuntimeId332.get(legacyId);
            default: // Current protocol
                return legacyToRuntimeId340.get(legacyId);
        }
    }

    private static void registerMapping(int protocol, int legacyId) {
        switch (protocol) {
            case 281:
            case 282:
                legacyToRuntimeId282.put(legacyId, runtimeIdAllocator282.getAndIncrement());
                break;
            case 291:
                legacyToRuntimeId291.put(legacyId, runtimeIdAllocator291.getAndIncrement());
                break;
            case 313:
                legacyToRuntimeId313.put(legacyId, runtimeIdAllocator313.getAndIncrement());
                break;
            case 332:
                legacyToRuntimeId332.put(legacyId, runtimeIdAllocator332.getAndIncrement());
                break;
            default: // Current protocol
                legacyToRuntimeId340.put(legacyId, runtimeIdAllocator340.getAndIncrement());
                break;
        }
    }

    public static byte[] getCompiledTable(int protocol) {
        switch (protocol) {
            case 281:
            case 282:
                return compiledTable282;
            case 291:
                return compiledTable291;
            case 313:
                return compiledTable313;
            case 332:
                return compiledTable332;
            default: // Current protocol
                return compiledTable340;
        }
    }

    @SuppressWarnings("unused")
    private static class TableEntry {
        private int id;
        private int data;
        private String name;
    }
}

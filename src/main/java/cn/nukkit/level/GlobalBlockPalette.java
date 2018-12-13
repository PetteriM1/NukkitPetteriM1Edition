package cn.nukkit.level;

import cn.nukkit.Server;
import cn.nukkit.utils.BinaryStream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalBlockPalette {

    private static final Int2IntArrayMap legacyToRuntimeId = new Int2IntArrayMap();
    private static final Int2IntArrayMap runtimeIdToLegacy = new Int2IntArrayMap();
    private static final AtomicInteger runtimeIdAllocator = new AtomicInteger(0);
    private static byte[] compiledTable282;
    private static byte[] compiledTable291;
    private static byte[] compiledTable313;

    static {
        legacyToRuntimeId.defaultReturnValue(-1);
        runtimeIdToLegacy.defaultReturnValue(-1);

        Server.getInstance().getScheduler().scheduleTask(null, () -> {
            InputStream stream1 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_282.json");
            if (stream1 == null) {
                throw new AssertionError("Unable to locate RuntimeID table 282");
            }
            Reader reader1 = new InputStreamReader(stream1, StandardCharsets.UTF_8);

            Gson gson1 = new Gson();
            Type collectionType1 = new TypeToken<Collection<TableEntry>>() {
            }.getType();
            Collection<TableEntry> entries1 = gson1.fromJson(reader1, collectionType1);
            BinaryStream table1 = new BinaryStream();

            table1.putUnsignedVarInt(entries1.size());

            for (TableEntry entry1 : entries1) {
                table1.putString(entry1.name);
                table1.putLShort(entry1.data);
            }

            compiledTable282 = table1.getBuffer();

            InputStream stream2 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_291.json");
            if (stream2 == null) {
                throw new AssertionError("Unable to locate RuntimeID table 291");
            }
            Reader reader2 = new InputStreamReader(stream2, StandardCharsets.UTF_8);

            Gson gson2 = new Gson();
            Type collectionType2 = new TypeToken<Collection<TableEntry>>() {
            }.getType();
            Collection<TableEntry> entries2 = gson2.fromJson(reader2, collectionType2);
            BinaryStream table2 = new BinaryStream();

            table2.putUnsignedVarInt(entries2.size());

            for (TableEntry entry2 : entries2) {
                table2.putString(entry2.name);
                table2.putLShort(entry2.data);
            }

            compiledTable291 = table2.getBuffer();

            InputStream stream3 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_313.json");
            if (stream3 == null) {
                throw new AssertionError("Unable to locate RuntimeID table 313");
            }
            Reader reader3 = new InputStreamReader(stream3, StandardCharsets.UTF_8);

            Gson gson3 = new Gson();
            Type collectionType3 = new TypeToken<Collection<TableEntry>>() {
            }.getType();
            Collection<TableEntry> entries3 = gson3.fromJson(reader3, collectionType3);
            BinaryStream table3 = new BinaryStream();

            table3.putUnsignedVarInt(entries3.size());

            for (TableEntry entry3 : entries3) {
                registerMapping((entry3.id << 4) | entry3.data);
                table3.putString(entry3.name);
                table3.putLShort(entry3.data);
            }

            compiledTable313 = table3.getBuffer();
        }, true);
    }

    public static int getOrCreateRuntimeId(int id, int meta) {
        return getOrCreateRuntimeId((id << 4) | meta);
    }

    public static int getOrCreateRuntimeId(int legacyId) {
        int runtimeId = legacyToRuntimeId.get(legacyId);
        return runtimeId;
    }

    private static int registerMapping(int legacyId) {
        int runtimeId = runtimeIdAllocator.getAndIncrement();
        runtimeIdToLegacy.put(runtimeId, legacyId);
        legacyToRuntimeId.put(legacyId, runtimeId);
        return runtimeId;
    }

    public static byte[] getCompiledTable(int protocol) {
        if (protocol < 291) {
            return compiledTable282;
        } else if (protocol > 282 && protocol < 313) {
            return compiledTable291;
        } else {
            return compiledTable313;
        }
    }

    private static class TableEntry {
        private int id;
        private int data;
        private String name;
    }
}

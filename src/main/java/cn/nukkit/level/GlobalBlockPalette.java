package cn.nukkit.level;

import cn.nukkit.Server;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class GlobalBlockPalette {

    private static final AtomicInteger runtimeIdAllocator282 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator291 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator313 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator332 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator340 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator354 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator361 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator388 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator389 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator407 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator408 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator419 = new AtomicInteger(0);
    private static final AtomicInteger runtimeIdAllocator428 = new AtomicInteger(0);
    private static final Int2IntMap legacyToRuntimeId223 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId261 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId274 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId282 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId291 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId313 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId332 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId340 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId354 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId361 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId388 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId389 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId407 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId408 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId419 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId428 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId440 = new Int2IntOpenHashMap();
    private static final Int2IntMap runtimeIdToLegacy428 = new Int2IntOpenHashMap();
    private static final Int2IntMap runtimeIdToLegacy440 = new Int2IntOpenHashMap();
    private static final byte[] compiledTable282;
    private static final byte[] compiledTable291;
    private static final byte[] compiledTable313;
    private static final byte[] compiledTable332;
    private static final byte[] compiledTable340;
    private static final byte[] compiledTable354;
    private static final byte[] compiledTable361;
    private static final byte[] compiledTable388;
    private static final byte[] compiledTable389;
    private static final byte[] compiledTable407;
    private static final byte[] compiledTable408;

    static {
        legacyToRuntimeId223.defaultReturnValue(-1);
        legacyToRuntimeId261.defaultReturnValue(-1);
        legacyToRuntimeId274.defaultReturnValue(-1);
        legacyToRuntimeId282.defaultReturnValue(-1);
        legacyToRuntimeId291.defaultReturnValue(-1);
        legacyToRuntimeId313.defaultReturnValue(-1);
        legacyToRuntimeId332.defaultReturnValue(-1);
        legacyToRuntimeId340.defaultReturnValue(-1);
        legacyToRuntimeId354.defaultReturnValue(-1);
        legacyToRuntimeId361.defaultReturnValue(-1);
        legacyToRuntimeId388.defaultReturnValue(-1);
        legacyToRuntimeId389.defaultReturnValue(-1);
        legacyToRuntimeId407.defaultReturnValue(-1);
        legacyToRuntimeId408.defaultReturnValue(-1);
        legacyToRuntimeId419.defaultReturnValue(-1);
        legacyToRuntimeId428.defaultReturnValue(-1);
        legacyToRuntimeId440.defaultReturnValue(-1);
        runtimeIdToLegacy428.defaultReturnValue(-1);
        runtimeIdToLegacy440.defaultReturnValue(-1);

        Server.getInstance().getLogger().debug("Loading block palette...");
        // 223
        InputStream stream223 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_223.json");
        if (stream223 == null) throw new AssertionError("Unable to locate RuntimeID table 223");
        Collection<TableEntryOld> entries223 = new Gson().fromJson(new InputStreamReader(stream223, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntryOld>>(){}.getType());
        for (TableEntryOld entry : entries223) {
            legacyToRuntimeId223.put((entry.id << 4) | entry.data, entry.runtimeID);
        }
        // Compiled table not needed for 223
        // 261
        InputStream stream261 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_261.json");
        if (stream261 == null) throw new AssertionError("Unable to locate RuntimeID table 261");
        Collection<TableEntryOld> entries261 = new Gson().fromJson(new InputStreamReader(stream261, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntryOld>>(){}.getType());
        for (TableEntryOld entry : entries261) {
            legacyToRuntimeId261.put((entry.id << 4) | entry.data, entry.runtimeID);
        }
        // Compiled table not needed 261
        // 274
        InputStream stream274 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_274.json");
        if (stream274 == null) throw new AssertionError("Unable to locate RuntimeID table 274");
        Collection<TableEntryOld> entries274 = new Gson().fromJson(new InputStreamReader(stream274, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntryOld>>(){}.getType());
        for (TableEntryOld entry : entries274) {
            legacyToRuntimeId274.put((entry.id << 4) | entry.data, entry.runtimeID);
        }
        // Compiled table not needed 274
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
        // 354
        InputStream stream354 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_354.json");
        if (stream354 == null) throw new AssertionError("Unable to locate RuntimeID table 354");
        Collection<TableEntry> entries354 = new Gson().fromJson(new InputStreamReader(stream354, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
        BinaryStream table354 = new BinaryStream();
        table354.putUnsignedVarInt(entries354.size());
        for (TableEntry entry354 : entries354) {
            registerMapping(354, (entry354.id << 4) | entry354.data);
            table354.putString(entry354.name);
            table354.putLShort(entry354.data);
        }
        compiledTable354 = table354.getBuffer();
        // 361
        InputStream stream361 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_361.json");
        if (stream361 == null) throw new AssertionError("Unable to locate RuntimeID table 361");
        Collection<TableEntry> entries361 = new Gson().fromJson(new InputStreamReader(stream361, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
        BinaryStream table361 = new BinaryStream();
        table361.putUnsignedVarInt(entries361.size());
        for (TableEntry entry361 : entries361) {
            registerMapping(361, (entry361.id << 4) | entry361.data);
            table361.putString(entry361.name);
            table361.putLShort(entry361.data);
            table361.putLShort(entry361.id);
        }
        compiledTable361 = table361.getBuffer();
        // 388
        InputStream stream388 = Server.class.getClassLoader().getResourceAsStream("runtime_block_states_388.dat");
        if (stream388 == null) throw new AssertionError("Unable to locate block state nbt 388");
        ListTag<CompoundTag> tag388;
        try {
            compiledTable388 = ByteStreams.toByteArray(stream388);
            //noinspection unchecked
            tag388 = (ListTag<CompoundTag>) NBTIO.readNetwork(new ByteArrayInputStream(compiledTable388));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        for (CompoundTag state388 : tag388.getAll()) {
            int runtimeId = runtimeIdAllocator388.getAndIncrement();
            if (!state388.contains("meta")) continue;
            for (int val : state388.getIntArray("meta")) {
                legacyToRuntimeId388.put(state388.getShort("id") << 6 | val, runtimeId);
            }
            state388.remove("meta");
        }
        // 389
        InputStream stream389 = Server.class.getClassLoader().getResourceAsStream("runtime_block_states_389.dat");
        if (stream389 == null) throw new AssertionError("Unable to locate block state nbt 389");
        ListTag<CompoundTag> tag389;
        try {
            //noinspection unchecked
            tag389 = (ListTag<CompoundTag>) NBTIO.readTag(stream389, ByteOrder.LITTLE_ENDIAN, false);
        } catch (IOException e) {
            throw new AssertionError("Unable to load block palette 389", e);
        }
        for (CompoundTag state : tag389.getAll()) {
            int runtimeId = runtimeIdAllocator389.getAndIncrement();
            if (!state.contains("meta")) continue;
            for (int val : state.getIntArray("meta")) {
                legacyToRuntimeId389.put(state.getShort("id") << 6 | val, runtimeId);
            }
            state.remove("meta");
        }
        try {
            compiledTable389 = NBTIO.write(tag389, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new AssertionError("Unable to write block palette 389", e);
        }
        // 407
        ListTag<CompoundTag> tag407;
        try (InputStream stream407 = Server.class.getClassLoader().getResourceAsStream("runtime_block_states_407.dat")) {
            if (stream407 == null) {
                throw new AssertionError("Unable to locate block state nbt 407");
            }
            //noinspection unchecked
            tag407 = (ListTag<CompoundTag>) NBTIO.readTag(new ByteArrayInputStream(ByteStreams.toByteArray(stream407)), ByteOrder.LITTLE_ENDIAN, false);
        } catch (IOException e) {
            throw new AssertionError("Unable to load block palette 407", e);
        }
        for (CompoundTag state : tag407.getAll()) {
            int runtimeId = runtimeIdAllocator407.getAndIncrement();
            if (!state.contains("LegacyStates")) continue;
            List<CompoundTag> legacyStates = state.getList("LegacyStates", CompoundTag.class).getAll();
            for (CompoundTag legacyState : legacyStates) {
                int legacyId = legacyState.getInt("id") << 6 | legacyState.getShort("val");
                legacyToRuntimeId407.put(legacyId, runtimeId);
            }
            state.remove("meta");
        }
        try {
            compiledTable407 = NBTIO.write(tag407, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new AssertionError("Unable to write block palette 407", e);
        }
        // 408
        ListTag<CompoundTag> tag408;
        try (InputStream stream408 = Server.class.getClassLoader().getResourceAsStream("runtime_block_states_408.dat")) {
            if (stream408 == null) {
                throw new AssertionError("Unable to locate block state nbt 408");
            }
            //noinspection unchecked
            tag408 = (ListTag<CompoundTag>) NBTIO.readTag(new ByteArrayInputStream(ByteStreams.toByteArray(stream408)), ByteOrder.LITTLE_ENDIAN, false);
        } catch (IOException e) {
            throw new AssertionError("Unable to load block palette 408", e);
        }
        for (CompoundTag state : tag408.getAll()) {
            int runtimeId = runtimeIdAllocator408.getAndIncrement();
            if (!state.contains("LegacyStates")) continue;
            List<CompoundTag> legacyStates = state.getList("LegacyStates", CompoundTag.class).getAll();
            for (CompoundTag legacyState : legacyStates) {
                int legacyId = legacyState.getInt("id") << 6 | legacyState.getShort("val");
                legacyToRuntimeId408.put(legacyId, runtimeId);
            }
            state.remove("meta");
        }
        try {
            compiledTable408 = NBTIO.write(tag408, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new AssertionError("Unable to write block palette 408", e);
        }
        // 419
        ListTag<CompoundTag> tag419;
        try (InputStream stream419 = Server.class.getClassLoader().getResourceAsStream("runtime_block_states_419.dat")) {
            if (stream419 == null) {
                throw new AssertionError("Unable to locate block state nbt 419");
            }
            //noinspection unchecked
            tag419 = (ListTag<CompoundTag>) NBTIO.readTag(new ByteArrayInputStream(ByteStreams.toByteArray(stream419)), ByteOrder.LITTLE_ENDIAN, false);
        } catch (IOException e) {
            throw new AssertionError("Unable to load block palette 419", e);
        }
        for (CompoundTag state : tag419.getAll()) {
            int runtimeId = runtimeIdAllocator419.getAndIncrement();
            if (!state.contains("LegacyStates")) continue;
            List<CompoundTag> legacyStates = state.getList("LegacyStates", CompoundTag.class).getAll();
            for (CompoundTag legacyState : legacyStates) {
                int legacyId = legacyState.getInt("id") << 6 | legacyState.getShort("val");
                legacyToRuntimeId419.put(legacyId, runtimeId);
            }
        }
        // 428
        ListTag<CompoundTag> tag428;
        try (InputStream stream428 = Server.class.getClassLoader().getResourceAsStream("runtime_block_states_428.dat")) {
            if (stream428 == null) {
                throw new AssertionError("Unable to locate block state nbt 428");
            }
            //noinspection unchecked
            tag428 = (ListTag<CompoundTag>) NBTIO.readTag(new ByteArrayInputStream(ByteStreams.toByteArray(stream428)), ByteOrder.LITTLE_ENDIAN, false);
        } catch (IOException e) {
            throw new AssertionError("Unable to load block palette 428", e);
        }
        for (CompoundTag state : tag428.getAll()) {
            int runtimeId = runtimeIdAllocator428.getAndIncrement();
            if (!state.contains("LegacyStates")) continue;
            List<CompoundTag> legacyStates = state.getList("LegacyStates", CompoundTag.class).getAll();
            for (CompoundTag legacyState : legacyStates) {
                int legacyId = legacyState.getInt("id") << 6 | legacyState.getShort("val");
                legacyToRuntimeId428.put(legacyId, runtimeId);
                runtimeIdToLegacy428.put(runtimeId, legacyId);
            }
        }
        // 440
        ListTag<CompoundTag> tag440;
        try (InputStream stream440 = Server.class.getClassLoader().getResourceAsStream("runtime_block_states_440.dat")) {
            if (stream440 == null) {
                throw new AssertionError("Unable to locate block state nbt 440");
            }
            //noinspection unchecked
            tag440 = (ListTag<CompoundTag>) NBTIO.readTag(new ByteArrayInputStream(ByteStreams.toByteArray(stream440)), ByteOrder.BIG_ENDIAN, false);
        } catch (IOException e) {
            throw new AssertionError("Unable to load block palette 440", e);
        }
        for (CompoundTag state : tag440.getAll()) {
            int id = state.getInt("id");
            int data = state.getShort("data");
            int runtimeId = state.getInt("runtimeId");
            int legacyId = id << 6 | data;
            legacyToRuntimeId440.put(legacyId, runtimeId);
            runtimeIdToLegacy440.put(runtimeId, legacyId);
        }
    }

    public static int getOrCreateRuntimeId(int protocol, int id, int meta) {
        int legacyId = protocol >= 388 ? ((id << 6) | meta) : ((id << 4) | meta);
        switch (protocol) {
            // Versions before this doesn't use runtime IDs
            case 223:
            case 224:
                return legacyToRuntimeId223.get(legacyId);
            case 261:
                return legacyToRuntimeId261.get(legacyId);
            case 274:
                return legacyToRuntimeId274.get(legacyId);
            case 281:
            case 282:
                return legacyToRuntimeId282.get(legacyId);
            case 291:
                return legacyToRuntimeId291.get(legacyId);
            case 313:
                return legacyToRuntimeId313.get(legacyId);
            case 332:
                return legacyToRuntimeId332.get(legacyId);
            case 340:
                return legacyToRuntimeId340.get(legacyId);
            case 354:
                return legacyToRuntimeId354.get(legacyId);
            case 361:
                return legacyToRuntimeId361.get(legacyId);
            case 388:
                return legacyToRuntimeId388.get(legacyId);
            case 389:
            case 390:
                int id389 = legacyToRuntimeId389.get(legacyId);
                if (id389 == -1) {
                    id389 = legacyToRuntimeId389.get(id << 6);
                    if (id389 == -1) {
                        log.info("(389) Creating new runtime ID for unknown block {}", id);
                        id389 = runtimeIdAllocator389.getAndIncrement();
                        legacyToRuntimeId389.put(id << 6, id389);
                    }
                }
                return id389;
            case 407:
                int id407 = legacyToRuntimeId407.get(legacyId);
                if (id407 == -1) {
                    id407 = legacyToRuntimeId407.get(id << 6);
                    if (id407 == -1) {
                        log.info("(407) Creating new runtime ID for unknown block {}", id);
                        id407 = runtimeIdAllocator407.getAndIncrement();
                        legacyToRuntimeId407.put(id << 6, id407);
                    }
                }
                return id407;
            case 408:
            case 409:
            case 410:
            case 411:
                int id408 = legacyToRuntimeId408.get(legacyId);
                if (id408 == -1) {
                    id408 = legacyToRuntimeId408.get(id << 6);
                    if (id408 == -1) {
                        log.info("(408) Creating new runtime ID for unknown block {}", id);
                        id408 = runtimeIdAllocator408.getAndIncrement();
                        legacyToRuntimeId408.put(id << 6, id408);
                    }
                }
                return id408;
            case 419:
            case 420:
            case 422:
            case ProtocolInfo.v1_16_210_50:
                int id419 = legacyToRuntimeId419.get(legacyId);
                if (id419 == -1) {
                    id419 = legacyToRuntimeId419.get(id << 6);
                    if (id419 == -1) {
                        log.info("(419) Creating new runtime ID for unknown block {}", id);
                        id419 = runtimeIdAllocator419.getAndIncrement();
                        legacyToRuntimeId419.put(id << 6, id419);
                    }
                }
                return id419;
            case ProtocolInfo.v1_16_210:
            case ProtocolInfo.v1_16_220:
            case ProtocolInfo.v1_16_230_50:
            case ProtocolInfo.v1_16_230:
            case ProtocolInfo.v1_16_230_54:
                int id428 = legacyToRuntimeId428.get(legacyId);
                if (id428 == -1) {
                    id428 = legacyToRuntimeId428.get(id << 6);
                    if (id428 == -1) {
                        log.info("(428) Creating new runtime ID for unknown block {}", id);
                        id428 = runtimeIdAllocator428.getAndIncrement();
                        legacyToRuntimeId428.put(id << 6, id428);
                    }
                }
                return id428;
            case ProtocolInfo.v1_17_0:
                int id440 = legacyToRuntimeId440.get(legacyId);
                if (id440 == -1) {
                    id440 = legacyToRuntimeId440.get(id << 6);
                    if (id440 == -1) {
                        log.info("(440) Missing runtime id mappings for " + id + ':' + meta);
                        return 5035; // Update game block
                    }
                }
                return id440;
            default:
                throw new IllegalArgumentException("Tried to get block runtime id for unsupported protocol version: " + protocol);
        }
    }

    private static void registerMapping(int protocol, int legacyId) {
        switch (protocol) { // NOTE: Not all versions are supposed to be here
            // 223, 261 and 274 registered directly on read
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
            case 340:
                legacyToRuntimeId340.put(legacyId, runtimeIdAllocator340.getAndIncrement());
                break;
            case 354:
                legacyToRuntimeId354.put(legacyId, runtimeIdAllocator354.getAndIncrement());
                break;
            case 361:
                legacyToRuntimeId361.put(legacyId, runtimeIdAllocator361.getAndIncrement());
                break;
            default: // Not used for 388+
                throw new IllegalArgumentException("Tried to register mapping for unsupported protocol version: " + protocol);
        }
    }

    public static byte[] getCompiledTable(int protocol) {
        switch (protocol) {
            // Versions before this doesn't send compiled table in StartGamePacket
            case 281:
            case 282:
                return compiledTable282;
            case 291:
                return compiledTable291;
            case 313:
                return compiledTable313;
            case 332:
                return compiledTable332;
            case 340:
                return compiledTable340;
            case 354:
                return compiledTable354;
            case 361:
                return compiledTable361;
            case 388:
                return compiledTable388;
            case 389:
            case 390:
                return compiledTable389;
            case 407:
                return compiledTable407;
            case 408:
            case 409:
            case 410:
            case 411:
                return compiledTable408;
            default: // Unused since 1.16.100 (419)
                throw new IllegalArgumentException("Tried to get compiled runtime id table for unsupported protocol version: " + protocol);
        }
    }

    public static int getOrCreateRuntimeId(int protocol, int legacyId) throws NoSuchElementException {
        switch (protocol) {
            // Versions before this doesn't use runtime IDs
            case 223:
            case 224:
                return legacyToRuntimeId223.get(legacyId);
            case 261:
                return legacyToRuntimeId261.get(legacyId);
            case 274:
                return legacyToRuntimeId274.get(legacyId);
            case 281:
            case 282:
                return legacyToRuntimeId282.get(legacyId);
            case 291:
                return legacyToRuntimeId291.get(legacyId);
            case 313:
                return legacyToRuntimeId313.get(legacyId);
            case 332:
                return legacyToRuntimeId332.get(legacyId);
            case 340:
                return legacyToRuntimeId340.get(legacyId);
            case 354:
                return legacyToRuntimeId354.get(legacyId);
            case 361:
                return legacyToRuntimeId361.get(legacyId);
            default: // 388+
                return getOrCreateRuntimeId(protocol, legacyId >> 4, legacyId & 0xf);
        }
    }

    public static int getLegacyFullId(int protocolId, int runtimeId) {
        if (protocolId < ProtocolInfo.v1_17_0) {
            return runtimeIdToLegacy428.get(runtimeId);
        } else {
            return runtimeIdToLegacy440.get(runtimeId);
        }
    }

    @SuppressWarnings("unused")
    private static class TableEntry {
        private int id;
        private int data;
        private String name;
    }

    @SuppressWarnings("unused")
    private static class TableEntryOld {
        private int id;
        private int data;
        private int runtimeID;
        private String name;
    }
}

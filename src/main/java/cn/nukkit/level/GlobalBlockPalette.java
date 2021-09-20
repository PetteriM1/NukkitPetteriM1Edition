package cn.nukkit.level;

import cn.nukkit.Server;
import cn.nukkit.block.BlockID;
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
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class GlobalBlockPalette {

    private static final Gson GSON = new Gson();
    private static boolean initialized;

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
    private static final Int2IntMap legacyToRuntimeId419 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId428 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId440 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId448 = new Int2IntOpenHashMap();
    private static final Int2IntMap runtimeIdToLegacy428 = new Int2IntOpenHashMap();
    private static final Int2IntMap runtimeIdToLegacy440 = new Int2IntOpenHashMap();
    private static final Int2IntMap runtimeIdToLegacy448 = new Int2IntOpenHashMap();
    private static byte[] compiledTable282;
    private static byte[] compiledTable291;
    private static byte[] compiledTable313;
    private static byte[] compiledTable332;
    private static byte[] compiledTable340;
    private static byte[] compiledTable354;
    private static byte[] compiledTable361;
    private static byte[] compiledTable388;
    private static byte[] compiledTable389;
    private static byte[] compiledTable407;

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
        legacyToRuntimeId419.defaultReturnValue(-1);
        legacyToRuntimeId428.defaultReturnValue(-1);
        legacyToRuntimeId440.defaultReturnValue(-1);
        legacyToRuntimeId448.defaultReturnValue(-1);
        runtimeIdToLegacy428.defaultReturnValue(-1);
        runtimeIdToLegacy440.defaultReturnValue(-1);
        runtimeIdToLegacy448.defaultReturnValue(-1);
    }

    public static void init() {
        if (initialized) {
            throw new IllegalStateException("BlockPalette was already generated!");
        }
        initialized = true;
        log.debug("Loading block palette...");

        // 223
        InputStream stream223 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_223.json");
        if (stream223 == null) throw new AssertionError("Unable to locate RuntimeID table 223");
        Collection<TableEntryOld> entries223 = GSON.fromJson(new InputStreamReader(stream223, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntryOld>>(){}.getType());
        for (TableEntryOld entry : entries223) {
            legacyToRuntimeId223.put((entry.id << 4) | entry.data, entry.runtimeID);
        }
        // Compiled table not needed for 223
        // 261
        InputStream stream261 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_261.json");
        if (stream261 == null) throw new AssertionError("Unable to locate RuntimeID table 261");
        Collection<TableEntryOld> entries261 = GSON.fromJson(new InputStreamReader(stream261, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntryOld>>(){}.getType());
        for (TableEntryOld entry : entries261) {
            legacyToRuntimeId261.put((entry.id << 4) | entry.data, entry.runtimeID);
        }
        // Compiled table not needed 261
        // 274
        InputStream stream274 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_274.json");
        if (stream274 == null) throw new AssertionError("Unable to locate RuntimeID table 274");
        Collection<TableEntryOld> entries274 = GSON.fromJson(new InputStreamReader(stream274, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntryOld>>(){}.getType());
        for (TableEntryOld entry : entries274) {
            legacyToRuntimeId274.put((entry.id << 4) | entry.data, entry.runtimeID);
        }
        // Compiled table not needed 274
        // 282
        InputStream stream282 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_282.json");
        if (stream282 == null) throw new AssertionError("Unable to locate RuntimeID table 282");
        Collection<TableEntry> entries282 = GSON.fromJson(new InputStreamReader(stream282, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
        BinaryStream table282 = new BinaryStream();
        table282.putUnsignedVarInt(entries282.size());
        for (TableEntry entry : entries282) {
            legacyToRuntimeId282.put((entry.id << 4) | entry.data, runtimeIdAllocator282.getAndIncrement());
            table282.putString(entry.name);
            table282.putLShort(entry.data);
        }
        compiledTable282 = table282.getBuffer();
        // 291
        InputStream stream291 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_291.json");
        if (stream291 == null) throw new AssertionError("Unable to locate RuntimeID table 291");
        Collection<TableEntry> entries291 = GSON.fromJson(new InputStreamReader(stream291, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
        BinaryStream table291 = new BinaryStream();
        table291.putUnsignedVarInt(entries291.size());
        for (TableEntry entry : entries291) {
            legacyToRuntimeId291.put((entry.id << 4) | entry.data, runtimeIdAllocator291.getAndIncrement());
            table291.putString(entry.name);
            table291.putLShort(entry.data);
        }
        compiledTable291 = table291.getBuffer();
        // 313
        InputStream stream313 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_313.json");
        if (stream313 == null) throw new AssertionError("Unable to locate RuntimeID table 313");
        Collection<TableEntry> entries313 = GSON.fromJson(new InputStreamReader(stream313, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
        BinaryStream table313 = new BinaryStream();
        table313.putUnsignedVarInt(entries313.size());
        for (TableEntry entry : entries313) {
            legacyToRuntimeId313.put((entry.id << 4) | entry.data, runtimeIdAllocator313.getAndIncrement());
            table313.putString(entry.name);
            table313.putLShort(entry.data);
        }
        compiledTable313 = table313.getBuffer();
        // 332
        InputStream stream332 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_332.json");
        if (stream332 == null) throw new AssertionError("Unable to locate RuntimeID table 332");
        Collection<TableEntry> entries332 = GSON.fromJson(new InputStreamReader(stream332, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
        BinaryStream table332 = new BinaryStream();
        table332.putUnsignedVarInt(entries332.size());
        for (TableEntry entry : entries332) {
            legacyToRuntimeId332.put((entry.id << 4) | entry.data, runtimeIdAllocator332.getAndIncrement());
            table332.putString(entry.name);
            table332.putLShort(entry.data);
        }
        compiledTable332 = table332.getBuffer();
        // 340
        InputStream stream340 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_340.json");
        if (stream340 == null) throw new AssertionError("Unable to locate RuntimeID table 340");
        Collection<TableEntry> entries340 = GSON.fromJson(new InputStreamReader(stream340, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
        BinaryStream table340 = new BinaryStream();
        table340.putUnsignedVarInt(entries340.size());
        for (TableEntry entry : entries340) {
            legacyToRuntimeId340.put((entry.id << 4) | entry.data, runtimeIdAllocator340.getAndIncrement());
            table340.putString(entry.name);
            table340.putLShort(entry.data);
        }
        compiledTable340 = table340.getBuffer();
        // 354
        InputStream stream354 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_354.json");
        if (stream354 == null) throw new AssertionError("Unable to locate RuntimeID table 354");
        Collection<TableEntry> entries354 = GSON.fromJson(new InputStreamReader(stream354, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
        BinaryStream table354 = new BinaryStream();
        table354.putUnsignedVarInt(entries354.size());
        for (TableEntry entry : entries354) {
            legacyToRuntimeId354.put((entry.id << 4) | entry.data, runtimeIdAllocator354.getAndIncrement());
            table354.putString(entry.name);
            table354.putLShort(entry.data);
        }
        compiledTable354 = table354.getBuffer();
        // 361
        InputStream stream361 = Server.class.getClassLoader().getResourceAsStream("runtimeid_table_361.json");
        if (stream361 == null) throw new AssertionError("Unable to locate RuntimeID table 361");
        Collection<TableEntry> entries361 = GSON.fromJson(new InputStreamReader(stream361, StandardCharsets.UTF_8), new TypeToken<Collection<TableEntry>>(){}.getType());
        BinaryStream table361 = new BinaryStream();
        table361.putUnsignedVarInt(entries361.size());
        for (TableEntry entry : entries361) {
            legacyToRuntimeId361.put((entry.id << 4) | entry.data, runtimeIdAllocator361.getAndIncrement());
            table361.putString(entry.name);
            table361.putLShort(entry.data);
            table361.putLShort(entry.id);
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
        for (CompoundTag state : tag388.getAll()) {
            int runtimeId = runtimeIdAllocator388.getAndIncrement();
            if (!state.contains("meta")) continue;
            for (int val : state.getIntArray("meta")) {
                legacyToRuntimeId388.put(state.getShort("id") << 6 | val, runtimeId);
            }
            state.remove("meta");
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
            tag407 = (ListTag<CompoundTag>) NBTIO.readTag(new ByteArrayInputStream(ByteStreams.toByteArray(stream407)), ByteOrder.BIG_ENDIAN, false);
        } catch (IOException e) {
            throw new AssertionError("Unable to load block palette 407", e);
        }
        for (CompoundTag state : tag407.getAll()) {
            int id = state.getInt("id");
            int data = state.getShort("data");
            int runtimeId = runtimeIdAllocator407.getAndIncrement();
            int legacyId = id << 6 | data;
            legacyToRuntimeId407.put(legacyId, runtimeId);
            state.remove("data");
        }
        try {
            compiledTable407 = NBTIO.write(tag407, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new AssertionError("Unable to write block palette 407", e);
        }
        // 419
        ListTag<CompoundTag> tag419;
        try (InputStream stream419 = Server.class.getClassLoader().getResourceAsStream("runtime_block_states_419.dat")) {
            if (stream419 == null) {
                throw new AssertionError("Unable to locate block state nbt 419");
            }
            //noinspection unchecked
            tag419 = (ListTag<CompoundTag>) NBTIO.readTag(new ByteArrayInputStream(ByteStreams.toByteArray(stream419)), ByteOrder.BIG_ENDIAN, false);
        } catch (IOException e) {
            throw new AssertionError("Unable to load block palette 419", e);
        }
        for (CompoundTag state : tag419.getAll()) {
            int id = state.getInt("id");
            int data = state.getShort("data");
            int runtimeId = state.getInt("runtimeId");
            int legacyId = id << 6 | data;
            legacyToRuntimeId419.put(legacyId, runtimeId);
        }
        // 428
        ListTag<CompoundTag> tag428;
        try (InputStream stream428 = Server.class.getClassLoader().getResourceAsStream("runtime_block_states_428.dat")) {
            if (stream428 == null) {
                throw new AssertionError("Unable to locate block state nbt 428");
            }
            //noinspection unchecked
            tag428 = (ListTag<CompoundTag>) NBTIO.readTag(new ByteArrayInputStream(ByteStreams.toByteArray(stream428)), ByteOrder.BIG_ENDIAN, false);
        } catch (IOException e) {
            throw new AssertionError("Unable to load block palette 428", e);
        }
        loadBlockStates(tag428, legacyToRuntimeId428, runtimeIdToLegacy428);
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
        loadBlockStates(tag440, legacyToRuntimeId440, runtimeIdToLegacy440);
        // 448
        ListTag<CompoundTag> tag448;
        try (InputStream stream448 = Server.class.getClassLoader().getResourceAsStream("runtime_block_states_448.dat")) {
            if (stream448 == null) {
                throw new AssertionError("Unable to locate block state nbt 448");
            }
            //noinspection unchecked
            tag448 = (ListTag<CompoundTag>) NBTIO.readTag(new ByteArrayInputStream(ByteStreams.toByteArray(stream448)), ByteOrder.BIG_ENDIAN, false);
        } catch (IOException e) {
            throw new AssertionError("Unable to load block palette 448", e);
        }
        loadBlockStates(tag448, legacyToRuntimeId448, runtimeIdToLegacy448);
    }

    private static void loadBlockStates(ListTag<CompoundTag> blockStates, Int2IntMap legacyToRuntime, Int2IntMap runtimeIdToLegacy) {
        for (CompoundTag state : blockStates.getAll()) {
            int id = state.getInt("id");
            int data = state.getShort("data");
            int runtimeId = state.getInt("runtimeId");
            int legacyId = id << 6 | data;
            legacyToRuntime.put(legacyId, runtimeId);
            if (!runtimeIdToLegacy.containsKey(runtimeId)) {
                runtimeIdToLegacy.put(runtimeId, legacyId);
            }
        }
    }

    public static int getOrCreateRuntimeId(int protocol, int id, int meta) {
        if (protocol < 223) throw new IllegalArgumentException("Tried to get block runtime id for unsupported protocol version: " + protocol);
        int legacyId = protocol >= 388 ? ((id << 6) | meta) : ((id << 4) | meta);
        int runtimeId;
        switch (protocol) {
            // Versions before this doesn't use runtime IDs
            case 223:
            case 224:
                runtimeId = legacyToRuntimeId223.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId223.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 261:
                runtimeId = legacyToRuntimeId261.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId261.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 274:
                runtimeId = legacyToRuntimeId274.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId274.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 281:
            case 282:
                runtimeId = legacyToRuntimeId282.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId282.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 291:
                runtimeId = legacyToRuntimeId291.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId291.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 313:
                runtimeId = legacyToRuntimeId313.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId313.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 332:
                runtimeId = legacyToRuntimeId332.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId332.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 340:
                runtimeId = legacyToRuntimeId340.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId340.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 354:
                runtimeId = legacyToRuntimeId354.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId354.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 361:
                runtimeId = legacyToRuntimeId361.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId361.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 388:
                runtimeId = legacyToRuntimeId388.get(legacyId);
                if (runtimeId == -1) {
                    runtimeId = legacyToRuntimeId388.get(id << 6);
                    if (runtimeId == -1) runtimeId = legacyToRuntimeId388.get(BlockID.INFO_UPDATE << 6);
                }
                return runtimeId;
            case 389:
            case 390:
                runtimeId = legacyToRuntimeId389.get(legacyId);
                if (runtimeId == -1) {
                    runtimeId = legacyToRuntimeId389.get(id << 6);
                    if (runtimeId == -1) runtimeId = legacyToRuntimeId389.get(BlockID.INFO_UPDATE << 6);
                }
                return runtimeId;
            case 407:
            case 408:
            case 409:
            case 410:
            case 411:
                runtimeId = legacyToRuntimeId407.get(legacyId);
                if (runtimeId == -1) {
                    runtimeId = legacyToRuntimeId407.get(id << 6);
                    if (runtimeId == -1) runtimeId = legacyToRuntimeId407.get(BlockID.INFO_UPDATE << 6);
                }
                return runtimeId;
            case 419:
            case 420:
            case 422:
            case ProtocolInfo.v1_16_210_50:
            case ProtocolInfo.v1_16_210_53:
                runtimeId = legacyToRuntimeId419.get(legacyId);
                if (runtimeId == -1) {
                    runtimeId = legacyToRuntimeId419.get(id << 6);
                    if (runtimeId == -1) {
                        log.info("(419) Missing block runtime id mappings for " + id + ':' + meta);
                        runtimeId = legacyToRuntimeId419.get(BlockID.INFO_UPDATE << 6);
                    }
                }
                return runtimeId;
            case ProtocolInfo.v1_16_210:
            case ProtocolInfo.v1_16_220:
            case ProtocolInfo.v1_16_230_50:
            case ProtocolInfo.v1_16_230:
            case ProtocolInfo.v1_16_230_54:
                runtimeId = legacyToRuntimeId428.get(legacyId);
                if (runtimeId == -1) {
                    runtimeId = legacyToRuntimeId428.get(id << 6);
                    if (runtimeId == -1) {
                        log.info("(428) Missing block runtime id mappings for " + id + ':' + meta);
                        runtimeId = legacyToRuntimeId428.get(BlockID.INFO_UPDATE << 6);
                    }
                }

                return runtimeId;
            case ProtocolInfo.v1_17_0:
                runtimeId = legacyToRuntimeId440.get(legacyId);
                if (runtimeId == -1) {
                    runtimeId = legacyToRuntimeId440.get(id << 6);
                    if (runtimeId == -1) {
                        log.info("(440) Missing block runtime id mappings for " + id + ':' + meta);
                        runtimeId = legacyToRuntimeId440.get(BlockID.INFO_UPDATE << 6);
                    }
                }
                return runtimeId;
            case ProtocolInfo.v1_17_10:
            case ProtocolInfo.v1_17_20_20:
                runtimeId = legacyToRuntimeId448.get(legacyId);
                if (runtimeId == -1) {
                    runtimeId = legacyToRuntimeId448.get(id << 6);
                    if (runtimeId == -1) {
                        log.info("(448) Missing block runtime id mappings for " + id + ':' + meta);
                        runtimeId = legacyToRuntimeId448.get(BlockID.INFO_UPDATE << 6);
                    }
                }
                return runtimeId;
            default:
                throw new IllegalArgumentException("Tried to get block runtime id for unsupported protocol version: " + protocol);
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
            case 408:
            case 409:
            case 410:
            case 411:
                return compiledTable407;
            default: // Unused since 1.16.100 (419)
                throw new IllegalArgumentException("Tried to get compiled block runtime id table for unsupported protocol version: " + protocol);
        }
    }

    public static int getOrCreateRuntimeId(int protocol, int legacyId) throws NoSuchElementException {
        if (protocol < 223) throw new IllegalArgumentException("Tried to get block runtime id for unsupported protocol version: " + protocol);
        int runtimeId;
        switch (protocol) {
            case 223:
            case 224:
                runtimeId = legacyToRuntimeId223.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId223.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 261:
                runtimeId = legacyToRuntimeId261.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId261.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 274:
                runtimeId = legacyToRuntimeId274.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId274.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 281:
            case 282:
                runtimeId = legacyToRuntimeId282.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId282.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 291:
                runtimeId = legacyToRuntimeId291.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId291.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 313:
                runtimeId = legacyToRuntimeId313.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId313.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 332:
                runtimeId = legacyToRuntimeId332.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId332.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 340:
                runtimeId = legacyToRuntimeId340.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId340.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 354:
                runtimeId = legacyToRuntimeId354.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId354.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            case 361:
                runtimeId = legacyToRuntimeId361.get(legacyId);
                if (runtimeId == -1) runtimeId = legacyToRuntimeId361.get(BlockID.INFO_UPDATE << 4);
                return runtimeId;
            default: // 388+
                return getOrCreateRuntimeId(protocol, legacyId >> 4, legacyId & 0xf);
        }
    }

    public static int getLegacyFullId(int protocolId, int runtimeId) {
        if (protocolId >= ProtocolInfo.v1_17_10) {
            return runtimeIdToLegacy448.get(runtimeId);
        } else if (protocolId >= ProtocolInfo.v1_17_0) {
            return runtimeIdToLegacy440.get(runtimeId);
        } else if (protocolId >= ProtocolInfo.v1_16_210) {
            return runtimeIdToLegacy428.get(runtimeId);
        }
        throw new IllegalArgumentException("Tried to get legacyFullId for unsupported protocol version: " + protocolId);
    }

    public static int getOrCreateRuntimeId(int legacyId) throws NoSuchElementException {
        Server.mvw("GlobalBlockPalette#getOrCreateRuntimeId(int)");
        return getOrCreateRuntimeId(ProtocolInfo.CURRENT_PROTOCOL, legacyId >> 4, legacyId & 0xf);
    }

    public static int getLegacyFullId(int runtimeId) {
        Server.mvw("GlobalBlockPalette#getLegacyFullId(int)");
        return getLegacyFullId(ProtocolInfo.CURRENT_PROTOCOL, runtimeId);
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

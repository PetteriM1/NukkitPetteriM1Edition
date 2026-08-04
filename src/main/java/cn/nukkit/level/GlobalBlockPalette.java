package cn.nukkit.level;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.Utils;
import com.google.common.io.ByteStreams;
import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class GlobalBlockPalette {

    private static boolean initialized;

    private static final BlockPalette blockPalette428 = new BlockPalette(ProtocolInfo.v1_16_210);
    private static final BlockPalette blockPalette440 = new BlockPalette(ProtocolInfo.v1_17_0);
    private static final BlockPalette blockPalette448 = new BlockPalette(ProtocolInfo.v1_17_10);
    private static final BlockPalette blockPalette465 = new BlockPalette(ProtocolInfo.v1_17_30);
    private static final BlockPalette blockPalette471 = new BlockPalette(ProtocolInfo.v1_17_40);
    private static final BlockPalette blockPalette486 = new BlockPalette(ProtocolInfo.v1_18_10);
    private static final BlockPalette blockPalette503 = new BlockPalette(ProtocolInfo.v1_18_30);
    private static final BlockPalette blockPalette527 = new BlockPalette(ProtocolInfo.v1_19_0);
    private static final BlockPalette blockPalette544 = new BlockPalette(ProtocolInfo.v1_19_20);
    private static final BlockPalette blockPalette560 = new BlockPalette(ProtocolInfo.v1_19_50);
    private static final BlockPalette blockPalette567 = new BlockPalette(ProtocolInfo.v1_19_60);
    private static final BlockPalette blockPalette575 = new BlockPalette(ProtocolInfo.v1_19_70);
    private static final BlockPalette blockPalette582 = new BlockPalette(ProtocolInfo.v1_19_80);
    private static final BlockPalette blockPalette589 = new BlockPalette(ProtocolInfo.v1_20_0);
    private static final BlockPalette blockPalette594 = new BlockPalette(ProtocolInfo.v1_20_10);
    private static final BlockPalette blockPalette618 = new BlockPalette(ProtocolInfo.v1_20_30);
    private static final BlockPalette blockPalette622 = new BlockPalette(ProtocolInfo.v1_20_40);
    private static final BlockPalette blockPalette630 = new BlockPalette(ProtocolInfo.v1_20_50);
    private static final BlockPalette blockPalette649 = new BlockPalette(ProtocolInfo.v1_20_60);
    private static final BlockPalette blockPalette662 = new BlockPalette(ProtocolInfo.v1_20_70);
    private static final BlockPalette blockPalette671 = new BlockPalette(ProtocolInfo.v1_20_80);
    private static final BlockPalette blockPalette685 = new BlockPalette(ProtocolInfo.v1_21_0);
    private static final BlockPalette blockPalette712 = new BlockPalette(ProtocolInfo.v1_21_20);
    private static final BlockPalette blockPalette729 = new BlockPalette(ProtocolInfo.v1_21_30);
    private static final BlockPalette blockPalette748 = new BlockPalette(ProtocolInfo.v1_21_40);
    private static final BlockPalette blockPalette766 = new BlockPalette(ProtocolInfo.v1_21_50);
    private static final BlockPalette blockPalette776 = new BlockPalette(ProtocolInfo.v1_21_60);
    private static final BlockPalette blockPalette786 = new BlockPalette(ProtocolInfo.v1_21_70);
    private static final BlockPalette blockPalette800 = new BlockPalette(ProtocolInfo.v1_21_80);
    private static final BlockPalette blockPalette827 = new BlockPalette(ProtocolInfo.v1_21_100);
    private static final BlockPalette blockPalette843 = new BlockPalette(ProtocolInfo.v1_21_110);
    private static final BlockPalette blockPalette944 = new BlockPalette(ProtocolInfo.v1_26_10);
    private static final BlockPalette blockPalette975 = new BlockPalette(ProtocolInfo.v1_26_20);
    private static final BlockPalette blockPalette1001 = new BlockPalette(ProtocolInfo.v1_26_30);
    private static final BlockPalette blockPalette2168 = new BlockPalette(ProtocolInfo.v1_26_40);
    private static final BlockPalette blockPalette2192 = new BlockPalette(ProtocolInfo.v1_26_50_27);

    // Leave this public to expose for custom blocks impl
    public static final BlockPalette[] NEW_PALETTES = new BlockPalette[]{
            blockPalette428,
            blockPalette440,
            blockPalette448,
            blockPalette465,
            blockPalette471,
            blockPalette486,
            blockPalette503,
            blockPalette527,
            blockPalette544,
            blockPalette560,
            blockPalette567,
            blockPalette575,
            blockPalette582,
            blockPalette589,
            blockPalette594,
            blockPalette618,
            blockPalette622,
            blockPalette630,
            blockPalette649,
            blockPalette662,
            blockPalette671,
            blockPalette685,
            blockPalette712,
            blockPalette729,
            blockPalette748,
            blockPalette766,
            blockPalette776,
            blockPalette786,
            blockPalette800,
            blockPalette827,
            blockPalette843,
            blockPalette944,
            blockPalette975,
            blockPalette1001,
            blockPalette2168,
            blockPalette2192
    }; // Did you remember to update getChunkProtocol/matchChunkProtocol?
    private static final Int2IntMap legacyToRuntimeId388 = new Int2IntOpenHashMap();

    // Legacy stuff here
    private static final Int2IntMap legacyToRuntimeId389 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId407 = new Int2IntOpenHashMap();
    private static final Int2IntMap legacyToRuntimeId419 = new Int2IntOpenHashMap();
    private static byte[] compiledTable388;
    private static byte[] compiledTable389;
    private static byte[] compiledTable407;

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

    private static class TableEntryCollectionTypeToken extends TypeToken<Collection<TableEntry>> {
    }

    private static class TableEntryOldCollectionTypeToken extends TypeToken<Collection<TableEntryOld>> {
    }

    public static BlockPalette getPaletteByProtocol(int protocolId) {
        if (protocolId >= ProtocolInfo.v1_26_50_27) {
            return blockPalette2192;
        } else if (protocolId >= ProtocolInfo.v1_26_40) {
            return blockPalette2168;
        } else if (protocolId >= ProtocolInfo.v1_26_30) {
            return blockPalette1001;
        } else if (protocolId >= ProtocolInfo.v1_26_20_26) {
            return blockPalette975;
        } else if (protocolId >= ProtocolInfo.v1_26_10) {
            return blockPalette944;
        } else if (protocolId >= ProtocolInfo.v1_21_110) {
            return blockPalette843;
        } else if (protocolId >= ProtocolInfo.v1_21_100) {
            return blockPalette827;
        } else if (protocolId >= ProtocolInfo.v1_21_80) {
            return blockPalette800;
        } else if (protocolId >= ProtocolInfo.v1_21_70_24) {
            return blockPalette786;
        } else if (protocolId >= ProtocolInfo.v1_21_60) {
            return blockPalette776;
        } else if (protocolId >= ProtocolInfo.v1_21_50_28) {
            return blockPalette766;
        } else if (protocolId >= ProtocolInfo.v1_21_40) {
            return blockPalette748;
        } else if (protocolId >= ProtocolInfo.v1_21_30) {
            return blockPalette729;
        } else if (protocolId >= ProtocolInfo.v1_21_20) {
            return blockPalette712;
        } else if (protocolId >= ProtocolInfo.v1_21_0) {
            return blockPalette685;
        } else if (protocolId >= ProtocolInfo.v1_20_80) {
            return blockPalette671;
        } else if (protocolId >= ProtocolInfo.v1_20_70) {
            return blockPalette662;
        } else if (protocolId >= ProtocolInfo.v1_20_60) {
            return blockPalette649;
        } else if (protocolId >= ProtocolInfo.v1_20_50) {
            return blockPalette630;
        } else if (protocolId >= ProtocolInfo.v1_20_40) {
            return blockPalette622;
        } else if (protocolId >= ProtocolInfo.v1_20_30) {
            return blockPalette618;
        } else if (protocolId >= ProtocolInfo.v1_20_10_21) {
            return blockPalette594;
        } else if (protocolId >= ProtocolInfo.v1_20_0_23) {
            return blockPalette589;
        } else if (protocolId >= ProtocolInfo.v1_19_80) {
            return blockPalette582;
        } else if (protocolId >= ProtocolInfo.v1_19_70_24) {
            return blockPalette575;
        } else if (protocolId >= ProtocolInfo.v1_19_60) {
            return blockPalette567;
        } else if (protocolId >= ProtocolInfo.v1_19_50) {
            return blockPalette560;
        } else if (protocolId >= ProtocolInfo.v1_19_20) {
            return blockPalette544;
        } else if (protocolId >= ProtocolInfo.v1_19_0_29) {
            return blockPalette527;
        } else if (protocolId >= ProtocolInfo.v1_18_30) {
            return blockPalette503;
        } else if (protocolId >= ProtocolInfo.v1_18_10_26) {
            return blockPalette486;
        } else if (protocolId >= ProtocolInfo.v1_17_40) {
            return blockPalette471;
        } else if (protocolId >= ProtocolInfo.v1_17_30) {
            return blockPalette465;
        } else if (protocolId >= ProtocolInfo.v1_17_10) {
            return blockPalette448;
        } else if (protocolId >= ProtocolInfo.v1_17_0) {
            return blockPalette440;
        } else if (protocolId >= ProtocolInfo.v1_16_210) {
            return blockPalette428;
        }
        throw new IllegalArgumentException("Tried to getPaletteByProtocol for unsupported protocol version: " + protocolId);
    }

    public static void init() {
        if (initialized) {
            throw new IllegalStateException("GlobalBlockPalette was already generated!");
        }
        initialized = true;
        log.debug("Loading block palette...");
        // init legacyToRuntime
        legacyToRuntimeId388.defaultReturnValue(-1);
        legacyToRuntimeId389.defaultReturnValue(-1);
        legacyToRuntimeId407.defaultReturnValue(-1);
        legacyToRuntimeId419.defaultReturnValue(-1);
        // allocators
        AtomicInteger runtimeIdAllocator388 = new AtomicInteger(0);
        AtomicInteger runtimeIdAllocator389 = new AtomicInteger(0);
        AtomicInteger runtimeIdAllocator407 = new AtomicInteger(0);

        // 388
        ListTag<CompoundTag> tag388;
        try (InputStream stream388 = Server.class.getClassLoader().getResourceAsStream("runtime_block_states_388.dat")) {
            if (stream388 == null) throw new AssertionError("Unable to locate block state nbt 388");
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
        ListTag<CompoundTag> tag389;
        try (InputStream stream389 = Server.class.getClassLoader().getResourceAsStream("runtime_block_states_389.dat")) {
            if (stream389 == null) throw new AssertionError("Unable to locate block state nbt 389");
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
        //noinspection unchecked
        ListTag<CompoundTag> tag407 = (ListTag<CompoundTag>) Utils.loadTagResource("runtime_block_states_407.dat");
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
        //noinspection unchecked
        ListTag<CompoundTag> tag419 = (ListTag<CompoundTag>) Utils.loadTagResource("runtime_block_states_419.dat");
        for (CompoundTag state : tag419.getAll()) {
            int id = state.getInt("id");
            int data = state.getShort("data");
            int runtimeId = state.getInt("runtimeId");
            int legacyId = id << 6 | data;
            legacyToRuntimeId419.put(legacyId, runtimeId);
        }

        for (BlockPalette palette : NEW_PALETTES) {
            //noinspection unchecked
            loadBlockStates((ListTag<CompoundTag>) Utils.loadTagResource("runtime_block_states_" + palette.getProtocol() + ".dat"), palette);
        }
    }

    private static void loadBlockStates(ListTag<CompoundTag> blockStates, BlockPalette blockPalette) {
        List<CompoundTag> stateOverloads = new ObjectArrayList<>();
        for (CompoundTag state : blockStates.getAll()) {
            if (!registerBlockState(blockPalette, state, false)) {
                stateOverloads.add(state);
            }
        }

        for (CompoundTag state : stateOverloads) {
            if (Nukkit.DEBUG > 2) {
                log.debug("[{}] Registering block palette overload: {}", blockPalette.getProtocol(), state.getString("name"));
            }
            registerBlockState(blockPalette, state, true);
        }

        blockPalette.lock(); // prevent adding new states
    }

    private static boolean registerBlockState(BlockPalette blockPalette, CompoundTag state, boolean force) {
        int id = state.getInt("id");
        int data = state.getShort("data");
        int runtimeId = state.getInt("runtimeId");
        boolean stateOverload = state.getBoolean("stateOverload");

        if (stateOverload && !force) {
            return false;
        }

        CompoundTag vanillaState = state
                .remove("id")
                .remove("data")
                .remove("runtimeId")
                .remove("stateOverload");
        blockPalette.registerState(id, data, runtimeId, vanillaState);
        return true;
    }

    public static int getOrCreateRuntimeId(int protocol, int id, int meta) {
        if (protocol >= ProtocolInfo.v1_16_210) {
            return getPaletteByProtocol(protocol).getRuntimeId(id, meta);
        }

        int legacyId = (id << 6) | meta;
        int runtimeId;
        switch (protocol) {
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
                        if (Nukkit.DEBUG > 1) {
                            log.warn("(419) Missing block runtime id mappings for " + id + ':' + meta);
                        }
                        runtimeId = legacyToRuntimeId419.get(BlockID.INFO_UPDATE << 6);
                    }
                }
                return runtimeId;
            default:
                throw new IllegalArgumentException("Tried to get block runtime id for unsupported protocol version: " + protocol);
        }
    }

    public static byte[] getCompiledTable(int protocol) {
        switch (protocol) {
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
        if (protocol < ProtocolInfo.v1_13_0) {
            if (Nukkit.DEBUG < 2) {
                // Upstream getOrCreateRuntimeId(id, meta)
                Server.mvw("GlobalBlockPalette#getLegacyFullId(int, int)");
                return getOrCreateRuntimeId(ProtocolInfo.CURRENT_PROTOCOL, protocol, legacyId);
            } else {
                throw new IllegalArgumentException("Tried to get block runtime id for unsupported protocol version: " + protocol);
            }
        }

        return getOrCreateRuntimeId(protocol, legacyId >> Block.DATA_BITS, legacyId & Block.DATA_MASK);
    }

    public static int getLegacyFullId(int protocolId, int runtimeId) {
        return getPaletteByProtocol(protocolId).getLegacyFullId(runtimeId);
    }

    public static int getOrCreateRuntimeId(int legacyId) throws NoSuchElementException {
        Server.mvw("GlobalBlockPalette#getOrCreateRuntimeId(int)");
        return getOrCreateRuntimeId(ProtocolInfo.CURRENT_PROTOCOL, legacyId >> Block.DATA_BITS, legacyId & Block.DATA_MASK);
    }

    public static int getLegacyFullId(int runtimeId) {
        Server.mvw("GlobalBlockPalette#getLegacyFullId(int)");
        return getLegacyFullId(ProtocolInfo.CURRENT_PROTOCOL, runtimeId);
    }
}

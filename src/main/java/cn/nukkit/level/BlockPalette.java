package cn.nukkit.level;

import cn.nukkit.Nukkit;
import cn.nukkit.block.BlockID;
import cn.nukkit.nbt.tag.CompoundTag;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntMaps;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Data
@Log4j2
public class BlockPalette {

    private final int protocol;
    private final QuickLookupTable legacyToRuntimeId = new QuickLookupTable();
    private final QuickLookupTable runtimeIdToLegacy = new QuickLookupTable();
    private final Map<CompoundTag, Integer> stateToLegacy = new HashMap<>();
    private volatile boolean locked;

    public BlockPalette(int protocol) {
        this.protocol = protocol;
    }

    private static class QuickLookupTable {

        private int[] array;

        private void clear() {
            array = null;
        }

        private static int[] fill(int[] array, int start) {
            for (int i = start; i < array.length; i++) {
                array[i] = -1;
            }

            return array;
        }

        private int get(int k) {
            if (k < 0 || array == null || k >= array.length) {
                return -1;
            }

            return array[k];
        }

        private void put(int k, int v) {
            if (k < 0) {
                throw new IllegalArgumentException();
            }

            if (array == null) {
                array = fill(new int[k + 1], 0);
            } else if (array.length <= k) {
                array = fill(Arrays.copyOf(array, k + 1), array.length);
            }

            array[k] = v;
        }

        private void putIfAbsent(int k, int v) {
            if (get(k) != -1) {
                return;
            }

            put(k, v);
        }

        private Int2IntMap toIntMap() {
            Int2IntOpenHashMap map = new Int2IntOpenHashMap(array != null ? array.length : 0);
            map.defaultReturnValue(-1);

            if (array != null) {
                int k = 0;
                for (int v : array) {
                    if (v != -1) {
                        map.put(k, v);
                    }
                    k++;
                }
            }

            return map;
        }
    }

    public Int2IntMap getLegacyToRuntimeIdMap() {
        return Int2IntMaps.unmodifiable(this.legacyToRuntimeId.toIntMap());
    }

    public void clearStates() {
        this.locked = false;
        this.legacyToRuntimeId.clear();
        this.runtimeIdToLegacy.clear();
        this.stateToLegacy.clear();
    }

    public int getLegacyFullId(int runtimeId) {
        return this.runtimeIdToLegacy.get(runtimeId);
    }

    public int getLegacyFullId(CompoundTag state) {
        return this.stateToLegacy.getOrDefault(state, -1);
    }

    public int getRuntimeId(int id, int meta) {
        int legacyId = id << 6 | meta;
        int runtimeId = this.legacyToRuntimeId.get(legacyId);
        if (runtimeId == -1) {
            runtimeId = legacyToRuntimeId.get(id << 6);
            if (runtimeId == -1) {
                runtimeId = legacyToRuntimeId.get(BlockID.INFO_UPDATE << 6);
                if (Nukkit.DEBUG > 1) {
                    log.warn("({}) Missing block runtime id mappings for {}:{}", this.protocol, id, meta);
                }
            }
        }
        return runtimeId;
    }

    public int getRuntimeId(int legacyId) {
        int runtimeId = this.legacyToRuntimeId.get(legacyId);
        if (runtimeId == -1) {
            if (Nukkit.DEBUG > 1) {
                log.warn("({}) Missing block runtime id mappings for {}", this.protocol, legacyId);
            }
            return legacyToRuntimeId.get(BlockID.INFO_UPDATE << 6);
        }
        return runtimeId;
    }

    public void lock() {
        this.locked = true;
    }

    public void registerState(int blockId, int data, int runtimeId, CompoundTag blockState) {
        if (this.locked) {
            throw new IllegalStateException("Block palette is already locked!");
        }

        int legacyId = blockId << 6 | data;
        this.legacyToRuntimeId.put(legacyId, runtimeId);
        this.runtimeIdToLegacy.putIfAbsent(runtimeId, legacyId);
        this.stateToLegacy.putIfAbsent(blockState, legacyId);
    }
}

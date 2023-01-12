/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.nbt.tag.CompoundTag;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntMaps;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockPalette {
    private static final Logger e = LogManager.getLogger(BlockPalette.class);
    private final int f;
    private final Int2IntMap d = new Int2IntOpenHashMap();
    private final Int2IntMap c = new Int2IntOpenHashMap();
    private final Map<CompoundTag, Integer> b = new HashMap<CompoundTag, Integer>();
    private volatile boolean a;

    public BlockPalette(int n) {
        this.f = n;
        this.d.defaultReturnValue(-1);
        this.c.defaultReturnValue(-1);
    }

    public void clearStates() {
        this.a = false;
        this.d.clear();
        this.c.clear();
        this.b.clear();
    }

    public void registerState(int n, int n2, int n3, CompoundTag compoundTag) {
        if (this.a) {
            throw new IllegalStateException("Block palette is already locked!");
        }
        int n4 = n << 6 | n2;
        this.d.put(n4, n3);
        this.c.putIfAbsent(n3, n4);
        this.b.putIfAbsent(compoundTag, n4);
    }

    public void lock() {
        this.a = true;
    }

    public int getRuntimeId(int n, int n2) {
        int n3 = n << 6 | n2;
        int n4 = this.d.get(n3);
        if (n4 == -1 && (n4 = this.d.get(n << 6)) == -1) {
            n4 = this.d.get(15872);
            e.info("({}) Missing block runtime id mappings for {}:{}", (Object)this.f, (Object)n, (Object)n2);
        }
        return n4;
    }

    public int getRuntimeId(int n) {
        int n2 = this.d.get(n);
        if (n2 == -1) {
            e.info("({}) Missing block runtime id mappings for {}", (Object)this.f, (Object)n);
            return this.d.get(15872);
        }
        return n2;
    }

    public int getLegacyFullId(int n) {
        return this.c.get(n);
    }

    public int getLegacyFullId(CompoundTag compoundTag) {
        return this.b.getOrDefault(compoundTag, -1);
    }

    public Int2IntMap getLegacyToRuntimeIdMap() {
        return Int2IntMaps.unmodifiable(this.d);
    }

    public int getProtocol() {
        return this.f;
    }

    public Int2IntMap getLegacyToRuntimeId() {
        return this.d;
    }

    public Int2IntMap getRuntimeIdToLegacy() {
        return this.c;
    }

    public Map<CompoundTag, Integer> getStateToLegacy() {
        return this.b;
    }

    public boolean isLocked() {
        return this.a;
    }

    public void setLocked(boolean bl) {
        this.a = bl;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof BlockPalette)) {
            return false;
        }
        BlockPalette blockPalette = (BlockPalette)object;
        if (!blockPalette.canEqual(this)) {
            return false;
        }
        if (this.getProtocol() != blockPalette.getProtocol()) {
            return false;
        }
        Int2IntMap int2IntMap = this.getLegacyToRuntimeId();
        Int2IntMap int2IntMap2 = blockPalette.getLegacyToRuntimeId();
        if (int2IntMap == null ? int2IntMap2 != null : !int2IntMap.equals(int2IntMap2)) {
            return false;
        }
        Int2IntMap int2IntMap3 = this.getRuntimeIdToLegacy();
        Int2IntMap int2IntMap4 = blockPalette.getRuntimeIdToLegacy();
        if (int2IntMap3 == null ? int2IntMap4 != null : !int2IntMap3.equals(int2IntMap4)) {
            return false;
        }
        Map<CompoundTag, Integer> map = this.getStateToLegacy();
        Map<CompoundTag, Integer> map2 = blockPalette.getStateToLegacy();
        if (map == null ? map2 != null : !((Object)map).equals(map2)) {
            return false;
        }
        return this.isLocked() == blockPalette.isLocked();
    }

    protected boolean canEqual(Object object) {
        return object instanceof BlockPalette;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        n2 = n2 * 59 + this.getProtocol();
        Int2IntMap int2IntMap = this.getLegacyToRuntimeId();
        n2 = n2 * 59 + (int2IntMap == null ? 43 : int2IntMap.hashCode());
        Int2IntMap int2IntMap2 = this.getRuntimeIdToLegacy();
        n2 = n2 * 59 + (int2IntMap2 == null ? 43 : int2IntMap2.hashCode());
        Map<CompoundTag, Integer> map = this.getStateToLegacy();
        n2 = n2 * 59 + (map == null ? 43 : ((Object)map).hashCode());
        n2 = n2 * 59 + (this.isLocked() ? 79 : 97);
        return n2;
    }

    public String toString() {
        return "BlockPalette(protocol=" + this.getProtocol() + ", legacyToRuntimeId=" + this.getLegacyToRuntimeId() + ", runtimeIdToLegacy=" + this.getRuntimeIdToLegacy() + ", stateToLegacy=" + this.getStateToLegacy() + ", locked=" + this.isLocked() + ")";
    }

    private static IllegalStateException a(IllegalStateException illegalStateException) {
        return illegalStateException;
    }
}


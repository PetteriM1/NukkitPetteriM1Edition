/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.scheduler;

import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockUpdateEntry;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class BlockUpdateScheduler {
    private final Level c;
    private long b;
    private final Long2ObjectMap<LinkedHashSet<BlockUpdateEntry>> a = new Long2ObjectOpenHashMap<LinkedHashSet<BlockUpdateEntry>>();
    private Set<BlockUpdateEntry> d;

    public BlockUpdateScheduler(Level level, long l) {
        this.b = l;
        this.c = level;
    }

    public synchronized void tick(long l) {
        if (l - this.b < 32767L) {
            for (long k = this.b + 1L; k <= l; ++k) {
                this.a(k);
            }
        } else {
            long l2;
            ArrayList<Long> arrayList = new ArrayList<Long>(this.a.keySet());
            Collections.sort(arrayList);
            Iterator<Long> iterator = arrayList.iterator();
            while (iterator.hasNext() && (l2 = iterator.next().longValue()) <= l) {
                this.a(l2);
            }
        }
        this.b = l;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void a(long l) {
        try {
            this.b = l;
            Set set = this.d = (Set)this.a.remove(l);
            if (set != null) {
                for (BlockUpdateEntry blockUpdateEntry : set) {
                    if (this.c.isAreaLoaded(new AxisAlignedBB(blockUpdateEntry.pos, blockUpdateEntry.pos))) {
                        Block block = this.c.getBlock(blockUpdateEntry.pos);
                        if (!Block.equals(block, blockUpdateEntry.block, false)) continue;
                        block.onUpdate(3);
                        continue;
                    }
                    this.c.scheduleUpdate(blockUpdateEntry.block, blockUpdateEntry.pos, 0);
                }
            }
        }
        finally {
            this.d = null;
        }
    }

    public Set<BlockUpdateEntry> getPendingBlockUpdates(AxisAlignedBB axisAlignedBB) {
        LinkedHashSet<BlockUpdateEntry> linkedHashSet = null;
        for (Map.Entry entry : this.a.entrySet()) {
            LinkedHashSet linkedHashSet2 = (LinkedHashSet)entry.getValue();
            for (BlockUpdateEntry blockUpdateEntry : linkedHashSet2) {
                Vector3 vector3 = blockUpdateEntry.pos;
                if (!(vector3.getX() >= axisAlignedBB.minX) || !(vector3.getX() < axisAlignedBB.maxX) || !(vector3.getZ() >= axisAlignedBB.minZ) || !(vector3.getZ() < axisAlignedBB.maxZ)) continue;
                if (linkedHashSet == null) {
                    linkedHashSet = new LinkedHashSet<BlockUpdateEntry>();
                }
                linkedHashSet.add(blockUpdateEntry);
            }
        }
        return linkedHashSet;
    }

    public boolean isBlockTickPending(Vector3 vector3, Block block) {
        Set<BlockUpdateEntry> set = this.d;
        if (set == null || set.isEmpty()) {
            return false;
        }
        return set.contains(new BlockUpdateEntry(vector3, block));
    }

    private long a(BlockUpdateEntry blockUpdateEntry) {
        return Math.max(blockUpdateEntry.delay, this.b + 1L);
    }

    public void add(BlockUpdateEntry blockUpdateEntry) {
        LinkedHashSet linkedHashSet;
        long l = this.a(blockUpdateEntry);
        LinkedHashSet<BlockUpdateEntry> linkedHashSet2 = (LinkedHashSet<BlockUpdateEntry>)this.a.get(l);
        if (linkedHashSet2 == null && (linkedHashSet = this.a.putIfAbsent(l, linkedHashSet2 = new LinkedHashSet<BlockUpdateEntry>())) != null) {
            linkedHashSet2 = linkedHashSet;
        }
        linkedHashSet2.add(blockUpdateEntry);
    }

    public boolean contains(BlockUpdateEntry blockUpdateEntry) {
        for (Map.Entry entry : this.a.entrySet()) {
            if (!((LinkedHashSet)entry.getValue()).contains(blockUpdateEntry)) continue;
            return true;
        }
        return false;
    }

    public boolean remove(BlockUpdateEntry blockUpdateEntry) {
        for (Map.Entry entry : this.a.entrySet()) {
            if (!((LinkedHashSet)entry.getValue()).remove(blockUpdateEntry)) continue;
            return true;
        }
        return false;
    }

    public boolean remove(Vector3 vector3) {
        for (Map.Entry entry : this.a.entrySet()) {
            if (!((LinkedHashSet)entry.getValue()).remove(vector3)) continue;
            return true;
        }
        return false;
    }
}


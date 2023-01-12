/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.block.Block;
import cn.nukkit.math.Vector3;

public class BlockUpdateEntry
implements Comparable<BlockUpdateEntry> {
    private static long a = 0L;
    public int priority;
    public long delay;
    public final Vector3 pos;
    public final Block block;
    public final long id;

    public BlockUpdateEntry(Vector3 vector3, Block block) {
        this.pos = vector3;
        this.block = block;
        this.id = a++;
    }

    public BlockUpdateEntry(Vector3 vector3, Block block, long l, int n) {
        this.id = a++;
        this.pos = vector3;
        this.priority = n;
        this.delay = l;
        this.block = block;
    }

    @Override
    public int compareTo(BlockUpdateEntry blockUpdateEntry) {
        return this.delay < blockUpdateEntry.delay ? -1 : (this.delay > blockUpdateEntry.delay ? 1 : (this.priority != blockUpdateEntry.priority ? this.priority - blockUpdateEntry.priority : Long.compare(this.id, blockUpdateEntry.id)));
    }

    public boolean equals(Object object) {
        if (!(object instanceof BlockUpdateEntry)) {
            if (object instanceof Vector3) {
                return this.pos.equals(object);
            }
            return false;
        }
        BlockUpdateEntry blockUpdateEntry = (BlockUpdateEntry)object;
        return this.pos.equals(blockUpdateEntry.pos) && Block.equals(this.block, blockUpdateEntry.block, false);
    }

    public int hashCode() {
        return this.pos.hashCode();
    }
}


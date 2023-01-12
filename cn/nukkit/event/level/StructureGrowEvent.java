/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.level;

import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.LevelEvent;
import java.util.List;
import java.util.Objects;

public class StructureGrowEvent
extends LevelEvent
implements Cancellable {
    private static final HandlerList e = new HandlerList();
    private final Block c;
    private final List<Block> d;

    public static HandlerList getHandlers() {
        return e;
    }

    public StructureGrowEvent(Block block, List<Block> list) {
        super(Objects.requireNonNull(block.getLevel()));
        this.c = block;
        this.d = list;
    }

    public Block getBlock() {
        return this.c;
    }

    public List<Block> getBlockList() {
        return this.d;
    }

    public void setBlockList(List<Block> list) {
        this.d.clear();
        if (list != null) {
            this.d.addAll(list);
        }
    }
}


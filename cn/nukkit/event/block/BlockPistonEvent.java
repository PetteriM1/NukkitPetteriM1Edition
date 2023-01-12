/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockPistonBase;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;
import cn.nukkit.math.BlockFace;
import java.util.ArrayList;
import java.util.List;

public class BlockPistonEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private final BlockFace f;
    private final List<Block> e;
    private final List<Block> c;
    private final boolean b;

    public static HandlerList getHandlers() {
        return d;
    }

    public BlockPistonEvent(BlockPistonBase blockPistonBase, BlockFace blockFace, List<Block> list, List<Block> list2, boolean bl) {
        super(blockPistonBase);
        this.f = blockFace;
        this.e = list;
        this.c = list2;
        this.b = bl;
    }

    public BlockFace getDirection() {
        return this.f;
    }

    public List<Block> getBlocks() {
        return new ArrayList<Block>(this.e);
    }

    public List<Block> getDestroyedBlocks() {
        return this.c;
    }

    public boolean isExtending() {
        return this.b;
    }

    @Override
    public BlockPistonBase getBlock() {
        return (BlockPistonBase)super.getBlock();
    }
}


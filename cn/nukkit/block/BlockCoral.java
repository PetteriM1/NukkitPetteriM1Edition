/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

public class BlockCoral
extends BlockTransparentMeta {
    public BlockCoral() {
        this(0);
    }

    public BlockCoral(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Coral";
    }

    @Override
    public int getId() {
        return 386;
    }

    @Override
    public double getHardness() {
        return 0.0;
    }

    @Override
    public double getResistance() {
        return 0.0;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && this.down().isTransparent()) {
            this.getLevel().useBreakOn(this);
            return 1;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (this.down().isTransparent()) {
            return false;
        }
        return this.getLevel().setBlock(this, this, true, true);
    }
}


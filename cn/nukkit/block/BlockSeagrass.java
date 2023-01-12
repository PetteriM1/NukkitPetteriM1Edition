/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparent;
import cn.nukkit.block.BlockWater;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;

public class BlockSeagrass
extends BlockTransparent {
    @Override
    public String getName() {
        return "Seagrass";
    }

    @Override
    public int getId() {
        return 385;
    }

    @Override
    public int getToolType() {
        return 359;
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
        if (!(block instanceof BlockWater) || this.down().isTransparent()) {
            return false;
        }
        return this.getLevel().setBlock(this, this, true, true);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item != null && item.isShears()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }
}


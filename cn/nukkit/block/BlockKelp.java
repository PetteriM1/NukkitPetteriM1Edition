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

public class BlockKelp
extends BlockTransparent {
    @Override
    public String getName() {
        return "Kelp";
    }

    @Override
    public int getId() {
        return 393;
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
        Block block;
        if (n == 1 && (block = this.down()).getId() != this.getId() && block.isTransparent()) {
            this.getLevel().useBreakOn(this);
            return 1;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Block block3;
        if (!(block instanceof BlockWater) || (block3 = this.down()).getId() != this.getId() && block3.isTransparent()) {
            return false;
        }
        return this.getLevel().setBlock(this, this, true, true);
    }

    @Override
    public Item toItem() {
        return Item.get(335, 0, 1);
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFallableMeta;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

public class BlockConcretePowder
extends BlockFallableMeta {
    public BlockConcretePowder() {
        super(0);
    }

    public BlockConcretePowder(int n) {
        super(n);
    }

    @Override
    public int getFullId() {
        return 3792 + this.getDamage();
    }

    @Override
    public int getId() {
        return 237;
    }

    @Override
    public String getName() {
        return "Concrete Powder";
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public int getToolType() {
        return 2;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            super.onUpdate(1);
            for (int k = 1; k <= 5; ++k) {
                Block block = this.getSide(BlockFace.fromIndex(k));
                if (block.getId() != 8 && block.getId() != 9) continue;
                this.level.setBlock(this, Block.get(236, this.getDamage()), true, true);
            }
            return 1;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        boolean bl = false;
        for (int k = 1; k <= 5; ++k) {
            Block block3 = this.getSide(BlockFace.fromIndex(k));
            if (block3.getId() != 8 && block3.getId() != 9) continue;
            bl = true;
            break;
        }
        if (bl) {
            this.level.setBlock(this, Block.get(236, this.getDamage()), true, true);
        } else {
            this.level.setBlock(this, this, true, true);
        }
        return true;
    }
}


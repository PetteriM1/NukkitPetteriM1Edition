/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockIce;
import cn.nukkit.item.Item;

public class BlockIcePacked
extends BlockIce {
    @Override
    public int getId() {
        return 174;
    }

    @Override
    public String getName() {
        return "Packed Ice";
    }

    @Override
    public boolean onBreak(Item item) {
        return this.getLevel().setBlock(this, Block.get(0), true);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.hasEnchantment(16)) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public int onUpdate(int n) {
        return 0;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean isTransparent() {
        return false;
    }
}


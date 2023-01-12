/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;

public class BlockChemicalHeat
extends BlockSolid {
    @Override
    public int getId() {
        return 192;
    }

    @Override
    public String getName() {
        return "Heat Block";
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public double getHardness() {
        return 2.5;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }
}


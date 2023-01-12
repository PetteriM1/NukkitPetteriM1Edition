/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockButton;
import cn.nukkit.item.Item;

public class BlockButtonStone
extends BlockButton {
    public BlockButtonStone() {
        this(0);
    }

    public BlockButtonStone(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 77;
    }

    @Override
    public String getName() {
        return "Stone Button";
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= 1) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}


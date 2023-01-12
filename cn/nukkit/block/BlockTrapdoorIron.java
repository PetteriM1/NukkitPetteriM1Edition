/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.BlockTrapdoor;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockTrapdoorIron
extends BlockTrapdoor {
    public BlockTrapdoorIron() {
        this(0);
    }

    public BlockTrapdoorIron(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 167;
    }

    @Override
    public String getName() {
        return "Iron Trapdoor";
    }

    @Override
    public double getHardness() {
        return 5.0;
    }

    @Override
    public double getResistance() {
        return 25.0;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}


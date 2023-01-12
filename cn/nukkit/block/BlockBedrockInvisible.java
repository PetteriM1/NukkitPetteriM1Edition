/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockBedrockInvisible
extends BlockSolid {
    @Override
    public int getId() {
        return 95;
    }

    @Override
    public String getName() {
        return "Invisible Bedrock";
    }

    @Override
    public double getHardness() {
        return -1.0;
    }

    @Override
    public double getResistance() {
        return 1.8E7;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.TRANSPARENT_BLOCK_COLOR;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public Item toItem() {
        return Item.get(0);
    }
}


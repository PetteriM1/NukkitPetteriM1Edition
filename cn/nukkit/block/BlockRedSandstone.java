/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSandstone;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockRedSandstone
extends BlockSandstone {
    public BlockRedSandstone() {
        this(0);
    }

    public BlockRedSandstone(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 179;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Red Sandstone", "Chiseled Red Sandstone", "Smooth Red Sandstone", ""};
        return stringArray[this.getDamage() & 3];
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, this.getDamage() & 3);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTrapdoor;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

public class BlockTrapdoorAcacia
extends BlockTrapdoor {
    public BlockTrapdoorAcacia() {
        this(0);
    }

    public BlockTrapdoorAcacia(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Acacia Trapdoor";
    }

    @Override
    public int getId() {
        return 400;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }
}


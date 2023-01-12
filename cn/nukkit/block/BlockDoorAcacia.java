/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockDoorWood;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockDoorAcacia
extends BlockDoorWood {
    public BlockDoorAcacia() {
        this(0);
    }

    public BlockDoorAcacia(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Acacia Door Block";
    }

    @Override
    public int getId() {
        return 196;
    }

    @Override
    public Item toItem() {
        return Item.get(430);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }
}


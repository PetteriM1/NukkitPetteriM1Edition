/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.utils.DyeColor;

public class ItemBed
extends Item {
    public ItemBed() {
        this((Integer)0, 1);
    }

    public ItemBed(Integer n) {
        this(n, 1);
    }

    public ItemBed(Integer n, int n2) {
        super(355, n, n2, DyeColor.getByWoolData(n).getName() + " Bed");
        this.block = Block.get(26);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}


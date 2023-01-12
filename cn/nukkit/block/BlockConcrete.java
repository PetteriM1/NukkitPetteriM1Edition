/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;

public class BlockConcrete
extends BlockSolidMeta {
    public BlockConcrete() {
        this(0);
    }

    public BlockConcrete(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 236;
    }

    @Override
    public double getResistance() {
        return 9.0;
    }

    @Override
    public double getHardness() {
        return 1.8;
    }

    @Override
    public String getName() {
        return "Concrete";
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public Item[] getDrops(Item item) {
        Item[] itemArray;
        if (item.getTier() >= 1) {
            Item[] itemArray2 = new Item[1];
            itemArray = itemArray2;
            itemArray2[0] = this.toItem();
        } else {
            itemArray = new Item[]{};
        }
        return itemArray;
    }

    @Override
    public BlockColor getColor() {
        return DyeColor.getByWoolData(this.getDamage()).getColor();
    }

    public DyeColor getDyeColor() {
        return DyeColor.getByWoolData(this.getDamage());
    }
}


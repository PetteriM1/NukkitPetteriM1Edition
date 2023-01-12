/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.TerracottaColor;

public class BlockTerracotta
extends BlockSolidMeta {
    public BlockTerracotta() {
        this(0);
    }

    public BlockTerracotta(int n) {
        super(0);
    }

    public BlockTerracotta(DyeColor dyeColor) {
        this(dyeColor.getWoolData());
    }

    public BlockTerracotta(TerracottaColor terracottaColor) {
        this(terracottaColor.getTerracottaData());
    }

    @Override
    public int getId() {
        return 172;
    }

    @Override
    public String getName() {
        return "Terracotta";
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public double getHardness() {
        return 1.25;
    }

    @Override
    public double getResistance() {
        return 7.0;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return TerracottaColor.getByTerracottaData(this.getDamage()).getColor();
    }

    public TerracottaColor getDyeColor() {
        return TerracottaColor.getByTerracottaData(this.getDamage());
    }
}


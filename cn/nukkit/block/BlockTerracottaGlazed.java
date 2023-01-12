/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.Utils;

public abstract class BlockTerracottaGlazed
extends BlockSolidMeta {
    public BlockTerracottaGlazed() {
        this(0);
    }

    public BlockTerracottaGlazed(int n) {
        super(n);
    }

    @Override
    public double getResistance() {
        return 7.0;
    }

    @Override
    public double getHardness() {
        return 1.4;
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
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.setDamage(Utils.faces2534[player != null ? player.getDirection().getHorizontalIndex() : 0]);
        return this.getLevel().setBlock(block, this, true, true);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockColor getColor() {
        return DyeColor.getByDyeData(this.getDyeColor().getDyeData()).getColor();
    }

    public DyeColor getDyeColor() {
        return DyeColor.BLACK;
    }
}


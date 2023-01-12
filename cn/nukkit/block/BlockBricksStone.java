/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;

public class BlockBricksStone
extends BlockSolidMeta {
    public static final int NORMAL = 0;
    public static final int MOSSY = 1;
    public static final int CRACKED = 2;
    public static final int CHISELED = 3;

    public BlockBricksStone() {
        this(0);
    }

    public BlockBricksStone(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 98;
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public double getResistance() {
        return 30.0;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Stone Bricks", "Mossy Stone Bricks", "Cracked Stone Bricks", "Chiseled Stone Bricks"};
        return stringArray[this.getDamage() & 3];
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{Item.get(98, this.getDamage() & 3, 1)};
        }
        return new Item[0];
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}


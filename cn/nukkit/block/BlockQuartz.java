/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

public class BlockQuartz
extends BlockSolidMeta {
    public static final int QUARTZ_NORMAL = 0;
    public static final int QUARTZ_CHISELED = 1;
    public static final int QUARTZ_PILLAR = 2;
    public static final int QUARTZ_PILLAR2 = 3;
    private static final short[] d = new short[]{0, 0, 8, 8, 4, 4};

    public BlockQuartz() {
        this(0);
    }

    public BlockQuartz(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 155;
    }

    @Override
    public double getHardness() {
        return 0.8;
    }

    @Override
    public double getResistance() {
        return 4.0;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Quartz Block", "Chiseled Quartz Block", "Quartz Pillar", "Quartz Pillar"};
        return stringArray[this.getDamage() & 3];
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (this.getDamage() != 0) {
            this.setDamage(this.getDamage() & 3 | d[blockFace.getIndex()]);
        }
        this.getLevel().setBlock(block, this, true, true);
        return true;
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
        return new ItemBlock((Block)this, this.getDamage() & 3, 1);
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.QUARTZ_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}


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

public class BlockPurpur
extends BlockSolidMeta {
    public static final int PURPUR_NORMAL = 0;
    public static final int PURPUR_PILLAR = 2;
    private static final short[] d = new short[]{0, 0, 8, 8, 4, 4};

    public BlockPurpur() {
        this(0);
    }

    public BlockPurpur(int n) {
        super(n);
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Purpur Block", "", "Purpur Pillar", ""};
        return stringArray[this.getDamage() & 3];
    }

    @Override
    public int getId() {
        return 201;
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
    public int getToolType() {
        return 3;
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
        return new ItemBlock(Block.get(201), this.getDamage() & 3, 1);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.MAGENTA_BLOCK_COLOR;
    }
}


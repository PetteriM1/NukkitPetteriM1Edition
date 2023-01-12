/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.block.BlockWoodBark;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

public class BlockWood
extends BlockSolidMeta {
    public static final int OAK = 0;
    public static final int SPRUCE = 1;
    public static final int BIRCH = 2;
    public static final int JUNGLE = 3;
    private static final short[] e = new short[]{0, 0, 8, 8, 4, 4};
    private static final int[] d = new int[]{265, 260, 261, 262};

    public BlockWood() {
        this(0);
    }

    public BlockWood(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 17;
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public double getResistance() {
        return 10.0;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Oak Wood", "Spruce Wood", "Birch Wood", "Jungle Wood"};
        return stringArray[this.getDamage() & 3];
    }

    @Override
    public int getBurnChance() {
        return 5;
    }

    @Override
    public int getBurnAbility() {
        return 10;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.setDamage(this.getDamage() & 3 | e[blockFace.getIndex()]);
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, this.getDamage() & 3);
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public BlockColor getColor() {
        switch (this.getDamage() & 7) {
            default: {
                return BlockColor.WOOD_BLOCK_COLOR;
            }
            case 1: {
                return BlockColor.SPRUCE_BLOCK_COLOR;
            }
            case 2: {
                return BlockColor.SAND_BLOCK_COLOR;
            }
            case 3: 
        }
        return BlockColor.DIRT_BLOCK_COLOR;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.isAxe() && player != null && (player.isSurvival() || player.isCreative()) && (!(this instanceof BlockWoodBark) || this.getDamage() < 8)) {
            Block block = Block.get(this.getStrippedId());
            block.setDamage(this.getStrippedDamage());
            item.useOn(this);
            this.level.setBlock(this, block, true, true);
            return true;
        }
        return false;
    }

    protected int getStrippedId() {
        return d[this.getDamage() & 3];
    }

    protected int getStrippedDamage() {
        return this.getDamage();
    }
}


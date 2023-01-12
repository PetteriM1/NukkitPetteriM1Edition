/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockWood;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;

public abstract class BlockWoodStripped
extends BlockWood {
    private static final short[] f = new short[]{0, 0, 2, 2, 1, 1};

    public BlockWoodStripped() {
        this(0);
    }

    public BlockWoodStripped(int n) {
        super(n);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.setDamage(this.getDamage() | f[blockFace.getIndex()]);
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public boolean canBeActivated() {
        return false;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, this.getDamage());
    }
}


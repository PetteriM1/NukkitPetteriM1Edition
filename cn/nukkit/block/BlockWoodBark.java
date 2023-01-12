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

public class BlockWoodBark
extends BlockWood {
    public BlockWoodBark() {
        this(0);
    }

    public BlockWoodBark(int n) {
        super(n);
    }

    @Override
    public void setDamage(int n) {
        super.setDamage(n);
    }

    @Override
    public int getId() {
        return 467;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Oak Wood", "Spruce Wood", "Birch Wood", "Jungle Wood", "Acacia Wood", "Dark Oak Wood", "Oak Wood", "Oak Wood"};
        return stringArray[this.getDamage() & 7];
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, this.getDamage() & 0xF);
    }

    @Override
    protected int getStrippedId() {
        return 467;
    }

    @Override
    protected int getStrippedDamage() {
        return this.getDamage() + 8;
    }
}


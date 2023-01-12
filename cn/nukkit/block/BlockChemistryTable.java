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

public class BlockChemistryTable
extends BlockSolidMeta {
    public BlockChemistryTable() {
        this(0);
    }

    public BlockChemistryTable(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 238;
    }

    @Override
    public String getName() {
        int n = this.getDamage();
        if (n >= 12) {
            return "Lab Table";
        }
        if (n >= 8) {
            return "Element Constructor";
        }
        if (n >= 4) {
            return "Material Reducer";
        }
        return "Compound Creator";
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public double getResistance() {
        return this.getDamage() >= 12 ? 3.0 : 2.0;
    }

    @Override
    public double getHardness() {
        return 2.5;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, this.getDamage() >> 2 << 2);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.setDamage((this.getDamage() >> 2 << 2) + (player != null ? player.getDirection().getHorizontalIndex() : 0));
        return this.getLevel().setBlock(block, this, true);
    }
}


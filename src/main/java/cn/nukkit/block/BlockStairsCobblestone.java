package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

/**
 * Created on 2015/11/25 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockStairsCobblestone extends BlockStairs {

    public BlockStairsCobblestone() {
        this(0);
    }

    public BlockStairsCobblestone(int meta) {
        super(meta);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public int getId() {
        return COBBLESTONE_STAIRS;
    }

    @Override
    public String getName() {
        return "Cobblestone Stairs";
    }

    @Override
    public double getResistance() {
        return 30;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
}

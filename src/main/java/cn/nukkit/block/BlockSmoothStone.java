package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

public class BlockSmoothStone extends BlockSolid {

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public double getHardness() {
        return 1.5; // 2
    }

    @Override
    public int getId() {
        return SMOOTH_STONE;
    }

    @Override
    public String getName() {
        return "Smooth Stone";
    }

    @Override
    public double getResistance() {
        return 10;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
}

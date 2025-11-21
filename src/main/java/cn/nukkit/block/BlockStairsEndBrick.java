package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

public class BlockStairsEndBrick extends BlockStairs {

    public BlockStairsEndBrick() {
        this(0);
    }

    public BlockStairsEndBrick(int meta) {
        super(meta);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public double getHardness() {
        return 2; //3
    }

    @Override
    public int getId() {
        return END_BRICK_STAIRS;
    }

    @Override
    public String getName() {
        return "End Brick Stairs";
    }

    @Override
    public double getResistance() {
        return 9;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
}

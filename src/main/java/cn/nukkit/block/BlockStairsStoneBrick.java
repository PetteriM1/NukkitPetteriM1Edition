package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

/**
 * Created on 2015/11/25 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockStairsStoneBrick extends BlockStairs {

    public BlockStairsStoneBrick() {
        this(0);
    }

    public BlockStairsStoneBrick(int meta) {
        super(meta);
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public int getId() {
        return STONE_BRICK_STAIRS;
    }

    @Override
    public String getName() {
        return "Stone Brick Stairs";
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

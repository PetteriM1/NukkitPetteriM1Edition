package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

/**
 * Created by PetteriM1
 * For SuomiCraft PE
 */
public class BlockStructureBlock extends BlockSolid {

    public BlockStructureBlock() {
    }

    @Override
    public int getId() {
        return STRUCTURE_BLOCK;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 30;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_NONE;
    }

    @Override
    public String getName() {
        return "Protection Block";
    }
}

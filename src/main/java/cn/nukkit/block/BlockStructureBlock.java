package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockStructureBlock extends BlockSolid {

    @Override
    public BlockColor getColor() {
        return BlockColor.LIGHT_GRAY_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public int getId() {
        return STRUCTURE_BLOCK;
    }

    @Override
    public String getName() {
        return "Structure Block";
    }

    @Override
    public double getResistance() {
        return 18000000;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }
}

package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockBarrier extends BlockTransparent {

    @Override
    public boolean canBeFlowedInto() {
        return false;
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
    public BlockColor getColor() {
        return BlockColor.TRANSPARENT_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public int getId() {
        return BARRIER;
    }

    @Override
    public String getName() {
        return "Barrier";
    }

    @Override
    public double getResistance() {
        return 18000000;
    }

    @Override
    public WaterloggingType getWaterloggingType() {
        return WaterloggingType.WHEN_PLACED_IN_WATER;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }
}

package cn.nukkit.block;

import cn.nukkit.item.Item;

public class BlockBorder extends BlockTransparent {

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public int getId() {
        return BORDER_BLOCK;
    }

    @Override
    public String getName() {
        return "Border";
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

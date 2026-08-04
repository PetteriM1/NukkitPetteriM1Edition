package cn.nukkit.block;

import cn.nukkit.item.Item;

public class BlockCommandBlock extends BlockSolid {

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public int getId() {
        return COMMAND_BLOCK;
    }

    @Override
    public String getName() {
        return "Command Block";
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

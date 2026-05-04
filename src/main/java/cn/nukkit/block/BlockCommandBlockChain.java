package cn.nukkit.block;

import cn.nukkit.item.Item;

public class BlockCommandBlockChain extends BlockSolid {

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public int getId() {
        return CHAIN_COMMAND_BLOCK;
    }

    @Override
    public String getName() {
        return "Chain Command Block";
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

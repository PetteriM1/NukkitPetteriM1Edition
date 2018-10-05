package cn.nukkit.block;

import cn.nukkit.item.Item;

/**
 * Created by PetteriM1
 */
public class BlockHardGlass extends BlockTransparent {

    public BlockHardGlass() {
    }

    @Override
    public int getId() {
        return HARD_GLASS;
    }

    @Override
    public String getName() {
        return "Hardened Glass";
    }

    @Override
    public double getResistance() {
        return 1.5;
    }

    @Override
    public double getHardness() {
        return 0.3;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}

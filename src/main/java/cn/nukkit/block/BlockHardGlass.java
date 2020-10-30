package cn.nukkit.block;

import cn.nukkit.item.Item;

/**
 * Created by PetteriM1
 */
public class BlockHardGlass extends BlockTransparent {

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
        return Item.EMPTY_ARRAY;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}

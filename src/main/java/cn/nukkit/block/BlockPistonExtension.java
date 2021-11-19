package cn.nukkit.block;

import cn.nukkit.item.Item;

public class BlockPistonExtension extends BlockTransparent {

    @Override
    public int getId() {
        return PISTON_EXTENSION;
    }

    @Override
    public String getName() {
        return "Piston Extension";
    }

    @Override
    public double getHardness() {
        return 0.1;
    }

    @Override
    public double getResistance() {
        return 0.1;
    }

    @Override
    public Item[] getDrops(Item item) {
            return Item.EMPTY_ARRAY;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBePulled() {
        return false;
    }
}

package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

/**
 * @author Angelic47
 * Nukkit Project
 */
public class BlockCobblestone extends BlockSolid {

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public int getId() {
        return COBBLESTONE;
    }

    @Override
    public String getName() {
        return "Cobblestone";
    }

    @Override
    public double getResistance() {
        return 30;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{
                    toItem()
            };
        } else {
            return new Item[0];
        }
    }
}

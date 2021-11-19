package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

/**
 * Created by PetteriM1
 */
public class BlockChemicalHeat extends BlockSolid {

    @Override
    public int getId() {
        return CHEMICAL_HEAT;
    }

    @Override
    public String getName() {
        return "Heat Block";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public double getHardness() {
        return 2.5;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{
                    this.toItem()
            };
        }
        return Item.EMPTY_ARRAY;
    }
}

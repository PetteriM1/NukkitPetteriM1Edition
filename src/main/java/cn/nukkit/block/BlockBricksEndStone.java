package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

public class BlockBricksEndStone extends BlockSolid {

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{
                    Item.get(Item.END_BRICKS, 0, 1)
            };
        } else {
            return new Item[0];
        }
    }

    @Override
    public double getHardness() {
        return 0.8;
    }

    @Override
    public int getId() {
        return END_BRICKS;
    }

    @Override
    public String getName() {
        return "End Stone Bricks";
    }

    @Override
    public double getResistance() {
        return 4;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
}

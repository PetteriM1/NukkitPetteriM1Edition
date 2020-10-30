package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

/**
 * Created by PetteriM1
 */
public class BlockHardGlassPane extends BlockThin {

    @Override
    public String getName() {
        return "Hardened Glass Pane";
    }

    @Override
    public int getId() {
        return HARD_GLASS_PANE;
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
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}

package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemFlint;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class BlockGravel extends BlockFallable {

    @Override
    public int getId() {
        return GRAVEL;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 3;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHOVEL;
    }

    @Override
    public String getName() {
        return "Gravel";
    }

    @Override
    public Item[] getDrops(Item item) {
        if (Utils.random.nextInt(9) == 0 && !item.hasEnchantment(Enchantment.ID_SILK_TOUCH)) {
            return new Item[]{
                    new ItemFlint()
            };
        } else {
            return new Item[]{
                    toItem()
            };
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GRAY_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}

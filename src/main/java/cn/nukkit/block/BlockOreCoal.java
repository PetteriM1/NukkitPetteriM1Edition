package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemCoal;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.Utils;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class BlockOreCoal extends BlockSolid {

    @Override
    public int getId() {
        return COAL_ORE;
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public double getResistance() {
        return 15;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public String getName() {
        return "Coal Ore";
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= this.getToolTier()) {
            int count = 1;
            Enchantment fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING);
            if (fortune != null && fortune.getLevel() >= 1) {
                int i = Utils.random.nextInt(fortune.getLevel() + 2) - 1;

                if (i < 0) {
                    i = 0;
                }

                count = i + 1;
            }

            return new Item[]{
                    new ItemCoal(0, count)
            };
        } else {
            return Item.EMPTY_ARRAY;
        }
    }

    @Override
    public int getDropExp() {
        return Utils.rand(0, 2);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
    
    @Override
    public boolean canSilkTouch() {
        return true;
    }
}

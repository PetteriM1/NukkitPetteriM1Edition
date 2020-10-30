package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemRedstone;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.utils.Utils;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class BlockOreRedstone extends BlockSolid {

    @Override
    public int getId() {
        return REDSTONE_ORE;
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
        return ItemTool.TIER_IRON;
    }

    @Override
    public String getName() {
        return "Redstone Ore";
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= this.getToolTier()) {
            int count = Utils.random.nextInt(2) + 4;

            Enchantment fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING);
            if (fortune != null && fortune.getLevel() >= 1) {
                count += Utils.random.nextInt(fortune.getLevel() + 1);
            }

            return new Item[]{
                    new ItemRedstone(0, count)
            };
        } else {
            return Item.EMPTY_ARRAY;
        }
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_TOUCH) {
            this.getLevel().setBlock(this, Block.get(GLOWING_REDSTONE_ORE), false, false);
            this.getLevel().scheduleUpdate(this, 600);

            return Level.BLOCK_UPDATE_WEAK;
        }

        return 0;
    }

    @Override
    public int getDropExp() {
        return Utils.rand(1, 5);
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

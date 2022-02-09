package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class BlockIcePacked extends BlockIce {

    @Override
    public int getId() {
        return PACKED_ICE;
    }

    @Override
    public String getName() {
        return "Packed Ice";
    }

    @Override
    public boolean onBreak(Item item) {
        return this.getLevel().setBlock(this, Block.get(BlockID.AIR), true);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.hasEnchantment(Enchantment.ID_SILK_TOUCH)) {
            return new Item[]{
                    this.toItem()
            };
        }
        return Item.EMPTY_ARRAY;
    }

    @Override
    public int onUpdate(int type) {
        return 0;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public int getLightLevel() {
        return 4;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public boolean isTransparent() {
        return false;
    }
}

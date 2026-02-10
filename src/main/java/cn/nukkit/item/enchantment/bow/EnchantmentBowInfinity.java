package cn.nukkit.item.enchantment.bow;

import cn.nukkit.item.enchantment.Enchantment;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class EnchantmentBowInfinity extends EnchantmentBow {

    public EnchantmentBowInfinity() {
        super(Enchantment.ID_BOW_INFINITY, "arrowInfinite", Rarity.VERY_RARE);
    }

    @Override
    public int getMaxEnchantAbility(int level) {
        return 50;
    }

    @Override
    public int getMinEnchantAbility(int level) {
        return 20;
    }
}

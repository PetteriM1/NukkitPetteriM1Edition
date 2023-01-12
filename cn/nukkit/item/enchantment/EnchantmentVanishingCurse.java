/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public class EnchantmentVanishingCurse
extends Enchantment {
    protected EnchantmentVanishingCurse() {
        super(28, "curse.vanishing", Enchantment.Rarity.VERY_RARE, EnchantmentType.BREAKABLE);
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean canEnchant(Item item) {
        return item.getId() == 397 || item.getId() == 345 || super.canEnchant(item);
    }
}


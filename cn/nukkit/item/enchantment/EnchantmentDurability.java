/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;
import java.util.Random;

public class EnchantmentDurability
extends Enchantment {
    protected EnchantmentDurability() {
        super(17, "durability", Enchantment.Rarity.UNCOMMON, EnchantmentType.BREAKABLE);
    }

    @Override
    public int getMinEnchantAbility(int n) {
        return 5 + (n - 1 << 3);
    }

    @Override
    public int getMaxEnchantAbility(int n) {
        return super.getMinEnchantAbility(n) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment.id != 18;
    }

    @Override
    public boolean canEnchant(Item item) {
        return item.getMaxDurability() >= 0 || super.canEnchant(item);
    }

    public static boolean negateDamage(Item item, int n, Random random) {
        return !(item.isArmor() && random.nextFloat() < 0.6f || random.nextInt(n + 1) <= 0);
    }
}


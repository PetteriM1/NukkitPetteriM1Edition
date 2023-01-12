/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment.protection;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.EnchantmentType;

public abstract class EnchantmentProtection
extends Enchantment {
    protected final TYPE protectionType;

    protected EnchantmentProtection(int n, String string, Enchantment.Rarity rarity, TYPE tYPE) {
        super(n, string, rarity, EnchantmentType.ARMOR);
        this.protectionType = tYPE;
        if (this.protectionType == TYPE.FALL) {
            this.type = EnchantmentType.ARMOR_FEET;
        }
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        if (enchantment instanceof EnchantmentProtection) {
            if (((EnchantmentProtection)enchantment).protectionType == this.protectionType) {
                return false;
            }
            return ((EnchantmentProtection)enchantment).protectionType == TYPE.FALL || this.protectionType == TYPE.FALL;
        }
        return super.checkCompatibility(enchantment);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public String getName() {
        return "%enchantment.protect." + this.name;
    }

    public double getTypeModifier() {
        return 0.0;
    }

    @Override
    public boolean isMajor() {
        return true;
    }

    public static enum TYPE {
        ALL,
        FIRE,
        FALL,
        EXPLOSION,
        PROJECTILE;

    }
}


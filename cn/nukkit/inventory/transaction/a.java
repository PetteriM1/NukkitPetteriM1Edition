/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction;

import cn.nukkit.item.enchantment.Enchantment;

class a {
    static final int[] a = new int[Enchantment.Rarity.values().length];

    static {
        try {
            cn.nukkit.inventory.transaction.a.a[Enchantment.Rarity.COMMON.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.inventory.transaction.a.a[Enchantment.Rarity.UNCOMMON.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.inventory.transaction.a.a[Enchantment.Rarity.RARE.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.inventory.transaction.a.a[Enchantment.Rarity.VERY_RARE.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}


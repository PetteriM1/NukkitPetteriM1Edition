/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.enchantment.Enchantment;

public class EnchantmentEntry {
    private final Enchantment[] b;
    private final int a;
    private final String c;

    public EnchantmentEntry(Enchantment[] enchantmentArray, int n, String string) {
        this.b = enchantmentArray;
        this.a = n;
        this.c = string;
    }

    public Enchantment[] getEnchantments() {
        return this.b;
    }

    public int getCost() {
        return this.a;
    }

    public String getRandomName() {
        return this.c;
    }
}


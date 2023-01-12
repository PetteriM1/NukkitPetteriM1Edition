/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.item.enchantment.EnchantmentEntry;

public class EnchantmentList {
    private final EnchantmentEntry[] a;

    public EnchantmentList(int n) {
        this.a = new EnchantmentEntry[n];
    }

    public EnchantmentList setSlot(int n, EnchantmentEntry enchantmentEntry) {
        this.a[n] = enchantmentEntry;
        return this;
    }

    public EnchantmentEntry getSlot(int n) {
        return this.a[n];
    }

    public int getSize() {
        return this.a.length;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemEdible;

public class ItemPumpkinPie
extends ItemEdible {
    public ItemPumpkinPie() {
        this((Integer)0, 1);
    }

    public ItemPumpkinPie(Integer n) {
        this(n, 1);
    }

    public ItemPumpkinPie(Integer n, int n2) {
        super(400, n, n2, "Pumpkin Pie");
    }
}


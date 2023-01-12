/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemCoal
extends Item {
    public ItemCoal() {
        this((Integer)0, 1);
    }

    public ItemCoal(Integer n) {
        this(n, 1);
    }

    public ItemCoal(Integer n, int n2) {
        super(263, n, n2, "Coal");
        if (this.meta == 1) {
            this.name = "Charcoal";
        }
    }
}


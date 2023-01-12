/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemLead
extends Item {
    public ItemLead() {
        this((Integer)0, 1);
    }

    public ItemLead(Integer n) {
        this(n, 1);
    }

    public ItemLead(Integer n, int n2) {
        super(420, 0, n2, "Lead");
    }
}


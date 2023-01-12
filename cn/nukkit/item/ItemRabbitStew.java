/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemEdible;

public class ItemRabbitStew
extends ItemEdible {
    public ItemRabbitStew() {
        this((Integer)0, 1);
    }

    public ItemRabbitStew(Integer n) {
        this(n, 1);
    }

    public ItemRabbitStew(Integer n, int n2) {
        super(413, n, n2, "Rabbit Stew");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}


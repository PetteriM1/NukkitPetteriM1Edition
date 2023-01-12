/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemEdible;

public class ItemRabbitCooked
extends ItemEdible {
    public ItemRabbitCooked() {
        this((Integer)0, 1);
    }

    public ItemRabbitCooked(Integer n) {
        this(n, 1);
    }

    public ItemRabbitCooked(Integer n, int n2) {
        super(412, n, n2, "Cooked Rabbit");
    }
}


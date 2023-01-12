/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemKelp
extends Item {
    public ItemKelp() {
        this((Integer)0, 1);
    }

    public ItemKelp(Integer n) {
        this(n, 1);
    }

    public ItemKelp(Integer n, int n2) {
        super(335, n, n2, "Kelp");
        this.block = Block.get(393);
    }
}


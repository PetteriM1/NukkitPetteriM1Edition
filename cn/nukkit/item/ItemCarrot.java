/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.ItemEdible;

public class ItemCarrot
extends ItemEdible {
    public ItemCarrot() {
        this((Integer)0, 1);
    }

    public ItemCarrot(Integer n) {
        this(n, 1);
    }

    public ItemCarrot(Integer n, int n2) {
        super(391, 0, n2, "Carrot");
        this.block = Block.get(141);
    }
}


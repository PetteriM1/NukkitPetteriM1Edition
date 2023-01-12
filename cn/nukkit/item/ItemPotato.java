/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.ItemEdible;

public class ItemPotato
extends ItemEdible {
    public ItemPotato() {
        this((Integer)0, 1);
    }

    public ItemPotato(Integer n) {
        this(n, 1);
    }

    public ItemPotato(Integer n, int n2) {
        super(392, n, n2, "Potato");
        this.block = Block.get(142);
    }

    protected ItemPotato(int n, Integer n2, int n3, String string) {
        super(n, n2, n3, string);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemCampfire
extends Item {
    public ItemCampfire() {
        this((Integer)0, 1);
    }

    public ItemCampfire(Integer n) {
        this(n, 1);
    }

    public ItemCampfire(Integer n, int n2) {
        super(720, n, n2, "Campfire");
        this.block = Block.get(464);
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 340;
    }
}


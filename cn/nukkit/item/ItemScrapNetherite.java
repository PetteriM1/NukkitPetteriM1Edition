/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.Item;

public class ItemScrapNetherite
extends Item {
    public ItemScrapNetherite() {
        this((Integer)0, 1);
    }

    public ItemScrapNetherite(Integer n) {
        this(n, 1);
    }

    public ItemScrapNetherite(Integer n, int n2) {
        super(752, 0, n2, "Netherite Scrap");
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 407;
    }
}


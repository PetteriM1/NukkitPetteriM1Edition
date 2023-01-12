/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemDoorAcacia
extends Item {
    public ItemDoorAcacia() {
        this((Integer)0, 1);
    }

    public ItemDoorAcacia(Integer n) {
        this(n, 1);
    }

    public ItemDoorAcacia(Integer n, int n2) {
        super(430, 0, n2, "Acacia Door");
        this.block = Block.get(196);
    }
}


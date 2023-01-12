/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ItemBookWritable;

public class ItemBookAndQuill
extends ItemBookWritable {
    public ItemBookAndQuill() {
        this((Integer)0, 1);
    }

    public ItemBookAndQuill(Integer n) {
        this(n, 1);
    }

    public ItemBookAndQuill(Integer n, int n2) {
        super(386, 0, n2, "Book & Quill");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}


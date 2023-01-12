/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.item.Item;

public interface BlockEntityContainer {
    public Item getItem(int var1);

    public void setItem(int var1, Item var2);

    public int getSize();
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ProjectileItem;

public class ItemEgg
extends ProjectileItem {
    public ItemEgg() {
        this((Integer)0, 1);
    }

    public ItemEgg(Integer n) {
        this(n, 1);
    }

    public ItemEgg(Integer n, int n2) {
        super(344, n, n2, "Egg");
    }

    @Override
    public String getProjectileEntityType() {
        return "Egg";
    }

    @Override
    public float getThrowForce() {
        return 1.5f;
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ProjectileItem;

public class ItemSnowball
extends ProjectileItem {
    public ItemSnowball() {
        this((Integer)0, 1);
    }

    public ItemSnowball(Integer n) {
        this(n, 1);
    }

    public ItemSnowball(Integer n, int n2) {
        super(332, 0, n2, "Snowball");
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }

    @Override
    public String getProjectileEntityType() {
        return "Snowball";
    }

    @Override
    public float getThrowForce() {
        return 1.5f;
    }
}


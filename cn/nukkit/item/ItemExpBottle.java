/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ProjectileItem;

public class ItemExpBottle
extends ProjectileItem {
    public ItemExpBottle() {
        this((Integer)0, 1);
    }

    public ItemExpBottle(Integer n) {
        this(n, 1);
    }

    public ItemExpBottle(Integer n, int n2) {
        super(384, n, n2, "Bottle o' Enchanting");
    }

    @Override
    public String getProjectileEntityType() {
        return "ThrownExpBottle";
    }

    @Override
    public float getThrowForce() {
        return 1.0f;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ProjectileItem;

public class ItemEnderEye
extends ProjectileItem {
    public ItemEnderEye() {
        this((Integer)0, 1);
    }

    public ItemEnderEye(Integer n) {
        this(n, 1);
    }

    public ItemEnderEye(Integer n, int n2) {
        super(381, n, n2, "Ender Eye");
    }

    @Override
    public String getProjectileEntityType() {
        return "EnderEye";
    }

    @Override
    public float getThrowForce() {
        return 1.5f;
    }
}


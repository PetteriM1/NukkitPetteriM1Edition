/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ProjectileItem;

public class ItemEnderPearl
extends ProjectileItem {
    public ItemEnderPearl() {
        this((Integer)0, 1);
    }

    public ItemEnderPearl(Integer n) {
        this(n, 1);
    }

    public ItemEnderPearl(Integer n, int n2) {
        super(368, 0, n2, "Ender Pearl");
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }

    @Override
    public String getProjectileEntityType() {
        return "EnderPearl";
    }

    @Override
    public float getThrowForce() {
        return 1.5f;
    }
}


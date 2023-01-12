/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ProjectileItem;
import cn.nukkit.nbt.tag.CompoundTag;

public class ItemPotionSplash
extends ProjectileItem {
    public ItemPotionSplash(Integer n) {
        this(n, 1);
    }

    public ItemPotionSplash(Integer n, int n2) {
        super(438, n, n2, "Splash Potion");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public String getProjectileEntityType() {
        return "ThrownPotion";
    }

    @Override
    public float getThrowForce() {
        return 0.5f;
    }

    @Override
    protected void correctNBT(CompoundTag compoundTag) {
        compoundTag.putInt("PotionId", this.meta);
    }
}


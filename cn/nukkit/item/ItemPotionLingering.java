/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.item.ProjectileItem;
import cn.nukkit.nbt.tag.CompoundTag;

public class ItemPotionLingering
extends ProjectileItem {
    public ItemPotionLingering() {
        this((Integer)0, 1);
    }

    public ItemPotionLingering(Integer n) {
        this(n, 1);
    }

    public ItemPotionLingering(Integer n, int n2) {
        super(441, n, n2, "Lingering Potion");
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
        return "ThrownLingeringPotion";
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


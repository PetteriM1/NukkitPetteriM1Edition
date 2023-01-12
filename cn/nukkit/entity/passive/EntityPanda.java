/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.entity.passive.EntityWalkingAnimal;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityPanda
extends EntityWalkingAnimal {
    public static final int NETWORK_ID = 113;

    public EntityPanda(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 113;
    }

    @Override
    public float getLength() {
        return 1.825f;
    }

    @Override
    public float getWidth() {
        return 1.125f;
    }

    @Override
    public float getHeight() {
        return 1.25f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }
}


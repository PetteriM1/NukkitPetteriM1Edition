/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.entity.passive.EntityFish;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityTadpole
extends EntityFish {
    public static final int NETWORK_ID = 133;

    public EntityTadpole(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getKillExperience() {
        return 0;
    }

    @Override
    int b() {
        return 13;
    }

    @Override
    public int getNetworkId() {
        return 133;
    }

    @Override
    public float getHeight() {
        return 0.8f;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(6);
        super.initEntity();
    }
}


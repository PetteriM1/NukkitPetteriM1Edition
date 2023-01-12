/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.entity.passive.EntityFish;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityAxolotl
extends EntityFish {
    public static final int NETWORK_ID = 130;

    public EntityAxolotl(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(14);
        super.initEntity();
    }

    @Override
    public int getNetworkId() {
        return 130;
    }

    @Override
    public float getWidth() {
        return 0.75f;
    }

    @Override
    public float getHeight() {
        return 0.42f;
    }

    @Override
    int b() {
        return 12;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.entity.passive.EntityVillagerV1;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityVillager
extends EntityVillagerV1 {
    public static final int NETWORK_ID = 115;

    public EntityVillager(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 115;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Villager";
    }
}


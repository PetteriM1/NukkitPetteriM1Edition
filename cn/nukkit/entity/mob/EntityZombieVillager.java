/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.mob.EntityZombieVillagerV1;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityZombieVillager
extends EntityZombieVillagerV1
implements EntitySmite {
    public static final int NETWORK_ID = 116;

    public EntityZombieVillager(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 116;
    }
}


package cn.nukkit.entity.item;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityPotionLingering extends EntityPotion {

    public static final int NETWORK_ID = 101;

    public EntityPotionLingering(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public EntityPotionLingering(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
}

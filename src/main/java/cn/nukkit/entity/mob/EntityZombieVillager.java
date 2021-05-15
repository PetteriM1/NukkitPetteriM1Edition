package cn.nukkit.entity.mob;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityZombieVillager extends EntityZombie {

    public static final int NETWORK_ID = 44;

    public EntityZombieVillager(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Zombie Villager";
    }
}

package cn.nukkit.entity.mob;

import cn.nukkit.entity.EntitySmite;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityZombieVillagerV2 extends EntityZombieVillager implements EntitySmite {

    public static final int NETWORK_ID = 116;

    public EntityZombieVillagerV2(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
}

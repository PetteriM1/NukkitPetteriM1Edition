package cn.nukkit.entity.passive;

import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.level.format.FullChunk;

/**
 * Created by PetteriM1
 */
public abstract class EntityFish extends EntityWaterAnimal {
    public EntityFish(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
}
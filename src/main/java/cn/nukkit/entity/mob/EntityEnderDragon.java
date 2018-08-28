package cn.nukkit.entity.mob;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityFlyingMob;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityEnderDragon extends EntityFlyingMob {

    public static final int NETWORK_ID = 53;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityEnderDragon(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getWidth() {
        return 13f;
    }

    @Override
    public float getHeight() {
        return 4f;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(200);
    }

    @Override
    public int getKillExperience() {
        return 500;
    }

    @Override
    public void attackEntity(Entity player) {
        //TODO
    }
}

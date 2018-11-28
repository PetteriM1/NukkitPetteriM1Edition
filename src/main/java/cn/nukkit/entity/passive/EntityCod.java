package cn.nukkit.entity.passive;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityCod extends EntityFish {

    public static final int NETWORK_ID = 112;

    public EntityCod(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.5f;
    }

    @Override
    public float getHeight() {
        return 0.2f;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(3);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return false;
    }

    @Override
    public int getKillExperience() {
        return 0;
    }
}

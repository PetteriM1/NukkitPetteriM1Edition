package cn.nukkit.entity.passive;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.passive.EntityWalkingAnimal;
import cn.nukkit.entity.EntityUtils;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class EntitySkeletonHorse extends EntityWalkingAnimal {

    public static final int NETWORK_ID = 26;

    public EntitySkeletonHorse(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.6982f;
        }
        return 1.3965f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.8f;
        }
        return 1.6f;
    }

    @Override
    public float getMaxJumpHeight() {
        return 2;
    }

    @Override
    public boolean isBaby() {
        return this.getDataFlag(DATA_FLAGS, Entity.DATA_FLAG_BABY);
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(15);
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int leather = EntityUtils.rand(0, 3);
            int bone = EntityUtils.rand(0, 2);

            for (int i = 0; i < leather; i++) {
                drops.add(Item.get(Item.LEATHER, 0, 1));
            }

            for (int i = 0; i < bone; i++) {
                drops.add(Item.get(Item.BONE, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }

    @Override
    public int getKillExperience() {
        return EntityUtils.rand(1, 4);
    }
}

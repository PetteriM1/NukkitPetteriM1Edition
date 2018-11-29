package cn.nukkit.entity.passive;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityPufferfish extends EntityFish {

    public static final int NETWORK_ID = 108;

    public EntityPufferfish(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.35f;
    }

    @Override
    public float getHeight() {
        return 0.35f;
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
    public Item[] getDrops() {
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && !this.isBaby()) {
            return new Item[]{Item.get(Item.PUFFERFISH, 0, 1)};
        } else {
            return new Item[0];
        }
    }

    @Override
    public int getKillExperience() {
        return 0;
    }
}

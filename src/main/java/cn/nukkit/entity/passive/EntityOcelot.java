package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.passive.EntityWalkingAnimal;
import cn.nukkit.entity.EntityUtils;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityOcelot extends EntityWalkingAnimal {

    public static final int NETWORK_ID = 22;

    public EntityOcelot(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.3f;
        }
        return 0.6f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.35f;
        }
        return 0.7f;
    }

    @Override
    public double getSpeed() {
        return 1.4;
    }

    @Override
    public boolean isBaby() {
        return this.getDataFlag(DATA_FLAGS, Entity.DATA_FLAG_BABY);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(10);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed && player.getInventory().getItemInHand().getId() == Item.RAW_FISH && distance <= 40;
        }
        return false;
    }

    public Item[] getDrops() {
        return new Item[0];
    }

    public int getKillExperience() {
        return EntityUtils.rand(1, 4);
    }
}

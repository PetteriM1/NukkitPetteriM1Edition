package cn.nukkit.entity.passive;

import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityUtils;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDye;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.utils.DyeColor;

public class EntitySquid extends EntityWaterAnimal {

    public static final int NETWORK_ID = 17;

    public EntitySquid(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.8f;
    }

    @Override
    public float getHeight() {
        return 0.8f;
    }

    @Override
    public float getEyeHeight() {
        return 0.7f;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(10);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return false;
    }

    @Override
    public Item[] getDrops() {
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            return new Item[]{new ItemDye(DyeColor.BLACK.getDyeData())};
        } else {
            return new Item[0];
        }
    }

    @Override
    public int getKillExperience() {
        return EntityUtils.rand(1, 4);
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        boolean att =  super.attack(source);
        if (source.isCancelled()) {
            return att;
        }

        EntityEventPacket pk0 = new EntityEventPacket();
        pk0.eid = this.getId();
        pk0.event = EntityEventPacket.SQUID_INK_CLOUD;
        this.level.addChunkPacket(this.getChunkX() >> 4,this.getChunkZ() >> 4,pk0);
        return att;
    }
}

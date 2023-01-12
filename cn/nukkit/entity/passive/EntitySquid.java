/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.entity.passive.EntityWaterAnimal;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.Utils;

public class EntitySquid
extends EntityWaterAnimal {
    public static final int NETWORK_ID = 17;

    public EntitySquid(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 17;
    }

    @Override
    public float getWidth() {
        return 0.95f;
    }

    @Override
    public float getHeight() {
        return 0.95f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(10);
        super.initEntity();
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(351, DyeColor.BLACK.getDyeData(), Utils.rand(1, 3))};
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 3);
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        boolean bl = super.attack(entityDamageEvent);
        if (entityDamageEvent.isCancelled()) {
            return bl;
        }
        EntityEventPacket entityEventPacket = new EntityEventPacket();
        entityEventPacket.eid = this.getId();
        entityEventPacket.event = 15;
        this.level.addChunkPacket(this.getChunkX(), this.getChunkZ(), entityEventPacket);
        return bl;
    }
}


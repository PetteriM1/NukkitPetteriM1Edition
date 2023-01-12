/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.passive.EntityFish;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Utils;

public class EntityPufferfish
extends EntityFish {
    public static final int NETWORK_ID = 108;
    private int t = 0;

    public EntityPufferfish(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    int b() {
        return 5;
    }

    @Override
    public int getNetworkId() {
        return 108;
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
        this.setMaxHealth(3);
        super.initEntity();
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(462, 0, 1), Item.get(352, 0, Utils.rand(0, 2))};
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        Entity entity;
        super.attack(entityDamageEvent);
        if (entityDamageEvent instanceof EntityDamageByEntityEvent && entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && (entity = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager()) instanceof Player) {
            if (this.isPuffed()) {
                return true;
            }
            this.t = 200;
            entity.addEffect(Effect.getEffect(19).setDuration(200));
            this.setDataProperty(new ByteEntityData(86, 2));
        }
        return true;
    }

    @Override
    public boolean entityBaseTick(int n) {
        if (this.t == 0 && this.getDataPropertyByte(86) == 2) {
            this.setDataProperty(new ByteEntityData(86, 0));
        }
        if (this.t > 0) {
            --this.t;
        }
        return super.entityBaseTick(n);
    }

    public boolean isPuffed() {
        return this.t > 0;
    }
}


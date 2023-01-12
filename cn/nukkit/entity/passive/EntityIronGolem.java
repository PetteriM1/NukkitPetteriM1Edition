/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.entity.passive.EntityWolf;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityIronGolem
extends EntityWalkingMob {
    public static final int NETWORK_ID = 20;

    public EntityIronGolem(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 20;
    }

    @Override
    public float getWidth() {
        return 1.4f;
    }

    @Override
    public float getHeight() {
        return 2.9f;
    }

    @Override
    public double getSpeed() {
        return 0.7;
    }

    @Override
    public void initEntity() {
        this.setFriendly(true);
        this.setMaxHealth(100);
        super.initEntity();
        this.noFallDamage = true;
        this.setDamage(new int[]{0, 11, 21, 31});
        this.setMinDamage(new int[]{0, 4, 7, 11});
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 40 && this.distanceSquared(entity) < 4.0) {
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> hashMap = new HashMap<EntityDamageEvent.DamageModifier, Float>();
            hashMap.put(EntityDamageEvent.DamageModifier.BASE, Float.valueOf(this.getDamage()));
            if (entity instanceof Player) {
                float f2 = 0.0f;
                for (Item item : ((Player)entity).getInventory().getArmorContents()) {
                    f2 += this.getArmorPoints(item.getId());
                }
                hashMap.put(EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf((float)((double)hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf(0.0f)).floatValue() - Math.floor((double)(hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.BASE, Float.valueOf(1.0f)).floatValue() * f2) * 0.04))));
            }
            entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, hashMap));
            this.playAttack();
        }
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        return (!(entityCreature instanceof Player) || entityCreature.getId() == this.isAngryTo) && !(entityCreature instanceof EntityWolf) && entityCreature.isAlive() && d2 <= 100.0;
    }

    @Override
    public Item[] getDrops() {
        int n;
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (n = 0; n < Utils.rand(3, 5); ++n) {
            arrayList.add(Item.get(265, 0, 1));
        }
        for (n = 0; n < Utils.rand(0, 2); ++n) {
            arrayList.add(Item.get(38, 0, 1));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return 0;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Iron Golem";
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (super.attack(entityDamageEvent)) {
            Entity entity;
            if (entityDamageEvent instanceof EntityDamageByEntityEvent && (!((entity = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager()) instanceof Player) || ((Player)entity).isSurvival() || ((Player)entity).isAdventure())) {
                this.isAngryTo = entity.getId();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canTarget(Entity entity) {
        return entity.canBeFollowed() && entity.getId() == this.isAngryTo;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityPolarBear
extends EntityWalkingMob {
    public static final int NETWORK_ID = 28;
    private int w = 0;

    public EntityPolarBear(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 28;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.65f;
        }
        return 1.3f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.7f;
        }
        return 1.4f;
    }

    @Override
    public double getSpeed() {
        return 1.25;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(30);
        super.initEntity();
        this.setDamage(new int[]{0, 4, 6, 9});
        if (this.namedTag.contains("Angry")) {
            this.w = this.namedTag.getInt("Angry");
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("Angry", this.w);
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        return this.isAngry() && super.targetOption(entityCreature, d2);
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 23 && this.distanceSquared(entity) < 1.44) {
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
        }
    }

    public boolean isAngry() {
        return this.w > 0;
    }

    public void setAngry(int n) {
        this.w = n;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        super.attack(entityDamageEvent);
        if (!entityDamageEvent.isCancelled() && entityDamageEvent instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)entityDamageEvent).getDamager() instanceof Player) {
            this.setAngry(2400);
        }
        return true;
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (!this.isBaby()) {
            arrayList.add(Item.get(349, 0, Utils.rand(0, 2)));
            arrayList.add(Item.get(460, 0, Utils.rand(0, 2)));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Polar Bear";
    }

    @Override
    public boolean entityBaseTick(int n) {
        if (this.w > 0) {
            --this.w;
        }
        return super.entityBaseTick(n);
    }
}


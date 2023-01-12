/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.mob.EntitySwimmingMob;
import cn.nukkit.entity.passive.EntitySquid;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.HashMap;

public class EntityGuardian
extends EntitySwimmingMob {
    public static final int NETWORK_ID = 49;
    private int A = 60;
    private long w = -1L;

    public EntityGuardian(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 49;
    }

    @Override
    public float getWidth() {
        return 0.85f;
    }

    @Override
    public float getHeight() {
        return 0.85f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(30);
        super.initEntity();
        this.setDamage(new int[]{0, 4, 6, 9});
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            return !player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure()) && d2 <= 100.0;
        }
        if (entityCreature instanceof EntitySquid) {
            return entityCreature.isAlive() && this.distanceSquared(entityCreature) <= 80.0;
        }
        return false;
    }

    @Override
    public void attackEntity(Entity entity) {
        HashMap<EntityDamageEvent.DamageModifier, Float> hashMap = new HashMap<EntityDamageEvent.DamageModifier, Float>();
        hashMap.put(EntityDamageEvent.DamageModifier.BASE, Float.valueOf(1.0f));
        if (entity instanceof Player) {
            float f2 = 0.0f;
            for (Item item : ((Player)entity).getInventory().getArmorContents()) {
                f2 += this.getArmorPoints(item.getId());
            }
            hashMap.put(EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf((float)((double)hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.ARMOR, Float.valueOf(0.0f)).floatValue() - Math.floor((double)(hashMap.getOrDefault((Object)EntityDamageEvent.DamageModifier.BASE, Float.valueOf(1.0f)).floatValue() * f2) * 0.04))));
        }
        entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, hashMap));
    }

    @Override
    public boolean entityBaseTick(int n) {
        if (this.getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }
        boolean bl = super.entityBaseTick(n);
        if (!this.closed && this.followTarget != null) {
            if (this.w != this.followTarget.getId()) {
                this.w = this.followTarget.getId();
                this.setDataProperty(new LongEntityData(6, this.w));
                this.A = 60;
            }
            if (this.targetOption((EntityCreature)this.followTarget, this.distanceSquared(this.followTarget))) {
                if (--this.A < 0) {
                    if (this.getServer().getMobAiEnabled()) {
                        this.attackEntity(this.followTarget);
                    }
                    this.w = -1L;
                    this.setDataProperty(new LongEntityData(6, -1L));
                    this.A = 60;
                }
            } else {
                this.w = -1L;
                this.setDataProperty(new LongEntityData(6, -1L));
                this.A = 60;
            }
        }
        return bl;
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(409, 0, Utils.rand(0, 2))};
    }

    @Override
    public int getKillExperience() {
        return 10;
    }
}


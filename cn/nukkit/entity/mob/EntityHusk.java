/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Utils;
import java.util.HashMap;

public class EntityHusk
extends EntityWalkingMob
implements EntitySmite {
    public static final int NETWORK_ID = 47;

    public EntityHusk(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 47;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.95f;
    }

    @Override
    public double getSpeed() {
        return 1.1;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();
        this.setDamage(new int[]{0, 3, 4, 6});
    }

    public void setHealth(int n) {
        super.setHealth(n);
        if (this.isAlive()) {
            if (15.0f < this.getHealth()) {
                this.setDamage(new int[]{0, 2, 3, 4});
            } else if (10.0f < this.getHealth()) {
                this.setDamage(new int[]{0, 3, 4, 6});
            } else if (5.0f < this.getHealth()) {
                this.setDamage(new int[]{0, 3, 5, 7});
            } else {
                this.setDamage(new int[]{0, 4, 6, 9});
            }
        }
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 23 && entity.distanceSquared(this) <= 1.0) {
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
            if (entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, hashMap))) {
                entity.addEffect(Effect.getEffect(17).setDuration(140));
            }
            this.playAttack();
        }
    }

    @Override
    public Item[] getDrops() {
        Item[] itemArray;
        if (this.isBaby()) {
            itemArray = new Item[]{};
        } else {
            Item[] itemArray2 = new Item[1];
            itemArray = itemArray2;
            itemArray2[0] = Item.get(367, 0, Utils.rand(0, 2));
        }
        return itemArray;
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : 5;
    }
}


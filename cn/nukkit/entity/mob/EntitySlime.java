/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityJumpingMob;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;

public class EntitySlime
extends EntityJumpingMob {
    public static final int NETWORK_ID = 37;
    public static final int SIZE_SMALL = 1;
    public static final int SIZE_MEDIUM = 2;
    public static final int SIZE_BIG = 3;
    private int w = 3;

    public EntitySlime(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 37;
    }

    @Override
    public float getWidth() {
        return 0.51f + (float)this.w * 0.51f;
    }

    @Override
    public float getHeight() {
        return 0.51f + (float)this.w * 0.51f;
    }

    @Override
    public float getLength() {
        return 0.51f + (float)this.w * 0.51f;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.w = this.namedTag.contains("Size") ? this.namedTag.getInt("Size") : Utils.rand(1, 3);
        this.setScale(0.51f + (float)this.w * 0.51f);
        if (this.w == 3) {
            this.setMaxHealth(16);
        } else if (this.w == 2) {
            this.setMaxHealth(4);
        } else if (this.w == 1) {
            this.setMaxHealth(1);
        }
        if (this.w == 3) {
            this.setDamage(new int[]{0, 3, 4, 6});
        } else if (this.w == 2) {
            this.setDamage(new int[]{0, 2, 2, 3});
        } else {
            this.setDamage(Utils.emptyDamageArray);
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("Size", this.getSlimeSize());
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 23 && this.distanceSquared(entity) < 1.0) {
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

    @Override
    public Item[] getDrops() {
        if (this.w == 3) {
            CreatureSpawnEvent creatureSpawnEvent = new CreatureSpawnEvent(37, this, CreatureSpawnEvent.SpawnReason.SLIME_SPLIT);
            this.level.getServer().getPluginManager().callEvent(creatureSpawnEvent);
            if (creatureSpawnEvent.isCancelled()) {
                return new Item[0];
            }
            EntitySlime entitySlime = (EntitySlime)Entity.createEntity("Slime", (Position)this, new Object[0]);
            if (entitySlime != null) {
                entitySlime.w = 2;
                entitySlime.setScale(1.53f);
                entitySlime.spawnToAll();
            }
            return new Item[0];
        }
        if (this.w == 2) {
            CreatureSpawnEvent creatureSpawnEvent = new CreatureSpawnEvent(37, this, CreatureSpawnEvent.SpawnReason.SLIME_SPLIT);
            this.level.getServer().getPluginManager().callEvent(creatureSpawnEvent);
            if (creatureSpawnEvent.isCancelled()) {
                return new Item[0];
            }
            EntitySlime entitySlime = (EntitySlime)Entity.createEntity("Slime", (Position)this, new Object[0]);
            if (entitySlime != null) {
                entitySlime.w = 1;
                entitySlime.setScale(1.02f);
                entitySlime.spawnToAll();
            }
            return new Item[0];
        }
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (int k = 0; k < Utils.rand(0, 2); ++k) {
            arrayList.add(Item.get(341, 0, 1));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        if (this.w == 3) {
            return 4;
        }
        if (this.w == 2) {
            return 2;
        }
        if (this.w == 1) {
            return 1;
        }
        return 0;
    }

    public int getSlimeSize() {
        return this.w;
    }
}


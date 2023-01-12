/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.entity.projectile.EntityThrownTrident;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.utils.Utils;
import co.aikar.timings.Timings;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityDrowned
extends EntityWalkingMob
implements EntitySmite {
    public static final int NETWORK_ID = 110;
    private Item w;

    public EntityDrowned(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 110;
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
    protected void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();
        this.setDamage(new int[]{0, 2, 3, 4});
        if (this.namedTag.contains("Item")) {
            this.w = NBTIO.getItemHelper(this.namedTag.getCompound("Item"));
        } else if (!this.namedTag.getBoolean("HandItemSet")) {
            this.b();
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
            entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, hashMap));
            this.playAttack();
        } else if (this.w != null && this.w.getId() == 455 && this.attackDelay > 120 && Utils.rand(1, 32) < 4 && this.distanceSquared(entity) <= 55.0) {
            this.attackDelay = 0;
            double d2 = 1.3;
            double d3 = this.yaw;
            double d4 = this.pitch;
            double d5 = FastMathLite.toRadians(d3);
            double d6 = FastMathLite.toRadians(d4);
            Location location = new Location(this.x - Math.sin(d5) * Math.cos(d6) * 0.5, this.y + (double)this.getHeight() - 0.18, this.z + Math.cos(d5) * Math.cos(d6) * 0.5, d3, d4, this.level);
            if (this.getLevel().getBlockIdAt(this.chunk, location.getFloorX(), location.getFloorY(), location.getFloorZ()) == 0) {
                Entity entity2 = Entity.createEntity("ThrownTrident", (Position)location, this);
                if (!(entity2 instanceof EntityThrownTrident)) {
                    return;
                }
                EntityThrownTrident entityThrownTrident = (EntityThrownTrident)entity2;
                EntityDrowned.setProjectileMotion(entityThrownTrident, d4, d5, d6, d2);
                entityThrownTrident.setShotByPlayer(false);
                EntityShootBowEvent entityShootBowEvent = new EntityShootBowEvent(this, Item.get(455, 0, 1), entityThrownTrident, d2);
                this.server.getPluginManager().callEvent(entityShootBowEvent);
                EntityProjectile entityProjectile = entityShootBowEvent.getProjectile();
                if (entityShootBowEvent.isCancelled()) {
                    entityProjectile.close();
                } else {
                    ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(entityProjectile);
                    this.server.getPluginManager().callEvent(projectileLaunchEvent);
                    if (projectileLaunchEvent.isCancelled()) {
                        entityProjectile.close();
                    } else {
                        entityProjectile.spawnToAll();
                        ((EntityThrownTrident)entityProjectile).setPickupMode(0);
                        this.level.addLevelSoundEvent(this, 183);
                    }
                }
            }
        }
    }

    @Override
    public boolean entityBaseTick(int n) {
        if (this.getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }
        if (Timings.entityBaseTickTimer != null) {
            Timings.entityBaseTickTimer.startTiming();
        }
        boolean bl = super.entityBaseTick(n);
        if (!this.closed && this.level.shouldMobBurn(this)) {
            this.setOnFire(100);
        }
        if (Timings.entityBaseTickTimer != null) {
            Timings.entityBaseTickTimer.stopTiming();
        }
        return bl;
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (!this.isBaby()) {
            for (int k = 0; k < Utils.rand(0, 2); ++k) {
                arrayList.add(Item.get(367, 0, 1));
            }
            if (Utils.rand(1, 100) <= 11) {
                arrayList.add(Item.get(266, 0, 1));
            }
            if (this.w != null && Utils.rand(1, 4) == 1) {
                arrayList.add(this.w);
            } else if (this.w == null && Utils.rand(1, 80) <= 3) {
                arrayList.add(Item.get(455, Utils.rand(230, 246), 1));
            }
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : 5;
    }

    private void b() {
        switch (Utils.rand(1, 3)) {
            case 1: {
                if (Utils.rand(1, 100) <= 15) {
                    this.w = Item.get(455, Utils.rand(200, 246), 1);
                }
                return;
            }
            case 2: {
                if (Utils.rand(1, 100) == 1) {
                    this.w = Item.get(346, Utils.rand(51, 61), 1);
                }
                return;
            }
            case 3: {
                if (Utils.rand(1, 100) > 8) break;
                this.w = Item.get(465, 0, 1);
            }
        }
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);
        if (this.w != null) {
            MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
            mobEquipmentPacket.eid = this.getId();
            mobEquipmentPacket.hotbarSlot = 0;
            mobEquipmentPacket.item = this.w;
            player.dataPacket(mobEquipmentPacket);
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("HandItemSet", true);
        if (this.w != null) {
            this.namedTag.put("Item", NBTIO.putItemHelper(this.w));
        }
    }

    public Item getTool() {
        return this.w;
    }
}


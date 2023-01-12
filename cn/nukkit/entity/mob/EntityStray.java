/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.Vector2;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntityStray
extends EntityWalkingMob
implements EntitySmite {
    public static final int NETWORK_ID = 46;
    private boolean w;

    public EntityStray(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();
    }

    @Override
    public int getNetworkId() {
        return 46;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.99f;
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);
        MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
        mobEquipmentPacket.eid = this.getId();
        mobEquipmentPacket.item = Item.get(261);
        mobEquipmentPacket.hotbarSlot = 0;
        player.dataPacket(mobEquipmentPacket);
    }

    @Override
    public boolean entityBaseTick(int n) {
        if (this.getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }
        boolean bl = super.entityBaseTick(n);
        if (!this.closed && this.level.shouldMobBurn(this)) {
            this.setOnFire(100);
        }
        return bl;
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 23 && Utils.rand(1, 32) < 4 && this.distanceSquared(entity) <= 55.0) {
            this.attackDelay = 0;
            double d2 = 1.3;
            double d3 = FastMathLite.toRadians(this.yaw);
            double d4 = FastMathLite.toRadians(this.pitch);
            Location location = new Location(this.x - Math.sin(d3) * Math.cos(d4) * 0.5, this.y + (double)this.getHeight() - 0.18, this.z + Math.cos(d3) * Math.cos(d4) * 0.5, this.yaw, this.pitch, this.level);
            if (this.getLevel().getBlockIdAt(this.chunk, location.getFloorX(), location.getFloorY(), location.getFloorZ()) == 0) {
                EntityArrow entityArrow = (EntityArrow)Entity.createEntity("Arrow", (Position)location, this);
                entityArrow.setData(19);
                EntityStray.setProjectileMotion(entityArrow, this.pitch, d3, d4, d2);
                EntityShootBowEvent entityShootBowEvent = new EntityShootBowEvent(this, Item.get(262, 0, 1), entityArrow, d2);
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
                        ((EntityArrow)entityProjectile).setPickupMode(0);
                        this.level.addLevelSoundEvent(this, 21);
                    }
                }
            }
        }
    }

    @Override
    public Item[] getDrops() {
        int n;
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (n = 0; n < Utils.rand(0, 2); ++n) {
            arrayList.add(Item.get(352, 0, 1));
        }
        for (n = 0; n < Utils.rand(0, 2); ++n) {
            arrayList.add(Item.get(262, 0, 1));
        }
        if (Utils.rand()) {
            arrayList.add(Item.get(262, 18, 1));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public int nearbyDistanceMultiplier() {
        return 10;
    }

    @Override
    public void kill() {
        Entity entity;
        if (!this.isAlive()) {
            return;
        }
        super.kill();
        if (this.lastDamageCause instanceof EntityDamageByChildEntityEvent && ((EntityDamageByChildEntityEvent)this.lastDamageCause).getChild() instanceof EntityArrow && (entity = ((EntityDamageByChildEntityEvent)this.lastDamageCause).getDamager()) instanceof Player) {
            Vector2 vector2 = new Vector2(this.x, this.z);
            Vector2 vector22 = new Vector2(entity.x, entity.z);
            if (vector2.distance(vector22) >= 50.0) {
                ((Player)entity).awardAchievement("snipeSkeleton");
            }
        }
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        boolean bl = super.targetOption(entityCreature, d2);
        if (bl) {
            if (!this.w && entityCreature != null) {
                this.setDataProperty(new LongEntityData(6, entityCreature.getId()));
                this.w = true;
            }
        } else if (this.w) {
            this.setDataProperty(new LongEntityData(6, 0L));
            this.w = false;
            this.stayTime = 100;
        }
        return bl;
    }
}


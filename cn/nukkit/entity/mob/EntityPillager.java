/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntityPillager
extends EntityWalkingMob {
    public static final int NETWORK_ID = 114;
    private boolean w;

    public EntityPillager(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 114;
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
    public void initEntity() {
        this.setMaxHealth(24);
        super.initEntity();
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 80 && Utils.rand(1, 32) < 4 && this.distanceSquared(entity) <= 100.0) {
            this.attackDelay = 0;
            double d2 = 1.5;
            double d3 = this.yaw;
            double d4 = this.pitch;
            double d5 = FastMathLite.toRadians(d3);
            double d6 = FastMathLite.toRadians(d4);
            Location location = new Location(this.x - Math.sin(d5) * Math.cos(d6) * 0.5, this.y + (double)this.getHeight() - 0.18, this.z + Math.cos(d5) * Math.cos(d6) * 0.5, d3, d4, this.level);
            if (this.getLevel().getBlockIdAt(this.chunk, location.getFloorX(), location.getFloorY(), location.getFloorZ()) == 0) {
                Entity entity2 = Entity.createEntity("Arrow", (Position)location, this);
                if (!(entity2 instanceof EntityArrow)) {
                    return;
                }
                EntityArrow entityArrow = (EntityArrow)entity2;
                EntityPillager.setProjectileMotion(entityArrow, d4, d5, d6, d2);
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
                        entityProjectile.namedTag.putDouble("damage", 4.0);
                        entityProjectile.spawnToAll();
                        ((EntityArrow)entityProjectile).setPickupMode(0);
                        this.level.addLevelSoundEvent(this, 248);
                    }
                }
            }
        }
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (int k = 0; k < Utils.rand(0, 2); ++k) {
            arrayList.add(Item.get(262, 0, 1));
        }
        if (Utils.rand(1, 12) == 1) {
            arrayList.add(Item.get(471, Utils.rand(300, 380), Utils.rand(0, 1)));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);
        MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
        mobEquipmentPacket.eid = this.getId();
        mobEquipmentPacket.item = Item.get(471, 0, 1);
        mobEquipmentPacket.hotbarSlot = 0;
        player.dataPacket(mobEquipmentPacket);
    }

    @Override
    public int nearbyDistanceMultiplier() {
        return 20;
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        boolean bl = super.targetOption(entityCreature, d2);
        if (bl) {
            if (!this.w) {
                this.setDataFlag(0, 27, true);
                this.w = true;
            }
        } else if (this.w) {
            this.setDataFlag(0, 27, false);
            this.w = false;
            this.stayTime = 100;
        }
        return bl;
    }
}


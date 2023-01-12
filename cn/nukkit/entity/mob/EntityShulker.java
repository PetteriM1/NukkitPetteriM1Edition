/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.entity.projectile.EntityShulkerBullet;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityShulker
extends EntityWalkingMob {
    public static final int NETWORK_ID = 54;

    public EntityShulker(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 54;
    }

    @Override
    public float getWidth() {
        return 1.0f;
    }

    @Override
    public float getHeight() {
        return 1.0f;
    }

    @Override
    public double getSpeed() {
        return 0.0;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(15);
        super.initEntity();
        this.fireProof = true;
        this.noFallDamage = true;
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay > 60 && Utils.rand(1, 32) < 4 && this.distanceSquared(entity) <= 256.0) {
            this.attackDelay = 0;
            double d2 = 0.5;
            double d3 = this.yaw + Utils.rand(-4.0, 4.0);
            double d4 = this.pitch + Utils.rand(-4.0, 4.0);
            Location location = new Location(this.x - Math.sin(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * 0.5, this.y + (double)this.getHeight() - 0.18, this.z + Math.cos(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * 0.5, d3, d4, this.level);
            if (this.getLevel().getBlockIdAt(this.chunk, location.getFloorX(), location.getFloorY(), location.getFloorZ()) != 0) {
                return;
            }
            Entity entity2 = Entity.createEntity("ShulkerBullet", (Position)location, this);
            if (!(entity2 instanceof EntityShulkerBullet)) {
                return;
            }
            EntityShulkerBullet entityShulkerBullet = (EntityShulkerBullet)entity2;
            entityShulkerBullet.setMotion(new Vector3(-Math.sin(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * d2 * d2, -Math.sin(FastMathLite.toRadians(d4)) * d2 * d2, Math.cos(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * d2 * d2));
            ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(entityShulkerBullet);
            this.server.getPluginManager().callEvent(projectileLaunchEvent);
            if (projectileLaunchEvent.isCancelled()) {
                entityShulkerBullet.close();
            } else {
                entityShulkerBullet.spawnToAll();
                this.level.addSound((Vector3)this, Sound.MOB_SHULKER_SHOOT);
            }
        }
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        super.attack(entityDamageEvent);
        if (!entityDamageEvent.isCancelled() && Utils.rand(1, 10) == 1) {
            this.level.addLevelSoundEvent(this, 118);
            this.move(Utils.rand(-10, 10), 0.0, Utils.rand(-10, 10));
            this.level.addLevelSoundEvent(this, 118);
        }
        return true;
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(445, 0, Utils.rand(0, 1))};
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public void knockBack(Entity entity, double d2, double d3, double d4, double d5) {
    }
}


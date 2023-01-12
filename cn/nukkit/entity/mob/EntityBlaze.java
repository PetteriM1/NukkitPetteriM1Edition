/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityFlyingMob;
import cn.nukkit.entity.projectile.EntityBlazeFireBall;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityBlaze
extends EntityFlyingMob {
    public static final int NETWORK_ID = 43;
    private int w;

    public EntityBlaze(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 43;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.8f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();
        this.fireProof = true;
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            return !player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure()) && d2 <= 2304.0;
        }
        return false;
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.attackDelay == 60 && this.w == 0) {
            this.setDataFlag(0, 27, true);
        }
        if ((this.w > 0 && this.w < 3 && this.attackDelay > 5 || this.attackDelay > 120 && Utils.rand(1, 32) < 4) && this.distanceSquared(entity) <= 256.0) {
            this.attackDelay = 0;
            ++this.w;
            if (this.w == 3) {
                this.w = 0;
                this.setDataFlag(0, 27, false);
            }
            double d2 = 1.1;
            double d3 = this.yaw + Utils.rand(-4.0, 4.0);
            double d4 = this.pitch + Utils.rand(-4.0, 4.0);
            Location location = new Location(this.x - Math.sin(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * 0.5, this.y + (double)this.getEyeHeight(), this.z + Math.cos(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * 0.5, d3, d4, this.level);
            if (this.getLevel().getBlockIdAt(this.chunk, location.getFloorX(), location.getFloorY(), location.getFloorZ()) != 0) {
                return;
            }
            EntityBlazeFireBall entityBlazeFireBall = (EntityBlazeFireBall)Entity.createEntity("BlazeFireBall", (Position)location, this);
            entityBlazeFireBall.setMotion(new Vector3(-Math.sin(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * d2 * d2, -Math.sin(FastMathLite.toRadians(d4)) * d2 * d2, Math.cos(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * d2 * d2));
            ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(entityBlazeFireBall);
            this.server.getPluginManager().callEvent(projectileLaunchEvent);
            if (projectileLaunchEvent.isCancelled()) {
                entityBlazeFireBall.close();
            } else {
                entityBlazeFireBall.spawnToAll();
                this.level.addLevelEvent(this, 1009);
            }
        }
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(369, 0, Utils.rand(0, 1))};
    }

    @Override
    public int getKillExperience() {
        return 10;
    }

    @Override
    public int nearbyDistanceMultiplier() {
        return 1000;
    }
}


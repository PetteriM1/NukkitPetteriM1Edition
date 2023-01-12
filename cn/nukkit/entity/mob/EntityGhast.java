/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityFlyingMob;
import cn.nukkit.entity.projectile.EntityGhastFireBall;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntityGhast
extends EntityFlyingMob {
    public static final int NETWORK_ID = 41;
    private boolean w;

    public EntityGhast(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 41;
    }

    @Override
    public float getWidth() {
        return 4.0f;
    }

    @Override
    public float getHeight() {
        return 4.0f;
    }

    @Override
    public double getSpeed() {
        return 1.2;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(10);
        super.initEntity();
        this.fireProof = true;
        this.setDataFlag(0, 50, true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (!(entityCreature instanceof Player)) return false;
        Player player = (Player)entityCreature;
        if (player.closed) return false;
        if (!player.spawned) return false;
        if (!player.isAlive()) return false;
        if (!player.isSurvival()) {
            if (!player.isAdventure()) return false;
        }
        int n = this.w ? 4096 : 784;
        if (!(d2 <= (double)n)) return false;
        return true;
    }

    @Override
    public void attackEntity(Entity entity) {
        double d2 = this.distanceSquared(entity);
        int n = this.w ? 4096 : 784;
        if (d2 <= (double)n) {
            if (Utils.rand()) {
                --this.attackDelay;
                return;
            }
            if (this.attackDelay == 50) {
                this.level.addLevelEvent(this, 1007);
            }
            if (this.attackDelay > 60) {
                this.attackDelay = 0;
                double d3 = 1.01;
                double d4 = this.yaw + Utils.rand(-4.0, 4.0);
                double d5 = this.pitch + Utils.rand(-4.0, 4.0);
                Location location = new Location(this.x - Math.sin(FastMathLite.toRadians(d4)) * Math.cos(FastMathLite.toRadians(d5)) * 0.5, this.y + (double)this.getEyeHeight() - 1.0, this.z + Math.cos(FastMathLite.toRadians(d4)) * Math.cos(FastMathLite.toRadians(d5)) * 0.5, d4, d5, this.level);
                if (this.getLevel().getBlockIdAt(this.chunk, location.getFloorX(), location.getFloorY(), location.getFloorZ()) != 0) {
                    return;
                }
                EntityGhastFireBall entityGhastFireBall = (EntityGhastFireBall)Entity.createEntity("GhastFireBall", (Position)location, this);
                entityGhastFireBall.setExplode(true);
                entityGhastFireBall.setMotion(new Vector3(-Math.sin(FastMathLite.toRadians(d4)) * Math.cos(FastMathLite.toRadians(d5)) * d3 * d3, -Math.sin(FastMathLite.toRadians(d5)) * d3 * d3, Math.cos(FastMathLite.toRadians(d4)) * Math.cos(FastMathLite.toRadians(d5)) * d3 * d3));
                ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(entityGhastFireBall);
                this.server.getPluginManager().callEvent(projectileLaunchEvent);
                if (projectileLaunchEvent.isCancelled()) {
                    entityGhastFireBall.close();
                } else {
                    entityGhastFireBall.spawnToAll();
                    this.level.addLevelEvent(this, 1008);
                }
            }
        }
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        boolean bl = super.attack(entityDamageEvent);
        if (!entityDamageEvent.isCancelled() && entityDamageEvent instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)entityDamageEvent).getDamager() instanceof Player) {
            this.w = true;
        }
        return bl;
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (int k = 0; k < Utils.rand(0, 2); ++k) {
            arrayList.add(Item.get(289, 0, 1));
        }
        arrayList.add(Item.get(370, 0, Utils.rand(0, 1)));
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public int nearbyDistanceMultiplier() {
        return 1000;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityBoss;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.mob.EntityFlyingMob;
import cn.nukkit.entity.projectile.EntityBlueWitherSkull;
import cn.nukkit.entity.projectile.EntityWitherSkull;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Utils;

public class EntityWither
extends EntityFlyingMob
implements EntityBoss,
EntitySmite {
    public static final int NETWORK_ID = 52;
    private boolean w;

    public EntityWither(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 52;
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public float getHeight() {
        return 3.5f;
    }

    @Override
    public double getSpeed() {
        return 1.3;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(this.b());
        super.initEntity();
        this.fireProof = true;
        this.setDamage(new int[]{0, 2, 4, 6});
        if (this.age == 0) {
            this.setDataProperty(new IntEntityData(48, 200));
        }
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        Player player;
        if (entityCreature instanceof Player && !(player = (Player)entityCreature).isSurvival() && !player.isAdventure()) {
            return false;
        }
        return entityCreature.isAlive() && !entityCreature.closed && d2 <= 10000.0;
    }

    @Override
    public int getKillExperience() {
        return 50;
    }

    @Override
    public void attackEntity(Entity entity) {
        if (this.age > 220 && this.attackDelay > 40 && this.distanceSquared(entity) <= 4096.0) {
            EntityWitherSkull entityWitherSkull;
            this.attackDelay = 0;
            double d2 = 1.0;
            double d3 = this.yaw + Utils.rand(-4.0, 4.0);
            double d4 = this.pitch + Utils.rand(-4.0, 4.0);
            Location location = new Location(this.x - Math.sin(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * 0.5, this.y + (double)this.getEyeHeight(), this.z + Math.cos(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * 0.5, d3, d4, this.level);
            if (this.getLevel().getBlockIdAt(this.chunk, location.getFloorX(), location.getFloorY(), location.getFloorZ()) != 0) {
                return;
            }
            if (Utils.rand(0, 200) > 180 || Utils.rand(0, 200) < 20) {
                d2 = 0.8;
                Entity entity2 = Entity.createEntity("BlueWitherSkull", (Position)location, this);
                entityWitherSkull = (EntityBlueWitherSkull)entity2;
                ((EntityBlueWitherSkull)entityWitherSkull).setExplode(true);
            } else {
                Entity entity3 = Entity.createEntity("WitherSkull", (Position)location, this);
                entityWitherSkull = (EntityWitherSkull)entity3;
            }
            entityWitherSkull.setMotion(new Vector3(-Math.sin(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * d2 * d2, -Math.sin(FastMathLite.toRadians(d4)) * d2 * d2, Math.cos(FastMathLite.toRadians(d3)) * Math.cos(FastMathLite.toRadians(d4)) * d2 * d2));
            ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(entityWitherSkull);
            this.server.getPluginManager().callEvent(projectileLaunchEvent);
            if (projectileLaunchEvent.isCancelled()) {
                entityWitherSkull.close();
            } else {
                entityWitherSkull.spawnToAll();
                this.level.addSound((Vector3)this, Sound.MOB_WITHER_SHOOT);
            }
        }
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(399, 0, 1)};
    }

    @Override
    protected DataPacket createAddEntityPacket() {
        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.type = 52;
        addEntityPacket.entityUniqueId = this.getId();
        addEntityPacket.entityRuntimeId = this.getId();
        addEntityPacket.yaw = (float)this.yaw;
        addEntityPacket.headYaw = (float)this.yaw;
        addEntityPacket.pitch = (float)this.pitch;
        addEntityPacket.x = (float)this.x;
        addEntityPacket.y = (float)this.y;
        addEntityPacket.z = (float)this.z;
        addEntityPacket.speedX = (float)this.motionX;
        addEntityPacket.speedY = (float)this.motionY;
        addEntityPacket.speedZ = (float)this.motionZ;
        addEntityPacket.metadata = this.dataProperties.clone();
        addEntityPacket.attributes = new Attribute[]{Attribute.getAttribute(4).setMaxValue(this.b()).setValue(this.b())};
        return addEntityPacket;
    }

    @Override
    public boolean entityBaseTick(int n) {
        if (this.getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }
        if (!this.closed && this.age == 200) {
            this.c();
            this.setDataProperty(new IntEntityData(48, 0));
        }
        return super.entityBaseTick(n);
    }

    @Override
    public int nearbyDistanceMultiplier() {
        return 30;
    }

    @Override
    public void kill() {
        if (!this.isAlive()) {
            return;
        }
        if (!this.w && this.lastDamageCause != null && EntityDamageEvent.DamageCause.SUICIDE != this.lastDamageCause.getCause()) {
            Entity entity;
            if (this.lastDamageCause instanceof EntityDamageByEntityEvent && (entity = ((EntityDamageByEntityEvent)this.lastDamageCause).getDamager()) instanceof Player) {
                ((Player)entity).awardAchievement("killWither");
            }
            this.w = true;
            this.c();
        }
        super.kill();
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (this.age <= 200 && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.SUICIDE) {
            return false;
        }
        return super.attack(entityDamageEvent);
    }

    private int b() {
        switch (this.getServer().getDifficulty()) {
            case 2: {
                return 450;
            }
            case 3: {
                return 600;
            }
        }
        return 300;
    }

    private void c() {
        EntityExplosionPrimeEvent entityExplosionPrimeEvent = new EntityExplosionPrimeEvent(this, 7.0);
        this.server.getPluginManager().callEvent(entityExplosionPrimeEvent);
        if (!entityExplosionPrimeEvent.isCancelled()) {
            Explosion explosion = new Explosion(this, (float)entityExplosionPrimeEvent.getForce(), this);
            if (entityExplosionPrimeEvent.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
                explosion.explodeA();
            }
            explosion.explodeB();
        }
    }

    @Override
    public boolean canTarget(Entity entity) {
        return entity.canBeFollowed();
    }
}


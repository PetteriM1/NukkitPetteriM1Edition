/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.item.EntityBoat;
import cn.nukkit.entity.item.EntityEndCrystal;
import cn.nukkit.entity.item.EntityMinecartAbstract;
import cn.nukkit.entity.mob.EntityBlaze;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntitySnowball;
import cn.nukkit.event.Event;
import cn.nukkit.event.entity.EntityCombustByEntityEvent;
import cn.nukkit.event.entity.EntityCombustEvent;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.item.ItemArrow;
import cn.nukkit.level.MovingObjectPosition;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import java.util.concurrent.ThreadLocalRandom;

public abstract class EntityProjectile
extends Entity {
    public static final int DATA_SHOOTER_ID = 17;
    public static final int PICKUP_NONE_REMOVE = -1;
    public static final int PICKUP_NONE = 0;
    public static final int PICKUP_ANY = 1;
    public static final int PICKUP_CREATIVE = 2;
    public Entity shootingEntity;
    public float knockBack = 0.3f;
    public boolean hadCollision = false;
    public int piercing;

    protected double getDamage() {
        return this.namedTag.contains("damage") ? this.namedTag.getDouble("damage") : this.getBaseDamage();
    }

    protected double getBaseDamage() {
        return 0.0;
    }

    public EntityProjectile(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityProjectile(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag);
        this.shootingEntity = entity;
    }

    public int getResultDamage() {
        return NukkitMath.ceilDouble(this.getDamage());
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        return entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.VOID && super.attack(entityDamageEvent);
    }

    public void onCollideWithEntity(Entity entity) {
        this.server.getPluginManager().callEvent(new ProjectileHitEvent(this, MovingObjectPosition.fromEntity(entity)));
        float f2 = this instanceof EntitySnowball && entity instanceof EntityBlaze ? 3.0f : (float)this.getResultDamage();
        EntityDamageByEntityEvent entityDamageByEntityEvent = this.shootingEntity == null ? new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.PROJECTILE, f2, this.knockBack) : new EntityDamageByChildEntityEvent(this.shootingEntity, (Entity)this, entity, EntityDamageEvent.DamageCause.PROJECTILE, f2, this.knockBack);
        if (entity.attack(entityDamageByEntityEvent)) {
            Object object;
            this.hadCollision = true;
            this.onHit();
            if (this.fireTicks > 0 && entity.isAlive()) {
                object = new EntityCombustByEntityEvent(this, entity, 5);
                this.server.getPluginManager().callEvent((Event)object);
                if (!((Event)object).isCancelled()) {
                    entity.setOnFire(((EntityCombustEvent)object).getDuration());
                }
            }
            if (this instanceof EntityArrow && entity instanceof EntityLiving && (object = ItemArrow.getEffect(((EntityArrow)this).getData())) != null) {
                EntityProjectile.addEffectFromTippedArrow(entity, (Effect)object, entityDamageByEntityEvent.getFinalDamage());
            }
        }
        this.close();
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(1);
        super.initEntity();
        this.setHealth(1.0f);
        if (this.namedTag.contains("Age")) {
            this.age = this.namedTag.getShort("Age");
        }
        if (this.namedTag.contains("knockback")) {
            this.knockBack = this.namedTag.getFloat("knockback");
        }
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return (entity instanceof EntityLiving || entity instanceof EntityEndCrystal || entity instanceof EntityMinecartAbstract || entity instanceof EntityBoat) && !this.onGround && !entity.noClip && !this.noClip;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putShort("Age", this.age);
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        int n2 = n - this.lastUpdate;
        if (n2 <= 0 && !this.justCreated) {
            return true;
        }
        this.lastUpdate = n;
        boolean bl = this.entityBaseTick(n2);
        if (this.isAlive()) {
            MovingObjectPosition movingObjectPosition = null;
            if (!this.isCollided) {
                this.motionY = this.isInsideOfWater() ? (this.motionY -= (double)(this.getGravity() - this.getGravity() / 2.0f)) : (this.motionY -= (double)this.getGravity());
                this.motionX *= (double)(1.0f - this.getDrag());
                this.motionZ *= (double)(1.0f - this.getDrag());
            }
            Vector3 vector3 = new Vector3(this.x + this.motionX, this.y + this.motionY, this.z + this.motionZ);
            Entity[] entityArray = this.noClip ? new Entity[]{} : this.getLevel().getCollidingEntities(this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0), this);
            double d2 = 2.147483647E9;
            Entity entity = null;
            for (Entity entity2 : entityArray) {
                double d3;
                AxisAlignedBB axisAlignedBB;
                MovingObjectPosition movingObjectPosition2;
                if (entity2 == this.shootingEntity && this.age < 5 || entity2 instanceof Player && ((Player)entity2).getGamemode() == 3 || (movingObjectPosition2 = (axisAlignedBB = entity2.boundingBox.grow(0.3, 0.3, 0.3)).calculateIntercept(this, vector3)) == null || !((d3 = this.distanceSquared(movingObjectPosition2.hitVector)) < d2)) continue;
                d2 = d3;
                entity = entity2;
            }
            if (entity != null) {
                movingObjectPosition = MovingObjectPosition.fromEntity(entity);
            }
            if (movingObjectPosition != null && movingObjectPosition.entityHit != null) {
                this.onCollideWithEntity(movingObjectPosition.entityHit);
                return true;
            }
            this.move(this.motionX, this.motionY, this.motionZ);
            if (this.isCollided && !this.hadCollision) {
                this.hadCollision = true;
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
                this.server.getPluginManager().callEvent(new ProjectileHitEvent(this, MovingObjectPosition.fromBlock(this.getFloorX(), this.getFloorY(), this.getFloorZ(), -1, this)));
                this.onHit();
                this.onHitGround();
                return false;
            }
            if (!this.isCollided && this.hadCollision) {
                this.hadCollision = false;
            }
            if (!this.hadCollision || Math.abs(this.motionX) > 1.0E-5 || Math.abs(this.motionY) > 1.0E-5 || Math.abs(this.motionZ) > 1.0E-5) {
                this.updateRotation();
                bl = true;
            }
            this.updateMovement();
        }
        return bl;
    }

    public void updateRotation() {
        double d2 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.yaw = FastMathLite.atan2(this.motionX, this.motionZ) * 180.0 / Math.PI;
        this.pitch = FastMathLite.atan2(this.motionY, d2) * 180.0 / Math.PI;
    }

    public void inaccurate(float f2) {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        this.motionX += threadLocalRandom.nextGaussian() * (double)0.0075f * (double)f2;
        this.motionY += threadLocalRandom.nextGaussian() * (double)0.0075f * (double)f2;
        this.motionZ += threadLocalRandom.nextGaussian() * (double)0.0075f * (double)f2;
    }

    protected void onHit() {
    }

    protected void onHitGround() {
    }
}


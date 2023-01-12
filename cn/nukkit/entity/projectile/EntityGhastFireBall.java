/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.WeakExplosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityGhastFireBall
extends EntityProjectile
implements EntityExplosive {
    public static final int NETWORK_ID = 85;
    private boolean k;
    private boolean l;

    @Override
    public int getNetworkId() {
        return 85;
    }

    @Override
    public float getWidth() {
        return 0.31f;
    }

    @Override
    public float getHeight() {
        return 0.31f;
    }

    @Override
    public float getGravity() {
        return 0.005f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    public double getBaseDamage() {
        return 6.0;
    }

    public EntityGhastFireBall(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityGhastFireBall(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    public void setExplode(boolean bl) {
        this.k = bl;
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (this.timing != null) {
            this.timing.startTiming();
        }
        if (this.age > 1200 || this.isCollided || this.hadCollision) {
            if (this.isCollided && this.k) {
                this.explode();
            } else {
                this.close();
            }
            if (this.timing != null) {
                this.timing.stopTiming();
            }
            return false;
        }
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        super.onUpdate(n);
        return !this.closed;
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        this.explode();
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (!this.l && entityDamageEvent instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)entityDamageEvent).getDamager() instanceof Player) {
            this.l = true;
            this.setMotion(((EntityDamageByEntityEvent)entityDamageEvent).getDamager().getLocation().getDirectionVector());
        }
        return true;
    }

    @Override
    public void explode() {
        if (this.closed) {
            return;
        }
        this.close();
        EntityExplosionPrimeEvent entityExplosionPrimeEvent = new EntityExplosionPrimeEvent(this, 1.2);
        this.server.getPluginManager().callEvent(entityExplosionPrimeEvent);
        if (!entityExplosionPrimeEvent.isCancelled()) {
            WeakExplosion weakExplosion = new WeakExplosion(this, (float)entityExplosionPrimeEvent.getForce(), this.shootingEntity);
            if (entityExplosionPrimeEvent.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
                weakExplosion.explodeA();
            }
            weakExplosion.explodeB();
        }
    }
}


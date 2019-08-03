package cn.nukkit.entity.projectile;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.SmallExplosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.CriticalParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityGhastFireBall extends EntityProjectile implements EntityExplosive {

    public static final int NETWORK_ID = 85;

    protected boolean critical;

    protected boolean canExplode = false;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.45f;
    }

    @Override
    public float getHeight() {
        return 0.45f;
    }

    @Override
    public float getGravity() {
        return 0.01f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    public double getDamage() {
        return 5;
    }

    public EntityGhastFireBall(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityGhastFireBall(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        this(chunk, nbt, shootingEntity, false);
    }

    public EntityGhastFireBall(FullChunk chunk, CompoundTag nbt, Entity shootingEntity, boolean critical) {
        super(chunk, nbt, shootingEntity);

        this.critical = critical;
    }

    public void setExplode(boolean bool) {
        this.canExplode = bool;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (!this.hadCollision && this.critical) {
            this.level.addParticle(new CriticalParticle(
                    this.add(this.getWidth() / 2 + Utils.rand(-100.0, 100.0) / 500, this.getHeight() / 2 + Utils.rand(-100.0, 100.0) / 500, this.getWidth() / 2 + Utils.rand(-100.0, 100.0) / 500)));
        } else if (this.onGround) {
            this.critical = false;
        }

        if (this.age > 1200 || this.isCollided) {
            if (this.isCollided && this.canExplode) {
                this.explode();
            }
            this.close();
        }

        return super.onUpdate(currentTick);
    }
    
    @Override
    public void onCollideWithEntity(Entity entity) {
        this.isCollided = true;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        if (source instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) source).getDamager() instanceof Player) {
                this.setMotion(((EntityDamageByEntityEvent) source).getDamager().getLocation().getDirectionVector());
            }
        }

        return true;
    }

    @Override
    public void explode() {
        ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this, 1);
        this.server.getPluginManager().callEvent(ev);
        if (!ev.isCancelled()) {
            SmallExplosion explosion = new SmallExplosion(this, (float) ev.getForce(), this.shootingEntity);
            if (ev.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
                explosion.explodeA();
            }
            explosion.explodeB();
        }
    }
}

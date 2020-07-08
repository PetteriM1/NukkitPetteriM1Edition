package cn.nukkit.entity.projectile;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.WeakExplosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityGhastFireBall extends EntityProjectile implements EntityExplosive {

    public static final int NETWORK_ID = 85;

    private boolean canExplode;

    private boolean directionChanged;

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
    }

    public void setExplode(boolean bool) {
        this.canExplode = bool;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
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
        if (!directionChanged && source instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) source).getDamager() instanceof Player) {
                directionChanged = true;
                this.setMotion(((EntityDamageByEntityEvent) source).getDamager().getLocation().getDirectionVector());
            }
        }

        return true;
    }

    @Override
    public void explode() {
        ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this, 1.2);
        this.server.getPluginManager().callEvent(ev);
        if (!ev.isCancelled()) {
            WeakExplosion explosion = new WeakExplosion(this, (float) ev.getForce(), this.shootingEntity);
            if (ev.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
                explosion.explodeA();
            }
            explosion.explodeB();
        }
    }
}

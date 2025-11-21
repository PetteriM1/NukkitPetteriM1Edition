package cn.nukkit.entity.projectile;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityGhastFireBall extends EntityProjectile implements EntityExplosive {

    public static final int NETWORK_ID = 85;

    private boolean canExplode;

    public Player directionChanged;

    public EntityGhastFireBall(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityGhastFireBall(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        if (this.directionChanged == null && source instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) source).getDamager() instanceof Player) {
                this.directionChanged = (Player) ((EntityDamageByEntityEvent) source).getDamager();
                this.setMotion(((EntityDamageByEntityEvent) source).getDamager().getDirectionVector());
            }
        }

        return true;
    }

    @Override
    public void explode() {
        if (this.closed) {
            return;
        }
        this.close();
        EntityExplosionPrimeEvent ev = new EntityExplosionPrimeEvent(this, 1);
        this.server.getPluginManager().callEvent(ev);
        if (!ev.isCancelled()) {
            Explosion explosion = new Explosion(this, (float) ev.getForce(), this.shootingEntity);
            explosion.setFireSpawnChance(0.3333);
            explosion.setMaxResistance(20);
            if (ev.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
                explosion.explodeA();
            }
            explosion.explodeB();
        }
    }

    @Override
    public double getBaseDamage() {
        return 6;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    public float getGravity() {
        return 0.001f;
    }

    @Override
    public float getHeight() {
        return 0.31f;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.31f;
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        this.explode();
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (this.age > 1200 || this.isCollided || this.hadCollision) {
            if (this.isCollided && this.canExplode) {
                this.explode();
            } else {
                this.close();
            }
            return false;
        }

        super.onUpdate(currentTick);
        return !this.closed;
    }

    public void setExplode(boolean bool) {
        this.canExplode = bool;
    }
}

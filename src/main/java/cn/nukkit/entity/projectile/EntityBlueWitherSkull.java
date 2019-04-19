package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.level.SmallExplosion;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.EntityUtils;

public class EntityBlueWitherSkull extends EntityWitherSkull {

    public static final int NETWORK_ID = 91;

    public EntityBlueWitherSkull(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityBlueWitherSkull(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        this(chunk, nbt, shootingEntity, false);
    }

    public EntityBlueWitherSkull(FullChunk chunk, CompoundTag nbt, Entity shootingEntity, boolean critical) {
        super(chunk, nbt, shootingEntity);

        this.critical = critical;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        this.timing.startTiming();

        if (this.age > 1200 || this.hadCollision) {
            if (this.canExplode) {
                ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this, 1);
                this.server.getPluginManager().callEvent(ev);

                if (!ev.isCancelled()) {
                    SmallExplosion explosion = new SmallExplosion(this, (float) ev.getForce(), this.shootingEntity);
                    if (ev.isBlockBreaking()) {
                        explosion.explodeA();
                    }
                    explosion.explodeB();
                }
            }

            this.kill();
        } else {
            this.level.addParticle(new SmokeParticle(this.add(this.getWidth() / 2 + EntityUtils.rand(-100.0, 100.0) / 500, this.getHeight() / 2 + EntityUtils.rand(-100.0, 100.0) / 500, this.getWidth() / 2 + EntityUtils.rand(-100.0, 100.0) / 500)));
        }

        this.timing.stopTiming();

        return super.onUpdate(currentTick);
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        super.onCollideWithEntity(entity);
        entity.addEffect(Effect.getEffect(Effect.WITHER).setAmplifier(1).setDuration(200));
    }
}

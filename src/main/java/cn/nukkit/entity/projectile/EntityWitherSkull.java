package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.EntityUtils;

public class EntityWitherSkull extends EntityProjectile {

    public static final int NETWORK_ID = 89;

    protected boolean critical = false;

    protected boolean canExplode = false;

    public EntityWitherSkull(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getLength() {
        return 0.25f;
    }

    @Override
    public float getHeight() {
        return 0.25f;
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
    protected double getDamage() {
        return 5;
    }

    public EntityWitherSkull(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        this(chunk, nbt, shootingEntity, false);
    }

    public EntityWitherSkull(FullChunk chunk, CompoundTag nbt, Entity shootingEntity, boolean critical) {
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

        this.timing.startTiming();

        if (this.age > 1200 || this.isCollided) {
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

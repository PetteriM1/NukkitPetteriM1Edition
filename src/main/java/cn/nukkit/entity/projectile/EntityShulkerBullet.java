package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.potion.Effect;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityShulkerBullet extends EntityProjectile {

    public static final int NETWORK_ID = 76;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getGravity() {
        return 0.001f;
    }

    @Override
    public float getDrag() {
        return 0.001f;
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getLength() {
        return 0.40f;
    }

    @Override
    public float getHeight() {
        return 0.40f;
    }

    public EntityShulkerBullet(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityShulkerBullet(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        this.timing.startTiming();

        boolean hasUpdate = super.onUpdate(currentTick);

        if (this.age > 1200 || this.isCollided) {
            this.kill();
            hasUpdate = true;
        }

        this.timing.stopTiming();

        return hasUpdate;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        this.level.addSound(this, "mob.shulker.bullet.hit");
        this.close();
        return true;
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        super.onCollideWithEntity(entity);
        
        Effect levitation = Effect.getEffect(Effect.LEVITATION);
        levitation.setAmplifier(1);
        levitation.setDuration(200);
        entity.addEffect(levitation);
    }
}

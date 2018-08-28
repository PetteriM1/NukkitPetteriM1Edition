package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityShulkerBullet extends EntityProjectile {

    public static final int NETWORK_ID = 76;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    protected float getGravity() {
        return 0.01f;
    }

    @Override
    protected float getDrag() {
        return 0.01f;
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
}

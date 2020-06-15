package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityBlazeFireBall extends EntityProjectile {

    public static final int NETWORK_ID = 94;

    public EntityBlazeFireBall(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityBlazeFireBall(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.3125f;
    }

    @Override
    public float getHeight() {
        return 0.3125f;
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

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (this.age > 1200 || this.isCollided) {
            this.close();
        }

        return super.onUpdate(currentTick);
    }
    
    @Override
    public void onCollideWithEntity(Entity entity) {
        super.onCollideWithEntity(entity);
        this.isCollided = true;
        entity.setOnFire(5);
    }
}

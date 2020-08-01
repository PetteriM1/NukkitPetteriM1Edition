package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityLlamaSpit extends EntityProjectile {

    public static final int NETWORK_ID = 102;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.3f;
    }

    @Override
    public float getHeight() {
        return 0.3f;
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
        return 1;
    }

    public EntityLlamaSpit(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityLlamaSpit(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        this(chunk, nbt, shootingEntity, false);
    }

    public EntityLlamaSpit(FullChunk chunk, CompoundTag nbt, Entity shootingEntity, boolean critical) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (this.age > 100 || this.isCollided) {
            this.close();
        }

        return super.onUpdate(currentTick);
    }
}

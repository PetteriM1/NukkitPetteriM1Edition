/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityBlazeFireBall
extends EntityProjectile {
    public static final int NETWORK_ID = 94;

    public EntityBlazeFireBall(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityBlazeFireBall(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    public int getNetworkId() {
        return 94;
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
        return 5.0;
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (this.age > 1200 || this.isCollided || this.hadCollision) {
            this.close();
            return false;
        }
        this.fireTicks = 2;
        super.onUpdate(n);
        return !this.closed;
    }
}


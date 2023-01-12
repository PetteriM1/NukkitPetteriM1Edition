/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityLlamaSpit
extends EntityProjectile {
    public static final int NETWORK_ID = 102;

    @Override
    public int getNetworkId() {
        return 102;
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
        return 0.005f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    protected double getBaseDamage() {
        return 1.0;
    }

    public EntityLlamaSpit(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityLlamaSpit(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (this.age > 100 || this.isCollided || this.hadCollision) {
            this.close();
            return false;
        }
        super.onUpdate(n);
        return !this.closed;
    }
}


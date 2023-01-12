/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityEvocationFangs
extends EntityProjectile {
    public static final int NETWORK_ID = 103;

    public EntityEvocationFangs(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityEvocationFangs(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    public int getNetworkId() {
        return 103;
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
        return 0.03f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (this.age > 1200 || this.isCollided) {
            this.close();
        }
        super.onUpdate(n);
        return !this.closed;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityBlueWitherSkull;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Utils;

public class EntityWitherSkull
extends EntityProjectile {
    public static final int NETWORK_ID = 89;

    public EntityWitherSkull(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityWitherSkull(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    public int getNetworkId() {
        return 89;
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
        return 0.005f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    protected double getBaseDamage() {
        switch (this.server.getDifficulty()) {
            case 2: {
                return 8.0;
            }
            case 3: {
                return 12.0;
            }
        }
        return 5.0;
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (this.timing != null) {
            this.timing.startTiming();
        }
        boolean bl = super.onUpdate(n);
        if (this.age > 1200 || this.isCollided || this.hadCollision) {
            if (this instanceof EntityBlueWitherSkull && ((EntityBlueWitherSkull)this).k) {
                ((EntityBlueWitherSkull)this).explode();
            }
            this.close();
        } else if (this.age % 4 == 0) {
            this.level.addParticle(new SmokeParticle(this.add((double)(this.getWidth() / 2.0f) + Utils.rand(-100.0, 100.0) / 500.0, (double)(this.getHeight() / 2.0f) + Utils.rand(-100.0, 100.0) / 500.0, (double)(this.getWidth() / 2.0f) + Utils.rand(-100.0, 100.0) / 500.0)));
        }
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return bl;
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        super.onCollideWithEntity(entity);
        entity.addEffect(Effect.getEffect(20).setDuration(200));
    }
}


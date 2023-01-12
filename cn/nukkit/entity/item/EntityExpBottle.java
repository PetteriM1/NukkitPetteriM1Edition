/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.SpellParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityExpBottle
extends EntityProjectile {
    public static final int NETWORK_ID = 68;

    @Override
    public int getNetworkId() {
        return 68;
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
    protected float getGravity() {
        return 0.1f;
    }

    @Override
    protected float getDrag() {
        return 0.01f;
    }

    public EntityExpBottle(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityExpBottle(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (this.age > 1200) {
            this.close();
            return false;
        }
        if (this.timing != null) {
            this.timing.startTiming();
        }
        boolean bl = super.onUpdate(n);
        if (this.isCollided) {
            this.dropXp();
            this.close();
            if (this.timing != null) {
                this.timing.stopTiming();
            }
            return false;
        }
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return bl;
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        this.dropXp();
        this.close();
    }

    public void dropXp() {
        this.getLevel().addLevelSoundEvent(this, 127);
        this.getLevel().addParticle(new SpellParticle((Vector3)this, 3694022));
        this.getLevel().dropExpOrb(this, Utils.rand(3, 12));
    }
}


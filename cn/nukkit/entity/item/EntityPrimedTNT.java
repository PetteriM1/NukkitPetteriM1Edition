/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.block.BlockSlab;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityPrimedTNT
extends Entity
implements EntityExplosive {
    public static final int NETWORK_ID = 65;
    protected int fuse;
    protected Entity source;

    @Override
    public float getWidth() {
        return 0.98f;
    }

    @Override
    public float getLength() {
        return 0.98f;
    }

    @Override
    public float getHeight() {
        return 0.98f;
    }

    @Override
    protected float getGravity() {
        return 0.04f;
    }

    @Override
    protected float getDrag() {
        return 0.02f;
    }

    @Override
    protected float getBaseOffset() {
        return 0.49f;
    }

    public EntityPrimedTNT(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityPrimedTNT(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag);
        this.source = entity;
    }

    @Override
    public int getNetworkId() {
        return 65;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        return entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.VOID && super.attack(entityDamageEvent);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.fuse = this.namedTag.contains("Fuse") ? this.namedTag.getByte("Fuse") : 80;
        this.setDataFlag(0, 10, true);
        this.setDataProperty(new IntEntityData(55, this.fuse));
        this.getLevel().addLevelSoundEvent(this, 27);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putByte("Fuse", this.fuse);
    }

    @Override
    public boolean onUpdate(int n) {
        int n2;
        if (this.closed) {
            return false;
        }
        if (this.timing != null) {
            this.timing.startTiming();
        }
        if ((n2 = n - this.lastUpdate) <= 0 && !this.justCreated) {
            return true;
        }
        if (this.fuse % 5 == 0) {
            this.setDataProperty(new IntEntityData(55, this.fuse));
        }
        this.lastUpdate = n;
        boolean bl = this.entityBaseTick(n2);
        if (this.isAlive()) {
            if (!this.isOnGround()) {
                this.motionY -= (double)this.getGravity();
            }
            this.move(this.motionX, this.motionY, this.motionZ);
            float f2 = 1.0f - this.getDrag();
            this.motionX *= (double)f2;
            this.motionY *= (double)f2;
            this.motionZ *= (double)f2;
            this.updateMovement();
            if (this.onGround) {
                this.motionY *= -0.5;
                this.motionX *= 0.7;
                this.motionZ *= 0.7;
            }
            this.fuse -= n2;
            if (this.fuse <= 0) {
                if (this.level.getGameRules().getBoolean(GameRule.TNT_EXPLODES)) {
                    this.explode();
                }
                this.close();
                return false;
            }
        }
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return bl || this.fuse >= 0 || Math.abs(this.motionX) > 1.0E-5 || Math.abs(this.motionY) > 1.0E-5 || Math.abs(this.motionZ) > 1.0E-5;
    }

    @Override
    public void explode() {
        EntityExplosionPrimeEvent entityExplosionPrimeEvent = new EntityExplosionPrimeEvent(this, 4.0);
        this.server.getPluginManager().callEvent(entityExplosionPrimeEvent);
        if (entityExplosionPrimeEvent.isCancelled()) {
            return;
        }
        Explosion explosion = new Explosion(this.level.getBlock(this) instanceof BlockSlab ? this.add(0.0, 0.1, 0.0) : this, entityExplosionPrimeEvent.getForce(), this);
        if (entityExplosionPrimeEvent.isBlockBreaking()) {
            explosion.explodeA();
        }
        explosion.explodeB();
    }

    public Entity getSource() {
        return this.source;
    }
}


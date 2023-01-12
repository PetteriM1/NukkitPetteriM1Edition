/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityArrow
extends EntityProjectile {
    public static final int NETWORK_ID = 80;
    protected int pickupMode;
    protected boolean critical;
    private int l;
    private boolean k;

    @Override
    public int getNetworkId() {
        return 80;
    }

    @Override
    public float getWidth() {
        return 0.05f;
    }

    @Override
    public float getLength() {
        return 0.5f;
    }

    @Override
    public float getHeight() {
        return 0.05f;
    }

    @Override
    public float getGravity() {
        return 0.05f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    public EntityArrow(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityArrow(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        this(fullChunk, compoundTag, entity, false);
    }

    public EntityArrow(FullChunk fullChunk, CompoundTag compoundTag, Entity entity, boolean bl) {
        this(fullChunk, compoundTag, entity, bl, false);
    }

    public EntityArrow(FullChunk fullChunk, CompoundTag compoundTag, Entity entity, boolean bl, boolean bl2) {
        super(fullChunk, compoundTag, entity);
        this.setCritical(bl);
        this.k = bl2;
    }

    public void setData(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("data < 0");
        }
        this.l = n;
        this.setDataProperty(new ByteEntityData(18, this.l), false);
    }

    public int getData() {
        return this.l;
    }

    public boolean isFromCrossbow() {
        return this.k;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.pickupMode = this.namedTag.contains("pickup") ? this.namedTag.getByte("pickup") : 1;
        int n = this.namedTag.getByte("arrowData");
        if (n != 0) {
            this.setData(n);
        }
        this.k = this.namedTag.getBoolean("isFromCrossbow");
    }

    public void setCritical() {
        this.setCritical(true);
    }

    public void setCritical(boolean bl) {
        block0: {
            if (this.critical == bl) break block0;
            this.critical = bl;
            this.setDataFlag(0, 13, bl);
        }
    }

    public boolean isCritical() {
        return this.critical;
    }

    @Override
    public int getResultDamage() {
        int n;
        block1: {
            n = NukkitMath.ceilDouble(Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ) * this.getDamage());
            if (this.isCritical()) {
                n += Utils.random.nextInt((n >> 1) + 2);
            }
            if (!this.k) break block1;
            n += 2;
        }
        return n;
    }

    @Override
    protected double getBaseDamage() {
        return 2.0;
    }

    @Override
    public boolean onUpdate(int n) {
        boolean bl;
        block5: {
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
            if (this.onGround || this.hadCollision) {
                this.setCritical(false);
            }
            if (this.fireTicks > 0 && this.level.isRaining() && this.canSeeSky()) {
                this.extinguish();
            }
            bl = super.onUpdate(n);
            if (this.timing == null) break block5;
            this.timing.stopTiming();
        }
        return bl;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putByte("pickup", this.pickupMode);
        this.namedTag.putByte("arrowData", this.l);
        this.namedTag.putBoolean("isFromCrossbow", this.k);
    }

    public int getPickupMode() {
        return this.pickupMode;
    }

    public void setPickupMode(int n) {
        this.pickupMode = n;
    }

    @Override
    public void onHit() {
        this.getLevel().addLevelSoundEvent(this, 63);
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}


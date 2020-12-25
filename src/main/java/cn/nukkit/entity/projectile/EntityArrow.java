package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Utils;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class EntityArrow extends EntityProjectile {

    public static final int NETWORK_ID = 80;

    public static final int DATA_SOURCE_ID = 17;

    public static final int PICKUP_NONE = 0;
    public static final int PICKUP_ANY = 1;
    public static final int PICKUP_CREATIVE = 2;

    protected int pickupMode;
    public boolean isFromStray;
    protected boolean critical;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
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

    public EntityArrow(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityArrow(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        this(chunk, nbt, shootingEntity, false);
    }

    public EntityArrow(FullChunk chunk, CompoundTag nbt, Entity shootingEntity, boolean critical) {
        super(chunk, nbt, shootingEntity);
        this.setCritical(critical);
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.pickupMode = namedTag.contains("pickup") ? namedTag.getByte("pickup") : PICKUP_ANY;
    }

    public void setCritical() {
        this.setCritical(true);
    }

    public void setCritical(boolean value) {
        //this.setDataFlag(DATA_FLAGS, DATA_FLAG_CRITICAL, value);
        this.critical = value;
    }

    public boolean isCritical() {
        //return this.getDataFlag(DATA_FLAGS, DATA_FLAG_CRITICAL);
        return this.critical;
    }

    @Override
    public int getResultDamage() {
        int base = super.getResultDamage();

        if (this.isCritical()) {
            base += Utils.random.nextInt((base >> 1) + 2);
        }

        return base;
    }

    @Override
    protected double getBaseDamage() {
        return 2;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (this.timing != null) this.timing.startTiming();

        if (this.age > 1200) {
            this.close();
            return false;
        }

        if (this.onGround || this.hadCollision) {
            this.setCritical(false);
        }

        if (this.fireTicks > 0 && this.level.isRaining() && this.level.canBlockSeeSky(this)) {
            this.extinguish();
        }

        if (this.timing != null) this.timing.stopTiming();

        return super.onUpdate(currentTick);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putByte("pickup", this.pickupMode);
    }

    public int getPickupMode() {
        return this.pickupMode;
    }

    public void setPickupMode(int pickupMode) {
        this.pickupMode = pickupMode;
    }

    @Override
    public void onHit() {
        this.getLevel().addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_BOW_HIT);
    }

    public void onCollideWithEntity(Entity entity) {
        super.onCollideWithEntity(entity);

        if (this.isFromStray) {
            entity.addEffect(Effect.getEffect(Effect.SLOWNESS).setDuration(600));
        }
    }
}

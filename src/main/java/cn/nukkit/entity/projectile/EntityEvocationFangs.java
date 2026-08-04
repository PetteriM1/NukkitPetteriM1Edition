package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityEvocationFangs extends Entity {

    public static final int NETWORK_ID = 103;

    private final Entity shootingEntity;

    public EntityEvocationFangs(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityEvocationFangs(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt);
        this.shootingEntity = shootingEntity;
    }

    @Override
    public float getDrag() {
        return 0f;
    }

    @Override
    public float getGravity() {
        return 0f;
    }

    @Override
    public float getHeight() {
        return 0.8f;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.5f;
    }

    @Override
    public boolean canSaveToStorage() {
        return false;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        if (!this.closed) {
            this.level.addSound(this, Sound.MOB_EVOCATION_FANGS_ATTACK);
        }
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (this.age > 10) {
            this.close();

            for (Entity entity : this.level.getCollidingEntities(this.getBoundingBox(), this)) {
                if (entity == shootingEntity) {
                    continue;
                }
                entity.attack(new EntityDamageByEntityEvent(this, entity, EntityDamageEvent.DamageCause.MAGIC, 6));
            }
            return false;
        }

        super.onUpdate(currentTick);
        return !this.closed;
    }
}

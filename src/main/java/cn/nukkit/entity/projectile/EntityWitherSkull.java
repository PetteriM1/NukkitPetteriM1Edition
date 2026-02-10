package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityPotionEffectEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Utils;

public class EntityWitherSkull extends EntityProjectile {

    public static final int NETWORK_ID = 89;

    public EntityWitherSkull(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityWitherSkull(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    protected double getBaseDamage() {
        switch (server.getDifficulty()) {
            case 2: // normal
                return 8;
            case 3: // hard
                return 12;
            default:
                return 5;
        }
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    public float getGravity() {
        return 0.001f;
    }

    @Override
    public float getHeight() {
        return 0.25f;
    }

    @Override
    public float getLength() {
        return 0.25f;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        super.onCollideWithEntity(entity);
        entity.addEffect(Effect.getEffect(Effect.WITHER).setDuration(200), EntityPotionEffectEvent.Cause.ATTACK);
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }
        boolean update = super.onUpdate(currentTick);

        if (this.age > 1200 || this.isCollided || this.hadCollision) {
            if (this instanceof EntityBlueWitherSkull) {
                if (((EntityBlueWitherSkull) this).canExplode) {
                    ((EntityBlueWitherSkull) this).explode();
                }
            }
            this.close();
        } else if (this.age % 4 == 0) {
            this.level.addParticle(new SmokeParticle(this.add(this.getWidth() / 2 + Utils.rand(-100.0, 100.0) / 500, this.getHeight() / 2 + Utils.rand(-100.0, 100.0) / 500, this.getWidth() / 2 + Utils.rand(-100.0, 100.0) / 500)));
        }
        return update;
    }
}

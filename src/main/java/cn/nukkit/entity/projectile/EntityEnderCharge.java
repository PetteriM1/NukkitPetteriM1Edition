package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityAreaEffectCloud;
import cn.nukkit.entity.mob.EntityEnderDragon;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.Potion;

public class EntityEnderCharge extends EntityProjectile {

    public static final int NETWORK_ID = 79;

    public EntityEnderCharge(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityEnderCharge(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    protected double getBaseDamage() {
        return 0;
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
    public boolean canCollideWith(Entity entity) {
        return !(entity instanceof EntityEnderDragon);
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        this.splash();
        this.close();
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }
        boolean update = super.onUpdate(currentTick);

        if (this.isCollided) {
            this.splash();
        }

        if (this.age > 1200 || this.isCollided || this.hadCollision) {
            this.close();
            return false;
        }
        return update;
    }

    protected void splash() {
        EntityAreaEffectCloud entity = (EntityAreaEffectCloud) Entity.createEntity("AreaEffectCloud", getChunk(),
                Entity.getDefaultNBT(this)
                        .putShort("PotionId", Potion.HARMING_II)
                        .putFloat("InitialRadius", 5f)
                        .putInt("ParticleColor", 8339378)
        );

        Effect effect = Potion.getEffect(Potion.HARMING_II, true);
        if (effect != null && entity != null) {
            entity.setFromDragon(true);
            entity.cloudEffects.add(effect.setDuration(1).setVisible(false).setAmbient(false));
            entity.spawnToAll();
        }
    }
}

package cn.nukkit.entity.item;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.EnchantParticle;
import cn.nukkit.level.particle.SpellParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

/**
 * @author xtypr
 */
public class EntityExpBottle extends EntityProjectile {

    public static final int NETWORK_ID = 68;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
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

    public EntityExpBottle(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityExpBottle(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
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

        if (this.isCollided) {
            this.dropXp();
            this.close();
            return false;
        }

        if (this.timing != null) this.timing.stopTiming();

        return super.onUpdate(currentTick);
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        this.dropXp();
        this.close();
    }

    public void dropXp() {
        this.getLevel().addParticle(new EnchantParticle(this));
        this.getLevel().addParticle(new SpellParticle(this, 0x00385dc6));
        this.getLevel().dropExpOrb(this, Utils.rand(3, 12));
    }
}

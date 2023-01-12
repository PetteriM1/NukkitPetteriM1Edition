/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;

public class EntityShulkerBullet
extends EntityProjectile {
    public static final int NETWORK_ID = 76;

    @Override
    public int getNetworkId() {
        return 76;
    }

    @Override
    public float getGravity() {
        return 0.001f;
    }

    @Override
    public float getDrag() {
        return 0.001f;
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getLength() {
        return 0.4f;
    }

    @Override
    public float getHeight() {
        return 0.4f;
    }

    @Override
    protected double getBaseDamage() {
        return 4.0;
    }

    public EntityShulkerBullet(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityShulkerBullet(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (this.age > 1200 || this.isCollided || this.hadCollision) {
            this.close();
            return false;
        }
        super.onUpdate(n);
        return !this.closed;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        this.level.addSound((Vector3)this, Sound.MOB_SHULKER_BULLET_HIT);
        this.close();
        return true;
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        super.onCollideWithEntity(entity);
        this.level.addSound((Vector3)this, Sound.MOB_SHULKER_BULLET_HIT);
        entity.addEffect(Effect.getEffect(24).setDuration(200));
    }
}


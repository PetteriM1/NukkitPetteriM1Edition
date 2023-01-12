/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityAreaEffectCloud;
import cn.nukkit.entity.mob.EntityEnderDragon;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.Potion;

public class EntityEnderCharge
extends EntityProjectile {
    public static final int NETWORK_ID = 79;

    public EntityEnderCharge(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityEnderCharge(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    public int getNetworkId() {
        return 79;
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
        return 0.0;
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
        if (this.isCollided) {
            this.splash();
        }
        if (this.age > 1200 || this.isCollided || this.hadCollision) {
            this.close();
            return false;
        }
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return bl;
    }

    protected void splash() {
        this.saveNBT();
        ListTag listTag = (ListTag)this.namedTag.getList("Pos", CompoundTag.class).copy();
        EntityAreaEffectCloud entityAreaEffectCloud = (EntityAreaEffectCloud)Entity.createEntity("AreaEffectCloud", this.getChunk(), new CompoundTag().putList(listTag).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0.0f)).add(new FloatTag("", 0.0f))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putShort("PotionId", 24).putFloat("InitialRadius", 5.0f).putInt("ParticleColor", 8339378), new Object[0]);
        Effect effect = Potion.getEffect(24, true);
        if (effect != null && entityAreaEffectCloud != null) {
            entityAreaEffectCloud.cloudEffects.add(effect.setDuration(1).setVisible(false).setAmbient(false));
            entityAreaEffectCloud.spawnToAll();
        }
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        this.splash();
        this.close();
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return !(entity instanceof EntityEnderDragon);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.potion.PotionCollideEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.SpellParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.Potion;

public class EntityPotion
extends EntityProjectile {
    public static final int NETWORK_ID = 86;
    public int potionId;

    public EntityPotion(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityPotion(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.potionId = this.namedTag.getShort("PotionId");
        this.dataProperties.putShort(36, this.potionId);
        Effect effect = Potion.getEffect(this.potionId, true);
        if (effect != null) {
            int n = 0;
            int[] nArray = effect.getColor();
            int n2 = nArray[0] * (effect.getAmplifier() + 1) / (n += effect.getAmplifier() + 1) & 0xFF;
            int n3 = nArray[1] * (effect.getAmplifier() + 1) / n & 0xFF;
            int n4 = nArray[2] * (effect.getAmplifier() + 1) / n & 0xFF;
            this.setDataProperty(new IntEntityData(8, (n2 << 16) + (n3 << 8) + n4));
        }
    }

    @Override
    public int getNetworkId() {
        return 86;
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
        return 0.05f;
    }

    @Override
    protected float getDrag() {
        return 0.01f;
    }

    protected void splash(Entity entity) {
        Object[] objectArray;
        int n;
        int n2;
        int n3;
        Potion potion = Potion.getPotion(this.potionId);
        PotionCollideEvent potionCollideEvent = new PotionCollideEvent(potion, this);
        this.server.getPluginManager().callEvent(potionCollideEvent);
        if (potionCollideEvent.isCancelled()) {
            return;
        }
        this.close();
        potion = potionCollideEvent.getPotion();
        if (potion == null) {
            return;
        }
        potion.setSplash(true);
        Effect effect = Potion.getEffect(potion.getId(), true);
        if (effect == null) {
            n3 = 40;
            n2 = 40;
            n = 255;
        } else {
            objectArray = effect.getColor();
            n3 = objectArray[0];
            n2 = objectArray[1];
            n = objectArray[2];
        }
        SpellParticle spellParticle = new SpellParticle(this, n3, n2, n);
        this.getLevel().addParticle(spellParticle);
        this.getLevel().addLevelSoundEvent(this, 127);
        for (int n4 : objectArray = (Object[])this.getLevel().getNearbyEntities(this.getBoundingBox().grow(4.125, 2.125, 4.125), this)) {
            double d2;
            if (n4 instanceof Player && ((Player)n4).isSpectator() || !((d2 = n4.distanceSquared(this)) < 16.0)) continue;
            double d3 = n4.equals(entity) ? 1.0 : 1.0 - Math.sqrt(d2) / 4.0;
            potion.applyPotion((Entity)n4, d3);
        }
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        this.splash(entity);
        this.close();
    }

    @Override
    public boolean onUpdate(int n) {
        boolean bl = super.onUpdate(n);
        if (this.closed) {
            return false;
        }
        if (this.timing != null) {
            this.timing.startTiming();
        }
        if (this.isCollided) {
            this.splash(null);
        }
        if (this.age > 1200 || this.isCollided) {
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
}


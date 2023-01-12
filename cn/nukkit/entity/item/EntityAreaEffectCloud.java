/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityBoss;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.data.ShortEntityData;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityRegainHealthEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.InstantEffect;
import cn.nukkit.potion.Potion;
import java.util.ArrayList;
import java.util.List;

public class EntityAreaEffectCloud
extends Entity {
    public static final int NETWORK_ID = 95;
    private int n;
    private int m;
    private float o;
    private float p;
    private int k;
    public List<Effect> cloudEffects;
    private int l;

    public EntityAreaEffectCloud(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    public int getWaitTime() {
        return this.getDataPropertyInt(62);
    }

    public void setWaitTime(int n) {
        this.setWaitTime(n, true);
    }

    public void setWaitTime(int n, boolean bl) {
        this.setDataProperty(new IntEntityData(62, n), bl);
    }

    public int getPotionId() {
        return this.getDataPropertyShort(36);
    }

    public void setPotionId(int n) {
        this.setPotionId(n, true);
    }

    public void setPotionId(int n, boolean bl) {
        this.setDataProperty(new ShortEntityData(36, n & 0xFFFF), bl);
    }

    public void recalculatePotionColor() {
        this.recalculatePotionColor(true);
    }

    public void recalculatePotionColor(boolean bl) {
        int n;
        int n2;
        int n3;
        int n4;
        if (this.namedTag.contains("ParticleColor")) {
            int n5 = this.namedTag.getInt("ParticleColor");
            n4 = (n5 & 0xFF000000) >> 24;
            n3 = (n5 & 0xFF0000) >> 16;
            n2 = (n5 & 0xFF00) >> 8;
            n = n5 & 0xFF;
        } else {
            n4 = 255;
            Effect effect = Potion.getEffect(this.getPotionId(), true);
            if (effect == null) {
                n3 = 40;
                n2 = 40;
                n = 255;
            } else {
                int[] nArray = effect.getColor();
                n3 = nArray[0];
                n2 = nArray[1];
                n = nArray[2];
            }
        }
        this.setPotionColor(n4, n3, n2, n, bl);
    }

    public int getPotionColor() {
        return this.getDataPropertyInt(8);
    }

    public void setPotionColor(int n, int n2, int n3, int n4, boolean bl) {
        this.setPotionColor((n & 0xFF) << 24 | (n2 & 0xFF) << 16 | (n3 & 0xFF) << 8 | n4 & 0xFF, bl);
    }

    public void setPotionColor(int n) {
        this.setPotionColor(n, true);
    }

    public void setPotionColor(int n, boolean bl) {
        this.setDataProperty(new IntEntityData(8, n), bl);
    }

    public int getPickupCount() {
        return this.getDataPropertyInt(99);
    }

    public void setPickupCount(int n) {
        this.setPickupCount(n, true);
    }

    public void setPickupCount(int n, boolean bl) {
        this.setDataProperty(new IntEntityData(99, n), bl);
    }

    public float getRadiusChangeOnPickup() {
        return this.getDataPropertyFloat(98);
    }

    public void setRadiusChangeOnPickup(float f2) {
        this.setRadiusChangeOnPickup(f2, true);
    }

    public void setRadiusChangeOnPickup(float f2, boolean bl) {
        this.setDataProperty(new FloatEntityData(98, f2), bl);
    }

    public float getRadiusPerTick() {
        return this.getDataPropertyFloat(97);
    }

    public void setRadiusPerTick(float f2) {
        this.setRadiusPerTick(f2, true);
    }

    public void setRadiusPerTick(float f2, boolean bl) {
        this.setDataProperty(new FloatEntityData(97, f2), bl);
    }

    public long getSpawnTime() {
        return this.getDataPropertyInt(96);
    }

    public void setSpawnTime(long l) {
        this.setSpawnTime(l, true);
    }

    public void setSpawnTime(long l, boolean bl) {
        this.setDataProperty(new LongEntityData(96, l), bl);
    }

    public int getDuration() {
        return this.getDataPropertyInt(95);
    }

    public void setDuration(int n) {
        this.setDuration(n, true);
    }

    public void setDuration(int n, boolean bl) {
        this.setDataProperty(new IntEntityData(95, n), bl);
    }

    public float getRadius() {
        return this.getDataPropertyFloat(61);
    }

    public void setRadius(float f2) {
        this.setRadius(f2, true);
    }

    public void setRadius(float f2, boolean bl) {
        this.setDataProperty(new FloatEntityData(61, f2), bl);
    }

    public int getParticleId() {
        return this.getDataPropertyInt(63);
    }

    public void setParticleId(int n) {
        this.setParticleId(n, true);
    }

    public void setParticleId(int n, boolean bl) {
        this.setDataProperty(new IntEntityData(63, n), bl);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.invulnerable = true;
        this.fireProof = true;
        this.setDataProperty(new ShortEntityData(63, 32), false);
        this.setDataProperty(new LongEntityData(96, this.level.getCurrentTick()), false);
        this.setDataProperty(new IntEntityData(99, 0), false);
        this.cloudEffects = new ArrayList<Effect>(1);
        for (CompoundTag compoundTag : this.namedTag.getList("mobEffects", CompoundTag.class).getAll()) {
            Effect effect = Effect.getEffect(compoundTag.getByte("Id")).setAmbient(compoundTag.getBoolean("Ambient")).setAmplifier(compoundTag.getByte("Amplifier")).setVisible(compoundTag.getBoolean("DisplayOnScreenTextureAnimation")).setDuration(compoundTag.getInt("Duration"));
            this.cloudEffects.add(effect);
        }
        int n = this.namedTag.getShort("PotionId");
        this.setPotionId(n, false);
        this.recalculatePotionColor();
        if (this.namedTag.contains("Duration")) {
            this.setDuration(this.namedTag.getInt("Duration"), false);
        } else {
            this.setDuration(600, false);
        }
        this.m = this.namedTag.contains("DurationOnUse") ? this.namedTag.getInt("DurationOnUse") : 0;
        this.n = this.namedTag.contains("ReapplicationDelay") ? this.namedTag.getInt("ReapplicationDelay") : 0;
        this.o = this.namedTag.contains("InitialRadius") ? this.namedTag.getFloat("InitialRadius") : 3.0f;
        if (this.namedTag.contains("Radius")) {
            this.setRadius(this.namedTag.getFloat("Radius"), false);
        } else {
            this.setRadius(this.o, false);
        }
        if (this.namedTag.contains("RadiusChangeOnPickup")) {
            this.setRadiusChangeOnPickup(this.namedTag.getFloat("RadiusChangeOnPickup"), false);
        } else {
            this.setRadiusChangeOnPickup(-0.5f, false);
        }
        this.p = this.namedTag.contains("RadiusOnUse") ? this.namedTag.getFloat("RadiusOnUse") : -0.5f;
        if (this.namedTag.contains("RadiusPerTick")) {
            this.setRadiusPerTick(this.namedTag.getFloat("RadiusPerTick"), false);
        } else {
            this.setRadiusPerTick(-0.005f, false);
        }
        if (this.namedTag.contains("WaitTime")) {
            this.setWaitTime(this.namedTag.getInt("WaitTime"), false);
        } else {
            this.setWaitTime(10, false);
        }
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        return false;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        ListTag<CompoundTag> listTag = new ListTag<CompoundTag>("mobEffects");
        for (Effect effect : this.cloudEffects) {
            listTag.add(new CompoundTag().putByte("Id", effect.getId()).putBoolean("Ambient", effect.isAmbient()).putByte("Amplifier", effect.getAmplifier()).putBoolean("DisplayOnScreenTextureAnimation", effect.isVisible()).putInt("Duration", effect.getDuration()));
        }
        this.namedTag.putList(listTag);
        this.namedTag.putInt("ParticleColor", this.getPotionColor());
        this.namedTag.putShort("PotionId", this.getPotionId());
        this.namedTag.putInt("Duration", this.getDuration());
        this.namedTag.putInt("DurationOnUse", this.m);
        this.namedTag.putInt("ReapplicationDelay", this.n);
        this.namedTag.putFloat("Radius", this.getRadius());
        this.namedTag.putFloat("RadiusChangeOnPickup", this.getRadiusChangeOnPickup());
        this.namedTag.putFloat("RadiusOnUse", this.p);
        this.namedTag.putFloat("RadiusPerTick", this.getRadiusPerTick());
        this.namedTag.putInt("WaitTime", this.getWaitTime());
        this.namedTag.putFloat("InitialRadius", this.o);
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (this.timing != null) {
            this.timing.startTiming();
        }
        super.onUpdate(n);
        boolean bl = this.age % 10 == 0;
        int n2 = this.age;
        float f2 = this.getRadius();
        int n3 = this.getWaitTime();
        if (n2 < n3) {
            f2 = this.o;
        } else {
            if (n2 > n3 + this.getDuration()) {
                this.close();
                if (this.timing != null) {
                    this.timing.stopTiming();
                }
                return false;
            }
            int n4 = n2 - this.l;
            f2 += this.getRadiusPerTick() * (float)n4;
            if ((this.k -= n4) <= 0) {
                this.k = this.n + 10;
                Entity[] entityArray = this.level.getCollidingEntities(this.getBoundingBox());
                if (entityArray.length > 0) {
                    f2 += this.p;
                    this.p /= 2.0f;
                    this.setDuration(this.getDuration() + this.m);
                    for (Entity entity : entityArray) {
                        if (entity == this || !(entity instanceof EntityLiving) || entity instanceof EntityBoss) continue;
                        for (Effect effect : this.cloudEffects) {
                            if (effect instanceof InstantEffect) {
                                boolean bl2 = false;
                                if (effect.getId() == 7) {
                                    bl2 = true;
                                }
                                if (entity instanceof EntitySmite) {
                                    boolean bl3 = bl2 = !bl2;
                                }
                                if (bl2) {
                                    entity.attack(new EntityDamageByEntityEvent((Entity)this, entity, EntityDamageEvent.DamageCause.MAGIC, (float)(0.5 * (double)(6 << effect.getAmplifier() + 1))));
                                    continue;
                                }
                                entity.heal(new EntityRegainHealthEvent(entity, (float)(0.5 * (double)(4 << effect.getAmplifier() + 1)), 2));
                                continue;
                            }
                            entity.addEffect(effect);
                        }
                    }
                }
            }
        }
        this.l = n2;
        if ((double)f2 <= 1.5 && n2 >= n3) {
            this.setRadius(f2, false);
            this.close();
            if (this.timing != null) {
                this.timing.stopTiming();
            }
            return false;
        }
        this.setRadius(f2, bl);
        float f3 = this.getHeight();
        this.boundingBox.setBounds(this.x - (double)f2, this.y - (double)f3, this.z - (double)f2, this.x + (double)f2, this.y + (double)f3, this.z + (double)f2);
        this.setDataProperty(new FloatEntityData(54, f3), false);
        this.setDataProperty(new FloatEntityData(53, f2), false);
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return true;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return entity instanceof EntityLiving;
    }

    @Override
    public float getHeight() {
        return 0.3f + this.getRadius() / 2.0f;
    }

    @Override
    public float getWidth() {
        return this.getRadius();
    }

    @Override
    public float getLength() {
        return this.getRadius();
    }

    @Override
    protected float getGravity() {
        return 0.0f;
    }

    @Override
    protected float getDrag() {
        return 0.0f;
    }

    @Override
    public int getNetworkId() {
        return 95;
    }
}


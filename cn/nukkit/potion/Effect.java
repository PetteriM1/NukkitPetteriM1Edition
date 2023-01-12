/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.potion;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityBoss;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityRegainHealthEvent;
import cn.nukkit.network.protocol.MobEffectPacket;
import cn.nukkit.potion.InstantEffect;
import cn.nukkit.utils.ServerException;

public class Effect
implements Cloneable {
    public static final int SPEED = 1;
    public static final int SLOWNESS = 2;
    public static final int HASTE = 3;
    public static final int SWIFTNESS = 3;
    public static final int FATIGUE = 4;
    public static final int MINING_FATIGUE = 4;
    public static final int STRENGTH = 5;
    public static final int HEALING = 6;
    public static final int INSTANT_HEALTH = 6;
    public static final int HARMING = 7;
    public static final int INSTANT_DAMAGE = 7;
    public static final int JUMP = 8;
    public static final int JUMP_BOOST = 8;
    public static final int NAUSEA = 9;
    public static final int CONFUSION = 9;
    public static final int REGENERATION = 10;
    public static final int DAMAGE_RESISTANCE = 11;
    public static final int RESISTANCE = 11;
    public static final int FIRE_RESISTANCE = 12;
    public static final int WATER_BREATHING = 13;
    public static final int INVISIBILITY = 14;
    public static final int BLINDNESS = 15;
    public static final int NIGHT_VISION = 16;
    public static final int HUNGER = 17;
    public static final int WEAKNESS = 18;
    public static final int POISON = 19;
    public static final int WITHER = 20;
    public static final int HEALTH_BOOST = 21;
    public static final int ABSORPTION = 22;
    public static final int SATURATION = 23;
    public static final int LEVITATION = 24;
    public static final int FATAL_POISON = 25;
    public static final int CONDUIT_POWER = 26;
    public static final int SLOW_FALLING = 27;
    public static final int BAD_OMEN = 28;
    public static final int VILLAGE_HERO = 29;
    public static final int DARKNESS = 30;
    protected static Effect[] effects;
    protected final int id;
    protected final String name;
    protected int duration;
    protected int amplifier = 0;
    protected int color;
    protected boolean show = true;
    protected boolean ambient = false;
    protected final boolean bad;

    public static void init() {
        effects = new Effect[256];
        Effect.effects[1] = new Effect(1, "%potion.moveSpeed", 124, 175, 198);
        Effect.effects[2] = new Effect(2, "%potion.moveSlowdown", 90, 108, 129, true);
        Effect.effects[3] = new Effect(3, "%potion.digSpeed", 217, 192, 67);
        Effect.effects[4] = new Effect(4, "%potion.digSlowDown", 74, 66, 23, true);
        Effect.effects[5] = new Effect(5, "%potion.damageBoost", 147, 36, 35);
        Effect.effects[6] = new InstantEffect(6, "%potion.heal", 248, 36, 35);
        Effect.effects[7] = new InstantEffect(7, "%potion.harm", 67, 10, 9, true);
        Effect.effects[8] = new Effect(8, "%potion.jump", 34, 255, 76);
        Effect.effects[9] = new Effect(9, "%potion.confusion", 85, 29, 74, true);
        Effect.effects[10] = new Effect(10, "%potion.regeneration", 205, 92, 171);
        Effect.effects[11] = new Effect(11, "%potion.resistance", 153, 69, 58);
        Effect.effects[12] = new Effect(12, "%potion.fireResistance", 228, 154, 58);
        Effect.effects[13] = new Effect(13, "%potion.waterBreathing", 46, 82, 153);
        Effect.effects[14] = new Effect(14, "%potion.invisibility", 127, 131, 146);
        Effect.effects[15] = new Effect(15, "%potion.blindness", 191, 192, 192);
        Effect.effects[16] = new Effect(16, "%potion.nightVision", 0, 0, 139);
        Effect.effects[17] = new Effect(17, "%potion.hunger", 46, 139, 87);
        Effect.effects[18] = new Effect(18, "%potion.weakness", 72, 77, 72, true);
        Effect.effects[19] = new Effect(19, "%potion.poison", 78, 147, 49, true);
        Effect.effects[20] = new Effect(20, "%potion.wither", 53, 42, 39, true);
        Effect.effects[21] = new Effect(21, "%potion.healthBoost", 248, 125, 35);
        Effect.effects[22] = new Effect(22, "%potion.absorption", 36, 107, 251);
        Effect.effects[23] = new Effect(23, "%potion.saturation", 255, 0, 255);
        Effect.effects[24] = new Effect(24, "%potion.levitation", 206, 255, 255, true);
        Effect.effects[25] = new Effect(25, "%potion.poison", 78, 147, 49, true);
        Effect.effects[26] = new Effect(26, "%potion.conduitPower", 29, 194, 209);
        Effect.effects[27] = new Effect(27, "%potion.slowFalling", 206, 255, 255);
        Effect.effects[28] = new Effect(28, "%effect.badOmen", 11, 97, 56, true);
        Effect.effects[29] = new Effect(29, "%effect.villageHero", 68, 255, 68).setVisible(false);
        Effect.effects[30] = new Effect(30, "%effect.darkness", 41, 39, 33, true).setVisible(false);
    }

    public static Effect getEffect(int n) {
        if (n >= 0 && n < effects.length && effects[n] != null) {
            return effects[n].clone();
        }
        throw new ServerException("Effect id: " + n + " not found");
    }

    public static Effect getEffectByName(String string) {
        string = string.trim().replace(' ', '_').replace("minecraft:", "");
        try {
            int n = Effect.class.getField(string.toUpperCase()).getInt(null);
            return Effect.getEffect(n);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public Effect(int n, String string, int n2, int n3, int n4) {
        this(n, string, n2, n3, n4, false);
    }

    public Effect(int n, String string, int n2, int n3, int n4, boolean bl) {
        this.id = n;
        this.name = string;
        this.bad = bl;
        this.setColor(n2, n3, n4);
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public Effect setDuration(int n) {
        this.duration = n;
        return this;
    }

    public int getDuration() {
        return this.duration;
    }

    public boolean isVisible() {
        return this.show;
    }

    public Effect setVisible(boolean bl) {
        this.show = bl;
        return this;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public Effect setAmplifier(int n) {
        this.amplifier = n;
        return this;
    }

    public boolean isAmbient() {
        return this.ambient;
    }

    public Effect setAmbient(boolean bl) {
        this.ambient = bl;
        return this;
    }

    public boolean isBad() {
        return this.bad;
    }

    public boolean canTick() {
        switch (this.id) {
            case 19: 
            case 25: {
                int n = 25 >> this.amplifier;
                if (n > 0) {
                    return this.duration % n == 0;
                }
                return true;
            }
            case 20: {
                int n = 50 >> this.amplifier;
                if (n > 0) {
                    return this.duration % n == 0;
                }
                return true;
            }
            case 10: {
                int n = 40 >> this.amplifier;
                if (n > 0) {
                    return this.duration % n == 0;
                }
                return true;
            }
        }
        return false;
    }

    public void applyEffect(Entity entity) {
        if (entity instanceof EntityBoss) {
            return;
        }
        switch (this.id) {
            case 19: 
            case 25: {
                if (!(entity.getHealth() > 1.0f) && this.id != 25) break;
                entity.attack(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.MAGIC, 1.0f));
                break;
            }
            case 20: {
                entity.attack(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.MAGIC, 1.0f));
                break;
            }
            case 10: {
                if (!(entity.getHealth() < (float)entity.getRealMaxHealth())) break;
                entity.heal(new EntityRegainHealthEvent(entity, 1.0f, 2));
                break;
            }
        }
    }

    public int[] getColor() {
        return new int[]{this.color >> 16, this.color >> 8 & 0xFF, this.color & 0xFF};
    }

    public void setColor(int n, int n2, int n3) {
        this.color = ((n & 0xFF) << 16) + ((n2 & 0xFF) << 8) + (n3 & 0xFF);
    }

    public void add(Entity entity) {
        block5: {
            int n;
            Effect effect = entity.getEffect(this.id);
            if (effect != null && (Math.abs(this.amplifier) < Math.abs(effect.amplifier) || Math.abs(this.amplifier) == Math.abs(effect.amplifier) && this.duration < effect.duration)) {
                return;
            }
            if (entity instanceof Player) {
                Player player = (Player)entity;
                MobEffectPacket mobEffectPacket = new MobEffectPacket();
                mobEffectPacket.eid = entity.getId();
                mobEffectPacket.effectId = this.id;
                mobEffectPacket.amplifier = this.amplifier;
                mobEffectPacket.particles = this.show;
                mobEffectPacket.duration = this.duration;
                mobEffectPacket.eventId = effect != null ? 2 : 1;
                player.dataPacket(mobEffectPacket);
                if (this.id == 1) {
                    player.setMovementSpeed(0.1f * (1.0f + 0.2f * (float)(this.amplifier + 1)));
                }
                if (this.id == 2) {
                    player.setMovementSpeed(0.1f * (1.0f - 0.15f * (float)(this.amplifier + 1)));
                }
            }
            if (this.id == 14) {
                entity.setDataFlag(0, 5, true);
                entity.setNameTagVisible(false);
            }
            if (this.id != 22 || !((float)(n = this.amplifier + 1 << 2) > entity.getAbsorption())) break block5;
            entity.setAbsorption(n);
        }
    }

    public void remove(Entity entity) {
        block4: {
            if (entity instanceof Player) {
                MobEffectPacket mobEffectPacket = new MobEffectPacket();
                mobEffectPacket.eid = entity.getId();
                mobEffectPacket.effectId = this.id;
                mobEffectPacket.eventId = 3;
                ((Player)entity).dataPacket(mobEffectPacket);
                if (this.id == 1) {
                    ((Player)entity).setMovementSpeed(((Player)entity).getMovementSpeed() / (1.0f + 0.2f * (float)(this.amplifier + 1)));
                }
                if (this.id == 2) {
                    ((Player)entity).setMovementSpeed(((Player)entity).getMovementSpeed() / (1.0f - 0.15f * (float)(this.amplifier + 1)));
                }
            }
            if (this.id == 14) {
                entity.setDataFlag(0, 5, false);
                entity.setNameTagVisible(true);
            }
            if (this.id != 22) break block4;
            entity.setAbsorption(0.0f);
        }
    }

    public Effect clone() {
        try {
            return (Effect)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    private static ServerException a(ServerException serverException) {
        return serverException;
    }
}


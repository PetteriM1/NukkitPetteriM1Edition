/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.potion;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.mob.EntityBlaze;
import cn.nukkit.entity.mob.EntityEnderman;
import cn.nukkit.entity.mob.EntitySnowGolem;
import cn.nukkit.entity.passive.EntityStrider;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityRegainHealthEvent;
import cn.nukkit.event.potion.PotionApplyEvent;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.ServerException;

public class Potion
implements Cloneable {
    public static final int NO_EFFECTS = 0;
    public static final int WATER = 0;
    public static final int MUNDANE = 1;
    public static final int MUNDANE_II = 2;
    public static final int THICK = 3;
    public static final int AWKWARD = 4;
    public static final int NIGHT_VISION = 5;
    public static final int NIGHT_VISION_LONG = 6;
    public static final int INVISIBLE = 7;
    public static final int INVISIBLE_LONG = 8;
    public static final int LEAPING = 9;
    public static final int LEAPING_LONG = 10;
    public static final int LEAPING_II = 11;
    public static final int FIRE_RESISTANCE = 12;
    public static final int FIRE_RESISTANCE_LONG = 13;
    public static final int SPEED = 14;
    public static final int SPEED_LONG = 15;
    public static final int SPEED_II = 16;
    public static final int SLOWNESS = 17;
    public static final int SLOWNESS_LONG = 18;
    public static final int WATER_BREATHING = 19;
    public static final int WATER_BREATHING_LONG = 20;
    public static final int INSTANT_HEALTH = 21;
    public static final int INSTANT_HEALTH_II = 22;
    public static final int HARMING = 23;
    public static final int HARMING_II = 24;
    public static final int POISON = 25;
    public static final int POISON_LONG = 26;
    public static final int POISON_II = 27;
    public static final int REGENERATION = 28;
    public static final int REGENERATION_LONG = 29;
    public static final int REGENERATION_II = 30;
    public static final int STRENGTH = 31;
    public static final int STRENGTH_LONG = 32;
    public static final int STRENGTH_II = 33;
    public static final int WEAKNESS = 34;
    public static final int WEAKNESS_LONG = 35;
    public static final int WITHER_II = 36;
    public static final int TURTLE_MASTER = 37;
    public static final int TURTLE_MASTER_LONG = 38;
    public static final int TURTLE_MASTER_II = 39;
    public static final int SLOW_FALLING = 40;
    public static final int SLOW_FALLING_LONG = 41;
    public static final int SLOWNESS_LONG_II = 42;
    public static final int SLOWNESS_IV = 43;
    protected static Potion[] potions;
    protected final int id;
    protected final int level;
    protected boolean splash;

    public static void init() {
        potions = new Potion[256];
        Potion.potions[0] = new Potion(0);
        Potion.potions[1] = new Potion(1);
        Potion.potions[2] = new Potion(2, 2);
        Potion.potions[3] = new Potion(3);
        Potion.potions[4] = new Potion(4);
        Potion.potions[5] = new Potion(5);
        Potion.potions[6] = new Potion(6);
        Potion.potions[7] = new Potion(7);
        Potion.potions[8] = new Potion(8);
        Potion.potions[9] = new Potion(9);
        Potion.potions[10] = new Potion(10);
        Potion.potions[11] = new Potion(11, 2);
        Potion.potions[12] = new Potion(12);
        Potion.potions[13] = new Potion(13);
        Potion.potions[14] = new Potion(14);
        Potion.potions[15] = new Potion(15);
        Potion.potions[16] = new Potion(16, 2);
        Potion.potions[17] = new Potion(17);
        Potion.potions[18] = new Potion(18);
        Potion.potions[19] = new Potion(19);
        Potion.potions[20] = new Potion(20);
        Potion.potions[21] = new Potion(21);
        Potion.potions[22] = new Potion(22, 2);
        Potion.potions[23] = new Potion(23);
        Potion.potions[24] = new Potion(24, 2);
        Potion.potions[25] = new Potion(25);
        Potion.potions[26] = new Potion(26);
        Potion.potions[27] = new Potion(27, 2);
        Potion.potions[28] = new Potion(28);
        Potion.potions[29] = new Potion(29);
        Potion.potions[30] = new Potion(30, 2);
        Potion.potions[31] = new Potion(31);
        Potion.potions[32] = new Potion(32);
        Potion.potions[33] = new Potion(33, 2);
        Potion.potions[34] = new Potion(34);
        Potion.potions[35] = new Potion(35);
        Potion.potions[36] = new Potion(36, 2);
        Potion.potions[37] = new Potion(37);
        Potion.potions[38] = new Potion(38);
        Potion.potions[39] = new Potion(39, 2);
        Potion.potions[40] = new Potion(40);
        Potion.potions[41] = new Potion(41);
        Potion.potions[42] = new Potion(42, 2);
        Potion.potions[43] = new Potion(17, 4);
    }

    public static Potion getPotion(int n) {
        if (n >= 0 && n < potions.length && potions[n] != null) {
            return potions[n].clone();
        }
        throw new ServerException("Effect id: " + n + " not found");
    }

    public static Potion getPotionByName(String string) {
        try {
            byte by = Potion.class.getField(string.toUpperCase()).getByte(null);
            return Potion.getPotion(by);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public Potion(int n) {
        this(n, 1);
    }

    public Potion(int n, int n2) {
        this(n, n2, false);
    }

    public Potion(int n, int n2, boolean bl) {
        this.id = n;
        this.level = n2;
        this.splash = bl;
    }

    public Effect getEffect() {
        return Potion.getEffect(this.id, this.splash);
    }

    public int getId() {
        return this.id;
    }

    public int getLevel() {
        return this.level;
    }

    public boolean isSplash() {
        return this.splash;
    }

    public Potion setSplash(boolean bl) {
        this.splash = bl;
        return this;
    }

    public void applyPotion(Entity entity) {
        this.applyPotion(entity, 0.5);
    }

    public void applyPotion(Entity entity, double d2) {
        if (!(entity instanceof EntityLiving)) {
            return;
        }
        if (this.id == 0 && (entity instanceof EntityEnderman || entity instanceof EntityStrider || entity instanceof EntitySnowGolem || entity instanceof EntityBlaze)) {
            entity.attack(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.MAGIC, 1.0f));
            return;
        }
        Effect effect = Potion.getEffect(this.id, this.splash);
        if (effect == null) {
            return;
        }
        PotionApplyEvent potionApplyEvent = new PotionApplyEvent(this, effect, entity);
        entity.getServer().getPluginManager().callEvent(potionApplyEvent);
        if (potionApplyEvent.isCancelled()) {
            return;
        }
        effect = potionApplyEvent.getApplyEffect();
        switch (this.id) {
            case 21: 
            case 22: {
                if (entity instanceof EntitySmite) {
                    entity.attack(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.MAGIC, (float)(d2 * (double)(6 << effect.getAmplifier() + 1))));
                    break;
                }
                entity.heal(new EntityRegainHealthEvent(entity, (float)(d2 * (double)(4 << effect.getAmplifier() + 1)), 2));
                break;
            }
            case 23: {
                if (entity instanceof EntitySmite) {
                    entity.heal(new EntityRegainHealthEvent(entity, (float)(d2 * (double)(4 << effect.getAmplifier() + 1)), 2));
                    break;
                }
                entity.attack(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.MAGIC, (float)(d2 * 6.0)));
                break;
            }
            case 24: {
                if (entity instanceof EntitySmite) {
                    entity.heal(new EntityRegainHealthEvent(entity, (float)(d2 * (double)(4 << effect.getAmplifier() + 1)), 2));
                    break;
                }
                entity.attack(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.MAGIC, (float)(d2 * 12.0)));
                break;
            }
            default: {
                effect.setDuration((int)((this.splash ? d2 : 1.0) * (double)effect.getDuration() + 0.5));
                entity.addEffect(effect);
            }
        }
    }

    public Potion clone() {
        try {
            return (Potion)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    public static Effect getEffect(int n, boolean bl) {
        Effect effect;
        block18: {
            switch (n) {
                case 0: 
                case 1: 
                case 2: 
                case 3: 
                case 4: {
                    return null;
                }
                case 5: 
                case 6: {
                    effect = Effect.getEffect(16);
                    break;
                }
                case 7: 
                case 8: {
                    effect = Effect.getEffect(14);
                    break;
                }
                case 9: 
                case 10: 
                case 11: {
                    effect = Effect.getEffect(8);
                    break;
                }
                case 12: 
                case 13: {
                    effect = Effect.getEffect(12);
                    break;
                }
                case 14: 
                case 15: 
                case 16: {
                    effect = Effect.getEffect(1);
                    break;
                }
                case 17: 
                case 18: 
                case 42: 
                case 43: {
                    effect = Effect.getEffect(2);
                    break;
                }
                case 19: 
                case 20: {
                    effect = Effect.getEffect(13);
                    break;
                }
                case 21: 
                case 22: {
                    return Effect.getEffect(6);
                }
                case 23: 
                case 24: {
                    return Effect.getEffect(7);
                }
                case 25: 
                case 26: 
                case 27: {
                    effect = Effect.getEffect(19);
                    break;
                }
                case 28: 
                case 29: 
                case 30: {
                    effect = Effect.getEffect(10);
                    break;
                }
                case 31: 
                case 32: 
                case 33: {
                    effect = Effect.getEffect(5);
                    break;
                }
                case 34: 
                case 35: {
                    effect = Effect.getEffect(18);
                    break;
                }
                case 36: {
                    effect = Effect.getEffect(20);
                    break;
                }
                default: {
                    return null;
                }
            }
            if (Potion.getLevel(n) > 1) {
                effect.setAmplifier(1);
            }
            if (Potion.isInstant(n)) break block18;
            effect.setDuration(20 * Potion.getApplySeconds(n, bl));
        }
        return effect;
    }

    public static int getLevel(int n) {
        switch (n) {
            case 43: {
                return 4;
            }
            case 2: 
            case 11: 
            case 16: 
            case 22: 
            case 24: 
            case 27: 
            case 30: 
            case 33: 
            case 36: 
            case 39: 
            case 42: {
                return 2;
            }
        }
        return 1;
    }

    public static boolean isInstant(int n) {
        switch (n) {
            case 21: 
            case 22: 
            case 23: 
            case 24: {
                return true;
            }
        }
        return false;
    }

    public static int getApplySeconds(int n, boolean bl) {
        if (bl) {
            switch (n) {
                case 0: {
                    return 0;
                }
                case 1: {
                    return 0;
                }
                case 2: {
                    return 0;
                }
                case 3: {
                    return 0;
                }
                case 4: {
                    return 0;
                }
                case 5: {
                    return 135;
                }
                case 6: {
                    return 360;
                }
                case 7: {
                    return 135;
                }
                case 8: {
                    return 360;
                }
                case 9: {
                    return 135;
                }
                case 10: {
                    return 360;
                }
                case 11: {
                    return 67;
                }
                case 12: {
                    return 135;
                }
                case 13: {
                    return 360;
                }
                case 14: {
                    return 135;
                }
                case 15: {
                    return 360;
                }
                case 16: {
                    return 67;
                }
                case 17: {
                    return 67;
                }
                case 18: {
                    return 180;
                }
                case 19: {
                    return 135;
                }
                case 20: {
                    return 360;
                }
                case 21: {
                    return 0;
                }
                case 22: {
                    return 0;
                }
                case 23: {
                    return 0;
                }
                case 24: {
                    return 0;
                }
                case 25: {
                    return 33;
                }
                case 26: {
                    return 90;
                }
                case 27: {
                    return 16;
                }
                case 28: {
                    return 33;
                }
                case 29: {
                    return 90;
                }
                case 30: {
                    return 16;
                }
                case 31: {
                    return 135;
                }
                case 32: {
                    return 360;
                }
                case 33: {
                    return 67;
                }
                case 34: {
                    return 67;
                }
                case 35: {
                    return 180;
                }
                case 36: {
                    return 30;
                }
                case 43: {
                    return 15;
                }
            }
            return 0;
        }
        switch (n) {
            case 0: {
                return 0;
            }
            case 1: {
                return 0;
            }
            case 2: {
                return 0;
            }
            case 3: {
                return 0;
            }
            case 4: {
                return 0;
            }
            case 5: {
                return 180;
            }
            case 6: {
                return 480;
            }
            case 7: {
                return 180;
            }
            case 8: {
                return 480;
            }
            case 9: {
                return 180;
            }
            case 10: {
                return 480;
            }
            case 11: {
                return 90;
            }
            case 12: {
                return 180;
            }
            case 13: {
                return 480;
            }
            case 14: {
                return 180;
            }
            case 15: {
                return 480;
            }
            case 16: {
                return 90;
            }
            case 17: {
                return 90;
            }
            case 18: {
                return 240;
            }
            case 19: {
                return 180;
            }
            case 20: {
                return 480;
            }
            case 21: {
                return 0;
            }
            case 22: {
                return 0;
            }
            case 23: {
                return 0;
            }
            case 24: {
                return 0;
            }
            case 25: {
                return 45;
            }
            case 26: {
                return 120;
            }
            case 27: {
                return 22;
            }
            case 28: {
                return 45;
            }
            case 29: {
                return 120;
            }
            case 30: {
                return 22;
            }
            case 31: {
                return 180;
            }
            case 32: {
                return 480;
            }
            case 33: {
                return 90;
            }
            case 34: {
                return 90;
            }
            case 35: {
                return 240;
            }
            case 36: {
                return 30;
            }
            case 43: {
                return 20;
            }
        }
        return 0;
    }

    private static ServerException a(ServerException serverException) {
        return serverException;
    }
}


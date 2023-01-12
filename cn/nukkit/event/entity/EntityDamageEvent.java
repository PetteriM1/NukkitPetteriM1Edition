/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;
import cn.nukkit.event.entity.a;
import cn.nukkit.event.entity.b;
import cn.nukkit.utils.EventException;
import com.google.common.collect.ImmutableMap;
import java.util.EnumMap;
import java.util.Map;

public class EntityDamageEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private int f;
    private final DamageCause b;
    private final Map<DamageModifier, Float> e;
    private final Map<DamageModifier, Float> c;

    public static HandlerList getHandlers() {
        return d;
    }

    public EntityDamageEvent(Entity entity, DamageCause damageCause, float f2) {
        this(entity, damageCause, new a(f2));
    }

    public EntityDamageEvent(Entity entity, DamageCause damageCause, Map<DamageModifier, Float> map) {
        block1: {
            this.f = 10;
            this.entity = entity;
            this.b = damageCause;
            this.e = new EnumMap<DamageModifier, Float>(map);
            this.c = ImmutableMap.copyOf(this.e);
            if (!this.e.containsKey((Object)DamageModifier.BASE)) {
                throw new EventException("BASE Damage modifier missing");
            }
            if (!entity.hasEffect(11)) break block1;
            this.setDamage((float)(-((double)this.getDamage(DamageModifier.BASE) * 0.2 * (double)(entity.getEffect(11).getAmplifier() + 1))), DamageModifier.RESISTANCE);
        }
    }

    public DamageCause getCause() {
        return this.b;
    }

    public float getOriginalDamage() {
        return this.getOriginalDamage(DamageModifier.BASE);
    }

    public float getOriginalDamage(DamageModifier damageModifier) {
        if (this.c.containsKey((Object)damageModifier)) {
            return this.c.get((Object)damageModifier).floatValue();
        }
        return 0.0f;
    }

    public float getDamage() {
        return this.getDamage(DamageModifier.BASE);
    }

    public float getDamage(DamageModifier damageModifier) {
        if (this.e.containsKey((Object)damageModifier)) {
            return this.e.get((Object)damageModifier).floatValue();
        }
        return 0.0f;
    }

    public void setDamage(float f2) {
        this.setDamage(f2, DamageModifier.BASE);
    }

    public void setDamage(float f2, DamageModifier damageModifier) {
        this.e.put(damageModifier, Float.valueOf(f2));
    }

    public boolean isApplicable(DamageModifier damageModifier) {
        return this.e.containsKey((Object)damageModifier);
    }

    public float getFinalDamage() {
        float f2 = 0.0f;
        for (Float f3 : this.e.values()) {
            if (f3 == null) continue;
            f2 += f3.floatValue();
        }
        return Math.max(f2, 0.0f);
    }

    public int getAttackCooldown() {
        return this.f;
    }

    public void setAttackCooldown(int n) {
        this.f = n;
    }

    public boolean canBeReducedByArmor() {
        switch (cn.nukkit.event.entity.b.a[this.b.ordinal()]) {
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                return false;
            }
        }
        return true;
    }

    private static EventException a(EventException eventException) {
        return eventException;
    }

    public static enum DamageCause {
        CONTACT,
        ENTITY_ATTACK,
        PROJECTILE,
        SUFFOCATION,
        FALL,
        FIRE,
        FIRE_TICK,
        LAVA,
        MAGMA,
        DROWNING,
        BLOCK_EXPLOSION,
        ENTITY_EXPLOSION,
        VOID,
        SUICIDE,
        MAGIC,
        CUSTOM,
        LIGHTNING,
        HUNGER,
        THORNS;

    }

    public static enum DamageModifier {
        BASE,
        ARMOR,
        STRENGTH,
        WEAKNESS,
        RESISTANCE,
        ABSORPTION,
        ARMOR_ENCHANTMENTS,
        CRITICAL;

    }
}


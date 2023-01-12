/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.enchantment.Enchantment;
import java.util.Map;

public class EntityDamageByEntityEvent
extends EntityDamageEvent {
    private final Entity h;
    private float g;
    private Enchantment[] i;

    public EntityDamageByEntityEvent(Entity entity, Entity entity2, EntityDamageEvent.DamageCause damageCause, float f2) {
        this(entity, entity2, damageCause, f2, 0.3f);
    }

    public EntityDamageByEntityEvent(Entity entity, Entity entity2, EntityDamageEvent.DamageCause damageCause, Map<EntityDamageEvent.DamageModifier, Float> map) {
        this(entity, entity2, damageCause, map, 0.3f);
    }

    public EntityDamageByEntityEvent(Entity entity, Entity entity2, EntityDamageEvent.DamageCause damageCause, float f2, float f3) {
        super(entity2, damageCause, f2);
        this.h = entity;
        this.g = f3;
        this.addAttackerModifiers(entity);
    }

    public EntityDamageByEntityEvent(Entity entity, Entity entity2, EntityDamageEvent.DamageCause damageCause, Map<EntityDamageEvent.DamageModifier, Float> map, float f2) {
        this(entity, entity2, damageCause, map, f2, new Enchantment[0]);
    }

    public EntityDamageByEntityEvent(Entity entity, Entity entity2, EntityDamageEvent.DamageCause damageCause, Map<EntityDamageEvent.DamageModifier, Float> map, float f2, Enchantment[] enchantmentArray) {
        super(entity2, damageCause, map);
        this.h = entity;
        this.g = f2;
        this.i = enchantmentArray;
        this.addAttackerModifiers(entity);
    }

    protected void addAttackerModifiers(Entity entity) {
        if (entity.hasEffect(5)) {
            this.setDamage((float)((double)this.getDamage(EntityDamageEvent.DamageModifier.BASE) * 0.3 * (double)(entity.getEffect(5).getAmplifier() + 1)), EntityDamageEvent.DamageModifier.STRENGTH);
        }
        if (entity.hasEffect(18)) {
            this.setDamage(-((float)((double)this.getDamage(EntityDamageEvent.DamageModifier.BASE) * 0.2 * (double)(entity.getEffect(18).getAmplifier() + 1))), EntityDamageEvent.DamageModifier.WEAKNESS);
        }
    }

    public Entity getDamager() {
        return this.h;
    }

    public float getKnockBack() {
        return this.g;
    }

    public void setKnockBack(float f2) {
        this.g = f2;
    }

    public Enchantment[] getWeaponEnchantments() {
        return this.i;
    }
}


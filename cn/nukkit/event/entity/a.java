/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.event.entity.EntityDamageEvent;
import java.util.EnumMap;

class a
extends EnumMap<EntityDamageEvent.DamageModifier, Float> {
    public a(float f2) {
        super(EntityDamageEvent.DamageModifier.class);
        this.put(EntityDamageEvent.DamageModifier.BASE, Float.valueOf(f2));
    }
}


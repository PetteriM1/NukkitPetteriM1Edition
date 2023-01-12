/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.event.entity.EntityDamageEvent;

class b {
    static final int[] a = new int[EntityDamageEvent.DamageCause.values().length];

    static {
        try {
            b.a[EntityDamageEvent.DamageCause.FIRE_TICK.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            b.a[EntityDamageEvent.DamageCause.SUFFOCATION.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            b.a[EntityDamageEvent.DamageCause.DROWNING.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            b.a[EntityDamageEvent.DamageCause.HUNGER.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            b.a[EntityDamageEvent.DamageCause.FALL.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            b.a[EntityDamageEvent.DamageCause.VOID.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            b.a[EntityDamageEvent.DamageCause.MAGIC.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            b.a[EntityDamageEvent.DamageCause.SUICIDE.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}


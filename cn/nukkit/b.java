/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.network.protocol.AnimatePacket;
import cn.nukkit.network.protocol.BookEditPacket;
import cn.nukkit.network.protocol.types.PlayerActionType;

class b {
    static final int[] c;
    static final int[] a;
    static final int[] b;
    static final int[] d;

    static {
        d = new int[EntityDamageEvent.DamageCause.values().length];
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.ENTITY_ATTACK.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.THORNS.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.PROJECTILE.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.VOID.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.FALL.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.SUFFOCATION.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.LAVA.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.MAGMA.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.FIRE.ordinal()] = 9;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.FIRE_TICK.ordinal()] = 10;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.DROWNING.ordinal()] = 11;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.CONTACT.ordinal()] = 12;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.BLOCK_EXPLOSION.ordinal()] = 13;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.ENTITY_EXPLOSION.ordinal()] = 14;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.MAGIC.ordinal()] = 15;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.LIGHTNING.ordinal()] = 16;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.d[EntityDamageEvent.DamageCause.HUNGER.ordinal()] = 17;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        b = new int[BookEditPacket.Action.values().length];
        try {
            cn.nukkit.b.b[BookEditPacket.Action.REPLACE_PAGE.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.b[BookEditPacket.Action.ADD_PAGE.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.b[BookEditPacket.Action.DELETE_PAGE.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.b[BookEditPacket.Action.SWAP_PAGES.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.b[BookEditPacket.Action.SIGN_BOOK.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        a = new int[AnimatePacket.Action.values().length];
        try {
            cn.nukkit.b.a[AnimatePacket.Action.ROW_RIGHT.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.a[AnimatePacket.Action.ROW_LEFT.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        c = new int[PlayerActionType.values().length];
        try {
            cn.nukkit.b.c[PlayerActionType.START_DESTROY_BLOCK.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.c[PlayerActionType.ABORT_DESTROY_BLOCK.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.c[PlayerActionType.STOP_DESTROY_BLOCK.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.c[PlayerActionType.CONTINUE_DESTROY_BLOCK.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            cn.nukkit.b.c[PlayerActionType.PREDICT_DESTROY_BLOCK.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.level.GameRules;

class j {
    static final int[] a = new int[GameRules.Type.values().length];

    static {
        try {
            j.a[GameRules.Type.BOOLEAN.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            j.a[GameRules.Type.INTEGER.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            j.a[GameRules.Type.FLOAT.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}


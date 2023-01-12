/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.PlayerActionType;

class d {
    static final int[] a = new int[PlayerActionType.values().length];

    static {
        try {
            d.a[PlayerActionType.START_DESTROY_BLOCK.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            d.a[PlayerActionType.ABORT_DESTROY_BLOCK.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            d.a[PlayerActionType.CRACK_BLOCK.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            d.a[PlayerActionType.PREDICT_DESTROY_BLOCK.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            d.a[PlayerActionType.CONTINUE_DESTROY_BLOCK.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}


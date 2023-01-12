/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.utils.Rail;

class b {
    static final int[] a = new int[Rail.Orientation.values().length];

    static {
        try {
            b.a[Rail.Orientation.ASCENDING_NORTH.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            b.a[Rail.Orientation.ASCENDING_SOUTH.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            b.a[Rail.Orientation.ASCENDING_EAST.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            b.a[Rail.Orientation.ASCENDING_WEST.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}


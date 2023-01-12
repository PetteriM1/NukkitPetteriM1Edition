/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.math;

import cn.nukkit.math.NukkitRandom;

public class NukkitMath {
    public static int floorDouble(double d2) {
        int n = (int)d2;
        return d2 >= (double)n ? n : n - 1;
    }

    public static int ceilDouble(double d2) {
        int n = (int)(d2 + 1.0);
        return d2 >= (double)n ? n : n - 1;
    }

    public static int floorFloat(float f2) {
        int n = (int)f2;
        return f2 >= (float)n ? n : n - 1;
    }

    public static int ceilFloat(float f2) {
        int n = (int)(f2 + 1.0f);
        return f2 >= (float)n ? n : n - 1;
    }

    public static int randomRange(NukkitRandom nukkitRandom) {
        return NukkitMath.randomRange(nukkitRandom, 0);
    }

    public static int randomRange(NukkitRandom nukkitRandom, int n) {
        return NukkitMath.randomRange(nukkitRandom, 0, Integer.MAX_VALUE);
    }

    public static int randomRange(NukkitRandom nukkitRandom, int n, int n2) {
        return n + nukkitRandom.nextInt() % (n2 + 1 - n);
    }

    public static double round(double d2) {
        return NukkitMath.round(d2, 0);
    }

    public static double round(double d2, int n) {
        return (double)Math.round(d2 * Math.pow(10.0, n)) / Math.pow(10.0, n);
    }

    public static double clamp(double d2, double d3, double d4) {
        return d2 < d3 ? d3 : (d2 > d4 ? d4 : d2);
    }

    public static int clamp(int n, int n2, int n3) {
        return n < n2 ? n2 : (n > n3 ? n3 : n);
    }

    public static float clamp(float f2, float f3, float f4) {
        return f2 < f3 ? f3 : (f2 > f4 ? f4 : f2);
    }

    public static double getDirection(double d2, double d3) {
        d2 = Math.abs(d2);
        d3 = Math.abs(d3);
        return Math.max(d2, d3);
    }
}


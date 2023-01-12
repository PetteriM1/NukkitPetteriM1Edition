/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.math;

import cn.nukkit.math.Vector2;

public abstract class VectorMath {
    public static Vector2 getDirection2D(double d2) {
        return new Vector2(Math.cos(d2), Math.sin(d2));
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.level.particle.GenericParticle;
import cn.nukkit.math.Vector3;

public class HeartParticle
extends GenericParticle {
    public HeartParticle(Vector3 vector3) {
        this(vector3, 0);
    }

    public HeartParticle(Vector3 vector3, int n) {
        super(vector3, 19, n);
    }
}


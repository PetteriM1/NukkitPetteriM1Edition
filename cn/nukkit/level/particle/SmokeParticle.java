/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.level.particle.GenericParticle;
import cn.nukkit.math.Vector3;

public class SmokeParticle
extends GenericParticle {
    public SmokeParticle(Vector3 vector3) {
        this(vector3, 0);
    }

    public SmokeParticle(Vector3 vector3, int n) {
        super(vector3, 5, n);
    }
}


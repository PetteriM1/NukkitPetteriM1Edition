/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.level.particle.GenericParticle;
import cn.nukkit.math.Vector3;

public class InkParticle
extends GenericParticle {
    public InkParticle(Vector3 vector3) {
        this(vector3, 0);
    }

    public InkParticle(Vector3 vector3, int n) {
        super(vector3, 36, n);
    }
}


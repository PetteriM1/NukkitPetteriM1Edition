/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.level.particle.GenericParticle;
import cn.nukkit.math.Vector3;

public class CriticalParticle
extends GenericParticle {
    public CriticalParticle(Vector3 vector3) {
        this(vector3, 2);
    }

    public CriticalParticle(Vector3 vector3, int n) {
        super(vector3, 3, n);
    }
}


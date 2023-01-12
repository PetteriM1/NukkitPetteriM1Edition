/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.level.particle.GenericParticle;
import cn.nukkit.math.Vector3;

public class RedstoneParticle
extends GenericParticle {
    public RedstoneParticle(Vector3 vector3) {
        this(vector3, 1);
    }

    public RedstoneParticle(Vector3 vector3, int n) {
        super(vector3, 12, n);
    }
}


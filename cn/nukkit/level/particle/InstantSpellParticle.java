/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.level.particle.SpellParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;

public class InstantSpellParticle
extends SpellParticle {
    public InstantSpellParticle(Vector3 vector3) {
        this(vector3, 0);
    }

    public InstantSpellParticle(Vector3 vector3, int n) {
        super(vector3, n);
    }

    public InstantSpellParticle(Vector3 vector3, BlockColor blockColor) {
        this(vector3, blockColor.getRed(), blockColor.getGreen(), blockColor.getBlue());
    }

    public InstantSpellParticle(Vector3 vector3, int n, int n2, int n3) {
        super(vector3, n, n2, n3, 1);
    }
}


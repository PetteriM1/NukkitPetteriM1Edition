/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.level.particle.GenericParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;

public class DustParticle
extends GenericParticle {
    public DustParticle(Vector3 vector3, BlockColor blockColor) {
        this(vector3, blockColor.getRed(), blockColor.getGreen(), blockColor.getBlue(), blockColor.getAlpha());
    }

    public DustParticle(Vector3 vector3, int n, int n2, int n3) {
        this(vector3, n, n2, n3, 255);
    }

    public DustParticle(Vector3 vector3, int n, int n2, int n3, int n4) {
        super(vector3, 32, (n4 & 0xFF) << 24 | (n & 0xFF) << 16 | (n2 & 0xFF) << 8 | n3 & 0xFF);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.helper;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureBelow;

public interface EnsureGrassBelow {
    public static boolean ensureGrassBelow(int n, int n2, int n3, FullChunk fullChunk) {
        return EnsureBelow.ensureBelow(n, n2, n3, 2, fullChunk);
    }
}


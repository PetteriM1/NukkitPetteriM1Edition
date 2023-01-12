/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.helper;

import cn.nukkit.level.format.FullChunk;

public interface EnsureBelow {
    public static boolean ensureBelow(int n, int n2, int n3, int n4, FullChunk fullChunk) {
        return fullChunk.getBlockId(n, n2 - 1, n3) == n4;
    }
}


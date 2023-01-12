/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.helper;

import cn.nukkit.level.format.FullChunk;

public interface EnsureCover {
    public static boolean ensureCover(int n, int n2, int n3, FullChunk fullChunk) {
        if (n2 > 255) {
            return false;
        }
        int n4 = fullChunk.getBlockId(n, n2, n3);
        return n4 == 0 || n4 == 78;
    }

    public static boolean ensureWaterCover(int n, int n2, int n3, FullChunk fullChunk) {
        if (n2 > 255) {
            return false;
        }
        return fullChunk.getBlockId(n, n2, n3) == 9;
    }
}


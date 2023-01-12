/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.helper;

import cn.nukkit.block.BlockID;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureCover;
import cn.nukkit.level.generator.populator.helper.EnsureGrassBelow;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public final class PopulatorHelpers
implements BlockID {
    private static final IntSet a = new IntOpenHashSet();
    private static final IntSet b = new IntOpenHashSet();

    private PopulatorHelpers() {
    }

    public static boolean canGrassStay(int n, int n2, int n3, FullChunk fullChunk) {
        return EnsureCover.ensureCover(n, n2, n3, fullChunk) && EnsureGrassBelow.ensureGrassBelow(n, n2, n3, fullChunk);
    }

    public static boolean isNonSolid(int n) {
        return a.contains(n);
    }

    public static boolean isNonOceanSolid(int n) {
        return b.contains(n);
    }

    static {
        a.add(0);
        a.add(18);
        a.add(161);
        a.add(78);
        a.add(31);
        b.add(0);
        b.add(8);
        b.add(9);
        b.add(79);
        b.add(174);
        b.add(266);
    }
}


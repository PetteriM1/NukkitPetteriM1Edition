/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureBelow;
import cn.nukkit.level.generator.populator.helper.EnsureCover;
import cn.nukkit.level.generator.populator.helper.EnsureGrassBelow;
import cn.nukkit.level.generator.populator.type.PopulatorSurfaceBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;

public class PopulatorSugarcane
extends PopulatorSurfaceBlock {
    private boolean a(int n, int n2, int n3, FullChunk fullChunk) {
        int n4 = fullChunk.getBlockId(n + BlockFace.NORTH.getXOffset() & 0xF, n2, n3 + BlockFace.NORTH.getZOffset() & 0xF);
        if (n4 == 8 || n4 == 9) {
            return true;
        }
        n4 = fullChunk.getBlockId(n + BlockFace.EAST.getXOffset() & 0xF, n2, n3 + BlockFace.EAST.getZOffset() & 0xF);
        if (n4 == 8 || n4 == 9) {
            return true;
        }
        n4 = fullChunk.getBlockId(n + BlockFace.SOUTH.getXOffset() & 0xF, n2, n3 + BlockFace.SOUTH.getZOffset() & 0xF);
        if (n4 == 8 || n4 == 9) {
            return true;
        }
        n4 = fullChunk.getBlockId(n + BlockFace.WEST.getXOffset() & 0xF, n2, n3 + BlockFace.WEST.getZOffset() & 0xF);
        return n4 == 8 || n4 == 9;
    }

    @Override
    protected boolean canStay(int n, int n2, int n3, FullChunk fullChunk) {
        return EnsureCover.ensureCover(n, n2, n3, fullChunk) && (EnsureGrassBelow.ensureGrassBelow(n, n2, n3, fullChunk) || EnsureBelow.ensureBelow(n, n2, n3, 12, fullChunk)) && this.a(n, n2 - 1, n3, fullChunk);
    }

    @Override
    protected int getBlockId(int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        return 1329;
    }
}


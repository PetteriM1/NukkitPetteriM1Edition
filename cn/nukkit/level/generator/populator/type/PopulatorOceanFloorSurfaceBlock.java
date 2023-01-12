/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.type;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.PopulatorHelpers;
import cn.nukkit.level.generator.populator.type.PopulatorSurfaceBlock;

public abstract class PopulatorOceanFloorSurfaceBlock
extends PopulatorSurfaceBlock {
    @Override
    protected int getHighestWorkableBlock(ChunkManager chunkManager, int n, int n2, FullChunk fullChunk) {
        int n3;
        for (n3 = 63; n3 >= 0 && PopulatorHelpers.isNonOceanSolid(fullChunk.getBlockId(n, n3, n2)); --n3) {
        }
        return n3 == 0 ? -1 : ++n3;
    }
}


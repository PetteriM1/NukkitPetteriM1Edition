package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.object.structure.ObjectWell;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

/**
 * Created by PetteriM1
 */
public class PopulatorWell extends Populator {

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        if (random.nextBoundedInt(190) != 1) return;
            int x = NukkitMath.randomRange(random, chunkX << 4, (chunkX << 4) + 15);
            int z = NukkitMath.randomRange(random, chunkZ << 4, (chunkZ << 4) + 15);
            int y = this.getHighestWorkableBlock(chunk, x, z);
            if (y == -1 || y > 128) {
                return;
            }
            new ObjectWell(level, x, y, z);
    }

    private int getHighestWorkableBlock(FullChunk chunk, int x, int z) {
        int y;

        for (y = 127; y > 0; --y) {
            int b = chunk.getBlockId(x & 0x0f, y & 0xff, z & 0x0f);
            if (b == Block.SAND) {
                break;
            }
        }

        return ++y;
    }
}

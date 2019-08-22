package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.utils.Utils;

/**
 * @author Niall Lindsay (Niall7459)
 * <p>
 * Nukkit Project
 * </p>
 */

public class PopulatorTallSugarcane extends PopulatorSugarcane {

    @Override
    protected void placeBlock(int x, int y, int z, int id, FullChunk chunk, NukkitRandom random) {
        for (int i = 0; i < Utils.random.nextInt(3) + 1; i++)    {
            chunk.setFullBlockId(x, y + i, z, id);
        }
    }
}

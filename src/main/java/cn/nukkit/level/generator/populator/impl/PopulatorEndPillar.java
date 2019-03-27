package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.utils.EntityUtils;

public class PopulatorEndPillar extends Populator {

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        if (EntityUtils.rand(0, 99) < 10) {
            int x = random.nextRange(chunkX * 16, chunkX * 16 + 15);
            int z = random.nextRange(chunkX * 16, chunkX * 16 + 15);
            int y = this.getHighestWorkableBlock(level, x, z, chunk);

            if (level.getBlockIdAt(x, y, z) == Block.END_STONE) {
                for (int ny = y; ny < y + EntityUtils.rand(28, 50); ny++) {
                    for (double r = 0.5; r < 5; r += 0.5) {
                        double nd = 360 / (2 * Math.PI * r);
                        for (double d = 0; d < 360; d += nd) {
                            level.setBlockIdAt((int) Math.round(x + (Math.cos(Math.toRadians(d)) * r)), ny, (int) Math.round(z + (Math.sin(Math.toRadians(d)) * r)), Block.OBSIDIAN);
                            level.setBlockDataAt((int) Math.round(x + (Math.cos(Math.toRadians(d)) * r)), ny, (int) Math.round(z + (Math.sin(Math.toRadians(d)) * r)), 0);
                        }
                    }
                }
            }
        }
    }
}

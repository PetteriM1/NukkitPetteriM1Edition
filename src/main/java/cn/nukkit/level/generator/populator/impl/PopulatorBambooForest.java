package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.object.ObjectBamboo;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.level.generator.populator.type.PopulatorCount;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

/**
 * Created by Alemiz112
 */
public class PopulatorBambooForest extends Populator {

    private ChunkManager level;

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        this.level = level;
        int x = NukkitMath.randomRange(random, chunkX << 4, (chunkX << 4) + 15);
        int z = NukkitMath.randomRange(random, chunkZ << 4, (chunkZ << 4) + 15);
        int y = this.getHighestWorkableBlock(x, z);
        if (y < 3) {
            return;
        }

        if (random.nextBoundedInt(5) == 0 && !this.isBambooNear(level, x, y, z, random)) return;

        int count = random.nextRange(8, 15);
        for (int i = 0; i < count; i++){
            if (i > 0) {
                x += NukkitMath.randomRange(random, -3, 3);
                z += NukkitMath.randomRange(random, -3, 3);
                y = this.getHighestWorkableBlock(x, z);
            }
            ObjectBamboo.growBamboo(level, new Vector3(x, y, z), random);
        }
    }

    private int getHighestWorkableBlock(int x, int z) {
        int y;
        for (y = 255; y > 0; --y) {
            int b = this.level.getBlockIdAt(x, y, z);
            if (b == Block.DIRT || b == Block.GRASS) {
                break;
            } else if (b != Block.AIR && b != Block.SNOW_LAYER) {
                return -1;
            }
        }
        return ++y;
    }

    private boolean isBambooNear(ChunkManager level, int x, int y, int z, NukkitRandom random){
        return this.getNearBamboo(level, x, y, z, random) != null;
    }

    private Vector3 getNearBamboo(ChunkManager level, int x, int y, int z, NukkitRandom random){
        int checkRadius = 3;
        boolean xBased = random.nextBoolean();

        for (int yy = -checkRadius; yy < (checkRadius + 1); ++yy) {
            if (xBased){
                for (int xx = -checkRadius; xx < (checkRadius + 1); ++xx) {
                    for (int zz = -checkRadius; zz < (checkRadius + 1); ++zz) {
                        if (level.getBlockIdAt(x + xx, y + yy, z + zz) == Block.BAMBOO) {
                            new Vector3(x + xx, y + yy, z + zz);
                        }
                    }
                }
            }else {
                for (int zz = -checkRadius; zz < (checkRadius + 1); ++zz) {
                    for (int xx = -checkRadius; xx < (checkRadius + 1); ++xx) {
                        if (level.getBlockIdAt(x + xx, y + yy, z + zz) == Block.BAMBOO) {
                            new Vector3(x + xx, y + yy, z + zz);
                        }
                    }
                }
            }
        }
        return null;
    }


}

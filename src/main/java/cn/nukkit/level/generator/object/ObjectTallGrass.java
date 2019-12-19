package cn.nukkit.level.generator.object;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

/**
 * @author ItsLucas
 * Nukkit Project
 */

public class ObjectTallGrass {

    public static void growGrass(ChunkManager level, Vector3 pos, NukkitRandom random) {
        for (int i = 0; i < 128; ++i) {
            int num = 0;

            int x = pos.getFloorX();
            int y = pos.getFloorY() + 1;
            int z = pos.getFloorZ();

            while (true) {
                if (num >= i >> 4) {
                    if (level.getBlockIdAt(x, y, z) == Block.AIR) {
                        if (random.nextBoundedInt(8) == 0) {
                            if (random.nextBoolean()) {
                                level.setBlockAt(x, y, z, Block.DANDELION);
                            } else {
                                level.setBlockAt(x, y, z, Block.POPPY);
                            }
                        } else {
                            level.setBlockAt(x, y, z, Block.TALL_GRASS, 1);
                        }
                    }

                    break;
                }

                x += random.nextRange(-1, 1);
                y += random.nextRange(-1, 1) * random.nextBoundedInt(3) >> 1;
                z += random.nextRange(-1, 1);

                if (level.getBlockIdAt(x, y - 1, z) != Block.GRASS || y > 255 || y < 0) {
                    break;
                }

                ++num;
            }
        }
    }
}

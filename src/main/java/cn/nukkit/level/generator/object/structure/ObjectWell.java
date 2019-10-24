package cn.nukkit.level.generator.object.structure;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;

/**
 * Created by PetteriM1
 */
public class ObjectWell {

    public ObjectWell(ChunkManager level, int x, int y, int z) {
        if (canPlaceObject(level, x, y, z)) {
            placeObject(level, x, y, z);
        }
    }

    public boolean canPlaceObject(ChunkManager level, int x, int y, int z) {
        int radiusToCheck = 0;
        for (int yy = 0; yy < 8; ++yy) {
            if (yy == 1 || yy == 5) {
                ++radiusToCheck;
            }
            for (int xx = -radiusToCheck; xx < (radiusToCheck + 1); ++xx) {
                for (int zz = -radiusToCheck; zz < (radiusToCheck + 1); ++zz) {
                    if (level.getBlockIdAt(x + xx, y + yy, z + zz) != Block.AIR) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void placeObject(ChunkManager level, int x, int y, int z) {
        for (int yy = y - 3 + 5; yy <= y + 5; ++yy) {
            double yOff = yy - (y + 5);
            int mid = (int) (1 - yOff / 2);
            for (int xx = x - mid; xx <= x + mid; ++xx) {
                for (int zz = z - mid; zz <= z + mid; ++zz) {
                    level.setBlockAt(xx, yy - 3, zz, Block.SANDSTONE);
                }
            }
        }
        
        //slabs for all sides
        level.setBlockAt(x + 2, y, z, Block.SLAB, 1);
        level.setBlockAt(x - 2, y, z, Block.SLAB, 1);
        level.setBlockAt(x, y, z + 2, Block.SLAB, 1);
        level.setBlockAt(x, y, z - 2, Block.SLAB, 1);

        //water
        level.setBlockAt(x, y, z, Block.WATER);
        level.setBlockAt(x + 1, y, z, Block.WATER);
        level.setBlockAt(x - 1, y, z, Block.WATER);
        level.setBlockAt(x, y, z + 1, Block.WATER);
        level.setBlockAt(x, y, z - 1, Block.WATER);
        
        //air
        level.setBlockAt(x, y + 1, z, Block.AIR);
        level.setBlockAt(x + 1, y + 1, z, Block.AIR);
        level.setBlockAt(x - 1, y + 1, z, Block.AIR);
        level.setBlockAt(x, y + 1, z + 1, Block.AIR);
        level.setBlockAt(x, y + 1, z - 1, Block.AIR);
        
        //more air
        level.setBlockAt(x, y + 2, z, Block.AIR);
        level.setBlockAt(x + 1, y + 2, z, Block.AIR);
        level.setBlockAt(x - 1, y + 2, z, Block.AIR);
        level.setBlockAt(x, y + 2, z + 1, Block.AIR);
        level.setBlockAt(x, y + 2, z - 1, Block.AIR);
        
        //roof
        level.setBlockAt(x, y + 3, z, Block.SANDSTONE);
        level.setBlockAt(x + 1, y + 3, z, Block.SLAB, 1);
        level.setBlockAt(x - 1, y + 3, z, Block.SLAB, 1);
        level.setBlockAt(x, y + 3, z + 1, Block.SLAB, 1);
        level.setBlockAt(x, y + 3, z - 1, Block.SLAB, 1);
        level.setBlockAt(x + 1, y + 3, z + 1, Block.SLAB, 1);
        level.setBlockAt(x + 1, y + 3, z - 1, Block.SLAB, 1);
        level.setBlockAt(x - 1, y + 3, z - 1, Block.SLAB, 1);
        level.setBlockAt(x - 1, y + 3, z + 1, Block.SLAB, 1);
    }
}

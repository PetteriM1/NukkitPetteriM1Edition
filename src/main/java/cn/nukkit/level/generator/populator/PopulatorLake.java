package cn.nukkit.level.generator.populator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.ore.ObjectLake;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

/**
 * Created by PetteriM1
 */
public class PopulatorLake extends Populator {
    private final int replaceId;
    private final int replaceId2;
    private OreType[] oreTypes = new OreType[0];

    public PopulatorLake() {
        this(Block.GRASS, Block.DIRT);
    }
    
    public PopulatorLake(int id, int id2) {
        this.replaceId = id;
        this.replaceId2 = id2;
    }

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
        for (OreType type : this.oreTypes) {
            ObjectLake lake = new ObjectLake(random, type, replaceId, replaceId2);
            for (int i = 0; i < lake.type.clusterCount; ++i) {
                int x = NukkitMath.randomRange(random, chunkX << 4, (chunkX << 4) + 15);
                int y = NukkitMath.randomRange(random, lake.type.minHeight, lake.type.maxHeight);
                int z = NukkitMath.randomRange(random, chunkZ << 4, (chunkZ << 4) + 15);
                if (lake.canPlaceObject(level, x, y, z)) {
                    lake.placeObject(level, x, y, z);
                }
            }
        }
    }

    public void setOreTypes(OreType[] oreTypes) {
        this.oreTypes = oreTypes;
    }
}

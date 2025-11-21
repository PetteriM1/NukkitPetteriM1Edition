package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.mob.EntityEnderman;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class EndermanSpawner extends AbstractEntitySpawner {

    public EndermanSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityEnderman.NETWORK_ID;
    }

    public void spawn(Player player, Position pos, Level level) {
        boolean nether = level.getDimension() == Level.DIMENSION_NETHER;
        boolean end = level.getDimension() == Level.DIMENSION_THE_END;

        if (!level.isMobSpawningAllowedByTime() && !nether && !end) {
            return;
        }

        int biome = level.getBiomeId((int) pos.x, (int) pos.z);
        if (!end && Utils.rand(1, nether ? (biome == EnumBiome.WARPED_FOREST.id ? 4 : 10) : 10) != 1) {
            return;
        }

        if (level.getBlockLightAt((int) pos.x, (int) pos.y + 1, (int) pos.z) == 0 || nether || end) {
            if (!nether || (biome != EnumBiome.CRIMSON_FOREST.id && biome != EnumBiome.BASALT_DELTAS.id)) {
                if (end) {
                    for (int i = 0; i < Utils.rand(1, 4); i++) {
                        this.spawnTask.createEntity("Enderman", pos.add(0.5, 1, 0.5));
                    }
                } else {
                    this.spawnTask.createEntity("Enderman", pos.add(0.5, 1, 0.5));
                }
            }
        }
    }
}

package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.mob.EntitySkeleton;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class SkeletonSpawner extends AbstractEntitySpawner {

    public SkeletonSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 5) == 1) {
            return;
        }
        if (level.getBlockLightAt((int) pos.x, (int) pos.y + 1, (int) pos.z) == 0) {
            if (level.getDimension() == Level.DIMENSION_NETHER) {
                if (level.getBiomeId((int) pos.x, (int) pos.z) == EnumBiome.SOULSAND_VALLEY.id) {
                    this.spawnTask.createEntity("Skeleton", pos.add(0.5, 1, 0.5));
                }
            } else if (level.isMobSpawningAllowedByTime()) {
                this.spawnTask.createEntity("Skeleton", pos.add(0.5, 1, 0.5));
            }
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntitySkeleton.NETWORK_ID;
    }
}

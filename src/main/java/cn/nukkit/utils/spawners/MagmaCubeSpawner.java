package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.mob.EntityMagmaCube;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;

public class MagmaCubeSpawner extends AbstractEntitySpawner {

    public MagmaCubeSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityMagmaCube.NETWORK_ID;
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        int biome = level.getBiomeId((int) pos.x, (int) pos.z);
        if (biome != EnumBiome.SOULSAND_VALLEY.id && biome != EnumBiome.CRIMSON_FOREST.id && biome != EnumBiome.WARPED_FOREST.id) {
            this.spawnTask.createEntity("MagmaCube", pos.add(0.5, 1, 0.5));
        }
    }
}

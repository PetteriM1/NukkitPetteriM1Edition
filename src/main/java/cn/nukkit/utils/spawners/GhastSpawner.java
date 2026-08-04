package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.mob.EntityGhast;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class GhastSpawner extends AbstractEntitySpawner {

    public GhastSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 100) < 95) {
            return;
        }
        int biome = level.getBiomeId((int) pos.x, (int) pos.z);
        if (biome != EnumBiome.CRIMSON_FOREST.id && biome != EnumBiome.WARPED_FOREST.id) {
            this.spawnTask.createEntity("Ghast", pos.add(0.5, 1, 0.5));
        }
    }

    @Override
    public int getEntityNetworkId() {
        return EntityGhast.NETWORK_ID;
    }
}

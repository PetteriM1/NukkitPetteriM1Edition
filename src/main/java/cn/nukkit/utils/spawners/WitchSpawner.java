package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.mob.EntityWitch;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class WitchSpawner extends AbstractEntitySpawner {

    public WitchSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityWitch.NETWORK_ID;
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        if (!level.isMobSpawningAllowedByTime()) {
            return;
        }
        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
        if (Utils.rand(1, 20) != 1 && biomeId != 6 && biomeId != 134) {
            return;
        }
        if (level.getBlockLightAt((int) pos.x, (int) pos.y + 1, (int) pos.z) == 0) {
            this.spawnTask.createEntity("Witch", pos.add(0.5, 1, 0.5));
        }
    }
}

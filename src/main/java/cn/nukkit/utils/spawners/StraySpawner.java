package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.mob.EntityStray;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;

public class StraySpawner extends AbstractEntitySpawner {

    public StraySpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityStray.NETWORK_ID;
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        if (!level.isMobSpawningAllowedByTime()) {
            return;
        }
        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
        if (biomeId == 12 || biomeId == 30 || biomeId == 140 || biomeId == 10 || biomeId == 46 || biomeId == 47) {
            if (level.getBlockLightAt((int) pos.x, (int) pos.y + 1, (int) pos.z) == 0 && level.canBlockSeeSky(new Vector3((int) pos.x, (int) pos.y + 1, (int) pos.z))) {
                this.spawnTask.createEntity("Stray", pos.add(0.5, 1, 0.5));
            }
        }
    }
}

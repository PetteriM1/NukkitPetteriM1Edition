package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.mob.EntityHusk;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class HuskSpawner extends AbstractEntitySpawner {

    public HuskSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityHusk.NETWORK_ID;
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        if (!level.isMobSpawningAllowedByTime()) {
            return;
        }
        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
        if (biomeId == 2 || biomeId == 130) {
            if (level.getBlockLightAt((int) pos.x, (int) pos.y + 1, (int) pos.z) == 0 && level.canBlockSeeSky(new Vector3((int) pos.x, (int) pos.y + 1, (int) pos.z))) {
                BaseEntity entity = this.spawnTask.createEntity("Husk", pos.add(0.5, 1, 0.5));
                if (entity == null) return;
                if (Utils.rand(1, 20) == 1) {
                    entity.setBaby(true);
                }
            }
        }
    }
}

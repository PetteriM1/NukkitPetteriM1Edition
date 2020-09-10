package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.mob.EntityZombie;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.Utils;
import cn.nukkit.utils.SpawnerTask;

public class ZombieSpawner extends AbstractEntitySpawner {

    public ZombieSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);

        if (pos.y > 255 || pos.y < 1 || biomeId == 14 || biomeId == 15) {
        } else if (level.getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z) > 7) {
        } else if (Block.transparent[level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z)]) {
        } else if (level.isMobSpawningAllowedByTime()) {
            if (Utils.rand(1, 40) == 30) {
                BaseEntity entity = this.spawnTask.createEntity("ZombieVillager", pos.add(0, 1, 0));
                if (Utils.rand(1, 20) == 1) {
                    entity.setBaby(true);
                }
            } else {
                BaseEntity entity = this.spawnTask.createEntity("Zombie", pos.add(0, 1, 0));
                if (Utils.rand(1, 20) == 1) {
                    entity.setBaby(true);
                }
            }
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityZombie.NETWORK_ID;
    }
}

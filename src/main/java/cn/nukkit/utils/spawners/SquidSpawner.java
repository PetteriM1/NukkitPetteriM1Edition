package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.passive.EntitySquid;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class SquidSpawner extends AbstractEntitySpawner {

    public SquidSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 10) != 1) {
            return;
        }
        if (!level.isAnimalSpawningAllowedByTime()) {
            return;
        }
        final int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        if (blockId == Block.WATER || blockId == Block.STILL_WATER) {
            final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
            if (biomeId == 0 || biomeId == 10 || biomeId == 24 || (biomeId >= 40 && biomeId <= 47)) {
                final int b = level.getBlockIdAt((int) pos.x, (int) (pos.y - 1), (int) pos.z);
                if (b == Block.WATER || b == Block.STILL_WATER) {
                    for (int i = 0; i < Utils.rand(2, 4); i++) {
                        BaseEntity entity = this.spawnTask.createEntity("Squid", pos.add(0, -1, 0));
                        if (entity == null) return;
                        if (Utils.rand(1, 20) == 1) {
                            entity.setBaby(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntitySquid.NETWORK_ID;
    }

    @Override
    public boolean isWaterMob() {
        return true;
    }
}

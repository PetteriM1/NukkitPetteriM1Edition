package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.passive.EntityWolf;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class WolfSpawner extends AbstractEntitySpawner {

    public WolfSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityWolf.NETWORK_ID;
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 10) != 1) {
            return;
        }
        if (!level.isAnimalSpawningAllowedByTime()) {
            return;
        }
        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
        if (biomeId == 4 || biomeId == 5 || biomeId == 20 || biomeId == 27 || biomeId == 30 || biomeId == 32 || biomeId == 133 || biomeId == 158) {
            int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
            if (blockId == Block.GRASS || blockId == Block.SNOW_LAYER) {
                for (int i = 0; i < 4; i++) {
                    BaseEntity entity = this.spawnTask.createEntity("Wolf", pos.add(0.5, 1, 0.5));
                    if (entity == null) return;
                    if (Utils.rand(1, 10) == 1) {
                        entity.setBaby(true);
                    }
                }
            }
        }
    }
}

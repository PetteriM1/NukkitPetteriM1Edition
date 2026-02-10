package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.passive.EntityRabbit;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class RabbitSpawner extends AbstractEntitySpawner {

    public RabbitSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityRabbit.NETWORK_ID;
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 10) != 1) {
            return;
        }
        if (!level.isAnimalSpawningAllowedByTime()) {
            return;
        }
        int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        if (blockId == Block.GRASS || blockId == Block.SNOW_LAYER || blockId == Block.SAND) {
            final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
            if (biomeId == 2 || biomeId == 130 || biomeId == 30 || biomeId == 5 || biomeId == 12 || biomeId == 26 || biomeId == 11) {
                for (int i = 0; i < Utils.rand(1, 3); i++) {
                    this.spawnTask.createEntity("Rabbit", pos.add(0.5, 1, 0.5));
                }
            }
        }
    }
}

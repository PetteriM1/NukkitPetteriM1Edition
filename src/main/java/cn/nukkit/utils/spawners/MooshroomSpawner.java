package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.passive.EntityMooshroom;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class MooshroomSpawner extends AbstractEntitySpawner {

    public MooshroomSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityMooshroom.NETWORK_ID;
    }

    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 10) != 1) {
            return;
        }
        if (!level.isAnimalSpawningAllowedByTime()) {
            return;
        }
        int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
        if (biomeId == 14 || biomeId == 15) {
            if (level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.MYCELIUM) {
                for (int i = 0; i < Utils.rand(4, 8); i++) {
                    BaseEntity entity = this.spawnTask.createEntity("Mooshroom", pos.add(0.5, 1, 0.5));
                    if (entity == null) return;
                    if (Utils.rand(1, 20) == 1) {
                        entity.setBaby(true);
                    }
                }
            }
        }
    }
}

package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.passive.EntityMooshroom;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.Utils;
import cn.nukkit.utils.SpawnerTask;

public class MooshroomSpawner extends AbstractEntitySpawner {

    public MooshroomSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(0, 3) == 1) {
            return;
        }

        int bid = level.getBiomeId((int) pos.x, (int) pos.z);
        if (bid != 14 && bid != 15) {
        } else if (level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) != Block.MYCELIUM) {
        } else if (pos.y > 255 || pos.y < 1) {
        } else if (level.isAnimalSpawningAllowedByTime()) {
            BaseEntity entity = this.spawnTask.createEntity("Mooshroom", pos.add(0, 1, 0));
            if (entity == null) return;
            if (Utils.rand(1, 20) == 1) {
                entity.setBaby(true);
            }
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityMooshroom.NETWORK_ID;
    }
}

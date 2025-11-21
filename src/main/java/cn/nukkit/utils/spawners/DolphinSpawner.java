package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.passive.EntityDolphin;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class DolphinSpawner extends AbstractEntitySpawner {

    public DolphinSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityDolphin.NETWORK_ID;
    }

    @Override
    public boolean isWaterMob() {
        return true;
    }

    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 10) != 1) {
            return;
        }
        if (!level.isAnimalSpawningAllowedByTime()) {
            return;
        }
        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
        final int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        if ((blockId == Block.WATER || blockId == Block.STILL_WATER) && (biomeId == 0 || biomeId == 24)) {
            final int b = level.getBlockIdAt((int) pos.x, (int) (pos.y - 1), (int) pos.z);
            if (b == Block.WATER || b == Block.STILL_WATER) {
                for (int i = 0; i < Utils.rand(1, 3); i++) {
                    BaseEntity entity = this.spawnTask.createEntity("Dolphin", pos.add(0, -1, 0));
                    if (entity == null) return;
                    if (Utils.rand(1, 10) == 1) {
                        entity.setBaby(true);
                    }
                }
            }
        }
    }
}

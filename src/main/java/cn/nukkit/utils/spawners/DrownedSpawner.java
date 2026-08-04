package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityDrowned;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class DrownedSpawner extends AbstractEntitySpawner {

    public DrownedSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 15) != 1) {
            return;
        }
        if (!level.isMobSpawningAllowedByTime()) {
            return;
        }
        final int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        if (blockId == Block.WATER || blockId == Block.STILL_WATER) {
            final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
            if (biomeId == 0 || biomeId == 7 || biomeId == 10 || biomeId == 11 || biomeId == 24 || (biomeId >= 40 && biomeId <= 47)) {
                if (level.getBlockLightAt((int) pos.x, (int) pos.y + 1, (int) pos.z) == 0) {
                    final int b = level.getBlockIdAt((int) pos.x, (int) (pos.y - 1), (int) pos.z);
                    if (b == Block.WATER || b == Block.STILL_WATER) {
                        this.spawnTask.createEntity("Drowned", pos.add(0.5, -1, 0.5));
                    }
                }
            }
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityDrowned.NETWORK_ID;
    }

    @Override
    public boolean isWaterMob() {
        return true;
    }
}

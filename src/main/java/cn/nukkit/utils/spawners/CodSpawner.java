package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.passive.EntityCod;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class CodSpawner extends AbstractEntitySpawner {

    public CodSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityCod.NETWORK_ID;
    }

    @Override
    public boolean isWaterMob() {
        return true;
    }

    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 4) == 1) {
            return;
        }
        final int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        if (blockId == Block.WATER || blockId == Block.STILL_WATER) {
            final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
            if (biomeId == 0 || biomeId == 24 || (biomeId >= 42 && biomeId <= 45)) {
                final int b = level.getBlockIdAt((int) pos.x, (int) (pos.y - 1), (int) pos.z);
                if (b == Block.WATER || b == Block.STILL_WATER) {
                    for (int i = 0; i < Utils.rand(4, 7); i++) {
                        this.spawnTask.createEntity("Cod", pos.add(0, -1, 0));
                    }
                }
            }
        }
    }
}

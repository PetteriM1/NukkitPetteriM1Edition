package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityEnderman;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.Utils;
import cn.nukkit.utils.SpawnerTask;

public class EndermanSpawner extends AbstractEntitySpawner {

    public EndermanSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    public void spawn(Player player, Position pos, Level level) {
        boolean nether = level.getDimension() == Level.DIMENSION_NETHER;

        if (Utils.rand(1, nether ? 10 : 7) != 1 && !level.isEnd) {
            return;
        }

        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);

        if ((pos.y > 255 || (nether && pos.y > 127)) || pos.y < 1 || biomeId == 14 || biomeId == 15) {
        } else if (Block.transparent[level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z)]) {
        } else if (level.getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z) > 7 && !nether && !level.isEnd) {
        } else if (level.isMobSpawningAllowedByTime() || nether || level.isEnd) {
            this.spawnTask.createEntity("Enderman", pos.add(0, 1, 0));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityEnderman.NETWORK_ID;
    }
}

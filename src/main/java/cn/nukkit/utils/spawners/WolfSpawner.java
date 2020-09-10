package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityWolf;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;

public class WolfSpawner extends AbstractEntitySpawner {

    public WolfSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);

        if (biomeId != 4 && biomeId != 5 && biomeId != 20 && biomeId != 27 && biomeId != 30 && biomeId != 32 && biomeId != 133 && biomeId != 158) {
        } else if (pos.y > 255 || pos.y < 1) {
        } else if (Block.transparent[level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z)]) {
        } else if (level.isAnimalSpawningAllowedByTime()) {
            this.spawnTask.createEntity("Wolf", pos.add(0, 1, 0));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityWolf.NETWORK_ID;
    }
}

package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.passive.EntityRabbit;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.Spawner;

public class RabbitSpawner extends AbstractEntitySpawner {

    public RabbitSpawner(Spawner spawnTask) {
        super(spawnTask);
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);

        if (level.isNether || level.isEnd) {
        } else if (biomeId != 2 && biomeId != 130 && biomeId != 30 && biomeId != 5 && biomeId != 12) {
        } else if (pos.y > 255 || pos.y < 1) {
        } else if (Block.transparent[level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z)]) {
        } else if (level.isAnimalSpawningAllowedByTime()) {
            this.spawnTask.createEntity("Rabbit", pos.add(0, 1, 0));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityRabbit.NETWORK_ID;
    }
}

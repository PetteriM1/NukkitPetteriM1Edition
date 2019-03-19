package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.passive.EntityRabbit;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnResult;
import cn.nukkit.utils.Spawner;

public class RabbitSpawner extends AbstractEntitySpawner {

    public RabbitSpawner(Spawner spawnTask) {
        super(spawnTask);
    }

    @Override
    public SpawnResult spawn(Player player, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        final int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);

        if (level.getName().equals("nether") || level.getName().equals("end")) {
            result = SpawnResult.WRONG_BIOME;
        } else if (biomeId != 2 && biomeId != 130 && biomeId != 30 && biomeId != 5 && biomeId != 12) {
            result = SpawnResult.WRONG_BIOME;
        } else if ((pos.y > 255 || (level.getName().equals("nether") && pos.y > 127)) || pos.y < 1 || blockId == Block.AIR) {
            result = SpawnResult.POSITION_MISMATCH;
        } else if (Block.transparent[blockId]) {
            result = SpawnResult.WRONG_BLOCK;
        } else {
            this.spawnTask.createEntity("Rabbit", pos.add(0, 1, 0));
        }

        return result;
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityRabbit.NETWORK_ID;
    }
}

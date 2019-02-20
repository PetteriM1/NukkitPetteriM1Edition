package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.mob.EntityHusk;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.EntityUtils;
import cn.nukkit.utils.SpawnResult;
import cn.nukkit.utils.Spawner;

public class HuskSpawner extends AbstractEntitySpawner {

    public HuskSpawner(Spawner spawnTask) {
        super(spawnTask);
    }

    @Override
    public SpawnResult spawn(Player player, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        final int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
        final int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        final int time = level.getTime() % Level.TIME_FULL;

        if (level.getName().equals("nether") || level.getName().equals("end")) {
            result = SpawnResult.WRONG_BIOME;
        } else if (biomeId != 2 && biomeId != 130) {
            result = SpawnResult.WRONG_BIOME;
        } else if (pos.y > 127 || pos.y < 1 || blockId == Block.AIR) {
            result = SpawnResult.POSITION_MISMATCH;
        } else if (blockId != Block.SAND) {
            result = SpawnResult.WRONG_BLOCK;
        } else if (level.getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z) > 7) {
            result = SpawnResult.WRONG_LIGHTLEVEL;
        } else if (time > 13184 && time < 22800) {
            BaseEntity entity = this.spawnTask.createEntity("Husk", pos.add(0, 1, 0));
            if (EntityUtils.rand(0, 500) > 480) {
                entity.setBaby(true);
            }
        }

        return result;
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityHusk.NETWORK_ID;
    }
}

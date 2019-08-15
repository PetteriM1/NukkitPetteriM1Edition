package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityEnderman;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.Utils;
import cn.nukkit.utils.Spawner;
import cn.nukkit.utils.SpawnResult;

public class EndermanSpawner extends AbstractEntitySpawner {

    public EndermanSpawner(Spawner spawnTask) {
        super(spawnTask);
    }

    public SpawnResult spawn(Player player, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        if (Utils.rand(1, 4) != 1 && !level.isEnd) {
            return SpawnResult.SPAWN_DENIED;
        }

        if ((pos.y > 255 || (level.isNether && pos.y > 127)) || pos.y < 1) {
            result = SpawnResult.POSITION_MISMATCH;
        } else if (Block.transparent[level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z)]) {
            result = SpawnResult.WRONG_BLOCK;
        } else if (level.getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z) > 7 && !level.isNether && !level.isEnd) {
            result = SpawnResult.WRONG_LIGHTLEVEL;
        } else if (level.isMobSpawningAllowedByTime() || level.isNether || level.isEnd) {
            this.spawnTask.createEntity("Enderman", pos.add(0, 1, 0));
        }

        return result;
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityEnderman.NETWORK_ID;
    }
}

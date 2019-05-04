package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.mob.EntityBlaze;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnResult;
import cn.nukkit.utils.Spawner;

public class BlazeSpawner extends AbstractEntitySpawner {

    public BlazeSpawner(Spawner spawnTask) {
        super(spawnTask);
    }

    @Override
    public SpawnResult spawn(Player player, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        if (!level.getName().equals("nether")) {
            result = SpawnResult.WRONG_BIOME;
        } else if (pos.y > 127 || pos.y < 1) {
            result = SpawnResult.POSITION_MISMATCH;
        } else {
            this.spawnTask.createEntity("Blaze", pos);
        }

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return EntityBlaze.NETWORK_ID;
    }
}

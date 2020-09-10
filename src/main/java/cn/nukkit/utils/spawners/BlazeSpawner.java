package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.mob.EntityBlaze;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class BlazeSpawner extends AbstractEntitySpawner {

    public BlazeSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 3) == 1) {
            return;
        }

        if (!(pos.y > 127) && !(pos.y < 1)) {
            this.spawnTask.createEntity("Blaze", pos);
        }
    }

    @Override
    public int getEntityNetworkId() {
        return EntityBlaze.NETWORK_ID;
    }
}

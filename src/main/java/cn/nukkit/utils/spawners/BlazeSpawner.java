package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.mob.EntityBlaze;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.Spawner;

public class BlazeSpawner extends AbstractEntitySpawner {

    public BlazeSpawner(Spawner spawnTask) {
        super(spawnTask);
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        if (level.isNether && (!(pos.y > 127) && !(pos.y < 1))) {
            this.spawnTask.createEntity("Blaze", pos);
        }
    }

    @Override
    public int getEntityNetworkId() {
        return EntityBlaze.NETWORK_ID;
    }
}

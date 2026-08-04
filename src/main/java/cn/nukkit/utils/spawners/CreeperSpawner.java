package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;

public class CreeperSpawner extends AbstractEntitySpawner {

    public CreeperSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    public void spawn(Player player, Position pos, Level level) {
        if (level.isMobSpawningAllowedByTime() && level.getBlockLightAt((int) pos.x, (int) pos.y + 1, (int) pos.z) == 0) {
            this.spawnTask.createEntity("Creeper", pos.add(0.5, 1, 0.5));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityCreeper.NETWORK_ID;
    }
}

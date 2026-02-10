package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.mob.EntitySpider;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;

public class SpiderSpawner extends AbstractEntitySpawner {

    public SpiderSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public final int getEntityNetworkId() {
        return EntitySpider.NETWORK_ID;
    }

    public void spawn(Player player, Position pos, Level level) {
        if (level.isMobSpawningAllowedByTime() && level.getBlockLightAt((int) pos.x, (int) pos.y + 1, (int) pos.z) == 0) {
            this.spawnTask.createEntity("Spider", pos.add(0.5, 1, 0.5));
        }
    }
}

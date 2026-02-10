package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.passive.EntityStrider;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class StriderSpawner extends AbstractEntitySpawner {

    public StriderSpawner(SpawnerTask spawnTask) {
        super(spawnTask);
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityStrider.NETWORK_ID;
    }

    @Override
    public void spawn(Player player, Position pos, Level level) {
        if (Utils.rand(1, 5) != 1) {
            return;
        }
        this.spawnTask.createEntity("Strider", pos.add(0.5, 1, 0.5));
    }
}

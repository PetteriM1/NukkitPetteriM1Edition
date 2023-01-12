/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class TurtleSpawner
extends AbstractEntitySpawner {
    public TurtleSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        int n;
        if (Utils.rand(1, 3) == 1) {
            return;
        }
        int n2 = level.getBlockIdAt((int)position.x, (int)position.y, (int)position.z);
        if (!(n2 != 8 && n2 != 9 || level.getBiomeId((int)position.x, (int)position.z) != 0 || !level.isAnimalSpawningAllowedByTime() || (n = level.getBlockIdAt((int)position.x, (int)(position.y - 1.0), (int)position.z)) != 8 && n != 9)) {
            this.spawnTask.createEntity("Turtle", position.add(0.5, -1.0, 0.5));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return 74;
    }
}


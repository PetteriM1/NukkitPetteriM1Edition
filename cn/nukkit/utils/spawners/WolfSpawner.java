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

public class WolfSpawner
extends AbstractEntitySpawner {
    public WolfSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        int n;
        if (Utils.rand(1, 3) == 1) {
            return;
        }
        int n2 = level.getBiomeId((int)position.x, (int)position.z);
        if (!(!level.isAnimalSpawningAllowedByTime() || n2 != 4 && n2 != 5 && n2 != 20 && n2 != 27 && n2 != 30 && n2 != 32 && n2 != 133 && n2 != 158 || (n = level.getBlockIdAt((int)position.x, (int)position.y, (int)position.z)) != 2 && n != 78)) {
            this.spawnTask.createEntity("Wolf", position.add(0.5, 1.0, 0.5));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return 14;
    }
}


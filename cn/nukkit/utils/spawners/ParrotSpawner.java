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

public class ParrotSpawner
extends AbstractEntitySpawner {
    public ParrotSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        int n;
        if (Utils.rand(1, 3) == 1) {
            return;
        }
        int n2 = level.getBiomeId((int)position.x, (int)position.z);
        if (!(n2 != 21 && n2 != 149 && n2 != 23 && n2 != 151 || (n = level.getBlockIdAt((int)position.x, (int)position.y, (int)position.z)) != 2 && n != 18 && n != 17 || !level.isAnimalSpawningAllowedByTime())) {
            this.spawnTask.createEntity("Parrot", position.add(0.5, 1.0, 0.5));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return 30;
    }
}


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

public class EndermanSpawner
extends AbstractEntitySpawner {
    public EndermanSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        boolean bl = level.getDimension() == 1;
        boolean bl2 = level.getDimension() == 2;
        if (Utils.rand(1, bl ? 10 : 7) != 1 && !bl2) {
            return;
        }
        int n = level.getBiomeId((int)position.x, (int)position.z);
        if (n != 14 && n != 15 && (level.getBlockLightAt((int)position.x, (int)position.y, (int)position.z) <= 7 || bl || bl2) && (level.isMobSpawningAllowedByTime() || bl || bl2)) {
            this.spawnTask.createEntity("Enderman", position.add(0.5, 1.0, 0.5));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return 38;
    }
}


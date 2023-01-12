/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;

public class CodSpawner
extends AbstractEntitySpawner {
    public CodSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        int n;
        int n2;
        int n3 = level.getBlockIdAt((int)position.x, (int)position.y, (int)position.z);
        if (!(n3 != 8 && n3 != 9 || (n2 = level.getBiomeId((int)position.x, (int)position.z)) != 0 && n2 != 7 || (n = level.getBlockIdAt((int)position.x, (int)(position.y - 1.0), (int)position.z)) != 8 && n != 9)) {
            this.spawnTask.createEntity("Cod", position.add(0.5, -1.0, 0.5));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return 112;
    }
}


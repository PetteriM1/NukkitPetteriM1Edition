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

public class PufferfishSpawner
extends AbstractEntitySpawner {
    public PufferfishSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        int n;
        int n2;
        if (Utils.rand(1, 3) == 1) {
            return;
        }
        int n3 = level.getBlockIdAt((int)position.x, (int)position.y, (int)position.z);
        if (!(n3 != 8 && n3 != 9 || (n2 = level.getBiomeId((int)position.x, (int)position.z)) != 0 && n2 != 7 || (n = level.getBlockIdAt((int)position.x, (int)(position.y - 1.0), (int)position.z)) != 8 && n != 9)) {
            this.spawnTask.createEntity("Pufferfish", position.add(0.5, -1.0, 0.5));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return 108;
    }
}


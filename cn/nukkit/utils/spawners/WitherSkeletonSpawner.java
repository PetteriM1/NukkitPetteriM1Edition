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

public class WitherSkeletonSpawner
extends AbstractEntitySpawner {
    public WitherSkeletonSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        if (Utils.rand(1, 3) == 1) {
            return;
        }
        if (level.getBlockIdAt((int)position.x, (int)position.y, (int)position.z) == 112) {
            this.spawnTask.createEntity("WitherSkeleton", position.add(0.5, 1.0, 0.5));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return 48;
    }
}


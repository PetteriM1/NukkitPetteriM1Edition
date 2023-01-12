/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.BlockWater;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class DolphinSpawner
extends AbstractEntitySpawner {
    public DolphinSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        if (Utils.rand(1, 3) == 1) {
            return;
        }
        int n = level.getBiomeId((int)position.x, (int)position.z);
        int n2 = level.getBlockIdAt((int)position.x, (int)position.y, (int)position.z);
        if ((n2 == 8 || n2 == 9) && n == 0 && level.isAnimalSpawningAllowedByTime() && level.getBlock(position.add(0.5, -1.0, 0.5)) instanceof BlockWater) {
            this.spawnTask.createEntity("Dolphin", position.add(0.5, -1.0, 0.5));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return 31;
    }
}


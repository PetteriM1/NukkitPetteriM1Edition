/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;

public class ZombieSpawner
extends AbstractEntitySpawner {
    public ZombieSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        int n = level.getBiomeId((int)position.x, (int)position.z);
        if (n != 14 && n != 15 && level.getBlockLightAt((int)position.x, (int)position.y, (int)position.z) <= 7 && level.isMobSpawningAllowedByTime()) {
            if (Utils.rand(1, 40) == 30) {
                BaseEntity baseEntity = this.spawnTask.createEntity("ZombieVillager", position.add(0.5, 1.0, 0.5));
                if (baseEntity == null) {
                    return;
                }
                if (Utils.rand(1, 20) == 1) {
                    baseEntity.setBaby(true);
                }
            } else {
                BaseEntity baseEntity = this.spawnTask.createEntity("Zombie", position.add(0.5, 1.0, 0.5));
                if (baseEntity == null) {
                    return;
                }
                if (Utils.rand(1, 20) == 1) {
                    baseEntity.setBaby(true);
                }
            }
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return 32;
    }
}


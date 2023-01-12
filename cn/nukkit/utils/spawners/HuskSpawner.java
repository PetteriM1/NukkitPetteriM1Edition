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

public class HuskSpawner
extends AbstractEntitySpawner {
    public HuskSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        int n = level.getBiomeId((int)position.x, (int)position.z);
        if ((n == 2 || n == 130) && level.getBlockLightAt((int)position.x, (int)position.y, (int)position.z) <= 7 && level.isMobSpawningAllowedByTime()) {
            BaseEntity baseEntity = this.spawnTask.createEntity("Husk", position.add(0.5, 1.0, 0.5));
            if (baseEntity == null) {
                return;
            }
            if (Utils.rand(1, 20) == 1) {
                baseEntity.setBaby(true);
            }
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return 47;
    }
}


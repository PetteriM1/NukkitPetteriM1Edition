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

public class PiglinSpawner
extends AbstractEntitySpawner {
    public PiglinSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        if (level.getBlockIdAt((int)position.x, (int)position.y, (int)position.z) == 87 && level.getBlockLightAt((int)position.x, (int)position.y, (int)position.z) < 8) {
            for (int k = 0; k < 4; ++k) {
                BaseEntity baseEntity = this.spawnTask.createEntity("Piglin", position.add(0.5, 1.0, 0.5));
                if (baseEntity == null) {
                    return;
                }
                if (Utils.rand(1, 20) != 1) continue;
                baseEntity.setBaby(true);
            }
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return 123;
    }
}


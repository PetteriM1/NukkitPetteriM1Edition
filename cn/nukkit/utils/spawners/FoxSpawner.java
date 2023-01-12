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

public class FoxSpawner
extends AbstractEntitySpawner {
    public FoxSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        int n;
        if (Utils.rand(1, 3) == 1) {
            return;
        }
        int n2 = level.getBlockIdAt((int)position.x, (int)position.y, (int)position.z);
        if (!(n2 != 2 && n2 != 78 || (n = level.getBiomeId((int)position.x, (int)position.z)) != 5 && n != 160 && n != 31 && n != 19 && n != 30 && n != 133 && n != 158 && n != 32 && n != 33 || !level.isAnimalSpawningAllowedByTime())) {
            int n3 = Utils.rand(2, 4);
            for (int k = 0; k < n3; ++k) {
                BaseEntity baseEntity = this.spawnTask.createEntity("Fox", position.add(0.5, 1.0, 0.5));
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
        return 121;
    }
}


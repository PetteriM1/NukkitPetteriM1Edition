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

public class MagmaCubeSpawner
extends AbstractEntitySpawner {
    public MagmaCubeSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        if (Utils.rand(1, 3) != 1) {
            this.spawnTask.createEntity("MagmaCube", position.add(0.5, 1.0, 0.5));
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return 42;
    }
}


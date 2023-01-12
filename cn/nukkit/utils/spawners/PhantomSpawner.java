/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.entity.mob.EntityPhantom;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.SpawnerTask;

public class PhantomSpawner
extends AbstractEntitySpawner {
    public PhantomSpawner(SpawnerTask spawnerTask) {
        super(spawnerTask);
    }

    @Override
    public void spawn(Player player, Position position, Level level) {
        EntityPhantom entityPhantom;
        int n;
        if (position.y < 130.0 && level.isMobSpawningAllowedByTime() && (n = level.getBiomeId((int)position.x, (int)position.z)) != 14 && n != 15 && level.getBlockIdAt((int)position.x, (int)position.y, (int)position.z) == 0 && level.getBlockLightAt((int)position.x, (int)position.y, (int)position.z) < 8 && (entityPhantom = (EntityPhantom)this.spawnTask.createEntity("Phantom", position)) != null) {
            entityPhantom.setTarget(player);
        }
    }

    @Override
    public final int getEntityNetworkId() {
        return 58;
    }
}


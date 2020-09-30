package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.mob.EntityPhantom;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;


public abstract class AbstractEntitySpawner implements EntitySpawner {

    protected SpawnerTask spawnTask;

    public AbstractEntitySpawner(SpawnerTask spawnTask) {
        this.spawnTask = spawnTask;
    }

    @Override
    public void spawn() {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            if (isSpawningAllowed(player)) {
                spawnTo(player);
            }
        }
    }

    private void spawnTo(Player player) {
        Level level = player.getLevel();

        if (SpawnerTask.entitySpawnAllowed(level, getEntityNetworkId(), player)) {
            Position pos = player.getPosition();

            if (getEntityNetworkId() == EntityPhantom.NETWORK_ID) {
                if (!level.isInSpawnRadius(pos)) { // Do not spawn mobs in the world spawn area
                    pos.x = pos.x + Utils.rand(-2, 2);
                    pos.y = pos.y + Utils.rand(20, 34);
                    pos.z = pos.z + Utils.rand(-2, 2);
                    spawn(player, pos, level);
                }
            } else if (pos != null) {
                pos.x += SpawnerTask.getRandomSafeXZCoord(Utils.rand(48, 52), Utils.rand(24, 28), Utils.rand(4, 8));
                pos.z += SpawnerTask.getRandomSafeXZCoord(Utils.rand(48, 52), Utils.rand(24, 28), Utils.rand(4, 8));

                if (!level.isChunkLoaded((int) pos.x >> 4, (int) pos.z >> 4) || !level.isChunkGenerated((int) pos.x >> 4, (int) pos.z >> 4)) {
                    return;
                }

                pos.y = SpawnerTask.getSafeYCoord(level, pos);

                if (!level.isInSpawnRadius(pos)) { // Do not spawn mobs in the world spawn area
                    try {
                        spawn(player, pos, level);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    private boolean isSpawningAllowed(Player player) {
        if (!player.getLevel().isMobSpawningAllowed() || Utils.rand(1, 4) == 1) {
            return false;
        }
        if (Server.getInstance().getDifficulty() == 0) {
            return !Utils.monstersList.contains(getEntityNetworkId());
        }
        return true;
    }
}

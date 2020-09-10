package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
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
            if (player.getLevel().isSpawningAllowed()) {
                if (isSpawnAllowedByDifficulty()) {
                    spawnTo(player);
                }
            }
        }
    }

    private void spawnTo(Player player) {
        Level level = player.getLevel();

        if (SpawnerTask.entitySpawnAllowed(level, getEntityNetworkId(), player)) {
            Position pos = player.getPosition();

            if (pos != null) {
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

    private static boolean isSpawnAllowedByDifficulty() {
        int randomNumber = Utils.rand(0, 3);

        switch (Server.getInstance().getDifficulty()) {
            case 0:
                return randomNumber == 0;
            case 1:
                return randomNumber <= 1;
            case 2:
                return randomNumber <= 2;
            default:
                return true;
        }
    }
}

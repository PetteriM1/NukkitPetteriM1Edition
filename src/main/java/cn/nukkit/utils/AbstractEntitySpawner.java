package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;


public abstract class AbstractEntitySpawner implements EntitySpawner {

    protected Spawner spawnTask;

    public AbstractEntitySpawner(Spawner spawnTask) {
        this.spawnTask = spawnTask;
    }

    @Override
    public void spawn() {
        if (isSpawnAllowedByDifficulty()) {
            SpawnResult lastSpawnResult;
            for (Player player : Server.getInstance().getOnlinePlayers().values()) {
                if (player.getLevel().isSpawningAllowed()) {
                    lastSpawnResult = spawn(player);
                    if (lastSpawnResult.equals(SpawnResult.MAX_SPAWN_REACHED)) {
                        break;
                    }
                }
            }
        }
    }

    protected SpawnResult spawn(Player player) {
        Position pos = player.getPosition();
        Level level = player.getLevel();

        if (Spawner.entitySpawnAllowed(level, getEntityNetworkId(), player)) {
            if (pos != null) {
                pos.x += Spawner.getRandomSafeXZCoord(Utils.rand(48, 52), Utils.rand(24, 28), Utils.rand(4, 8));
                pos.z += Spawner.getRandomSafeXZCoord(Utils.rand(48, 52), Utils.rand(24, 28), Utils.rand(4, 8));
                if (!level.isChunkLoaded((int) pos.x >> 4, (int) pos.z >> 4) || !level.isChunkGenerated((int) pos.x >> 4, (int) pos.z >> 4)) return SpawnResult.ERROR;
                pos.y = Spawner.getSafeYCoord(level, pos, 3);
            } else {
                return SpawnResult.POSITION_MISMATCH;
            }
        } else {
            return SpawnResult.MAX_SPAWN_REACHED;
        }

		try {
        	return spawn(player, pos, level);
		} catch (Exception e) {
			return SpawnResult.ERROR;
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

package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

public abstract class AbstractEntitySpawner implements EntitySpawner {

    protected Spawner spawnTask;

    protected List<String> disabledSpawnWorlds = new ArrayList<>();

    public AbstractEntitySpawner(Spawner spawnTask) {
        this.spawnTask = spawnTask;
        String disabledWorlds = Server.getInstance().getPropertyString("worlds-entity-spawning-disabled");
        if (!disabledWorlds.trim().isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(disabledWorlds, ", ");
            while (tokenizer.hasMoreTokens()) {
                disabledSpawnWorlds.add(tokenizer.nextToken());
            }
        }
    }

    @Override
    public void spawn(Collection<Player> onlinePlayers) {
        if (isSpawnAllowedByDifficulty()) {
            SpawnResult lastSpawnResult;
            for (Player player : onlinePlayers) {
                if (isWorldSpawnAllowed (player.getLevel())) {
                    lastSpawnResult = spawn(player);
                    if (lastSpawnResult.equals(SpawnResult.MAX_SPAWN_REACHED)) {
                        break;
                    }
                }
            }
        }
    }

    protected boolean isWorldSpawnAllowed(Level level) {
        for (String name : this.disabledSpawnWorlds) {
            if (level.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }
        return true;
    }

    protected SpawnResult spawn(Player player) {
        Position pos = player.getPosition();
        Level level = player.getLevel();

        if (this.spawnTask.entitySpawnAllowed(level, getEntityNetworkId())) {
            if (pos != null) {
                pos.x += this.spawnTask.getRandomSafeXZCoord(50, 26, 6);
                pos.z += this.spawnTask.getRandomSafeXZCoord(50, 26, 6);
                if (!level.isChunkLoaded((int) pos.x >> 4, (int) pos.z >> 4) || !level.isChunkGenerated((int) pos.x >> 4, (int) pos.z >> 4)) return SpawnResult.ERROR;
                pos.y = this.spawnTask.getSafeYCoord(level, pos, 3);
            }

            if (pos == null) {
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

    protected boolean isSpawnAllowedByDifficulty() {
        int randomNumber = EntityUtils.rand(0, 4);

        switch (Server.getInstance().getDifficulty()) {
            case 0:
                return randomNumber == 0;
            case 1:
                return randomNumber <= 1;
            case 2:
                return randomNumber <= 2;
            case 3:
                return true;
            default:
                return true;
        }
    }
}

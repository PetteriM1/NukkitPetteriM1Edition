package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

public class SpawnTask extends Thread {

    private static final int MAX_RADIUS = 10;
    private static final int MIN_RADIUS = 3;

    @Override
    public void run() {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            getSpawnPosition(new Position(player.x, player.y, player.z,player.getLevel()));
        }
    }

    private Position getSpawnPosition(Position startSpawnPosition) {
        int spawnX = (int) startSpawnPosition.x;
        int spawnZ = (int) startSpawnPosition.z;
        Position spawnPosition = null;

        int minSpawnX1 = spawnX - MIN_RADIUS;
        int minSpawnX2 = spawnX + MIN_RADIUS;
        int maxSpawnX1 = spawnX - MAX_RADIUS;
        int maxSpawnX2 = spawnX + MAX_RADIUS;

        int minSpawnZ1 = spawnZ - MIN_RADIUS;
        int minSpawnZ2 = spawnZ + MIN_RADIUS;
        int maxSpawnZ1 = spawnZ - MAX_RADIUS;
        int maxSpawnZ2 = spawnZ + MAX_RADIUS;

        boolean found = false;
        int findTries = 0;

        int x = EntityUtils.rand() ? EntityUtils.rand(minSpawnX1, maxSpawnX1) : EntityUtils.rand(minSpawnX2, maxSpawnX2);
        int z = EntityUtils.rand() ? EntityUtils.rand(minSpawnZ1, maxSpawnZ1) : EntityUtils.rand(minSpawnZ2, maxSpawnZ2);
        int y = (int) startSpawnPosition.y;

        while (!found && findTries < 5) {
            if (isEnoughAirAboveBlock(x, y, z, 2, startSpawnPosition.getLevel())) {
                found = true;
            }

            findTries ++;
        }

        if (found) {
            spawnPosition = new Position(x, y, z);
        }

        return spawnPosition;
    }

    private boolean isEnoughAirAboveBlock(int x, int y, int z, int minAirAbove, Level level) {
        if (minAirAbove > 0) {
            int maxTestY = y + minAirAbove;
            int addY = 1;
            while ((y + addY) <= maxTestY) {
                int blockId = level.getBlockIdAt(x, y + addY, z);
                if (blockId != Block.AIR) {
                    return false;
                }
                addY ++;
            }
        }
        return true;
    }
}

package cn.nukkit.utils;

import java.util.Arrays;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.level.Level;
import cn.nukkit.utils.spawners.*;

import java.util.ArrayList;
import java.util.List;

public class Spawner extends Thread {

    private final List<EntitySpawner> entitySpawners = Arrays.asList(
            new ChickenSpawner(this),
            new CowSpawner(this),
            new CreeperSpawner(this),
            new EndermanSpawner(this),
            new HorseSpawner(this),
            new HuskSpawner(this),
            new MooshroomSpawner(this),
            new OcelotSpawner(this),
            new PigSpawner(this),
            new PolarBearSpawner(this),
            new PigZombieSpawner(this),
            new RabbitSpawner(this),
            new SheepSpawner(this),
            new SkeletonSpawner(this),
            new SpiderSpawner(this),
            new StraySpawner(this),
            new ZombieSpawner(this),
            new WitchSpawner(this),
            new WitherSkeletonSpawner(this),
            new WolfSpawner(this)
    );

    @Override
    public void run() {
        List<Player> onlinePlayers = new ArrayList<>();

        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            onlinePlayers.add((Player) player);
        }

        if (onlinePlayers.size() > 0) {
            for (EntitySpawner spawner : entitySpawners) {
                spawner.spawn(onlinePlayers);
            }
        }
    }

    public boolean entitySpawnAllowed(Level level, int networkId) {
        try {
            return countEntity(level, networkId) < 1;
        } catch (Exception e) {
            return false;
        }
    }

    private int countEntity(Level level, int networkId) {
        int count = 0;
        for (Entity entity : level.getEntities()) {
            if (entity.isAlive() && entity.getNetworkId() == networkId) {
                count++;
            }
        }
        return count;
    }

    public BaseEntity createEntity(Object type, Position pos) {
        BaseEntity entity = (BaseEntity) EntityUtils.create(type, pos);
        if (entity != null) {
            entity.spawnToAll();
        }
        return entity;
    }

    public int getRandomSafeXZCoord(int degree, int safeDegree, int correctionDegree) {
        int addX = EntityUtils.rand(degree / 2 * -1, degree / 2);
        if (addX >= 0) {
            if (degree < safeDegree) {
                addX = safeDegree;
                addX += EntityUtils.rand(correctionDegree / 2 * -1, correctionDegree / 2);
            }
        } else {
            if (degree > safeDegree) {
                addX = -safeDegree;
                addX += EntityUtils.rand(correctionDegree / 2 * -1, correctionDegree / 2);
            }
        }
        return addX;
    }

    public int getSafeYCoord(Level level, Position pos, int needDegree) {
        int x = (int) pos.x;
        int y = (int) pos.y;
        int z = (int) pos.z;

        if (level.getBlockIdAt(x, y, z) == Block.AIR) {
            while (true) {
                y--;
                if (y > 255) {
                    y = 256;
                    break;
                }
                if (y < 1) {
                    y = 0;
                    break;
                }
                if (level.getBlockIdAt(x, y, z) != Block.AIR) {
                    int checkNeedDegree = needDegree;
                    int checkY = y;
                    while (true) {
                        checkY++;
                        checkNeedDegree--;
                        if (checkY > 255 || checkY < 1 || level.getBlockIdAt(x, checkY, z) != Block.AIR) {
                            break;
                        }

                        if (checkNeedDegree <= 0) {
                            return y;
                        }
                    }
                }
            }
        } else {
            while (true) {
                y++;
                if (y > 255) {
                    y = 256;
                    break;
                }

                if (y < 1) {
                    y = 0;
                    break;
                }

                if (level.getBlockIdAt(x, y, z) != Block.AIR) {
                    int checkNeedDegree = needDegree;
                    int checkY = y;
                    while (true) {
                        checkY--;
                        checkNeedDegree--;
                        if (checkY > 255 || checkY < 1 || level.getBlockIdAt(x, checkY, z) != Block.AIR) {
                            break;
                        }

                        if (checkNeedDegree <= 0) {
                            return y;
                        }
                    }
                }
            }
        }
        return y;
    }
}

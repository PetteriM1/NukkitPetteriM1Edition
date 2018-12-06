package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.*;
import cn.nukkit.entity.passive.*;
import cn.nukkit.level.Position;
import cn.nukkit.level.Level;
import cn.nukkit.utils.spawners.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spawner extends Thread {

    private Map<Integer, Integer> maxSpawns = new HashMap<>();
    private List<EntitySpawner> entitySpawners = new ArrayList<>();

    public Spawner() {        
        prepareMaxSpawns();
        prepareSpawnerClasses();
    }

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

    private void prepareSpawnerClasses() {
        entitySpawners.add(new ChickenSpawner(this));
        entitySpawners.add(new CowSpawner(this));
        entitySpawners.add(new CreeperSpawner(this));
        entitySpawners.add(new EndermanSpawner(this));
        entitySpawners.add(new HorseSpawner(this));
        entitySpawners.add(new MooshroomSpawner(this));
        entitySpawners.add(new OcelotSpawner(this));
        entitySpawners.add(new PigSpawner(this));
        entitySpawners.add(new PolarBearSpawner(this));
        entitySpawners.add(new PigZombieSpawner(this));
        entitySpawners.add(new SheepSpawner(this));
        entitySpawners.add(new SkeletonSpawner(this));
        entitySpawners.add(new SpiderSpawner(this));
        entitySpawners.add(new ZombieSpawner(this));
        entitySpawners.add(new WitchSpawner(this));
    }

    private void prepareMaxSpawns() {
        maxSpawns.put(EntityChicken.NETWORK_ID, 1);
        maxSpawns.put(EntityCow.NETWORK_ID, 1);
        maxSpawns.put(EntityCreeper.NETWORK_ID, 1);
        maxSpawns.put(EntityEnderman.NETWORK_ID, 1);
        maxSpawns.put(EntityHorse.NETWORK_ID, 1);
        maxSpawns.put(EntityMooshroom.NETWORK_ID, 1);
        maxSpawns.put(EntityOcelot.NETWORK_ID, 1);
        maxSpawns.put(EntityPig.NETWORK_ID, 1);
        maxSpawns.put(EntityPolarBear.NETWORK_ID, 1);
        maxSpawns.put(EntityZombiePigman.NETWORK_ID, 1);
        maxSpawns.put(EntitySheep.NETWORK_ID, 1);
        maxSpawns.put(EntitySkeleton.NETWORK_ID, 1);
        maxSpawns.put(EntitySpider.NETWORK_ID, 1);
        maxSpawns.put(EntityZombie.NETWORK_ID, 1);
        maxSpawns.put(EntityWitch.NETWORK_ID, 1);
    }

    public boolean entitySpawnAllowed(Level level, int networkId, String entityName) {
        return countEntity(level, networkId) < maxSpawns.getOrDefault(networkId, 0);
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

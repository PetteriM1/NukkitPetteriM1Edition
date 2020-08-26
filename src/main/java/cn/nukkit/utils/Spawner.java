package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.*;
import cn.nukkit.entity.passive.EntityCod;
import cn.nukkit.entity.passive.EntitySalmon;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.spawners.*;

import java.util.Arrays;
import java.util.List;

public class Spawner implements Runnable {

    private final List<EntitySpawner> animalSpawners = Arrays.asList(
            new ChickenSpawner(this),
            new CodSpawner(this),
            new CowSpawner(this),
            new DolphinSpawner(this),
            new DonkeySpawner(this),
            new HorseSpawner(this),
            new MooshroomSpawner(this),
            new OcelotSpawner(this),
            new ParrotSpawner(this),
            new PigSpawner(this),
            new PolarBearSpawner(this),
            new PufferfishSpawner(this),
            new RabbitSpawner(this),
            new SalmonSpawner(this),
            new SheepSpawner(this),
            new SquidSpawner(this),
            new TropicalFishSpawner(this),
            new TurtleSpawner(this),
            new WolfSpawner(this),
            new PandaSpawner(this),
            new FoxSpawner(this)
    );

    private final List<EntitySpawner> mobSpawners = Arrays.asList(
            new BlazeSpawner(this),
            new CreeperSpawner(this),
            new EndermanSpawner(this),
            new GhastSpawner(this),
            new HuskSpawner(this),
            new MagmaCubeSpawner(this),
            new SkeletonSpawner(this),
            new SlimeSpawner(this),
            new SpiderSpawner(this),
            new StraySpawner(this),
            new ZombieSpawner(this),
            new ZombiePigmanSpawner(this),
            new WitchSpawner(this),
            new WitherSkeletonSpawner(this),
            new DrownedSpawner(this)
    );

    @Override
    public void run() {
        if (!Server.getInstance().getOnlinePlayers().isEmpty()) {
            if (Server.getInstance().spawnAnimals) {
                for (EntitySpawner spawner : animalSpawners) {
                    spawner.spawn();
                }
            }
            if (Server.getInstance().spawnMobs) {
                for (EntitySpawner spawner : mobSpawners) {
                    spawner.spawn();
                }
            }
        }
    }

    static boolean entitySpawnAllowed(Level level, int networkId, Vector3 pos) {
        int max = getMaxSpawns(networkId, level.getDimension() == Level.DIMENSION_NETHER, level.getDimension() == Level.DIMENSION_THE_END);
        if (max == 0) return false;
        int count = 0;
        for (Entity entity : level.entities.values()) {
            if (entity.isAlive() && entity.getNetworkId() == networkId && new Vector3(pos.x, entity.y, pos.z).distanceSquared(entity) < 10000) { // 100 blocks
                count++;
                if (count > max) {
                    return false;
                }
            }
        }
        return count < max;
    }

    public BaseEntity createEntity(Object type, Position pos) {
        BaseEntity entity = (BaseEntity) Entity.createEntity((String) type, pos);
        if (entity != null) {
            if (!entity.isInsideOfSolid() && !tooNearOfPlayer(pos)) {
                CreatureSpawnEvent ev = new CreatureSpawnEvent(entity.getNetworkId(), pos, entity.namedTag, CreatureSpawnEvent.SpawnReason.NATURAL);
                Server.getInstance().getPluginManager().callEvent(ev);
                if (!ev.isCancelled()) {
                    entity.spawnToAll();
                } else {
                    entity.close();
                }
            } else {
                entity.close();
            }
        }
        return entity;
    }

    private static boolean tooNearOfPlayer(Position pos) {
        for (Player p : pos.getLevel().getPlayers().values()) {
            if (p.distanceSquared(pos) < 144) { // 12 blocks
                return true;
            }
        }
        return false;
    }

    static int getRandomSafeXZCoord(int degree, int safeDegree, int correctionDegree) {
        int addX = Utils.rand((degree >> 1) * -1, degree >> 1);
        if (addX >= 0) {
            if (degree < safeDegree) {
                addX = safeDegree;
                addX += Utils.rand((correctionDegree >> 1) * -1, correctionDegree >> 1);
            }
        } else {
            if (degree > safeDegree) {
                addX = -safeDegree;
                addX += Utils.rand((correctionDegree >> 1) * -1, correctionDegree >> 1);
            }
        }
        return addX;
    }

    static int getSafeYCoord(Level level, Position pos) {
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
                    int checkNeedDegree = 3;
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
                    int checkNeedDegree = 3;
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

    private static int getMaxSpawns(int id, boolean nether, boolean end) {
        switch (id) {
            case EntityZombiePigman.NETWORK_ID:
                return nether ? 4 : 0;
            case EntityGhast.NETWORK_ID:
            case EntityBlaze.NETWORK_ID:
            case EntityWitherSkeleton.NETWORK_ID:
            case EntityMagmaCube.NETWORK_ID:
                return nether ? 2 : 0;
            case EntityEnderman.NETWORK_ID:
                return end ? 10 : 2;
            case EntityCod.NETWORK_ID:
            case EntitySalmon.NETWORK_ID:
                return end || nether ? 0 : 4;
            case EntityWitch.NETWORK_ID:
                return end || nether ? 0 : 1;
            default:
                return end || nether ? 0 : 2;
        }
    }
}

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
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.spawners.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the automatic spawning of mobs.
 */
public class SpawnerTask implements Runnable {

    private final Map<Class<?>, EntitySpawner> animalSpawners = new HashMap<>();
    private final Map<Class<?>, EntitySpawner> mobSpawners = new HashMap<>();

    /**
     * Split monster and animal spawning to different ticks to avoid lag spikes
     * Animals and monsters are spawned every other time the spawer task runs
     */
    private boolean mobsNext;

    public SpawnerTask() {
        this.registerAnimalSpawner(ChickenSpawner.class);
        this.registerAnimalSpawner(CowSpawner.class);
        this.registerAnimalSpawner(DolphinSpawner.class);
        this.registerAnimalSpawner(DonkeySpawner.class);
        this.registerAnimalSpawner(HorseSpawner.class);
        this.registerAnimalSpawner(MooshroomSpawner.class);
        this.registerAnimalSpawner(OcelotSpawner.class);
        this.registerAnimalSpawner(ParrotSpawner.class);
        this.registerAnimalSpawner(PigSpawner.class);
        this.registerAnimalSpawner(PolarBearSpawner.class);
        this.registerAnimalSpawner(PufferfishSpawner.class);
        this.registerAnimalSpawner(RabbitSpawner.class);
        this.registerAnimalSpawner(SalmonSpawner.class);
        this.registerAnimalSpawner(SheepSpawner.class);
        this.registerAnimalSpawner(SquidSpawner.class);
        this.registerAnimalSpawner(TropicalFishSpawner.class);
        this.registerAnimalSpawner(TurtleSpawner.class);
        this.registerAnimalSpawner(WolfSpawner.class);
        this.registerAnimalSpawner(PandaSpawner.class);
        this.registerAnimalSpawner(FoxSpawner.class);

        this.registerMobSpawner(BlazeSpawner.class);
        this.registerMobSpawner(CreeperSpawner.class);
        this.registerMobSpawner(EndermanSpawner.class);
        this.registerMobSpawner(GhastSpawner.class);
        this.registerMobSpawner(HuskSpawner.class);
        this.registerMobSpawner(MagmaCubeSpawner.class);
        this.registerMobSpawner(SkeletonSpawner.class);
        this.registerMobSpawner(SlimeSpawner.class);
        this.registerMobSpawner(SpiderSpawner.class);
        this.registerMobSpawner(StraySpawner.class);
        this.registerMobSpawner(ZombieSpawner.class);
        this.registerMobSpawner(ZombiePigmanSpawner.class);
        this.registerMobSpawner(WitchSpawner.class);
        this.registerMobSpawner(WitherSkeletonSpawner.class);
        this.registerMobSpawner(DrownedSpawner.class);
        this.registerMobSpawner(PhantomSpawner.class);
        this.registerMobSpawner(PiglinSpawner.class);
        this.registerMobSpawner(HoglinSpawner.class);
    }

    /**
     * Register animal spawner
     *
     * @param clazz spawner class
     * @return whether the spawner was registered successfully (no errors and not already registered)
     */
    public boolean registerAnimalSpawner(Class<?> clazz) {
        if (this.animalSpawners.containsKey(clazz)) {
            return false;
        }

        try {
            EntitySpawner spawner = (EntitySpawner) clazz.getConstructor(SpawnerTask.class).newInstance(this);
            this.animalSpawners.put(clazz, spawner);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Get EntitySpawner for class
     *
     * @param clazz spawner class
     * @return EntitySpawner
     */
    public EntitySpawner getAnimalSpawner(Class<?> clazz) {
        return this.animalSpawners.get(clazz);
    }

    /**
     * Unregister animal spawner
     *
     * @param clazz spawner class
     * @return succeed
     */
    public boolean unregisterAnimalSpawner(Class<?> clazz) {
        return this.animalSpawners.remove(clazz) != null;
    }

    /**
     * Register monster spawner
     *
     * @param clazz spawner class
     * @return whether the spawner was registered successfully (no errors and not already registered)
     */
    public boolean registerMobSpawner(Class<?> clazz) {
        if (this.mobSpawners.containsKey(clazz)) {
            return false;
        }

        try {
            EntitySpawner spawner = (EntitySpawner) clazz.getConstructor(SpawnerTask.class).newInstance(this);
            this.mobSpawners.put(clazz, spawner);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Get EntitySpawner for class
     *
     * @param clazz spawner class
     * @return EntitySpawner
     */
    public EntitySpawner getMobSpawner(Class<?> clazz) {
        return this.mobSpawners.get(clazz);
    }

    /**
     * Unregister monster spawner
     *
     * @param clazz spawner class
     * @return succeed
     */
    public boolean unregisterMobSpawner(Class<?> clazz) {
        return this.mobSpawners.remove(clazz) != null;
    }

    @Override
    public void run() {
        if (Server.getInstance().getOnlinePlayersCount() != 0) {
            if (mobsNext) {
                mobsNext = false;
                if (Server.getInstance().spawnMonsters) {
                    for (EntitySpawner spawner : mobSpawners.values()) {
                        spawner.spawn();
                    }
                }
            } else {
                mobsNext = true;
                if (Server.getInstance().spawnAnimals) {
                    for (EntitySpawner spawner : animalSpawners.values()) {
                        spawner.spawn();
                    }
                }
            }
        }
    }

    /**
     * Check if mob spawning is allowed
     *
     * @param level world
     * @param networkId mob network id
     * @param player player
     * @return whether mob spawning is possible near the player
     */
    static boolean entitySpawnAllowed(Level level, int networkId, Player player) {
        if (networkId == EntityPhantom.NETWORK_ID && (player.ticksSinceLastRest < 72000 || player.isSleeping() || player.isSpectator() || !level.getGameRules().getBoolean(GameRule.DO_INSOMNIA))) {
            return false;
        }
        int max = getMaxSpawns(networkId, level.getDimension() == Level.DIMENSION_NETHER, level.getDimension() == Level.DIMENSION_THE_END);
        if (max == 0) return false;
        int count = 0;
        for (Entity entity : level.entities.values()) {
            if (entity.isAlive() && entity.getNetworkId() == networkId && new Vector3(player.x, entity.y, player.z).distanceSquared(entity) < 10000) { // 100 blocks
                count++;
                if (count > max) {
                    return false;
                }
            }
        }
        return count < max;
    }

    /**
     * Attempt to spawn a mob
     *
     * @param type mob id
     * @param pos position
     * @return spawned entity or null
     */
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
                    entity = null;
                }
            } else {
                entity.close();
                entity = null;
            }
        }
        return entity;
    }

    /**
     * Check if mob spawn position is too close to player
     *
     * @param pos position
     * @return whether the position is too close to player
     */
    private static boolean tooNearOfPlayer(Position pos) {
        for (Player p : pos.getLevel().getPlayers().values()) {
            if (p.distanceSquared(pos) < 196) { // 14 blocks
                return true;
            }
        }
        return false;
    }

    /**
     * Get safe x / z coordinate for mob spawning
     *
     * @param degree
     * @param safeDegree
     * @param correctionDegree
     * @return safe spawn x / z coordinate
     */
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

    /**
     * Get safe y coordinate for mob spawning
     *
     * @param level world
     * @param pos initial position
     * @return safe spawn y coordinate
     */
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

    /**
     * Get maximum amount of mobs in distance
     *
     * @param id mob network id
     * @param nether is nether world
     * @param end is end world
     * @return maximum amount of mobs
     */
    private static int getMaxSpawns(int id, boolean nether, boolean end) {
        switch (id) {
            case EntityZombiePigman.NETWORK_ID:
            case EntityPiglin.NETWORK_ID:
            case EntityHoglin.NETWORK_ID:
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
            case EntityPhantom.NETWORK_ID:
                int difficulty = Server.getInstance().getDifficulty();
                return end || nether ? 0 : difficulty == 1 ? 2 : difficulty == 2 ? 3 : 4;
            default:
                return end || nether ? 0 : 2;
        }
    }
}

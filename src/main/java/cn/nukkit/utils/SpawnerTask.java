package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.*;
import cn.nukkit.entity.passive.*;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.utils.spawners.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the automatic spawning of mobs.
 */
public class SpawnerTask implements Runnable {

    private final Map<Class<?>, EntitySpawner> animalSpawners = new HashMap<>();
    private final Map<Class<?>, EntitySpawner> mobSpawners = new HashMap<>();

    /**
     * Run the spawner on 2x speed but spawn only either monsters or animals
     */
    private boolean thisTickMobs;

    @Setter
    @Getter
    private boolean spawnAnimals;
    @Setter
    @Getter
    private boolean spawnMonsters;

    public SpawnerTask(boolean spawnAnimals, boolean spawnMonsters) {
        this.spawnAnimals = spawnAnimals;
        this.spawnMonsters = spawnMonsters;

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
        this.registerAnimalSpawner(CodSpawner.class);
        this.registerAnimalSpawner(SheepSpawner.class);
        this.registerAnimalSpawner(SquidSpawner.class);
        this.registerAnimalSpawner(TropicalFishSpawner.class);
        this.registerAnimalSpawner(TurtleSpawner.class);
        this.registerAnimalSpawner(WolfSpawner.class);
        this.registerAnimalSpawner(PandaSpawner.class);
        this.registerAnimalSpawner(FoxSpawner.class);
        this.registerAnimalSpawner(LlamaSpawner.class);

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
        this.registerMobSpawner(ZombifiedPiglinSpawner.class);
        this.registerMobSpawner(WitchSpawner.class);
        this.registerMobSpawner(WitherSkeletonSpawner.class);
        this.registerMobSpawner(DrownedSpawner.class);
        this.registerMobSpawner(PhantomSpawner.class);
        this.registerMobSpawner(PiglinSpawner.class);
        this.registerMobSpawner(StriderSpawner.class);
        this.registerMobSpawner(HoglinSpawner.class);
    }

    /**
     * Attempt to spawn a mob
     *
     * @param type mob id
     * @param pos  position
     * @return spawned entity or null
     */
    public BaseEntity createEntity(Object type, Position pos) {
        BaseEntity entity = (BaseEntity) Entity.createEntity((String) type, pos);
        if (entity != null) {
            if (!entity.isInsideOfSolid()) {
                CreatureSpawnEvent ev = new CreatureSpawnEvent(entity.getNetworkId(), pos, entity.namedTag, CreatureSpawnEvent.SpawnReason.NATURAL);
                Server.getInstance().getPluginManager().callEvent(ev);
                if (!ev.isCancelled()) {
                    entity.stayTime = 60;
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
     * Check if mob spawning is allowed
     *
     * @param level     world
     * @param networkId mob network id
     * @param player    player
     * @return whether mob spawning is possible near the player
     */
    static boolean entitySpawnAllowed(Level level, int networkId, Player player) {
        if (networkId == EntityPhantom.NETWORK_ID && (player.getTimeSinceRest() < 72000 || player.isSleeping() || player.isSpectator() || !level.getGameRules().getBoolean(GameRule.DO_INSOMNIA))) {
            return false;
        }
        int max = getMaxSpawns(networkId, level.getDimension() == Level.DIMENSION_NETHER, level.getDimension() == Level.DIMENSION_THE_END);
        if (max == 0) return false;
        int count = 0;
        int globalCount = 0;
        for (Entity entity : level.getEntitiesList()) {
            if (!(entity instanceof BaseEntity) || !entity.isAlive()) {
                continue;
            }
            if (Math.pow(player.x - entity.x, 2) + Math.pow(player.z - entity.z, 2) < 16384) { // 128 blocks, ignore y
                if (++globalCount > 200) {
                    return false;
                }
                if (entity.getNetworkId() == networkId && ++count > max) {
                    return false;
                }
            }
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
     * Get maximum amount of mobs in distance
     *
     * @param id     mob network id
     * @param nether is nether world
     * @param end    is end world
     * @return maximum amount of mobs
     */
    private static int getMaxSpawns(int id, boolean nether, boolean end) {
        switch (id) {
            case EntityZombiePigman.NETWORK_ID:
            case EntityPiglin.NETWORK_ID:
            case EntityHoglin.NETWORK_ID:
            case EntityGhast.NETWORK_ID:
            case EntityWitherSkeleton.NETWORK_ID:
            case EntityMagmaCube.NETWORK_ID:
            case EntityStrider.NETWORK_ID:
                return nether ? 4 : 0;
            case EntityEnderman.NETWORK_ID:
                return end ? 12 : nether ? 4 : 2;
            case EntityCod.NETWORK_ID:
            case EntitySalmon.NETWORK_ID:
                return end || nether ? 0 : 10;
            case EntityWitch.NETWORK_ID:
                return end || nether ? 0 : 1;
            case EntityPhantom.NETWORK_ID:
                int difficulty = Server.getInstance().getDifficulty();
                return end || nether ? 0 : difficulty == 1 ? 2 : difficulty == 2 ? 3 : 4;
            case EntitySkeleton.NETWORK_ID:
                return end ? 0 : 4;
            case EntityPufferfish.NETWORK_ID:
                return end || nether ? 0 : 3;
            default:
                return end || nether ? 0 : 4;
        }
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
     * Get safe y coordinate for mob spawning
     *
     * @param level world
     * @param pos   initial position
     * @param chunk which includes initial position
     * @return safe spawn y coordinate
     */
    static int getSafeYCoord(Level level, Position pos, FullChunk chunk) {
        int x = (int) pos.x;
        int y = (int) pos.y;
        int z = (int) pos.z;

        if (level.getBlockIdAt(chunk, x, y, z) == Block.AIR) {
            while (true) {
                y--;
                if (y > level.getMaxBlockY()) {
                    y = level.getMaxBlockY();
                    break;
                }
                if (y < level.getMinBlockY()) {
                    y = level.getMinBlockY();
                    break;
                }
                if (level.getBlockIdAt(chunk, x, y, z) != Block.AIR) {
                    int checkNeedDegree = 3;
                    int checkY = y;
                    while (true) {
                        checkY++;
                        checkNeedDegree--;
                        if (checkY > level.getMaxBlockY() || checkY < level.getMinBlockY() || level.getBlockIdAt(chunk, x, checkY, z) != Block.AIR) {
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
                if (y > level.getMaxBlockY()) {
                    y = level.getMaxBlockY();
                    break;
                }
                if (y < level.getMinBlockY()) {
                    y = level.getMinBlockY();
                    break;
                }
                if (level.getBlockIdAt(chunk, x, y, z) != Block.AIR) {
                    int checkNeedDegree = 3;
                    int checkY = y;
                    while (true) {
                        checkY--;
                        checkNeedDegree--;
                        if (checkY > level.getMaxBlockY() || checkY < level.getMinBlockY() || level.getBlockIdAt(chunk, x, checkY, z) != Block.AIR) {
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

    @Override
    public void run() {
        if (Server.getInstance().getOnlinePlayersCount() != 0) {
            thisTickMobs = !thisTickMobs;

            if (thisTickMobs) {
                if (spawnMonsters) {
                    for (EntitySpawner spawner : mobSpawners.values()) {
                        spawner.spawn();
                    }
                }
            } else {
                if (spawnAnimals) {
                    for (EntitySpawner spawner : animalSpawners.values()) {
                        spawner.spawn();
                    }
                }
            }
        }
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
     * Unregister monster spawner
     *
     * @param clazz spawner class
     * @return succeed
     */
    public boolean unregisterMobSpawner(Class<?> clazz) {
        return this.mobSpawners.remove(clazz) != null;
    }
}

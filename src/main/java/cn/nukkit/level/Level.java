package cn.nukkit.level;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockGrass;
import cn.nukkit.block.BlockID;
import cn.nukkit.block.BlockRedstoneDiode;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.item.EntityXPOrb;
import cn.nukkit.entity.mob.EntityWither;
import cn.nukkit.entity.passive.EntityIronGolem;
import cn.nukkit.entity.passive.EntitySnowGolem;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.BlockUpdateEvent;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.event.level.*;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.event.weather.LightningStrikeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.format.Chunk;
import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.format.generic.BaseLevelProvider;
import cn.nukkit.level.format.generic.EmptyChunkSection;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.PopChunkManager;
import cn.nukkit.level.generator.task.GenerationTask;
import cn.nukkit.level.generator.task.LightPopulationTask;
import cn.nukkit.level.generator.task.PopulationTask;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.level.sound.Sound;
import cn.nukkit.math.*;
import cn.nukkit.math.BlockFace.Plane;
import cn.nukkit.metadata.BlockMetadataStore;
import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.metadata.Metadatable;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.network.protocol.*;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.BlockUpdateScheduler;
import cn.nukkit.utils.*;
import co.aikar.timings.Timings;
import co.aikar.timings.TimingsHistory;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.*;


/**
 * @author MagicDroidX Nukkit Project
 */
public class Level implements ChunkManager, Metadatable {

    private static int levelIdCounter = 1;
    private static int chunkLoaderCounter = 1;

    public static final int BLOCK_UPDATE_NORMAL = 1;
    public static final int BLOCK_UPDATE_RANDOM = 2;
    public static final int BLOCK_UPDATE_SCHEDULED = 3;
    public static final int BLOCK_UPDATE_WEAK = 4;
    public static final int BLOCK_UPDATE_TOUCH = 5;
    public static final int BLOCK_UPDATE_REDSTONE = 6;
    public static final int BLOCK_UPDATE_TICK = 7;

    public static final int TIME_DAY = 0;
    public static final int TIME_NOON = 6000;
    public static final int TIME_SUNSET = 12000;
    public static final int TIME_NIGHT = 14000;
    public static final int TIME_MIDNIGHT = 18000;
    public static final int TIME_SUNRISE = 23000;

    public static final int TIME_FULL = 24000;

    public static final int DIMENSION_OVERWORLD = 0;
    public static final int DIMENSION_NETHER = 1;
    public static final int DIMENSION_THE_END = 2;

    // Lower values use less memory
    public static final int MAX_BLOCK_CACHE = 512;

    // The blocks that can randomly tick
    private static final boolean[] randomTickBlocks = new boolean[256];
    static {
        randomTickBlocks[Block.GRASS] = true;
        randomTickBlocks[Block.FARMLAND] = true;
        randomTickBlocks[Block.MYCELIUM] = true;
        randomTickBlocks[Block.SAPLING] = true;
        randomTickBlocks[Block.LEAVES] = true;
        randomTickBlocks[Block.LEAVES2] = true;
        randomTickBlocks[Block.SNOW_LAYER] = true;
        randomTickBlocks[Block.ICE] = true;
        if (!Server.getInstance().suomiCraftPEMode()) randomTickBlocks[Block.LAVA] = true;
        if (!Server.getInstance().suomiCraftPEMode()) randomTickBlocks[Block.STILL_LAVA] = true;
        randomTickBlocks[Block.CACTUS] = true;
        randomTickBlocks[Block.BEETROOT_BLOCK] = true;
        randomTickBlocks[Block.CARROT_BLOCK] = true;
        randomTickBlocks[Block.POTATO_BLOCK] = true;
        randomTickBlocks[Block.MELON_STEM] = true;
        randomTickBlocks[Block.PUMPKIN_STEM] = true;
        randomTickBlocks[Block.WHEAT_BLOCK] = true;
        randomTickBlocks[Block.SUGARCANE_BLOCK] = true;
        randomTickBlocks[Block.NETHER_WART_BLOCK] = true;
        if (!Server.getInstance().suomiCraftPEMode()) randomTickBlocks[Block.FIRE] = true;
        randomTickBlocks[Block.GLOWING_REDSTONE_ORE] = true;
        randomTickBlocks[Block.COCOA_BLOCK] = true;
        randomTickBlocks[Block.ICE_FROSTED] = true;
        randomTickBlocks[Block.VINE] = true;
        randomTickBlocks[Block.WATER] = true;
        randomTickBlocks[Block.CAULDRON_BLOCK] = true;
    }

    private final Long2ObjectOpenHashMap<BlockEntity> blockEntities = new Long2ObjectOpenHashMap<>();

    private final Long2ObjectOpenHashMap<Player> players = new Long2ObjectOpenHashMap<>();

    public final Long2ObjectOpenHashMap<Entity> entities = new Long2ObjectOpenHashMap<>();

    public final Long2ObjectOpenHashMap<Entity> updateEntities = new Long2ObjectOpenHashMap<>();

    private final ConcurrentLinkedQueue<BlockEntity> updateBlockEntities = new ConcurrentLinkedQueue<>();

    private final Server server;

    private final int levelId;

    private LevelProvider provider;

    private final Int2ObjectOpenHashMap<ChunkLoader> loaders = new Int2ObjectOpenHashMap<>();

    private final Int2IntMap loaderCounter = new Int2IntOpenHashMap();

    private final Long2ObjectOpenHashMap<Map<Integer, ChunkLoader>> chunkLoaders = new Long2ObjectOpenHashMap<>();

    private final Long2ObjectOpenHashMap<Map<Integer, Player>> playerLoaders = new Long2ObjectOpenHashMap<>();

    private final Long2ObjectOpenHashMap<Deque<DataPacket>> chunkPackets = new Long2ObjectOpenHashMap<>();

    private final Long2LongMap unloadQueue = Long2LongMaps.synchronize(new Long2LongOpenHashMap());

    private int time;

    public boolean stopTime;

    public float skyLightSubtracted;

    private final String folderName;

    // Avoid OOM, gc'd references result in whole chunk being sent (possibly higher cpu)
    private final Long2ObjectOpenHashMap<SoftReference<Map<Character, Object>>> changedBlocks = new Long2ObjectOpenHashMap<>();
    // Storing the vector is redundant
    private final Object changeBlocksPresent = new Object();
    // Storing extra blocks past 512 is redundant
    private final Map<Character, Object> changeBlocksFullMap = new CharacterHashMap();

    private final BlockUpdateScheduler updateQueue;
    private final Queue<Block> normalUpdateQueue = new ConcurrentLinkedDeque<>();
    //private final TreeSet<BlockUpdateEntry> updateQueue = new TreeSet<>();
    //private final List<BlockUpdateEntry> nextTickUpdates = Lists.newArrayList();
    //private final Map<BlockVector3, Integer> updateQueueIndex = new HashMap<>();

    private final Int2ObjectMap<ConcurrentMap<Long, Int2ObjectMap<Player>>> chunkSendQueues = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<LongSet> chunkSendTasks = new Int2ObjectOpenHashMap<>();

    private final Long2ObjectOpenHashMap<Boolean> chunkPopulationQueue = new Long2ObjectOpenHashMap<>();
    private final Long2ObjectOpenHashMap<Boolean> chunkPopulationLock = new Long2ObjectOpenHashMap<>();
    private final Long2ObjectOpenHashMap<Boolean> chunkGenerationQueue = new Long2ObjectOpenHashMap<>();
    private final int chunkGenerationQueueSize;
    private final int chunkPopulationQueueSize;

    private boolean autoSave;

    private BlockMetadataStore blockMetadata;

    private final boolean useSections;

    private final Vector3 temporalVector;

    public int sleepTicks = 0;

    private final int chunkTickRadius;
    private final Long2IntMap chunkTickList = new Long2IntOpenHashMap();
    private final int chunksPerTicks;
    private final boolean clearChunksOnTick;

    private int updateLCG = ThreadLocalRandom.current().nextInt();

    private static final int LCG_CONSTANT = 1013904223;

    public LevelTimings timings;

    private int tickRate;
    public int tickRateTime = 0;
    public int tickRateCounter = 0;

    // Notice: These shouldn't be used in the internal methods
    // Check the dimension id instead
    public final boolean isNether;
    public final boolean isEnd;

    private Class<? extends Generator> generatorClass;
    private IterableThreadLocal<Generator> generators = new IterableThreadLocal<Generator>() {

        @Override
        public Generator init() {
            try {
                Generator generator = generatorClass.getConstructor(Map.class).newInstance(provider.getGeneratorOptions());
                NukkitRandom rand = new NukkitRandom(getSeed());
                if (Server.getInstance().isPrimaryThread()) {
                    generator.init(Level.this, rand);
                }
                generator.init(new PopChunkManager(getSeed()), rand);
                return generator;
            } catch (Throwable e) {
                Server.getInstance().getLogger().logException(e);
                return null;
            }
        }
    };

    private boolean raining;
    private int rainTime;
    private boolean thundering;
    private int thunderTime;

    private long levelCurrentTick;

    private int dimension;

    public GameRules gameRules;

    private final boolean randomTickingEnabled;

    public Level(Server server, String name, String path, Class<? extends LevelProvider> provider) {
        this.levelId = levelIdCounter++;
        this.blockMetadata = new BlockMetadataStore(this);
        this.server = server;
        this.autoSave = server.getAutoSave();

        try {
            this.provider = provider.getConstructor(Level.class, String.class).newInstance(this, path);
        } catch (Exception e) {
            throw new LevelException("Caused by " + Utils.getExceptionMessage(e));
        }

        this.timings = new LevelTimings(this);

        this.provider.updateLevelName(name);

        this.server.getLogger().info(this.server.getLanguage().translateString("nukkit.level.preparing",
                TextFormat.GREEN + this.provider.getName() + TextFormat.WHITE));

        this.generatorClass = Generator.getGenerator(this.provider.getGenerator());

        try {
            this.useSections = (boolean) provider.getMethod("usesChunkSection").invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.folderName = name;
        this.time = (int) this.provider.getTime();

        this.raining = this.provider.isRaining();
        this.rainTime = this.provider.getRainTime();
        if (this.rainTime <= 0) {
            setRainTime(Utils.random.nextInt(168000) + 12000);
        }

        this.thundering = this.provider.isThundering();
        this.thunderTime = this.provider.getThunderTime();
        if (this.thunderTime <= 0) {
            setThunderTime(Utils.random.nextInt(168000) + 12000);
        }

        this.levelCurrentTick = this.provider.getCurrentTick();
        this.updateQueue = new BlockUpdateScheduler(this, levelCurrentTick);

        this.chunkTickRadius = Math.min(this.server.getViewDistance(), Math.max(1, this.server.getPropertyInt("chunk-ticking-radius", 4)));
        this.chunksPerTicks = this.server.getPropertyInt("chunk-ticking-per-tick", 40);
        this.chunkGenerationQueueSize = this.server.getPropertyInt("chunk-generation-queue-size", 8);
        this.chunkPopulationQueueSize = this.server.getPropertyInt("chunk-generation-population-queue-size", 8);
        this.chunkTickList.clear();
        this.clearChunksOnTick = this.server.getPropertyBoolean("clear-chunk-tick-list", true);
        this.temporalVector = new Vector3(0, 0, 0);
        this.tickRate = 1;

        this.skyLightSubtracted = this.calculateSkylightSubtracted(1);

        this.isNether = name.equals("nether");
        this.isEnd = name.equals("the_end");

        this.randomTickingEnabled = !Server.noTickingWorlds.contains(name);
    }

    public static long chunkHash(int x, int z) {
        return (((long) x) << 32) | (z & 0xffffffffL);
    }

    public static long blockHash(int x, int y, int z) {
        if (y < 0 || y >= 256) {
            throw new IllegalArgumentException("Y coordinate " + y + " is out of range!");
        }
        return (((long) x & (long) 0xFFFFFFF) << 36) | (((long) y & (long) 0xFF) << 28) | ((long) z & (long) 0xFFFFFFF);
    }

    public static char localBlockHash(double x, double y, double z) {
        byte hi = (byte) (((int) x & 15) + (((int) z & 15) << 4));
        byte lo = (byte) y;
        return (char) (((hi & 0xFF) << 8) | (lo & 0xFF));
    }

    public static Vector3 getBlockXYZ(long chunkHash, char blockHash) {
        int hi = (byte) (blockHash >>> 8);
        int lo = (byte) blockHash;
        int y = lo & 0xFF;
        int x = (hi & 0xF) + (getHashX(chunkHash) << 4);
        int z = ((hi >> 4) & 0xF) + (getHashZ(chunkHash) << 4);
        return new Vector3(x, y, z);
    }

    public static BlockVector3 blockHash(double x, double y, double z) {
        return new BlockVector3((int) x, (int) y, (int) z);
    }

    public static int chunkBlockHash(int x, int y, int z) {
        return (x << 12) | (z << 8) | y;
    }

    public static int getHashX(long hash) {
        return (int) (hash >> 32);
    }

    public static int getHashZ(long hash) {
        return (int) hash;
    }

    public static Vector3 getBlockXYZ(BlockVector3 hash) {
        return new Vector3(hash.x, hash.y, hash.z);
    }

    public static Chunk.Entry getChunkXZ(long hash) {
        return new Chunk.Entry(getHashX(hash), getHashZ(hash));
    }

    public static int generateChunkLoaderId(ChunkLoader loader) {
        if (loader.getLoaderId() == 0) {
            return chunkLoaderCounter++;
        } else {
            throw new IllegalStateException("ChunkLoader has a loader id already assigned: " + loader.getLoaderId());
        }
    }

    public int getTickRate() {
        return tickRate;
    }

    public int getTickRateTime() {
        return tickRateTime;
    }

    public void setTickRate(int tickRate) {
        this.tickRate = tickRate;
    }

    public void initLevel() {
        Generator generator = generators.get();
        this.dimension = generator.getDimension();
        this.gameRules = this.provider.getGamerules();
    }

    public Generator getGenerator() {
        return generators.get();
    }

    public BlockMetadataStore getBlockMetadata() {
        return this.blockMetadata;
    }

    public Server getServer() {
        return server;
    }

    final public LevelProvider getProvider() {
        return this.provider;
    }

    final public int getId() {
        return this.levelId;
    }

    public void close() {
        if (this.autoSave) {
            this.save(true);
        }

        this.provider.close();
        this.provider = null;
        this.blockMetadata = null;
        this.server.getLevels().remove(this.levelId);
        this.generators.clean();
    }

    public void addSound(Vector3 pos, String sound) {
        this.addSound(pos, sound, (Player[]) null);
    }

    public void addSound(Vector3 pos, String sound, Player... players) {
        PlaySoundPacket packet = new PlaySoundPacket();
        packet.name = sound;
        packet.volume = 1;
        packet.pitch = 1;
        packet.x = pos.getFloorX();
        packet.y = pos.getFloorY();
        packet.z = pos.getFloorZ();

        if (players == null || players.length == 0) {
            addChunkPacket(pos.getFloorX() >> 4, pos.getFloorZ() >> 4, packet);
        } else {
            Server.broadcastPacket(players, packet);
        }
    }

    public void addSoundToViewers(Vector3 pos, cn.nukkit.level.Sound sound) {
        PlaySoundPacket packet = new PlaySoundPacket();
        packet.name = sound.getSound();
        packet.volume = 1f;
        packet.pitch = 1f;
        packet.x = pos.getFloorX();
        packet.y = pos.getFloorY();
        packet.z = pos.getFloorZ();
        addChunkPacket(pos.getFloorX() >> 4, pos.getFloorZ() >> 4, packet);
    }

    public void addSound(Vector3 pos, cn.nukkit.level.Sound sound) {
        this.addSound(pos, sound, 1, 1, (Player[]) null);
    }

    public void addSound(Vector3 pos, cn.nukkit.level.Sound sound, float volume, float pitch) {
        this.addSound(pos, sound, volume, pitch, (Player[]) null);
    }

    public void addSound(Vector3 pos, cn.nukkit.level.Sound sound, float volume, float pitch, Collection<Player> players) {
        this.addSound(pos, sound, volume, pitch, players.toArray(new Player[0]));
    }

    public void addSound(Vector3 pos, cn.nukkit.level.Sound sound, float volume, float pitch, Player... players) {
        Preconditions.checkArgument(volume >= 0 && volume <= 1, "Sound volume must be between 0 and 1");
        Preconditions.checkArgument(pitch >= 0, "Sound pitch must be higher than 0");

        PlaySoundPacket packet = new PlaySoundPacket();
        packet.name = sound.getSound();
        packet.volume = volume;
        packet.pitch = pitch;
        packet.x = pos.getFloorX();
        packet.y = pos.getFloorY();
        packet.z = pos.getFloorZ();

        if (players == null || players.length == 0) {
            addChunkPacket(pos.getFloorX() >> 4, pos.getFloorZ() >> 4, packet);
        } else {
            Server.broadcastPacket(players, packet);
        }
    }

    public void addSound(Sound sound) {
        this.addSound(sound, (Player[]) null);
    }

    public void addSound(Sound sound, Player player) {
        this.addSound(sound, new Player[]{player});
    }

    public void addSound(Sound sound, Player[] players) {
        DataPacket packet = sound.encode();
        if (packet != null) {
            if (players == null) {
                this.addChunkPacket((int) sound.x >> 4, (int) sound.z >> 4, packet);
            } else {
                Server.broadcastPacket(players, packet);
            }
        }
    }

    public void addSound(Sound sound, Collection<Player> players) {
        this.addSound(sound, players.toArray(new Player[0]));
    }

    public void addLevelSoundEvent(Vector3 pos, int type, int data, int entityType) {
        this.addLevelSoundEvent(pos, type, data, entityType, null);
    }

    public void addLevelSoundEvent(Vector3 pos, int type, int data, int entityType, Player[] players) {
        this.addLevelSoundEvent(pos, type, data, entityType, false, false, players);
    }

    public void addLevelSoundEvent(Vector3 pos, int type, int data, int entityType, boolean isBaby, boolean isGlobal) {
        this.addLevelSoundEvent(pos, type, data, entityType, isBaby, isGlobal, null);
    }

    public void addLevelSoundEvent(Vector3 pos, int type, int data, int entityType, boolean isBaby, boolean isGlobal, Player[] players) {
        String identifier = AddEntityPacket.LEGACY_IDS.getOrDefault(entityType, ":");
        this.addLevelSoundEvent(pos, type, data, identifier, isBaby, isGlobal, players);
    }

    public void addLevelSoundEvent(Vector3 pos, int type) {
        this.addLevelSoundEvent(pos, type, null);
    }

    public void addLevelSoundEvent(Vector3 pos, int type, Player[] players) {
        this.addLevelSoundEvent(pos, type, -1, players);
    }

    public void addLevelSoundEvent(int type, int pitch, int data, Vector3 pos) {
        this.addLevelSoundEvent(type, pitch, data, pos, null);
    }

    public void addLevelSoundEvent(int type, int pitch, int data, Vector3 pos, Player[] players) {
        this.addLevelSoundEvent(pos, type, data, ":", false, false, players);
    }

    public void addLevelSoundEvent(Vector3 pos, int type, int data) {
        this.addLevelSoundEvent(pos, type, data, null);
    }

    public void addLevelSoundEvent(Vector3 pos, int type, int data, Player[] players) {
        this.addLevelSoundEvent(pos, type, data, ":", false, false, players);
    }

    public void addLevelSoundEvent(Vector3 pos, int type, int data, String identifier, boolean isBaby, boolean isGlobal) {
        this.addLevelSoundEvent(pos, type, data, identifier, isBaby, isGlobal, null);
    }

    public void addLevelSoundEvent(Vector3 pos, int type, int data, String identifier, boolean isBaby, boolean isGlobal, Player[] players) {
        LevelSoundEventPacket pk = new LevelSoundEventPacket();
        pk.sound = type;
        pk.extraData = data;
        pk.entityIdentifier = identifier;
        pk.x = (float) pos.x;
        pk.y = (float) pos.y;
        pk.z = (float) pos.z;
        pk.isGlobal = isGlobal;
        pk.isBabyMob = isBaby;

        if (players == null) {
            this.addChunkPacket(pos.getFloorX() >> 4, pos.getFloorZ() >> 4, pk);
        } else {
            Server.broadcastPacket(players, pk);
        }
    }

    public void addLevelSoundEvent(Vector3 pos, int type, int pitch, int data, boolean isGlobal) {
        LevelSoundEventPacketV1 pk = new LevelSoundEventPacketV1();
        pk.sound = type;
        pk.pitch = pitch;
        pk.extraData = data;
        pk.x = (float) pos.x;
        pk.y = (float) pos.y;
        pk.z = (float) pos.z;
        pk.isGlobal = isGlobal;

        this.addChunkPacket(pos.getFloorX() >> 4, pos.getFloorZ() >> 4, pk);
    }

    public void addParticle(Particle particle) {
        this.addParticle(particle, (Player[]) null);
    }

    public void addParticle(Particle particle, Player player) {
        this.addParticle(particle, new Player[]{player});
    }

    public void addParticle(Particle particle, Player[] players) {
        addParticle(particle, players, 1);
    }

    public void addParticle(Particle particle, Player[] players, int count) {
        Int2ObjectMap<ObjectList<Player>> targets;
        if (players == null) {
            targets = Server.sortPlayers(this.getChunkPlayers(particle.getChunkX(), particle.getChunkZ()).values());
        } else {
            targets = Server.sortPlayers(players);
        }

        for (int protocolId : targets.keySet()) {
            ObjectList<Player> protocolPlayers = targets.get(protocolId);
            DataPacket[] packets = particle.mvEncode(protocolId);
            if (packets != null) {
                if (count == 1) {
                    Server.broadcastPackets(protocolPlayers.toArray(new Player[0]), packets);
                    continue;
                }

                List<DataPacket> packetList = Arrays.asList(packets);
                List<DataPacket> sendList = new ObjectArrayList<>();
                for (int i = 0; i < count; i++) {
                    sendList.addAll(packetList);
                }
                Server.broadcastPackets(protocolPlayers.toArray(new Player[0]), sendList.toArray(new DataPacket[0]));
            }
        }
    }

    public void addParticle(Particle particle, Collection<Player> players) {
        this.addParticle(particle, players.toArray(new Player[0]));
    }

    public void addParticleEffect(Vector3 pos, ParticleEffect particleEffect) {
        this.addParticleEffect(pos, particleEffect, -1, this.dimension, (Player[]) null);
    }

    public void addParticleEffect(Vector3 pos, ParticleEffect particleEffect, long uniqueEntityId) {
        this.addParticleEffect(pos, particleEffect, uniqueEntityId, this.dimension, (Player[]) null);
    }

    public void addParticleEffect(Vector3 pos, ParticleEffect particleEffect, long uniqueEntityId, int dimensionId) {
        this.addParticleEffect(pos, particleEffect, uniqueEntityId, dimensionId, (Player[]) null);
    }

    public void addParticleEffect(Vector3 pos, ParticleEffect particleEffect, long uniqueEntityId, int dimensionId, Collection<Player> players) {
        this.addParticleEffect(pos, particleEffect, uniqueEntityId, dimensionId, players.toArray(new Player[0]));
    }

    public void addParticleEffect(Vector3 pos, ParticleEffect particleEffect, long uniqueEntityId, int dimensionId, Player... players) {
        this.addParticleEffect(pos.asVector3f(), particleEffect.getIdentifier(), uniqueEntityId, dimensionId, players);
    }

    public void addParticleEffect(Vector3f pos, String identifier, long uniqueEntityId, int dimensionId, Player... players) {
        SpawnParticleEffectPacket pk = new SpawnParticleEffectPacket();
        pk.identifier = identifier;
        pk.uniqueEntityId = uniqueEntityId;
        pk.dimensionId = dimensionId;
        pk.position = pos;

        if (players == null || players.length == 0) {
            addChunkPacket(pos.getFloorX() >> 4, pos.getFloorZ() >> 4, pk);
        } else {
            Server.broadcastPacket(players, pk);
        }
    }

    public boolean getAutoSave() {
        return this.autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public boolean unload() {
        return this.unload(false);
    }

    public boolean unload(boolean force) {
        LevelUnloadEvent ev = new LevelUnloadEvent(this);

        if (this == this.server.getDefaultLevel() && !force) {
            ev.setCancelled();
        }

        this.server.getPluginManager().callEvent(ev);

        if (!force && ev.isCancelled()) {
            return false;
        }

        this.server.getLogger().info(this.server.getLanguage().translateString("nukkit.level.unloading",
                TextFormat.GREEN + this.getName() + TextFormat.WHITE));
        Level defaultLevel = this.server.getDefaultLevel();

        for (Player player : new ArrayList<>(this.getPlayers().values())) {
            if (this == defaultLevel || defaultLevel == null) {
                player.close(player.getLeaveMessage(), "Forced default level unload");
            } else {
                player.teleport(this.server.getDefaultLevel().getSafeSpawn());
            }
        }

        if (this == defaultLevel) {
            this.server.setDefaultLevel(null);
        }

        this.close();

        return true;
    }

    public Map<Integer, Player> getChunkPlayers(int chunkX, int chunkZ) {
        long index = Level.chunkHash(chunkX, chunkZ);
        Map<Integer, Player> map = this.playerLoaders.get(index);
        if (map != null) {
            return new HashMap<>(map);
        } else {
            return new HashMap<>();
        }
    }

    public ChunkLoader[] getChunkLoaders(int chunkX, int chunkZ) {
        long index = Level.chunkHash(chunkX, chunkZ);
        Map<Integer, ChunkLoader> map = this.chunkLoaders.get(index);
        if (map != null) {
            return map.values().toArray(new ChunkLoader[0]);
        } else {
            return new ChunkLoader[0];
        }
    }

    public void addChunkPacket(int chunkX, int chunkZ, DataPacket packet) {
        long index = Level.chunkHash(chunkX, chunkZ);
        synchronized (chunkPackets) {
            Deque<DataPacket> packets = chunkPackets.computeIfAbsent(index, i -> new ArrayDeque<>());
            packets.add(packet);
        }
    }

    public void registerChunkLoader(ChunkLoader loader, int chunkX, int chunkZ) {
        this.registerChunkLoader(loader, chunkX, chunkZ, true);
    }

    public void registerChunkLoader(ChunkLoader loader, int chunkX, int chunkZ, boolean autoLoad) {
        int hash = loader.getLoaderId();
        long index = Level.chunkHash(chunkX, chunkZ);

        Map<Integer, ChunkLoader> map = this.chunkLoaders.get(index);
        if (map == null) {
            Map<Integer, ChunkLoader> newChunkLoader = new HashMap<>();
            newChunkLoader.put(hash, loader);
            this.chunkLoaders.put(index, newChunkLoader);
            Map<Integer, Player> newPlayerLoader = new HashMap<>();
            if (loader instanceof Player) {
                newPlayerLoader.put(hash, (Player) loader);
            }
            this.playerLoaders.put(index, newPlayerLoader);
        } else if (map.containsKey(hash)) {
            return;
        } else {
            map.put(hash, loader);
            if (loader instanceof Player) {
                this.playerLoaders.get(index).put(hash, (Player) loader);
            }
        }

        if (!this.loaders.containsKey(hash)) {
            this.loaderCounter.put(hash, 1);
            this.loaders.put(hash, loader);
        } else {
            this.loaderCounter.put(hash, this.loaderCounter.get(hash) + 1);
        }

        this.cancelUnloadChunkRequest(hash);

        if (autoLoad) {
            this.loadChunk(chunkX, chunkZ);
        }
    }

    public void unregisterChunkLoader(ChunkLoader loader, int chunkX, int chunkZ) {
        int hash = loader.getLoaderId();
        long index = Level.chunkHash(chunkX, chunkZ);
        Map<Integer, ChunkLoader> chunkLoadersIndex = this.chunkLoaders.get(index);
        if (chunkLoadersIndex != null) {
            ChunkLoader oldLoader = chunkLoadersIndex.remove(hash);
            if (oldLoader != null) {
                if (chunkLoadersIndex.isEmpty()) {
                    this.chunkLoaders.remove(index);
                    this.playerLoaders.remove(index);
                    this.unloadChunkRequest(chunkX, chunkZ, true);
                } else {
                    Map<Integer, Player> playerLoadersIndex = this.playerLoaders.get(index);
                    playerLoadersIndex.remove(hash);
                }

                int count = this.loaderCounter.get(hash);
                if (--count == 0) {
                    this.loaderCounter.remove(hash);
                    this.loaders.remove(hash);
                } else {
                    this.loaderCounter.put(hash, count);
                }
            }
        }
    }

    public void checkTime() {
        if (!this.stopTime && this.gameRules.getBoolean(GameRule.DO_DAYLIGHT_CYCLE)) {
            this.time = (this.time + tickRate) % TIME_FULL;
        }
    }

    public void sendTime(Player... players) {
        SetTimePacket pk = new SetTimePacket();
        pk.time = this.time;

        Server.broadcastPacket(players, pk);
    }

    public void sendTime() {
        sendTime(this.players.values().toArray(new Player[0]));
    }

    public GameRules getGameRules() {
        return gameRules;
    }

    @SuppressWarnings("unchecked")
    public void doTick(int currentTick) {
        if (this.timings.doTick != null) this.timings.doTick.startTiming();

        updateBlockLight(lightQueue);
        this.checkTime();
        
        if (/*stopTime || !this.gameRules.getBoolean(GameRule.DO_DAYLIGHT_CYCLE) ||*/ currentTick % 6000 == 0) { // Keep the time in sync
            this.sendTime();
        }

        // Tick Weather
        if (this.dimension != DIMENSION_NETHER && this.dimension != DIMENSION_THE_END && this.gameRules.getBoolean(GameRule.DO_WEATHER_CYCLE) && this.randomTickingEnabled()) {
            this.rainTime--;
            if (this.rainTime <= 0) {
                if (!this.setRaining(!this.raining)) {
                    if (this.raining) {
                        setRainTime(Utils.random.nextInt(12000) + 12000);
                    } else {
                        setRainTime(Utils.random.nextInt(168000) + 12000);
                    }
                }
            }

            this.thunderTime--;
            if (this.thunderTime <= 0) {
                if (!this.setThundering(!this.thundering)) {
                    if (this.thundering) {
                        setThunderTime(Utils.random.nextInt(12000) + 3600);
                    } else {
                        setThunderTime(Utils.random.nextInt(168000) + 12000);
                    }
                }
            }

            if (this.isThundering()) {
                Map<Long, ? extends FullChunk> chunks = getChunks();
                if (chunks instanceof Long2ObjectOpenHashMap) {
                    @SuppressWarnings("rawtypes")
                    Long2ObjectOpenHashMap<? extends FullChunk> fastChunks = (Long2ObjectOpenHashMap) chunks;
                    ObjectIterator<? extends Long2ObjectMap.Entry<? extends FullChunk>> iter = fastChunks.long2ObjectEntrySet().fastIterator();
                    while (iter.hasNext()) {
                        Long2ObjectMap.Entry<? extends FullChunk> entry = iter.next();
                        performThunder(entry.getLongKey(), entry.getValue());
                    }
                } else {
                    for (Map.Entry<Long, ? extends FullChunk> entry : getChunks().entrySet()) {
                        performThunder(entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        if (Server.getInstance().lightUpdates) {
            this.skyLightSubtracted = this.calculateSkylightSubtracted(1);
        }

        this.levelCurrentTick++;

        this.unloadChunks();

        if (this.timings.doTickPending != null) this.timings.doTickPending.startTiming();
        this.updateQueue.tick(this.levelCurrentTick);
        if (this.timings.doTickPending != null) this.timings.doTickPending.stopTiming();
        
        Block block;
        while ((block = this.normalUpdateQueue.poll()) != null) {
            block.onUpdate(BLOCK_UPDATE_NORMAL);
        }

        TimingsHistory.entityTicks += this.updateEntities.size();
        if (this.timings.entityTick != null) this.timings.entityTick.startTiming();

        if (!this.updateEntities.isEmpty()) {
            for (long id : new ArrayList<>(this.updateEntities.keySet())) {
                Entity entity = this.updateEntities.get(id);
                if (entity == null) {
                    this.updateEntities.remove(id);
                    continue;
                }
                if (entity.closed || !entity.onUpdate(currentTick)) {
                    this.updateEntities.remove(id);
                }
            }
        }
        if (this.timings.entityTick != null) this.timings.entityTick.stopTiming();

        TimingsHistory.tileEntityTicks += this.updateBlockEntities.size();
        if (this.timings.blockEntityTick != null) this.timings.blockEntityTick.startTiming();
        this.updateBlockEntities.removeIf(blockEntity -> !blockEntity.isValid() || !blockEntity.onUpdate());
        if (this.timings.blockEntityTick != null) this.timings.blockEntityTick.stopTiming();

        if (this.timings.tickChunks != null) this.timings.tickChunks.startTiming();
        this.tickChunks();
        if (this.timings.tickChunks != null)  this.timings.tickChunks.stopTiming();

        synchronized (changedBlocks) {
            if (!this.changedBlocks.isEmpty()) {
                if (!this.players.isEmpty()) {
                    ObjectIterator<Long2ObjectMap.Entry<SoftReference<Map<Character, Object>>>> iter = changedBlocks.long2ObjectEntrySet().fastIterator();
                    while (iter.hasNext()) {
                        Long2ObjectMap.Entry<SoftReference<Map<Character, Object>>> entry = iter.next();
                        long index = entry.getLongKey();
                        Map<Character, Object> blocks = entry.getValue().get();
                        int chunkX = Level.getHashX(index);
                        int chunkZ = Level.getHashZ(index);
                        if (blocks == null || blocks.size() > MAX_BLOCK_CACHE) {
                            FullChunk chunk = this.getChunk(chunkX, chunkZ);
                            for (Player p : this.getChunkPlayers(chunkX, chunkZ).values()) {
                                p.onChunkChanged(chunk);
                            }
                        } else {
                            Player[] playerArray = this.getChunkPlayers(chunkX, chunkZ).values().toArray(new Player[0]);
                            Vector3[] blocksArray = new Vector3[blocks.size()];
                            int i = 0;
                            for (char blockHash : blocks.keySet()) {
                                Vector3 hash = getBlockXYZ(index, blockHash);
                                blocksArray[i++] = hash;
                            }
                            this.sendBlocks(playerArray, blocksArray, UpdateBlockPacket.FLAG_ALL);
                        }
                    }
                }
                this.changedBlocks.clear();
            }
        }

        if (this.server.asyncChunks) {
            this.server.getScheduler().scheduleAsyncTask(new AsyncTask() {
                @Override
                public void onRun() {
                    processChunkRequest();
                }
            });
        }else {
            this.processChunkRequest();
        }

        if (this.sleepTicks > 0 && --this.sleepTicks <= 0) {
            this.checkSleep();
        }

        synchronized (chunkPackets) {
            for (long index : this.chunkPackets.keySet()) {
                int chunkX = Level.getHashX(index);
                int chunkZ = Level.getHashZ(index);
                Map<Integer, Player> map = this.getChunkPlayers(chunkX, chunkZ);
                if (!map.isEmpty()) {
                    Player[] chunkPlayers = map.values().toArray(new Player[0]);
                    for (DataPacket pk : this.chunkPackets.get(index)) {
                        Server.broadcastPacket(chunkPlayers, pk);
                    }
                }
            }
            this.chunkPackets.clear();
        }

        if (gameRules.isStale()) {
            GameRulesChangedPacket packet = new GameRulesChangedPacket();
            packet.gameRulesMap = gameRules.getGameRules();
            Server.broadcastPacket(players.values().toArray(new Player[0]), packet);
            gameRules.refresh();
        }

        if (this.timings.doTick != null) this.timings.doTick.stopTiming();
    }

    private void performThunder(long index, FullChunk chunk) {
        if (areNeighboringChunksLoaded(index)) return;
        if (Utils.random.nextInt(10000) == 0) {
            int LCG = this.getUpdateLCG() >> 2;

            int chunkX = chunk.getX() << 4;
            int chunkZ = chunk.getZ() << 4;
            Vector3 vector = this.adjustPosToNearbyEntity(new Vector3(chunkX + (LCG & 0xf), 0, chunkZ + (LCG >> 8 & 0xf)));

            Biome biome = Biome.getBiome(this.getBiomeId(vector.getFloorX(), vector.getFloorZ()));
            if (!biome.canRain()) {
                return;
            }

            int bId = this.getBlockIdAt(vector.getFloorX(), vector.getFloorY(), vector.getFloorZ());
            if (bId != Block.TALL_GRASS && bId != Block.WATER)
                vector.y += 1;
            CompoundTag nbt = new CompoundTag()
                    .putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", vector.x))
                            .add(new DoubleTag("", vector.y)).add(new DoubleTag("", vector.z)))
                    .putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0))
                            .add(new DoubleTag("", 0)).add(new DoubleTag("", 0)))
                    .putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0))
                            .add(new FloatTag("", 0)));

            EntityLightning bolt = new EntityLightning(chunk, nbt);
            LightningStrikeEvent ev = new LightningStrikeEvent(this, bolt);
            server.getPluginManager().callEvent(ev);
            if (!ev.isCancelled()) {
                bolt.spawnToAll();
                this.addLevelSoundEvent(vector, LevelSoundEventPacket.SOUND_THUNDER, -1, EntityLightning.NETWORK_ID);
                this.addLevelSoundEvent(vector, LevelSoundEventPacket.SOUND_EXPLODE, -1, EntityLightning.NETWORK_ID);
            } else {
                bolt.setEffect(false);
            }
        }
    }

    public Vector3 adjustPosToNearbyEntity(Vector3 pos) {
        pos.y = this.getHighestBlockAt(pos.getFloorX(), pos.getFloorZ());
        AxisAlignedBB axisalignedbb = new AxisAlignedBB(pos.x, pos.y, pos.z, pos.getX(), 255, pos.getZ()).expand(3, 3, 3);
        List<Entity> list = new ArrayList<>();

        for (Entity entity : this.getCollidingEntities(axisalignedbb)) {
            if (entity.isAlive() && entity.canSeeSky()) {
                list.add(entity);
            }
        }

        if (!list.isEmpty()) {
            return list.get(Utils.random.nextInt(list.size())).getPosition();
        } else {
            if (pos.getY() == -1) {
                pos = pos.up(2);
            }

            return pos;
        }
    }

    public void checkSleep() {
        if (this.players.isEmpty()) {
            return;
        }

        boolean resetTime = true;
        for (Player p : this.getPlayers().values()) {
            if (!p.isSleeping()) {
                resetTime = false;
                break;
            }
        }

        if (resetTime) {
            int time = this.getTime() % Level.TIME_FULL;

            if (time >= Level.TIME_NIGHT && time < Level.TIME_SUNRISE) {
                this.setTime(this.getTime() + Level.TIME_FULL - time);

                for (Player p : this.getPlayers().values()) {
                    p.stopSleep();
                }
            }
        }
    }

    public void sendBlockExtraData(int x, int y, int z, int id, int data) {
        this.sendBlockExtraData(x, y, z, id, data, this.getChunkPlayers(x >> 4, z >> 4).values());
    }

    public void sendBlockExtraData(int x, int y, int z, int id, int data, Collection<Player> players) {
        this.sendBlockExtraData(x, y, z, id, data, players.toArray(new Player[0]));
    }

    public void sendBlockExtraData(int x, int y, int z, int id, int data, Player[] players) {
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = LevelEventPacket.EVENT_SET_DATA;
        pk.x = x + 0.5f;
        pk.y = y + 0.5f;
        pk.z = z + 0.5f;
        pk.data = (data << 8) | id;

        Server.broadcastPacket(players, pk);
    }

    public void sendBlocks(Player[] target, Vector3[] blocks) {
        this.sendBlocks(target, blocks, UpdateBlockPacket.FLAG_NONE);
    }

    public void sendBlocks(Player[] target, Vector3[] blocks, int flags) {
        this.sendBlocks(target, blocks, flags, false);
    }

    public void sendBlocks(Player[] target, Vector3[] blocks, int flags, boolean optimizeRebuilds) {
        LongSet chunks = null;
        if (optimizeRebuilds) {
            chunks = new LongOpenHashSet();
        }

        Int2ObjectMap<ObjectList<Player>> targets = Server.sortPlayers(target);
        for (Vector3 b : blocks) {
            if (b == null) {
                continue;
            }
            boolean first = !optimizeRebuilds;

            if (optimizeRebuilds) {
                long index = Level.chunkHash((int) b.x >> 4, (int) b.z >> 4);
                if (!chunks.contains(index)) {
                    chunks.add(index);
                    first = true;
                }
            }
            UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
            updateBlockPacket.x = (int) b.x;
            updateBlockPacket.y = (int) b.y;
            updateBlockPacket.z = (int) b.z;
            updateBlockPacket.flags = first ? flags : UpdateBlockPacket.FLAG_NONE;

            for (int protocolId : targets.keySet()) {
                ObjectList<Player> players = targets.get(protocolId);
                UpdateBlockPacket packet = (UpdateBlockPacket) updateBlockPacket.clone();
                try {
                    if (protocolId > 201) {
                        if (b instanceof Block) {
                            packet.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(protocolId, ((Block) b).getFullId());
                        } else {
                            packet.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(protocolId, getFullBlock((int) b.x, (int) b.y, (int) b.z));
                        }
                    } else {
                        Block bl = b instanceof Block ? (Block) b : getBlock((int) b.x, (int) b.y, (int) b.z);
                        packet.blockId = bl.getId();
                        packet.blockData = bl.getDamage();
                    }
                } catch (NoSuchElementException e) {
                    throw new IllegalStateException("Unable to create BlockUpdatePacket at (" + b.x + ", " + b.y + ", " + b.z + ") in " + getName() + " for players with protocol " +protocolId);
                }

                for (Player player : players) {
                    player.batchDataPacket(packet);
                }
            }
        }
    }

    public void sendBlocks(Player target, Vector3[] blocks, int flags) {
        for (Vector3 b : blocks) {
            if (b == null) {
                continue;
            }

            UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
            updateBlockPacket.x = (int) b.x;
            updateBlockPacket.y = (int) b.y;
            updateBlockPacket.z = (int) b.z;
            updateBlockPacket.flags = flags;

            try {
                if (target.protocol > 201) {
                    if (b instanceof Block) {
                        updateBlockPacket.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(target.protocol, ((Block) b).getFullId());
                    } else {
                        updateBlockPacket.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(target.protocol, getFullBlock((int) b.x, (int) b.y, (int) b.z));
                    }
                } else {
                    Block bl = b instanceof Block ? (Block) b : getBlock((int) b.x, (int) b.y, (int) b.z);
                    updateBlockPacket.blockId = bl.getId();
                    updateBlockPacket.blockData = bl.getDamage();
                }
            } catch (NoSuchElementException e) {
                throw new IllegalStateException("Unable to create BlockUpdatePacket at (" + b.x + ", " + b.y + ", " + b.z + ") in " + getName() + " for player " + target.getName() + " with protocol " + target.protocol);
            }

            target.batchDataPacket(updateBlockPacket);
        }
    }

    private void tickChunks() {
        if (this.chunksPerTicks <= 0 || this.loaders.isEmpty()) {
            this.chunkTickList.clear();
            return;
        }

        int chunksPerLoader = Math.min(200, Math.max(1, (int) (((double) (this.chunksPerTicks - this.loaders.size()) / this.loaders.size() + 0.5))));
        int randRange = 3 + chunksPerLoader / 30;
        randRange = Math.min(randRange, this.chunkTickRadius);

        for (ChunkLoader loader : this.loaders.values()) {
            int chunkX = (int) loader.getX() >> 4;
            int chunkZ = (int) loader.getZ() >> 4;

            long index = Level.chunkHash(chunkX, chunkZ);
            int existingLoaders = Math.max(0, this.chunkTickList.getOrDefault(index, 0));
            this.chunkTickList.put(index, existingLoaders + 1);
            for (int chunk = 0; chunk < chunksPerLoader; ++chunk) {
                int dx = Utils.random.nextInt(randRange << 1) - randRange;
                int dz = Utils.random.nextInt(randRange << 1) - randRange;
                long hash = Level.chunkHash(dx + chunkX, dz + chunkZ);
                if (!this.chunkTickList.containsKey(hash) && provider.isChunkLoaded(hash)) {
                    this.chunkTickList.put(hash, -1);
                }
            }
        }

        int blockTest = 0;

        if (!chunkTickList.isEmpty()) {
            ObjectIterator<Long2IntMap.Entry> iter = chunkTickList.long2IntEntrySet().iterator();
            while (iter.hasNext()) {
                Long2IntMap.Entry entry = iter.next();
                long index = entry.getLongKey();
                if (!areNeighboringChunksLoaded(index)) {
                    iter.remove();
                    continue;
                }

                int loaders = entry.getIntValue();

                int chunkX = getHashX(index);
                int chunkZ = getHashZ(index);

                FullChunk chunk;
                if ((chunk = this.getChunk(chunkX, chunkZ, false)) == null) {
                    iter.remove();
                    continue;
                } else if (loaders <= 0) {
                    iter.remove();
                }

                for (Entity entity : chunk.getEntities().values()) {
                    entity.scheduleUpdate();
                }

                if (this.randomTickingEnabled()) {
                    if (this.useSections) {
                        for (ChunkSection section : ((Chunk) chunk).getSections()) {
                            if (!(section instanceof EmptyChunkSection)) {
                                int Y = section.getY();
                                for (int i = 0; i < gameRules.getInteger(GameRule.RANDOM_TICK_SPEED); ++i) {
                                    int lcg = this.getUpdateLCG();
                                    int x = lcg & 0x0f;
                                    int y = lcg >>> 8 & 0x0f;
                                    int z = lcg >>> 16 & 0x0f;

                                    int fullId = section.getFullBlock(x, y, z);
                                    int blockId = fullId >> 4;
                                    if (randomTickBlocks[blockId]) {
                                        Block block = Block.get(fullId, this, (chunkX << 4) + x, (Y << 4) + y, (chunkZ << 4) + z);
                                        block.onUpdate(BLOCK_UPDATE_RANDOM);
                                    }
                                }
                            }
                        }
                    } else {
                        for (int Y = 0; Y < 8 && (Y < 3 || blockTest != 0); ++Y) {
                            blockTest = 0;
                            for (int i = 0; i < gameRules.getInteger(GameRule.RANDOM_TICK_SPEED); ++i) {
                                int lcg = this.getUpdateLCG();
                                int x = lcg & 0x0f;
                                int y = lcg >>> 8 & 0x0f;
                                int z = lcg >>> 16 & 0x0f;

                                int fullId = chunk.getFullBlock(x, y + (Y << 4), z);
                                int blockId = fullId >> 4;
                                blockTest |= fullId;
                                if (randomTickBlocks[blockId]) {
                                    Block block = Block.get(fullId, this, x, y + (Y << 4), z);
                                    block.onUpdate(BLOCK_UPDATE_RANDOM);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (this.clearChunksOnTick) {
            this.chunkTickList.clear();
        }
    }

    public boolean save() {
        return this.save(false);
    }

    public boolean save(boolean force) {
        if (!this.autoSave && !force) {
            return false;
        }

        this.server.getPluginManager().callEvent(new LevelSaveEvent(this));

        this.provider.setTime(this.time);
        this.provider.setRaining(this.raining);
        this.provider.setRainTime(this.rainTime);
        this.provider.setThundering(this.thundering);
        this.provider.setThunderTime(this.thunderTime);
        this.provider.setCurrentTick(this.levelCurrentTick);
        this.provider.setGameRules(this.gameRules);
        this.saveChunks();
        if (this.provider instanceof BaseLevelProvider) {
            this.provider.saveLevelData();
        }

        return true;
    }

    public void saveChunks() {
        provider.saveChunks();
    }

    public void updateAroundRedstone(Vector3 pos, BlockFace face) {
        for (BlockFace side : BlockFace.values()) {
            if (face != null /*&&*/|| side == face) {
                continue;
            }

            this.getBlock(pos.getSide(side)).onUpdate(BLOCK_UPDATE_REDSTONE);
        }
    }

    public void updateComparatorOutputLevel(Vector3 v) {
        for (BlockFace face : Plane.HORIZONTAL) {
            Vector3 pos = v.getSide(face);

            if (this.isChunkLoaded((int) pos.x >> 4, (int) pos.z >> 4)) {
                Block block1 = this.getBlock(pos);

                if (BlockRedstoneDiode.isDiode(block1)) {
                    block1.onUpdate(BLOCK_UPDATE_REDSTONE);
                } else if (block1.isNormalBlock()) {
                    pos = pos.getSide(face);
                    block1 = this.getBlock(pos);

                    if (BlockRedstoneDiode.isDiode(block1)) {
                        block1.onUpdate(BLOCK_UPDATE_REDSTONE);
                    }
                }
            }
        }
    }

    public void updateAround(Vector3 pos) {
        updateAround((int) pos.x, (int) pos.y, (int) pos.z);
    }

    public void updateAround(int x, int y, int z) {
        BlockUpdateEvent ev;
        this.server.getPluginManager().callEvent(
                ev = new BlockUpdateEvent(this.getBlock(x, y - 1, z)));
        if (!ev.isCancelled()) {
            normalUpdateQueue.add(ev.getBlock());
        }

        this.server.getPluginManager().callEvent(
                ev = new BlockUpdateEvent(this.getBlock(x, y + 1, z)));
        if (!ev.isCancelled()) {
            normalUpdateQueue.add(ev.getBlock());
        }

        this.server.getPluginManager().callEvent(
                ev = new BlockUpdateEvent(this.getBlock(x - 1, y, z)));
        if (!ev.isCancelled()) {
            normalUpdateQueue.add(ev.getBlock());
        }

        this.server.getPluginManager().callEvent(
                ev = new BlockUpdateEvent(this.getBlock(x + 1, y, z)));
        if (!ev.isCancelled()) {
            normalUpdateQueue.add(ev.getBlock());
        }

        this.server.getPluginManager().callEvent(
                ev = new BlockUpdateEvent(this.getBlock(x, y, z - 1)));
        if (!ev.isCancelled()) {
            normalUpdateQueue.add(ev.getBlock());
        }

        this.server.getPluginManager().callEvent(
                ev = new BlockUpdateEvent(this.getBlock(x, y, z + 1)));
        if (!ev.isCancelled()) {
            normalUpdateQueue.add(ev.getBlock());
        }
    }

    public void scheduleUpdate(Block pos, int delay) {
        this.scheduleUpdate(pos, pos, delay, 0, true);
    }

    public void scheduleUpdate(Block block, Vector3 pos, int delay) {
        this.scheduleUpdate(block, pos, delay, 0, true);
    }

    public void scheduleUpdate(Block block, Vector3 pos, int delay, int priority) {
        this.scheduleUpdate(block, pos, delay, priority, true);
    }

    public void scheduleUpdate(Block block, Vector3 pos, int delay, int priority, boolean checkArea) {
        if (block.getId() == 0 || (checkArea && !this.isChunkLoaded(block.getFloorX() >> 4, block.getFloorZ() >> 4))) {
            return;
        }

        BlockUpdateEntry entry = new BlockUpdateEntry(pos.floor(), block, ((long) delay) + levelCurrentTick, priority);

        if (!this.updateQueue.contains(entry)) {
            this.updateQueue.add(entry);
        }
    }

    public boolean cancelSheduledUpdate(Vector3 pos, Block block) {
        return this.updateQueue.remove(new BlockUpdateEntry(pos, block));
    }

    public boolean isUpdateScheduled(Vector3 pos, Block block) {
        return this.updateQueue.contains(new BlockUpdateEntry(pos, block));
    }

    public boolean isBlockTickPending(Vector3 pos, Block block) {
        return this.updateQueue.isBlockTickPending(pos, block);
    }

    public Set<BlockUpdateEntry> getPendingBlockUpdates(FullChunk chunk) {
        int minX = (chunk.getX() << 4) - 2;
        int maxX = minX + 18;
        int minZ = (chunk.getZ() << 4) - 2;
        int maxZ = minZ + 18;

        return this.getPendingBlockUpdates(new AxisAlignedBB(minX, 0, minZ, maxX, 256, maxZ));
    }

    public Set<BlockUpdateEntry> getPendingBlockUpdates(AxisAlignedBB boundingBox) {
        return updateQueue.getPendingBlockUpdates(boundingBox);
    }

    public Block[] getCollisionBlocks(AxisAlignedBB bb) {
        return this.getCollisionBlocks(bb, false);
    }

    public Block[] getCollisionBlocks(AxisAlignedBB bb, boolean targetFirst) {
        int minX = NukkitMath.floorDouble(bb.minX);
        int minY = NukkitMath.floorDouble(bb.minY);
        int minZ = NukkitMath.floorDouble(bb.minZ);
        int maxX = NukkitMath.ceilDouble(bb.maxX);
        int maxY = NukkitMath.ceilDouble(bb.maxY);
        int maxZ = NukkitMath.ceilDouble(bb.maxZ);

        List<Block> collides = new ArrayList<>();

        if (targetFirst) {
            for (int z = minZ; z <= maxZ; ++z) {
                for (int x = minX; x <= maxX; ++x) {
                    for (int y = minY; y <= maxY; ++y) {
                        Block block = this.getBlock(x, y, z, false);
                        if (block != null && block.getId() != 0 && block.collidesWithBB(bb)) {
                            return new Block[]{block};
                        }
                    }
                }
            }
        } else {
            for (int z = minZ; z <= maxZ; ++z) {
                for (int x = minX; x <= maxX; ++x) {
                    for (int y = minY; y <= maxY; ++y) {
                        Block block = this.getBlock(x, y, z, false);
                        if (block != null && block.getId() != 0 && block.collidesWithBB(bb)) {
                            collides.add(block);
                        }
                    }
                }
            }
        }

        return collides.toArray(new Block[0]);
    }

    public boolean hasCollisionBlocks(AxisAlignedBB bb) {
        int minX = NukkitMath.floorDouble(bb.minX);
        int minY = NukkitMath.floorDouble(bb.minY);
        int minZ = NukkitMath.floorDouble(bb.minZ);
        int maxX = NukkitMath.ceilDouble(bb.maxX);
        int maxY = NukkitMath.ceilDouble(bb.maxY);
        int maxZ = NukkitMath.ceilDouble(bb.maxZ);

        for (int z = minZ; z <= maxZ; ++z) {
            for (int x = minX; x <= maxX; ++x) {
                for (int y = minY; y <= maxY; ++y) {
                    Block block = this.getBlock(x, y, z, false);
                    if (block != null && block.getId() != 0 && block.collidesWithBB(bb)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isFullBlock(Vector3 pos) {
        AxisAlignedBB bb;
        if (pos instanceof Block) {
            if (((Block) pos).isSolid()) {
                return true;
            }
            bb = ((Block) pos).getBoundingBox();
        } else {
            bb = this.getBlock(pos).getBoundingBox();
        }

        return bb != null && bb.getAverageEdgeLength() >= 1;
    }

    public AxisAlignedBB[] getCollisionCubes(Entity entity, AxisAlignedBB bb) {
        return this.getCollisionCubes(entity, bb, true);
    }

    public AxisAlignedBB[] getCollisionCubes(Entity entity, AxisAlignedBB bb, boolean entities) {
        return getCollisionCubes(entity, bb, entities, false);
    }

    public AxisAlignedBB[] getCollisionCubes(Entity entity, AxisAlignedBB bb, boolean entities, boolean solidEntities) {
        int minX = NukkitMath.floorDouble(bb.minX);
        int minY = NukkitMath.floorDouble(bb.minY);
        int minZ = NukkitMath.floorDouble(bb.minZ);
        int maxX = NukkitMath.ceilDouble(bb.maxX);
        int maxY = NukkitMath.ceilDouble(bb.maxY);
        int maxZ = NukkitMath.ceilDouble(bb.maxZ);

        List<AxisAlignedBB> collides = new ArrayList<>();

        for (int z = minZ; z <= maxZ; ++z) {
            for (int x = minX; x <= maxX; ++x) {
                for (int y = minY; y <= maxY; ++y) {
                    Block block = this.getBlock(x, y, z, false);
                    if (!block.canPassThrough() && block.collidesWithBB(bb)) {
                        collides.add(block.getBoundingBox());
                    }
                }
            }
        }

        if (entities || solidEntities) {
            for (Entity ent : this.getCollidingEntities(bb.grow(0.25f, 0.25f, 0.25f), entity)) {
                if (solidEntities && !ent.canPassThrough()) {
                    collides.add(ent.boundingBox.clone());
                }
            }
        }

        return collides.toArray(new AxisAlignedBB[0]);
    }

    public boolean hasCollision(Entity entity, AxisAlignedBB bb, boolean entities) {
        int minX = NukkitMath.floorDouble(bb.minX);
        int minY = NukkitMath.floorDouble(bb.minY);
        int minZ = NukkitMath.floorDouble(bb.minZ);
        int maxX = NukkitMath.ceilDouble(bb.maxX);
        int maxY = NukkitMath.ceilDouble(bb.maxY);
        int maxZ = NukkitMath.ceilDouble(bb.maxZ);

        for (int z = minZ; z <= maxZ; ++z) {
            for (int x = minX; x <= maxX; ++x) {
                for (int y = minY; y <= maxY; ++y) {
                    Block block = this.getBlock(x, y, z, false);
                    if (!block.canPassThrough() && block.collidesWithBB(bb)) {
                        return true;
                    }
                }
            }
        }

        if (entities) {
            return this.getCollidingEntities(bb.grow(0.25f, 0.25f, 0.25f), entity).length > 0;
        }
        return false;
    }

    public int getFullLight(Vector3 pos) {
        FullChunk chunk = this.getChunk((int) pos.x >> 4, (int) pos.z >> 4, false);
        int level = 0;
        if (chunk != null) {
            level = chunk.getBlockSkyLight((int) pos.x & 0x0f, (int) pos.y & 0xff, (int) pos.z & 0x0f);
            level -= this.skyLightSubtracted;

            if (level < 15) {
                level = Math.max(chunk.getBlockLight((int) pos.x & 0x0f, (int) pos.y & 0xff, (int) pos.z & 0x0f), level);
            }
        }

        return level;
    }

    public int calculateSkylightSubtracted(float tickDiff) {
        float light = 1 - (MathHelper.cos(this.calculateCelestialAngle(getTime(), tickDiff) * (6.2831855f)) * 2 + 0.5f);
        light = light < 0 ? 0 : light > 1 ? 1 : light;
        light = 1 - light;
        light = (float) ((double) light * ((raining ? 1 : 0) - 0.3125));
        light = (float) ((double) light * ((isThundering() ? 1 : 0) - 0.3125));
        light = 1 - light;
        return (int) (light * 11f);
    }

    public float calculateCelestialAngle(int time, float tickDiff) {
        float angle = ((float) time + tickDiff) / 24000f - 0.25f;

        if (angle < 0) {
            ++angle;
        }

        if (angle > 1) {
            --angle;
        }

        float i = 1 - (float) ((Math.cos((double) angle * Math.PI) + 1) / 2d);
        angle = angle + (i - angle) / 3;
        return angle;
    }

    public int getMoonPhase(long worldTime) {
        return (int) (worldTime / 24000 % 8 + 8) % 8;
    }

    public int getFullBlock(int x, int y, int z) {
        return this.getChunk(x >> 4, z >> 4, false).getFullBlock(x & 0x0f, y & 0xff, z & 0x0f);
    }

    public synchronized Block getBlock(Vector3 pos) {
        return this.getBlock(pos.getFloorX(), pos.getFloorY(), pos.getFloorZ());
    }

    public synchronized Block getBlock(Vector3 pos, boolean load) {
        return this.getBlock(pos.getFloorX(), pos.getFloorY(), pos.getFloorZ(), load);
    }

    public synchronized Block getBlock(int x, int y, int z) {
        return getBlock(x, y, z, true);
    }

    public synchronized Block getBlock(int x, int y, int z, boolean load) {
        int fullState;
        if (y >= 0 && y < 256) {
            int cx = x >> 4;
            int cz = z >> 4;
            BaseFullChunk chunk;
            if (load) {
                chunk = getChunk(cx, cz);
            } else {
                chunk = getChunkIfLoaded(cx, cz);
            }
            if (chunk != null) {
                fullState = chunk.getFullBlock(x & 0xF, y, z & 0xF);
            } else {
                fullState = 0;
            }
        } else {
            fullState = 0;
        }

        Block block = Block.fullList[fullState & 0xFFF].clone();

        block.x = x;
        block.y = y;
        block.z = z;
        block.level = this;

        return block;
    }

    public void updateAllLight(Vector3 pos) {
        this.updateBlockSkyLight((int) pos.x, (int) pos.y, (int) pos.z);
        this.addLightUpdate((int) pos.x, (int) pos.y, (int) pos.z);
    }

    public void updateBlockSkyLight(int x, int y, int z) {
    }

    public void updateBlockLight(Map<Long, Map<Character, Object>> map) {
        int size = map.size();
        if (size == 0) {
            return;
        }
        Queue<Long> lightPropagationQueue = new ConcurrentLinkedQueue<>();
        Queue<Object[]> lightRemovalQueue = new ConcurrentLinkedQueue<>();
        Long2ObjectOpenHashMap<Object> visited = new Long2ObjectOpenHashMap<>();
        Long2ObjectOpenHashMap<Object> removalVisited = new Long2ObjectOpenHashMap<>();

        Iterator<Map.Entry<Long, Map<Character, Object>>> iter = map.entrySet().iterator();
        while (iter.hasNext() && size-- > 0) {
            Map.Entry<Long, Map<Character, Object>> entry = iter.next();
            iter.remove();
            long index = entry.getKey();
            Map<Character, Object> blocks = entry.getValue();
            int chunkX = Level.getHashX(index);
            int chunkZ = Level.getHashZ(index);
            int bx = chunkX << 4;
            int bz = chunkZ << 4;
            for (char blockHash : blocks.keySet()) {
                int hi = (byte) (blockHash >>> 8);
                int lo = (byte) blockHash;
                int y = lo & 0xFF;
                int x = (hi & 0xF) + bx;
                int z = ((hi >> 4) & 0xF) + bz;
                BaseFullChunk chunk = getChunk(x >> 4, z >> 4, false);
                if (chunk != null) {
                    int lcx = x & 0xF;
                    int lcz = z & 0xF;
                    int oldLevel = chunk.getBlockLight(lcx, y, lcz);
                    int newLevel = Block.light[chunk.getBlockId(lcx, y, lcz)];
                    if (oldLevel != newLevel) {
                        this.setBlockLightAt(x, y, z, newLevel);
                        long hash = Hash.hashBlock(x, y, z);
                        if (newLevel < oldLevel) {
                            removalVisited.put(hash, changeBlocksPresent);
                            lightRemovalQueue.add(new Object[]{hash, oldLevel});
                        } else {
                            visited.put(hash, changeBlocksPresent);
                            lightPropagationQueue.add(hash);
                        }
                    }
                }
            }
        }

        while (!lightRemovalQueue.isEmpty()) {
            Object[] val = lightRemovalQueue.poll();
            long node = (long) val[0];
            int x = Hash.hashBlockX(node);
            int y = Hash.hashBlockY(node);
            int z = Hash.hashBlockZ(node);

            int lightLevel = (int) val[1];

            this.computeRemoveBlockLight(x - 1, y, z, lightLevel, lightRemovalQueue, lightPropagationQueue, removalVisited, visited);
            this.computeRemoveBlockLight(x + 1, y, z, lightLevel, lightRemovalQueue, lightPropagationQueue, removalVisited, visited);
            this.computeRemoveBlockLight(x, y - 1, z, lightLevel, lightRemovalQueue, lightPropagationQueue, removalVisited, visited);
            this.computeRemoveBlockLight(x, y + 1, z, lightLevel, lightRemovalQueue, lightPropagationQueue, removalVisited, visited);
            this.computeRemoveBlockLight(x, y, z - 1, lightLevel, lightRemovalQueue, lightPropagationQueue, removalVisited, visited);
            this.computeRemoveBlockLight(x, y, z + 1, lightLevel, lightRemovalQueue, lightPropagationQueue, removalVisited, visited);
        }

        while (!lightPropagationQueue.isEmpty()) {
            long node = lightPropagationQueue.poll();

            int x = Hash.hashBlockX(node);
            int y = Hash.hashBlockY(node);
            int z = Hash.hashBlockZ(node);

            int lightLevel = this.getBlockLightAt(x, y, z)
                    - Block.lightFilter[this.getBlockIdAt(x, y, z)];

            if (lightLevel >= 1) {
                this.computeSpreadBlockLight(x - 1, y, z, lightLevel, lightPropagationQueue, visited);
                this.computeSpreadBlockLight(x + 1, y, z, lightLevel, lightPropagationQueue, visited);
                this.computeSpreadBlockLight(x, y - 1, z, lightLevel, lightPropagationQueue, visited);
                this.computeSpreadBlockLight(x, y + 1, z, lightLevel, lightPropagationQueue, visited);
                this.computeSpreadBlockLight(x, y, z - 1, lightLevel, lightPropagationQueue, visited);
                this.computeSpreadBlockLight(x, y, z + 1, lightLevel, lightPropagationQueue, visited);
            }
        }
    }

    private void computeRemoveBlockLight(int x, int y, int z, int currentLight, Queue<Object[]> queue,
                                         Queue<Long> spreadQueue, Map<Long, Object> visited, Map<Long, Object> spreadVisited) {
        int current = this.getBlockLightAt(x, y, z);
        if (current != 0 && current < currentLight) {
            this.setBlockLightAt(x, y, z, 0);
            if (current > 1) {
                long index = Hash.hashBlock(x, y, z);
                if (!visited.containsKey(index)) {
                    visited.put(index, changeBlocksPresent);
                    queue.add(new Object[]{index, current});
                }
            }
        } else if (current >= currentLight) {
            long index = Hash.hashBlock(x, y, z);
            if (!spreadVisited.containsKey(index)) {
                spreadVisited.put(index, changeBlocksPresent);
                spreadQueue.add(index);
            }
        }
    }

    private void computeSpreadBlockLight(int x, int y, int z, int currentLight, Queue<Long> queue,
                                         Map<Long, Object> visited) {
        int current = this.getBlockLightAt(x, y, z);
        if (current < currentLight - 1) {
            this.setBlockLightAt(x, y, z, currentLight);

            long index = Hash.hashBlock(x, y, z);
            if (!visited.containsKey(index)) {
                visited.put(index, changeBlocksPresent);
                if (currentLight > 1) {
                    queue.add(index);
                }
            }
        }
    }

    private final Map<Long, Map<Character, Object>> lightQueue = new ConcurrentHashMap<>(8, 0.9f, 1);

    public void addLightUpdate(int x, int y, int z) {
        long index = chunkHash(x >> 4, z >> 4);
        Map<Character, Object> currentMap = lightQueue.get(index);
        if (currentMap == null) {
            currentMap = new ConcurrentHashMap<>(8, 0.9f, 1);
            this.lightQueue.put(index, currentMap);
        }
        currentMap.put(Level.localBlockHash(x, y, z), changeBlocksPresent);
    }

    @Override
    public synchronized void setBlockFullIdAt(int x, int y, int z, int fullId) {
        setBlock(x, y, z, Block.fullList[fullId], false, false);
    }

    public synchronized boolean setBlock(Vector3 pos, Block block) {
        return this.setBlock(pos, block, false);
    }

    public synchronized boolean setBlock(Vector3 pos, Block block, boolean direct) {
        return this.setBlock(pos, block, direct, true);
    }

    public synchronized boolean setBlock(Vector3 pos, Block block, boolean direct, boolean update) {
        return setBlock(pos.getFloorX(), pos.getFloorY(), pos.getFloorZ(), block, direct, update);
    }

    public synchronized boolean setBlock(int x, int y, int z, Block block, boolean direct, boolean update) {
        if (y < 0 || y >= 256) {
            return false;
        }
        BaseFullChunk chunk = this.getChunk(x >> 4, z >> 4, true);
        Block blockPrevious;
        blockPrevious = chunk.getAndSetBlock(x & 0xF, y, z & 0xF, block);
        if (blockPrevious.getFullId() == block.getFullId()) {
            return false;
        }
        block.x = x;
        block.y = y;
        block.z = z;
        block.level = this;
        int cx = x >> 4;
        int cz = z >> 4;

        if (direct) {
            this.sendBlocks(this.getChunkPlayers(cx, cz).values().toArray(new Player[0]), new Block[]{block}, UpdateBlockPacket.FLAG_ALL_PRIORITY);
        } else {
            addBlockChange(Level.chunkHash(cx, cz), x, y, z);
        }

        for (ChunkLoader loader : this.getChunkLoaders(cx, cz)) {
            loader.onBlockChanged(block);
        }
        if (update) {
            if (blockPrevious.isTransparent() != block.isTransparent() || blockPrevious.getLightLevel() != block.getLightLevel()) {
                addLightUpdate(x, y, z);
            }
            BlockUpdateEvent ev = new BlockUpdateEvent(block);
            this.server.getPluginManager().callEvent(ev);
            if (!ev.isCancelled()) {
                for (Entity entity : this.getNearbyEntities(new AxisAlignedBB(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1))) {
                    entity.scheduleUpdate();
                }
                block = ev.getBlock();
                block.onUpdate(BLOCK_UPDATE_NORMAL);
                this.updateAround(x, y, z);
            }
        }
        return true;
    }

    private void addBlockChange(int x, int y, int z) {
        long index = Level.chunkHash(x >> 4, z >> 4);
        addBlockChange(index, x, y, z);
    }

    private void addBlockChange(long index, int x, int y, int z) {
        synchronized (changedBlocks) {
            SoftReference<Map<Character, Object>> current = changedBlocks.computeIfAbsent(index, k -> new SoftReference<>(new HashMap<>()));
            Map<Character, Object> currentMap = current.get();
            if (currentMap != changeBlocksFullMap && currentMap != null) {
                if (currentMap.size() > MAX_BLOCK_CACHE) {
                    this.changedBlocks.put(index, new SoftReference<>(changeBlocksFullMap));
                } else {
                    currentMap.put(Level.localBlockHash(x, y, z), changeBlocksPresent);
                }
            }
        }
    }

    public void dropItem(Vector3 source, Item item) {
        this.dropItem(source, item, null);
    }

    public void dropItem(Vector3 source, Item item, Vector3 motion) {
        this.dropItem(source, item, motion, 10);
    }

    public void dropItem(Vector3 source, Item item, Vector3 motion, int delay) {
        this.dropItem(source, item, motion, false, delay);
    }

    public void dropItem(Vector3 source, Item item, Vector3 motion, boolean dropAround, int delay) {
        if (item.getId() > 0 && item.getCount() > 0) {
            if (motion == null) {
                if (dropAround) {
                    float f = ThreadLocalRandom.current().nextFloat() * 0.5f;
                    float f1 = ThreadLocalRandom.current().nextFloat() * 6.2831855f;

                    motion = new Vector3(-MathHelper.sin(f1) * f, 0.20000000298023224, MathHelper.cos(f1) * f);
                } else {
                    motion = new Vector3(Utils.random.nextDouble() * 0.2 - 0.1, 0.2, Utils.random.nextDouble() * 0.2 - 0.1);
                }
            }

            CompoundTag itemTag = NBTIO.putItemHelper(item);
            itemTag.setName("Item");

            EntityItem itemEntity = new EntityItem(
                    this.getChunk((int) source.getX() >> 4, (int) source.getZ() >> 4, true),
                    new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", source.getX()))
                            .add(new DoubleTag("", source.getY())).add(new DoubleTag("", source.getZ())))

                            .putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", motion.x))
                                    .add(new DoubleTag("", motion.y)).add(new DoubleTag("", motion.z)))

                            .putList(new ListTag<FloatTag>("Rotation")
                                    .add(new FloatTag("", ThreadLocalRandom.current().nextFloat() * 360))
                                    .add(new FloatTag("", 0)))

                            .putShort("Health", 5).putCompound("Item", itemTag).putShort("PickupDelay", delay));

            itemEntity.spawnToAll();
        }
    }

    public EntityItem dropAndGetItem(Vector3 source, Item item) {
        return this.dropAndGetItem(source, item, null);
    }

    public EntityItem dropAndGetItem(Vector3 source, Item item, Vector3 motion) {
        return this.dropAndGetItem(source, item, motion, 10);
    }

    public EntityItem dropAndGetItem(Vector3 source, Item item, Vector3 motion, int delay) {
        return this.dropAndGetItem(source, item, motion, false, delay);
    }

    public EntityItem dropAndGetItem(Vector3 source, Item item, Vector3 motion, boolean dropAround, int delay) {
        EntityItem itemEntity = null;

        if (motion == null) {
            if (dropAround) {
                float f = ThreadLocalRandom.current().nextFloat() * 0.5f;
                float f1 = ThreadLocalRandom.current().nextFloat() * 6.2831855f;

                motion = new Vector3(-MathHelper.sin(f1) * f, 0.20000000298023224, MathHelper.cos(f1) * f);
            } else {
                motion = new Vector3(Utils.random.nextDouble() * 0.2 - 0.1, 0.2,
                        Utils.random.nextDouble() * 0.2 - 0.1);
            }
        }

        CompoundTag itemTag = NBTIO.putItemHelper(item);
        itemTag.setName("Item");

        if (item.getId() > 0 && item.getCount() > 0) {
            itemEntity = (EntityItem) Entity.createEntity("Item",
                    this.getChunk((int) source.getX() >> 4, (int) source.getZ() >> 4, true),
                    new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", source.getX()))
                            .add(new DoubleTag("", source.getY())).add(new DoubleTag("", source.getZ())))

                            .putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", motion.x))
                                    .add(new DoubleTag("", motion.y)).add(new DoubleTag("", motion.z)))

                            .putList(new ListTag<FloatTag>("Rotation")
                                    .add(new FloatTag("", ThreadLocalRandom.current().nextFloat() * 360))
                                    .add(new FloatTag("", 0)))

                            .putShort("Health", 5).putCompound("Item", itemTag).putShort("PickupDelay", delay));

            if (itemEntity != null) {
                itemEntity.spawnToAll();
            }
        }

        return itemEntity;
    }

    public Item useBreakOn(Vector3 vector) {
        return this.useBreakOn(vector, null);
    }

    public Item useBreakOn(Vector3 vector, Item item) {
        return this.useBreakOn(vector, item, null);
    }

    public Item useBreakOn(Vector3 vector, Item item, Player player) {
        return this.useBreakOn(vector, item, player, false);
    }

    public Item useBreakOn(Vector3 vector, Item item, Player player, boolean createParticles) {
        return useBreakOn(vector, null, item, player, createParticles);
    }

    public Item useBreakOn(Vector3 vector, BlockFace face, Item item, Player player, boolean createParticles) {
        if (player != null && player.getGamemode() > Player.ADVENTURE) {
            return null;
        }
        Block target = this.getBlock(vector);
        Item[] drops;
        int dropExp = target.getDropExp();

        if (item == null) {
            item = new ItemBlock(Block.get(BlockID.AIR), 0, 0);
        }

        boolean isSilkTouch = item.hasEnchantment(Enchantment.ID_SILK_TOUCH);

        if (player != null) {
            if (player.getGamemode() == Player.ADVENTURE) {
                Tag tag = item.getNamedTagEntry("CanDestroy");
                boolean canBreak = false;
                if (tag instanceof ListTag) {
                    for (Tag v : ((ListTag<? extends Tag>) tag).getAll()) {
                        if (v instanceof StringTag) {
                            Item entry = Item.fromString(((StringTag) v).data);
                            if (entry.getId() > 0 && entry.getBlock() != null && entry.getBlock().getId() == target.getId()) {
                                canBreak = true;
                                break;
                            }
                        }
                    }
                }
                if (!canBreak) {
                    return null;
                }
            }

            double breakTime = target.getBreakTime(item, player);

            if (player.isCreative() && breakTime > 0.15) {
                breakTime = 0.15;
            }

            if (player.hasEffect(Effect.HASTE)) {
                breakTime *= 1 - (0.2 * (player.getEffect(Effect.HASTE).getAmplifier() + 1));
            }

            if (player.hasEffect(Effect.MINING_FATIGUE)) {
                breakTime *= 1 - (0.3 * (player.getEffect(Effect.MINING_FATIGUE).getAmplifier() + 1));
            }

            Enchantment eff = item.getEnchantment(Enchantment.ID_EFFICIENCY);

            if (eff != null && eff.getLevel() > 0) {
                breakTime *= 1 - (0.3 * eff.getLevel());
            }

            breakTime -= 0.15;

            Item[] eventDrops;
            if (!player.isSurvival()) {
                eventDrops = new Item[0];
            } else if (isSilkTouch && target.canSilkTouch()) {
                eventDrops = new Item[]{target.toItem()};
            } else {
                eventDrops = target.getDrops(item);
            }
            BlockBreakEvent ev = new BlockBreakEvent(player, target, face, item, eventDrops, player.isCreative(),
                    (player.lastBreak + breakTime * 1000) > System.currentTimeMillis());

            if ((player.isSurvival() || player.isAdventure()) && !target.isBreakable(item)) {
                ev.setCancelled();
            } else if (!player.isOp() && isInSpawnRadius(target)) {
                ev.setCancelled();
            } else if (!ev.getInstaBreak() && ev.isFastBreak()) {
                ev.setCancelled();
            }

            player.lastBreak = System.currentTimeMillis();

            this.server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return null;
            }

            drops = ev.getDrops();
            dropExp = ev.getDropExp();
        } else if (!target.isBreakable(item)) {
            return null;
        } else if (item.hasEnchantment(Enchantment.ID_SILK_TOUCH)) {
            drops = new Item[]{target.toItem()};
        } else {
            drops = target.getDrops(item);
        }

        Vector3 above = new Vector3(target.x, target.y + 1, target.z);
        if (this.getBlockIdAt((int) above.x, (int) above.y, (int) above.z) == Item.FIRE) {
            this.setBlock(above, Block.get(BlockID.AIR), true);
        }

        if (createParticles) {
            Map<Integer, Player> players = this.getChunkPlayers((int) target.x >> 4, (int) target.z >> 4);
            this.addParticle(new DestroyBlockParticle(target.add(0.5), target), players.values());
        }

        BlockEntity blockEntity = this.getBlockEntity(target);
        if (blockEntity != null) {
            blockEntity.onBreak();
            blockEntity.close();

            this.updateComparatorOutputLevel(target);
        }
        
        target.onBreak(item, player);

        item.useOn(target);
        if (item.isTool() && item.getDamage() >= item.getMaxDurability()) {
            this.addSoundToViewers(target, cn.nukkit.level.Sound.RANDOM_BREAK);
            this.addParticle(new ItemBreakParticle(target, item));
            item = new ItemBlock(Block.get(BlockID.AIR), 0, 0);
        }

        if (this.gameRules.getBoolean(GameRule.DO_TILE_DROPS)) {
            if (!isSilkTouch && player != null && drops.length != 0) { // For example no xp from redstone if it's mined with stone pickaxe
                if (player.isSurvival() || player.isAdventure()) {
                    this.dropExpOrb(vector.add(0.5, 0.5, 0.5), dropExp);
                }
            }

            if (player == null || player.isSurvival() || player.isAdventure()) {
                for (Item drop : drops) {
                    if (drop.getCount() > 0) {
                        this.dropItem(vector.add(0.5, 0.5, 0.5), drop);
                    }
                }
            }
        }

        return item;
    }

    public void dropExpOrb(Vector3 source, int exp) {
        if (exp > 0) {
            dropExpOrb(source, exp, null);
        }
    }

    public void dropExpOrb(Vector3 source, int exp, Vector3 motion) {
        dropExpOrb(source, exp, motion, 10);
    }

    public void dropExpOrb(Vector3 source, int exp, Vector3 motion, int delay) {
        Random rand = ThreadLocalRandom.current();
        if (server.suomiCraftPEMode()) {
            CompoundTag nbt = Entity.getDefaultNBT(source, motion == null ? new Vector3(
                            (rand.nextDouble() * 0.2 - 0.1) * 2,
                            rand.nextDouble() * 0.4,
                            (rand.nextDouble() * 0.2 - 0.1) * 2) : motion,
                    rand.nextFloat() * 360f, 0);
            nbt.putShort("Value", exp);
            nbt.putShort("PickupDelay", delay);
            Entity entity = Entity.createEntity("XpOrb", this.getChunk(source.getChunkX(), source.getChunkZ()), nbt);
            if (entity != null) {
                entity.spawnToAll();
            }
        } else {
            for (int split : EntityXPOrb.splitIntoOrbSizes(exp)) {
                CompoundTag nbt = Entity.getDefaultNBT(source, motion == null ? new Vector3(
                                (rand.nextDouble() * 0.2 - 0.1) * 2,
                                rand.nextDouble() * 0.4,
                                (rand.nextDouble() * 0.2 - 0.1) * 2) : motion,
                        rand.nextFloat() * 360f, 0);
                nbt.putShort("Value", split);
                nbt.putShort("PickupDelay", delay);
                Entity.createEntity("XpOrb", this.getChunk(source.getChunkX(), source.getChunkZ()), nbt).spawnToAll();
            }
        }
    }

    public Item useItemOn(Vector3 vector, Item item, BlockFace face, float fx, float fy, float fz) {
        return this.useItemOn(vector, item, face, fx, fy, fz, null);
    }

    public Item useItemOn(Vector3 vector, Item item, BlockFace face, float fx, float fy, float fz, Player player) {
        return this.useItemOn(vector, item, face, fx, fy, fz, player, true);
    }

    @SuppressWarnings("unchecked")
    public Item useItemOn(Vector3 vector, Item item, BlockFace face, float fx, float fy, float fz, Player player, boolean playSound) {
        Block target = this.getBlock(vector);
        Block block = target.getSide(face);

        if (block.y > 255 || block.y < 0) {
            return null;
        }

        if (block.y > 127 && this.dimension == DIMENSION_NETHER) {
            return null;
        }

        if (target.getId() == Item.AIR) {
            return null;
        }

        if (player != null) {
            PlayerInteractEvent ev = new PlayerInteractEvent(player, item, target, face, Action.RIGHT_CLICK_BLOCK);

            if (player.getGamemode() > Player.ADVENTURE) {
                ev.setCancelled();
            }

            if (!player.isOp() && isInSpawnRadius(target)) {
                ev.setCancelled();
            }

            this.server.getPluginManager().callEvent(ev);

            if (!ev.isCancelled()) {
                target.onUpdate(BLOCK_UPDATE_TOUCH);

                if ((!player.isSneaking() || player.getInventory().getItemInHand().isNull()) && target.canBeActivated() && target.onActivate(item, player)) {
                    if (item.isTool() && item.getDamage() >= item.getMaxDurability()) {
                        this.addSoundToViewers(target, cn.nukkit.level.Sound.RANDOM_BREAK);
                        this.addParticle(new ItemBreakParticle(target, item));
                        item = new ItemBlock(Block.get(BlockID.AIR), 0, 0);
                    }

                    return item;
                }

                if (item.canBeActivated() && item.onActivate(this, player, block, target, face, fx, fy, fz)) {
                    if (item.getCount() <= 0) {
                        item = new ItemBlock(Block.get(BlockID.AIR), 0, 0);
                        return item;
                    }
                }
            } else {
                return null;
            }
        } else if (target.canBeActivated() && target.onActivate(item, null)) {
            if (item.isTool() && item.getDamage() >= item.getMaxDurability()) {
                this.addSoundToViewers(target, cn.nukkit.level.Sound.RANDOM_BREAK);
                this.addParticle(new ItemBreakParticle(target, item));
                item = new ItemBlock(Block.get(BlockID.AIR), 0, 0);
            }

            return item;
        }

        Block hand;

        if (item.canBePlaced()) {
            hand = item.getBlock();
            hand.position(block);
        } else {
            return null;
        }

        if (!(block.canBeReplaced() || (hand.getId() == Item.SLAB && block.getId() == Item.SLAB))) {
            return null;
        }

        if (target.canBeReplaced()) {
            block = target;
            hand.position(block);
        }

        if (!hand.canPassThrough() && hand.getBoundingBox() != null) {
            Entity[] entities = this.getCollidingEntities(hand.getBoundingBox());
            int realCount = 0;
            for (Entity e : entities) {
                if (e == player || e instanceof EntityArrow || e instanceof EntityItem || (e instanceof Player && ((Player) e).isSpectator() || !e.canCollide())) {
                    continue;
                }
                ++realCount;
            }

            if (player != null) {
                Vector3 diff = player.getNextPosition().subtract(player.getPosition());
                //if (diff.lengthSquared() > 0.00001) {
                    if (hand.getBoundingBox().intersectsWith(player.getBoundingBox().getOffsetBoundingBox(diff.x, diff.y, diff.z))) {
                        ++realCount;
                    }
                //}
            }

            if (realCount > 0) {
                return null;
            }
        }

        if (player != null) {
            BlockPlaceEvent event = new BlockPlaceEvent(player, hand, block, target, item);
            if (player.getGamemode() == Player.ADVENTURE) {
                Tag tag = item.getNamedTagEntry("CanPlaceOn");
                boolean canPlace = false;
                if (tag instanceof ListTag) {
                    for (Tag v : ((ListTag<Tag>) tag).getAll()) {
                        if (v instanceof StringTag) {
                            Item entry = Item.fromString(((StringTag) v).data);
                            if (entry.getId() > 0 && entry.getBlock() != null && entry.getBlock().getId() == target.getId()) {
                                canPlace = true;
                                break;
                            }
                        }
                    }
                }
                if (!canPlace) {
                    event.setCancelled();
                }
            }

            if (!player.isOp() && isInSpawnRadius(target)) {
                event.setCancelled();
            }

            this.server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return null;
            }

            if (server.mobsFromBlocks) {
                if (item.getId() == Item.JACK_O_LANTERN || item.getId() == Item.PUMPKIN) {
                    if (block.getSide(BlockFace.DOWN).getId() == Item.SNOW_BLOCK && block.getSide(BlockFace.DOWN, 2).getId() == Item.SNOW_BLOCK) {
                        block.getLevel().setBlock(target, Block.get(BlockID.AIR));
                        block.getLevel().setBlock(target.add(0, -1, 0), Block.get(BlockID.AIR));

                        CreatureSpawnEvent ev = new CreatureSpawnEvent(EntitySnowGolem.NETWORK_ID, CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN);
                        server.getPluginManager().callEvent(ev);

                        if (ev.isCancelled()) {
                            return null;
                        }

                        Entity.createEntity("SnowGolem", target.add(0.5, -1, 0.5)).spawnToAll();

                        if (!player.isCreative()) {
                            item.setCount(item.getCount() - 1);
                            player.getInventory().setItemInHand(item);
                        }
                        return null;
                    } else if (block.getSide(BlockFace.DOWN).getId() == Item.IRON_BLOCK && block.getSide(BlockFace.DOWN, 2).getId() == Item.IRON_BLOCK) {
                        block = block.getSide(BlockFace.DOWN);
                        Block first, second = null;
                        if ((first = block.getSide(BlockFace.EAST)).getId() == Item.IRON_BLOCK && (second = block.getSide(BlockFace.WEST)).getId() == Item.IRON_BLOCK) {
                            block.getLevel().setBlock(first, Block.get(BlockID.AIR));
                            block.getLevel().setBlock(second, Block.get(BlockID.AIR));
                        } else if ((first = block.getSide(BlockFace.NORTH)).getId() == Item.IRON_BLOCK && (second = block.getSide(BlockFace.SOUTH)).getId() == Item.IRON_BLOCK) {
                            block.getLevel().setBlock(first, Block.get(BlockID.AIR));
                            block.getLevel().setBlock(second, Block.get(BlockID.AIR));
                        }

                        if (second != null) {
                            block.getLevel().setBlock(block, Block.get(BlockID.AIR));
                            block.getLevel().setBlock(block.add(0, -1, 0), Block.get(BlockID.AIR));

                            CreatureSpawnEvent ev = new CreatureSpawnEvent(EntityIronGolem.NETWORK_ID, CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM);
                            server.getPluginManager().callEvent(ev);

                            if (ev.isCancelled()) {
                                return null;
                            }

                            Entity.createEntity("IronGolem", block.add(0.5, -1, 0.5)).spawnToAll();

                            if (!player.isCreative()) {
                                item.setCount(item.getCount() - 1);
                                player.getInventory().setItemInHand(item);
                            }
                            return null;
                        }
                    }
                } else if (item.getId() == Item.SKULL && item.getDamage() == 1) {
                    if (block.getSide(BlockFace.DOWN).getId() == Item.SOUL_SAND && block.getSide(BlockFace.DOWN, 2).getId() == Item.SOUL_SAND) {
                        Block first, second;

                        if (!(((first = block.getSide(BlockFace.EAST)).getId() == Item.SKULL_BLOCK && first.toItem().getDamage() == 1) && ((second = block.getSide(BlockFace.WEST)).getId() == Item.SKULL_BLOCK && second.toItem().getDamage() == 1) || ((first = block.getSide(BlockFace.NORTH)).getId() == Item.SKULL_BLOCK && first.toItem().getDamage() == 1) && ((second = block.getSide(BlockFace.SOUTH)).getId() == Item.SKULL_BLOCK && second.toItem().getDamage() == 1))) {
                            return null;
                        }

                        block = block.getSide(BlockFace.DOWN);

                        Block first2, second2;

                        if (!((first2 = block.getSide(BlockFace.EAST)).getId() == Item.SOUL_SAND && (second2 = block.getSide(BlockFace.WEST)).getId() == Item.SOUL_SAND || (first2 = block.getSide(BlockFace.NORTH)).getId() == Item.SOUL_SAND && (second2 = block.getSide(BlockFace.SOUTH)).getId() == Item.SOUL_SAND)) {
                            return null;
                        }

                        block.getLevel().setBlock(first, Block.get(BlockID.AIR));
                        block.getLevel().setBlock(second, Block.get(BlockID.AIR));
                        block.getLevel().setBlock(first2, Block.get(BlockID.AIR));
                        block.getLevel().setBlock(second2, Block.get(BlockID.AIR));
                        block.getLevel().setBlock(block, Block.get(BlockID.AIR));
                        block.getLevel().setBlock(block.add(0, -1, 0), Block.get(BlockID.AIR));

                        CreatureSpawnEvent ev = new CreatureSpawnEvent(EntityWither.NETWORK_ID, CreatureSpawnEvent.SpawnReason.BUILD_WITHER);
                        server.getPluginManager().callEvent(ev);

                        if (ev.isCancelled()) {
                            return null;
                        }

                        if (!player.isCreative()) {
                            item.setCount(item.getCount() - 1);
                            player.getInventory().setItemInHand(item);
                        }

                        EntityWither wither = (EntityWither) Entity.createEntity("Wither", block.add(0.5, -1, 0.5));
                        wither.stayTime = 220;
                        wither.spawnToAll();
                        this.addSoundToViewers(wither, cn.nukkit.level.Sound.MOB_WITHER_SPAWN);
                        return null;
                    }
                }
            }
        }

        if (!hand.place(item, block, target, face, fx, fy, fz, player)) {
            return null;
        }

        if (player != null) {
            if (!player.isCreative()) {
                item.setCount(item.getCount() - 1);
            }
        }


        if (playSound) {
            Int2ObjectMap<ObjectList<Player>> players = Server.sortPlayers(this.getChunkPlayers(hand.getChunkX(), hand.getChunkZ()).values());
            for (int protocolId : players.keySet()) {
                ObjectList<Player> targets = players.get(protocolId);
                int soundData = GlobalBlockPalette.getOrCreateRuntimeId(protocolId > ProtocolInfo.v1_2_10 ? protocolId : ProtocolInfo.CURRENT_PROTOCOL, // no block palette in <= 1.2.10
                        hand.getId(), hand.getDamage());
                this.addLevelSoundEvent(hand, LevelSoundEventPacket.SOUND_PLACE, soundData, targets.toArray(new Player[0]));
            }
        }

        if (item.getCount() <= 0) {
            item = new ItemBlock(Block.get(BlockID.AIR), 0, 0);
        }
        return item;
    }

    public boolean isInSpawnRadius(Vector3 vector3) {
        return server.getSpawnRadius() > -1 && new Vector2(vector3.x, vector3.z).distance(new Vector2(this.getSpawnLocation().x, this.getSpawnLocation().z)) <= server.getSpawnRadius();
    }

    public Entity getEntity(long entityId) {
        return this.entities.containsKey(entityId) ? this.entities.get(entityId) : null;
    }

    public Entity[] getEntities() {
        return entities.values().toArray(new Entity[0]);
    }

    public Entity[] getCollidingEntities(AxisAlignedBB bb) {
        return this.getCollidingEntities(bb, null);
    }

    public Entity[] getCollidingEntities(AxisAlignedBB bb, Entity entity) {
        List<Entity> nearby = new ArrayList<>();

        if (entity == null || entity.canCollide()) {
            int minX = NukkitMath.floorDouble((bb.minX - 2) / 16);
            int maxX = NukkitMath.ceilDouble((bb.maxX + 2) / 16);
            int minZ = NukkitMath.floorDouble((bb.minZ - 2) / 16);
            int maxZ = NukkitMath.ceilDouble((bb.maxZ + 2) / 16);

            for (int x = minX; x <= maxX; ++x) {
                for (int z = minZ; z <= maxZ; ++z) {
                    for (Entity ent : this.getChunkEntities(x, z, false).values()) {
                        if ((entity == null || (ent != entity && entity.canCollideWith(ent)))
                                && ent.boundingBox.intersectsWith(bb)) {
                            nearby.add(ent);
                        }
                    }
                }
            }
        }

        return nearby.toArray(new Entity[0]);
    }

    public Entity[] getNearbyEntities(AxisAlignedBB bb) {
        return this.getNearbyEntities(bb, null);
    }

    private static final Entity[] EMPTY_ENTITY_ARR = new Entity[0];
    private static final Entity[] ENTITY_BUFFER = new Entity[512];

    public Entity[] getNearbyEntities(AxisAlignedBB bb, Entity entity) {
        return getNearbyEntities(bb, entity, false);
    }

    public Entity[] getNearbyEntities(AxisAlignedBB bb, Entity entity, boolean loadChunks) {
        int index = 0;

        int minX = NukkitMath.floorDouble((bb.minX - 2) * 0.0625);
        int maxX = NukkitMath.ceilDouble((bb.maxX + 2) * 0.0625);
        int minZ = NukkitMath.floorDouble((bb.minZ - 2) * 0.0625);
        int maxZ = NukkitMath.ceilDouble((bb.maxZ + 2) * 0.0625);

        ArrayList<Entity> overflow = null;

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                for (Entity ent : this.getChunkEntities(x, z, loadChunks).values()) {
                    if (ent != entity && ent.boundingBox.intersectsWith(bb)) {
                        if (index < ENTITY_BUFFER.length) {
                            ENTITY_BUFFER[index] = ent;
                        } else {
                            if (overflow == null) overflow = new ArrayList<>(1024);
                            overflow.add(ent);
                        }
                        index++;
                    }
                }
            }
        }

        if (index == 0) return EMPTY_ENTITY_ARR;
        Entity[] copy;
        if (overflow == null) {
            copy = Arrays.copyOfRange(ENTITY_BUFFER, 0, index);
            Arrays.fill(ENTITY_BUFFER, 0, index, null);
        } else {
            copy = new Entity[ENTITY_BUFFER.length + overflow.size()];
            System.arraycopy(ENTITY_BUFFER, 0, copy, 0, ENTITY_BUFFER.length);
            for (int i = 0; i < overflow.size(); i++) {
                copy[ENTITY_BUFFER.length + i] = overflow.get(i);
            }
        }
        return copy;
    }

    public Map<Long, BlockEntity> getBlockEntities() {
        return blockEntities;
    }

    public BlockEntity getBlockEntityById(long blockEntityId) {
        return this.blockEntities.containsKey(blockEntityId) ? this.blockEntities.get(blockEntityId) : null;
    }

    public Map<Long, Player> getPlayers() {
        return players;
    }

    public Map<Integer, ChunkLoader> getLoaders() {
        return loaders;
    }

    public BlockEntity getBlockEntity(Vector3 pos) {
        FullChunk chunk = this.getChunk((int) pos.x >> 4, (int) pos.z >> 4, false);

        if (chunk != null) {
            return chunk.getTile((int) pos.x & 0x0f, (int) pos.y & 0xff, (int) pos.z & 0x0f);
        }

        return null;
    }

    public BlockEntity getBlockEntityIfLoaded(Vector3 pos) {
        FullChunk chunk = this.getChunkIfLoaded((int) pos.x >> 4, (int) pos.z >> 4);

        if (chunk != null) {
            return chunk.getTile((int) pos.x & 0x0f, (int) pos.y & 0xff, (int) pos.z & 0x0f);
        }

        return null;
    }

    public Map<Long, Entity> getChunkEntities(int X, int Z) {
        return getChunkEntities(X, Z, true);
    }

    public Map<Long, Entity> getChunkEntities(int X, int Z, boolean loadChunks) {
        FullChunk chunk = loadChunks ? this.getChunk(X, Z) : this.getChunkIfLoaded(X, Z);
        return chunk != null ? chunk.getEntities() : Collections.emptyMap();
    }

    public Map<Long, BlockEntity> getChunkBlockEntities(int X, int Z) {
        FullChunk chunk;
        return (chunk = this.getChunk(X, Z)) != null ? chunk.getBlockEntities() : Collections.emptyMap();
    }

    @Override
    public synchronized int getBlockIdAt(int x, int y, int z) {
        return this.getChunk(x >> 4, z >> 4, true).getBlockId(x & 0x0f, y & 0xff, z & 0x0f);
    }

    public int getBlockIdAt(FullChunk chunk, int x, int y, int z) {
        if (chunk == null) chunk = this.getChunk(x >> 4, z >> 4, true);
        return chunk.getBlockId(x & 0x0f, y & 0xff, z & 0x0f);
    }

    @Override
    public synchronized void setBlockIdAt(int x, int y, int z, int id) {
        this.getChunk(x >> 4, z >> 4, true).setBlockId(x & 0x0f, y & 0xff, z & 0x0f, id & 0xff);
        addBlockChange(x, y, z);
        temporalVector.setComponents(x, y, z);
        for (ChunkLoader loader : this.getChunkLoaders(x >> 4, z >> 4)) {
            loader.onBlockChanged(temporalVector);
        }
    }

    public synchronized void setBlockAt(int x, int y, int z, int id, int data) {
        BaseFullChunk chunk = this.getChunk(x >> 4, z >> 4, true);
        chunk.setBlockId(x & 0x0f, y & 0xff, z & 0x0f, id & 0xff);
        chunk.setBlockData(x & 0x0f, y & 0xff, z & 0x0f, data & 0xf);
        addBlockChange(x, y, z);
        temporalVector.setComponents(x, y, z);
        for (ChunkLoader loader : this.getChunkLoaders(x >> 4, z >> 4)) {
            loader.onBlockChanged(temporalVector);
        }
    }

    public synchronized int getBlockExtraDataAt(int x, int y, int z) {
        return this.getChunk(x >> 4, z >> 4, true).getBlockExtraData(x & 0x0f, y & 0xff, z & 0x0f);
    }

    public synchronized void setBlockExtraDataAt(int x, int y, int z, int id, int data) {
        this.getChunk(x >> 4, z >> 4, true).setBlockExtraData(x & 0x0f, y & 0xff, z & 0x0f, (data << 8) | id);

        this.sendBlockExtraData(x, y, z, id, data);
    }

    @Override
    public synchronized int getBlockDataAt(int x, int y, int z) {
        return this.getChunk(x >> 4, z >> 4, true).getBlockData(x & 0x0f, y & 0xff, z & 0x0f);
    }

    @Override
    public synchronized void setBlockDataAt(int x, int y, int z, int data) {
        this.getChunk(x >> 4, z >> 4, true).setBlockData(x & 0x0f, y & 0xff, z & 0x0f, data & 0x0f);
        addBlockChange(x, y, z);
        temporalVector.setComponents(x, y, z);
        for (ChunkLoader loader : this.getChunkLoaders(x >> 4, z >> 4)) {
            loader.onBlockChanged(temporalVector);
        }
    }

    public synchronized int getBlockSkyLightAt(int x, int y, int z) {
        return this.getChunk(x >> 4, z >> 4, true).getBlockSkyLight(x & 0x0f, y & 0xff, z & 0x0f);
    }

    public synchronized void setBlockSkyLightAt(int x, int y, int z, int level) {
        this.getChunk(x >> 4, z >> 4, true).setBlockSkyLight(x & 0x0f, y & 0xff, z & 0x0f, level & 0x0f);
    }

    public synchronized int getBlockLightAt(int x, int y, int z) {
        BaseFullChunk chunk = this.getChunkIfLoaded(x >> 4, z >> 4);
        return chunk == null ? 0 : chunk.getBlockLight(x & 0x0f, y & 0xff, z & 0x0f);
    }

    public synchronized void setBlockLightAt(int x, int y, int z, int level) {
        BaseFullChunk c = this.getChunkIfLoaded(x >> 4, z >> 4);
        if (null != c) {
            c.setBlockLight(x & 0x0f, y & 0xff, z & 0x0f, level & 0x0f);
        }
    }

    public int getBiomeId(int x, int z) {
        return this.getChunk(x >> 4, z >> 4, true).getBiomeId(x & 0x0f, z & 0x0f);
    }

    public void setBiomeId(int x, int z, int biomeId) {
        this.getChunk(x >> 4, z >> 4, true).setBiomeId(x & 0x0f, z & 0x0f, biomeId & 0x0f);
    }

    public void setBiomeId(int x, int z, byte biomeId) {
        this.getChunk(x >> 4, z >> 4, true).setBiomeId(x & 0x0f, z & 0x0f, biomeId & 0x0f);
    }

    public int getHeightMap(int x, int z) {
        return this.getChunk(x >> 4, z >> 4, true).getHeightMap(x & 0x0f, z & 0x0f);
    }

    public void setHeightMap(int x, int z, int value) {
        this.getChunk(x >> 4, z >> 4, true).setHeightMap(x & 0x0f, z & 0x0f, value & 0x0f);
    }

    public int getBiomeColor(int x, int z) {
        return this.getChunk(x >> 4, z >> 4, true).getBiomeColor(x & 0x0f, z & 0x0f);
    }

    public void setBiomeColor(int x, int z, int R, int G, int B) {
        this.getChunk(x >> 4, z >> 4, true).setBiomeColor(x & 0x0f, z & 0x0f, R, G, B);
    }

    public Map<Long, ? extends FullChunk> getChunks() {
        return provider.getLoadedChunks();
    }

    @Override
    public BaseFullChunk getChunk(int chunkX, int chunkZ) {
        return this.getChunk(chunkX, chunkZ, false);
    }

    public BaseFullChunk getChunk(int chunkX, int chunkZ, boolean create) {
        long index = Level.chunkHash(chunkX, chunkZ);
        BaseFullChunk chunk = this.provider.getLoadedChunk(index);
        if (chunk == null) {
            chunk = this.forceLoadChunk(index, chunkX, chunkZ, create);
        }
        return chunk;
    }

    public BaseFullChunk getChunkIfLoaded(int chunkX, int chunkZ) {
        return this.provider.getLoadedChunk(Level.chunkHash(chunkX, chunkZ));
    }

    public void generateChunkCallback(int x, int z, BaseFullChunk chunk) {
        generateChunkCallback(x, z, chunk, true);
    }

    public void generateChunkCallback(int x, int z, BaseFullChunk chunk, boolean isPopulated) {
        if (Timings.generationCallbackTimer != null) Timings.generationCallbackTimer.startTiming();
        long index = Level.chunkHash(x, z);
        if (this.chunkPopulationQueue.containsKey(index)) {
            FullChunk oldChunk = this.getChunk(x, z, false);
            for (int xx = -1; xx <= 1; ++xx) {
                for (int zz = -1; zz <= 1; ++zz) {
                    this.chunkPopulationLock.remove(Level.chunkHash(x + xx, z + zz));
                }
            }
            this.chunkPopulationQueue.remove(index);
            chunk.setProvider(this.provider);
            this.setChunk(x, z, chunk, false);
            chunk = this.getChunk(x, z, false);
            if (chunk != null && (oldChunk == null || !isPopulated) && chunk.isPopulated()
                    && chunk.getProvider() != null) {
                this.server.getPluginManager().callEvent(new ChunkPopulateEvent(chunk));

                for (ChunkLoader loader : this.getChunkLoaders(x, z)) {
                    loader.onChunkPopulated(chunk);
                }
            }
        } else if (this.chunkGenerationQueue.containsKey(index) || this.chunkPopulationLock.containsKey(index)) {
            this.chunkGenerationQueue.remove(index);
            this.chunkPopulationLock.remove(index);
            chunk.setProvider(this.provider);
            this.setChunk(x, z, chunk, false);
        } else {
            chunk.setProvider(this.provider);
            this.setChunk(x, z, chunk, false);
        }
        if (Timings.generationCallbackTimer != null) Timings.generationCallbackTimer.stopTiming();
    }

    @Override
    public void setChunk(int chunkX, int chunkZ) {
        this.setChunk(chunkX, chunkZ, null);
    }

    @Override
    public void setChunk(int chunkX, int chunkZ, BaseFullChunk chunk) {
        this.setChunk(chunkX, chunkZ, chunk, true);
    }

    public void setChunk(int chunkX, int chunkZ, BaseFullChunk chunk, boolean unload) {
        if (chunk == null) {
            return;
        }

        long index = Level.chunkHash(chunkX, chunkZ);
        FullChunk oldChunk = this.getChunk(chunkX, chunkZ, false);

        if (oldChunk != chunk) {
            if (unload && oldChunk != null) {
                this.unloadChunk(chunkX, chunkZ, false, false);
            } else {
                Map<Long, Entity> oldEntities = oldChunk != null ? oldChunk.getEntities() : Collections.emptyMap();

                Map<Long, BlockEntity> oldBlockEntities = oldChunk != null ? oldChunk.getBlockEntities() : Collections.emptyMap();

                if (!oldEntities.isEmpty()) {
                    Iterator<Map.Entry<Long, Entity>> iter = oldEntities.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<Long, Entity> entry = iter.next();
                        Entity entity = entry.getValue();
                        chunk.addEntity(entity);
                        if (oldChunk != null) {
                            iter.remove();
                            oldChunk.removeEntity(entity);
                            entity.chunk = chunk;
                        }
                    }
                }

                if (!oldBlockEntities.isEmpty()) {
                    Iterator<Map.Entry<Long, BlockEntity>> iter = oldBlockEntities.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<Long, BlockEntity> entry = iter.next();
                        BlockEntity blockEntity = entry.getValue();
                        chunk.addBlockEntity(blockEntity);
                        if (oldChunk != null) {
                            iter.remove();
                            oldChunk.removeBlockEntity(blockEntity);
                            blockEntity.chunk = chunk;
                        }
                    }
                }

            }
            this.provider.setChunk(chunkX, chunkZ, chunk);
        }

        chunk.setChanged();

        if (!this.isChunkInUse(index)) {
            this.unloadChunkRequest(chunkX, chunkZ);
        } else {
            for (ChunkLoader loader : this.getChunkLoaders(chunkX, chunkZ)) {
                loader.onChunkChanged(chunk);
            }
        }
    }

    public int getHighestBlockAt(int x, int z) {
        return this.getHighestBlockAt(x, z, true);
    }

    public int getHighestBlockAt(int x, int z, boolean cache) {
        return this.getChunk(x >> 4, z >> 4, true).getHighestBlockAt(x & 0x0f, z & 0x0f, cache);
    }

    public BlockColor getMapColorAt(int x, int z) {
        int y = getHighestBlockAt(x, z, false);

        while (y > 1) {
            Block block = getBlock(new Vector3(x, y, z));
            if (block instanceof BlockGrass) {
                return getGrassColorAt(x, z);
            //} else if (block instanceof BlockWater) {
            //    return getWaterColorAt(x, z);
            } else {
                BlockColor blockColor = block.getColor();
                if (blockColor.getAlpha() == 0x00) {
                    y--;
                } else {
                    return blockColor;
                }
            }
        }

        return BlockColor.VOID_BLOCK_COLOR;
    }

    public BlockColor getGrassColorAt(int x, int z) {
        int biome = this.getBiomeId(x, z);

        switch (biome) {
            case 0: //ocean
            case 7: //river
            case 9: //end
            case 24: //deep ocean
                return new BlockColor("#8eb971");
            case 1: //plains
            case 16: //beach
            case 129: //sunflower plains
                return new BlockColor("#91bd59");
            case 2: //desert
            case 8: //hell
            case 17: //desert hills
            case 35: //savanna
            case 36: //savanna plateau
            case 130: //desert m
            case 163: //savanna m
            case 164: //savanna plateau m
                return new BlockColor("#bfb755");
            case 3: //extreme hills
            case 20: //extreme hills edge
            case 25: //stone beach
            case 34: //extreme hills
            case 131: //extreme hills m
            case 162: //extreme hills plus m
                return new BlockColor("#8ab689");
            case 4: //forest
            case 132: //flower forest
                return new BlockColor("#79c05a");
            case 5: //taiga
            case 19: //taiga hills
            case 32: //mega taiga
            case 33: //mega taiga hills
            case 133: //taiga m
            case 160: //mega spruce taiga
                return new BlockColor("#86b783");
            case 6: //swamp
            case 134: //swampland m
                return new BlockColor("#6A7039");
            case 10: //frozen ocean
            case 11: //frozen river
            case 12: //ice plains
            case 30: //cold taiga
            case 31: //cold taiga hills
            case 140: //ice plains spikes
            case 158: //cold taiga m
                return new BlockColor("#80b497");
            case 14: //mushroom island
            case 15: //mushroom island shore
                return new BlockColor("#55c93f");
            case 18: //forest hills
            case 27: //birch forest
            case 28: //birch forest hills
            case 155: //birch forest m
            case 156: //birch forest hills m
                return new BlockColor("#88bb67");
            case 21: //jungle
            case 22: //jungle hills
            case 149: //jungle m
                return new BlockColor("#59c93c");
            case 23: //jungle edge
            case 151: //jungle edge m
                return new BlockColor("#64c73f");
            case 26: //cold beach
                return new BlockColor("#83b593");
            case 29: //roofed forest
            case 157: //roofed forest m
                return new BlockColor("#507a32");
            case 37: //mesa
            case 38: //mesa plateau f
            case 39: //mesa plateau
            case 165: //mesa bryce
            case 166: //mesa plateau f m
            case 167: //mesa plateau m
                return new BlockColor("#90814d");
            default:
                return BlockColor.GRASS_BLOCK_COLOR;
        }
    }

    public BlockColor getWaterColorAt(int x, int z) {
        int biome = this.getBiomeId(x, z);

        switch (biome) {
            case 2: //desert
            case 130: //desert m
                return new BlockColor("#32A598");
            case 4: //forest
                return new BlockColor("#1E97F2");
            case 132: //flower forest
                return new BlockColor("#20A3CC");
            case 5: //taiga
            case 19: //taiga hills
            case 133: //taiga m
            case 3: //extreme hills
            case 20: //extreme hills edge
            case 34: //extreme hills
            case 131: //extreme hills m
            case 162: //extreme hills plus m
                return new BlockColor("#1E6B82");
            case 6: //swamp
                return new BlockColor("#4c6559");
            case 134: //swampland m
                return new BlockColor("#4c6156");
            case 7: //river
                return new BlockColor("#0084FF");
            case 9: //end
                return new BlockColor("#62529e");
            case 8: //hell
                return new BlockColor("#905957");
            case 11: //frozen river
                return new BlockColor("#185390");
            case 12: //ice plains
            case 140: //ice plains spikes
                return new BlockColor("#14559b");
            case 14: //mushroom island
                return new BlockColor("#8a8997");
            case 15: //mushroom island shore
                return new BlockColor("#818193");
            case 16: //beach
                return new BlockColor("#157cab");
            case 17: //desert hills
                return new BlockColor("#1a7aa1");
            case 18: //forest hills
                return new BlockColor("#056bd1");
            case 21: //jungle
                return new BlockColor("#14A2C5");
            case 22: //jungle hills
            case 149: //jungle m
                return new BlockColor("#1B9ED8");
            case 23: //jungle edge
            case 151: //jungle edge m
                return new BlockColor("#0D8AE3");
            case 25: //stone beach
                return new BlockColor("#0d67bb");
            case 26: //cold beach
                return new BlockColor("#1463a5");
            case 27: //birch forest
            case 155: //birch forest m
                return new BlockColor("#0677ce");
            case 28: //birch forest hills
            case 156: //birch forest hills m
                return new BlockColor("#0a74c4");
            case 29: //roofed forest
            case 157: //roofed forest m
                return new BlockColor("#3B6CD1");
            case 30: //cold taiga
            case 158: //cold taiga m
                return new BlockColor("#205e83");
            case 31: //cold taiga hills
                return new BlockColor("#245b78");
            case 32: //mega taiga
            case 160: //mega spruce taiga
                return new BlockColor("#2d6d77");
            case 33: //mega taiga hills
                return new BlockColor("#286378");
            case 35: //savanna
            case 163: //savanna m
                return new BlockColor("#2C8B9C");
            case 36: //savanna plateau
            case 164: //savanna plateau m
                return new BlockColor("#2590A8");
            case 0: //ocean
            case 24: //deep ocean
                return new BlockColor("#1787D4");
            case 10: //frozen ocean
                return new BlockColor("#2570B5");
            default: // plains, sunflower plains, others
                return new BlockColor("#44AFF5");
            case 37: //mesa
                return new BlockColor("#4E7F81");
            case 38: //mesa plateau f
            case 39: //mesa plateau
            case 165: //mesa bryce
                return new BlockColor("#497F99");
            case 166: //mesa plateau f m
            case 167: //mesa plateau m
                return new BlockColor("#55809E");
        }
    }

    public boolean isChunkLoaded(int x, int z) {
        return this.provider.isChunkLoaded(x, z);
    }

    private boolean areNeighboringChunksLoaded(long hash) {
        return this.provider.isChunkLoaded(hash + 1) &&
               this.provider.isChunkLoaded(hash - 1) &&
               this.provider.isChunkLoaded(hash + (4294967296L)) &&
               this.provider.isChunkLoaded(hash - (4294967296L));
    }

    public boolean isChunkGenerated(int x, int z) {
        FullChunk chunk = this.getChunk(x, z);
        return chunk != null && chunk.isGenerated();
    }

    public boolean isChunkPopulated(int x, int z) {
        FullChunk chunk = this.getChunk(x, z);
        return chunk != null && chunk.isPopulated();
    }

    public Position getSpawnLocation() {
        return Position.fromObject(this.provider.getSpawn(), this);
    }

    public void setSpawnLocation(Vector3 pos) {
        Position previousSpawn = this.getSpawnLocation();
        this.provider.setSpawn(pos);
        this.server.getPluginManager().callEvent(new SpawnChangeEvent(this, previousSpawn));
        SetSpawnPositionPacket pk = new SetSpawnPositionPacket();
        pk.spawnType = SetSpawnPositionPacket.TYPE_WORLD_SPAWN;
        pk.x = pos.getFloorX();
        pk.y = pos.getFloorY();
        pk.z = pos.getFloorZ();
        pk.dimension = this.getDimension();
        for (Player p : getPlayers().values()) p.dataPacket(pk);
    }

    public void requestChunk(int x, int z, Player player) {
        Preconditions.checkState(player.getLoaderId() > 0, player.getName() + " has no chunk loader");
        long index = Level.chunkHash(x, z);

        this.getChunkSendQueue(player.protocol).computeIfAbsent(index, k ->
                new Int2ObjectOpenHashMap<>()).put(player.getLoaderId(), player);
    }

    private void sendChunk(int x, int z, long index, DataPacket packet) {
        for (int protocolId : chunkSendTasks.keySet()) {
            this.sendChunkInternal(x, z, index, packet, protocolId);
        }
    }

    private void sendChunkInternal(int x, int z, long index, DataPacket packet, int protocol) {
        LongSet tasks = this.getChunkSendTasks(protocol);
        if (!tasks.contains(index)) {
            return;
        }

        ConcurrentMap<Long, Int2ObjectMap<Player>> queue = this.getChunkSendQueue(protocol);
        for (Player player : queue.get(index).values()) {
            if (player.isConnected() && player.usedChunks.containsKey(index)) {
                player.sendChunk(x, z, packet);
            }
        }
        queue.remove(index);
        tasks.remove(index);
    }

    private void processChunkRequest() {
        if (this.timings.syncChunkSendTimer != null) {
            this.timings.syncChunkSendTimer.startTiming();
        }

        // Map shorted by index => requested protocols
        Long2ObjectMap<IntSet> chunkRequests = new Long2ObjectOpenHashMap<>();
        for (int protocolId : this.chunkSendQueues.keySet()) {
            Set<Long> indexes = this.getChunkSendQueue(protocolId).keySet();
            LongSet tasks = this.getChunkSendTasks(protocolId);
            for (long index : indexes) {
                if (!tasks.contains(index)) {
                    chunkRequests.computeIfAbsent(index, l -> new IntOpenHashSet()).add(protocolId);
                    tasks.add(index);
                }
            }
        }

        this.chunkRequestInternal(chunkRequests);

        if (this.timings.syncChunkSendTimer != null) {
            this.timings.syncChunkSendTimer.stopTiming();
        }
    }

    private void chunkRequestInternal(Long2ObjectMap<IntSet> chunkRequests) {
        for (long index : chunkRequests.keySet()) {
            IntSet protocols = new IntOpenHashSet(chunkRequests.get(index));
            int x = getHashX(index);
            int z = getHashZ(index);

            for (int protocol : chunkRequests.get(index)) {
                BaseFullChunk chunk = this.getChunk(x, z);
                if (chunk != null) {
                    BatchPacket packet = chunk.getChunkPacket(protocol);
                    if (packet != null) {
                        this.sendChunk(x, z, index, packet);
                        protocols.remove(protocol);
                    }
                }
            }

            if (protocols.isEmpty()) {
                continue;
            }

            if (this.timings.syncChunkSendPrepareTimer != null) {
                this.timings.syncChunkSendPrepareTimer.startTiming();
            }

            this.provider.requestChunkTask(protocols, x, z);

            if (this.timings.syncChunkSendPrepareTimer != null) {
                this.timings.syncChunkSendPrepareTimer.stopTiming();
            }
        }
    }

    public void chunkRequestCallback(int protocol, long timestamp, int x, int z, int subChunkCount, byte[] payload) {
        if (this.timings.syncChunkSendTimer != null) this.timings.syncChunkSendTimer.startTiming();
        long index = Level.chunkHash(x, z);

        if (server.cacheChunks) {
            BatchPacket data = Player.getChunkCacheFromData(protocol, x, z, subChunkCount, payload);
            BaseFullChunk chunk = getChunk(x, z, false);
            if (chunk != null && chunk.getChanges() <= timestamp) {
                chunk.setChunkPacket(protocol, data);
            }
            this.sendChunk(x, z, index, data);
            if (this.timings.syncChunkSendTimer != null) this.timings.syncChunkSendTimer.stopTiming();
            return;
        }

        LongSet tasks = this.getChunkSendTasks(protocol);
        if (tasks.contains(index)) {
            ConcurrentMap<Long, Int2ObjectMap<Player>> queue = this.getChunkSendQueue(protocol);
            for (Player player : queue.get(index).values()) {
                if (player.isConnected() && player.usedChunks.containsKey(index)) {
                    if (matchMVChunkProtocol(protocol, player.protocol)) {
                        player.sendChunk(x, z, subChunkCount, payload);
                    }
                }
            }

            queue.remove(index);
            tasks.remove(index);
        }

        if (this.timings.syncChunkSendTimer != null) this.timings.syncChunkSendTimer.stopTiming();
    }

    public void removeEntity(Entity entity) {
        if (entity.getLevel() != this) {
            throw new LevelException("Invalid Entity level");
        }

        if (entity instanceof Player) {
            this.players.remove(entity.getId());
            this.checkSleep();
        } else {
            entity.close();
        }

        this.entities.remove(entity.getId());
        this.updateEntities.remove(entity.getId());
    }

    public void addEntity(Entity entity) {
        if (entity.getLevel() != this) {
            throw new LevelException("Invalid Entity level");
        }

        if (entity instanceof Player) {
            this.players.put(entity.getId(), (Player) entity);
        }
        this.entities.put(entity.getId(), entity);
    }

    public void addBlockEntity(BlockEntity blockEntity) {
        if (blockEntity.getLevel() != this) {
            throw new LevelException("Invalid BlockEntity level");
        }
        blockEntities.put(blockEntity.getId(), blockEntity);
    }

    public void scheduleBlockEntityUpdate(BlockEntity entity) {
        Preconditions.checkNotNull(entity, "entity");
        Preconditions.checkArgument(entity.getLevel() == this, "BlockEntity is not in this level");
        if (!updateBlockEntities.contains(entity)) {
            updateBlockEntities.add(entity);
        }
    }

    public void removeBlockEntity(BlockEntity entity) {
        Preconditions.checkNotNull(entity, "entity");
        Preconditions.checkArgument(entity.getLevel() == this, "BlockEntity is not in this level");
        blockEntities.remove(entity.getId());
        updateBlockEntities.remove(entity);
    }

    public boolean isChunkInUse(int x, int z) {
        return isChunkInUse(Level.chunkHash(x, z));
    }

    public boolean isChunkInUse(long hash) {
        Map<Integer, ChunkLoader> map = this.chunkLoaders.get(hash);
        return map != null && !map.isEmpty();
    }

    public boolean loadChunk(int x, int z) {
        return this.loadChunk(x, z, true);
    }

    public boolean loadChunk(int x, int z, boolean generate) {
        long index = Level.chunkHash(x, z);
        if (this.provider.isChunkLoaded(index)) {
            return true;
        }
        return forceLoadChunk(index, x, z, generate) != null;
    }

    private synchronized BaseFullChunk forceLoadChunk(long index, int x, int z, boolean generate) {
        if (this.timings.syncChunkLoadTimer != null) this.timings.syncChunkLoadTimer.startTiming();

        BaseFullChunk chunk = this.provider.getChunk(x, z, generate);

        if (chunk == null) {
            if (generate) {
                throw new IllegalStateException("Could not create new chunk");
            }
            if (this.timings.syncChunkLoadTimer != null) this.timings.syncChunkLoadTimer.stopTiming();
            return null;
        }

        if (chunk.getProvider() != null) {
            this.server.getPluginManager().callEvent(new ChunkLoadEvent(chunk, !chunk.isGenerated()));
        } else {
            this.unloadChunk(x, z, false);
            if (this.timings.syncChunkLoadTimer != null) this.timings.syncChunkLoadTimer.stopTiming();
            return chunk;
        }

        chunk.initChunk();

        if (!chunk.isLightPopulated() && chunk.isPopulated() && this.server.lightUpdates) {
            this.server.getScheduler().scheduleAsyncTask(new LightPopulationTask(this, chunk));
        }

        if (this.isChunkInUse(index)) {
            this.unloadQueue.remove(index);
            for (ChunkLoader loader : this.getChunkLoaders(x, z)) {
                loader.onChunkLoaded(chunk);
            }
        } else {
            this.unloadQueue.put(index, System.currentTimeMillis());
        }
        if (this.timings.syncChunkLoadTimer != null) this.timings.syncChunkLoadTimer.stopTiming();
        return chunk;
    }

    private void queueUnloadChunk(int x, int z) {
        long index = Level.chunkHash(x, z);
        this.unloadQueue.put(index, System.currentTimeMillis());
    }

    public boolean unloadChunkRequest(int x, int z) {
        return this.unloadChunkRequest(x, z, true);
    }

    public boolean unloadChunkRequest(int x, int z, boolean safe) {
        if ((safe && this.isChunkInUse(x, z)) || this.isSpawnChunk(x, z)) {
            return false;
        }

        this.queueUnloadChunk(x, z);

        return true;
    }

    public void cancelUnloadChunkRequest(int x, int z) {
        this.cancelUnloadChunkRequest(Level.chunkHash(x, z));
    }

    public void cancelUnloadChunkRequest(long hash) {
        this.unloadQueue.remove(hash);
    }

    public boolean unloadChunk(int x, int z) {
        return this.unloadChunk(x, z, true);
    }

    public boolean unloadChunk(int x, int z, boolean safe) {
        return this.unloadChunk(x, z, safe, true);
    }

    public synchronized boolean unloadChunk(int x, int z, boolean safe, boolean trySave) {
        if (safe && this.isChunkInUse(x, z)) {
            return false;
        }

        if (!this.isChunkLoaded(x, z)) {
            return true;
        }

        if (this.timings.doChunkUnload != null) this.timings.doChunkUnload.startTiming();

        BaseFullChunk chunk = this.getChunk(x, z);

        if (chunk != null && chunk.getProvider() != null) {
            ChunkUnloadEvent ev = new ChunkUnloadEvent(chunk);
            this.server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                if (this.timings.doChunkUnload != null) this.timings.doChunkUnload.stopTiming();
                return false;
            }
        }

        try {
            if (chunk != null) {
                if (trySave && this.autoSave) {
                    int entities = 0;
                    for (Entity e : chunk.getEntities().values()) {
                        if (e instanceof Player) {
                            continue;
                        }
                        ++entities;
                    }

                    if (chunk.hasChanged() || !chunk.getBlockEntities().isEmpty() || entities > 0) {
                        this.provider.setChunk(x, z, chunk);
                        this.provider.saveChunk(x, z);
                    }
                }
                for (ChunkLoader loader : this.getChunkLoaders(x, z)) {
                    loader.onChunkUnloaded(chunk);
                }
            }
            this.provider.unloadChunk(x, z, safe);
        } catch (Exception e) {
            MainLogger logger = this.server.getLogger();
            logger.error(this.server.getLanguage().translateString("nukkit.level.chunkUnloadError", e.toString()));
            logger.logException(e);
        }

        if (this.timings.doChunkUnload != null) this.timings.doChunkUnload.stopTiming();

        return true;
    }

    public boolean isSpawnChunk(int X, int Z) {
        Vector3 spawn = this.getSpawnLocation();

        if (this.server.suomiCraftPEMode() && !this.randomTickingEnabled()) {
            if (this.equals(this.getServer().getDefaultLevel())) {
                return Math.abs(X - (spawn.getFloorX() >> 4)) <= 9 && Math.abs(Z - (spawn.getFloorZ() >> 4)) <= 9;
            }
            return Math.abs(X - (spawn.getFloorX() >> 4)) <= 5 && Math.abs(Z - (spawn.getFloorZ() >> 4)) <= 5;
        } else {
            return Math.abs(X - (spawn.getFloorX() >> 4)) <= 1 && Math.abs(Z - (spawn.getFloorZ() >> 4)) <= 1;
        }
    }

    public Position getSafeSpawn() {
        return this.getSafeSpawn(null);
    }

    public Position getSafeSpawn(Vector3 spawn) {
        if (spawn == null /*|| spawn.y < 1*/) {
            spawn = this.getSpawnLocation();
        }

        Vector3 pos = new Vector3(spawn.getFloorX(), (int) Math.floor(spawn.y + 0.1), spawn.getFloorZ());
        FullChunk chunk = this.getChunk((int) pos.x >> 4, (int) pos.z >> 4, false);
        int x = (int) pos.x & 0x0f;
        int z = (int) pos.z & 0x0f;
        if (chunk != null && chunk.isGenerated()) {
            int y = NukkitMath.clamp((int) pos.y, 1, 254);
            boolean wasAir = chunk.getBlockId(x, y - 1, z) == 0;
            for (; y > 0; --y) {
                int b = chunk.getFullBlock(x, y, z);
                Block block = Block.get(b >> 4, b & 0x0f);
                if (this.isFullBlock(block)) {
                    if (wasAir) {
                        y++;
                        break;
                    }
                } else {
                    wasAir = true;
                }
            }

            for (; y >= 0 && y < 255; y++) {
                int b = chunk.getFullBlock(x, y + 1, z);
                Block block = Block.get(b >> 4, b & 0x0f);
                if (!this.isFullBlock(block)) {
                    b = chunk.getFullBlock(x, y, z);
                    block = Block.get(b >> 4, b & 0x0f);
                    if (!this.isFullBlock(block)) {
                        return new Position(pos.x + 0.5, pos.y + 0.1, pos.z + 0.5, this);
                    }
                } else {
                    ++y;
                }
            }

            pos.y = y;
        }

        return new Position(pos.x + 0.5, pos.y + 0.1, pos.z + 0.5, this);
    }

    public int getTime() {
        return time;
    }

    public boolean isDaytime() {
        return this.skyLightSubtracted < 4;
    }

    public long getCurrentTick() {
        return this.levelCurrentTick;
    }

    public String getName() {
        return this.provider.getName();
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setTime(int time) {
        this.time = time;
        this.sendTime();
    }

    public void stopTime() {
        this.stopTime = true;
        this.sendTime();
    }

    public void startTime() {
        this.stopTime = false;
        this.sendTime();
    }

    @Override
    public long getSeed() {
        return this.provider.getSeed();
    }

    public void setSeed(int seed) {
        this.provider.setSeed(seed);
    }

    public boolean populateChunk(int x, int z) {
        return this.populateChunk(x, z, false);
    }

    public boolean populateChunk(int x, int z, boolean force) {
        long index = Level.chunkHash(x, z);
        if (this.chunkPopulationQueue.containsKey(index) || this.chunkPopulationQueue.size() >= this.chunkPopulationQueueSize && !force) {
            return false;
        }

        BaseFullChunk chunk = this.getChunk(x, z, true);
        boolean populate;
        if (!chunk.isPopulated()) {
            if (Timings.populationTimer != null) Timings.populationTimer.startTiming();
            populate = true;
            for (int xx = -1; xx <= 1; ++xx) {
                for (int zz = -1; zz <= 1; ++zz) {
                    if (this.chunkPopulationLock.containsKey(Level.chunkHash(x + xx, z + zz))) {
                        populate = false;
                        break;
                    }
                }
            }

            if (populate) {
                if (!this.chunkPopulationQueue.containsKey(index)) {
                    this.chunkPopulationQueue.put(index, Boolean.TRUE);
                    for (int xx = -1; xx <= 1; ++xx) {
                        for (int zz = -1; zz <= 1; ++zz) {
                            this.chunkPopulationLock.put(Level.chunkHash(x + xx, z + zz), Boolean.TRUE);
                        }
                    }

                    this.server.getScheduler().scheduleAsyncTask(new PopulationTask(this, chunk));
                }
            }
            if (Timings.populationTimer != null) Timings.populationTimer.stopTiming();
            return false;
        }

        return true;
    }

    public void generateChunk(int x, int z) {
        this.generateChunk(x, z, false);
    }

    public void generateChunk(int x, int z, boolean force) {
        if (this.chunkGenerationQueue.size() >= this.chunkGenerationQueueSize && !force) {
            return;
        }

        long index = Level.chunkHash(x, z);
        if (!this.chunkGenerationQueue.containsKey(index)) {
            if (Timings.generationTimer != null) Timings.generationTimer.startTiming();
            this.chunkGenerationQueue.put(index, Boolean.TRUE);
            GenerationTask task = new GenerationTask(this, this.getChunk(x, z, true));
            this.server.getScheduler().scheduleAsyncTask(task);
            if (Timings.generationTimer != null) Timings.generationTimer.stopTiming();
        }
    }

    public void regenerateChunk(int x, int z) {
        this.unloadChunk(x, z, false, false);
        this.cancelUnloadChunkRequest(x, z);
        provider.setChunk(x, z, provider.getEmptyChunk(x, z));
        this.generateChunk(x, z);
    }

    public void doChunkGarbageCollection() {
        if (this.timings.doChunkGC != null) this.timings.doChunkGC.startTiming();
        // Remove all invalid block entities
        if (!blockEntities.isEmpty()) {
            ObjectIterator<BlockEntity> iter = blockEntities.values().iterator();
            while (iter.hasNext()) {
                BlockEntity blockEntity = iter.next();
                if (blockEntity != null) {
                    if (!blockEntity.isValid()) {
                        iter.remove();
                        blockEntity.close();
                    }
                } else {
                    iter.remove();
                }
            }
        }

        for (Map.Entry<Long, ? extends FullChunk> entry : provider.getLoadedChunks().entrySet()) {
            long index = entry.getKey();
            if (!this.unloadQueue.containsKey(index)) {
                FullChunk chunk = entry.getValue();
                int X = chunk.getX();
                int Z = chunk.getZ();
                if (!this.isSpawnChunk(X, Z)) {
                    this.unloadChunkRequest(X, Z, true);
                }
            }
        }

        this.provider.doGarbageCollection();
        if (this.timings.doChunkGC != null) this.timings.doChunkGC.stopTiming();
    }


    public void doGarbageCollection(long allocatedTime) {
        long start = System.currentTimeMillis();
        if (unloadChunks(start, allocatedTime, false)) {
            allocatedTime -= (System.currentTimeMillis() - start);
            provider.doGarbageCollection(allocatedTime);
        }
    }

    public void unloadChunks() {
        this.unloadChunks(false);
    }

    public void unloadChunks(boolean force) {
        this.unloadChunks(50, force);
    }

    public void unloadChunks(int maxUnload, boolean force) {
        if (!this.unloadQueue.isEmpty()) {
            long now = System.currentTimeMillis();

            int unloaded = 0;
            LongList toRemove = null;
            for (Long2LongMap.Entry entry : unloadQueue.long2LongEntrySet()) {
                long index = entry.getLongKey();

                if (isChunkInUse(index)) {
                    continue;
                }

                if (!force) {
                    long time = entry.getLongValue();
                    if (unloaded > maxUnload) {
                        break;
                    } else if (time > (now - 20000)) {
                        continue;
                    }
                    unloaded++;
                }

                if (toRemove == null) toRemove = new LongArrayList();
                toRemove.add(index);
            }

            if (toRemove != null) {
                int size = toRemove.size();
                for (int i = 0; i < size; i++) {
                    long index = toRemove.getLong(i);
                    int X = getHashX(index);
                    int Z = getHashZ(index);

                    if (this.unloadChunk(X, Z, true)) {
                        this.unloadQueue.remove(index);
                    }
                }
            }
        }
    }

    private int lastUnloadIndex;

    /**
     * @param now current time
     * @param allocatedTime allocated time
     * @param force force
     * @return true if there is allocated time remaining
     */
    private boolean unloadChunks(long now, long allocatedTime, boolean force) {
        if (!this.unloadQueue.isEmpty()) {
            boolean result = true;
            int maxIterations = this.unloadQueue.size();

            if (lastUnloadIndex > maxIterations) lastUnloadIndex = 0;
            ObjectIterator<Long2LongMap.Entry> iter = this.unloadQueue.long2LongEntrySet().iterator();
            if (lastUnloadIndex != 0) iter.skip(lastUnloadIndex);

            LongList toUnload = null;

            for (int i = 0; i < maxIterations; i++) {
                if (!iter.hasNext()) {
                    iter = this.unloadQueue.long2LongEntrySet().iterator();
                }
                Long2LongMap.Entry entry = iter.next();

                long index = entry.getLongKey();

                if (isChunkInUse(index)) {
                    continue;
                }

                if (!force) {
                    long time = entry.getLongValue();
                    if (time > (now - 20000)) {
                        continue;
                    }
                }

                if (toUnload == null) toUnload = new LongArrayList();
                toUnload.add(index);
            }

            if (toUnload != null) {
                //long[] arr = toUnload.toLongArray();
                for (long index : toUnload) {
                    int X = getHashX(index);
                    int Z = getHashZ(index);
                    if (this.unloadChunk(X, Z, true)) {
                        this.unloadQueue.remove(index);
                        if (System.currentTimeMillis() - now >= allocatedTime) {
                            result = false;
                            break;
                        }
                    }
                }
            }
            return result;
        } else {
            return true;
        }
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) throws Exception {
        this.server.getLevelMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) throws Exception {
        return this.server.getLevelMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) throws Exception {
        return this.server.getLevelMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) throws Exception {
        this.server.getLevelMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @SuppressWarnings("unused")
    public void addPlayerMovement(Entity entity, double x, double y, double z, double yaw, double pitch, double headYaw) {
        MovePlayerPacket pk = new MovePlayerPacket();
        pk.eid = entity.getId();
        pk.x = (float) x;
        pk.y = (float) y;
        pk.z = (float) z;
        pk.yaw = (float) yaw;
        pk.headYaw = (float) headYaw;
        pk.pitch = (float) pitch;
        pk.onGround = entity.onGround;

        if (entity.riding != null) {
            pk.ridingEid = entity.riding.getId();
            pk.mode = MovePlayerPacket.MODE_PITCH;
        }

        Server.broadcastPacket(entity.getViewers().values(), pk);
    }

    public void addEntityMovement(Entity entity, double x, double y, double z, double yaw, double pitch, double headYaw) {
        MoveEntityAbsolutePacket pk = new MoveEntityAbsolutePacket();
        pk.eid = entity.getId();
        pk.x = (float) x;
        pk.y = (float) y;
        pk.z = (float) z;
        pk.yaw = (float) yaw;
        pk.headYaw = (float) headYaw;
        pk.pitch = (float) pitch;
        pk.onGround = entity.onGround;

        for (Player p : entity.getViewers().values()) {
            p.batchDataPacket(pk); // Server.broadcastPacket would only use batching for >= 1.16.100
        }
    }

    public boolean isRaining() {
        return this.raining;
    }

    public boolean setRaining(boolean raining) {
        WeatherChangeEvent ev = new WeatherChangeEvent(this, raining);
        this.server.getPluginManager().callEvent(ev);

        if (ev.isCancelled()) {
            return false;
        }

        this.raining = raining;

        LevelEventPacket pk = new LevelEventPacket();
        // These numbers are from Minecraft

        if (raining) {
            pk.evid = LevelEventPacket.EVENT_START_RAIN;
            int time = Utils.random.nextInt(12000) + 12000;
            pk.data = time;
            setRainTime(time);
        } else {
            pk.evid = LevelEventPacket.EVENT_STOP_RAIN;
            setRainTime(Utils.random.nextInt(168000) + 12000);
        }

        Server.broadcastPacket(this.getPlayers().values(), pk);

        return true;
    }

    public int getRainTime() {
        return this.rainTime;
    }

    public void setRainTime(int rainTime) {
        this.rainTime = rainTime;
    }

    public boolean isThundering() {
        return raining && this.thundering;
    }

    public boolean setThundering(boolean thundering) {
        ThunderChangeEvent ev = new ThunderChangeEvent(this, thundering);
        this.server.getPluginManager().callEvent(ev);

        if (ev.isCancelled()) {
            return false;
        }

        if (thundering && !raining) {
            setRaining(true);
        }

        this.thundering = thundering;

        LevelEventPacket pk = new LevelEventPacket();
        // These numbers are from Minecraft
        if (thundering) {
            pk.evid = LevelEventPacket.EVENT_START_THUNDER;
            int time = Utils.random.nextInt(12000) + 3600;
            pk.data = time;
            setThunderTime(time);
        } else {
            pk.evid = LevelEventPacket.EVENT_STOP_THUNDER;
            setThunderTime(Utils.random.nextInt(168000) + 12000);
        }

        Server.broadcastPacket(this.getPlayers().values(), pk);

        return true;
    }

    public int getThunderTime() {
        return this.thunderTime;
    }

    public void setThunderTime(int thunderTime) {
        this.thunderTime = thunderTime;
    }

    public void sendWeather(Player[] players) {
        if (players == null) {
            players = this.getPlayers().values().toArray(new Player[0]);
        }

        LevelEventPacket pk = new LevelEventPacket();

        if (this.raining) {
            pk.evid = LevelEventPacket.EVENT_START_RAIN;
            pk.data = this.rainTime;
        } else {
            pk.evid = LevelEventPacket.EVENT_STOP_RAIN;
        }

        Server.broadcastPacket(players, pk);

        if (this.isThundering()) {
            pk.evid = LevelEventPacket.EVENT_START_THUNDER;
            pk.data = this.thunderTime;
        } else {
            pk.evid = LevelEventPacket.EVENT_STOP_THUNDER;
        }

        Server.broadcastPacket(players, pk);
    }

    public void sendWeather(Player player) {
        if (player != null) {
            this.sendWeather(new Player[]{player});
        }
    }

    public void sendWeather(Collection<Player> players) {
        if (players == null) {
            players = this.getPlayers().values();
        }
        this.sendWeather(players.toArray(new Player[0]));
    }

    public int getDimension() {
        return dimension;
    }

    public boolean canBlockSeeSky(Vector3 pos) {
        return this.getHighestBlockAt(pos.getFloorX(), pos.getFloorZ()) < pos.getY();
    }

    public boolean canBlockSeeSky(Block block) {
        return this.getHighestBlockAt((int) block.getX(), (int) block.getZ()) < block.getY();
    }

    public int getStrongPower(Vector3 pos, BlockFace direction) {
        return this.getBlock(pos).getStrongPower(direction);
    }

    public int getStrongPower(Vector3 pos) {
        int i = 0;

        for (BlockFace face : BlockFace.values()) {
            i = Math.max(i, this.getStrongPower(pos.getSide(face), face));

            if (i >= 15) {
                return i;
            }
        }

        return i;
    }

    public boolean isSidePowered(Vector3 pos, BlockFace face) {
        return this.getRedstonePower(pos, face) > 0;
    }

    public int getRedstonePower(Vector3 pos, BlockFace face) {
        Block block = this.getBlock(pos);
        return block.isNormalBlock() ? this.getStrongPower(pos) : block.getWeakPower(face);
    }

    public boolean isBlockPowered(Vector3 pos) {
        for (BlockFace face : BlockFace.values()) {
            if (this.getRedstonePower(pos.getSide(face), face) > 0) {
                return true;
            }
        }
        return false;
    }

    public int isBlockIndirectlyGettingPowered(Vector3 pos) {
        int power = 0;

        for (BlockFace face : BlockFace.values()) {
            int blockPower = this.getRedstonePower(pos.getSide(face), face);

            if (blockPower >= 15) {
                return 15;
            }

            if (blockPower > power) {
                power = blockPower;
            }
        }

        return power;
    }

    public boolean isAreaLoaded(AxisAlignedBB bb) {
        if (bb.maxY < 0 || bb.minY >= 256) {
            return false;
        }
        int minX = NukkitMath.floorDouble(bb.minX) >> 4;
        int minZ = NukkitMath.floorDouble(bb.minZ) >> 4;
        int maxX = NukkitMath.floorDouble(bb.maxX) >> 4;
        int maxZ = NukkitMath.floorDouble(bb.maxZ) >> 4;

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                if (!this.isChunkLoaded(x, z)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void addLevelEvent(Vector3 pos, int event) {
        this.addLevelEvent(pos, event, 0);
    }

    public void addLevelEvent(Vector3 pos, int event, int data) {
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = event;
        pk.x = (float) pos.x;
        pk.y = (float) pos.y;
        pk.z = (float) pos.z;
        pk.data = data;

        addChunkPacket(pos.getFloorX() >> 4, pos.getFloorZ() >> 4, pk);
    }

    private int getUpdateLCG() {
        return (this.updateLCG = (this.updateLCG * 3) ^ LCG_CONSTANT);
    }

    public boolean randomTickingEnabled() {
        return this.randomTickingEnabled;
    }

    public boolean isAnimalSpawningAllowedByTime() {
        int time = this.getTime() % TIME_FULL;
        return time < 13184 || time > 22800;
    }

    public boolean isMobSpawningAllowedByTime() {
        int time = this.getTime() % TIME_FULL;
        return time > 13184 && time < 22800;
    }

    public boolean shouldMobBurn(BaseEntity entity) {
        int time = this.getTime() % TIME_FULL;
        return !entity.isOnFire() && !this.raining && !entity.isBaby() && (time < 12567 || time > 23450) && !entity.isInsideOfWater() && entity.canSeeSky();
    }

    public boolean isMobSpawningAllowed() {
        return !Server.disabledSpawnWorlds.contains(getName()) && gameRules.getBoolean(GameRule.DO_MOB_SPAWNING);
    }

    public boolean createPortal(Block target, boolean fireCharge) {
        if (this.dimension == DIMENSION_THE_END) return false;
        final int maxPortalSize = 23;
        final int targX = target.getFloorX();
        final int targY = target.getFloorY();
        final int targZ = target.getFloorZ();
        //check if there's air above (at least 3 blocks)
        for (int i = 1; i < 4; i++) {
            if (this.getBlockIdAt(targX, targY + i, targZ) != BlockID.AIR) {
                return false;
            }
        }
        int sizePosX = 0;
        int sizeNegX = 0;
        int sizePosZ = 0;
        int sizeNegZ = 0;
        for (int i = 1; i < maxPortalSize; i++) {
            if (this.getBlockIdAt(targX + i, targY, targZ) == BlockID.OBSIDIAN) {
                sizePosX++;
            } else {
                break;
            }
        }
        for (int i = 1; i < maxPortalSize; i++) {
            if (this.getBlockIdAt(targX - i, targY, targZ) == BlockID.OBSIDIAN) {
                sizeNegX++;
            } else {
                break;
            }
        }
        for (int i = 1; i < maxPortalSize; i++) {
            if (this.getBlockIdAt(targX, targY, targZ + i) == BlockID.OBSIDIAN) {
                sizePosZ++;
            } else {
                break;
            }
        }
        for (int i = 1; i < maxPortalSize; i++) {
            if (this.getBlockIdAt(targX, targY, targZ - i) == BlockID.OBSIDIAN) {
                sizeNegZ++;
            } else {
                break;
            }
        }
        //plus one for target block
        int sizeX = sizePosX + sizeNegX + 1;
        int sizeZ = sizePosZ + sizeNegZ + 1;
        if (sizeX >= 2 && sizeX <= maxPortalSize) {
            //start scan from 1 block above base
            //find pillar or end of portal to start scan
            int scanX = targX;
            int scanY = targY + 1;
            for (int i = 0; i < sizePosX + 1; i++) {
                //this must be air
                if (this.getBlockIdAt(scanX + i, scanY, targZ) != BlockID.AIR) {
                    return false;
                }
                if (this.getBlockIdAt(scanX + i + 1, scanY, targZ) == BlockID.OBSIDIAN) {
                    scanX += i;
                    break;
                }
            }
            //make sure that the above loop finished
            if (this.getBlockIdAt(scanX + 1, scanY, targZ) != BlockID.OBSIDIAN) {
                return false;
            }

            int innerWidth = 0;
            LOOP: for (int i = 0; i < 21; i++) {
                int id = this.getBlockIdAt(scanX - i, scanY, targZ);
                switch (id) {
                    case BlockID.AIR:
                        innerWidth++;
                        break;
                    case BlockID.OBSIDIAN:
                        break LOOP;
                    default:
                        return false;
                }
            }
            int innerHeight = 0;
            LOOP: for (int i = 0; i < 21; i++) {
                int id = this.getBlockIdAt(scanX, scanY + i, targZ);
                switch (id) {
                    case BlockID.AIR:
                        innerHeight++;
                        break;
                    case BlockID.OBSIDIAN:
                        break LOOP;
                    default:
                        return false;
                }
            }
            if (!(innerWidth <= 21
                    && innerWidth >= 2
                    && innerHeight <= 21
                    && innerHeight >= 3))   {
                return false;
            }

            for (int height = 0; height < innerHeight + 1; height++)    {
                if (height == innerHeight) {
                    for (int width = 0; width < innerWidth; width++) {
                        if (this.getBlockIdAt(scanX - width, scanY + height, targZ) != BlockID.OBSIDIAN) {
                            return false;
                        }
                    }
                } else {
                    if (this.getBlockIdAt(scanX + 1, scanY + height, targZ) != BlockID.OBSIDIAN
                            || this.getBlockIdAt(scanX - innerWidth, scanY + height, targZ) != BlockID.OBSIDIAN) {
                        return false;
                    }

                    for (int width = 0; width < innerWidth; width++) {
                        if (this.getBlockIdAt(scanX - width, scanY + height, targZ) != BlockID.AIR) {
                            return false;
                        }
                    }
                }
            }

            for (int height = 0; height < innerHeight; height++)    {
                for (int width = 0; width < innerWidth; width++)    {
                    this.setBlock(new Vector3(scanX - width, scanY + height, targZ), Block.get(BlockID.NETHER_PORTAL));
                }
            }

            if (fireCharge) {
                this.addSoundToViewers(target, cn.nukkit.level.Sound.MOB_GHAST_FIREBALL);
            } else {
                this.addLevelSoundEvent(target, LevelSoundEventPacket.SOUND_IGNITE);
            }
            return true;
        } else if (sizeZ >= 2 && sizeZ <= maxPortalSize) {
            //start scan from 1 block above base
            //find pillar or end of portal to start scan
            int scanY = targY + 1;
            int scanZ = targZ;
            for (int i = 0; i < sizePosZ + 1; i++) {
                //this must be air
                if (this.getBlockIdAt(targX, scanY, scanZ + i) != BlockID.AIR) {
                    return false;
                }
                if (this.getBlockIdAt(targX, scanY, scanZ + i + 1) == BlockID.OBSIDIAN) {
                    scanZ += i;
                    break;
                }
            }
            //make sure that the above loop finished
            if (this.getBlockIdAt(targX, scanY, scanZ + 1) != BlockID.OBSIDIAN) {
                return false;
            }

            int innerWidth = 0;
            LOOP: for (int i = 0; i < 21; i++) {
                int id = this.getBlockIdAt(targX, scanY, scanZ - i);
                switch (id) {
                    case BlockID.AIR:
                        innerWidth++;
                        break;
                    case BlockID.OBSIDIAN:
                        break LOOP;
                    default:
                        return false;
                }
            }
            int innerHeight = 0;
            LOOP: for (int i = 0; i < 21; i++) {
                int id = this.getBlockIdAt(targX, scanY + i, scanZ);
                switch (id) {
                    case BlockID.AIR:
                        innerHeight++;
                        break;
                    case BlockID.OBSIDIAN:
                        break LOOP;
                    default:
                        return false;
                }
            }
            if (!(innerWidth <= 21
                    && innerWidth >= 2
                    && innerHeight <= 21
                    && innerHeight >= 3))   {
                return false;
            }

            for (int height = 0; height < innerHeight + 1; height++)    {
                if (height == innerHeight) {
                    for (int width = 0; width < innerWidth; width++) {
                        if (this.getBlockIdAt(targX, scanY + height, scanZ - width) != BlockID.OBSIDIAN) {
                            return false;
                        }
                    }
                } else {
                    if (this.getBlockIdAt(targX, scanY + height, scanZ + 1) != BlockID.OBSIDIAN
                            || this.getBlockIdAt(targX, scanY + height, scanZ - innerWidth) != BlockID.OBSIDIAN) {
                        return false;
                    }

                    for (int width = 0; width < innerWidth; width++) {
                        if (this.getBlockIdAt(targX, scanY + height, scanZ - width) != BlockID.AIR) {
                            return false;
                        }
                    }
                }
            }

            for (int height = 0; height < innerHeight; height++)    {
                for (int width = 0; width < innerWidth; width++)    {
                    this.setBlock(new Vector3(targX, scanY + height, scanZ - width), Block.get(BlockID.NETHER_PORTAL));
                }
            }

            if (fireCharge) {
                this.addSoundToViewers(target, cn.nukkit.level.Sound.MOB_GHAST_FIREBALL);
            } else {
                this.addLevelSoundEvent(target, LevelSoundEventPacket.SOUND_IGNITE);
            }
            return true;
        }

        return false;
    }

    public Position calculatePortalMirror(Vector3 portal) {
        Level nether = Server.getInstance().getNetherWorld(this.getName());
        if (nether == null) {
            return null;
        }

        double x;
        double z;
        if (this == nether) {
            x = Math.floor(portal.getFloorX() << 3);
            z = Math.floor(portal.getFloorZ() << 3);
        } else {
            x = Math.floor(portal.getFloorX() >> 3);
            z = Math.floor(portal.getFloorZ() >> 3);
        }
        return new Position(x, portal.getFloorY(), z, this == nether ? Server.getInstance().getDefaultLevel() : nether);
    }

    private ConcurrentMap<Long, Int2ObjectMap<Player>> getChunkSendQueue(int protocol) {
        int protocolId = this.getChunkProtocol(protocol);
        return this.chunkSendQueues.computeIfAbsent(protocolId, i -> new ConcurrentHashMap<>());
    }

    private LongSet getChunkSendTasks(int protocol) {
        int protocolId = this.getChunkProtocol(protocol);
        return this.chunkSendTasks.computeIfAbsent(protocolId, i -> new LongOpenHashSet());
    }

    private int getChunkProtocol(int protocol) {
        if (protocol >= ProtocolInfo.v1_17_40) {
            return ProtocolInfo.v1_17_40;
        } if (protocol >= ProtocolInfo.v1_17_30) {
            return ProtocolInfo.v1_17_30;
        } else if (protocol >= ProtocolInfo.v1_17_10) {
            return ProtocolInfo.v1_17_10;
        } else if (protocol >= ProtocolInfo.v1_17_0) {
            return ProtocolInfo.v1_17_0;
        } else if (protocol >= ProtocolInfo.v1_16_210) {
            return ProtocolInfo.v1_16_210;
        } else if (protocol >= ProtocolInfo.v1_16_100) {
            return ProtocolInfo.v1_16_100;
        } else if (protocol >= ProtocolInfo.v1_16_0 && protocol <= ProtocolInfo.v1_16_100_52) {
            return ProtocolInfo.v1_16_0;
        } else if (protocol == ProtocolInfo.v1_14_0 || protocol == ProtocolInfo.v1_14_60) {
            return ProtocolInfo.v1_14_0;
        } else if (protocol == ProtocolInfo.v1_13_0) {
            return ProtocolInfo.v1_13_0;
        } else if (protocol == ProtocolInfo.v1_12_0) {
            return ProtocolInfo.v1_12_0;
        } else if (protocol < ProtocolInfo.v1_12_0) {
            return 0;
        }
        throw new IllegalArgumentException("Invalid chunk protocol: " + protocol);
    }

    private static boolean matchMVChunkProtocol(int chunk, int player) {
        if (chunk == 0) if (player < ProtocolInfo.v1_12_0) return true;
        if (chunk == ProtocolInfo.v1_12_0) if (player == ProtocolInfo.v1_12_0) return true;
        if (chunk == ProtocolInfo.v1_13_0) if (player == ProtocolInfo.v1_13_0) return true;
        if (chunk == ProtocolInfo.v1_14_0)
            if (player == ProtocolInfo.v1_14_0 || player == ProtocolInfo.v1_14_60) return true;
        if (chunk == ProtocolInfo.v1_16_0)
            if (player >= ProtocolInfo.v1_16_0) if (player <= ProtocolInfo.v1_16_100_52) return true;
        if (chunk == ProtocolInfo.v1_16_100)
            if (player >= ProtocolInfo.v1_16_100) if (player < ProtocolInfo.v1_16_210) return true;
        if (chunk == ProtocolInfo.v1_16_210)
            if (player >= ProtocolInfo.v1_16_210) if (player < ProtocolInfo.v1_17_0) return true;
        if (chunk == ProtocolInfo.v1_17_0) if (player == ProtocolInfo.v1_17_0) return true;
        if (chunk == ProtocolInfo.v1_17_10)
            if (player >= ProtocolInfo.v1_17_10) if (player < ProtocolInfo.v1_17_30) return true;
        if (chunk == ProtocolInfo.v1_17_30) if (player == ProtocolInfo.v1_17_30) return true;
        if (chunk == ProtocolInfo.v1_17_40) if (player >= ProtocolInfo.v1_17_40) return true;
        return false; // Remember to update when block palette changes
    }

    private static class CharacterHashMap extends HashMap<Character, Object> {

        @Override
        public int size() {
            return Character.MAX_VALUE;
        }
    }
}

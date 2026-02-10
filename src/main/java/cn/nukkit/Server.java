package cn.nukkit;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.block.custom.CustomBlockManager;
import cn.nukkit.blockentity.*;
import cn.nukkit.command.*;
import cn.nukkit.console.NukkitConsole;
import cn.nukkit.dispenser.DispenseBehaviorRegister;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.custom.EntityManager;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.entity.item.*;
import cn.nukkit.entity.mob.*;
import cn.nukkit.entity.passive.*;
import cn.nukkit.entity.projectile.*;
import cn.nukkit.entity.route.RouteFinderThreadPool;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.LevelInitEvent;
import cn.nukkit.event.level.LevelLoadEvent;
import cn.nukkit.event.server.PlayerDataSerializeEvent;
import cn.nukkit.event.server.QueryRegenerateEvent;
import cn.nukkit.event.server.ServerStopEvent;
import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.Recipe;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemMap;
import cn.nukkit.item.RuntimeItemMapping;
import cn.nukkit.item.RuntimeItems;
import cn.nukkit.item.custom.CustomItemManager;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.lang.BaseLang;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.EnumLevel;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.Level;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.LevelProviderManager;
import cn.nukkit.level.format.anvil.Anvil;
import cn.nukkit.level.format.leveldb.LevelDBProvider;
import cn.nukkit.level.generator.*;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.metadata.EntityMetadataStore;
import cn.nukkit.metadata.LevelMetadataStore;
import cn.nukkit.metadata.PlayerMetadataStore;
import cn.nukkit.metrics.NukkitMetrics;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.BatchingHelper;
import cn.nukkit.network.Network;
import cn.nukkit.network.RakNetInterface;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.network.protocol.*;
import cn.nukkit.network.query.QueryHandler;
import cn.nukkit.network.rcon.RCON;
import cn.nukkit.permission.BanEntry;
import cn.nukkit.permission.BanList;
import cn.nukkit.permission.DefaultPermissions;
import cn.nukkit.permission.Permissible;
import cn.nukkit.plugin.JavaPluginLoader;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginLoadOrder;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.plugin.service.NKServiceManager;
import cn.nukkit.plugin.service.ServiceManager;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.Potion;
import cn.nukkit.resourcepacks.ResourcePackManager;
import cn.nukkit.resourcepacks.loader.JarPluginResourcePackLoader;
import cn.nukkit.resourcepacks.loader.ZippedResourcePackLoader;
import cn.nukkit.scheduler.ServerScheduler;
import cn.nukkit.scheduler.Task;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.*;
import cn.nukkit.utils.bugreport.ExceptionHandler;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.extern.log4j.Log4j2;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * The main server class
 *
 * @author MagicDroidX
 * @author Box
 */
@Log4j2
public class Server {

    /**
     * Permission to receive admin broadcasts such as command usage.
     */
    public static final String BROADCAST_CHANNEL_ADMINISTRATIVE = "nukkit.broadcast.admin";
    /**
     * Permission to receive common broadcasts such as join/quit/death/achievement messages.
     */
    public static final String BROADCAST_CHANNEL_USERS = "nukkit.broadcast.user";

    private static Server instance;

    private final BanList banByName;
    private final BanList banByIP;
    private final Config operators;
    private final Config whitelist;
    private final Config properties;
    private final Config mapInfo;

    private final String filePath;
    private final String dataPath;
    private final String pluginPath;

    private final PluginManager pluginManager;
    private final ServerScheduler scheduler;
    private final BaseLang baseLang;
    private final NukkitConsole console;
    private final ConsoleThread consoleThread;
    private final SimpleCommandMap commandMap;
    private final CraftingManager craftingManager;
    private final ResourcePackManager resourcePackManager;
    private final ConsoleCommandSender consoleSender;

    private boolean hasStopped;
    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private int tickCounter;
    private long nextTick;
    private final float[] tickAverage = {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20};
    private final float[] useAverage = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private float maxTick = 20;
    private float maxUse;
    private int baseTickRate;
    private int autoSaveTicker;
    private int maxPlayers; // setMaxPlayers
    private boolean autoSave = true; // setAutoSave
    private int difficulty; // setDifficulty
    int spawnThresholdRadius;
    private final boolean suomiCraftPEMode;
    private String ip;
    private int port;
    private final UUID serverID;
    private RCON rcon;
    private final Network network;
    private QueryHandler queryHandler;
    private QueryRegenerateEvent queryRegenerateEvent;
    private final EntityMetadataStore entityMetadata;
    private final PlayerMetadataStore playerMetadata;
    private final LevelMetadataStore levelMetadata;

    private final Map<InetSocketAddress, Player> players = new HashMap<>();
    final Map<UUID, Player> playerList = new HashMap<>();

    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}.dat$");
    private Level[] levelArray = new Level[0];    private final Map<Integer, Level> levels = new HashMap<Integer, Level>() {
        public Level put(Integer key, Level value) {
            Level result = super.put(key, value);
            levelArray = levels.values().toArray(new Level[0]);
            return result;
        }

        public boolean remove(Object key, Object value) {
            boolean result = super.remove(key, value);
            levelArray = levels.values().toArray(new Level[0]);
            return result;
        }

        public Level remove(Object key) {
            Level result = super.remove(key);
            levelArray = levels.values().toArray(new Level[0]);
            return result;
        }
    };
    private final ServiceManager serviceManager = new NKServiceManager();
    private Level defaultLevel;
    private final Thread currentThread;
    private Watchdog watchdog;
    private DB nameLookup;
    private PlayerDataSerializer playerDataSerializer;
    private TaskHandler spawnerTask;
    private final BatchingHelper batchingHelper;
    /**
     * Worlds where automatic mob spawning is disabled.
     */
    public static final Set<String> disabledSpawnWorlds = new HashSet<>();
    /**
     * Worlds where periodic automatic saving is disabled.
     */
    public static final Set<String> nonAutoSaveWorlds = new HashSet<>();
    /**
     * Worlds that have their own nether worlds.
     */
    public static final Set<String> multiNetherWorlds = new HashSet<>();
    /**
     * Worlds that have their own end worlds.
     */
    public static final Set<String> multiEndWorlds = new HashSet<>();
    /**
     * Worlds where random block ticking is disabled.
     */
    public static final Set<String> noTickingWorlds = new HashSet<>();
    /**
     * Worlds where anti xray is enabled.
     */
    public static final Set<String> antiXrayWorlds = new HashSet<>();
    /**
     * The server's MOTD. Remember to call network.setName() when updated.
     */
    private String motd;
    /**
     * Disconnection message shown to players who are not allowed to join due to whitelist.
     */
    String whitelistReason;
    /**
     * Mob AI enabled.
     */
    private boolean mobAiEnabled;
    /**
     * Default player data saving enabled.
     */
    boolean shouldSavePlayerData;
    /**
     * Anti fly checks enabled.
     */
    private boolean allowFlight;
    /**
     * Hardcore mode enabled.
     */
    private boolean isHardcore;
    /**
     * Force resource packs.
     */
    private boolean forceResources;
    /**
     * Force player gamemode to default on every join.
     */
    private boolean forceGamemode;
    /**
     * Level garbage collection between ticks enabled.
     */
    private boolean doLevelGC;
    /**
     * Call BatchPacketsEvent on batch packet sending.
     */
    public boolean callBatchPkEvent;
    /**
     * Whitelist enabled.
     */
    public boolean whitelistEnabled;
    /**
     * Xbox authentication enabled.
     */
    public boolean xboxAuth;
    /**
     * Spawn eggs enabled.
     */
    public boolean spawnEggsEnabled;
    /**
     * Call DataPacketSendEvent on data packet sending.
     */
    public boolean callDataPkSendEvent;
    /**
     * Bed spawnpoints enabled.
     */
    public boolean bedSpawnpoints;
    /**
     * Server side achievements enabled.
     */
    boolean achievementsEnabled;
    /**
     * Pvp enabled. Can be changed per world using game rules.
     */
    boolean pvpEnabled;
    /**
     * Announce server side announcements to all players.
     */
    boolean announceAchievements;
    /**
     * Disable player interaction spam limiter.
     */
    boolean doNotLimitInteractions;
    /**
     * Whether vanilla mob despawning outside activation range is enabled.
     */
    public boolean despawnMobs;
    /**
     * How many chunks are sent to player per tick.
     */
    public int chunksPerTick;
    /**
     * How many chunks needs to be sent before the player can spawn.
     */
    int spawnThreshold;
    /**
     * Zlib compression level for sent packets.
     */
    public int networkCompressionLevel;
    /**
     * Maximum view distance in chunks.
     */
    private int viewDistance;
    /**
     * Server's default gamemode.
     */
    public int gamemode;
    /**
     * Minimum amount of time between player skin changes.
     */
    private int skinChangeCooldown;
    /**
     * Spawn protection radius.
     */
    private int spawnRadius;
    /**
     * Minimum allowed protocol version. Set -1 to disable multiversion.
     */
    public int minimumProtocol;
    /**
     * How often auto save should happen.
     */
    private int autoSaveTicks;
    /**
     * Limit automatic tick rate.
     */
    private int autoTickRateLimit;
    /**
     * How many seconds a chunk should be unused before it's unloaded.
     */
    public int chunkUnloadDelay;
    /**
     * Do not limit the maximum size of player skins.
     */
    public boolean doNotLimitSkinGeometry;
    /**
     * Mob spawning from blocks and items enabled.
     */
    public boolean mobsFromBlocks;
    /**
     * Explosions breaking blocks enabled.
     */
    public boolean explosionBreakBlocks;
    /**
     * Boss bars enabled for wither and ender dragon.
     */
    public boolean vanillaBossBar;
    /**
     * Showing plugins in query enabled.
     */
    public boolean queryPlugins;
    /**
     * Strong RakNet level IP bans enabled.
     */
    public boolean strongIPBans;
    /**
     * Player data is saved by player uuid instead of by player name.
     */
    public boolean savePlayerDataByUuid; // public for plugins
    /**
     * More vanilla like portal logics enabled.
     */
    boolean vanillaPortals;
    /**
     * Persona skins allowed.
     */
    boolean personaSkins;
    /**
     * Chunk caching enabled.
     */
    public boolean cacheChunks;
    /**
     * Check for new releases automatically.
     */
    boolean updateChecks;
    /**
     * Include player's location in the login message.
     */
    boolean logJoinLocation;
    /**
     * Make redstone work but with possible crash exploits.
     */
    public boolean unsafeRedstone;
    /**
     * Whether attacking an entity should stop player from sprinting.
     */
    boolean attackStopSprint;
    /**
     * Enable automatic tick rate adjustments.
     */
    private boolean autoTickRate;
    /**
     * Force server side translations.
     */
    private boolean forceLanguage;
    /**
     * Always tick players.
     */
    private boolean alwaysTickPlayers;
    /**
     * Reduce network traffic by not sending some non-critical data.
     */
    public boolean reduceTraffic;
    /**
     * Don't disable client's own packs when force-resources is enabled.
     */
    boolean forceResourcesAllowOwnPacks;
    /**
     * Enable encryption.
     */
    boolean encryptionEnabled;
    /**
     * Use Snappy for packet compression for 1.19.30+ clients.
     */
    public final boolean useSnappy;
    /**
     * Batch packets smaller than this will not be compressed.
     */
    public int networkCompressionThreshold;
    /**
     * Use raw ore drops for iron and gold.
     */
    public boolean useRawOres;
    /**
     * Temporary disable world saving to allow safe backup of leveldb worlds.
     */
    public boolean holdWorldSave;
    /**
     * How close to player a mob must be for it to be ticked (squared distance)
     */
    public double entityActivationRange;
    /**
     * Whether "Unbreakable" nbt should be ignored in Item isUnbreakable()
     */
    public boolean ignoreUnbreakableItems;
    Server(final String filePath, String dataPath, String pluginPath, boolean loadPlugins, boolean debug) {
        Preconditions.checkState(instance == null, "Already initialized!");
        instance = this;
        currentThread = Thread.currentThread(); // Saves the current thread instance as a reference, used in Server#isPrimaryThread()
        log.info("§b-- §cNukkit §aPetteriM1 Edition §b--");

        this.filePath = filePath;
        if (!new File(dataPath + "worlds/").exists()) {
            //noinspection ResultOfMethodCallIgnored
            new File(dataPath + "worlds/").mkdirs();
        }

        if (!new File(pluginPath).exists()) {
            //noinspection ResultOfMethodCallIgnored
            new File(pluginPath).mkdirs();
        }

        this.dataPath = new File(dataPath).getAbsolutePath() + '/';
        this.pluginPath = new File(pluginPath).getAbsolutePath() + '/';

        log.info("Loading server properties...");
        this.properties = new Config(this.dataPath + "server.properties", Config.PROPERTIES, new ServerProperties());

        int debugLvl = NukkitMath.clamp(this.getPropertyInt("debug-level", 1), 1, 3);
        if (debug && debugLvl < 2) {
            debugLvl = 2;
        }
        Nukkit.DEBUG = debugLvl;

        // Settings that should not be modified after startup
        this.useSnappy = this.getPropertyBoolean("use-snappy-compression", false);
        Normal.seaHeight = this.getPropertyInt("generator-force-sea-height", 64); // default 64 for backwards compatibility, 62 on vanilla
        this.suomiCraftPEMode = this.getPropertyBoolean("suomicraftpe-mode", false); // not in default config

        if (this.getPropertyBoolean("legacy-protocol-support", false)) {
            ProtocolInfo.ENABLED_PROTOCOLS.addAll(Arrays.asList(ProtocolInfo.v1_2_0, ProtocolInfo.v1_2_5_11, ProtocolInfo.v1_2_5, ProtocolInfo.v1_2_6, ProtocolInfo.v1_2_7,
                    ProtocolInfo.v1_2_10, ProtocolInfo.v1_2_13, ProtocolInfo.v1_2_13_11, ProtocolInfo.v1_4_0, ProtocolInfo.v1_5_0, ProtocolInfo.v1_6_0_5, ProtocolInfo.v1_6_0,
                    ProtocolInfo.v1_7_0, ProtocolInfo.v1_8_0, ProtocolInfo.v1_9_0, ProtocolInfo.v1_10_0, ProtocolInfo.v1_11_0, ProtocolInfo.v1_12_0));
        }

        this.loadSettings();

        if (debugLvl < 2/*this.getPropertyBoolean("automatic-bug-report", true)*/) {
            ExceptionHandler.initSentry();
            ExceptionHandler.registerExceptionHandler();
        }

        if (!new File(dataPath + "players/").exists() && this.shouldSavePlayerData) {
            //noinspection ResultOfMethodCallIgnored
            new File(dataPath + "players/").mkdirs();
        }

        this.baseLang = new BaseLang(BaseLang.FALLBACK_LANGUAGE); // Only English is implemented
        this.playerDataSerializer = new DefaultPlayerDataSerializer(this);
        this.console = new NukkitConsole();
        this.consoleThread = new ConsoleThread();
        this.consoleThread.start();

        Object poolSize = this.getProperty("async-workers", "auto");
        if (!(poolSize instanceof Integer)) {
            try {
                poolSize = Integer.valueOf((String) poolSize);
            } catch (Exception e) {
                poolSize = Math.max(Runtime.getRuntime().availableProcessors() + 1, 4);
            }
        }

        ServerScheduler.WORKERS = (int) poolSize;

        this.scheduler = new ServerScheduler();

        this.console.setExecutingCommands(true); // Scheduler needs to be ready

        this.batchingHelper = new BatchingHelper();

        if (this.getPropertyBoolean("enable-rcon", false)) {
            try {
                this.rcon = new RCON(this, this.getPropertyString("rcon.password", ""), (!this.getIp().isEmpty()) ? this.getIp() : "0.0.0.0", this.getPropertyInt("rcon.port", this.getPort()));
            } catch (IllegalArgumentException e) {
                log.error(baseLang.translateString(e.getMessage(), e.getCause().getMessage()));
            }
        }

        this.mapInfo = new Config(this.dataPath + "maps.yml", Config.YAML);
        ItemMap.mapCount = this.mapInfo.getLong("map_count_do_not_edit");

        this.entityMetadata = new EntityMetadataStore();
        this.playerMetadata = new PlayerMetadataStore();
        this.levelMetadata = new LevelMetadataStore();

        this.operators = new Config(this.dataPath + "ops.txt", Config.ENUM);
        this.whitelist = new Config(this.dataPath + "white-list.txt", Config.ENUM);
        this.banByName = new BanList(this.dataPath + "banned-players.json");
        this.banByName.load();
        this.banByIP = new BanList(this.dataPath + "banned-ips.json");
        this.banByIP.load();

        org.apache.logging.log4j.Level currentLevel = Nukkit.getLogLevel();
        for (org.apache.logging.log4j.Level level : org.apache.logging.log4j.Level.values()) {
            if (level.intLevel() == (Nukkit.DEBUG + 3) * 100 && level.intLevel() > currentLevel.intLevel()) {
                Nukkit.setLogLevel(level);
                break;
            }
        }

        this.consoleSender = new ConsoleCommandSender();
        this.commandMap = new SimpleCommandMap(this);

        registerEntities();
        registerBlockEntities();

        Block.init();
        Enchantment.init();
        GlobalBlockPalette.init();
        RuntimeItems.init();
        Item.init();
        //noinspection ResultOfMethodCallIgnored
        EnumBiome.values();
        Effect.init();
        Potion.init();
        Attribute.init();
        DispenseBehaviorRegister.init();
        Skin.initDefaultSkin();
        CustomBlockManager.init(this);
        //noinspection ResultOfMethodCallIgnored
        EntityManager.get();
        //noinspection ResultOfMethodCallIgnored
        BiomeDefinitionListPacket.getCachedPacket(ProtocolInfo.CURRENT_PROTOCOL);
        //noinspection ResultOfMethodCallIgnored
        TrimDataPacket.getCachedPacket(ProtocolInfo.CURRENT_PROTOCOL);

        // Convert legacy data before plugins get the chance to mess with it
        try {
            if (this.shouldSavePlayerData || this.savePlayerDataByUuid) {
                if (this.getPropertyBoolean("use-old-leveldb", false)) {
                    this.getLogger().debug("nameLookup: Using old LevelDB");
                    nameLookup = org.iq80.oldleveldb.impl.Iq80DBFactory.factory.open(new File(dataPath, "players"),
                            new Options().createIfMissing(true).compressionType(CompressionType.ZLIB_RAW));
                } else {
                    nameLookup = Iq80DBFactory.factory.open(new File(dataPath, "players"),
                            new Options().createIfMissing(true).compressionType(CompressionType.ZLIB_RAW));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (this.savePlayerDataByUuid) {
            convertLegacyPlayerData();
        }

        String uuid = this.getPropertyString("uuid", null);
        UUID serverID;
        try {
            serverID = UUID.fromString(uuid);
        } catch (Exception ignore) {
            serverID = UUID.randomUUID();
        }
        this.serverID = serverID;

        this.craftingManager = new CraftingManager();
        this.resourcePackManager = new ResourcePackManager(
                new ZippedResourcePackLoader(new File(Nukkit.DATA_PATH, "resource_packs")),
                new JarPluginResourcePackLoader(new File(this.pluginPath))
        );

        this.pluginManager = new PluginManager(this, this.commandMap);
        this.pluginManager.subscribeToPermission(Server.BROADCAST_CHANNEL_ADMINISTRATIVE, this.consoleSender);
        this.pluginManager.registerInterface(JavaPluginLoader.class);

        this.queryRegenerateEvent = new QueryRegenerateEvent(this, 5);

        log.info(this.baseLang.translateString("nukkit.server.networkStart", new String[]{this.getIp().isEmpty() ? "*" : this.getIp(), String.valueOf(this.getPort())}));
        this.network = new Network(this);
        this.network.setName(this.getMotd());
        this.network.setSubName(this.getSubMotd());
        this.network.registerInterface(new RakNetInterface(this));

        if (!this.encryptionEnabled) {
            this.getLogger().warning("Encryption is not enabled! For better security, it's recommended to enable it (encryption=on) if you don't use a proxy software.");
        }

        if (!this.xboxAuth && !this.suomiCraftPEMode) {
            this.getLogger().warning("Xbox authentication is not enabled! It's recommended to enable it (xbox-auth=on) if you don't use a proxy software or an authentication plugin.");
        }

        if (loadPlugins) {
            this.pluginManager.loadPlugins(this.pluginPath);
            this.enablePlugins(PluginLoadOrder.STARTUP);
        }

        boolean regenerateItemPalette = false;

        try {
            if (CustomBlockManager.get().closeRegistry()) {
                regenerateItemPalette = true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to init custom blocks", e);
        }

        if (CustomItemManager.get().closeRegistry()) {
            regenerateItemPalette = true;
        }

        if (regenerateItemPalette) {
            for (RuntimeItemMapping mapping : RuntimeItems.VALUES) {
                mapping.generatePalette();
            }
        }

        EntityManager.get().closeRegistry();

        Item.initCreativeItems();

        // We don't call craftingManager.rebuildPacket() here anymore as we only want to generate cached packet for versions players use
        craftingManager.getCachedPacket(ProtocolInfo.CURRENT_PROTOCOL); // Always cache the packet for the current protocol

        LevelProviderManager.addProvider(this, Anvil.class);
        LevelProviderManager.addProvider(this, LevelDBProvider.class);

        Generator.addGenerator(Flat.class, "flat", Generator.TYPE_FLAT);
        Generator.addGenerator(Normal.class, "normal", Generator.TYPE_INFINITE);
        Generator.addGenerator(Normal.class, "default", Generator.TYPE_INFINITE);
        Generator.addGenerator(Nether.class, "nether", Generator.TYPE_NETHER);
        Generator.addGenerator(TheEnd.class, "the_end", Generator.TYPE_THE_END);
        Generator.addGenerator(cn.nukkit.level.generator.Void.class, "void", Generator.TYPE_VOID);

        if (this.defaultLevel == null) {
            String defaultName = this.getPropertyString("level-name", "world");
            if (defaultName == null || defaultName.trim().isEmpty()) {
                this.getLogger().warning("level-name cannot be null, using default");
                defaultName = "world";
                this.setPropertyString("level-name", defaultName);
            }

            if (!this.loadLevel(defaultName)) {
                long seed;
                String seedString = String.valueOf(this.getProperty("level-seed", System.currentTimeMillis()));
                try {
                    seed = Long.parseLong(seedString);
                } catch (NumberFormatException e) {
                    seed = seedString.hashCode();
                }
                this.generateLevel(defaultName, seed == 0 ? System.currentTimeMillis() : seed);
            }

            this.setDefaultLevel(this.getLevelByName(defaultName));
        }

        if (this.defaultLevel == null) {
            this.getLogger().emergency(this.baseLang.translateString("nukkit.level.defaultError"));
            this.forceShutdown();
            return;
        }

        this.properties.save(true);

        //for (Map.Entry<Integer, Level> entry : this.getLevels().entrySet()) {
        Level level = this.defaultLevel;//entry.getValue();
        this.getLogger().debug("Preparing spawn region for level " + level.getName());
        Vector3 spawn = level.getProvider().getSpawn();
        level.populateChunk(spawn.getChunkX(), spawn.getChunkZ(), true);
        //}

        // Load levels
        if (this.getPropertyBoolean("load-all-worlds", true)) {
            try {
                for (File fs : new File(new File("").getCanonicalPath() + "/worlds/").listFiles()) {
                    if ((fs.isDirectory() && !this.isLevelLoaded(fs.getName()))) {
                        this.loadLevel(fs.getName());
                    }
                }
                EnumLevel.initLevels();
            } catch (Exception e) {
                this.getLogger().error("Unable to load levels", e);
            }
        }

        checkMobSpawnerTask();

        if (loadPlugins) {
            this.enablePlugins(PluginLoadOrder.POSTWORLD);
        }

        int watchdogTick = this.getPropertyInt("thread-watchdog-tick", 60000);
        if (watchdogTick > 0) {
            this.watchdog = new Watchdog(this, watchdogTick);
            this.watchdog.start();
        }

        if (!this.getPropertyBoolean("disable-bstats-metrics", false)) {
            new NukkitMetrics(this);
        }

        Plugin wgext = this.getPluginManager().getPlugin("WorldGeneratorExtension");
        if (wgext == null) {
            if (this.defaultLevel.getGenerator() instanceof Normal) {
                this.getLogger().warning("WorldGeneratorExtension not found! If you want structures to generate, you can download it from https://github.com/PetteriM1/WorldGeneratorExtension/releases");
            }
        } else if ("worldgeneratorextension.Loader".equals(wgext.getDescription().getMain()) && Integer.parseInt(wgext.getDescription().getVersion().split("\\.")[1]) < 8) {
            this.getLogger().warning("There is a new version of WorldGeneratorExtension available! You can download it from https://github.com/PetteriM1/WorldGeneratorExtension/releases");
        }

        this.getLogger().debug("Checking for updates...");
        this.updateNotification(this.getConsoleSender(), false);

        this.start();
    }
    private static final byte[] PREFIX = {(byte) 0xfe, (byte) 0xfd};
    private int lastLevelGC;

    /**
     * This class contains all default server.properties values.
     */
    private static class ServerProperties extends ConfigSection {
        {
            put("motd", "A Minecraft Server");
            put("sub-motd", "Powered by Nukkit PM1E");
            put("server-port", 19132);
            put("server-ip", "0.0.0.0");
            put("view-distance", 8);
            put("achievements", true);
            put("announce-player-achievements", true);
            put("spawn-protection", 10);
            put("max-players", 50);
            put("spawn-animals", true);
            put("spawn-mobs", true);
            put("gamemode", 0);
            put("force-gamemode", true);
            put("difficulty", 2);
            put("hardcore", false);
            put("pvp", true);
            put("white-list", false);
            put("whitelist-reason", "Server is white-listed");
            put("generator-settings", "");
            put("level-name", "world");
            put("level-seed", "");
            put("level-type", "default");
            put("generator-force-sea-height", 64);
            put("enable-rcon", false);
            put("rcon.password", Base64.getEncoder().encodeToString(UUID.randomUUID().toString().replace("-", "").getBytes(StandardCharsets.UTF_8)).substring(3, 13));
            put("force-resources", false);
            put("force-resources-allow-client-packs", false);
            put("xbox-auth", true);
            put("encryption", true);
            put("auto-save", true);
            put("save-player-data", true);
            put("bed-spawnpoints", true);
            put("explosion-break-blocks", true);
            put("stop-in-game", false);
            put("op-in-game", true);
            put("attack-stop-sprint", true);
            put("use-raw-ores", true);
            put("spawn-eggs", true);
            put("mob-ai", true);
            put("force-language", false);
            put("shutdown-message", "Server closed");
            put("enable-query", false);
            put("query-plugins", false);
            put("debug-level", 1);
            put("async-workers", "auto");
            put("auto-tick-rate", true);
            put("auto-tick-rate-limit", 20);
            put("base-tick-rate", 1);
            put("always-tick-players", false);
            put("clear-chunk-tick-list", false);
            put("do-level-gc", true);
            put("spawn-threshold", 56);
            put("entity-activation-blocks", 80);
            put("chunk-sending-per-tick", 4);
            put("chunk-ticking-per-tick", 40);
            put("chunk-ticking-radius", 3);
            put("chunk-unload-delay", 30);
            put("chunk-generation-population-queue-size", 8);
            put("ticks-per-autosave", 6000);
            put("ticks-per-entity-spawns", 200);
            put("entity-despawn-task", true);
            put("thread-watchdog-tick", 60000);
            put("allow-nether", true);
            put("allow-the-end", true);
            put("vanilla-portals", true);
            put("multi-nether-worlds", "");
            put("multi-end-worlds", "");
            put("do-not-tick-worlds", "");
            put("worlds-entity-spawning-disabled", "");
            put("anti-xray-worlds", "");
            put("worlds-level-auto-save-disabled", "");
            put("load-all-worlds", true);
            put("ansi-title", true);
            put("block-listener", true);
            put("allow-flight", false);
            put("vanilla-bossbars", true);
            put("strong-ip-bans", false);
            put("save-player-data-by-uuid", true);
            put("log-join-location", true);
            //put("automatic-bug-report", true);
            //put("bstats-metrics", true);
            put("update-notifications", true);
            put("do-not-limit-interactions", false);
            put("do-not-limit-skin-geometry", true);
            put("persona-skins", true);
            put("skin-change-cooldown", 15);
            put("compression-level", 5);
            put("compression-threshold", 256);
            put("use-snappy-compression", false);
            put("max-mtu", 1400);
            put("timeout-milliseconds", 20000);
            put("multiversion-min-protocol", 0);
            put("reduce-traffic", false);
            put("cache-chunks", false);
            put("leveldb-cache-mb", 80);
            put("unsafe-redstone", true);
            put("new-blocks-preview", false);
            put("force-new-generator", false);
            put("uuid", UUID.randomUUID().toString());
        }
    }

    private class ConsoleThread extends Thread implements InterruptibleThread {

        @Override
        public void run() {
            console.start();
        }
    }

    public void addOnlinePlayer(Player player) {
        this.playerList.put(player.getUniqueId(), player);
        player.updatePlayerListData(false);
    }

    /**
     * Give player the operator status
     *
     * @param name player name
     */
    public void addOp(String name) {
        this.operators.set(name.toLowerCase(Locale.ROOT), true);
        Player player = this.getPlayerExact(name);
        if (player != null) {
            player.recalculatePermissions();
        }
        this.operators.save(true);
    }

    public void addPlayer(InetSocketAddress socketAddress, Player player) {
        this.players.put(socketAddress, player);
    }

    /**
     * Register a recipe to CraftingManager.
     * Please use getCraftingManager().registerRecipe(protocol, recipe) instead
     *
     * @param recipe recipe
     */
    public void addRecipe(Recipe recipe) {
        this.craftingManager.registerRecipe(recipe);
    }

    /**
     * Add a player to whitelist
     *
     * @param name player name
     */
    public void addWhitelist(String name) {
        this.whitelist.set(name.toLowerCase(Locale.ROOT), true);
        this.whitelist.save(true);
    }

    public void batchPackets(Player[] players, DataPacket[] packets) {
        this.batchingHelper.batchPackets(this, players, packets);
    }

    public int broadcast(String message, String permissions) {
        Set<CommandSender> recipients = new HashSet<>();

        for (String permission : permissions.split(";")) {
            for (Permissible permissible : this.pluginManager.getPermissionSubscriptions(permission)) {
                if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                    recipients.add((CommandSender) permissible);
                }
            }
        }

        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    public int broadcast(TextContainer message, String permissions) {
        Set<CommandSender> recipients = new HashSet<>();

        for (String permission : permissions.split(";")) {
            for (Permissible permissible : this.pluginManager.getPermissionSubscriptions(permission)) {
                if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                    recipients.add((CommandSender) permissible);
                }
            }
        }

        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    @SuppressWarnings("UnusedReturnValue")
    public int broadcastMessage(String message) {
        return this.broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    @SuppressWarnings("UnusedReturnValue")
    public int broadcastMessage(TextContainer message) {
        return this.broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    public int broadcastMessage(String message, CommandSender[] recipients) {
        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.length;
    }

    @SuppressWarnings("UnusedReturnValue")
    public int broadcastMessage(String message, Collection<? extends CommandSender> recipients) {
        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    public int broadcastMessage(TextContainer message, Collection<? extends CommandSender> recipients) {
        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    public static void broadcastPacket(Collection<Player> players, DataPacket packet) {
        for (Player player : players) {
            player.dataPacket(packet);
        }
    }

    public static void broadcastPacket(Player[] players, DataPacket packet) {
        for (Player player : players) {
            player.dataPacket(packet);
        }
    }

    private void checkMobSpawnerTask() {
        // loadSettings is called before scheduler is created on startup
        if (this.scheduler == null) {
            return;
        }

        boolean spawnAnimals = this.getPropertyBoolean("spawn-animals", true);
        boolean spawnMonsters = this.getPropertyBoolean("spawn-mobs", true);

        if (this.spawnerTask == null) {
            if (spawnAnimals || spawnMonsters) {
                // Run the spawner on 2x speed but spawn only either monsters or animals
                int spawnerTicks = Math.max(this.getPropertyInt("ticks-per-entity-spawns", 200), 2) >> 1;
                this.spawnerTask = this.scheduler.scheduleDelayedRepeatingTask(new SpawnerTask(spawnAnimals, spawnMonsters), spawnerTicks, spawnerTicks);
            }
        } else {
            this.spawnerTask.cancel();

            if (spawnAnimals || spawnMonsters) {
                // Run the spawner on 2x speed but spawn only either monsters or animals
                int spawnerTicks = Math.max(this.getPropertyInt("ticks-per-entity-spawns", 200), 2) >> 1;
                this.spawnerTask = this.scheduler.scheduleDelayedRepeatingTask(new SpawnerTask(spawnAnimals, spawnMonsters), spawnerTicks, spawnerTicks);
            } else {
                this.spawnerTask = null;
            }
        }
    }

    private void checkTickUpdates(int currentTick) {
        if (this.alwaysTickPlayers) {
            for (Player p : new ArrayList<>(this.players.values())) {
                p.onUpdate(currentTick);
            }
        }

        for (Player p : this.getOnlinePlayersList()) {
            p.resetPacketCounters();
        }

        // Do level ticks
        for (Level level : this.levelArray) {
            if (level.isBeingConverted || (level.getTickRate() > this.baseTickRate && --level.tickRateCounter > 0)) {
                continue;
            }

            try {
                long levelTime = System.currentTimeMillis();
                level.doTick(currentTick);
                int tickMs = (int) (System.currentTimeMillis() - levelTime);
                level.tickRateTime = tickMs;

                if (this.autoTickRate) {
                    int limit = level.getAutoTickRateThreshold();

                    if (tickMs < limit && level.getTickRate() > this.baseTickRate) {
                        int r;
                        level.setTickRate(r = level.getTickRate() - 1);
                        if (r > this.baseTickRate) {
                            level.tickRateCounter = level.getTickRate();
                        }
                        this.getLogger().debug("Raising level \"" + level.getName() + "\" tick rate to " + level.getTickRate() + " ticks");
                    } else if (tickMs >= limit) {
                        if (level.getTickRate() == this.baseTickRate) {
                            level.setTickRate(Math.max(this.baseTickRate + 1, Math.min(this.autoTickRateLimit, tickMs / limit)));
                            this.getLogger().debug("Level \"" + level.getName() + "\" took " + tickMs + "ms, setting tick rate to " + level.getTickRate() + " ticks");
                        } else if ((tickMs / level.getTickRate()) >= limit && level.getTickRate() < this.autoTickRateLimit) {
                            level.setTickRate(level.getTickRate() + 1);
                            this.getLogger().debug("Level \"" + level.getName() + "\" took " + tickMs + "ms, setting tick rate to " + level.getTickRate() + " ticks");
                        }
                        level.tickRateCounter = level.getTickRate();
                    }
                }
            } catch (Exception e) {
                log.error(this.baseLang.translateString("nukkit.level.tickError", new String[]{level.getFolderName(), Utils.getExceptionMessage(e)}));
            }
        }
    }

    /**
     * Internal: Convert legacy player saves to the uuid based saving
     */
    private void convertLegacyPlayerData() {
        File dataDirectory = new File(getDataPath(), "players/");

        File[] files = dataDirectory.listFiles(file -> {
            String name = file.getName();
            return !UUID_PATTERN.matcher(name).matches() && name.endsWith(".dat");
        });

        if (files == null) {
            return;
        }

        for (File legacyData : files) {
            String name = legacyData.getName();
            // Remove file extension
            name = name.substring(0, name.length() - 4);

            log.debug("Attempting legacy player data conversion for {}", name);

            CompoundTag tag = getOfflinePlayerDataInternal(name, false, false);

            if (tag == null || !tag.contains("UUIDLeast") || !tag.contains("UUIDMost")) {
                // No UUID so we cannot convert. Wait until player logs in.
                continue;
            }

            UUID uuid = new UUID(tag.getLong("UUIDMost"), tag.getLong("UUIDLeast"));
            if (!tag.contains("NameTag")) {
                tag.putString("NameTag", name);
            }

            if (new File(getDataPath() + "players/" + uuid + ".dat").exists()) {
                // We don't want to overwrite existing data.
                continue;
            }

            saveOfflinePlayerData(uuid.toString(), tag, false, false);

            // Add name to lookup table
            updateName(uuid, name);

            // Delete legacy data
            if (!legacyData.delete()) {
                log.warn("Unable to delete legacy data for {}", name);
            }
        }
    }

    /**
     * Disable all loaded plugins
     */
    public void disablePlugins() {
        this.pluginManager.disablePlugins();
    }

    /**
     * Run a command as CommandSender. Use server.getConsoleSender() to run as CONSOLE.
     *
     * @param sender      command sender
     * @param commandLine command without slash
     * @return command was found and attempted to be executed
     */
    public boolean dispatchCommand(CommandSender sender, String commandLine) throws ServerException {
        // First we need to check if this command is on the main thread or not, if not, warn the user
        if (!this.isPrimaryThread()) {
            getLogger().warning("Command dispatched asynchronously: " + commandLine);
        }

        if (sender == null) {
            throw new ServerException("CommandSender is not valid");
        }

        if (this.commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.unknown", commandLine));

        return false;
    }

    public void doAutoSave() {
        if (this.autoSave) {
            log.debug("Running auto save...");

            for (Player player : this.players.values()) {
                if (player.isOnline()) {
                    player.save(true);
                }
            }

            int saveDelay = 0;
            for (Level level : this.levelArray) {
                if (level.getAutoSave()) {
                    this.scheduler.scheduleDelayedTask(null, () -> {
                        if (level.getProvider() != null) {
                            try {
                                level.save();
                            } catch (Exception ex) {
                                getLogger().error("Failed to auto save " + level.getName(), ex);
                                ExceptionHandler.handleSilently(ex);
                            }
                        }
                    }, saveDelay++);
                }
            }

            if (ItemMap.mapCount != this.mapInfo.getLong("map_count_do_not_edit")) {
                this.mapInfo.set("map_count_do_not_edit", ItemMap.mapCount);
                this.mapInfo.save(false, true);
            }
        }
    }

    /**
     * Enable a plugin
     *
     * @param plugin plugin
     */
    public void enablePlugin(Plugin plugin) {
        this.pluginManager.enablePlugin(plugin);
    }

    /**
     * Enable all plugins with matching load order
     *
     * @param type load order
     */
    public void enablePlugins(PluginLoadOrder type) {
        for (Plugin plugin : new ArrayList<>(this.pluginManager.getPlugins().values())) {
            if (!plugin.isEnabled() && type == plugin.getDescription().getOrder()) {
                this.enablePlugin(plugin);
            }
        }

        if (type == PluginLoadOrder.POSTWORLD) {
            DefaultPermissions.registerCorePermissions();
        }
    }

    /**
     * Shut down the server immediately.
     */
    public void forceShutdown() {
        this.forceShutdown(this.getPropertyString("shutdown-message", "Server closed").replace("§n", "\n"));
    }

    /**
     * Shut down the server immediately.
     *
     * @param reason message that shows to players on disconnect
     */
    public void forceShutdown(String reason) {
        if (this.hasStopped) {
            return;
        }

        try {
            isRunning.compareAndSet(true, false);

            this.hasStopped = true;

            ServerStopEvent serverStopEvent = new ServerStopEvent();
            pluginManager.callEvent(serverStopEvent);

            if (this.holdWorldSave) {
                this.getLogger().warning("World save hold was not released! Any backup currently being taken may be invalid");
            }

            if (this.rcon != null) {
                this.getLogger().debug("Closing RCON...");
                this.rcon.close();
            }

            this.getLogger().debug("Disconnecting all players...");
            for (Player player : new ArrayList<>(this.players.values())) {
                player.close(player.getLeaveMessage(), reason);
            }

            this.getLogger().debug("Disabling all plugins...");
            this.disablePlugins();

            this.getLogger().debug("Removing event handlers...");
            HandlerList.unregisterAll();

            this.getLogger().debug("Stopping all tasks...");
            this.scheduler.cancelAllTasks();
            this.scheduler.mainThreadHeartbeat(Integer.MAX_VALUE);

            RouteFinderThreadPool.shutdown();
            Config.shutdownWriter();

            this.getLogger().debug("Unloading all levels...");
            for (Level level : this.levelArray) {
                this.unloadLevel(level, true);
                this.nextTick = System.currentTimeMillis(); // Fix Watchdog killing the server while saving worlds
            }

            this.getLogger().debug("Saving map count...");
            this.mapInfo.set("map_count_do_not_edit", ItemMap.mapCount);
            this.mapInfo.save(false);

            this.getLogger().debug("Closing console...");
            this.consoleThread.interrupt();

            this.getLogger().debug("Closing BatchingHelper...");
            this.batchingHelper.shutdown();

            this.getLogger().debug("Stopping network interfaces...");
            for (SourceInterface interfaz : this.network.getInterfaces()) {
                interfaz.shutdown();
                this.network.unregisterInterface(interfaz);
            }

            if (nameLookup != null) {
                this.getLogger().debug("Closing name lookup DB...");
                nameLookup.close();
            }

            if (this.watchdog != null) {
                this.getLogger().debug("Stopping Watchdog...");
                this.watchdog.kill();
            }
        } catch (Exception e) {
            log.fatal("Exception happened while shutting down, exiting the process", e);
            System.exit(1);
        }
    }

    /**
     * Generate a new level
     *
     * @param name level name
     * @return generated
     */
    public boolean generateLevel(String name) {
        return this.generateLevel(name, ThreadLocalRandom.current().nextLong());
    }

    /**
     * Generate a new level
     *
     * @param name level name
     * @param seed level seed
     * @return generated
     */
    public boolean generateLevel(String name, long seed) {
        return this.generateLevel(name, seed, null);
    }

    /**
     * Generate a new level
     *
     * @param name      level name
     * @param seed      level seed
     * @param generator level generator
     * @return generated
     */
    public boolean generateLevel(String name, long seed, Class<? extends Generator> generator) {
        return this.generateLevel(name, seed, generator, new HashMap<>());
    }

    /**
     * Generate a new level
     *
     * @param name      level name
     * @param seed      level seed
     * @param generator level generator
     * @param options   level generator options
     * @return generated
     */
    public boolean generateLevel(String name, long seed, Class<? extends Generator> generator, Map<String, Object> options) {
        return generateLevel(name, seed, generator, options, null);
    }

    /**
     * Generate a new level
     *
     * @param name      level name
     * @param seed      level seed
     * @param generator level generator
     * @param options   level generator options
     * @param provider  level provider
     * @return generated
     */
    public boolean generateLevel(String name, long seed, Class<? extends Generator> generator, Map<String, Object> options, Class<? extends LevelProvider> provider) {
        if (Objects.equals(name.trim(), "") || this.isLevelGenerated(name)) {
            return false;
        }

        if (!options.containsKey("preset")) {
            options.put("preset", this.getPropertyString("generator-settings", ""));
        }

        if (generator == null) {
            generator = Generator.getGenerator(this.getLevelType());
        }

        if (provider == null) {
            provider = LevelProviderManager.getProviderByName("leveldb");
        }

        String path;

        if (name.contains("/") || name.contains("\\")) {
            path = name;

            if (this.suomiCraftPEMode) {
                String[] pathParts = path.split("/");
                name = pathParts[pathParts.length - 1];
            }
        } else {
            path = this.dataPath + "worlds/" + name + '/';
        }

        Level level;
        try {
            provider.getMethod("generate", String.class, String.class, long.class, Class.class, Map.class).invoke(null, path, name, seed, generator, options);

            level = new Level(this, name, path, provider);

            level.initLevel();

            this.levels.put(level.getId(), level);

            level.setTickRate(this.baseTickRate);
        } catch (Exception e) {
            log.error(this.baseLang.translateString("nukkit.level.generationError", new String[]{name, Utils.getExceptionMessage(e)}));
            return false;
        }

        this.pluginManager.callEvent(new LevelInitEvent(level));
        this.pluginManager.callEvent(new LevelLoadEvent(level));
        return true;
    }

    public boolean getAllowFlight() {
        return allowFlight;
    }

    public String getApiVersion() {
        return "9.9.9.PM1E-" + Nukkit.BUILD_VERSION_NUMBER;
    }

    public boolean getAutoSave() {
        return this.autoSave;
    }

    public String getCodename() {
        return "PM1E";
    }

    /**
     * Nukkit PM1E: Not implemented!
     * Load command aliases from config
     */
    public Map<String, List<String>> getCommandAliases() {
        return new LinkedHashMap<>();
    }

    /**
     * Get command map
     *
     * @return command map
     */
    public SimpleCommandMap getCommandMap() {
        return commandMap;
    }

    /**
     * Get server console CommandSender
     *
     * @return ConsoleCommandSender
     */
    public ConsoleCommandSender getConsoleSender() {
        return consoleSender;
    }

    public CraftingManager getCraftingManager() {
        return craftingManager;
    }

    public String getDataPath() {
        return dataPath;
    }

    public int getDefaultGamemode() {
        return this.getGamemode();
    }

    /**
     * Get default level
     *
     * @return default level
     */
    public Level getDefaultLevel() {
        return defaultLevel;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public static int getDifficultyFromString(String str) {
        switch (str.trim().toLowerCase(Locale.ROOT)) {
            case "0":
            case "peaceful":
            case "p":
                return 0;
            case "1":
            case "easy":
            case "e":
                return 1;
            case "2":
            case "normal":
            case "n":
                return 2;
            case "3":
            case "hard":
            case "h":
                return 3;
        }
        return -1;
    }

    /**
     * Get end world for a level
     *
     * @param world level
     * @return end world for that level
     */
    public Level getEndWorld(String world) {
        return multiEndWorlds.contains(world) ? this.getLevelByName(world + "-the_end") : this.getLevelByName("the_end");
    }

    public EntityMetadataStore getEntityMetadata() {
        return entityMetadata;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean getForceGamemode() {
        return this.forceGamemode;
    }

    public boolean getForceResources() {
        return this.forceResources;
    }

    public int getGamemode() {
        return gamemode;
    }

    public static int getGamemodeFromString(String str) {
        switch (str.trim().toLowerCase(Locale.ROOT)) {
            case "0":
            case "survival":
            case "s":
                return Player.SURVIVAL;
            case "1":
            case "creative":
            case "c":
                return Player.CREATIVE;
            case "2":
            case "adventure":
            case "a":
                return Player.ADVENTURE;
            case "3":
            case "spectator":
            case "spc":
            case "view":
            case "v":
                return Player.SPECTATOR;
            case "default":
                return Server.getInstance().getDefaultGamemode();
        }
        return -1;
    }

    public static String getGamemodeString(int mode) {
        return getGamemodeString(mode, false);
    }

    public static String getGamemodeString(int mode, boolean direct) {
        switch (mode) {
            case Player.SURVIVAL:
                return direct ? "Survival" : "%gameMode.survival";
            case Player.CREATIVE:
                return direct ? "Creative" : "%gameMode.creative";
            case Player.ADVENTURE:
                return direct ? "Adventure" : "%gameMode.adventure";
            case Player.SPECTATOR:
                return direct ? "Spectator" : "%gameMode.spectator";
        }
        return "UNKNOWN";
    }

    /**
     * Get list of IP bans
     *
     * @return IP bans
     */
    public BanList getIPBans() {
        return this.banByIP;
    }

    /**
     * Get the Server instance
     *
     * @return Server
     */
    public static Server getInstance() {
        return instance;
    }

    public String getIp() {
        return ip;
    }

    /**
     * Get BaseLang (server's default language)
     *
     * @return BaseLang
     */
    public BaseLang getLanguage() {
        return baseLang;
    }

    /**
     * Get a level by ID
     *
     * @param levelId level ID
     * @return Level or null
     */
    public Level getLevel(int levelId) {
        return this.levels.get(levelId);
    }

    /**
     * Get a level by name
     *
     * @param name level name
     * @return Level or null
     */
    public Level getLevelByName(String name) {
        if (this.suomiCraftPEMode && name.startsWith(getDataPath())) {
            String[] pathParts = name.split("/");
            name = pathParts[pathParts.length - 1];
        }

        for (Level level : this.levelArray) {
            if (level.getFolderName().equalsIgnoreCase(name)) {
                return level;
            }
        }

        return null;
    }

    public LevelMetadataStore getLevelMetadata() {
        return levelMetadata;
    }

    public String getLevelType() {
        return this.getPropertyString("level-type", "default");
    }

    /**
     * Get all levels
     *
     * @return levels
     */
    public Map<Integer, Level> getLevels() {
        return levels;
    }

    public MainLogger getLogger() {
        return MainLogger.getLogger();
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean getMobAiEnabled() {
        return this.mobAiEnabled;
    }

    /**
     * Get MOTD
     *
     * @return motd
     */
    public String getMotd() {
        return motd;
    }

    public String getName() {
        return Nukkit.NUKKIT_PM1E;
    }

    /**
     * Get list of banned players
     *
     * @return ban list
     */
    public BanList getNameBans() {
        return this.banByName;
    }

    /**
     * Get nether world for a level
     *
     * @param world level
     * @return nether world for that level
     */
    public Level getNetherWorld(String world) {
        return multiNetherWorlds.contains(world) ? this.getLevelByName(world + "-nether") : this.getLevelByName("nether");
    }

    /**
     * Get Network
     *
     * @return Network
     */
    public Network getNetwork() {
        return network;
    }

    public long getNextTick() {
        return nextTick;
    }

    public String getNukkitVersion() {
        return Nukkit.VERSION;
    }

    public IPlayer getOfflinePlayer(final String name) {
        IPlayer result = this.getPlayerExact(name);
        if (result != null) {
            return result;
        }

        return lookupName(name).map(uuid -> new OfflinePlayer(this, uuid, name))
                .orElse(new OfflinePlayer(this, name));
    }

    public IPlayer getOfflinePlayer(UUID uuid) {
        Preconditions.checkNotNull(uuid, "uuid");
        Optional<Player> onlinePlayer = getPlayer(uuid);
        if (onlinePlayer.isPresent()) {
            return onlinePlayer.get();
        }

        return new OfflinePlayer(this, uuid);
    }

    public CompoundTag getOfflinePlayerData(UUID uuid) {
        return getOfflinePlayerData(uuid, false);
    }

    public CompoundTag getOfflinePlayerData(UUID uuid, boolean create) {
        return getOfflinePlayerDataInternal(uuid.toString(), true, create);
    }

    public CompoundTag getOfflinePlayerData(String name) {
        return getOfflinePlayerData(name, false);
    }

    public CompoundTag getOfflinePlayerData(String name, boolean create) {
        name = name.toLowerCase(Locale.ROOT); // Make sure offline player data is found even if plugins gets it with non-lowercase name

        if (this.savePlayerDataByUuid) {
            Optional<UUID> uuid = lookupName(name);
            return getOfflinePlayerDataInternal(uuid.map(UUID::toString).orElse(name), true, create);
        } else {
            return getOfflinePlayerDataInternal(name, true, create);
        }
    }

    private CompoundTag getOfflinePlayerDataInternal(String name, boolean runEvent, boolean create) {
        Preconditions.checkNotNull(name, "name");

        PlayerDataSerializeEvent event = new PlayerDataSerializeEvent(name, playerDataSerializer);
        if (runEvent) {
            pluginManager.callEvent(event);
        }

        Optional<InputStream> dataStream = Optional.empty();
        try {
            dataStream = event.getSerializer().read(name, event.getUuid().orElse(null));
            if (dataStream.isPresent()) {
                return NBTIO.readCompressed(dataStream.get());
            }
        } catch (IOException e) {
            log.warn(this.getLanguage().translateString("nukkit.data.playerCorrupted", name), e);
        } finally {
            if (dataStream.isPresent()) {
                try {
                    dataStream.get().close();
                } catch (IOException e) {
                    log.throwing(e);
                }
            }
        }
        CompoundTag nbt = null;
        if (create) {
            Vector3 spawn = this.getDefaultLevel().getProvider().getSpawn();
            long time = System.currentTimeMillis();
            nbt = new CompoundTag()
                    .putLong("firstPlayed", time / 1000)
                    .putLong("lastPlayed", time / 1000)
                    .putList(new ListTag<DoubleTag>("Pos")
                            .add(new DoubleTag("0", spawn.x))
                            .add(new DoubleTag("1", spawn.y))
                            .add(new DoubleTag("2", spawn.z)))
                    .putString("Level", this.getDefaultLevel().getName())
                    .putList(new ListTag<>("Inventory"))
                    .putCompound("Achievements", new CompoundTag())
                    .putInt("playerGameType", this.getGamemode())
                    .putList(new ListTag<DoubleTag>("Motion")
                            .add(new DoubleTag("0", 0))
                            .add(new DoubleTag("1", 0))
                            .add(new DoubleTag("2", 0)))
                    .putList(new ListTag<FloatTag>("Rotation")
                            .add(new FloatTag("0", 0))
                            .add(new FloatTag("1", 0)))
                    .putFloat("FallDistance", 0)
                    .putShort("Fire", 0)
                    .putShort("Air", 400)
                    .putBoolean("OnGround", true)
                    .putBoolean("Invulnerable", false);

            this.saveOfflinePlayerData(name, nbt, true, runEvent);
        }
        return nbt;
    }

    /**
     * Get all online players
     *
     * @return online players
     */
    public Map<UUID, Player> getOnlinePlayers() {
        return ImmutableMap.copyOf(playerList);
    }

    /**
     * Get online player count
     *
     * @return online player count
     */
    public int getOnlinePlayersCount() {
        return this.playerList.size();
    }

    /**
     * Get all online players
     *
     * @return online players
     */
    public List<Player> getOnlinePlayersList() {
        return ImmutableList.copyOf(playerList.values());
    }

    /**
     * Get operator list config
     *
     * @return operators
     */
    public Config getOps() {
        return operators;
    }

    /**
     * Get an online player by uuid
     *
     * @param uuid uuid
     * @return Optional Player
     */
    public Optional<Player> getPlayer(UUID uuid) {
        Preconditions.checkNotNull(uuid, "uuid");
        return Optional.ofNullable(playerList.get(uuid));
    }

    /**
     * Get an online player by name
     *
     * @param name player name
     * @return Player or null
     */
    public Player getPlayer(String name) {
        Player found = null;
        name = name.toLowerCase(Locale.ROOT);
        int delta = Integer.MAX_VALUE;

        if (this.suomiCraftPEMode) {
            for (Player player : this.getOnlinePlayersList()) {
                if (player.getDisplayName().toLowerCase(Locale.ROOT).startsWith(name)) {
                    int curDelta = player.getDisplayName().length() - name.length();
                    if (curDelta < delta) {
                        found = player;
                        delta = curDelta;
                    }
                    if (curDelta == 0) {
                        break;
                    }
                }
            }

            return found;
        }

        for (Player player : this.getOnlinePlayersList()) {
            if (player.getName().toLowerCase(Locale.ROOT).startsWith(name)) {
                int curDelta = player.getName().length() - name.length();
                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                }
                if (curDelta == 0) {
                    break;
                }
            }
        }

        return found;
    }

    /**
     * Get player data serializer that is used to save player data
     *
     * @return player data serializer
     */
    public PlayerDataSerializer getPlayerDataSerializer() {
        return playerDataSerializer;
    }

    /**
     * Get an online player by exact player name
     *
     * @param name exact player name
     * @return Player or null
     */
    public Player getPlayerExact(String name) {
        if (this.suomiCraftPEMode) {
            int index = name.indexOf('(');
            if (index > 1 && name.length() > index + 1) { // DisplayName (Guest_XXXXXX)
                name = name.substring(index + 1, name.length() - 1);
            }
        }

        for (Player player : this.getOnlinePlayersList()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }

        return null;
    }

    public PlayerMetadataStore getPlayerMetadata() {
        return playerMetadata;
    }

    /**
     * How often player is allowed to change skin in game (in seconds)
     *
     * @return skin change cooldown
     */
    public int getPlayerSkinChangeCooldown() {
        return skinChangeCooldown;
    }

    /**
     * Get plugin commands
     *
     * @param name command name
     * @return PluginIdentifiableCommand or null
     */
    public PluginIdentifiableCommand getPluginCommand(String name) {
        Command command = this.commandMap.getCommand(name);
        if (command instanceof PluginIdentifiableCommand) {
            return (PluginIdentifiableCommand) command;
        } else {
            return null;
        }
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public String getPluginPath() {
        return pluginPath;
    }

    public int getPort() {
        return port;
    }

    /**
     * Get server's primary thread
     *
     * @return primary thread
     */
    public Thread getPrimaryThread() {
        return currentThread;
    }

    /**
     * Get server.properties
     *
     * @return server.properties as a Config
     */
    public Config getProperties() {
        return this.properties;
    }

    /**
     * Get a value from server.properties
     *
     * @param variable key
     * @return value
     */
    public Object getProperty(String variable) {
        return this.getProperty(variable, null);
    }

    /**
     * Get a value from server.properties
     *
     * @param variable     key
     * @param defaultValue default value
     * @return value
     */
    public Object getProperty(String variable, Object defaultValue) {
        Object value = this.properties.get(variable);
        return value == null ? defaultValue : value;
    }

    /**
     * Get a boolean value from server.properties
     *
     * @param variable key
     * @return value
     */
    public boolean getPropertyBoolean(String variable) {
        return this.getPropertyBoolean(variable, null);
    }

    /**
     * Get a boolean value from server.properties
     *
     * @param variable     key
     * @param defaultValue default value
     * @return value
     */
    public boolean getPropertyBoolean(String variable, Object defaultValue) {
        Object value = this.properties.get(variable);
        if (value == null) {
            value = defaultValue;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        switch (String.valueOf(value).trim().toLowerCase(Locale.ROOT)) {
            case "on":
            case "true":
            case "1":
            case "yes":
                return true;
        }
        return false;
    }

    /**
     * Get an int value from server.properties
     *
     * @param variable key
     * @return value
     */
    public int getPropertyInt(String variable) {
        return this.getPropertyInt(variable, null);
    }

    /**
     * Get an int value from server.properties
     *
     * @param variable     key
     * @param defaultValue default value
     * @return value
     */
    public int getPropertyInt(String variable, Integer defaultValue) {
        Object value = this.properties.get(variable);
        if (value == null) {
            value = defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        String trimmed = String.valueOf(value).trim();
        if (trimmed.isEmpty()) {
            return defaultValue;
        }
        return Integer.parseInt(trimmed);
    }

    /**
     * Get a string value from server.properties
     *
     * @param key key
     * @return value
     */
    public String getPropertyString(String key) {
        return this.getPropertyString(key, null);
    }

    /**
     * Get a string value from server.properties
     *
     * @param key          key
     * @param defaultValue default value
     * @return value
     */
    public String getPropertyString(String key, String defaultValue) {
        Object value = this.properties.get(key);
        return value == null ? defaultValue : value.toString();
    }

    public QueryRegenerateEvent getQueryInformation() {
        return this.queryRegenerateEvent;
    }

    public ResourcePackManager getResourcePackManager() {
        return resourcePackManager;
    }

    public ServerScheduler getScheduler() {
        return scheduler;
    }

    public UUID getServerUniqueId() {
        return this.serverID;
    }

    /**
     * Get service manager
     *
     * @return service manager
     */
    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public int getSpawnRadius() {
        return spawnRadius;
    }

    /**
     * Get the mob spawner task
     *
     * @return spawner task or null if both monster and animal spawning are disabled
     */
    public SpawnerTask getSpawnerTask() {
        return this.spawnerTask == null ? null : (SpawnerTask) this.spawnerTask.getTask();
    }

    /**
     * Get Sub-MOTD (level name)
     *
     * @return sub-motd
     */
    public String getSubMotd() {
        String sub = this.getPropertyString("sub-motd", "Powered by Nukkit PM1E");
        if (sub.isEmpty()) sub = "Powered by Nukkit PM1E";
        return sub;
    }

    /**
     * Get current tick
     *
     * @return current tick
     */
    public int getTick() {
        return tickCounter;
    }

    /**
     * Get main thread load
     *
     * @return tick usage %
     */
    public float getTickUsage() {
        return (float) NukkitMath.round(this.maxUse * 100, 2);
    }

    /**
     * Get average main thread load
     *
     * @return average main thread load
     */
    public float getTickUsageAverage() {
        float sum = 0;
        for (float aUseAverage : this.useAverage) {
            sum += aUseAverage;
        }
        return ((float) Math.round(sum / this.useAverage.length * 100)) / 100;
    }

    /**
     * Get ticks per second
     *
     * @return TPS
     */
    public float getTicksPerSecond() {
        return ((float) Math.round(this.maxTick * 100)) / 100;
    }

    /**
     * Get average ticks per second
     *
     * @return average TPS
     */
    public float getTicksPerSecondAverage() {
        float sum = 0;
        int count = this.tickAverage.length;
        for (float aTickAverage : this.tickAverage) {
            sum += aTickAverage;
        }
        return (float) NukkitMath.round(sum / count, 2);
    }

    public String getVersion() {
        return ProtocolInfo.MINECRAFT_VERSION;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    /**
     * Get whitelist config
     *
     * @return whitelist
     */
    public Config getWhitelist() {
        return whitelist;
    }

    /**
     * Internal: Handle query
     *
     * @param address sender address
     * @param payload payload
     */
    public void handlePacket(InetSocketAddress address, ByteBuf payload) {
        try {
            if (this.queryHandler == null || !payload.isReadable(3)) {
                return;
            }
            byte[] prefix = new byte[2];
            payload.readBytes(prefix);
            if (Arrays.equals(prefix, PREFIX)) {
                this.queryHandler.handle(address, payload);
            }
        } catch (Exception e) {
            log.error("Error whilst handling packet", e);
            this.network.blockAddress(address.getAddress(), 300);
        }
    }

    public boolean hasWhitelist() {
        return this.whitelistEnabled;
    }

    public boolean isHardcore() {
        return this.isHardcore;
    }

    /**
     * Is forcing language enabled
     *
     * @return force-language enabled
     */
    public boolean isLanguageForced() {
        return forceLanguage;
    }

    /**
     * Check whether a level by name is generated
     *
     * @param name level name
     * @return level found
     */
    public boolean isLevelGenerated(String name) {
        if (Objects.equals(name.trim(), "")) {
            return false;
        }

        if (this.getLevelByName(name) == null) {
            String path;

            if (name.contains("/") || name.contains("\\")) {
                path = name;
            } else {
                path = this.dataPath + "worlds/" + name + '/';
            }

            return LevelProviderManager.getProvider(path) != null;
        }

        return true;
    }

    /**
     * Check whether a level is loaded
     *
     * @param name level name
     * @return is loaded
     */
    public boolean isLevelLoaded(String name) {
        return this.getLevelByName(name) != null;
    }

    /**
     * Is nether enabled on this server
     *
     * @return nether enabled
     */
    public boolean isNetherAllowed() {
        return EnumLevel.NETHER.getLevel() != null;
    }

    /**
     * Check whether a player is an operator
     *
     * @param name player name
     * @return is operator
     */
    public boolean isOp(String name) {
        return name != null && this.operators.exists(name, true);
    }

    /**
     * Checks the current thread against the expected primary thread for the server.
     * <p>
     * <b>Note:</b> this method should not be used to indicate the current synchronized state of the runtime. A current thread matching the main thread indicates that it is synchronized, but a mismatch does not preclude the same assumption.
     *
     * @return true if the current thread matches the expected primary thread, false otherwise
     */
    public boolean isPrimaryThread() {
        return Thread.currentThread() == currentThread;
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    /**
     * Check whether a player is whitelisted
     *
     * @param name player name
     * @return is whitelisted or whitelist is not enabled
     */
    public boolean isWhitelisted(String name) {
        return !this.hasWhitelist() || this.operators.exists(name, true) || this.whitelist.exists(name, true);
    }

    /**
     * Load a level by name
     *
     * @param name level name
     * @return loaded
     */
    public boolean loadLevel(String name) {
        if (Objects.equals(name.trim(), "")) {
            throw new LevelException("Invalid empty level name");
        }

        if (!this.isPrimaryThread()) {
            getLogger().warning("Level loaded asynchronously: " + name);
        }

        if (this.isLevelLoaded(name)) {
            return true;
        } else if (!this.isLevelGenerated(name)) {
            log.warn(this.baseLang.translateString("nukkit.level.notFound", name));
            return false;
        }

        String path;

        if (name.contains("/") || name.contains("\\")) {
            path = name;

            if (this.suomiCraftPEMode) {
                String[] pathParts = path.split("/");
                name = pathParts[pathParts.length - 1];
            }
        } else {
            path = this.dataPath + "worlds/" + name + '/';
        }

        Class<? extends LevelProvider> provider = LevelProviderManager.getProvider(path);

        if (provider == null) {
            log.error(this.baseLang.translateString("nukkit.level.loadError", new String[]{name, "Unknown provider"}));
            return false;
        }

        Level level;
        try {
            level = new Level(this, name, path, provider);
        } catch (Exception e) {
            log.error(this.baseLang.translateString("nukkit.level.loadError", new String[]{name, e.getMessage()}));
            return false;
        }

        level.initLevel();

        this.levels.put(level.getId(), level);

        level.setTickRate(this.baseTickRate);

        this.pluginManager.callEvent(new LevelLoadEvent(level));
        return true;
    }

    /**
     * Load some settings from server.properties
     */
    private void loadSettings() {
        if (!this.getPropertyBoolean("ansi-title", true)) Nukkit.TITLE = false;
        this.maxPlayers = this.getPropertyInt("max-players", 50);
        this.forceLanguage = this.getPropertyBoolean("force-language", false);
        this.networkCompressionLevel = Math.max(Math.min(this.getPropertyInt("compression-level", 5), 9), 0);
        this.networkCompressionThreshold = Math.max(this.getPropertyInt("compression-threshold", 256), 0);
        this.autoTickRate = this.getPropertyBoolean("auto-tick-rate", true);
        this.autoTickRateLimit = this.getPropertyInt("auto-tick-rate-limit", 20);
        this.alwaysTickPlayers = this.getPropertyBoolean("always-tick-players", false);
        this.baseTickRate = Math.max(this.getPropertyInt("base-tick-rate", 1), 1);
        this.doLevelGC = this.getPropertyBoolean("do-level-gc", true);
        this.mobAiEnabled = this.getPropertyBoolean("mob-ai", true);
        this.xboxAuth = this.getPropertyBoolean("xbox-auth", true);
        this.bedSpawnpoints = this.getPropertyBoolean("bed-spawnpoints", true);
        this.achievementsEnabled = this.getPropertyBoolean("achievements", true);
        this.pvpEnabled = this.getPropertyBoolean("pvp", true);
        this.announceAchievements = this.getPropertyBoolean("announce-player-achievements", true);
        this.spawnEggsEnabled = this.getPropertyBoolean("spawn-eggs", true);
        this.shouldSavePlayerData = this.getPropertyBoolean("save-player-data", true);
        this.mobsFromBlocks = this.getPropertyBoolean("block-listener", true);
        this.explosionBreakBlocks = this.getPropertyBoolean("explosion-break-blocks", true);
        this.vanillaBossBar = this.getPropertyBoolean("vanilla-bossbars", true);
        this.queryPlugins = this.getPropertyBoolean("query-plugins", false);
        this.allowFlight = this.getPropertyBoolean("allow-flight", false);
        this.isHardcore = this.getPropertyBoolean("hardcore", false);
        this.forceResources = this.getPropertyBoolean("force-resources", false);
        this.forceResourcesAllowOwnPacks = this.getPropertyBoolean("force-resources-allow-client-packs", false);
        this.whitelistEnabled = this.getPropertyBoolean("white-list", false);
        this.forceGamemode = this.getPropertyBoolean("force-gamemode", true);
        this.doNotLimitInteractions = this.getPropertyBoolean("do-not-limit-interactions", false);
        this.motd = this.getPropertyString("motd", "A Minecraft Server");
        this.viewDistance = this.getPropertyInt("view-distance", 8);
        this.despawnMobs = this.getPropertyBoolean("entity-despawn-task", true);
        this.port = this.getPropertyInt("server-port", 19132);
        this.ip = this.getPropertyString("server-ip", "0.0.0.0");
        this.skinChangeCooldown = this.getPropertyInt("skin-change-cooldown", 15);
        this.strongIPBans = this.getPropertyBoolean("strong-ip-bans", false);
        this.spawnRadius = this.getPropertyInt("spawn-protection", 10);
        this.autoSaveTicks = this.getPropertyInt("ticks-per-autosave", 6000);
        this.doNotLimitSkinGeometry = this.getPropertyBoolean("do-not-limit-skin-geometry", true);
        this.chunksPerTick = this.getPropertyInt("chunk-sending-per-tick", 4);
        this.spawnThreshold = this.getPropertyInt("spawn-threshold", 56);
        this.savePlayerDataByUuid = this.getPropertyBoolean("save-player-data-by-uuid", true);
        this.vanillaPortals = this.getPropertyBoolean("vanilla-portals", true);
        this.personaSkins = this.getPropertyBoolean("persona-skins", true);
        this.cacheChunks = this.getPropertyBoolean("cache-chunks", false);
        this.updateChecks = this.getPropertyBoolean("update-notifications", true);
        this.minimumProtocol = this.getPropertyInt("multiversion-min-protocol", 0);
        if (this.minimumProtocol == -1) {
            this.minimumProtocol = ProtocolInfo.CURRENT_PROTOCOL;
        }
        this.useRawOres = this.getPropertyBoolean("use-raw-ores", true);
        this.whitelistReason = this.getPropertyString("whitelist-reason", "Server is white-listed").replace("§n", "\n");
        this.logJoinLocation = this.getPropertyBoolean("log-join-location", true);
        this.unsafeRedstone = this.getPropertyBoolean("unsafe-redstone", true);
        this.attackStopSprint = this.getPropertyBoolean("attack-stop-sprint", true);
        this.reduceTraffic = this.getPropertyBoolean("reduce-traffic", false);
        this.encryptionEnabled = this.getPropertyBoolean("encryption", true);
        this.ignoreUnbreakableItems = this.getPropertyBoolean("ignore-unbreakable-items", false);
        this.chunkUnloadDelay = this.getPropertyInt("chunk-unload-delay", 30) * 1000;
        this.spawnThresholdRadius = (int) Math.ceil(Math.sqrt(this.spawnThreshold));
        this.entityActivationRange = Math.pow(this.getPropertyInt("entity-activation-blocks", 80), 2);
        this.setAutoSave(this.getPropertyBoolean("auto-save", true));
        try {
            this.gamemode = this.getPropertyInt("gamemode", 0) & 0b11;
        } catch (NumberFormatException exception) {
            this.gamemode = getGamemodeFromString(this.getPropertyString("gamemode", "0")) & 0b11;
        }
        String list = this.getPropertyString("do-not-tick-worlds", "");
        if (!list.trim().isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(list, ", ");
            while (tokenizer.hasMoreTokens()) {
                noTickingWorlds.add(tokenizer.nextToken());
            }
        }
        if (this.isHardcore && this.difficulty < 3) {
            this.setDifficulty(3);
        } else {
            this.setDifficulty(getDifficultyFromString(this.getPropertyString("difficulty", "2")));
        }
        antiXrayWorlds.clear();
        String antiXray = this.getPropertyString("anti-xray-worlds", "");
        if (!antiXray.trim().isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(antiXray, ", ");
            while (tokenizer.hasMoreTokens()) {
                antiXrayWorlds.add(tokenizer.nextToken());
            }
        }
        Level.xrayableBlocks[BlockID.NETHER_GOLD_ORE] = this.suomiCraftPEMode;
        disabledSpawnWorlds.clear();
        String noSpawning = this.getPropertyString("worlds-entity-spawning-disabled", "");
        if (!noSpawning.trim().isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(noSpawning, ", ");
            while (tokenizer.hasMoreTokens()) {
                disabledSpawnWorlds.add(tokenizer.nextToken());
            }
        }
        nonAutoSaveWorlds.clear();
        String noAutoSave = this.getPropertyString("worlds-level-auto-save-disabled", "");
        if (!noAutoSave.trim().isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(noAutoSave, ", ");
            while (tokenizer.hasMoreTokens()) {
                nonAutoSaveWorlds.add(tokenizer.nextToken());
            }
        }

        checkMobSpawnerTask();
    }

    /**
     * Get known player uuid by player name
     *
     * @param name player name
     * @return Optional UUID
     */
    public Optional<UUID> lookupName(String name) {
        if (nameLookup == null) {
            return Optional.empty();
        }

        byte[] nameBytes = name.toLowerCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8);
        byte[] uuidBytes = nameLookup.get(nameBytes);
        if (uuidBytes == null) {
            return Optional.empty();
        }

        if (uuidBytes.length != 16) {
            log.warn("Invalid uuid in name lookup database, removing: " + name);
            nameLookup.delete(nameBytes);
            return Optional.empty();
        }

        ByteBuffer buffer = ByteBuffer.wrap(uuidBytes);
        return Optional.of(new UUID(buffer.getLong(), buffer.getLong()));
    }

    /**
     * Get players that match with the name
     *
     * @param partialName name
     * @return matching players
     */
    public Player[] matchPlayer(String partialName) {
        partialName = partialName.toLowerCase(Locale.ROOT);
        List<Player> matchedPlayer = new ArrayList<>();
        for (Player player : this.getOnlinePlayersList()) {
            if (player.getName().toLowerCase(Locale.ROOT).equals(partialName)) {
                return new Player[]{player};
            } else if (player.getName().toLowerCase(Locale.ROOT).contains(partialName)) {
                matchedPlayer.add(player);
            }
        }

        return matchedPlayer.toArray(new Player[0]);
    }

    /**
     * Internal: Warn user about non multiversion compatible plugins.
     */
    public static void mvw(String action) {
        if (getInstance().minimumProtocol != ProtocolInfo.CURRENT_PROTOCOL) {
            getInstance().getLogger().logException(new PluginException("Default " + action + " used by a plugin. This can cause instability with the multiversion."));
        }
    }

    /**
     * Internal method to register all default block entities
     */
    private static void registerBlockEntities() {
        BlockEntity.registerBlockEntity(BlockEntity.FURNACE, BlockEntityFurnace.class);
        BlockEntity.registerBlockEntity(BlockEntity.CHEST, BlockEntityChest.class);
        BlockEntity.registerBlockEntity(BlockEntity.SIGN, BlockEntitySign.class);
        BlockEntity.registerBlockEntity(BlockEntity.ENCHANT_TABLE, BlockEntityEnchantTable.class);
        BlockEntity.registerBlockEntity(BlockEntity.SKULL, BlockEntitySkull.class);
        BlockEntity.registerBlockEntity(BlockEntity.FLOWER_POT, BlockEntityFlowerPot.class);
        BlockEntity.registerBlockEntity(BlockEntity.BREWING_STAND, BlockEntityBrewingStand.class);
        BlockEntity.registerBlockEntity(BlockEntity.ITEM_FRAME, BlockEntityItemFrame.class);
        BlockEntity.registerBlockEntity(BlockEntity.GLOW_ITEM_FRAME, BlockEntityItemFrameGlow.class);
        BlockEntity.registerBlockEntity(BlockEntity.CAULDRON, BlockEntityCauldron.class);
        BlockEntity.registerBlockEntity(BlockEntity.ENDER_CHEST, BlockEntityEnderChest.class);
        BlockEntity.registerBlockEntity(BlockEntity.BEACON, BlockEntityBeacon.class);
        BlockEntity.registerBlockEntity(BlockEntity.PISTON_ARM, BlockEntityPistonArm.class);
        BlockEntity.registerBlockEntity(BlockEntity.COMPARATOR, BlockEntityComparator.class);
        BlockEntity.registerBlockEntity(BlockEntity.HOPPER, BlockEntityHopper.class);
        BlockEntity.registerBlockEntity(BlockEntity.BED, BlockEntityBed.class);
        BlockEntity.registerBlockEntity(BlockEntity.JUKEBOX, BlockEntityJukebox.class);
        BlockEntity.registerBlockEntity(BlockEntity.SHULKER_BOX, BlockEntityShulkerBox.class);
        BlockEntity.registerBlockEntity(BlockEntity.BANNER, BlockEntityBanner.class);
        BlockEntity.registerBlockEntity(BlockEntity.DROPPER, BlockEntityDropper.class);
        BlockEntity.registerBlockEntity(BlockEntity.DISPENSER, BlockEntityDispenser.class);
        BlockEntity.registerBlockEntity(BlockEntity.MOB_SPAWNER, BlockEntitySpawner.class);
        BlockEntity.registerBlockEntity(BlockEntity.MUSIC, BlockEntityMusic.class);
        BlockEntity.registerBlockEntity(BlockEntity.CAMPFIRE, BlockEntityCampfire.class);
        BlockEntity.registerBlockEntity(BlockEntity.BARREL, BlockEntityBarrel.class);
        BlockEntity.registerBlockEntity(BlockEntity.LECTERN, BlockEntityLectern.class);
        BlockEntity.registerBlockEntity(BlockEntity.BLAST_FURNACE, BlockEntityBlastFurnace.class);
        BlockEntity.registerBlockEntity(BlockEntity.SMOKER, BlockEntitySmoker.class);
        BlockEntity.registerBlockEntity(BlockEntity.BELL, BlockEntityBell.class);
        BlockEntity.registerBlockEntity(BlockEntity.CONDUIT, BlockEntityConduit.class);

        // Not fully implemented yet but keep NBT for future use
        BlockEntity.registerBlockEntity(BlockEntity.MOVING_BLOCK, BlockEntityMovingBlock.class);
        BlockEntity.registerBlockEntity(BlockEntity.DAYLIGHT_DETECTOR, BlockEntityDaylightDetector.class);
        BlockEntity.registerBlockEntity(BlockEntity.SCULK_CATALYST, BlockEntitySculkCatalyst.class);
        BlockEntity.registerBlockEntity(BlockEntity.SCULK_SHRIEKER, BlockEntitySculkShrieker.class);
        BlockEntity.registerBlockEntity(BlockEntity.SCULK_SENSOR, BlockEntitySculkSensor.class);
        BlockEntity.registerBlockEntity(BlockEntity.CALIBRATED_SCULK_SENSOR, BlockEntityCalibratedSculkSensor.class);
        BlockEntity.registerBlockEntity(BlockEntity.CHISELED_BOOKSHELF, BlockEntityChiseledBookshelf.class);
        BlockEntity.registerBlockEntity(BlockEntity.TRIAL_SPAWNER, BlockEntityTrialSpawner.class);
        BlockEntity.registerBlockEntity(BlockEntity.BEEHIVE, BlockEntityBeehive.class);
        BlockEntity.registerBlockEntity(BlockEntity.HANGING_SIGN, BlockEntityHangingSign.class);
        BlockEntity.registerBlockEntity(BlockEntity.DECORATED_POT, BlockEntityDecoratedPot.class);
        BlockEntity.registerBlockEntity(BlockEntity.CRAFTER, BlockEntityCrafter.class);
        BlockEntity.registerBlockEntity(BlockEntity.CREAKING_HEART, BlockEntityCreakingHeart.class);
        BlockEntity.registerBlockEntity(BlockEntity.VAULT, BlockEntityVault.class);
        BlockEntity.registerBlockEntity(BlockEntity.SPORE_BLOSSOM, BlockEntitySporeBlossom.class);
        BlockEntity.registerBlockEntity(BlockEntity.STRUCTURE_BLOCK, BlockEntityStructureBlock.class);
        BlockEntity.registerBlockEntity(BlockEntity.END_PORTAL, BlockEntityEndPortal.class);
        BlockEntity.registerBlockEntity(BlockEntity.LODESTONE, BlockEntityLodestone.class);
        BlockEntity.registerBlockEntity(BlockEntity.BRUSHABLE_BLOCK, BlockEntityBrushableBlock.class);
        BlockEntity.registerBlockEntity(BlockEntity.COMMAND_BLOCK, BlockEntityCommandBlock.class);
        BlockEntity.registerBlockEntity(BlockEntity.JIGSAW, BlockEntityJigsaw.class);
        BlockEntity.registerBlockEntity(BlockEntity.JIGSAW_BLOCK, BlockEntityJigsawBlock.class);
        BlockEntity.registerBlockEntity(BlockEntity.END_GATEWAY, BlockEntityEndGateway.class);

        // Persistent container, not on vanilla
        BlockEntity.registerBlockEntity(BlockEntity.PERSISTENT_CONTAINER, PersistentDataContainerBlockEntity.class);
    }

    /**
     * Internal method to register all default entities
     */
    private static void registerEntities() {
        //Items
        Entity.registerEntity("Item", EntityItem.class);
        Entity.registerEntity("Painting", EntityPainting.class);
        Entity.registerEntity("XpOrb", EntityXPOrb.class);
        Entity.registerEntity("ArmorStand", EntityArmorStand.class);
        Entity.registerEntity("EndCrystal", EntityEndCrystal.class);
        Entity.registerEntity("FallingSand", EntityFallingBlock.class);
        Entity.registerEntity("PrimedTnt", EntityPrimedTNT.class);
        Entity.registerEntity("Firework", EntityFirework.class);
        //Projectiles
        Entity.registerEntity("Arrow", EntityArrow.class);
        Entity.registerEntity("Snowball", EntitySnowball.class);
        Entity.registerEntity("EnderPearl", EntityEnderPearl.class);
        Entity.registerEntity("ThrownExpBottle", EntityExpBottle.class);
        Entity.registerEntity("ThrownPotion", EntityPotion.class);
        Entity.registerEntity("Egg", EntityEgg.class);
        Entity.registerEntity("BlazeFireBall", EntityBlazeFireBall.class);
        Entity.registerEntity("GhastFireBall", EntityGhastFireBall.class);
        Entity.registerEntity("ShulkerBullet", EntityShulkerBullet.class);
        Entity.registerEntity("ThrownLingeringPotion", EntityPotionLingering.class);
        Entity.registerEntity("ThrownTrident", EntityThrownTrident.class);
        Entity.registerEntity("WitherSkull", EntityWitherSkull.class);
        Entity.registerEntity("BlueWitherSkull", EntityBlueWitherSkull.class);
        Entity.registerEntity("LlamaSpit", EntityLlamaSpit.class);
        Entity.registerEntity("EvocationFangs", EntityEvocationFangs.class);
        Entity.registerEntity("EnderCharge", EntityEnderCharge.class);
        Entity.registerEntity("FishingHook", EntityFishingHook.class);
        Entity.registerEntity("EnderEye", EntityEnderEye.class);
        Entity.registerEntity("AreaEffectCloud", EntityAreaEffectCloud.class);
        //Monsters
        Entity.registerEntity("Blaze", EntityBlaze.class);
        Entity.registerEntity("Creeper", EntityCreeper.class);
        Entity.registerEntity("CaveSpider", EntityCaveSpider.class);
        Entity.registerEntity("Drowned", EntityDrowned.class);
        Entity.registerEntity("ElderGuardian", EntityElderGuardian.class);
        Entity.registerEntity("EnderDragon", EntityEnderDragon.class);
        Entity.registerEntity("Enderman", EntityEnderman.class);
        Entity.registerEntity("Endermite", EntityEndermite.class);
        Entity.registerEntity("Evoker", EntityEvoker.class);
        Entity.registerEntity("Ghast", EntityGhast.class);
        Entity.registerEntity("Guardian", EntityGuardian.class);
        Entity.registerEntity("Husk", EntityHusk.class);
        Entity.registerEntity("MagmaCube", EntityMagmaCube.class);
        Entity.registerEntity("Phantom", EntityPhantom.class);
        Entity.registerEntity("Ravager", EntityRavager.class);
        Entity.registerEntity("Shulker", EntityShulker.class);
        Entity.registerEntity("Silverfish", EntitySilverfish.class);
        Entity.registerEntity("Skeleton", EntitySkeleton.class);
        Entity.registerEntity("SkeletonHorse", EntitySkeletonHorse.class);
        Entity.registerEntity("Slime", EntitySlime.class);
        Entity.registerEntity("Spider", EntitySpider.class);
        Entity.registerEntity("Stray", EntityStray.class);
        Entity.registerEntity("Vindicator", EntityVindicator.class);
        Entity.registerEntity("Vex", EntityVex.class);
        Entity.registerEntity("WitherSkeleton", EntityWitherSkeleton.class);
        Entity.registerEntity("Wither", EntityWither.class);
        Entity.registerEntity("Witch", EntityWitch.class);
        Entity.registerEntity("ZombiePigman", EntityZombiePigman.class);
        Entity.registerEntity("ZombieVillager", EntityZombieVillagerV1.class);
        Entity.registerEntity("Zombie", EntityZombie.class);
        Entity.registerEntity("Pillager", EntityPillager.class);
        Entity.registerEntity("ZombieVillagerV2", EntityZombieVillager.class);
        Entity.registerEntity("Hoglin", EntityHoglin.class);
        Entity.registerEntity("Piglin", EntityPiglin.class);
        Entity.registerEntity("Zoglin", EntityZoglin.class);
        Entity.registerEntity("PiglinBrute", EntityPiglinBrute.class);
        Entity.registerEntity("Warden", EntityWarden.class);
        Entity.registerEntity("Breeze", EntityBreeze.class);
        Entity.registerEntity("Bogged", EntityBogged.class);
        Entity.registerEntity("Creaking", EntityCreaking.class);
        //Passive
        Entity.registerEntity("Bat", EntityBat.class);
        Entity.registerEntity("Cat", EntityCat.class);
        Entity.registerEntity("Chicken", EntityChicken.class);
        Entity.registerEntity("Cod", EntityCod.class);
        Entity.registerEntity("Cow", EntityCow.class);
        Entity.registerEntity("Dolphin", EntityDolphin.class);
        Entity.registerEntity("Donkey", EntityDonkey.class);
        Entity.registerEntity("Horse", EntityHorse.class);
        Entity.registerEntity("IronGolem", EntityIronGolem.class);
        Entity.registerEntity("Llama", EntityLlama.class);
        Entity.registerEntity("Mooshroom", EntityMooshroom.class);
        Entity.registerEntity("Mule", EntityMule.class);
        Entity.registerEntity("Panda", EntityPanda.class);
        Entity.registerEntity("Parrot", EntityParrot.class);
        Entity.registerEntity("PolarBear", EntityPolarBear.class);
        Entity.registerEntity("Pig", EntityPig.class);
        Entity.registerEntity("Pufferfish", EntityPufferfish.class);
        Entity.registerEntity("Rabbit", EntityRabbit.class);
        Entity.registerEntity("Salmon", EntitySalmon.class);
        Entity.registerEntity("Sheep", EntitySheep.class);
        Entity.registerEntity("Squid", EntitySquid.class);
        Entity.registerEntity("SnowGolem", EntitySnowGolem.class);
        Entity.registerEntity("TropicalFish", EntityTropicalFish.class);
        Entity.registerEntity("Turtle", EntityTurtle.class);
        Entity.registerEntity("Wolf", EntityWolf.class);
        Entity.registerEntity("Ocelot", EntityOcelot.class);
        Entity.registerEntity("Villager", EntityVillagerV1.class);
        Entity.registerEntity("ZombieHorse", EntityZombieHorse.class);
        Entity.registerEntity("WanderingTrader", EntityWanderingTrader.class);
        Entity.registerEntity("VillagerV2", EntityVillager.class);
        Entity.registerEntity("Fox", EntityFox.class);
        Entity.registerEntity("Bee", EntityBee.class);
        Entity.registerEntity("Strider", EntityStrider.class);
        Entity.registerEntity("Goat", EntityGoat.class);
        Entity.registerEntity("Axolotl", EntityAxolotl.class);
        Entity.registerEntity("GlowSquid", EntityGlowSquid.class);
        Entity.registerEntity("Allay", EntityAllay.class);
        Entity.registerEntity("Frog", EntityFrog.class);
        Entity.registerEntity("Tadpole", EntityTadpole.class);
        Entity.registerEntity("Camel", EntityCamel.class);
        Entity.registerEntity("Sniffer", EntitySniffer.class);
        Entity.registerEntity("Armadillo", EntityArmadillo.class);
        Entity.registerEntity("HappyGhast", EntityHappyGhast.class);
        Entity.registerEntity("CopperGolem", EntityCopperGolem.class);
        //Vehicles
        Entity.registerEntity("MinecartRideable", EntityMinecartEmpty.class);
        Entity.registerEntity("MinecartChest", EntityMinecartChest.class);
        Entity.registerEntity("MinecartHopper", EntityMinecartHopper.class);
        Entity.registerEntity("MinecartTnt", EntityMinecartTNT.class);
        Entity.registerEntity("Boat", EntityBoat.class);
        Entity.registerEntity("ChestBoat", EntityChestBoat.class);
        //Others
        Entity.registerEntity("Human", EntityHuman.class, true);
        Entity.registerEntity("Lightning", EntityLightning.class);
    }

    /**
     * Reload the server. Notice: may cause issues with some plugins.
     */
    public void reload() {
        log.info("Saving levels...");
        for (Level level : this.levelArray) {
            level.save();
        }

        this.pluginManager.clearPlugins();
        this.commandMap.clearCommands();

        log.info("Reloading server properties...");
        this.properties.reload();
        this.loadSettings();

        this.banByIP.load();
        this.banByName.load();
        this.reloadWhitelist();
        this.operators.reload();

        for (BanEntry entry : this.banByIP.getEntires().values()) {
            try {
                this.network.blockAddress(InetAddress.getByName(entry.getName()));
            } catch (UnknownHostException ignore) {
            }
        }

        log.info("Reloading plugins...");
        this.pluginManager.registerInterface(JavaPluginLoader.class);
        this.pluginManager.loadPlugins(this.pluginPath);
        this.enablePlugins(PluginLoadOrder.STARTUP);
        this.enablePlugins(PluginLoadOrder.POSTWORLD);
    }

    /**
     * Reload whitelist
     */
    public void reloadWhitelist() {
        this.whitelist.reload();
    }

    public void removeOnlinePlayer(Player player) {
        if (player.getUniqueId() == null) {
            return;
        }
        if (this.playerList.remove(player.getUniqueId()) != null) {
            PlayerListPacket pk = new PlayerListPacket();
            pk.type = PlayerListPacket.TYPE_REMOVE;
            pk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(player.getUniqueId())};

            Server.broadcastPacket(this.playerList.values(), pk);
        }
    }

    /**
     * Remove player's operator status
     *
     * @param name player name
     */
    public void removeOp(String name) {
        this.operators.remove(name.toLowerCase(Locale.ROOT));
        Player player = this.getPlayerExact(name);
        if (player != null) {
            player.recalculatePermissions();
        }
        this.operators.save();
    }

    /**
     * Internal: Remove a player from the server
     *
     * @param player player
     */
    public void removePlayer(Player player) {
        if (this.players.remove(player.getSocketAddress()) != null) {
            return;
        }

        for (InetSocketAddress socketAddress : new ArrayList<>(this.players.keySet())) {
            if (player == this.players.get(socketAddress)) {
                this.players.remove(socketAddress);
                break;
            }
        }
    }

    public void removePlayerListData(UUID uuid) {
        this.removePlayerListData(uuid, this.playerList.values());
    }

    public void removePlayerListData(UUID uuid, Player[] players) {
        PlayerListPacket pk = new PlayerListPacket();
        pk.type = PlayerListPacket.TYPE_REMOVE;
        pk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid)};
        for (Player player : players) {
            player.dataPacket(pk);
        }
    }

    public void removePlayerListData(UUID uuid, Collection<Player> players) {
        this.removePlayerListData(uuid, players.toArray(new Player[0]));
    }

    public void removePlayerListData(UUID uuid, Player player) {
        PlayerListPacket pk = new PlayerListPacket();
        pk.type = PlayerListPacket.TYPE_REMOVE;
        pk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid)};
        player.dataPacket(pk);
    }

    /**
     * Remove a player from whitelist
     *
     * @param name player name
     */
    public void removeWhitelist(String name) {
        this.whitelist.remove(name.toLowerCase(Locale.ROOT));
        this.whitelist.save(true);
    }

    public void saveOfflinePlayerData(UUID uuid, CompoundTag tag) {
        this.saveOfflinePlayerData(uuid, tag, false);
    }

    public void saveOfflinePlayerData(String name, CompoundTag tag) {
        this.saveOfflinePlayerData(name, tag, false);
    }

    public void saveOfflinePlayerData(UUID uuid, CompoundTag tag, boolean async) {
        this.saveOfflinePlayerData(uuid.toString(), tag, async);
    }

    public void saveOfflinePlayerData(String name, CompoundTag tag, boolean async) {
        if (this.savePlayerDataByUuid) {
            Optional<UUID> uuid = lookupName(name);
            saveOfflinePlayerData(uuid.map(UUID::toString).orElse(name), tag, async, true);
        } else {
            saveOfflinePlayerData(name, tag, async, true);
        }
    }

    private void saveOfflinePlayerData(String name, CompoundTag tag, boolean async, boolean runEvent) {
        if (this.shouldSavePlayerData()) {
            String nameLower = name.toLowerCase(Locale.ROOT);
            PlayerDataSerializeEvent event = new PlayerDataSerializeEvent(nameLower, playerDataSerializer);
            if (runEvent) {
                pluginManager.callEvent(event);
            }

            if (async) {
                this.getScheduler().scheduleTask(new Task() {
                    private volatile boolean hasRun = false;

                    // Doing it like this ensures that the player data will be saved in a server shutdown
                    @Override
                    public void onCancel() {
                        if (!this.hasRun) {
                            this.hasRun = true;
                            saveOfflinePlayerDataInternal(event.getSerializer(), tag, nameLower, event.getUuid().orElse(null));
                        }
                    }

                    @Override
                    public void onRun(int currentTick) {
                        this.onCancel();
                    }
                }, true);
            } else {
                saveOfflinePlayerDataInternal(event.getSerializer(), tag, nameLower, event.getUuid().orElse(null));
            }
        }
    }

    /**
     * Internal: Save offline player data
     *
     * @param serializer serializer
     * @param tag        compound tag
     * @param name       player name
     * @param uuid       player uuid
     */
    private void saveOfflinePlayerDataInternal(PlayerDataSerializer serializer, CompoundTag tag, String name, UUID uuid) {
        try (OutputStream dataStream = serializer.write(name, uuid)) {
            NBTIO.writeGZIPCompressed(tag, dataStream, ByteOrder.BIG_ENDIAN);
        } catch (Exception e) {
            log.error(this.getLanguage().translateString("nukkit.data.saveError", name, e), e);
        }
    }

    public void sendFullPlayerListData(Player player) {
        PlayerListPacket pk = new PlayerListPacket();
        pk.type = PlayerListPacket.TYPE_ADD;
        pk.entries = this.playerList.values().stream()
                .map(p -> new PlayerListPacket.Entry(
                        p.getUniqueId(),
                        p.getId(),
                        p.getDisplayName(),
                        p.getSkin(),
                        p.getLoginChainData().getXUID(),
                        p.getLocatorBarColor()))
                .toArray(PlayerListPacket.Entry[]::new);
        player.dataPacket(pk);
    }

    public void sendRecipeList(Player player) {
        DataPacket pk = craftingManager.getCachedPacket(player.protocol);
        if (pk != null) {
            player.dataPacket(pk);
        } else {
            log.debug("No recipe list available for protocol " + player.protocol);
        }
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
        for (Level level : this.levelArray) {
            level.setAutoSave(this.autoSave);
        }
    }

    /**
     * Change the default level
     *
     * @param defaultLevel new default level
     */
    public void setDefaultLevel(Level defaultLevel) {
        if (defaultLevel == null || (this.isLevelLoaded(defaultLevel.getFolderName()) && defaultLevel != this.defaultLevel)) {
            this.defaultLevel = defaultLevel;
        }
    }

    public void setDifficulty(int difficulty) {
        int value = difficulty;
        if (value < 0) value = 0;
        if (value > 3) value = 3;
        this.difficulty = value;
        this.setPropertyInt("difficulty", value);
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * Set player data serializer that is used to save player data
     *
     * @param playerDataSerializer player data serializer
     */
    public void setPlayerDataSerializer(PlayerDataSerializer playerDataSerializer) {
        this.playerDataSerializer = Preconditions.checkNotNull(playerDataSerializer, "playerDataSerializer");
    }

    /**
     * Set a boolean value in server.properties
     *
     * @param variable key
     * @param value    value
     */
    public void setPropertyBoolean(String variable, boolean value) {
        this.properties.set(variable, value);
        this.properties.save();
    }

    /**
     * Set an int value in server.properties
     *
     * @param variable key
     * @param value    value
     */
    public void setPropertyInt(String variable, int value) {
        this.properties.set(variable, value);
        this.properties.save();
    }

    /**
     * Set a string value in server.properties
     *
     * @param variable key
     * @param value    value
     */
    public void setPropertyString(String variable, String value) {
        this.properties.set(variable, value);
        this.properties.save();
    }

    /**
     * Should player data saving be enabled
     *
     * @return player data saving enabled
     */
    public boolean shouldSavePlayerData() {
        return shouldSavePlayerData;
    }

    /**
     * Mark the server to be shut down.
     */
    public void shutdown() {
        isRunning.compareAndSet(true, false);
    }

    /**
     * Sort players by protocol version
     *
     * @param players players
     * @return players sorted by protocol
     */
    public static Int2ObjectMap<ObjectList<Player>> sortPlayers(Player[] players) {
        Int2ObjectMap<ObjectList<Player>> targets = new Int2ObjectOpenHashMap<>();
        for (Player player : players) {
            targets.computeIfAbsent(player.protocol, i -> new ObjectArrayList<>()).add(player);
        }
        return targets;
    }

    /**
     * Sort players by protocol version
     *
     * @param players players
     * @return players sorted by protocol
     */
    public static Int2ObjectMap<ObjectList<Player>> sortPlayers(Collection<Player> players) {
        Int2ObjectMap<ObjectList<Player>> targets = new Int2ObjectOpenHashMap<>();
        for (Player player : players) {
            targets.computeIfAbsent(player.protocol, i -> new ObjectArrayList<>()).add(player);
        }
        return targets;
    }

    /**
     * Internal: Start the server
     */
    public void start() {
        if (this.getPropertyBoolean("enable-query", false)) {
            this.queryHandler = new QueryHandler();
        }

        for (BanEntry entry : this.banByIP.getEntires().values()) {
            try {
                this.network.blockAddress(InetAddress.getByName(entry.getName()));
            } catch (UnknownHostException ignore) {
            }
        }

        this.tickCounter = 0;

        log.info(this.baseLang.translateString("nukkit.server.startFinished", String.valueOf((double) (System.currentTimeMillis() - Nukkit.START_TIME) / 1000)));

        this.tickProcessor();
        this.forceShutdown();
    }

    /**
     * SuomiCraft PE mode tweaks some stuff to work better on SuomiCraft PE server.
     *
     * @return SuomiCraft PE mode enabled
     */
    public boolean suomiCraftPEMode() {
        return this.suomiCraftPEMode;
    }

    private void tick() {
        long tickTime = System.currentTimeMillis();

        long time = tickTime - this.nextTick;
        if (time < -25) {
            try {
                Thread.sleep(Math.max(5, -time - 25));
            } catch (InterruptedException e) {
                this.getLogger().logException(e);
            }
        }

        long tickTimeNano = System.nanoTime();
        if ((tickTime - this.nextTick) < -25) {
            return;
        }

        ++this.tickCounter;

        this.network.processInterfaces();

        if (this.rcon != null) {
            this.rcon.check();
        }

        this.scheduler.mainThreadHeartbeat(this.tickCounter);

        this.checkTickUpdates(this.tickCounter);

        for (Player player : new ArrayList<>(this.players.values())) {
            player.checkNetwork();
        }

        if ((this.tickCounter & 0b1111) == 0) {
            this.titleTick();

            //this.network.resetStatistics(); // Unnecessary since addStatistics is not used in the new raknet
            this.maxTick = 20;
            this.maxUse = 0;

            if ((this.tickCounter & 0b111111111) == 0) {
                try {
                    this.pluginManager.callEvent(this.queryRegenerateEvent = new QueryRegenerateEvent(this, 5));
                    if (this.queryHandler != null) {
                        this.queryHandler.regenerateInfo();
                    }
                } catch (Exception e) {
                    log.error(e);
                }
            }

            this.network.updateName();
        }

        if (++this.autoSaveTicker >= this.autoSaveTicks) {
            this.autoSaveTicker = 0;
            this.doAutoSave();
        }

        if (this.tickCounter % 100 == 0) {
            for (Level level : this.levelArray) {
                if (!level.isBeingConverted) {
                    level.doChunkGarbageCollection();
                }
            }
        }

        long nowNano = System.nanoTime();

        float tick = (float) Math.min(20, 1000000000 / Math.max(1000000, ((double) nowNano - tickTimeNano)));
        float use = (float) Math.min(1, ((double) (nowNano - tickTimeNano)) / 50000000);

        if (this.maxTick > tick) {
            this.maxTick = tick;
        }

        if (this.maxUse < use) {
            this.maxUse = use;
        }

        System.arraycopy(this.tickAverage, 1, this.tickAverage, 0, this.tickAverage.length - 1);
        this.tickAverage[this.tickAverage.length - 1] = tick;

        System.arraycopy(this.useAverage, 1, this.useAverage, 0, this.useAverage.length - 1);
        this.useAverage[this.useAverage.length - 1] = use;

        if ((this.nextTick - tickTime) < -1000) {
            this.nextTick = tickTime;
        } else {
            this.nextTick += 50;
        }
    }

    /**
     * Internal: Tick the server
     */
    public void tickProcessor() {
        this.nextTick = System.currentTimeMillis();
        try {
            while (this.isRunning.get()) {
                try {
                    this.tick();

                    long next = this.nextTick;
                    long current = System.currentTimeMillis();

                    if (next - 0.1 > current) {
                        long allocated = next - current - 1;

                        // Instead of wasting time, do something potentially useful
                        if (this.doLevelGC) {
                            int offset = 0;
                            for (int i = 0; i < levelArray.length; i++) {
                                offset = (i + lastLevelGC) % levelArray.length;
                                Level level = levelArray[offset];
                                if (!level.isBeingConverted) {
                                    level.doGarbageCollection(allocated - 1);
                                }
                                allocated = next - System.currentTimeMillis();
                                if (allocated <= 0) break;
                            }
                            lastLevelGC = offset + 1;
                        }

                        if (allocated > 0 || !this.doLevelGC) {
                            try {
                                Thread.sleep(allocated, 900000);
                            } catch (Exception e) {
                                this.getLogger().logException(e);
                            }
                        }
                    }
                } catch (RuntimeException e) {
                    this.getLogger().logException(e);
                }
            }
        } catch (Throwable e) {
            log.fatal("Exception happened while ticking server", e);
            log.fatal(Utils.getAllThreadDumps());
        }
    }

    private void titleTick() {
        if (!Nukkit.TITLE) {
            return;
        }
        Runtime runtime = Runtime.getRuntime();
        double used = NukkitMath.round((double) (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024, 2);
        double max = NukkitMath.round(((double) runtime.maxMemory()) / 1024 / 1024, 2);
        System.out.print((char) 0x1b + "]0;Nukkit PM1E " + Nukkit.BUILD_VERSION +
                " | Online " + this.playerList.size() + '/' + this.maxPlayers +
                " | Memory " + Math.round(used / max * 100) + '%' +
                /*" | U " + NukkitMath.round((this.network.getUpload() / 1024 * 1000), 2) +
                " D " + NukkitMath.round((this.network.getDownload() / 1024 * 1000), 2) + " kB/s" +*/
                " | TPS " + this.getTicksPerSecond() +
                " | Load " + this.getTickUsage() + '%' + (char) 0x07);
    }

    /**
     * Unload a level.
     * Notice that the default level cannot be unloaded without forceUnload=true
     *
     * @param level Level
     * @return unloaded
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean unloadLevel(Level level) {
        return this.unloadLevel(level, false);
    }

    /**
     * Unload a level
     * <p>
     * Notice: the default level cannot be unloaded without forceUnload=true
     *
     * @param level       Level
     * @param forceUnload force unload (ignore cancelled events and default level)
     * @return unloaded
     */
    public boolean unloadLevel(Level level, boolean forceUnload) {
        if (level == this.defaultLevel && !forceUnload) {
            throw new IllegalStateException("The default level cannot be unloaded while running, please switch levels.");
        }

        return level.unload(forceUnload);
    }

    void updateName(UUID uuid, String name) {
        if (nameLookup == null) {
            return;
        }

        byte[] nameBytes = name.toLowerCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());

        nameLookup.put(nameBytes, buffer.array());
    }

    /**
     * Check for updates and notify target
     *
     * @param sender         target
     * @param notifyNoUpdate whether the already on the latest version message should be shown
     */
    public void updateNotification(CommandSender sender, boolean notifyNoUpdate) {
        CompletableFuture.runAsync(() -> {
            try {
                if (!Nukkit.getBranch().equals(Nukkit.MAIN_BRANCH)) {
                    if (sender instanceof ConsoleCommandSender) {
                        this.getLogger().warning("§eDevelopment build! Branch: " + Nukkit.getBranch());
                    }
                    return;
                }

                URLConnection request = new URL(Nukkit.RELEASES).openConnection();
                request.connect();
                InputStreamReader content = new InputStreamReader((InputStream) request.getContent(), StandardCharsets.UTF_8);
                JsonElement element = JsonParser.parseReader(content);
                content.close();

                String latestReleaseTag = null;
                String latestPreReleaseTag = null;
                String latestReleaseName = null;
                String latestPreReleaseName = null;

                // Find the latest release & pre-release
                for (JsonElement releaseElement : element.getAsJsonArray().getAsJsonArray()) {
                    if (!(releaseElement instanceof JsonObject)) {
                        continue;
                    }

                    if (latestReleaseTag != null && latestPreReleaseTag != null) {
                        break; // Both latest versions found already
                    }

                    JsonObject release = (JsonObject) releaseElement;
                    boolean preRelease = release.has("prerelease") && release.get("prerelease").getAsBoolean();

                    if (preRelease) {
                        if (latestPreReleaseTag == null) {
                            latestPreReleaseTag = release.get("tag_name").getAsString();

                            String releaseName = release.get("name").getAsString();
                            if (releaseName != null && !releaseName.isEmpty() && !releaseName.contains("(DEV)")) {
                                latestPreReleaseName = releaseName.replace("Nukkit PM1E ", "");
                            }
                        }
                    } else {
                        if (latestReleaseTag == null) {
                            latestReleaseTag = release.get("tag_name").getAsString();

                            String releaseName = release.get("name").getAsString();
                            if (releaseName == null || releaseName.isEmpty()) {
                                latestReleaseName = "Invalid release info for build " + latestReleaseTag;
                            } else {
                                latestReleaseName = releaseName.replace("Nukkit PM1E ", "");
                            }
                        }
                    }
                }

                if (latestReleaseTag == null) {
                    log.debug("Could not check for updates");
                    return;
                }

                try {
                    int currentVersion = Integer.parseInt(Nukkit.BUILD_VERSION_NUMBER);
                    int latestVersion = Integer.parseInt(latestReleaseTag);

                    if (currentVersion < latestVersion) { // Current build is not the latest release
                        sender.sendMessage("§c[Update] §eThere is a new version of Nukkit PetteriM1 Edition available! Current: " + Nukkit.BUILD_VERSION + " Latest: " + latestReleaseName);

                        if (sender instanceof ConsoleCommandSender) {
                            this.getLogger().info("§c[Update] §eYou can download the latest version from https://github.com/PetteriM1/NukkitPetteriM1Edition/releases");
                        }
                    } else if (currentVersion > latestVersion) { // Current build is unreleased
                        if (sender instanceof ConsoleCommandSender) {
                            log.warn("§eUnreleased build! Current: " + Nukkit.BUILD_VERSION + " Latest: " + latestReleaseName);
                        }
                    } else if (latestPreReleaseTag != null && latestPreReleaseName != null) { // Current build is the latest release and there's a pre-release available
                        int latestPreVersion = Integer.parseInt(latestPreReleaseTag);

                        if (currentVersion < latestPreVersion) {
                            sender.sendMessage("§c[Update] §eThere is a new pre-release version of Nukkit PetteriM1 Edition available! Current: " + Nukkit.BUILD_VERSION + " Latest: " + latestPreReleaseName);

                            if (sender instanceof ConsoleCommandSender) {
                                this.getLogger().info("§c[Update] §eYou can download the latest version from https://github.com/PetteriM1/NukkitPetteriM1Edition/releases");
                            }
                        }
                    } else { // No updates available
                        if (notifyNoUpdate) {
                            sender.sendMessage("§aYou are running the latest version.");
                        }
                    }
                } catch (Exception ex) {
                    log.debug("Failed to parse build info", ex);
                }
            } catch (Exception ex) {
                log.debug("Update check failed", ex);
            }
        });
    }

    public void updatePlayerListData(UUID uuid, long entityId, String name, Skin skin) {
        this.updatePlayerListData(uuid, entityId, name, skin, "", this.playerList.values());
    }

    public void updatePlayerListData(UUID uuid, long entityId, String name, Skin skin, String xboxUserId) {
        this.updatePlayerListData(uuid, entityId, name, skin, xboxUserId, this.playerList.values());
    }

    public void updatePlayerListData(UUID uuid, long entityId, String name, Skin skin, Player[] players) {
        this.updatePlayerListData(uuid, entityId, name, skin, "", players);
    }

    public void updatePlayerListData(UUID uuid, long entityId, String name, Skin skin, String xboxUserId, Player[] players) {
        this.updatePlayerListData(new PlayerListPacket.Entry(uuid, entityId, name, skin, xboxUserId, Color.WHITE), players);
    }

    public void updatePlayerListData(PlayerListPacket.Entry playerListEntry, Player[] players) {
        PlayerListPacket pk = new PlayerListPacket();
        pk.type = PlayerListPacket.TYPE_ADD;
        pk.entries = new PlayerListPacket.Entry[]{playerListEntry};
        this.batchPackets(players, new DataPacket[]{pk}); // This is sent "directly" so it always gets through before possible TYPE_REMOVE packet for NPCs etc.
    }

    public void updatePlayerListData(UUID uuid, long entityId, String name, Skin skin, String xboxUserId, Collection<Player> players) {
        this.updatePlayerListData(uuid, entityId, name, skin, xboxUserId, players.toArray(new Player[0]));
    }


}

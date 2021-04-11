package cn.nukkit;

import cn.nukkit.block.Block;
import cn.nukkit.blockentity.*;
import cn.nukkit.command.*;
import cn.nukkit.console.NukkitConsole;
import cn.nukkit.dispenser.DispenseBehaviorRegister;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.entity.item.*;
import cn.nukkit.entity.mob.*;
import cn.nukkit.entity.passive.*;
import cn.nukkit.entity.projectile.*;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.LevelInitEvent;
import cn.nukkit.event.level.LevelLoadEvent;
import cn.nukkit.event.server.BatchPacketsEvent;
import cn.nukkit.event.server.PlayerDataSerializeEvent;
import cn.nukkit.event.server.QueryRegenerateEvent;
import cn.nukkit.event.server.ServerStopEvent;
import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.Recipe;
import cn.nukkit.item.Item;
import cn.nukkit.item.RuntimeItems;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.lang.BaseLang;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.LevelProviderManager;
import cn.nukkit.level.format.anvil.Anvil;
import cn.nukkit.level.generator.*;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.metadata.EntityMetadataStore;
import cn.nukkit.metadata.LevelMetadataStore;
import cn.nukkit.metadata.PlayerMetadataStore;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.BatchingHelper;
import cn.nukkit.network.Network;
import cn.nukkit.network.RakNetInterface;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
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
import cn.nukkit.scheduler.ServerScheduler;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.*;
import cn.nukkit.utils.bugreport.ExceptionHandler;
import co.aikar.timings.Timings;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.extern.log4j.Log4j2;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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

    public static final String BROADCAST_CHANNEL_ADMINISTRATIVE = "nukkit.broadcast.admin";
    public static final String BROADCAST_CHANNEL_USERS = "nukkit.broadcast.user";

    private static Server instance;

    private final BanList banByName;
    private final BanList banByIP;
    private final Config operators;
    private final Config whitelist;

    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private boolean hasStopped;

    private final PluginManager pluginManager;
    private final ServerScheduler scheduler;

    private int tickCounter;
    private long nextTick;
    private final float[] tickAverage = {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20};
    private final float[] useAverage = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private float maxTick = 20;
    private float maxUse = 0;

    private final NukkitConsole console;
    private final ConsoleThread consoleThread;

    private final SimpleCommandMap commandMap;
    private final CraftingManager craftingManager;
    private final ResourcePackManager resourcePackManager;
    private final ConsoleCommandSender consoleSender;

    private int maxPlayers;
    private boolean autoSave = true;
    private RCON rcon;

    private final EntityMetadataStore entityMetadata;
    private final PlayerMetadataStore playerMetadata;
    private final LevelMetadataStore levelMetadata;
    private final Network network;

    private boolean autoTickRate;
    private int autoTickRateLimit;
    private boolean alwaysTickPlayers;
    private int baseTickRate;
    private int difficulty;
    private int defaultGameMode = Integer.MAX_VALUE;

    private int autoSaveTicker;
    private int autoSaveTicks;

    private final BaseLang baseLang;
    private boolean forceLanguage;

    private final UUID serverID;

    private final String filePath;
    private final String dataPath;
    private final String pluginPath;

    private QueryHandler queryHandler;
    private QueryRegenerateEvent queryRegenerateEvent;
    private final Config properties;

    public SentryClient sentry;

    private final Map<InetSocketAddress, Player> players = new HashMap<>();
    final Map<UUID, Player> playerList = new HashMap<>();

    public static final List<String> disabledSpawnWorlds = new ArrayList<>();
    public static final List<String> nonAutoSaveWorlds = new ArrayList<>();
    public static final List<String> multiNetherWorlds = new ArrayList<>();

    private static final Pattern uuidPattern = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}.dat$");

    private final Map<Integer, Level> levels = new HashMap<Integer, Level>() {
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

    private Level[] levelArray = new Level[0];
    private final ServiceManager serviceManager = new NKServiceManager();
    private Level defaultLevel;
    private final Thread currentThread;
    private Watchdog watchdog;
    private final DB nameLookup;
    private PlayerDataSerializer playerDataSerializer;
    public static List<String> noTickingWorlds = new ArrayList<>();

    private SpawnerTask spawnerTask;
    private final BatchingHelper batchingHelper;

    /* Some settings */
    private String motd;
    private String ip;
    private int port;
    private int viewDistance;
    private int gamemode;
    private int skinChangeCooldown;
    private int spawnRadius;
    private boolean suomicraftMode;
    private boolean doLevelGC;
    private boolean mobAI;
    private boolean shouldSavePlayerData;
    private boolean getAllowFlight;
    private boolean isHardcore;
    private boolean callBatchPkEv;
    private boolean forceResources;
    public boolean whitelistEnabled;
    private boolean forceGamemode;
    private boolean netherEnabled;
    public boolean xboxAuth;
    public boolean spawnEggsEnabled;
    public boolean xpBottlesOnCreative;
    public boolean dimensionsEnabled;
    boolean callDataPkEv;
    boolean bedSpawnpoints;
    boolean achievements;
    boolean banAuthFailed;
    boolean endEnabled;
    boolean pvp;
    boolean announceAchievements;
    boolean checkOpMovement;
    boolean doNotLimitInteractions;
    public int despawnTicks;
    int chunksPerTick;
    int spawnThreshold;
    int c_s_spawnThreshold;
    public int networkCompressionLevel;
    public boolean doNotLimitSkinGeometry;
    public boolean blockListener;
    public boolean explosionBreakBlocks;
    public boolean vanillaBB;
    public boolean stopInGame;
    public boolean opInGame;
    public boolean lightUpdates;
    public boolean queryPlugins;
    public boolean despawnEntities;
    public boolean strongIPBans;
    public boolean spawnAnimals;
    public boolean spawnMobs;
    public boolean anvilsEnabled;
    public boolean savePlayerDataByUuid;
    public boolean vanillaPortals;
    public boolean personaSkins;
    public boolean cacheChunks;
    public boolean callEntityMotionEv;

    Server(final String filePath, String dataPath, String pluginPath, boolean loadPlugins, boolean debug) {
        Preconditions.checkState(instance == null, "Already initialized!");
        currentThread = Thread.currentThread(); // Saves the current thread instance as a reference, used in Server#isPrimaryThread()
        instance = this;

        this.filePath = filePath;
        if (!new File(dataPath + "worlds/").exists()) {
            new File(dataPath + "worlds/").mkdirs();
        }

        if (!new File(pluginPath).exists()) {
            new File(pluginPath).mkdirs();
        }

        this.dataPath = new File(dataPath).getAbsolutePath() + '/';
        this.pluginPath = new File(pluginPath).getAbsolutePath() + '/';

        this.playerDataSerializer = new DefaultPlayerDataSerializer(this);

        this.console = new NukkitConsole();
        this.consoleThread = new ConsoleThread();
        this.consoleThread.start();
        this.console.setExecutingCommands(true);

        log.info("Loading server properties...");
        this.properties = new Config(this.dataPath + "server.properties", Config.PROPERTIES, new ServerProperties());

        if (!this.getPropertyBoolean("ansi-title", true)) Nukkit.TITLE = false;

        int debugLvl = NukkitMath.clamp(this.getPropertyInt("debug-level", 1), 1, 3);
        if (debug && debugLvl < 2) {
            debugLvl = 2;
        }
        Nukkit.DEBUG = debugLvl;

        this.loadSettings();

        if (this.getPropertyBoolean("automatic-bug-report", true)) {
            ExceptionHandler.registerExceptionHandler();
            this.sentry = SentryClientFactory.sentryClient("https://0e094ce5464f4663a0b521d61f4bfe54@o381665.ingest.sentry.io/5209314");
        }

        if (!new File(dataPath + "players/").exists() && this.shouldSavePlayerData) {
            new File(dataPath + "players/").mkdirs();
        }

        this.baseLang = new BaseLang(this.getPropertyString("language", BaseLang.FALLBACK_LANGUAGE));

        Object poolSize = this.getProperty("async-workers", "auto");
        if (!(poolSize instanceof Integer)) {
            try {
                poolSize = Integer.valueOf((String) poolSize);
            } catch (Exception e) {
                poolSize = Math.max(Runtime.getRuntime().availableProcessors() + 1, 4);
            }
        }

        ServerScheduler.WORKERS = (int) poolSize;

        Zlib.setProvider(this.getPropertyInt("zlib-provider", 2));

        this.scheduler = new ServerScheduler();

        this.batchingHelper = new BatchingHelper();

        if (this.getPropertyBoolean("enable-rcon", false)) {
            try {
                this.rcon = new RCON(this, this.getPropertyString("rcon.password", ""), (!this.getIp().isEmpty()) ? this.getIp() : "0.0.0.0", this.getPropertyInt("rcon.port", this.getPort()));
            } catch (IllegalArgumentException e) {
                log.error(baseLang.translateString(e.getMessage(), e.getCause().getMessage()));
            }
        }

        this.entityMetadata = new EntityMetadataStore();
        this.playerMetadata = new PlayerMetadataStore();
        this.levelMetadata = new LevelMetadataStore();

        this.operators = new Config(this.dataPath + "ops.txt", Config.ENUM);
        this.whitelist = new Config(this.dataPath + "white-list.txt", Config.ENUM);
        this.banByName = new BanList(this.dataPath + "banned-players.json");
        this.banByName.load();
        this.banByIP = new BanList(this.dataPath + "banned-ips.json");
        this.banByIP.load();

        this.maxPlayers = this.getPropertyInt("max-players", 50);
        this.setAutoSave(this.getPropertyBoolean("auto-save", true));

        if (this.isHardcore && this.difficulty < 3) {
            this.setDifficulty(3);
        } else {
            this.setDifficulty(getDifficultyFromString(this.getPropertyString("difficulty", "2")));
        }

        org.apache.logging.log4j.Level currentLevel = Nukkit.getLogLevel();
        for (org.apache.logging.log4j.Level level : org.apache.logging.log4j.Level.values()) {
            if (level.intLevel() == (Nukkit.DEBUG + 3) * 100  && level.intLevel() > currentLevel.intLevel()) {
                Nukkit.setLogLevel(level);
                break;
            }
        }

        log.info("\u00A7b-- \u00A7cNukkit \u00A7aPetteriM1 Edition \u00A7b--");

        this.consoleSender = new ConsoleCommandSender();
        this.commandMap = new SimpleCommandMap(this);

        registerEntities();
        registerBlockEntities();

        Block.init();
        Enchantment.init();
        Item.init();
        EnumBiome.values();
        Effect.init();
        Potion.init();
        Attribute.init();
        DispenseBehaviorRegister.init();
        GlobalBlockPalette.getOrCreateRuntimeId(ProtocolInfo.CURRENT_PROTOCOL, 0, 0);
        RuntimeItems.init();

        // Convert legacy data before plugins get the chance to mess with it
        try {
            nameLookup = Iq80DBFactory.factory.open(new File(dataPath, "players"), new Options()
                            .createIfMissing(true)
                            .compressionType(CompressionType.ZLIB_RAW));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (this.savePlayerDataByUuid) {
            convertLegacyPlayerData();
        }

        this.serverID = UUID.randomUUID();

        this.craftingManager = new CraftingManager();
        this.resourcePackManager = new ResourcePackManager(new File(Nukkit.DATA_PATH, "resource_packs"));

        this.pluginManager = new PluginManager(this, this.commandMap);
        this.pluginManager.subscribeToPermission(Server.BROADCAST_CHANNEL_ADMINISTRATIVE, this.consoleSender);

        this.pluginManager.registerInterface(JavaPluginLoader.class);

        this.queryRegenerateEvent = new QueryRegenerateEvent(this, 5);

        log.info(this.baseLang.translateString("nukkit.server.networkStart", new String[]{this.getIp().isEmpty() ? "*" : this.getIp(), String.valueOf(this.getPort())}));
        this.network = new Network(this);
        this.network.setName(this.getMotd());
        this.network.setSubName(this.getSubMotd());
        this.network.registerInterface(new RakNetInterface(this));

        if (loadPlugins) {
            this.pluginManager.loadPlugins(this.pluginPath);
            this.enablePlugins(PluginLoadOrder.STARTUP);
        }

        LevelProviderManager.addProvider(this, Anvil.class);

        Generator.addGenerator(Flat.class, "flat", Generator.TYPE_FLAT);
        Generator.addGenerator(Normal.class, "normal", Generator.TYPE_INFINITE);
        Generator.addGenerator(Normal.class, "default", Generator.TYPE_INFINITE);
        Generator.addGenerator(Nether.class, "nether", Generator.TYPE_NETHER);
        Generator.addGenerator(End.class, "the_end", Generator.TYPE_THE_END);
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

        this.properties.save(true);

        if (this.defaultLevel == null) {
            this.getLogger().emergency(this.baseLang.translateString("nukkit.level.defaultError"));
            this.forceShutdown();
            return;
        }

        for (Map.Entry<Integer, Level> entry : this.getLevels().entrySet()) {
            Level level = entry.getValue();
            this.getLogger().debug("Preparing spawn region for level " + level.getName());
            Position spawn = level.getSpawnLocation();
            level.populateChunk(spawn.getChunkX(), spawn.getChunkZ(), true);
        }

        // Load levels
        if (this.getPropertyBoolean("load-all-worlds", true)) {
            try {
                for (File fs : new File(new File("").getCanonicalPath() + "/worlds/").listFiles()) {
                    if ((fs.isDirectory() && !this.isLevelLoaded(fs.getName()))) {
                        this.loadLevel(fs.getName());
                    }
                }
                if (netherEnabled) {
                    if (this.getLevelByName("nether") == null) {
                        this.generateLevel("nether", System.currentTimeMillis(), Generator.getGenerator(Generator.TYPE_NETHER));
                        this.loadLevel("nether");
                    }
                    String list = this.getPropertyString("multi-nether-worlds");
                    if (!list.trim().isEmpty()) {
                        StringTokenizer tokenizer = new StringTokenizer(list, ", ");
                        while (tokenizer.hasMoreTokens()) {
                            String world = tokenizer.nextToken();
                            multiNetherWorlds.add(world);
                            String nether = world + "-nether";
                            if (this.getLevelByName(nether) == null) {
                                this.generateLevel(nether, System.currentTimeMillis(), Generator.getGenerator(Generator.TYPE_NETHER));
                                this.loadLevel(nether);
                            }
                        }
                    }
                }
                if (this.getLevelByName("the_end") == null && endEnabled) {
                    this.generateLevel("the_end", System.currentTimeMillis(), Generator.getGenerator(Generator.TYPE_THE_END));
                    this.loadLevel("the_end");
                }
            } catch (Exception e) {
                this.getLogger().error("Unable to load levels", e);
            }
        }

        if (loadPlugins) {
            this.enablePlugins(PluginLoadOrder.POSTWORLD);
        }

        if (this.getPropertyBoolean("thread-watchdog", true)) {
            this.watchdog = new Watchdog(this, this.getPropertyInt("thread-watchdog-tick", 60000));
            this.watchdog.start();
        }

        String worlds1 = Server.getInstance().getPropertyString("worlds-entity-spawning-disabled");
        if (!worlds1.trim().isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(worlds1, ", ");
            while (tokenizer.hasMoreTokens()) {
                disabledSpawnWorlds.add(tokenizer.nextToken());
            }
        }

        String worlds2 = Server.getInstance().getPropertyString("worlds-level-auto-save-disabled");
        if (!worlds2.trim().isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(worlds2, ", ");
            while (tokenizer.hasMoreTokens()) {
                nonAutoSaveWorlds.add(tokenizer.nextToken());
            }
        }

        if (this.getPropertyBoolean("entity-auto-spawn-task", true)) {
            this.spawnerTask = new SpawnerTask();
            int spawnerTicks = Math.max(this.getPropertyInt("ticks-per-entity-spawns", 200), 2) >> 1; // Run the spawner on 2x speed but spawn only either monsters or animals
            this.scheduler.scheduleDelayedRepeatingTask(this.spawnerTask, spawnerTicks, spawnerTicks);
        }

        // Check for updates
        CompletableFuture.runAsync(() -> {
            try {
                URLConnection request = new URL("https://api.github.com/repos/PetteriM1/NukkitPetteriM1Edition/commits/master").openConnection();
                request.connect();
                InputStreamReader content = new InputStreamReader((InputStream) request.getContent());
                String latest = "git-" + new JsonParser().parse(content).getAsJsonObject().get("sha").getAsString().substring(0, 7);
                content.close();

                boolean isMaster = Nukkit.getBranch().equals("master");
                if (!this.getNukkitVersion().equals(latest) && !this.getNukkitVersion().equals("git-null") && isMaster) {
                    this.getLogger().info("\u00A7c[Update] \u00A7eThere is a new build of Nukkit PetteriM1 Edition available! Current: " + this.getNukkitVersion() + " Latest: " + latest);
                    this.getLogger().info("\u00A7c[Update] \u00A7eYou can download the latest build from https://github.com/PetteriM1/NukkitPetteriM1Edition/releases");
                } else if (!isMaster) {
                    this.getLogger().warning("\u00A7eYou are running a dev build! Do not use in production! Branch: " + Nukkit.getBranch());
                }

                this.getLogger().debug("Update check done");
            } catch (Exception ignore) {
                this.getLogger().debug("Update check failed");
            }
        });

        this.start();
    }

    public int broadcastMessage(String message) {
        return this.broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    public int broadcastMessage(TextContainer message) {
        return this.broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    public int broadcastMessage(String message, CommandSender[] recipients) {
        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.length;
    }

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


    public static void broadcastPacket(Collection<Player> players, DataPacket packet) {
        if (packet.pid() == ProtocolInfo.BATCH_PACKET) {
            for (Player player : players) {
                player.directDataPacket(packet);
            }
            return;
        }
        instance.batchPackets(players.toArray(new Player[0]), new DataPacket[]{packet});
    }

    public static void broadcastPacket(Player[] players, DataPacket packet) {
        if (packet.pid() == ProtocolInfo.BATCH_PACKET) {
            for (Player player : players) {
                player.directDataPacket(packet);
            }
            return;
        }
        instance.batchPackets(players, new DataPacket[]{packet});
    }

    public static void broadcastPackets(Player[] players, DataPacket[] packets) {
        instance.batchPackets(players, packets, false);
    }

    public void batchPackets(Player[] players, DataPacket[] packets) {
        this.batchPackets(players, packets, false);
    }

    public void batchPackets(Player[] players, DataPacket[] packets, boolean forceSync) {
        if (players == null || packets == null || players.length == 0 || packets.length == 0) {
            return;
        }

        if (callBatchPkEv) {
            BatchPacketsEvent ev = new BatchPacketsEvent(players, packets, forceSync);
            pluginManager.callEvent(ev);
            if (ev.isCancelled()) {
                return;
            }
        }

        this.batchingHelper.batchPackets(players, packets);
    }

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

    public void enablePlugin(Plugin plugin) {
        this.pluginManager.enablePlugin(plugin);
    }

    public void disablePlugins() {
        this.pluginManager.disablePlugins();
    }

    public boolean dispatchCommand(CommandSender sender, String commandLine) throws ServerException {
        // First we need to check if this command is on the main thread or not, if not, warn the user
        if (!this.isPrimaryThread() && !this.suomiCraftPEMode()) {
            getLogger().warning("Command Dispatched Async: " + commandLine);
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

    public ConsoleCommandSender getConsoleSender() {
        return consoleSender;
    }

    public void reload() {
        log.info("Reloading...");

        log.info("Saving levels...");

        for (Level level : this.levelArray) {
            level.save();
        }

        this.pluginManager.clearPlugins();
        this.commandMap.clearCommands();

        log.info("Reloading properties...");
        this.properties.reload();
        this.maxPlayers = this.getPropertyInt("max-players", 50);

        if (this.isHardcore && this.difficulty < 3) {
            this.setDifficulty(3);
        }

        this.loadSettings();

        this.banByIP.load();
        this.banByName.load();
        this.reloadWhitelist();
        this.operators.reload();

        for (BanEntry entry : this.banByIP.getEntires().values()) {
            try {
                this.network.blockAddress(InetAddress.getByName(entry.getName()), -1);
            } catch (UnknownHostException ignore) {}
        }

        this.pluginManager.registerInterface(JavaPluginLoader.class);
        this.pluginManager.loadPlugins(this.pluginPath);
        this.enablePlugins(PluginLoadOrder.STARTUP);
        this.enablePlugins(PluginLoadOrder.POSTWORLD);
        Timings.reset();
    }

    public void shutdown() {
        isRunning.compareAndSet(true, false);
    }

    public void forceShutdown() {
        this.forceShutdown(this.getPropertyString("shutdown-message", "§cServer closed").replace("§n", "\n"));
    }

    public void forceShutdown(String reason) {
        if (this.hasStopped) {
            return;
        }

        try {
            isRunning.compareAndSet(true, false);

            this.hasStopped = true;

            ServerStopEvent serverStopEvent = new ServerStopEvent();
            pluginManager.callEvent(serverStopEvent);

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

            this.getLogger().debug("Unloading all levels...");
            for (Level level : this.levelArray) {
                this.unloadLevel(level, true);
                this.nextTick = System.currentTimeMillis(); // Fix Watchdog killing the server while saving worlds
            }

            this.getLogger().debug("Removing event handlers...");
            HandlerList.unregisterAll();

            this.getLogger().debug("Stopping all tasks...");
            this.scheduler.cancelAllTasks();
            this.scheduler.mainThreadHeartbeat(Integer.MAX_VALUE);

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

            this.getLogger().debug("Disabling timings...");
            Timings.stopServer();
        } catch (Exception e) {
            log.fatal("Exception happened while shutting down, exiting the process", e);
            System.exit(1);
        }
    }

    public void start() {
        if (this.getPropertyBoolean("enable-query", true)) {
            this.queryHandler = new QueryHandler();
        }

        for (BanEntry entry : this.banByIP.getEntires().values()) {
            try {
                this.network.blockAddress(InetAddress.getByName(entry.getName()), -1);
            } catch (UnknownHostException ignore) {}
        }

        this.tickCounter = 0;

        log.info(this.baseLang.translateString("nukkit.server.startFinished", String.valueOf((double) (System.currentTimeMillis() - Nukkit.START_TIME) / 1000)));

        this.tickProcessor();
        this.forceShutdown();
    }

    public void handlePacket(InetSocketAddress address, ByteBuf payload) {
        try {
            if (!payload.isReadable(3)) {
                return;
            }
            byte[] prefix = new byte[2];
            payload.readBytes(prefix);

            if (!Arrays.equals(prefix, new byte[]{(byte) 0xfe, (byte) 0xfd})) {
                return;
            }
            if (this.queryHandler != null) {
                this.queryHandler.handle(address, payload);
            }
        } catch (Exception e) {
            log.error("Error whilst handling packet", e);

            this.network.blockAddress(address.getAddress(), -1);
        }
    }

    private int lastLevelGC;

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

                        if (doLevelGC) { // Instead of wasting time, do something potentially useful
                            int offset = 0;
                            for (int i = 0; i < levelArray.length; i++) {
                                offset = (i + lastLevelGC) % levelArray.length;
                                levelArray[offset].doGarbageCollection(allocated - 1);
                                allocated = next - System.currentTimeMillis();
                                if (allocated <= 0) break;
                            }
                            lastLevelGC = offset + 1;
                        }

                        if (allocated > 0 || !doLevelGC) {
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

    public void onPlayerCompleteLoginSequence(Player player) {
        this.playerList.put(player.getUniqueId(), player);
        this.updatePlayerListData(player.getUniqueId(), player.getId(), player.getDisplayName(), player.getSkin(), player.getLoginChainData().getXUID());
    }

    public void addPlayer(InetSocketAddress socketAddress, Player player) {
        this.players.put(socketAddress, player);
    }

    public void addOnlinePlayer(Player player) {
        this.playerList.put(player.getUniqueId(), player);
        this.updatePlayerListData(player.getUniqueId(), player.getId(), player.getDisplayName(), player.getSkin(), player.getLoginChainData().getXUID());
    }

    public void removeOnlinePlayer(Player player) {
        if (this.playerList.containsKey(player.getUniqueId())) {
            this.playerList.remove(player.getUniqueId());

            PlayerListPacket pk = new PlayerListPacket();
            pk.type = PlayerListPacket.TYPE_REMOVE;
            pk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(player.getUniqueId())};

            Server.broadcastPacket(this.playerList.values(), pk);
        }
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
        PlayerListPacket pk = new PlayerListPacket();
        pk.type = PlayerListPacket.TYPE_ADD;
        pk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid, entityId, name, skin, xboxUserId)};
        Server.broadcastPacket(players, pk);
    }

    public void updatePlayerListData(UUID uuid, long entityId, String name, Skin skin, String xboxUserId, Collection<Player> players) {
        this.updatePlayerListData(uuid, entityId, name, skin, xboxUserId, players.toArray(new Player[0]));
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

    public void sendFullPlayerListData(Player player) {
        PlayerListPacket pk = new PlayerListPacket();
        pk.type = PlayerListPacket.TYPE_ADD;
        pk.entries = this.playerList.values().stream()
                .map(p -> new PlayerListPacket.Entry(
                        p.getUniqueId(),
                        p.getId(),
                        p.getDisplayName(),
                        p.getSkin(),
                        p.getLoginChainData().getXUID()))
                .toArray(PlayerListPacket.Entry[]::new);
        player.dataPacket(pk);
    }

    public void sendRecipeList(Player player) {
        if (player.protocol >= ProtocolInfo.v1_16_220) {
            player.dataPacket(CraftingManager.packet431);
        } else if (player.protocol >= ProtocolInfo.v1_16_100) {
            player.dataPacket(CraftingManager.packet419);
        } else if (player.protocol >= ProtocolInfo.v1_16_0) {
            player.dataPacket(CraftingManager.packet407);
        } else if (player.protocol > ProtocolInfo.v1_12_0) {
            player.dataPacket(CraftingManager.packet388);
        } else if (player.protocol == ProtocolInfo.v1_12_0) {
            player.dataPacket(CraftingManager.packet361);
        } else if (player.protocol == ProtocolInfo.v1_11_0) {
             player.dataPacket(CraftingManager.packet354);
        } else if (player.protocol == ProtocolInfo.v1_10_0) {
            player.dataPacket(CraftingManager.packet340);
        } else if (player.protocol == ProtocolInfo.v1_9_0 || player.protocol == ProtocolInfo.v1_8_0 || player.protocol == ProtocolInfo.v1_7_0) { // these should work just fine
            player.dataPacket(CraftingManager.packet313);
        }
        // Don't send recipes if they wouldn't work anyways
    }

    private void checkTickUpdates(int currentTick) {
        if (this.alwaysTickPlayers) {
            for (Player p : new ArrayList<>(this.players.values())) {
                p.onUpdate(currentTick);
            }
        }

        // Do level ticks
        for (Level level : this.levelArray) {
            if (level.getTickRate() > this.baseTickRate && --level.tickRateCounter > 0) {
                continue;
            }

            try {
                long levelTime = System.currentTimeMillis();
                level.doTick(currentTick);
                int tickMs = (int) (System.currentTimeMillis() - levelTime);
                level.tickRateTime = tickMs;

                if (this.autoTickRate) {
                    if (tickMs < 50 && level.getTickRate() > this.baseTickRate) {
                        int r;
                        level.setTickRate(r = level.getTickRate() - 1);
                        if (r > this.baseTickRate) {
                            level.tickRateCounter = level.getTickRate();
                        }
                        this.getLogger().debug("Raising level \"" + level.getName() + "\" tick rate to " + level.getTickRate() + " ticks");
                    } else if (tickMs >= 50) {
                        if (level.getTickRate() == this.baseTickRate) {
                            level.setTickRate(Math.max(this.baseTickRate + 1, Math.min(this.autoTickRateLimit, tickMs / 50)));
                            this.getLogger().debug("Level \"" + level.getName() + "\" took " + tickMs + "ms, setting tick rate to " + level.getTickRate() + " ticks");
                        } else if ((tickMs / level.getTickRate()) >= 50 && level.getTickRate() < this.autoTickRateLimit) {
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

    public void doAutoSave() {
        if (this.autoSave) {
            if (Timings.levelSaveTimer != null) Timings.levelSaveTimer.startTiming();
            for (Player player : new ArrayList<>(this.players.values())) {
                if (player.isOnline()) {
                    player.save(true);
                } else if (!player.isConnected()) {
                    this.removePlayer(player);
                }
            }

            for (Level level : this.levelArray) {
                if (!nonAutoSaveWorlds.contains(level.getName())) {
                    level.save();
                }
            }
            if (Timings.levelSaveTimer != null) Timings.levelSaveTimer.stopTiming();
        }
    }

    private void tick() {
        long tickTime = System.currentTimeMillis();

        long time = tickTime - this.nextTick;
        if (time < -25) {
            try {
                Thread.sleep(Math.max(5, -time - 25));
            } catch (InterruptedException e) {
                Server.getInstance().getLogger().logException(e);
            }
        }

        long tickTimeNano = System.nanoTime();
        if ((tickTime - this.nextTick) < -25) {
            return;
        }

        if (Timings.isTimingsEnabled()) {
            Timings.fullServerTickTimer.startTiming();
        }

        ++this.tickCounter;

        if (Timings.connectionTimer != null) Timings.connectionTimer.startTiming();
        this.network.processInterfaces();

        if (this.rcon != null) {
            this.rcon.check();
        }
        if (Timings.connectionTimer != null) Timings.connectionTimer.stopTiming();

        if (Timings.schedulerTimer != null) Timings.schedulerTimer.startTiming();
        this.scheduler.mainThreadHeartbeat(this.tickCounter);
        if (Timings.schedulerTimer != null) Timings.schedulerTimer.stopTiming();

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
                level.doChunkGarbageCollection();
            }
        }

        if (Timings.isTimingsEnabled()) {
            Timings.fullServerTickTimer.stopTiming();
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

    public long getNextTick() {
        return nextTick;
    }

    private void titleTick() {
        if (!Nukkit.TITLE) return;

        Runtime runtime = Runtime.getRuntime();
        double used = NukkitMath.round((double) (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024, 2);
        double max = NukkitMath.round(((double) runtime.maxMemory()) / 1024 / 1024, 2);
        System.out.print((char) 0x1b + "]0;" + Nukkit.NUKKIT +
                " | Online " + this.players.size() + '/' + this.maxPlayers +
                " | Memory " + Math.round(used / max * 100) + '%' +
                /*" | U " + NukkitMath.round((this.network.getUpload() / 1024 * 1000), 2) +
                " D " + NukkitMath.round((this.network.getDownload() / 1024 * 1000), 2) + " kB/s" +*/
                " | TPS " + this.getTicksPerSecond() +
                " | Load " + this.getTickUsage() + '%' + (char) 0x07);
    }

    public QueryRegenerateEvent getQueryInformation() {
        return this.queryRegenerateEvent;
    }

    public String getName() {
        return Nukkit.NUKKIT;
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    public String getNukkitVersion() {
        return Nukkit.VERSION;
    }

    public String getCodename() {
        return Nukkit.CODENAME;
    }

    public String getVersion() {
        return Nukkit.MINECRAFT_VERSION;
    }

    public String getApiVersion() {
        return Nukkit.API_VERSION;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getPluginPath() {
        return pluginPath;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getPort() {
        return port;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public String getIp() {
        return ip;
    }

    public UUID getServerUniqueId() {
        return this.serverID;
    }

    public boolean getAutoSave() {
        return this.autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
        for (Level level : this.levelArray) {
            level.setAutoSave(this.autoSave);
        }
    }

    public String getLevelType() {
        return this.getPropertyString("level-type", "default");
    }

    public int getGamemode() {
        return gamemode;
    }

    public boolean getForceGamemode() {
        return this.forceGamemode;
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

    public static int getGamemodeFromString(String str) {
        switch (str.trim().toLowerCase()) {
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
        }
        return -1;
    }

    public static int getDifficultyFromString(String str) {
        switch (str.trim().toLowerCase()) {
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

    public int getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(int difficulty) {
        int value = difficulty;
        if (value < 0) value = 0;
        if (value > 3) value = 3;
        this.difficulty = value;
        this.setPropertyInt("difficulty", value);
    }

    public boolean hasWhitelist() {
        return this.whitelistEnabled;
    }

    public int getSpawnRadius() {
        return spawnRadius;
    }

    public boolean getAllowFlight() {
        return getAllowFlight;
    }

    public boolean isHardcore() {
        return this.isHardcore;
    }

    public int getDefaultGamemode() {
        if (this.defaultGameMode == Integer.MAX_VALUE) {
            this.defaultGameMode = this.getGamemode();
        }
        return this.defaultGameMode;
    }

    public String getMotd() {
        return motd;
    }

    public String getSubMotd() {
        String sub = this.getPropertyString("sub-motd", "Powered by Nukkit");
        if (sub.isEmpty()) sub = "Powered by Nukkit";
        return sub;
    }

    public boolean getForceResources() {
        return this.forceResources;
    }

    public boolean getMobAiEnabled() {
        return this.mobAI;
    }

    public MainLogger getLogger() {
        return MainLogger.getLogger();
    }

    public EntityMetadataStore getEntityMetadata() {
        return entityMetadata;
    }

    public PlayerMetadataStore getPlayerMetadata() {
        return playerMetadata;
    }

    public LevelMetadataStore getLevelMetadata() {
        return levelMetadata;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public CraftingManager getCraftingManager() {
        return craftingManager;
    }

    public ResourcePackManager getResourcePackManager() {
        return resourcePackManager;
    }

    public ServerScheduler getScheduler() {
        return scheduler;
    }

    public int getTick() {
        return tickCounter;
    }

    public float getTicksPerSecond() {
        return ((float) Math.round(this.maxTick * 100)) / 100;
    }

    public float getTicksPerSecondAverage() {
        float sum = 0;
        int count = this.tickAverage.length;
        for (float aTickAverage : this.tickAverage) {
            sum += aTickAverage;
        }
        return (float) NukkitMath.round(sum / count, 2);
    }

    public float getTickUsage() {
        return (float) NukkitMath.round(this.maxUse * 100, 2);
    }

    public float getTickUsageAverage() {
        float sum = 0;
        for (float aUseAverage : this.useAverage) {
            sum += aUseAverage;
        }
        return ((float) Math.round(sum / this.useAverage.length * 100)) / 100;
    }

    public SimpleCommandMap getCommandMap() {
        return commandMap;
    }

    public Map<UUID, Player> getOnlinePlayers() {
        return ImmutableMap.copyOf(playerList);
    }

    public int getOnlinePlayersCount() {
        return this.playerList.size();
    }

    public void addRecipe(Recipe recipe) {
        this.craftingManager.registerRecipe(recipe);
    }

    public Optional<Player> getPlayer(UUID uuid) {
        Preconditions.checkNotNull(uuid, "uuid");
        return Optional.ofNullable(playerList.get(uuid));
    }

    public Optional<UUID> lookupName(String name) {
        byte[] nameBytes = name.toLowerCase().getBytes(StandardCharsets.UTF_8);
        byte[] uuidBytes = nameLookup.get(nameBytes);
        if (uuidBytes == null) {
            return Optional.empty();
        }

        if (uuidBytes.length != 16) {
            log.warn("Invalid uuid in name lookup database detected! Removing...");
            nameLookup.delete(nameBytes);
            return Optional.empty();
        }

        ByteBuffer buffer = ByteBuffer.wrap(uuidBytes);
        return Optional.of(new UUID(buffer.getLong(), buffer.getLong()));
    }

    void updateName(UUID uuid, String name) {
        byte[] nameBytes = name.toLowerCase().getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());

        nameLookup.put(nameBytes, buffer.array());
    }

    public IPlayer getOfflinePlayer(final String name) {
        IPlayer result = this.getPlayerExact(name.toLowerCase());
        if (result != null) {
            return result;
        }

        return lookupName(name).map(uuid -> new OfflinePlayer(this, uuid))
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
            log.warn(this.getLanguage().translateString("nukkit.data.playerCorrupted", name));
            log.throwing(e);
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
            Position spawn = this.getDefaultLevel().getSafeSpawn();
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
                    .putShort("Air", 300)
                    .putBoolean("OnGround", true)
                    .putBoolean("Invulnerable", false);

            this.saveOfflinePlayerData(name, nbt, true, runEvent);
        }
        return nbt;
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
        String nameLower = name.toLowerCase();
        if (this.shouldSavePlayerData()) {
            PlayerDataSerializeEvent event = new PlayerDataSerializeEvent(nameLower, playerDataSerializer);
            if (runEvent) {
                pluginManager.callEvent(event);
            }

            this.getScheduler().scheduleTask(new Task() {
                boolean hasRun = false;

                @Override
                public void onRun(int currentTick) {
                    this.onCancel();
                }

                // Doing it like this ensures that the player data will be saved in a server shutdown
                @Override
                public void onCancel() {
                    if (!this.hasRun)    {
                        this.hasRun = true;
                        saveOfflinePlayerDataInternal(event.getSerializer(), tag, nameLower, event.getUuid().orElse(null));
                    }
                }
            }, async);
        }
    }

    private void saveOfflinePlayerDataInternal(PlayerDataSerializer serializer, CompoundTag tag, String name, UUID uuid) {
        try (OutputStream dataStream = serializer.write(name, uuid)) {
            NBTIO.writeGZIPCompressed(tag, dataStream, ByteOrder.BIG_ENDIAN);
        } catch (Exception e) {
            log.error(this.getLanguage().translateString("nukkit.data.saveError", name, e));
        }
    }

    private void convertLegacyPlayerData() {
        File dataDirectory = new File(getDataPath(), "players/");

        File[] files = dataDirectory.listFiles(file -> {
            String name = file.getName();
            return !uuidPattern.matcher(name).matches() && name.endsWith(".dat");
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

            if (new File(getDataPath() + "players/" + uuid.toString() + ".dat").exists()) {
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

    public Player getPlayer(String name) {
        Player found = null;
        name = name.toLowerCase();
        int delta = Integer.MAX_VALUE;
        for (Player player : this.getOnlinePlayers().values()) {
            if (player.getName().toLowerCase().startsWith(name)) {
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

    public Player getPlayerExact(String name) {
        for (Player player : this.getOnlinePlayers().values()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }

        return null;
    }

    public Player[] matchPlayer(String partialName) {
        partialName = partialName.toLowerCase();
        List<Player> matchedPlayer = new ArrayList<>();
        for (Player player : this.getOnlinePlayers().values()) {
            if (player.getName().toLowerCase().equals(partialName)) {
                return new Player[]{player};
            } else if (player.getName().toLowerCase().contains(partialName)) {
                matchedPlayer.add(player);
            }
        }

        return matchedPlayer.toArray(new Player[0]);
    }

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

    public Map<Integer, Level> getLevels() {
        return levels;
    }

    public Level getDefaultLevel() {
        return defaultLevel;
    }

    public void setDefaultLevel(Level defaultLevel) {
        if (defaultLevel == null || (this.isLevelLoaded(defaultLevel.getFolderName()) && defaultLevel != this.defaultLevel)) {
            this.defaultLevel = defaultLevel;
        }
    }

    public boolean isLevelLoaded(String name) {
        return this.getLevelByName(name) != null;
    }

    public Level getLevel(int levelId) {
        if (this.levels.containsKey(levelId)) {
            return this.levels.get(levelId);
        }
        return null;
    }

    public Level getLevelByName(String name) {
        for (Level level : this.levelArray) {
            if (level.getFolderName().equalsIgnoreCase(name)) {
                return level;
            }
        }

        return null;
    }

    public boolean unloadLevel(Level level) {
        return this.unloadLevel(level, false);
    }

    public boolean unloadLevel(Level level, boolean forceUnload) {
        if (level == this.defaultLevel && !forceUnload) {
            throw new IllegalStateException("The default level cannot be unloaded while running, please switch levels.");
        }

        return level.unload(forceUnload);

    }

    public boolean loadLevel(String name) {
        if (Objects.equals(name.trim(), "")) {
            throw new LevelException("Invalid empty level name");
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

        this.levels.put(level.getId(), level);

        level.initLevel();

        this.pluginManager.callEvent(new LevelLoadEvent(level));

        level.setTickRate(this.baseTickRate);

        return true;
    }

    public boolean generateLevel(String name) {
        return this.generateLevel(name, Utils.random.nextLong());
    }

    public boolean generateLevel(String name, long seed) {
        return this.generateLevel(name, seed, null);
    }

    public boolean generateLevel(String name, long seed, Class<? extends Generator> generator) {
        return this.generateLevel(name, seed, generator, new HashMap<>());
    }

    public boolean generateLevel(String name, long seed, Class<? extends Generator> generator, Map<String, Object> options) {
        return generateLevel(name, seed, generator, options, null);
    }

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
            provider = LevelProviderManager.getProviderByName("anvil");
        }

        String path;

        if (name.contains("/") || name.contains("\\")) {
            path = name;
        } else {
            path = this.dataPath + "worlds/" + name + '/';
        }

        Level level;
        try {
            provider.getMethod("generate", String.class, String.class, long.class, Class.class, Map.class).invoke(null, path, name, seed, generator, options);

            level = new Level(this, name, path, provider);
            this.levels.put(level.getId(), level);

            level.initLevel();

            level.setTickRate(this.baseTickRate);
        } catch (Exception e) {
            log.error(this.baseLang.translateString("nukkit.level.generationError", new String[]{name, Utils.getExceptionMessage(e)}));
            return false;
        }

        this.pluginManager.callEvent(new LevelInitEvent(level));

        this.pluginManager.callEvent(new LevelLoadEvent(level));

        return true;
    }

    public boolean isLevelGenerated(String name) {
        if (Objects.equals(name.trim(), "")) {
            return false;
        }

        String path = this.dataPath + "worlds/" + name + '/';
        if (this.getLevelByName(name) == null) {
            return LevelProviderManager.getProvider(path) != null;
        }

        return true;
    }

    public BaseLang getLanguage() {
        return baseLang;
    }

    public boolean isLanguageForced() {
        return forceLanguage;
    }

    public Network getNetwork() {
        return network;
    }

    public Config getProperties() {
        return this.properties;
    }

    public Object getProperty(String variable) {
        return this.getProperty(variable, null);
    }

    public Object getProperty(String variable, Object defaultValue) {
        return this.properties.exists(variable) ? this.properties.get(variable) : defaultValue;
    }

    public void setPropertyString(String variable, String value) {
        this.properties.set(variable, value);
        this.properties.save();
    }

    public String getPropertyString(String key) {
        return this.getPropertyString(key, null);
    }

    public String getPropertyString(String key, String defaultValue) {
        return this.properties.exists(key) ? this.properties.get(key).toString() : defaultValue;
    }

    public int getPropertyInt(String variable) {
        return this.getPropertyInt(variable, null);
    }

    public int getPropertyInt(String variable, Integer defaultValue) {
        return this.properties.exists(variable) ? (!this.properties.get(variable).equals("") ? Integer.parseInt(String.valueOf(this.properties.get(variable))) : defaultValue) : defaultValue;
    }

    public void setPropertyInt(String variable, int value) {
        this.properties.set(variable, value);
        this.properties.save();
    }

    public boolean getPropertyBoolean(String variable) {
        return this.getPropertyBoolean(variable, null);
    }

    public boolean getPropertyBoolean(String variable, Object defaultValue) {
        Object value = this.properties.exists(variable) ? this.properties.get(variable) : defaultValue;
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        switch (String.valueOf(value)) {
            case "on":
            case "true":
            case "1":
            case "yes":
                return true;
        }
        return false;
    }

    public void setPropertyBoolean(String variable, boolean value) {
        this.properties.set(variable, value ? "1" : "0");
        this.properties.save();
    }

    public PluginIdentifiableCommand getPluginCommand(String name) {
        Command command = this.commandMap.getCommand(name);
        if (command instanceof PluginIdentifiableCommand) {
            return (PluginIdentifiableCommand) command;
        } else {
            return null;
        }
    }

    public BanList getNameBans() {
        return this.banByName;
    }

    public BanList getIPBans() {
        return this.banByIP;
    }

    public void addOp(String name) {
        this.operators.set(name.toLowerCase(), true);
        Player player = this.getPlayerExact(name);
        if (player != null) {
            player.recalculatePermissions();
        }
        this.operators.save(true);
    }

    public void removeOp(String name) {
        this.operators.remove(name.toLowerCase());
        Player player = this.getPlayerExact(name);
        if (player != null) {
            player.recalculatePermissions();
        }
        this.operators.save();
    }

    public void addWhitelist(String name) {
        this.whitelist.set(name.toLowerCase(), true);
        this.whitelist.save(true);
    }

    public void removeWhitelist(String name) {
        this.whitelist.remove(name.toLowerCase());
        this.whitelist.save(true);
    }

    public boolean isWhitelisted(String name) {
        return !this.hasWhitelist() || this.operators.exists(name, true) || this.whitelist.exists(name, true);
    }

    public boolean isOp(String name) {
        return this.operators.exists(name, true);
    }

    public Config getWhitelist() {
        return whitelist;
    }

    public Config getOps() {
        return operators;
    }

    public void reloadWhitelist() {
        this.whitelist.reload();
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public boolean shouldSavePlayerData() {
        return shouldSavePlayerData;
    }

    public int getPlayerSkinChangeCooldown() {
        return skinChangeCooldown;
    }

    public Level getNetherWorld(String world) {
        return multiNetherWorlds.contains(world) ? this.getLevelByName(world + "-nether") : this.getLevelByName("nether");
    }

    public static Int2ObjectMap<ObjectList<Player>> shortPlayers(Player[] players) {
        Int2ObjectMap<ObjectList<Player>> targets = new Int2ObjectOpenHashMap<>();
        for (Player player : players) {
            targets.computeIfAbsent(player.protocol, i -> new ObjectArrayList<>()).add(player);
        }
        return targets;
    }

    public static Int2ObjectMap<ObjectList<Player>> shortPlayers(Collection<Player> players) {
        Int2ObjectMap<ObjectList<Player>> targets = new Int2ObjectOpenHashMap<>();
        for (Player player : players) {
            targets.computeIfAbsent(player.protocol, i -> new ObjectArrayList<>()).add(player);
        }
        return targets;
    }

    /**
     * Checks the current thread against the expected primary thread for the server.
     *
     * <b>Note:</b> this method should not be used to indicate the current synchronized state of the runtime. A current thread matching the main thread indicates that it is synchronized, but a mismatch does not preclude the same assumption.
     *
     * @return true if the current thread matches the expected primary thread, false otherwise
     */
    public boolean isPrimaryThread() {
        return (Thread.currentThread() == currentThread);
    }

    public Thread getPrimaryThread() {
        return currentThread;
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
        Entity.registerEntity("ZombieVillager", EntityZombieVillager.class);
        Entity.registerEntity("Zombie", EntityZombie.class);
        Entity.registerEntity("Pillager", EntityPillager.class);
        Entity.registerEntity("ZombieVillagerV2", EntityZombieVillagerV2.class);
        Entity.registerEntity("Hoglin", EntityHoglin.class);
        Entity.registerEntity("Piglin", EntityPiglin.class);
        Entity.registerEntity("Zoglin", EntityZoglin.class);
        Entity.registerEntity("PiglinBrute", EntityPiglinBrute.class);
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
        Entity.registerEntity("Villager", EntityVillager.class);
        Entity.registerEntity("ZombieHorse", EntityZombieHorse.class);
        Entity.registerEntity("WanderingTrader", EntityWanderingTrader.class);
        Entity.registerEntity("VillagerV2", EntityVillagerV2.class);
        Entity.registerEntity("Fox", EntityFox.class);
        Entity.registerEntity("Bee", EntityBee.class);
        Entity.registerEntity("Strider", EntityStrider.class);
        //Vehicles
        Entity.registerEntity("MinecartRideable", EntityMinecartEmpty.class);
        Entity.registerEntity("MinecartChest", EntityMinecartChest.class);
        Entity.registerEntity("MinecartHopper", EntityMinecartHopper.class);
        Entity.registerEntity("MinecartTnt", EntityMinecartTNT.class);
        Entity.registerEntity("Boat", EntityBoat.class);
        //Others
        Entity.registerEntity("Human", EntityHuman.class, true);
        Entity.registerEntity("Lightning", EntityLightning.class);
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
    }

    public boolean isNetherAllowed() {
        return this.netherEnabled;
    }

    public PlayerDataSerializer getPlayerDataSerializer() {
        return playerDataSerializer;
    }

    public void setPlayerDataSerializer(PlayerDataSerializer playerDataSerializer) {
        this.playerDataSerializer = Preconditions.checkNotNull(playerDataSerializer, "playerDataSerializer");
    }

    public static Server getInstance() {
        return instance;
    }

    /**
     * SuomiCraft PE mode tweaks some stuff to work better on SuomiCraft PE server.
     *
     * @return SuomiCraft PE mode enabled
     */
    public boolean suomiCraftPEMode() {
        return suomicraftMode;
    }

    public SpawnerTask getSpawnerTask() {
        return this.spawnerTask;
    }

    /**
     * Load some settings from server.properties
     */
    private void loadSettings() {
        this.forceLanguage = this.getPropertyBoolean("force-language", false);
        this.networkCompressionLevel = Math.max(Math.min(this.getPropertyInt("compression-level", 4), 9), 0);
        this.autoTickRate = this.getPropertyBoolean("auto-tick-rate", true);
        this.autoTickRateLimit = this.getPropertyInt("auto-tick-rate-limit", 20);
        this.alwaysTickPlayers = this.getPropertyBoolean("always-tick-players", false);
        this.baseTickRate = this.getPropertyInt("base-tick-rate", 1);
        this.suomicraftMode = this.getPropertyBoolean("suomicraft-mode", false);
        this.callDataPkEv = this.getPropertyBoolean("call-data-pk-send-event", true);
        this.callBatchPkEv = this.getPropertyBoolean("call-batch-pk-send-event", true);
        this.doLevelGC = this.getPropertyBoolean("do-level-gc", true);
        this.mobAI = this.getPropertyBoolean("mob-ai", true);
        this.netherEnabled = this.getPropertyBoolean("nether", true);
        this.endEnabled = this.getPropertyBoolean("end", false);
        this.xboxAuth = this.getPropertyBoolean("xbox-auth", true);
        this.bedSpawnpoints = this.getPropertyBoolean("bed-spawnpoints", true);
        this.achievements = this.getPropertyBoolean("achievements", true);
        this.dimensionsEnabled = this.getPropertyBoolean("dimensions", false);
        this.banAuthFailed = this.getPropertyBoolean("temp-ip-ban-failed-xbox-auth", false);
        this.pvp = this.getPropertyBoolean("pvp", true);
        this.announceAchievements = this.getPropertyBoolean("announce-player-achievements", false);
        this.spawnEggsEnabled = this.getPropertyBoolean("spawn-eggs", true);
        this.xpBottlesOnCreative = this.getPropertyBoolean("xp-bottles-on-creative", false);
        this.shouldSavePlayerData = this.getPropertyBoolean("save-player-data", true);
        this.blockListener = this.getPropertyBoolean("block-listener", true);
        this.explosionBreakBlocks = this.getPropertyBoolean("explosion-break-blocks", true);
        this.vanillaBB = this.getPropertyBoolean("vanilla-bossbars", false);
        this.stopInGame = this.getPropertyBoolean("stop-in-game", false);
        this.opInGame = this.getPropertyBoolean("op-in-game", false);
        this.lightUpdates = this.getPropertyBoolean("light-updates", false);
        this.queryPlugins = this.getPropertyBoolean("query-plugins", false);
        this.getAllowFlight = this.getPropertyBoolean("allow-flight", false);
        this.isHardcore = this.getPropertyBoolean("hardcore", false);
        this.despawnEntities = this.getPropertyBoolean("entity-despawn-task", true);
        this.forceResources = this.getPropertyBoolean("force-resources", false);
        this.whitelistEnabled = this.getPropertyBoolean("white-list", false);
        this.checkOpMovement = this.getPropertyBoolean("check-op-movement", false);
        this.forceGamemode = this.getPropertyBoolean("force-gamemode", true);
        this.doNotLimitInteractions = this.getPropertyBoolean("do-not-limit-interactions", false);
        this.motd = this.getPropertyString("motd", "Minecraft Server");
        this.viewDistance = this.getPropertyInt("view-distance", 8);
        this.despawnTicks = this.getPropertyInt("ticks-per-entity-despawns", 12000);
        this.port = this.getPropertyInt("server-port", 19132);
        this.ip = this.getPropertyString("server-ip", "0.0.0.0");
        this.skinChangeCooldown = this.getPropertyInt("skin-change-cooldown", 30);
        this.strongIPBans = this.getPropertyBoolean("strong-ip-bans", false);
        this.spawnRadius = this.getPropertyInt("spawn-protection", 10);
        this.spawnAnimals = this.getPropertyBoolean("spawn-animals", true);
        this.spawnMobs = this.getPropertyBoolean("spawn-mobs", true);
        this.autoSaveTicks = this.getPropertyInt("ticks-per-autosave", 6000);
        this.doNotLimitSkinGeometry = this.getPropertyBoolean("do-not-limit-skin-geometry", true);
        this.anvilsEnabled = this.getPropertyBoolean("anvils-enabled", true);
        this.chunksPerTick = this.getPropertyInt("chunk-sending-per-tick", 5);
        this.spawnThreshold = this.getPropertyInt("spawn-threshold", 50);
        this.savePlayerDataByUuid = this.getPropertyBoolean("save-player-data-by-uuid", true);
        this.vanillaPortals = this.getPropertyBoolean("vanilla-portals", true);
        this.personaSkins = this.getPropertyBoolean("persona-skins", true);
        this.cacheChunks = this.getPropertyBoolean("cache-chunks", false);
        this.callEntityMotionEv = this.getPropertyBoolean("call-entity-motion-event", true);
        this.c_s_spawnThreshold = (int) Math.ceil(Math.sqrt(this.spawnThreshold));
        try {
            this.gamemode = this.getPropertyInt("gamemode", 0) & 0b11;
        } catch (NumberFormatException exception) {
            this.gamemode = getGamemodeFromString(this.getPropertyString("gamemode")) & 0b11;
        }
        String list = this.getPropertyString("do-not-tick-worlds");
        if (!list.trim().isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(list, ", ");
            while (tokenizer.hasMoreTokens()) {
                noTickingWorlds.add(tokenizer.nextToken());
            }
        }
    }

    /**
     * This class contains all default server.properties values.
     */
    private static class ServerProperties extends ConfigSection {
        {
            put("motd", "Minecraft Server");
            put("sub-motd", "Powered by Nukkit");
            put("server-port", 19132);
            put("server-ip", "0.0.0.0");
            put("view-distance", 8);
            put("white-list", false);
            put("achievements", true);
            put("announce-player-achievements", false);
            put("spawn-protection", 10);
            put("max-players", 50);
            put("spawn-animals", true);
            put("spawn-mobs", true);
            put("gamemode", 0);
            put("force-gamemode", true);
            put("hardcore", false);
            put("pvp", true);
            put("difficulty", 2);
            put("generator-settings", "");
            put("level-name", "world");
            put("level-seed", "");
            put("level-type", "default");
            put("enable-query", true);
            put("enable-rcon", false);
            put("rcon.password", Base64.getEncoder().encodeToString(UUID.randomUUID().toString().replace("-", "").getBytes()).substring(3, 13));
            put("auto-save", true);
            put("force-resources", false);
            put("xbox-auth", true);
            put("bed-spawnpoints", true);
            put("explosion-break-blocks", true);
            put("stop-in-game", false);
            put("op-in-game", true);
            put("xp-bottles-on-creative", true);
            put("spawn-eggs", true);
            put("mob-ai", true);
            put("entity-auto-spawn-task", true);
            put("entity-despawn-task", true);
            put("language", "eng");
            put("force-language", false);
            put("shutdown-message", "§cServer closed");
            put("save-player-data", true);
            put("query-plugins", false);
            put("debug-level", 1);
            put("async-workers", "auto");
            put("zlib-provider", 2);
            put("compression-level", 4);
            put("auto-tick-rate", true);
            put("auto-tick-rate-limit", 20);
            put("base-tick-rate", 1);
            put("always-tick-players", false);
            put("enable-timings", false);
            put("timings-verbose", false);
            put("timings-privacy", false);
            put("timings-history-interval", 6000);
            put("timings-history-length", 72000);
            put("timings-bypass-max", false);
            put("light-updates", false);
            put("clear-chunk-tick-list", true);
            put("cache-chunks", false);
            put("spawn-threshold", 50);
            put("chunk-sending-per-tick", 5);
            put("chunk-ticking-per-tick", 40);
            put("chunk-ticking-radius", 3);
            put("chunk-generation-queue-size", 8);
            put("chunk-generation-population-queue-size", 8);
            put("ticks-per-autosave", 6000);
            put("ticks-per-entity-spawns", 200);
            put("ticks-per-entity-despawns", 12000);
            put("thread-watchdog", true);
            put("thread-watchdog-tick", 60000);
            put("nether", true);
            put("end", false);
            put("suomicraft-mode", false);
            put("do-not-tick-worlds", "");
            put("load-all-worlds", true);
            put("ansi-title", true);
            put("worlds-entity-spawning-disabled", "");
            put("block-listener", true);
            put("allow-flight", false);
            put("timeout-milliseconds", 25000);
            put("multiversion-min-protocol", 0);
            put("vanilla-bossbars", false);
            put("dimensions", false);
            put("whitelist-reason", "§cServer is white-listed");
            put("chemistry-resources-enabled", false);
            put("strong-ip-bans", false);
            put("worlds-level-auto-save-disabled", "");
            put("temp-ip-ban-failed-xbox-auth", false);
            put("call-data-pk-send-event", true);
            put("call-batch-pk-send-event", true);
            put("do-level-gc", true);
            put("skin-change-cooldown", 30);
            put("check-op-movement", false);
            put("do-not-limit-interactions", false);
            put("do-not-limit-skin-geometry", true);
            put("automatic-bug-report", true);
            put("anvils-enabled", true);
            put("save-player-data-by-uuid", true);
            put("vanilla-portals", true);
            put("persona-skins", true);
            put("multi-nether-worlds", "");
            put("call-entity-motion-event", true);
        }
    }

    private class ConsoleThread extends Thread implements InterruptibleThread {

        @Override
        public void run() {
            console.start();
        }
    }
}

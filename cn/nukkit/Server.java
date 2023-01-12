/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.IPlayer;
import cn.nukkit.NOBF;
import cn.nukkit.Nukkit;
import cn.nukkit.OfflinePlayer;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBanner;
import cn.nukkit.blockentity.BlockEntityBarrel;
import cn.nukkit.blockentity.BlockEntityBeacon;
import cn.nukkit.blockentity.BlockEntityBed;
import cn.nukkit.blockentity.BlockEntityBell;
import cn.nukkit.blockentity.BlockEntityBlastFurnace;
import cn.nukkit.blockentity.BlockEntityBrewingStand;
import cn.nukkit.blockentity.BlockEntityCampfire;
import cn.nukkit.blockentity.BlockEntityCauldron;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.blockentity.BlockEntityComparator;
import cn.nukkit.blockentity.BlockEntityDispenser;
import cn.nukkit.blockentity.BlockEntityDropper;
import cn.nukkit.blockentity.BlockEntityEnchantTable;
import cn.nukkit.blockentity.BlockEntityEnderChest;
import cn.nukkit.blockentity.BlockEntityFlowerPot;
import cn.nukkit.blockentity.BlockEntityFurnace;
import cn.nukkit.blockentity.BlockEntityHopper;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.blockentity.BlockEntityJukebox;
import cn.nukkit.blockentity.BlockEntityLectern;
import cn.nukkit.blockentity.BlockEntityMusic;
import cn.nukkit.blockentity.BlockEntityPistonArm;
import cn.nukkit.blockentity.BlockEntityShulkerBox;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.blockentity.BlockEntitySkull;
import cn.nukkit.blockentity.BlockEntitySmoker;
import cn.nukkit.blockentity.BlockEntitySpawner;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.console.NukkitConsole;
import cn.nukkit.d;
import cn.nukkit.dispenser.DispenseBehaviorRegister;
import cn.nukkit.e;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.entity.item.EntityAreaEffectCloud;
import cn.nukkit.entity.item.EntityArmorStand;
import cn.nukkit.entity.item.EntityBoat;
import cn.nukkit.entity.item.EntityChestBoat;
import cn.nukkit.entity.item.EntityEndCrystal;
import cn.nukkit.entity.item.EntityExpBottle;
import cn.nukkit.entity.item.EntityFallingBlock;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.entity.item.EntityFishingHook;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.item.EntityMinecartChest;
import cn.nukkit.entity.item.EntityMinecartEmpty;
import cn.nukkit.entity.item.EntityMinecartHopper;
import cn.nukkit.entity.item.EntityMinecartTNT;
import cn.nukkit.entity.item.EntityPainting;
import cn.nukkit.entity.item.EntityPotion;
import cn.nukkit.entity.item.EntityPotionLingering;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.entity.item.EntityXPOrb;
import cn.nukkit.entity.mob.EntityBlaze;
import cn.nukkit.entity.mob.EntityCaveSpider;
import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.entity.mob.EntityDrowned;
import cn.nukkit.entity.mob.EntityElderGuardian;
import cn.nukkit.entity.mob.EntityEnderDragon;
import cn.nukkit.entity.mob.EntityEnderman;
import cn.nukkit.entity.mob.EntityEndermite;
import cn.nukkit.entity.mob.EntityEvoker;
import cn.nukkit.entity.mob.EntityGhast;
import cn.nukkit.entity.mob.EntityGuardian;
import cn.nukkit.entity.mob.EntityHoglin;
import cn.nukkit.entity.mob.EntityHusk;
import cn.nukkit.entity.mob.EntityMagmaCube;
import cn.nukkit.entity.mob.EntityPhantom;
import cn.nukkit.entity.mob.EntityPiglin;
import cn.nukkit.entity.mob.EntityPiglinBrute;
import cn.nukkit.entity.mob.EntityPillager;
import cn.nukkit.entity.mob.EntityRavager;
import cn.nukkit.entity.mob.EntityShulker;
import cn.nukkit.entity.mob.EntitySilverfish;
import cn.nukkit.entity.mob.EntitySkeleton;
import cn.nukkit.entity.mob.EntitySlime;
import cn.nukkit.entity.mob.EntitySnowGolem;
import cn.nukkit.entity.mob.EntitySpider;
import cn.nukkit.entity.mob.EntityStray;
import cn.nukkit.entity.mob.EntityVex;
import cn.nukkit.entity.mob.EntityVindicator;
import cn.nukkit.entity.mob.EntityWarden;
import cn.nukkit.entity.mob.EntityWitch;
import cn.nukkit.entity.mob.EntityWither;
import cn.nukkit.entity.mob.EntityWitherSkeleton;
import cn.nukkit.entity.mob.EntityZoglin;
import cn.nukkit.entity.mob.EntityZombie;
import cn.nukkit.entity.mob.EntityZombiePigman;
import cn.nukkit.entity.mob.EntityZombieVillager;
import cn.nukkit.entity.mob.EntityZombieVillagerV1;
import cn.nukkit.entity.passive.EntityAllay;
import cn.nukkit.entity.passive.EntityAxolotl;
import cn.nukkit.entity.passive.EntityBat;
import cn.nukkit.entity.passive.EntityBee;
import cn.nukkit.entity.passive.EntityCat;
import cn.nukkit.entity.passive.EntityChicken;
import cn.nukkit.entity.passive.EntityCod;
import cn.nukkit.entity.passive.EntityCow;
import cn.nukkit.entity.passive.EntityDolphin;
import cn.nukkit.entity.passive.EntityDonkey;
import cn.nukkit.entity.passive.EntityFox;
import cn.nukkit.entity.passive.EntityFrog;
import cn.nukkit.entity.passive.EntityGlowSquid;
import cn.nukkit.entity.passive.EntityGoat;
import cn.nukkit.entity.passive.EntityHorse;
import cn.nukkit.entity.passive.EntityIronGolem;
import cn.nukkit.entity.passive.EntityLlama;
import cn.nukkit.entity.passive.EntityMooshroom;
import cn.nukkit.entity.passive.EntityMule;
import cn.nukkit.entity.passive.EntityOcelot;
import cn.nukkit.entity.passive.EntityPanda;
import cn.nukkit.entity.passive.EntityParrot;
import cn.nukkit.entity.passive.EntityPig;
import cn.nukkit.entity.passive.EntityPolarBear;
import cn.nukkit.entity.passive.EntityPufferfish;
import cn.nukkit.entity.passive.EntityRabbit;
import cn.nukkit.entity.passive.EntitySalmon;
import cn.nukkit.entity.passive.EntitySheep;
import cn.nukkit.entity.passive.EntitySkeletonHorse;
import cn.nukkit.entity.passive.EntitySquid;
import cn.nukkit.entity.passive.EntityStrider;
import cn.nukkit.entity.passive.EntityTadpole;
import cn.nukkit.entity.passive.EntityTropicalFish;
import cn.nukkit.entity.passive.EntityTurtle;
import cn.nukkit.entity.passive.EntityVillager;
import cn.nukkit.entity.passive.EntityVillagerV1;
import cn.nukkit.entity.passive.EntityWanderingTrader;
import cn.nukkit.entity.passive.EntityWolf;
import cn.nukkit.entity.passive.EntityZombieHorse;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityBlazeFireBall;
import cn.nukkit.entity.projectile.EntityBlueWitherSkull;
import cn.nukkit.entity.projectile.EntityEgg;
import cn.nukkit.entity.projectile.EntityEnderCharge;
import cn.nukkit.entity.projectile.EntityEnderEye;
import cn.nukkit.entity.projectile.EntityEnderPearl;
import cn.nukkit.entity.projectile.EntityEvocationFangs;
import cn.nukkit.entity.projectile.EntityGhastFireBall;
import cn.nukkit.entity.projectile.EntityLlamaSpit;
import cn.nukkit.entity.projectile.EntityShulkerBullet;
import cn.nukkit.entity.projectile.EntitySnowball;
import cn.nukkit.entity.projectile.EntityThrownTrident;
import cn.nukkit.entity.projectile.EntityWitherSkull;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.LevelInitEvent;
import cn.nukkit.event.level.LevelLoadEvent;
import cn.nukkit.event.server.BatchPacketsEvent;
import cn.nukkit.event.server.PlayerDataSerializeEvent;
import cn.nukkit.event.server.QueryRegenerateEvent;
import cn.nukkit.event.server.ServerStopEvent;
import cn.nukkit.f;
import cn.nukkit.g;
import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.Recipe;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemMap;
import cn.nukkit.item.RuntimeItems;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.lang.BaseLang;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.EnumLevel;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.LevelProviderManager;
import cn.nukkit.level.format.anvil.Anvil;
import cn.nukkit.level.generator.End;
import cn.nukkit.level.generator.Flat;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.Nether;
import cn.nukkit.level.generator.Normal;
import cn.nukkit.level.generator.Void;
import cn.nukkit.math.NukkitMath;
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
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.DefaultPlayerDataSerializer;
import cn.nukkit.utils.LevelException;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.PlayerDataSerializer;
import cn.nukkit.utils.PluginException;
import cn.nukkit.utils.ServerException;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;
import cn.nukkit.utils.Watchdog;
import cn.nukkit.utils.Zlib;
import cn.nukkit.utils.bugreport.ExceptionHandler;
import co.aikar.timings.Timings;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

public class Server {
    private static final Logger y;
    public static final String BROADCAST_CHANNEL_ADMINISTRATIVE;
    public static final String BROADCAST_CHANNEL_USERS;
    private static Server I;
    private final BanList l;
    private final BanList R;
    private final Config b;
    private final Config L;
    private final Config z;
    private final Config q;
    private final String x;
    private final String E;
    private final String ae;
    private final PluginManager c;
    private final ServerScheduler o;
    private final BaseLang aa;
    private final NukkitConsole h;
    private final e J;
    private final SimpleCommandMap v;
    private final CraftingManager X;
    private final ResourcePackManager B;
    private final ConsoleCommandSender e;
    private boolean F;
    private final AtomicBoolean g = new AtomicBoolean(true);
    private int T;
    private long H;
    private final float[] Z = new float[]{20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f};
    private final float[] ac = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private float ad = 20.0f;
    private float s = 0.0f;
    private int k;
    private int u;
    private int w;
    private boolean N = true;
    private int a;
    private int A = Integer.MAX_VALUE;
    int af;
    private boolean D;
    private String Q;
    private int S;
    private final UUID M;
    private RCON f;
    private final Network O;
    private QueryHandler p;
    private QueryRegenerateEvent ag;
    private final EntityMetadataStore j;
    private final PlayerMetadataStore d;
    private final LevelMetadataStore V;
    private final Map<InetSocketAddress, Player> G = new HashMap<InetSocketAddress, Player>();
    final Map<UUID, Player> Y = new HashMap<UUID, Player>();
    private static final Pattern n;
    private final Map<Integer, Level> t = new d(this);
    private Level[] ab = new Level[0];
    private final ServiceManager U = new NKServiceManager();
    private Level m;
    private final Thread W;
    private Watchdog K;
    private final DB ah;
    private PlayerDataSerializer i;
    private SpawnerTask ai;
    private final BatchingHelper r;
    public static final List<String> disabledSpawnWorlds;
    public static final List<String> nonAutoSaveWorlds;
    public static final List<String> multiNetherWorlds;
    public static final List<String> noTickingWorlds;
    public static final List<String> antiXrayWorlds;
    public String motd;
    public String whitelistReason;
    public boolean mobAiEnabled;
    public boolean shouldSavePlayerData;
    public boolean flyChecks;
    public boolean isHardcore;
    public boolean forceResources;
    public boolean forceGamemode;
    public boolean netherEnabled;
    public boolean doLevelGC;
    public boolean callBatchPkEv;
    public boolean whitelistEnabled;
    public boolean xboxAuth;
    public boolean spawnEggsEnabled;
    public boolean xpBottlesOnCreative;
    public boolean dimensionsEnabled;
    public boolean callDataPkSendEv;
    public boolean bedSpawnpoints;
    public boolean achievementsEnabled;
    public boolean banXBAuthFailed;
    public boolean endEnabled;
    public boolean pvpEnabled;
    public boolean announceAchievements;
    public boolean checkOpMovement;
    public boolean doNotLimitInteractions;
    public int mobDespawnTicks;
    public int chunksPerTick;
    public int spawnThreshold;
    public int networkCompressionLevel;
    public int viewDistance;
    public int gamemode;
    public int skinChangeCooldown;
    public int spawnRadius;
    public int minimumProtocol;
    public int chunkCompressionLevel;
    public int autoSaveTicks;
    public int autoTickRateLimit;
    public boolean doNotLimitSkinGeometry;
    public boolean mobsFromBlocks;
    public boolean explosionBreakBlocks;
    public boolean vanillaBossBar;
    public boolean stopInGame;
    public boolean opInGame;
    public boolean lightUpdates;
    public boolean queryPlugins;
    public boolean despawnMobs;
    public boolean strongIPBans;
    public boolean spawnAnimals;
    public boolean spawnMonsters;
    public boolean anvilsEnabled;
    public boolean savePlayerDataByUuid;
    public boolean vanillaPortals;
    public boolean personaSkins;
    public boolean cacheChunks;
    public boolean callEntityMotionEv;
    public boolean updateChecks;
    public boolean asyncChunkSending;
    public boolean logJoinLocation;
    public boolean unsafeRedstone;
    public boolean disableNewBlocks;
    public boolean attackStopSprint;
    public boolean autoTickRate;
    public boolean forceLanguage;
    public boolean alwaysTickPlayers;
    public boolean reduceTraffic;
    public boolean forceResourcesAllowOwnPacks;
    private static final byte[] C;
    private int P;

    Server(String string, String string2, String string3, boolean bl, boolean bl2) {
        Object object;
        Preconditions.checkState(I == null, "Already initialized!");
        this.W = Thread.currentThread();
        I = this;
        this.x = string;
        if (!new File(string2 + "worlds/").exists()) {
            new File(string2 + "worlds/").mkdirs();
        }
        if (!new File(string3).exists()) {
            new File(string3).mkdirs();
        }
        this.E = new File(string2).getAbsolutePath() + '/';
        this.ae = new File(string3).getAbsolutePath() + '/';
        this.i = new DefaultPlayerDataSerializer(this);
        this.h = new NukkitConsole();
        this.J = new e(this, null);
        this.J.start();
        y.info("\u00a7b-- \u00a7cNukkit \u00a7aPetteriM1 Edition \u00a7b--");
        try {
            if (Integer.parseInt(System.getProperty("java.version").split("\\.")[0]) > 11) {
                this.getLogger().warning("For the best compatibility, please use Java 8 or 11");
            }
        }
        catch (Exception exception) {
            this.getLogger().debug("Failed to check Java version", exception);
        }
        y.info("Loading server properties...");
        this.z = new Config(this.E + "server.properties", 0, (ConfigSection)new f(null));
        int n = NukkitMath.clamp(this.getPropertyInt("debug-level", 1), 1, 3);
        if (bl2 && n < 2) {
            n = 2;
        }
        Nukkit.DEBUG = n;
        this.a();
        if (n < 2) {
            ExceptionHandler.registerExceptionHandler();
        }
        if (!new File(string2 + "players/").exists() && this.shouldSavePlayerData) {
            new File(string2 + "players/").mkdirs();
        }
        Zlib.setProvider(this.getPropertyInt("zlib-provider", 2));
        this.aa = new BaseLang("eng");
        Object object2 = this.getProperty("async-workers", "auto");
        if (!(object2 instanceof Integer)) {
            try {
                object2 = Integer.valueOf((String)object2);
            }
            catch (Exception exception) {
                object2 = Math.max(Runtime.getRuntime().availableProcessors() + 1, 4);
            }
        }
        ServerScheduler.WORKERS = (Integer)object2;
        this.o = new ServerScheduler();
        this.h.setExecutingCommands(true);
        this.r = new BatchingHelper();
        if (this.getPropertyBoolean("enable-rcon", false)) {
            try {
                this.f = new RCON(this, this.getPropertyString("rcon.password", ""), !this.getIp().isEmpty() ? this.getIp() : "0.0.0.0", this.getPropertyInt("rcon.port", this.getPort()));
            }
            catch (IllegalArgumentException illegalArgumentException) {
                y.error(this.aa.translateString(illegalArgumentException.getMessage(), illegalArgumentException.getCause().getMessage()));
            }
        }
        this.q = new Config(this.E + "maps.yml", 2);
        ItemMap.mapCount = this.q.getLong("map_count_do_not_edit");
        this.j = new EntityMetadataStore();
        this.d = new PlayerMetadataStore();
        this.V = new LevelMetadataStore();
        this.b = new Config(this.E + "ops.txt", 5);
        this.L = new Config(this.E + "white-list.txt", 5);
        this.l = new BanList(this.E + "banned-players.json");
        this.l.load();
        this.R = new BanList(this.E + "banned-ips.json");
        this.R.load();
        org.apache.logging.log4j.Level level = Nukkit.getLogLevel();
        for (org.apache.logging.log4j.Level object3 : org.apache.logging.log4j.Level.values()) {
            if (object3.intLevel() != (Nukkit.DEBUG + 3) * 100 || object3.intLevel() <= level.intLevel()) continue;
            Nukkit.setLogLevel(object3);
            break;
        }
        this.e = new ConsoleCommandSender();
        this.v = new SimpleCommandMap(this);
        Server.d();
        Server.f();
        Block.init();
        Enchantment.init();
        GlobalBlockPalette.init();
        RuntimeItems.init();
        Item.init();
        EnumBiome.values();
        Effect.init();
        Potion.init();
        Attribute.init();
        DispenseBehaviorRegister.init();
        Skin.initDefaultSkin();
        try {
            this.ah = Iq80DBFactory.factory.open(new File(string2, "players"), new Options().createIfMissing(true).compressionType(CompressionType.ZLIB_RAW));
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        if (this.savePlayerDataByUuid) {
            this.b();
        }
        this.M = UUID.randomUUID();
        this.X = new CraftingManager();
        this.B = new ResourcePackManager(new File(Nukkit.DATA_PATH, "resource_packs"));
        this.c = new PluginManager(this, this.v);
        this.c.subscribeToPermission("nukkit.broadcast.admin", this.e);
        this.c.registerInterface(JavaPluginLoader.class);
        this.ag = new QueryRegenerateEvent(this, 5);
        y.info(this.aa.translateString("nukkit.server.networkStart", new String[]{this.getIp().isEmpty() ? "*" : this.getIp(), String.valueOf(this.getPort())}));
        this.O = new Network(this);
        this.O.setName(this.getMotd());
        this.O.setSubName(this.getSubMotd());
        this.O.registerInterface(new RakNetInterface(this));
        if (bl) {
            this.c.loadPlugins(this.ae);
            this.enablePlugins(PluginLoadOrder.STARTUP);
        }
        Item.initCreativeItems();
        this.X.rebuildPacket();
        LevelProviderManager.addProvider(this, Anvil.class);
        Generator.addGenerator(Flat.class, "flat", 2);
        Generator.addGenerator(Normal.class, "normal", 1);
        Generator.addGenerator(Normal.class, "default", 1);
        Generator.addGenerator(Nether.class, "nether", 3);
        Generator.addGenerator(End.class, "the_end", 4);
        Generator.addGenerator(Void.class, "void", 5);
        if (this.m == null) {
            object = this.getPropertyString("level-name", "world");
            if (object == null || ((String)object).trim().isEmpty()) {
                this.getLogger().warning("level-name cannot be null, using default");
                object = "world";
                this.setPropertyString("level-name", (String)object);
            }
            if (!this.loadLevel((String)object)) {
                long l;
                String string4 = ((StringBuilder)this.getProperty("level-seed", System.currentTimeMillis())).toString();
                try {
                    l = Long.parseLong(string4);
                }
                catch (NumberFormatException numberFormatException) {
                    l = string4.hashCode();
                }
                this.generateLevel((String)object, l == 0L ? System.currentTimeMillis() : l);
            }
            this.setDefaultLevel(this.getLevelByName((String)object));
        }
        this.z.save(true);
        if (this.m == null) {
            this.getLogger().emergency(this.aa.translateString("nukkit.level.defaultError"));
            this.forceShutdown();
            return;
        }
        object = this.m;
        this.getLogger().debug("Preparing spawn region for level " + ((Level)object).getName());
        Position position = ((Level)object).getSpawnLocation();
        ((Level)object).populateChunk(position.getChunkX(), position.getChunkZ(), true);
        if (this.getPropertyBoolean("load-all-worlds", true)) {
            try {
                for (File file : new File(new File("").getCanonicalPath() + "/worlds/").listFiles()) {
                    if (!file.isDirectory() || this.isLevelLoaded(file.getName())) continue;
                    this.loadLevel(file.getName());
                }
                EnumLevel.initLevels();
            }
            catch (Exception exception) {
                this.getLogger().error("Unable to load levels", exception);
            }
        }
        if (this.spawnAnimals || this.spawnMonsters) {
            this.ai = new SpawnerTask();
            int n2 = Math.max(this.getPropertyInt("ticks-per-entity-spawns", 200), 2) >> 1;
            this.o.scheduleDelayedRepeatingTask(this.ai, n2, n2);
        }
        if (bl) {
            this.enablePlugins(PluginLoadOrder.POSTWORLD);
        }
        if (this.getPropertyBoolean("thread-watchdog", true)) {
            this.K = new Watchdog(this, this.getPropertyInt("thread-watchdog-tick", 60000));
            this.K.start();
        }
        new NukkitMetrics(this);
        this.getLogger().debug("Checking for updates...");
        this.updateNotification(this.getConsoleSender(), false);
        this.start();
    }

    public void updateNotification(CommandSender commandSender, boolean bl) {
        CompletableFuture.runAsync(() -> {
            block11: {
                try {
                    if (!Nukkit.getBranch().equals("private")) {
                        if (commandSender instanceof ConsoleCommandSender) {
                            this.getLogger().warning("\u00a7eDevelopment build! Branch: " + Nukkit.getBranch());
                        }
                        return;
                    }
                    URLConnection uRLConnection = new URL("https://api.github.com/repos/PetteriM1/NukkitPetteriM1Edition/releases").openConnection();
                    uRLConnection.connect();
                    InputStreamReader inputStreamReader = new InputStreamReader((InputStream)uRLConnection.getContent());
                    JsonObject jsonObject = new JsonParser().parse(inputStreamReader).getAsJsonArray().get(0).getAsJsonObject();
                    String string = jsonObject.get("tag_name").getAsString();
                    inputStreamReader.close();
                    if (!Nukkit.BUILD_VERSION_NUMBER.equals(string)) {
                        String string2 = jsonObject.get("name").getAsString();
                        string2 = string2 == null || string2.isEmpty() ? "Invalid release info for build " + string : string2.replace("Nukkit PM1E ", "");
                        boolean bl2 = true;
                        try {
                            bl2 = Integer.parseInt(Nukkit.BUILD_VERSION_NUMBER) > Integer.parseInt(string);
                        }
                        catch (Exception exception) {
                            y.debug("Failed to parse build info", (Throwable)exception);
                        }
                        if (bl2) {
                            if (commandSender instanceof ConsoleCommandSender) {
                                this.getLogger().warning("\u00a7eUnreleased build! Current: " + Nukkit.BUILD_VERSION + " Latest: " + string2);
                            }
                            return;
                        }
                        commandSender.sendMessage("\u00a7c[Update] \u00a7eThere is a new version of Nukkit PetteriM1 Edition available! Current: " + Nukkit.BUILD_VERSION + " Latest: " + string2);
                        if (commandSender instanceof ConsoleCommandSender) {
                            this.getLogger().info("\u00a7c[Update] \u00a7eYou can download the latest version from https://github.com/PetteriM1/NukkitPetteriM1Edition/releases");
                        }
                        break block11;
                    }
                    if (bl) {
                        commandSender.sendMessage("\u00a7aYou are running the latest version.");
                    }
                }
                catch (Exception exception) {
                    y.debug("Update check failed", (Throwable)exception);
                }
            }
        });
    }

    public int broadcastMessage(String string) {
        return this.broadcast(string, "nukkit.broadcast.user");
    }

    public int broadcastMessage(TextContainer textContainer) {
        return this.broadcast(textContainer, "nukkit.broadcast.user");
    }

    public int broadcastMessage(String string, CommandSender[] commandSenderArray) {
        for (CommandSender commandSender : commandSenderArray) {
            commandSender.sendMessage(string);
        }
        return commandSenderArray.length;
    }

    public int broadcastMessage(String string, Collection<? extends CommandSender> collection) {
        for (CommandSender commandSender : collection) {
            commandSender.sendMessage(string);
        }
        return collection.size();
    }

    public int broadcastMessage(TextContainer textContainer, Collection<? extends CommandSender> collection) {
        for (CommandSender commandSender : collection) {
            commandSender.sendMessage(textContainer);
        }
        return collection.size();
    }

    public int broadcast(String string, String string2) {
        HashSet<CommandSender> hashSet = new HashSet<CommandSender>();
        for (String string3 : string2.split(";")) {
            for (Permissible permissible : this.c.getPermissionSubscriptions(string3)) {
                if (!(permissible instanceof CommandSender) || !permissible.hasPermission(string3)) continue;
                hashSet.add((CommandSender)permissible);
            }
        }
        for (CommandSender commandSender : hashSet) {
            commandSender.sendMessage(string);
        }
        return hashSet.size();
    }

    public int broadcast(TextContainer textContainer, String string) {
        HashSet<CommandSender> hashSet = new HashSet<CommandSender>();
        for (String string2 : string.split(";")) {
            for (Permissible permissible : this.c.getPermissionSubscriptions(string2)) {
                if (!(permissible instanceof CommandSender) || !permissible.hasPermission(string2)) continue;
                hashSet.add((CommandSender)permissible);
            }
        }
        for (CommandSender commandSender : hashSet) {
            commandSender.sendMessage(textContainer);
        }
        return hashSet.size();
    }

    public static void broadcastPacket(Collection<Player> collection, DataPacket dataPacket) {
        for (Player player : collection) {
            player.dataPacket(dataPacket);
        }
    }

    public static void broadcastPacket(Player[] playerArray, DataPacket dataPacket) {
        for (Player player : playerArray) {
            player.dataPacket(dataPacket);
        }
    }

    public static void broadcastPackets(Player[] playerArray, DataPacket[] dataPacketArray) {
        for (DataPacket dataPacket : dataPacketArray) {
            for (Player player : playerArray) {
                player.dataPacket(dataPacket);
            }
        }
    }

    public void batchPackets(Player[] playerArray, DataPacket[] dataPacketArray) {
        this.batchPackets(playerArray, dataPacketArray, false);
    }

    public void batchPackets(Player[] playerArray, DataPacket[] dataPacketArray, boolean bl) {
        if (playerArray == null || dataPacketArray == null || playerArray.length == 0 || dataPacketArray.length == 0) {
            return;
        }
        if (this.callBatchPkEv) {
            BatchPacketsEvent batchPacketsEvent = new BatchPacketsEvent(playerArray, dataPacketArray, bl);
            this.c.callEvent(batchPacketsEvent);
            if (batchPacketsEvent.isCancelled()) {
                return;
            }
        }
        this.r.batchPackets(playerArray, dataPacketArray);
    }

    public void enablePlugins(PluginLoadOrder pluginLoadOrder) {
        block1: {
            for (Plugin plugin : new ArrayList<Plugin>(this.c.getPlugins().values())) {
                if (plugin.isEnabled() || pluginLoadOrder != plugin.getDescription().getOrder()) continue;
                this.enablePlugin(plugin);
            }
            if (pluginLoadOrder != PluginLoadOrder.POSTWORLD) break block1;
            DefaultPermissions.registerCorePermissions();
        }
    }

    public void enablePlugin(Plugin plugin) {
        this.c.enablePlugin(plugin);
    }

    public void disablePlugins() {
        this.c.disablePlugins();
    }

    public boolean dispatchCommand(CommandSender commandSender, String string) throws ServerException {
        if (!this.isPrimaryThread() && !this.suomiCraftPEMode()) {
            this.getLogger().warning("Command dispatched asynchronously: " + string);
        }
        if (commandSender == null) {
            throw new ServerException("CommandSender is not valid");
        }
        if (this.v.dispatch(commandSender, string)) {
            return true;
        }
        commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.unknown", string));
        return false;
    }

    public ConsoleCommandSender getConsoleSender() {
        return this.e;
    }

    public void reload() {
        y.info("Saving levels...");
        for (Level level : this.ab) {
            level.save();
        }
        this.c.clearPlugins();
        this.v.clearCommands();
        y.info("Reloading server properties...");
        this.z.reload();
        this.a();
        this.R.load();
        this.l.load();
        this.reloadWhitelist();
        this.b.reload();
        for (BanEntry banEntry : this.R.getEntires().values()) {
            try {
                this.O.blockAddress(InetAddress.getByName(banEntry.getName()));
            }
            catch (UnknownHostException unknownHostException) {}
        }
        y.info("Reloading plugins...");
        this.c.registerInterface(JavaPluginLoader.class);
        this.c.loadPlugins(this.ae);
        this.enablePlugins(PluginLoadOrder.STARTUP);
        this.enablePlugins(PluginLoadOrder.POSTWORLD);
        Timings.reset();
    }

    public void shutdown() {
        this.g.compareAndSet(true, false);
    }

    public void forceShutdown() {
        this.forceShutdown(this.getPropertyString("shutdown-message", "Server closed").replace("\u00a7n", "\n"));
    }

    public void forceShutdown(String string) {
        if (this.F) {
            return;
        }
        try {
            this.g.compareAndSet(true, false);
            this.F = true;
            ServerStopEvent serverStopEvent = new ServerStopEvent();
            this.c.callEvent(serverStopEvent);
            if (this.f != null) {
                this.getLogger().debug("Closing RCON...");
                this.f.close();
            }
            this.getLogger().debug("Disconnecting all players...");
            for (Player player : new ArrayList<Player>(this.G.values())) {
                player.close(player.getLeaveMessage(), string);
            }
            this.getLogger().debug("Disabling all plugins...");
            this.disablePlugins();
            this.getLogger().debug("Unloading all levels...");
            for (Level level : this.ab) {
                this.unloadLevel(level, true);
                this.H = System.currentTimeMillis();
            }
            this.getLogger().debug("Removing event handlers...");
            HandlerList.unregisterAll();
            this.getLogger().debug("Stopping all tasks...");
            this.o.cancelAllTasks();
            this.o.mainThreadHeartbeat(Integer.MAX_VALUE);
            this.getLogger().debug("Saving map count...");
            this.q.set("map_count_do_not_edit", ItemMap.mapCount);
            this.q.save(false);
            this.getLogger().debug("Closing console...");
            this.J.interrupt();
            this.getLogger().debug("Closing BatchingHelper...");
            this.r.shutdown();
            this.getLogger().debug("Stopping network interfaces...");
            for (SourceInterface sourceInterface : this.O.getInterfaces()) {
                sourceInterface.shutdown();
                this.O.unregisterInterface(sourceInterface);
            }
            if (this.ah != null) {
                this.getLogger().debug("Closing name lookup DB...");
                this.ah.close();
            }
            this.getLogger().debug("Disabling timings...");
            Timings.stopServer();
            this.getLogger().debug("Stopping Watchdog...");
            if (this.K != null) {
                this.K.kill();
            }
        }
        catch (Exception exception) {
            y.fatal("Exception happened while shutting down, exiting the process", (Throwable)exception);
            System.exit(1);
        }
    }

    public void start() {
        if (this.getPropertyBoolean("enable-query")) {
            this.p = new QueryHandler();
        }
        for (BanEntry banEntry : this.R.getEntires().values()) {
            try {
                this.O.blockAddress(InetAddress.getByName(banEntry.getName()));
            }
            catch (UnknownHostException unknownHostException) {}
        }
        this.T = 0;
        y.info(this.aa.translateString("nukkit.server.startFinished", String.valueOf((double)(System.currentTimeMillis() - Nukkit.START_TIME) / 1000.0)));
        this.tickProcessor();
        this.forceShutdown();
    }

    public void handlePacket(InetSocketAddress inetSocketAddress, ByteBuf byteBuf) {
        try {
            if (this.p == null || !byteBuf.isReadable(3)) {
                return;
            }
            byte[] byArray = new byte[2];
            byteBuf.readBytes(byArray);
            if (Arrays.equals(byArray, C)) {
                this.p.handle(inetSocketAddress, byteBuf);
            }
        }
        catch (Exception exception) {
            y.error("Error whilst handling packet", (Throwable)exception);
            this.O.blockAddress(inetSocketAddress.getAddress(), 300);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void tickProcessor() {
        this.H = System.currentTimeMillis();
        try {
            while (this.g.get()) {
                try {
                    this.e();
                    long l = this.H;
                    long l2 = System.currentTimeMillis();
                    if (!((double)l - 0.1 > (double)l2)) continue;
                    long l3 = l - l2 - 1L;
                    if (this.doLevelGC) {
                        int n = 0;
                        for (int k = 0; k < this.ab.length; ++k) {
                            n = (k + this.P) % this.ab.length;
                            this.ab[n].doGarbageCollection(l3 - 1L);
                            l3 = l - System.currentTimeMillis();
                            if (l3 <= 0L) break;
                        }
                        this.P = n + 1;
                    }
                    if (l3 <= 0L && this.doLevelGC) continue;
                    try {
                        Thread.sleep(l3, 900000);
                    }
                    catch (Exception exception) {
                        this.getLogger().logException(exception);
                    }
                }
                catch (RuntimeException runtimeException) {
                    this.getLogger().logException(runtimeException);
                }
            }
            return;
        }
        catch (Throwable throwable) {
            y.fatal("Exception happened while ticking server", throwable);
            y.fatal(Utils.getAllThreadDumps());
        }
    }

    public void onPlayerCompleteLoginSequence(Player player) {
        this.Y.put(player.getUniqueId(), player);
        this.updatePlayerListData(player.getUniqueId(), player.getId(), player.getDisplayName(), player.getSkin(), player.getLoginChainData().getXUID());
    }

    public void addPlayer(InetSocketAddress inetSocketAddress, Player player) {
        this.G.put(inetSocketAddress, player);
    }

    public void addOnlinePlayer(Player player) {
        this.Y.put(player.getUniqueId(), player);
        this.updatePlayerListData(player.getUniqueId(), player.getId(), player.getDisplayName(), player.getSkin(), player.getLoginChainData().getXUID());
    }

    public void removeOnlinePlayer(Player player) {
        if (this.Y.containsKey(player.getUniqueId())) {
            this.Y.remove(player.getUniqueId());
            PlayerListPacket playerListPacket = new PlayerListPacket();
            playerListPacket.type = 1;
            playerListPacket.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(player.getUniqueId())};
            Server.broadcastPacket(this.Y.values(), (DataPacket)playerListPacket);
        }
    }

    public void updatePlayerListData(UUID uUID, long l, String string, Skin skin) {
        this.updatePlayerListData(uUID, l, string, skin, "", this.Y.values());
    }

    public void updatePlayerListData(UUID uUID, long l, String string, Skin skin, String string2) {
        this.updatePlayerListData(uUID, l, string, skin, string2, this.Y.values());
    }

    public void updatePlayerListData(UUID uUID, long l, String string, Skin skin, Player[] playerArray) {
        this.updatePlayerListData(uUID, l, string, skin, "", playerArray);
    }

    public void updatePlayerListData(UUID uUID, long l, String string, Skin skin, String string2, Player[] playerArray) {
        PlayerListPacket playerListPacket = new PlayerListPacket();
        playerListPacket.type = 0;
        playerListPacket.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uUID, l, string, skin, string2)};
        this.batchPackets(playerArray, new DataPacket[]{playerListPacket}, true);
    }

    public void updatePlayerListData(UUID uUID, long l, String string, Skin skin, String string2, Collection<Player> collection) {
        this.updatePlayerListData(uUID, l, string, skin, string2, collection.toArray(new Player[0]));
    }

    public void removePlayerListData(UUID uUID) {
        this.removePlayerListData(uUID, this.Y.values());
    }

    public void removePlayerListData(UUID uUID, Player[] playerArray) {
        PlayerListPacket playerListPacket = new PlayerListPacket();
        playerListPacket.type = 1;
        playerListPacket.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uUID)};
        for (Player player : playerArray) {
            player.dataPacket(playerListPacket);
        }
    }

    public void removePlayerListData(UUID uUID, Collection<Player> collection) {
        this.removePlayerListData(uUID, collection.toArray(new Player[0]));
    }

    public void removePlayerListData(UUID uUID, Player player) {
        PlayerListPacket playerListPacket = new PlayerListPacket();
        playerListPacket.type = 1;
        playerListPacket.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uUID)};
        player.dataPacket(playerListPacket);
    }

    public void sendFullPlayerListData(Player player2) {
        PlayerListPacket playerListPacket = new PlayerListPacket();
        playerListPacket.type = 0;
        playerListPacket.entries = (PlayerListPacket.Entry[])this.Y.values().stream().map(player -> new PlayerListPacket.Entry(player.getUniqueId(), player.getId(), player.getDisplayName(), player.getSkin(), player.getLoginChainData().getXUID())).toArray(PlayerListPacket.Entry[]::new);
        player2.dataPacket(playerListPacket);
    }

    public void sendRecipeList(Player player) {
        if (player.protocol >= 560) {
            player.dataPacket(CraftingManager.packet560);
        } else if (player.protocol >= 553) {
            player.dataPacket(CraftingManager.packet554);
        } else if (player.protocol >= 544) {
            player.dataPacket(CraftingManager.packet544);
        } else if (player.protocol >= 524) {
            player.dataPacket(CraftingManager.packet527);
        } else if (player.protocol >= 503) {
            player.dataPacket(CraftingManager.packet503);
        } else if (player.protocol >= 485) {
            player.dataPacket(CraftingManager.packet486);
        } else if (player.protocol >= 471) {
            player.dataPacket(CraftingManager.packet471);
        } else if (player.protocol >= 465) {
            player.dataPacket(CraftingManager.packet465);
        } else if (player.protocol >= 448) {
            player.dataPacket(CraftingManager.packet448);
        } else if (player.protocol >= 440) {
            player.dataPacket(CraftingManager.packet440);
        } else if (player.protocol >= 431) {
            player.dataPacket(CraftingManager.packet431);
        } else if (player.protocol >= 419) {
            player.dataPacket(CraftingManager.packet419);
        } else if (player.protocol >= 407) {
            player.dataPacket(CraftingManager.packet407);
        } else if (player.protocol > 361) {
            player.dataPacket(CraftingManager.packet388);
        } else if (player.protocol == 361) {
            player.dataPacket(CraftingManager.packet361);
        } else if (player.protocol == 354) {
            player.dataPacket(CraftingManager.packet354);
        } else if (player.protocol == 340) {
            player.dataPacket(CraftingManager.packet340);
        } else if (player.protocol == 332 || player.protocol == 313 || player.protocol == 291) {
            player.dataPacket(CraftingManager.packet313);
        } else {
            y.debug("No recipe list available for protocol " + player.protocol);
        }
    }

    private void a(int n) {
        if (this.alwaysTickPlayers) {
            for (Player player : new ArrayList<Player>(this.G.values())) {
                player.onUpdate(n);
            }
        }
        for (Player player : this.getOnlinePlayers().values()) {
            player.c();
        }
        for (Level level : this.ab) {
            if (level.getTickRate() > this.k && --level.tickRateCounter > 0) continue;
            try {
                int n2;
                long l = System.currentTimeMillis();
                level.doTick(n);
                level.tickRateTime = n2 = (int)(System.currentTimeMillis() - l);
                if (!this.autoTickRate) continue;
                if (n2 < 50 && level.getTickRate() > this.k) {
                    int n3 = level.getTickRate() - 1;
                    level.setTickRate(n3);
                    if (n3 > this.k) {
                        level.tickRateCounter = level.getTickRate();
                    }
                    this.getLogger().debug("Raising level \"" + level.getName() + "\" tick rate to " + level.getTickRate() + " ticks");
                    continue;
                }
                if (n2 < 50) continue;
                if (level.getTickRate() == this.k) {
                    level.setTickRate(Math.max(this.k + 1, Math.min(this.autoTickRateLimit, n2 / 50)));
                    this.getLogger().debug("Level \"" + level.getName() + "\" took " + n2 + "ms, setting tick rate to " + level.getTickRate() + " ticks");
                } else if (n2 / level.getTickRate() >= 50 && level.getTickRate() < this.autoTickRateLimit) {
                    level.setTickRate(level.getTickRate() + 1);
                    this.getLogger().debug("Level \"" + level.getName() + "\" took " + n2 + "ms, setting tick rate to " + level.getTickRate() + " ticks");
                }
                level.tickRateCounter = level.getTickRate();
            }
            catch (Exception exception) {
                y.error(this.aa.translateString("nukkit.level.tickError", new String[]{level.getFolderName(), Utils.getExceptionMessage(exception)}));
            }
        }
    }

    public void doAutoSave() {
        block5: {
            if (!this.N) break block5;
            y.debug("Running auto save...");
            if (Timings.levelSaveTimer != null) {
                Timings.levelSaveTimer.startTiming();
            }
            for (Player player : new ArrayList<Player>(this.G.values())) {
                if (player.isOnline()) {
                    player.save(true);
                    continue;
                }
                if (player.isConnected()) continue;
                this.removePlayer(player);
            }
            for (Level level : this.ab) {
                if (nonAutoSaveWorlds.contains(level.getName())) continue;
                level.save();
            }
            this.q.set("map_count_do_not_edit", ItemMap.mapCount);
            this.q.save(true);
            if (Timings.levelSaveTimer != null) {
                Timings.levelSaveTimer.stopTiming();
            }
        }
    }

    private void e() {
        long l = System.currentTimeMillis();
        long l2 = l - this.H;
        if (l2 < -25L) {
            try {
                Thread.sleep(Math.max(5L, -l2 - 25L));
            }
            catch (InterruptedException interruptedException) {
                this.getLogger().logException(interruptedException);
            }
        }
        long l3 = System.nanoTime();
        if (l - this.H < -25L) {
            return;
        }
        if (Timings.isTimingsEnabled()) {
            Timings.fullServerTickTimer.startTiming();
        }
        ++this.T;
        if (Timings.connectionTimer != null) {
            Timings.connectionTimer.startTiming();
        }
        this.O.processInterfaces();
        if (this.f != null) {
            this.f.check();
        }
        if (Timings.connectionTimer != null) {
            Timings.connectionTimer.stopTiming();
        }
        if (Timings.schedulerTimer != null) {
            Timings.schedulerTimer.startTiming();
        }
        this.o.mainThreadHeartbeat(this.T);
        if (Timings.schedulerTimer != null) {
            Timings.schedulerTimer.stopTiming();
        }
        this.a(this.T);
        for (Player player : new ArrayList<Player>(this.G.values())) {
            player.checkNetwork();
        }
        if ((this.T & 0xF) == 0) {
            this.c();
            this.ad = 20.0f;
            this.s = 0.0f;
            if ((this.T & 0x1FF) == 0) {
                try {
                    this.ag = new QueryRegenerateEvent(this, 5);
                    this.c.callEvent(this.ag);
                    if (this.p != null) {
                        this.p.regenerateInfo();
                    }
                }
                catch (Exception exception) {
                    y.error(exception);
                }
            }
            this.O.updateName();
        }
        if (++this.u >= this.autoSaveTicks) {
            this.u = 0;
            this.doAutoSave();
        }
        if (this.T % 100 == 0) {
            for (Level level : this.ab) {
                level.doChunkGarbageCollection();
            }
        }
        if (Timings.isTimingsEnabled()) {
            Timings.fullServerTickTimer.stopTiming();
        }
        long l4 = System.nanoTime();
        float f2 = (float)Math.min(20.0, 1.0E9 / Math.max(1000000.0, (double)l4 - (double)l3));
        float f3 = (float)Math.min(1.0, (double)(l4 - l3) / 5.0E7);
        if (this.ad > f2) {
            this.ad = f2;
        }
        if (this.s < f3) {
            this.s = f3;
        }
        System.arraycopy(this.Z, 1, this.Z, 0, this.Z.length - 1);
        this.Z[this.Z.length - 1] = f2;
        System.arraycopy(this.ac, 1, this.ac, 0, this.ac.length - 1);
        this.ac[this.ac.length - 1] = f3;
        this.H = this.H - l < -1000L ? l : (this.H += 50L);
    }

    public long getNextTick() {
        return this.H;
    }

    private void c() {
        if (!Nukkit.TITLE) {
            return;
        }
        Runtime runtime = Runtime.getRuntime();
        double d2 = NukkitMath.round((double)(runtime.totalMemory() - runtime.freeMemory()) / 1024.0 / 1024.0, 2);
        double d3 = NukkitMath.round((double)runtime.maxMemory() / 1024.0 / 1024.0, 2);
        System.out.print("\u001b]0;Nukkit PetteriM1 Edition " + Nukkit.BUILD_VERSION + " | Online " + this.Y.size() + '/' + this.w + " | Memory " + Math.round(d2 / d3 * 100.0) + '%' + " | TPS " + this.getTicksPerSecond() + " | Load " + this.getTickUsage() + '%' + '\u0007');
    }

    public QueryRegenerateEvent getQueryInformation() {
        return this.ag;
    }

    public String getName() {
        return "Nukkit PetteriM1 Edition";
    }

    public boolean isRunning() {
        return this.g.get();
    }

    public String getNukkitVersion() {
        return Nukkit.VERSION;
    }

    public String getCodename() {
        return "PM1E";
    }

    public String getVersion() {
        return "v1.19.50";
    }

    public String getApiVersion() {
        return "9.9.9.PM1E-" + Nukkit.BUILD_VERSION_NUMBER;
    }

    public String getFilePath() {
        return this.x;
    }

    public String getDataPath() {
        return this.E;
    }

    public String getPluginPath() {
        return this.ae;
    }

    public int getMaxPlayers() {
        return this.w;
    }

    public void setMaxPlayers(int n) {
        this.w = n;
    }

    public int getPort() {
        return this.S;
    }

    public int getViewDistance() {
        return this.viewDistance;
    }

    public String getIp() {
        return this.Q;
    }

    public UUID getServerUniqueId() {
        return this.M;
    }

    public boolean getAutoSave() {
        return this.N;
    }

    public void setAutoSave(boolean bl) {
        this.N = bl;
        for (Level level : this.ab) {
            level.setAutoSave(this.N);
        }
    }

    public String getLevelType() {
        return this.getPropertyString("level-type", "default");
    }

    public int getGamemode() {
        return this.gamemode;
    }

    public boolean getForceGamemode() {
        return this.forceGamemode;
    }

    public static String getGamemodeString(int n) {
        return Server.getGamemodeString(n, false);
    }

    public static String getGamemodeString(int n, boolean bl) {
        switch (n) {
            case 0: {
                return bl ? "Survival" : "%gameMode.survival";
            }
            case 1: {
                return bl ? "Creative" : "%gameMode.creative";
            }
            case 2: {
                return bl ? "Adventure" : "%gameMode.adventure";
            }
            case 3: {
                return bl ? "Spectator" : "%gameMode.spectator";
            }
        }
        return "UNKNOWN";
    }

    public static int getGamemodeFromString(String string) {
        switch (string.trim().toLowerCase()) {
            case "0": 
            case "survival": 
            case "s": {
                return 0;
            }
            case "1": 
            case "creative": 
            case "c": {
                return 1;
            }
            case "2": 
            case "adventure": 
            case "a": {
                return 2;
            }
            case "3": 
            case "spectator": 
            case "spc": 
            case "view": 
            case "v": {
                return 3;
            }
        }
        return -1;
    }

    public static int getDifficultyFromString(String string) {
        switch (string.trim().toLowerCase()) {
            case "0": 
            case "peaceful": 
            case "p": {
                return 0;
            }
            case "1": 
            case "easy": 
            case "e": {
                return 1;
            }
            case "2": 
            case "normal": 
            case "n": {
                return 2;
            }
            case "3": 
            case "hard": 
            case "h": {
                return 3;
            }
        }
        return -1;
    }

    public int getDifficulty() {
        return this.a;
    }

    public void setDifficulty(int n) {
        int n2 = n;
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 > 3) {
            n2 = 3;
        }
        this.a = n2;
        this.setPropertyInt("difficulty", n2);
    }

    public boolean hasWhitelist() {
        return this.whitelistEnabled;
    }

    public int getSpawnRadius() {
        return this.spawnRadius;
    }

    public boolean getAllowFlight() {
        return this.flyChecks;
    }

    public boolean isHardcore() {
        return this.isHardcore;
    }

    public int getDefaultGamemode() {
        block0: {
            if (this.A != Integer.MAX_VALUE) break block0;
            this.A = this.getGamemode();
        }
        return this.A;
    }

    public String getMotd() {
        return this.motd;
    }

    public String getSubMotd() {
        String string = this.getPropertyString("sub-motd", "Powered by Nukkit PM1E");
        if (string.isEmpty()) {
            string = "Powered by Nukkit PM1E";
        }
        return string;
    }

    public boolean getForceResources() {
        return this.forceResources;
    }

    public boolean getMobAiEnabled() {
        return this.mobAiEnabled;
    }

    public MainLogger getLogger() {
        return MainLogger.getLogger();
    }

    public EntityMetadataStore getEntityMetadata() {
        return this.j;
    }

    public PlayerMetadataStore getPlayerMetadata() {
        return this.d;
    }

    public LevelMetadataStore getLevelMetadata() {
        return this.V;
    }

    public PluginManager getPluginManager() {
        return this.c;
    }

    public CraftingManager getCraftingManager() {
        return this.X;
    }

    public ResourcePackManager getResourcePackManager() {
        return this.B;
    }

    public ServerScheduler getScheduler() {
        return this.o;
    }

    public int getTick() {
        return this.T;
    }

    public float getTicksPerSecond() {
        return (float)Math.round(this.ad * 100.0f) / 100.0f;
    }

    public float getTicksPerSecondAverage() {
        float f2 = 0.0f;
        int n = this.Z.length;
        for (float f3 : this.Z) {
            f2 += f3;
        }
        return (float)NukkitMath.round(f2 / (float)n, 2);
    }

    public float getTickUsage() {
        return (float)NukkitMath.round(this.s * 100.0f, 2);
    }

    public float getTickUsageAverage() {
        float f2 = 0.0f;
        for (float f3 : this.ac) {
            f2 += f3;
        }
        return (float)Math.round(f2 / (float)this.ac.length * 100.0f) / 100.0f;
    }

    public SimpleCommandMap getCommandMap() {
        return this.v;
    }

    public Map<UUID, Player> getOnlinePlayers() {
        return ImmutableMap.copyOf(this.Y);
    }

    public int getOnlinePlayersCount() {
        return this.Y.size();
    }

    public void addRecipe(Recipe recipe) {
        this.X.registerRecipe(recipe);
    }

    public Optional<Player> getPlayer(UUID uUID) {
        Preconditions.checkNotNull(uUID, "uuid");
        return Optional.ofNullable(this.Y.get(uUID));
    }

    public Optional<UUID> lookupName(String string) {
        byte[] byArray = string.toLowerCase().getBytes(StandardCharsets.UTF_8);
        byte[] byArray2 = this.ah.get(byArray);
        if (byArray2 == null) {
            return Optional.empty();
        }
        if (byArray2.length != 16) {
            y.warn("Invalid uuid in name lookup database detected! Removing...");
            this.ah.delete(byArray);
            return Optional.empty();
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(byArray2);
        return Optional.of(new UUID(byteBuffer.getLong(), byteBuffer.getLong()));
    }

    @NOBF
    void updateName(UUID uUID, String string) {
        byte[] byArray = string.toLowerCase().getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.putLong(uUID.getMostSignificantBits());
        byteBuffer.putLong(uUID.getLeastSignificantBits());
        this.ah.put(byArray, byteBuffer.array());
    }

    public IPlayer getOfflinePlayer(String string) {
        Player player = this.getPlayerExact(string);
        if (player != null) {
            return player;
        }
        return this.lookupName(string).map(uUID -> new OfflinePlayer(this, (UUID)uUID, string)).orElse(new OfflinePlayer(this, string));
    }

    public IPlayer getOfflinePlayer(UUID uUID) {
        Preconditions.checkNotNull(uUID, "uuid");
        Optional<Player> optional = this.getPlayer(uUID);
        if (optional.isPresent()) {
            return optional.get();
        }
        return new OfflinePlayer(this, uUID);
    }

    public CompoundTag getOfflinePlayerData(UUID uUID) {
        return this.getOfflinePlayerData(uUID, false);
    }

    public CompoundTag getOfflinePlayerData(UUID uUID, boolean bl) {
        return this.a(uUID.toString(), true, bl);
    }

    public CompoundTag getOfflinePlayerData(String string) {
        return this.getOfflinePlayerData(string, false);
    }

    public CompoundTag getOfflinePlayerData(String string, boolean bl) {
        if (this.savePlayerDataByUuid) {
            Optional<UUID> optional = this.lookupName(string);
            return this.a(optional.map(UUID::toString).orElse(string), true, bl);
        }
        return this.a(string, true, bl);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private CompoundTag a(String string, boolean bl, boolean bl2) {
        Preconditions.checkNotNull(string, "name");
        PlayerDataSerializeEvent playerDataSerializeEvent = new PlayerDataSerializeEvent(string, this.i);
        if (bl) {
            this.c.callEvent(playerDataSerializeEvent);
        }
        Optional<Object> optional = Optional.empty();
        try {
            optional = playerDataSerializeEvent.getSerializer().read(string, playerDataSerializeEvent.getUuid().orElse(null));
            if (optional.isPresent()) {
                CompoundTag compoundTag = NBTIO.readCompressed((InputStream)optional.get());
                return compoundTag;
            }
        }
        catch (IOException iOException) {
            y.warn(this.getLanguage().translateString("nukkit.data.playerCorrupted", string));
            y.throwing(iOException);
        }
        finally {
            if (optional.isPresent()) {
                try {
                    ((InputStream)optional.get()).close();
                }
                catch (IOException iOException) {
                    y.throwing(iOException);
                }
            }
        }
        CompoundTag compoundTag = null;
        if (bl2) {
            Position position = this.getDefaultLevel().getSafeSpawn();
            long l = System.currentTimeMillis();
            compoundTag = new CompoundTag().putLong("firstPlayed", l / 1000L).putLong("lastPlayed", l / 1000L).putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("0", position.x)).add(new DoubleTag("1", position.y)).add(new DoubleTag("2", position.z))).putString("Level", this.getDefaultLevel().getName()).putList(new ListTag("Inventory")).putCompound("Achievements", new CompoundTag()).putInt("playerGameType", this.getGamemode()).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("0", 0.0)).add(new DoubleTag("1", 0.0)).add(new DoubleTag("2", 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("0", 0.0f)).add(new FloatTag("1", 0.0f))).putFloat("FallDistance", 0.0f).putShort("Fire", 0).putShort("Air", 400).putBoolean("OnGround", true).putBoolean("Invulnerable", false);
            this.a(string, compoundTag, true, bl);
        }
        return compoundTag;
    }

    public void saveOfflinePlayerData(UUID uUID, CompoundTag compoundTag) {
        this.saveOfflinePlayerData(uUID, compoundTag, false);
    }

    public void saveOfflinePlayerData(String string, CompoundTag compoundTag) {
        this.saveOfflinePlayerData(string, compoundTag, false);
    }

    public void saveOfflinePlayerData(UUID uUID, CompoundTag compoundTag, boolean bl) {
        this.saveOfflinePlayerData(uUID.toString(), compoundTag, bl);
    }

    public void saveOfflinePlayerData(String string, CompoundTag compoundTag, boolean bl) {
        if (this.savePlayerDataByUuid) {
            Optional<UUID> optional = this.lookupName(string);
            this.a(optional.map(UUID::toString).orElse(string), compoundTag, bl, true);
        } else {
            this.a(string, compoundTag, bl, true);
        }
    }

    private void a(String string, CompoundTag compoundTag, boolean bl, boolean bl2) {
        String string2 = string.toLowerCase();
        if (this.shouldSavePlayerData()) {
            PlayerDataSerializeEvent playerDataSerializeEvent = new PlayerDataSerializeEvent(string2, this.i);
            if (bl2) {
                this.c.callEvent(playerDataSerializeEvent);
            }
            if (bl) {
                this.getScheduler().scheduleTask(new g(this, playerDataSerializeEvent, compoundTag, string2), true);
            } else {
                this.a(playerDataSerializeEvent.getSerializer(), compoundTag, string2, playerDataSerializeEvent.getUuid().orElse(null));
            }
        }
    }

    private void a(PlayerDataSerializer playerDataSerializer, CompoundTag compoundTag, String string, UUID uUID) {
        try (OutputStream outputStream = playerDataSerializer.write(string, uUID);){
            NBTIO.writeGZIPCompressed(compoundTag, outputStream, ByteOrder.BIG_ENDIAN);
        }
        catch (Exception exception) {
            y.error(this.getLanguage().translateString("nukkit.data.saveError", string, exception));
        }
    }

    private void b() {
        File file2 = new File(this.getDataPath(), "players/");
        File[] fileArray = file2.listFiles(file -> {
            String string = file.getName();
            return !n.matcher(string).matches() && string.endsWith(".dat");
        });
        if (fileArray == null) {
            return;
        }
        for (File file3 : fileArray) {
            String string = file3.getName();
            string = string.substring(0, string.length() - 4);
            y.debug("Attempting legacy player data conversion for {}", (Object)string);
            CompoundTag compoundTag = this.a(string, false, false);
            if (compoundTag == null || !compoundTag.contains("UUIDLeast") || !compoundTag.contains("UUIDMost")) continue;
            UUID uUID = new UUID(compoundTag.getLong("UUIDMost"), compoundTag.getLong("UUIDLeast"));
            if (!compoundTag.contains("NameTag")) {
                compoundTag.putString("NameTag", string);
            }
            if (new File(this.getDataPath() + "players/" + uUID.toString() + ".dat").exists()) continue;
            this.a(uUID.toString(), compoundTag, false, false);
            this.updateName(uUID, string);
            if (file3.delete()) continue;
            y.warn("Unable to delete legacy data for {}", (Object)string);
        }
    }

    public Player getPlayer(String string) {
        Player player = null;
        string = string.toLowerCase();
        int n = Integer.MAX_VALUE;
        for (Player player2 : this.getOnlinePlayers().values()) {
            if (!player2.getName().toLowerCase().startsWith(string)) continue;
            int n2 = player2.getName().length() - string.length();
            if (n2 < n) {
                player = player2;
                n = n2;
            }
            if (n2 != 0) continue;
            break;
        }
        return player;
    }

    public Player getPlayerExact(String string) {
        for (Player player : this.getOnlinePlayers().values()) {
            if (!player.getName().equalsIgnoreCase(string)) continue;
            return player;
        }
        return null;
    }

    public Player[] matchPlayer(String string) {
        string = string.toLowerCase();
        ArrayList<Player> arrayList = new ArrayList<Player>();
        for (Player player : this.getOnlinePlayers().values()) {
            if (player.getName().toLowerCase().equals(string)) {
                return new Player[]{player};
            }
            if (!player.getName().toLowerCase().contains(string)) continue;
            arrayList.add(player);
        }
        return arrayList.toArray(new Player[0]);
    }

    public void removePlayer(Player player) {
        if (this.G.remove(player.getSocketAddress()) != null) {
            return;
        }
        for (InetSocketAddress inetSocketAddress : new ArrayList<InetSocketAddress>(this.G.keySet())) {
            if (player != this.G.get(inetSocketAddress)) continue;
            this.G.remove(inetSocketAddress);
            break;
        }
    }

    public Map<Integer, Level> getLevels() {
        return this.t;
    }

    public Level getDefaultLevel() {
        return this.m;
    }

    public void setDefaultLevel(Level level) {
        block0: {
            if (level != null && (!this.isLevelLoaded(level.getFolderName()) || level == this.m)) break block0;
            this.m = level;
        }
    }

    public boolean isLevelLoaded(String string) {
        return this.getLevelByName(string) != null;
    }

    public Level getLevel(int n) {
        if (this.t.containsKey(n)) {
            return this.t.get(n);
        }
        return null;
    }

    public Level getLevelByName(String string) {
        for (Level level : this.ab) {
            if (!level.getFolderName().equalsIgnoreCase(string)) continue;
            return level;
        }
        return null;
    }

    public boolean unloadLevel(Level level) {
        return this.unloadLevel(level, false);
    }

    public boolean unloadLevel(Level level, boolean bl) {
        if (level == this.m && !bl) {
            throw new IllegalStateException("The default level cannot be unloaded while running, please switch levels.");
        }
        return level.unload(bl);
    }

    public boolean loadLevel(String string) {
        Level level;
        if (Objects.equals(string.trim(), "")) {
            throw new LevelException("Invalid empty level name");
        }
        if (!this.isPrimaryThread() && !this.suomiCraftPEMode()) {
            this.getLogger().warning("Loading level asynchronously: " + string);
        }
        if (this.isLevelLoaded(string)) {
            return true;
        }
        if (!this.isLevelGenerated(string)) {
            y.warn(this.aa.translateString("nukkit.level.notFound", string));
            return false;
        }
        String string2 = string.contains("/") || string.contains("\\") ? string : this.E + "worlds/" + string + '/';
        Class<? extends LevelProvider> clazz = LevelProviderManager.getProvider(string2);
        if (clazz == null) {
            y.error(this.aa.translateString("nukkit.level.loadError", new String[]{string, "Unknown provider"}));
            return false;
        }
        try {
            level = new Level(this, string, string2, clazz);
        }
        catch (Exception exception) {
            y.error(this.aa.translateString("nukkit.level.loadError", new String[]{string, exception.getMessage()}));
            return false;
        }
        this.t.put(level.getId(), level);
        level.initLevel();
        level.setTickRate(this.k);
        this.c.callEvent(new LevelLoadEvent(level));
        return true;
    }

    public boolean generateLevel(String string) {
        return this.generateLevel(string, Utils.random.nextLong());
    }

    public boolean generateLevel(String string, long l) {
        return this.generateLevel(string, l, null);
    }

    public boolean generateLevel(String string, long l, Class<? extends Generator> clazz) {
        return this.generateLevel(string, l, clazz, new HashMap<String, Object>());
    }

    public boolean generateLevel(String string, long l, Class<? extends Generator> clazz, Map<String, Object> map) {
        return this.generateLevel(string, l, clazz, map, null);
    }

    public boolean generateLevel(String string, long l, Class<? extends Generator> clazz, Map<String, Object> map, Class<? extends LevelProvider> clazz2) {
        Level level;
        if (Objects.equals(string.trim(), "") || this.isLevelGenerated(string)) {
            return false;
        }
        if (!map.containsKey("preset")) {
            map.put("preset", this.getPropertyString("generator-settings", ""));
        }
        if (clazz == null) {
            clazz = Generator.getGenerator(this.getLevelType());
        }
        if (clazz2 == null) {
            clazz2 = LevelProviderManager.getProviderByName("anvil");
        }
        String string2 = string.contains("/") || string.contains("\\") ? string : this.E + "worlds/" + string + '/';
        try {
            clazz2.getMethod("generate", String.class, String.class, Long.TYPE, Class.class, Map.class).invoke(null, string2, string, l, clazz, map);
            level = new Level(this, string, string2, clazz2);
            this.t.put(level.getId(), level);
            level.initLevel();
            level.setTickRate(this.k);
        }
        catch (Exception exception) {
            y.error(this.aa.translateString("nukkit.level.generationError", new String[]{string, Utils.getExceptionMessage(exception)}));
            return false;
        }
        this.c.callEvent(new LevelInitEvent(level));
        this.c.callEvent(new LevelLoadEvent(level));
        return true;
    }

    public boolean isLevelGenerated(String string) {
        if (Objects.equals(string.trim(), "")) {
            return false;
        }
        if (this.getLevelByName(string) == null) {
            String string2 = string.contains("/") || string.contains("\\") ? string : this.E + "worlds/" + string + '/';
            return LevelProviderManager.getProvider(string2) != null;
        }
        return true;
    }

    public BaseLang getLanguage() {
        return this.aa;
    }

    public boolean isLanguageForced() {
        return this.forceLanguage;
    }

    public Network getNetwork() {
        return this.O;
    }

    public Config getProperties() {
        return this.z;
    }

    public Object getProperty(String string) {
        return this.getProperty(string, null);
    }

    public Object getProperty(String string, Object object) {
        return this.z.exists(string) ? this.z.get(string) : object;
    }

    public void setPropertyString(String string, String string2) {
        this.z.set(string, string2);
        this.z.save();
    }

    public String getPropertyString(String string) {
        return this.getPropertyString(string, null);
    }

    public String getPropertyString(String string, String string2) {
        return this.z.exists(string) ? this.z.get(string).toString() : string2;
    }

    public int getPropertyInt(String string) {
        return this.getPropertyInt(string, null);
    }

    public int getPropertyInt(String string, Integer n) {
        return this.z.exists(string) ? (!this.z.get(string).equals("") ? Integer.parseInt(((StringBuilder)this.z.get(string)).toString()) : n) : n;
    }

    public void setPropertyInt(String string, int n) {
        this.z.set(string, n);
        this.z.save();
    }

    public boolean getPropertyBoolean(String string) {
        return this.getPropertyBoolean(string, null);
    }

    public boolean getPropertyBoolean(String string, Object object) {
        Object object2;
        Object object3 = object2 = this.z.exists(string) ? this.z.get(string) : object;
        if (object2 instanceof Boolean) {
            return (Boolean)object2;
        }
        switch (((StringBuilder)object2).toString()) {
            case "on": 
            case "true": 
            case "1": 
            case "yes": {
                return true;
            }
        }
        return false;
    }

    public void setPropertyBoolean(String string, boolean bl) {
        this.z.set(string, bl ? "1" : "0");
        this.z.save();
    }

    public PluginIdentifiableCommand getPluginCommand(String string) {
        Command command = this.v.getCommand(string);
        if (command instanceof PluginIdentifiableCommand) {
            return (PluginIdentifiableCommand)((Object)command);
        }
        return null;
    }

    public BanList getNameBans() {
        return this.l;
    }

    public BanList getIPBans() {
        return this.R;
    }

    public void addOp(String string) {
        this.b.set(string.toLowerCase(), true);
        Player player = this.getPlayerExact(string);
        if (player != null) {
            player.recalculatePermissions();
        }
        this.b.save(true);
    }

    public void removeOp(String string) {
        this.b.remove(string.toLowerCase());
        Player player = this.getPlayerExact(string);
        if (player != null) {
            player.recalculatePermissions();
        }
        this.b.save();
    }

    public void addWhitelist(String string) {
        this.L.set(string.toLowerCase(), true);
        this.L.save(true);
    }

    public void removeWhitelist(String string) {
        this.L.remove(string.toLowerCase());
        this.L.save(true);
    }

    public boolean isWhitelisted(String string) {
        return !this.hasWhitelist() || this.b.exists(string, true) || this.L.exists(string, true);
    }

    public boolean isOp(String string) {
        return this.b.exists(string, true);
    }

    public Config getWhitelist() {
        return this.L;
    }

    public Config getOps() {
        return this.b;
    }

    public void reloadWhitelist() {
        this.L.reload();
    }

    public Map<String, List<String>> getCommandAliases() {
        return new LinkedHashMap<String, List<String>>();
    }

    public ServiceManager getServiceManager() {
        return this.U;
    }

    public boolean shouldSavePlayerData() {
        return this.shouldSavePlayerData;
    }

    public int getPlayerSkinChangeCooldown() {
        return this.skinChangeCooldown;
    }

    public Level getNetherWorld(String string) {
        return multiNetherWorlds.contains(string) ? this.getLevelByName(string + "-nether") : this.getLevelByName("nether");
    }

    public static Int2ObjectMap<ObjectList<Player>> sortPlayers(Player[] playerArray) {
        Int2ObjectOpenHashMap<ObjectList<Player>> int2ObjectOpenHashMap = new Int2ObjectOpenHashMap<ObjectList<Player>>();
        for (Player player : playerArray) {
            int2ObjectOpenHashMap.computeIfAbsent(player.protocol, n -> new ObjectArrayList()).add(player);
        }
        return int2ObjectOpenHashMap;
    }

    public static Int2ObjectMap<ObjectList<Player>> sortPlayers(Collection<Player> collection) {
        Int2ObjectOpenHashMap<ObjectList<Player>> int2ObjectOpenHashMap = new Int2ObjectOpenHashMap<ObjectList<Player>>();
        for (Player player : collection) {
            int2ObjectOpenHashMap.computeIfAbsent(player.protocol, n -> new ObjectArrayList()).add(player);
        }
        return int2ObjectOpenHashMap;
    }

    public boolean isPrimaryThread() {
        return Thread.currentThread() == this.W;
    }

    public Thread getPrimaryThread() {
        return this.W;
    }

    private static void d() {
        Entity.registerEntity("Item", EntityItem.class);
        Entity.registerEntity("Painting", EntityPainting.class);
        Entity.registerEntity("XpOrb", EntityXPOrb.class);
        Entity.registerEntity("ArmorStand", EntityArmorStand.class);
        Entity.registerEntity("EndCrystal", EntityEndCrystal.class);
        Entity.registerEntity("FallingSand", EntityFallingBlock.class);
        Entity.registerEntity("PrimedTnt", EntityPrimedTNT.class);
        Entity.registerEntity("Firework", EntityFirework.class);
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
        Entity.registerEntity("MinecartRideable", EntityMinecartEmpty.class);
        Entity.registerEntity("MinecartChest", EntityMinecartChest.class);
        Entity.registerEntity("MinecartHopper", EntityMinecartHopper.class);
        Entity.registerEntity("MinecartTnt", EntityMinecartTNT.class);
        Entity.registerEntity("Boat", EntityBoat.class);
        Entity.registerEntity("ChestBoat", EntityChestBoat.class);
        Entity.registerEntity("Human", EntityHuman.class, true);
        Entity.registerEntity("Lightning", EntityLightning.class);
    }

    private static void f() {
        BlockEntity.registerBlockEntity("Furnace", BlockEntityFurnace.class);
        BlockEntity.registerBlockEntity("Chest", BlockEntityChest.class);
        BlockEntity.registerBlockEntity("Sign", BlockEntitySign.class);
        BlockEntity.registerBlockEntity("EnchantTable", BlockEntityEnchantTable.class);
        BlockEntity.registerBlockEntity("Skull", BlockEntitySkull.class);
        BlockEntity.registerBlockEntity("FlowerPot", BlockEntityFlowerPot.class);
        BlockEntity.registerBlockEntity("BrewingStand", BlockEntityBrewingStand.class);
        BlockEntity.registerBlockEntity("ItemFrame", BlockEntityItemFrame.class);
        BlockEntity.registerBlockEntity("Cauldron", BlockEntityCauldron.class);
        BlockEntity.registerBlockEntity("EnderChest", BlockEntityEnderChest.class);
        BlockEntity.registerBlockEntity("Beacon", BlockEntityBeacon.class);
        BlockEntity.registerBlockEntity("PistonArm", BlockEntityPistonArm.class);
        BlockEntity.registerBlockEntity("Comparator", BlockEntityComparator.class);
        BlockEntity.registerBlockEntity("Hopper", BlockEntityHopper.class);
        BlockEntity.registerBlockEntity("Bed", BlockEntityBed.class);
        BlockEntity.registerBlockEntity("Jukebox", BlockEntityJukebox.class);
        BlockEntity.registerBlockEntity("ShulkerBox", BlockEntityShulkerBox.class);
        BlockEntity.registerBlockEntity("Banner", BlockEntityBanner.class);
        BlockEntity.registerBlockEntity("Dropper", BlockEntityDropper.class);
        BlockEntity.registerBlockEntity("Dispenser", BlockEntityDispenser.class);
        BlockEntity.registerBlockEntity("MobSpawner", BlockEntitySpawner.class);
        BlockEntity.registerBlockEntity("Music", BlockEntityMusic.class);
        BlockEntity.registerBlockEntity("Campfire", BlockEntityCampfire.class);
        BlockEntity.registerBlockEntity("Barrel", BlockEntityBarrel.class);
        BlockEntity.registerBlockEntity("Lectern", BlockEntityLectern.class);
        BlockEntity.registerBlockEntity("BlastFurnace", BlockEntityBlastFurnace.class);
        BlockEntity.registerBlockEntity("Smoker", BlockEntitySmoker.class);
        BlockEntity.registerBlockEntity("Bell", BlockEntityBell.class);
    }

    public boolean isNetherAllowed() {
        return this.netherEnabled;
    }

    public PlayerDataSerializer getPlayerDataSerializer() {
        return this.i;
    }

    public void setPlayerDataSerializer(PlayerDataSerializer playerDataSerializer) {
        this.i = Preconditions.checkNotNull(playerDataSerializer, "playerDataSerializer");
    }

    public static Server getInstance() {
        return I;
    }

    public boolean suomiCraftPEMode() {
        return this.D;
    }

    public SpawnerTask getSpawnerTask() {
        return this.ai;
    }

    private void a() {
        Object object;
        Object object2;
        Object object3;
        if (!this.getPropertyBoolean("ansi-title", true)) {
            Nukkit.TITLE = false;
        }
        this.w = this.getPropertyInt("max-players", 50);
        this.forceLanguage = this.getPropertyBoolean("force-language", false);
        this.networkCompressionLevel = Math.max(Math.min(this.getPropertyInt("compression-level", 4), 9), 0);
        this.chunkCompressionLevel = Math.max(Math.min(this.getPropertyInt("chunk-compression-level", 7), 9), 1);
        this.autoTickRate = this.getPropertyBoolean("auto-tick-rate", true);
        this.autoTickRateLimit = this.getPropertyInt("auto-tick-rate-limit", 20);
        this.alwaysTickPlayers = this.getPropertyBoolean("always-tick-players", false);
        this.k = this.getPropertyInt("base-tick-rate", 1);
        this.D = this.getPropertyBoolean("suomicraft-mode", false);
        this.callDataPkSendEv = this.getPropertyBoolean("call-data-pk-send-event", true);
        this.callBatchPkEv = this.getPropertyBoolean("call-batch-pk-send-event", true);
        this.doLevelGC = this.getPropertyBoolean("do-level-gc", true);
        this.mobAiEnabled = this.getPropertyBoolean("mob-ai", true);
        this.netherEnabled = this.getPropertyBoolean("nether", true);
        this.endEnabled = this.getPropertyBoolean("end", false);
        this.xboxAuth = this.getPropertyBoolean("xbox-auth", true);
        this.bedSpawnpoints = this.getPropertyBoolean("bed-spawnpoints", true);
        this.achievementsEnabled = this.getPropertyBoolean("achievements", true);
        this.dimensionsEnabled = this.getPropertyBoolean("dimensions", true);
        this.banXBAuthFailed = this.getPropertyBoolean("temp-ip-ban-failed-xbox-auth", false);
        this.pvpEnabled = this.getPropertyBoolean("pvp", true);
        this.announceAchievements = this.getPropertyBoolean("announce-player-achievements", true);
        this.spawnEggsEnabled = this.getPropertyBoolean("spawn-eggs", true);
        this.xpBottlesOnCreative = this.getPropertyBoolean("xp-bottles-on-creative", true);
        this.shouldSavePlayerData = this.getPropertyBoolean("save-player-data", true);
        this.mobsFromBlocks = this.getPropertyBoolean("block-listener", true);
        this.explosionBreakBlocks = this.getPropertyBoolean("explosion-break-blocks", true);
        this.vanillaBossBar = this.getPropertyBoolean("vanilla-bossbars", false);
        this.stopInGame = this.getPropertyBoolean("stop-in-game", false);
        this.opInGame = this.getPropertyBoolean("op-in-game", false);
        this.lightUpdates = this.getPropertyBoolean("light-updates", false);
        this.queryPlugins = this.getPropertyBoolean("query-plugins", false);
        this.flyChecks = this.getPropertyBoolean("allow-flight", false);
        this.isHardcore = this.getPropertyBoolean("hardcore", false);
        this.despawnMobs = this.getPropertyBoolean("entity-despawn-task", true);
        this.forceResources = this.getPropertyBoolean("force-resources", false);
        this.forceResourcesAllowOwnPacks = this.getPropertyBoolean("force-resources-allow-client-packs", false);
        this.whitelistEnabled = this.getPropertyBoolean("white-list", false);
        this.checkOpMovement = this.getPropertyBoolean("check-op-movement", false);
        this.forceGamemode = this.getPropertyBoolean("force-gamemode", true);
        this.doNotLimitInteractions = this.getPropertyBoolean("do-not-limit-interactions", false);
        this.motd = this.getPropertyString("motd", "Minecraft Server");
        this.viewDistance = this.getPropertyInt("view-distance", 8);
        this.mobDespawnTicks = this.getPropertyInt("ticks-per-entity-despawns", 12000);
        this.S = this.getPropertyInt("server-port", 19132);
        this.Q = this.getPropertyString("server-ip", "0.0.0.0");
        this.skinChangeCooldown = this.getPropertyInt("skin-change-cooldown", 30);
        this.strongIPBans = this.getPropertyBoolean("strong-ip-bans", false);
        this.spawnRadius = this.getPropertyInt("spawn-protection", 10);
        this.spawnAnimals = this.getPropertyBoolean("spawn-animals", true);
        this.spawnMonsters = this.getPropertyBoolean("spawn-mobs", true);
        this.autoSaveTicks = this.getPropertyInt("ticks-per-autosave", 6000);
        this.doNotLimitSkinGeometry = this.getPropertyBoolean("do-not-limit-skin-geometry", true);
        this.anvilsEnabled = this.getPropertyBoolean("anvils-enabled", true);
        this.chunksPerTick = this.getPropertyInt("chunk-sending-per-tick", 4);
        this.spawnThreshold = this.getPropertyInt("spawn-threshold", 56);
        this.savePlayerDataByUuid = this.getPropertyBoolean("save-player-data-by-uuid", true);
        this.vanillaPortals = this.getPropertyBoolean("vanilla-portals", true);
        this.personaSkins = this.getPropertyBoolean("persona-skins", true);
        this.cacheChunks = this.getPropertyBoolean("cache-chunks", false);
        this.callEntityMotionEv = this.getPropertyBoolean("call-entity-motion-event", true);
        this.updateChecks = this.getPropertyBoolean("update-notifications", true);
        this.minimumProtocol = this.getPropertyInt("multiversion-min-protocol", 0);
        this.whitelistReason = this.getPropertyString("whitelist-reason", "Server is white-listed").replace("\u00a7n", "\n");
        this.asyncChunkSending = this.getPropertyBoolean("async-chunks", true);
        this.logJoinLocation = this.getPropertyBoolean("log-join-location", true);
        this.unsafeRedstone = this.getPropertyBoolean("unsafe-redstone", true);
        this.disableNewBlocks = this.getPropertyBoolean("disable-new-blocks", false);
        this.attackStopSprint = this.getPropertyBoolean("attack-stop-sprint", true);
        this.reduceTraffic = this.getPropertyBoolean("reduce-traffic", false);
        this.af = (int)Math.ceil(Math.sqrt(this.spawnThreshold));
        this.setAutoSave(this.getPropertyBoolean("auto-save", true));
        try {
            this.gamemode = this.getPropertyInt("gamemode", 0) & 3;
        }
        catch (NumberFormatException numberFormatException) {
            this.gamemode = Server.getGamemodeFromString(this.getPropertyString("gamemode")) & 3;
        }
        String string = this.getPropertyString("do-not-tick-worlds");
        if (!string.trim().isEmpty()) {
            object3 = new StringTokenizer(string, ", ");
            while (((StringTokenizer)object3).hasMoreTokens()) {
                noTickingWorlds.add(((StringTokenizer)object3).nextToken());
            }
        }
        if (this.isHardcore && this.a < 3) {
            this.setDifficulty(3);
        } else {
            this.setDifficulty(Server.getDifficultyFromString(this.getPropertyString("difficulty", "2")));
        }
        antiXrayWorlds.clear();
        object3 = this.getPropertyString("anti-xray-worlds");
        if (!((String)object3).trim().isEmpty()) {
            object2 = new StringTokenizer((String)object3, ", ");
            while (((StringTokenizer)object2).hasMoreTokens()) {
                antiXrayWorlds.add(((StringTokenizer)object2).nextToken());
            }
        }
        disabledSpawnWorlds.clear();
        object2 = Server.getInstance().getPropertyString("worlds-entity-spawning-disabled");
        if (!((String)object2).trim().isEmpty()) {
            object = new StringTokenizer((String)object2, ", ");
            while (((StringTokenizer)object).hasMoreTokens()) {
                disabledSpawnWorlds.add(((StringTokenizer)object).nextToken());
            }
        }
        nonAutoSaveWorlds.clear();
        object = Server.getInstance().getPropertyString("worlds-level-auto-save-disabled");
        if (!((String)object).trim().isEmpty()) {
            StringTokenizer stringTokenizer = new StringTokenizer((String)object, ", ");
            while (stringTokenizer.hasMoreTokens()) {
                nonAutoSaveWorlds.add(stringTokenizer.nextToken());
            }
        }
    }

    public static void mvw(String string) {
        block0: {
            if (Server.getInstance().minimumProtocol == ProtocolInfo.CURRENT_PROTOCOL) break block0;
            Server.getInstance().getLogger().logException(new PluginException("Default " + string + " used by a plugin. This can cause instability with the multiversion."));
        }
    }

    static /* synthetic */ Level[] access$002(Server server, Level[] levelArray) {
        server.ab = levelArray;
        return levelArray;
    }

    static /* synthetic */ Map access$100(Server server) {
        return server.t;
    }

    static /* synthetic */ void access$400(Server server, PlayerDataSerializer playerDataSerializer, CompoundTag compoundTag, String string, UUID uUID) {
        server.a(playerDataSerializer, compoundTag, string, uUID);
    }

    static /* synthetic */ NukkitConsole access$500(Server server) {
        return server.h;
    }

    static {
        BROADCAST_CHANNEL_ADMINISTRATIVE = "nukkit.broadcast.admin";
        BROADCAST_CHANNEL_USERS = "nukkit.broadcast.user";
        y = LogManager.getLogger(Server.class);
        n = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}.dat$");
        disabledSpawnWorlds = new ArrayList<String>();
        nonAutoSaveWorlds = new ArrayList<String>();
        multiNetherWorlds = new ArrayList<String>();
        noTickingWorlds = new ArrayList<String>();
        antiXrayWorlds = new ArrayList<String>();
        C = new byte[]{-2, -3};
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}


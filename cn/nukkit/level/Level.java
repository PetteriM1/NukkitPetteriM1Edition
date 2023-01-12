/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.NOBF;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockBed;
import cn.nukkit.block.BlockGrass;
import cn.nukkit.block.BlockRedstoneDiode;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.custom.EntityDefinition;
import cn.nukkit.entity.custom.EntityManager;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.item.EntityXPOrb;
import cn.nukkit.entity.mob.EntityWither;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.Event;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.BlockUpdateEvent;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.event.level.ChunkLoadEvent;
import cn.nukkit.event.level.ChunkPopulateEvent;
import cn.nukkit.event.level.ChunkUnloadEvent;
import cn.nukkit.event.level.LevelSaveEvent;
import cn.nukkit.event.level.LevelUnloadEvent;
import cn.nukkit.event.level.SpawnChangeEvent;
import cn.nukkit.event.level.ThunderChangeEvent;
import cn.nukkit.event.level.WeatherChangeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.weather.LightningStrikeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.ChunkLoader;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.DimensionData;
import cn.nukkit.level.EnumLevel;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.GameRules;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.ParticleEffect;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.a;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.c;
import cn.nukkit.level.d;
import cn.nukkit.level.e;
import cn.nukkit.level.format.Chunk;
import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.format.generic.BaseLevelProvider;
import cn.nukkit.level.format.generic.EmptyChunkSection;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.task.GenerationTask;
import cn.nukkit.level.generator.task.LightPopulationTask;
import cn.nukkit.level.generator.task.PopulationTask;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.metadata.BlockMetadataStore;
import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.metadata.Metadatable;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.GameRulesChangedPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.SetSpawnPositionPacket;
import cn.nukkit.network.protocol.SetTimePacket;
import cn.nukkit.network.protocol.SpawnParticleEffectPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.BlockUpdateScheduler;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.BlockUpdateEntry;
import cn.nukkit.utils.Hash;
import cn.nukkit.utils.LevelException;
import cn.nukkit.utils.LevelTimings;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;
import co.aikar.timings.Timings;
import co.aikar.timings.TimingsHistory;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongMaps;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongListIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.lang.ref.SoftReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

public class Level
implements ChunkManager,
Metadatable {
    private static int F = 1;
    private static int f = 1;
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
    public static final int MAX_BLOCK_CACHE = 512;
    @NOBF
    private static final boolean[] randomTickBlocks = new boolean[512];
    public static final boolean[] xrayableBlocks = new boolean[512];
    private final Long2ObjectOpenHashMap<BlockEntity> R;
    private final Long2ObjectOpenHashMap<Player> H;
    public final Long2ObjectOpenHashMap<Entity> entities;
    public final Long2ObjectOpenHashMap<Entity> updateEntities;
    private final ConcurrentLinkedQueue<BlockEntity> D;
    private final Server N;
    private final int o;
    private LevelProvider u;
    private final Int2ObjectMap<ChunkLoader> U;
    private final Int2IntMap h;
    private final Long2ObjectOpenHashMap<Map<Integer, ChunkLoader>> m;
    private final Long2ObjectOpenHashMap<Map<Integer, Player>> E;
    private final Long2ObjectOpenHashMap<Deque<DataPacket>> V;
    private final Long2LongMap A;
    private int r;
    public boolean stopTime;
    public float skyLightSubtracted;
    private final String X;
    private final Long2ObjectOpenHashMap<SoftReference<Map<Character, Object>>> w;
    private final Object n;
    private final Map<Character, Object> S;
    private final BlockUpdateScheduler q;
    private final Queue<Block> Q;
    private final Int2ObjectMap<ConcurrentMap<Long, Int2ObjectMap<Player>>> K;
    private final Int2ObjectMap<LongSet> s;
    private final Long2ObjectOpenHashMap<Boolean> l;
    private final Long2ObjectOpenHashMap<Boolean> e;
    private final Long2ObjectOpenHashMap<Boolean> J;
    private final int B;
    private final int Y;
    private boolean O;
    private BlockMetadataStore x;
    private final boolean M;
    private final Vector3 b;
    public int sleepTicks;
    private final int G;
    private final Long2IntMap t;
    private final int v;
    private final boolean g;
    private int L;
    private static final int W = 1013904223;
    public LevelTimings timings;
    private int Z;
    public int tickRateTime;
    public int tickRateCounter;
    public final boolean isNether;
    public final boolean isEnd;
    @NOBF
    private Class<? extends Generator> generatorClass;
    @NOBF
    private ThreadLocal<Generator> generators;
    private boolean c;
    private int C;
    private boolean j;
    private int k;
    private long P;
    private DimensionData y;
    public GameRules gameRules;
    private final boolean p;
    private final boolean T;
    private e a;
    private Vector3 d;
    private final List<Long> z;
    private final Map<Long, Map<Character, Object>> i;
    private int I;

    public Level(Server server, String string, String string2, Class<? extends LevelProvider> clazz) {
        block6: {
            this.R = new Long2ObjectOpenHashMap();
            this.H = new Long2ObjectOpenHashMap();
            this.entities = new Long2ObjectOpenHashMap();
            this.updateEntities = new Long2ObjectOpenHashMap();
            this.D = new ConcurrentLinkedQueue();
            this.U = new Int2ObjectOpenHashMap<ChunkLoader>();
            this.h = new Int2IntOpenHashMap();
            this.m = new Long2ObjectOpenHashMap();
            this.E = new Long2ObjectOpenHashMap();
            this.V = new Long2ObjectOpenHashMap();
            this.A = Long2LongMaps.synchronize(new Long2LongOpenHashMap());
            this.w = new Long2ObjectOpenHashMap();
            this.n = new Object();
            this.S = new d(null);
            this.Q = new ConcurrentLinkedDeque<Block>();
            this.K = new Int2ObjectOpenHashMap<ConcurrentMap<Long, Int2ObjectMap<Player>>>();
            this.s = new Int2ObjectOpenHashMap<LongSet>();
            this.l = new Long2ObjectOpenHashMap();
            this.e = new Long2ObjectOpenHashMap();
            this.J = new Long2ObjectOpenHashMap();
            this.sleepTicks = 0;
            this.t = new Long2IntOpenHashMap();
            this.L = ThreadLocalRandom.current().nextInt();
            this.tickRateTime = 0;
            this.tickRateCounter = 0;
            this.generators = new a(this);
            this.z = new LongArrayList();
            this.i = new ConcurrentHashMap<Long, Map<Character, Object>>(8, 0.9f, 1);
            this.o = F++;
            this.x = new BlockMetadataStore(this);
            this.N = server;
            this.O = server.getAutoSave();
            try {
                this.u = clazz.getConstructor(Level.class, String.class).newInstance(this, string2);
            }
            catch (Exception exception) {
                throw new LevelException("Caused by " + Utils.getExceptionMessage(exception));
            }
            this.timings = new LevelTimings(this);
            this.u.updateLevelName(string);
            this.N.getLogger().info(this.N.getLanguage().translateString("nukkit.level.preparing", (Object)((Object)TextFormat.GREEN) + this.u.getName() + (Object)((Object)TextFormat.WHITE)));
            this.generatorClass = Generator.getGenerator(this.u.getGenerator());
            try {
                this.M = (Boolean)clazz.getMethod("usesChunkSection", new Class[0]).invoke(null, new Object[0]);
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
            this.X = string;
            this.r = (int)this.u.getTime();
            this.c = this.u.isRaining();
            this.C = this.u.getRainTime();
            if (this.C <= 0) {
                this.setRainTime(Utils.random.nextInt(168000) + 12000);
            }
            this.j = this.u.isThundering();
            this.k = this.u.getThunderTime();
            if (this.k <= 0) {
                this.setThunderTime(Utils.random.nextInt(168000) + 12000);
            }
            this.P = this.u.getCurrentTick();
            this.q = new BlockUpdateScheduler(this, this.P);
            this.G = Math.min(this.N.getViewDistance(), Math.max(1, this.N.getPropertyInt("chunk-ticking-radius", 4)));
            this.v = this.N.getPropertyInt("chunk-ticking-per-tick", 40);
            this.B = this.N.getPropertyInt("chunk-generation-queue-size", 8);
            this.Y = this.N.getPropertyInt("chunk-generation-population-queue-size", 8);
            this.t.clear();
            this.g = this.N.getPropertyBoolean("clear-chunk-tick-list", true);
            this.b = new Vector3(0.0, 0.0, 0.0);
            this.Z = 1;
            this.skyLightSubtracted = this.calculateSkylightSubtracted(1.0f);
            this.isNether = string.equals("nether");
            this.isEnd = string.equals("the_end");
            this.p = !Server.noTickingWorlds.contains(string);
            this.T = Server.antiXrayWorlds.contains(string);
            if (!server.asyncChunkSending) break block6;
            this.a = new e(string);
        }
    }

    public static long chunkHash(int n, int n2) {
        return (long)n << 32 | (long)n2 & 0xFFFFFFFFL;
    }

    public static long blockHash(int n, int n2, int n3) {
        if (n2 < 0 || n2 >= 256) {
            throw new IllegalArgumentException("Y coordinate " + n2 + " is out of range!");
        }
        return ((long)n & 0xFFFFFFFL) << 36 | ((long)n2 & 0xFFL) << 28 | (long)n3 & 0xFFFFFFFL;
    }

    public static char localBlockHash(double d2, double d3, double d4) {
        byte by = (byte)(((int)d2 & 0xF) + (((int)d4 & 0xF) << 4));
        byte by2 = (byte)d3;
        return (char)((by & 0xFF) << 8 | by2 & 0xFF);
    }

    public static Vector3 getBlockXYZ(long l, char c2) {
        byte by = (byte)(c2 >>> 8);
        byte by2 = (byte)c2;
        int n = by2 & 0xFF;
        int n2 = (by & 0xF) + (Level.getHashX(l) << 4);
        int n3 = (by >> 4 & 0xF) + (Level.getHashZ(l) << 4);
        return new Vector3(n2, n, n3);
    }

    public static BlockVector3 blockHash(double d2, double d3, double d4) {
        return new BlockVector3((int)d2, (int)d3, (int)d4);
    }

    public static int chunkBlockHash(int n, int n2, int n3) {
        return n << 12 | n3 << 8 | n2;
    }

    public static int getHashX(long l) {
        return (int)(l >> 32);
    }

    public static int getHashZ(long l) {
        return (int)l;
    }

    public static Vector3 getBlockXYZ(BlockVector3 blockVector3) {
        return new Vector3(blockVector3.x, blockVector3.y, blockVector3.z);
    }

    public static Chunk.Entry getChunkXZ(long l) {
        return new Chunk.Entry(Level.getHashX(l), Level.getHashZ(l));
    }

    public static int generateChunkLoaderId(ChunkLoader chunkLoader) {
        if (chunkLoader.getLoaderId() == 0) {
            return f++;
        }
        throw new IllegalStateException("ChunkLoader has a loader id already assigned: " + chunkLoader.getLoaderId());
    }

    public int getTickRate() {
        return this.Z;
    }

    public int getTickRateTime() {
        return this.tickRateTime;
    }

    public void setTickRate(int n) {
        this.Z = n;
    }

    public void initLevel() {
        Generator generator = this.generators.get();
        this.y = generator.getDimensionData();
        this.gameRules = this.u.getGamerules();
    }

    public Generator getGenerator() {
        return this.generators.get();
    }

    public BlockMetadataStore getBlockMetadata() {
        return this.x;
    }

    public Server getServer() {
        return this.N;
    }

    public final LevelProvider getProvider() {
        return this.u;
    }

    public final int getId() {
        return this.o;
    }

    public void close() {
        if (this.O) {
            this.save(true);
        }
        if (this.a != null) {
            this.a.a();
        }
        this.u.close();
        this.u = null;
        this.x = null;
        this.N.getLevels().remove(this.o);
    }

    public void addSound(Vector3 vector3, Sound sound) {
        this.addSound(vector3, sound, 1.0f, 1.0f, (Player[])null);
    }

    public void addSound(Vector3 vector3, Sound sound, float f2, float f3) {
        this.addSound(vector3, sound, f2, f3, (Player[])null);
    }

    public void addSound(Vector3 vector3, Sound sound, float f2, float f3, Collection<Player> collection) {
        this.addSound(vector3, sound, f2, f3, collection.toArray(new Player[0]));
    }

    public void addSound(Vector3 vector3, Sound sound, float f2, float f3, Player ... playerArray) {
        Preconditions.checkArgument(f2 >= 0.0f && f2 <= 1.0f, "Sound volume must be between 0 and 1");
        Preconditions.checkArgument(f3 >= 0.0f, "Sound pitch must be higher than 0");
        PlaySoundPacket playSoundPacket = new PlaySoundPacket();
        playSoundPacket.name = sound.getSound();
        playSoundPacket.volume = f2;
        playSoundPacket.pitch = f3;
        playSoundPacket.x = vector3.getFloorX();
        playSoundPacket.y = vector3.getFloorY();
        playSoundPacket.z = vector3.getFloorZ();
        if (playerArray == null || playerArray.length == 0) {
            this.addChunkPacket(vector3.getChunkX(), vector3.getChunkZ(), playSoundPacket);
        } else {
            Server.broadcastPacket(playerArray, (DataPacket)playSoundPacket);
        }
    }

    public void addSound(Vector3 vector3, String string) {
        this.addSound(vector3, string, null);
    }

    public void addSound(Vector3 vector3, String string, Player ... playerArray) {
        PlaySoundPacket playSoundPacket = new PlaySoundPacket();
        playSoundPacket.name = string;
        playSoundPacket.volume = 1.0f;
        playSoundPacket.pitch = 1.0f;
        playSoundPacket.x = vector3.getFloorX();
        playSoundPacket.y = vector3.getFloorY();
        playSoundPacket.z = vector3.getFloorZ();
        if (playerArray == null || playerArray.length == 0) {
            this.addChunkPacket(vector3.getChunkX(), vector3.getChunkZ(), playSoundPacket);
        } else {
            Server.broadcastPacket(playerArray, (DataPacket)playSoundPacket);
        }
    }

    public void addLevelEvent(Vector3 vector3, int n) {
        this.addLevelEvent(vector3, n, 0);
    }

    public void addLevelEvent(Vector3 vector3, int n, int n2) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.evid = n;
        levelEventPacket.x = (float)vector3.x;
        levelEventPacket.y = (float)vector3.y;
        levelEventPacket.z = (float)vector3.z;
        levelEventPacket.data = n2;
        this.addChunkPacket(vector3.getChunkX(), vector3.getChunkZ(), levelEventPacket);
    }

    public void addLevelSoundEvent(Vector3 vector3, int n, int n2, int n3) {
        this.addLevelSoundEvent(vector3, n, n2, n3, false, false);
    }

    public void addLevelSoundEvent(Vector3 vector3, int n, int n2, int n3, boolean bl, boolean bl2) {
        String string = AddEntityPacket.LEGACY_IDS.get(n3);
        if (string == null) {
            EntityDefinition entityDefinition = EntityManager.get().getDefinition(n3);
            string = entityDefinition == null ? ":" : entityDefinition.getIdentifier();
        }
        this.addLevelSoundEvent(vector3, n, n2, string, bl, bl2);
    }

    public void addLevelSoundEvent(Vector3 vector3, int n) {
        this.addLevelSoundEvent(vector3, n, -1);
    }

    public void addLevelSoundEvent(Vector3 vector3, int n, int n2) {
        this.addLevelSoundEvent(vector3, n, n2, ":", false, false);
    }

    public void addLevelSoundEvent(Vector3 vector3, int n, int n2, String string, boolean bl, boolean bl2) {
        LevelSoundEventPacket levelSoundEventPacket = new LevelSoundEventPacket();
        levelSoundEventPacket.sound = n;
        levelSoundEventPacket.extraData = n2;
        levelSoundEventPacket.entityIdentifier = string;
        levelSoundEventPacket.x = (float)vector3.x;
        levelSoundEventPacket.y = (float)vector3.y;
        levelSoundEventPacket.z = (float)vector3.z;
        levelSoundEventPacket.isGlobal = bl2;
        levelSoundEventPacket.isBabyMob = bl;
        this.addChunkPacket(vector3.getChunkX(), vector3.getChunkZ(), levelSoundEventPacket);
    }

    public void addParticle(Particle particle) {
        this.addParticle(particle, (Player[])null);
    }

    public void addParticle(Particle particle, Player player) {
        this.addParticle(particle, new Player[]{player});
    }

    public void addParticle(Particle particle, Player[] playerArray) {
        this.addParticle(particle, playerArray, 1);
    }

    public void addParticle(Particle particle, Player[] playerArray, int n) {
        Int2ObjectMap<ObjectList<Player>> int2ObjectMap = playerArray == null ? Server.sortPlayers(this.getChunkPlayers(particle.getChunkX(), particle.getChunkZ()).values()) : Server.sortPlayers(playerArray);
        IntIterator intIterator = int2ObjectMap.keySet().iterator();
        while (intIterator.hasNext()) {
            int n2 = (Integer)intIterator.next();
            ObjectList objectList = (ObjectList)int2ObjectMap.get(n2);
            DataPacket[] dataPacketArray = particle.mvEncode(n2);
            if (dataPacketArray == null) continue;
            if (n == 1) {
                Server.broadcastPackets(objectList.toArray(new Player[0]), dataPacketArray);
                continue;
            }
            List<DataPacket> list = Arrays.asList(dataPacketArray);
            ObjectArrayList objectArrayList = new ObjectArrayList();
            for (int k = 0; k < n; ++k) {
                objectArrayList.addAll(list);
            }
            Server.broadcastPackets(objectList.toArray(new Player[0]), objectArrayList.toArray(new DataPacket[0]));
        }
    }

    public void addParticle(Particle particle, Collection<Player> collection) {
        this.addParticle(particle, collection.toArray(new Player[0]));
    }

    public void addParticleEffect(Vector3 vector3, ParticleEffect particleEffect) {
        this.addParticleEffect(vector3, particleEffect, -1L, this.getDimension(), (Player[])null);
    }

    public void addParticleEffect(Vector3 vector3, ParticleEffect particleEffect, long l) {
        this.addParticleEffect(vector3, particleEffect, l, this.getDimension(), (Player[])null);
    }

    public void addParticleEffect(Vector3 vector3, ParticleEffect particleEffect, long l, int n) {
        this.addParticleEffect(vector3, particleEffect, l, n, (Player[])null);
    }

    public void addParticleEffect(Vector3 vector3, ParticleEffect particleEffect, long l, int n, Collection<Player> collection) {
        this.addParticleEffect(vector3, particleEffect, l, n, collection.toArray(new Player[0]));
    }

    public void addParticleEffect(Vector3 vector3, ParticleEffect particleEffect, long l, int n, Player ... playerArray) {
        this.addParticleEffect(vector3.asVector3f(), particleEffect.getIdentifier(), l, n, playerArray);
    }

    public void addParticleEffect(Vector3f vector3f, String string, long l, int n, Player ... playerArray) {
        SpawnParticleEffectPacket spawnParticleEffectPacket = new SpawnParticleEffectPacket();
        spawnParticleEffectPacket.identifier = string;
        spawnParticleEffectPacket.uniqueEntityId = l;
        spawnParticleEffectPacket.dimensionId = n;
        spawnParticleEffectPacket.position = vector3f;
        if (playerArray == null || playerArray.length == 0) {
            this.addChunkPacket(vector3f.getFloorX() >> 4, vector3f.getFloorZ() >> 4, spawnParticleEffectPacket);
        } else {
            Server.broadcastPacket(playerArray, (DataPacket)spawnParticleEffectPacket);
        }
    }

    public boolean getAutoSave() {
        return this.O;
    }

    public void setAutoSave(boolean bl) {
        this.O = bl;
    }

    public boolean unload() {
        return this.unload(false);
    }

    public boolean unload(boolean bl) {
        LevelUnloadEvent levelUnloadEvent = new LevelUnloadEvent(this);
        if (this == this.N.getDefaultLevel() && !bl) {
            levelUnloadEvent.setCancelled(true);
        }
        this.N.getPluginManager().callEvent(levelUnloadEvent);
        if (!bl && levelUnloadEvent.isCancelled()) {
            return false;
        }
        this.N.getLogger().info(this.N.getLanguage().translateString("nukkit.level.unloading", (Object)((Object)TextFormat.GREEN) + this.getName() + (Object)((Object)TextFormat.WHITE)));
        Level level = this.N.getDefaultLevel();
        for (Player player : new ArrayList<Player>(this.getPlayers().values())) {
            if (this == level || level == null) {
                player.close(player.getLeaveMessage(), "Forced default level unload");
                continue;
            }
            player.teleport(this.N.getDefaultLevel().getSafeSpawn());
        }
        if (this == level) {
            this.N.setDefaultLevel(null);
        }
        this.close();
        return true;
    }

    public Map<Integer, Player> getChunkPlayers(int n, int n2) {
        long l = Level.chunkHash(n, n2);
        Map<Integer, Player> map = this.E.get(l);
        if (map != null) {
            return new HashMap<Integer, Player>(map);
        }
        return new HashMap<Integer, Player>();
    }

    public ChunkLoader[] getChunkLoaders(int n, int n2) {
        long l = Level.chunkHash(n, n2);
        Map<Integer, ChunkLoader> map = this.m.get(l);
        if (map != null) {
            return map.values().toArray(new ChunkLoader[0]);
        }
        return new ChunkLoader[0];
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addChunkPacket(int n, int n2, DataPacket dataPacket) {
        long l2 = Level.chunkHash(n, n2);
        Long2ObjectOpenHashMap<Deque<DataPacket>> long2ObjectOpenHashMap = this.V;
        synchronized (long2ObjectOpenHashMap) {
            Deque deque = this.V.computeIfAbsent(l2, l -> new ArrayDeque());
            deque.add(dataPacket);
        }
    }

    public void registerChunkLoader(ChunkLoader chunkLoader, int n, int n2) {
        this.registerChunkLoader(chunkLoader, n, n2, true);
    }

    public void registerChunkLoader(ChunkLoader chunkLoader, int n, int n2, boolean bl) {
        block7: {
            int n3 = chunkLoader.getLoaderId();
            long l = Level.chunkHash(n, n2);
            Map<Integer, ChunkLoader> map = this.m.get(l);
            if (map == null) {
                HashMap<Integer, ChunkLoader> hashMap = new HashMap<Integer, ChunkLoader>();
                hashMap.put(n3, chunkLoader);
                this.m.put(l, (Map<Integer, ChunkLoader>)hashMap);
                HashMap<Integer, Player> hashMap2 = new HashMap<Integer, Player>();
                if (chunkLoader instanceof Player) {
                    hashMap2.put(n3, (Player)chunkLoader);
                }
                this.E.put(l, (Map<Integer, Player>)hashMap2);
            } else {
                if (map.containsKey(n3)) {
                    return;
                }
                map.put(n3, chunkLoader);
                if (chunkLoader instanceof Player) {
                    this.E.get(l).put(n3, (Player)chunkLoader);
                }
            }
            if (!this.U.containsKey(n3)) {
                this.h.put(n3, 1);
                this.U.put(n3, chunkLoader);
            } else {
                this.h.put(n3, this.h.get(n3) + 1);
            }
            this.cancelUnloadChunkRequest(n3);
            if (!bl) break block7;
            this.loadChunk(n, n2);
        }
    }

    public void unregisterChunkLoader(ChunkLoader chunkLoader, int n, int n2) {
        ChunkLoader chunkLoader2;
        int n3 = chunkLoader.getLoaderId();
        long l = Level.chunkHash(n, n2);
        Map<Integer, ChunkLoader> map = this.m.get(l);
        if (map != null && (chunkLoader2 = map.remove(n3)) != null) {
            if (map.isEmpty()) {
                this.m.remove(l);
                this.E.remove(l);
                this.unloadChunkRequest(n, n2, true);
            } else {
                Map<Integer, Player> map2 = this.E.get(l);
                map2.remove(n3);
            }
            int n4 = this.h.get(n3);
            if (--n4 == 0) {
                this.h.remove(n3);
                this.U.remove(n3);
            } else {
                this.h.put(n3, n4);
            }
        }
    }

    public void checkTime() {
        block1: {
            if (this.stopTime || !this.gameRules.getBoolean(GameRule.DO_DAYLIGHT_CYCLE)) break block1;
            this.r += this.Z;
            if (this.r < 0) {
                this.r = 0;
            }
        }
    }

    public void sendTime(Player ... playerArray) {
        SetTimePacket setTimePacket = new SetTimePacket();
        setTimePacket.time = this.r;
        Server.broadcastPacket(playerArray, (DataPacket)setTimePacket);
    }

    public void sendTime() {
        this.sendTime(this.H.values().toArray(new Player[0]));
    }

    public GameRules getGameRules() {
        return this.gameRules;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void doTick(int n) {
        block45: {
            Block block;
            Long2ObjectMap.Entry entry;
            if (this.timings.doTick != null) {
                this.timings.doTick.startTiming();
            }
            if (this.N.asyncChunkSending) {
                c object2;
                int n2 = (this.getPlayers().size() + 1) * this.N.chunksPerTick;
                for (int k = 0; k < n2 && (object2 = this.a.a.poll()) != null; ++k) {
                    this.chunkRequestCallback(object2.g, object2.a, object2.f, object2.e, object2.b, object2.d, object2.c);
                }
            }
            this.updateBlockLight(this.i);
            this.checkTime();
            if (n % 6000 == 0) {
                this.sendTime();
            }
            if (this.getDimension() != 1 && this.getDimension() != 2 && this.gameRules.getBoolean(GameRule.DO_WEATHER_CYCLE) && this.randomTickingEnabled()) {
                --this.C;
                if (this.C <= 0 && !this.setRaining(!this.c)) {
                    if (this.c) {
                        this.setRainTime(Utils.random.nextInt(12000) + 12000);
                    } else {
                        this.setRainTime(Utils.random.nextInt(168000) + 12000);
                    }
                }
                --this.k;
                if (this.k <= 0 && !this.setThundering(!this.j)) {
                    if (this.j) {
                        this.setThunderTime(Utils.random.nextInt(12000) + 3600);
                    } else {
                        this.setThunderTime(Utils.random.nextInt(168000) + 12000);
                    }
                }
                if (this.isThundering()) {
                    Map<Long, ? extends FullChunk> map = this.getChunks();
                    if (map instanceof Long2ObjectOpenHashMap) {
                        Long2ObjectOpenHashMap long2ObjectOpenHashMap = (Long2ObjectOpenHashMap)map;
                        ObjectIterator l = long2ObjectOpenHashMap.long2ObjectEntrySet().fastIterator();
                        while (l.hasNext()) {
                            entry = (Long2ObjectMap.Entry)l.next();
                            this.a(entry.getLongKey(), (FullChunk)entry.getValue());
                        }
                    } else {
                        for (Map.Entry<Long, ? extends FullChunk> entry2 : map.entrySet()) {
                            this.a(entry2.getKey(), entry2.getValue());
                        }
                    }
                }
            }
            if (Server.getInstance().lightUpdates) {
                this.skyLightSubtracted = this.calculateSkylightSubtracted(1.0f);
            }
            ++this.P;
            this.unloadChunks();
            if (this.timings.doTickPending != null) {
                this.timings.doTickPending.startTiming();
            }
            this.q.tick(this.P);
            if (this.timings.doTickPending != null) {
                this.timings.doTickPending.stopTiming();
            }
            while ((block = this.Q.poll()) != null) {
                block.onUpdate(1);
            }
            TimingsHistory.entityTicks += (long)this.updateEntities.size();
            if (this.timings.entityTick != null) {
                this.timings.entityTick.startTiming();
            }
            if (!this.updateEntities.isEmpty()) {
                for (long l : new ArrayList<Long>(this.updateEntities.keySet())) {
                    Entity entity = this.updateEntities.get(l);
                    if (entity == null) {
                        this.updateEntities.remove(l);
                        continue;
                    }
                    if (!entity.closed && entity.onUpdate(n)) continue;
                    this.updateEntities.remove(l);
                }
            }
            if (this.timings.entityTick != null) {
                this.timings.entityTick.stopTiming();
            }
            TimingsHistory.tileEntityTicks += (long)this.D.size();
            if (this.timings.blockEntityTick != null) {
                this.timings.blockEntityTick.startTiming();
            }
            this.D.removeIf(blockEntity -> !blockEntity.isValid() || !blockEntity.onUpdate());
            if (this.timings.blockEntityTick != null) {
                this.timings.blockEntityTick.stopTiming();
            }
            if (this.timings.tickChunks != null) {
                this.timings.tickChunks.startTiming();
            }
            this.a();
            if (this.timings.tickChunks != null) {
                this.timings.tickChunks.stopTiming();
            }
            Cloneable cloneable = this.w;
            synchronized (cloneable) {
                if (!this.w.isEmpty()) {
                    if (!this.H.isEmpty()) {
                        ObjectIterator objectIterator = this.w.long2ObjectEntrySet().fastIterator();
                        while (objectIterator.hasNext()) {
                            Object object;
                            entry = (Long2ObjectMap.Entry)objectIterator.next();
                            long l = entry.getLongKey();
                            Map map = (Map)((SoftReference)entry.getValue()).get();
                            int n2 = Level.getHashX(l);
                            int n3 = Level.getHashZ(l);
                            if (map == null || map.size() > 512) {
                                object = this.getChunk(n2, n3);
                                for (Player player : this.getChunkPlayers(n2, n3).values()) {
                                    player.onChunkChanged((FullChunk)object);
                                }
                                continue;
                            }
                            object = this.getChunkPlayers(n2, n3).values().toArray(new Player[0]);
                            Vector3[] vector3Array = new Vector3[map.size()];
                            int n4 = 0;
                            Iterator iterator = map.keySet().iterator();
                            while (iterator.hasNext()) {
                                char c2 = ((Character)iterator.next()).charValue();
                                Vector3 vector3 = Level.getBlockXYZ(l, c2);
                                vector3Array[n4++] = vector3;
                            }
                            this.sendBlocks((Player[])object, vector3Array, 3);
                        }
                    }
                    this.w.clear();
                }
            }
            this.b();
            if (this.sleepTicks > 0 && --this.sleepTicks <= 0) {
                this.checkSleep();
            }
            cloneable = this.V;
            synchronized (cloneable) {
                LongIterator longIterator = this.V.keySet().iterator();
                while (longIterator.hasNext()) {
                    int n5;
                    long l = (Long)longIterator.next();
                    int n6 = Level.getHashX(l);
                    Map<Integer, Player> map = this.getChunkPlayers(n6, n5 = Level.getHashZ(l));
                    if (map.isEmpty()) continue;
                    Player[] playerArray = map.values().toArray(new Player[0]);
                    for (DataPacket dataPacket : this.V.get(l)) {
                        Server.broadcastPacket(playerArray, dataPacket);
                    }
                }
                this.V.clear();
            }
            if (this.gameRules.isStale()) {
                cloneable = new GameRulesChangedPacket();
                ((GameRulesChangedPacket)cloneable).gameRulesMap = this.gameRules.getGameRules();
                Server.broadcastPacket(this.H.values(), cloneable);
                this.gameRules.refresh();
            }
            if (this.timings.doTick == null) break block45;
            this.timings.doTick.stopTiming();
        }
    }

    private void a(long l, FullChunk fullChunk) {
        if (Utils.random.nextInt(10000) == 0) {
            int n;
            if (this.a(l)) {
                return;
            }
            int n2 = this.c() >> 2;
            int n3 = fullChunk.getX() << 4;
            Vector3 vector3 = this.adjustPosToNearbyEntity(new Vector3(n3 + (n2 & 0xF), 0.0, (n = fullChunk.getZ() << 4) + (n2 >> 8 & 0xF)));
            Biome biome = Biome.getBiome(this.getBiomeId(vector3.getFloorX(), vector3.getFloorZ()));
            if (!biome.canRain()) {
                return;
            }
            int n4 = this.getBlockIdAt(vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ());
            if (n4 != 31 && n4 != 8) {
                vector3.y += 1.0;
            }
            CompoundTag compoundTag = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", vector3.x)).add(new DoubleTag("", vector3.y)).add(new DoubleTag("", vector3.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0.0f)).add(new FloatTag("", 0.0f)));
            EntityLightning entityLightning = new EntityLightning(fullChunk, compoundTag);
            LightningStrikeEvent lightningStrikeEvent = new LightningStrikeEvent(this, entityLightning);
            this.N.getPluginManager().callEvent(lightningStrikeEvent);
            if (!lightningStrikeEvent.isCancelled()) {
                entityLightning.spawnToAll();
                this.addLevelSoundEvent(vector3, 47, -1, 93);
                this.addLevelSoundEvent(vector3, 48, -1, 93);
            } else {
                entityLightning.setEffect(false);
            }
        }
    }

    public Vector3 adjustPosToNearbyEntity(Vector3 vector3) {
        vector3.y = this.getHighestBlockAt(vector3.getFloorX(), vector3.getFloorZ());
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(vector3.x, vector3.y, vector3.z, vector3.getX(), 255.0, vector3.getZ()).expand(3.0, 3.0, 3.0);
        ArrayList<Entity> arrayList = new ArrayList<Entity>();
        for (Entity entity : this.getCollidingEntities(axisAlignedBB)) {
            if (!entity.isAlive() || !entity.canSeeSky()) continue;
            arrayList.add(entity);
        }
        if (!arrayList.isEmpty()) {
            return ((Entity)arrayList.get(Utils.random.nextInt(arrayList.size()))).getPosition();
        }
        if (vector3.getY() == -1.0) {
            vector3 = vector3.up(2);
        }
        return vector3;
    }

    public void checkSleep() {
        int n;
        if (this.H.isEmpty()) {
            return;
        }
        boolean bl = true;
        for (Player object : this.getPlayers().values()) {
            if (object.isSleeping()) continue;
            bl = false;
            break;
        }
        if (bl && (n = this.getTime() % 24000) >= 14000 && n < 23000) {
            this.setTime(this.getTime() + 24000 - n);
            for (Player player : this.getPlayers().values()) {
                player.stopSleep();
            }
        }
    }

    public void sendBlockExtraData(int n, int n2, int n3, int n4, int n5) {
        this.sendBlockExtraData(n, n2, n3, n4, n5, this.getChunkPlayers(n >> 4, n3 >> 4).values());
    }

    public void sendBlockExtraData(int n, int n2, int n3, int n4, int n5, Collection<Player> collection) {
        this.sendBlockExtraData(n, n2, n3, n4, n5, collection.toArray(new Player[0]));
    }

    public void sendBlockExtraData(int n, int n2, int n3, int n4, int n5, Player[] playerArray) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.evid = 4000;
        levelEventPacket.x = (float)n + 0.5f;
        levelEventPacket.y = (float)n2 + 0.5f;
        levelEventPacket.z = (float)n3 + 0.5f;
        levelEventPacket.data = n5 << 8 | n4;
        Server.broadcastPacket(playerArray, (DataPacket)levelEventPacket);
    }

    public void sendBlocks(Player[] playerArray, Vector3[] vector3Array) {
        this.sendBlocks(playerArray, vector3Array, 0);
    }

    public void sendBlocks(Player[] playerArray, Vector3[] vector3Array, int n) {
        this.sendBlocks(playerArray, vector3Array, n, false);
    }

    public void sendBlocks(Player[] playerArray, Vector3[] vector3Array, int n, boolean bl) {
        LongOpenHashSet longOpenHashSet = null;
        if (bl) {
            longOpenHashSet = new LongOpenHashSet();
        }
        Int2ObjectMap<ObjectList<Player>> int2ObjectMap = Server.sortPlayers(playerArray);
        for (Vector3 vector3 : vector3Array) {
            long l;
            boolean bl2;
            if (vector3 == null) continue;
            boolean bl3 = bl2 = !bl;
            if (bl && !longOpenHashSet.contains(l = Level.chunkHash((int)vector3.x >> 4, (int)vector3.z >> 4))) {
                longOpenHashSet.add(l);
                bl2 = true;
            }
            UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
            updateBlockPacket.x = (int)vector3.x;
            updateBlockPacket.y = (int)vector3.y;
            updateBlockPacket.z = (int)vector3.z;
            updateBlockPacket.flags = bl2 ? n : 0;
            IntIterator intIterator = int2ObjectMap.keySet().iterator();
            while (intIterator.hasNext()) {
                int n2 = (Integer)intIterator.next();
                ObjectList objectList = (ObjectList)int2ObjectMap.get(n2);
                UpdateBlockPacket updateBlockPacket2 = (UpdateBlockPacket)updateBlockPacket.clone();
                try {
                    if (n2 > 201) {
                        updateBlockPacket2.blockRuntimeId = vector3 instanceof Block ? GlobalBlockPalette.getOrCreateRuntimeId(n2, ((Block)vector3).getFullId()) : GlobalBlockPalette.getOrCreateRuntimeId(n2, this.getFullBlock((int)vector3.x, (int)vector3.y, (int)vector3.z));
                    } else {
                        Block block = vector3 instanceof Block ? (Block)vector3 : this.getBlock((int)vector3.x, (int)vector3.y, (int)vector3.z);
                        updateBlockPacket2.blockId = block.getId();
                        updateBlockPacket2.blockData = block.getDamage();
                    }
                }
                catch (NoSuchElementException noSuchElementException) {
                    throw new IllegalStateException("Unable to create BlockUpdatePacket at (" + vector3.x + ", " + vector3.y + ", " + vector3.z + ") in " + this.getName() + " for players with protocol " + n2);
                }
                for (Player player : objectList) {
                    player.dataPacket(updateBlockPacket2);
                }
            }
        }
    }

    public void sendBlocks(Player player, Vector3[] vector3Array, int n) {
        for (Vector3 vector3 : vector3Array) {
            if (vector3 == null) continue;
            UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
            updateBlockPacket.x = (int)vector3.x;
            updateBlockPacket.y = (int)vector3.y;
            updateBlockPacket.z = (int)vector3.z;
            updateBlockPacket.flags = n;
            try {
                if (player.protocol > 201) {
                    updateBlockPacket.blockRuntimeId = vector3 instanceof Block ? GlobalBlockPalette.getOrCreateRuntimeId(player.protocol, ((Block)vector3).getFullId()) : GlobalBlockPalette.getOrCreateRuntimeId(player.protocol, this.getFullBlock((int)vector3.x, (int)vector3.y, (int)vector3.z));
                } else {
                    Block block = vector3 instanceof Block ? (Block)vector3 : this.getBlock((int)vector3.x, (int)vector3.y, (int)vector3.z);
                    updateBlockPacket.blockId = block.getId();
                    updateBlockPacket.blockData = block.getDamage();
                }
            }
            catch (NoSuchElementException noSuchElementException) {
                throw new IllegalStateException("Unable to create BlockUpdatePacket at (" + vector3.x + ", " + vector3.y + ", " + vector3.z + ") in " + this.getName() + " for player " + player.getName() + " with protocol " + player.protocol);
            }
            player.dataPacket(updateBlockPacket);
        }
    }

    private void a() {
        block14: {
            int n;
            int n2;
            if (this.v <= 0 || this.U.isEmpty()) {
                this.t.clear();
                return;
            }
            int n3 = Math.min(200, Math.max(1, (int)((double)(this.v - this.U.size()) / (double)this.U.size() + 0.5)));
            int n4 = 3 + n3 / 30;
            n4 = Math.min(n4, this.G);
            for (Object object : this.U.values()) {
                int n5 = NukkitMath.floorDouble(object.getX()) >> 4;
                int n6 = NukkitMath.floorDouble(object.getZ()) >> 4;
                long l = Level.chunkHash(n5, n6);
                n2 = Math.max(0, this.t.getOrDefault(l, 0));
                this.t.put(l, n2 + 1);
                for (n = 0; n < n3; ++n) {
                    int n7;
                    int n8 = Utils.random.nextInt(n4 << 1) - n4;
                    long l2 = Level.chunkHash(n8 + n5, (n7 = Utils.random.nextInt(n4 << 1) - n4) + n6);
                    if (this.t.containsKey(l2) || !this.u.isChunkLoaded(l2)) continue;
                    this.t.put(l2, -1);
                }
            }
            int n9 = 0;
            if (!this.t.isEmpty()) {
                Object object;
                object = this.t.long2IntEntrySet().iterator();
                while (object.hasNext()) {
                    int n10;
                    int n11;
                    int n12;
                    int n13;
                    Long2IntMap.Entry entry = (Long2IntMap.Entry)object.next();
                    long l = entry.getLongKey();
                    if (!this.a(l)) {
                        object.remove();
                        continue;
                    }
                    int n14 = entry.getIntValue();
                    n2 = Level.getHashX(l);
                    BaseFullChunk baseFullChunk = this.getChunk(n2, n = Level.getHashZ(l), false);
                    if (baseFullChunk == null) {
                        object.remove();
                        continue;
                    }
                    if (n14 <= 0) {
                        object.remove();
                    }
                    for (Entity entity : baseFullChunk.getEntities().values()) {
                        entity.scheduleUpdate();
                    }
                    if (!this.randomTickingEnabled()) continue;
                    if (this.M) {
                        for (ChunkSection chunkSection : ((Chunk)((Object)baseFullChunk)).getSections()) {
                            if (chunkSection instanceof EmptyChunkSection) continue;
                            n13 = chunkSection.getY();
                            for (n12 = 0; n12 < this.gameRules.getInteger(GameRule.RANDOM_TICK_SPEED); ++n12) {
                                int n15;
                                int n16;
                                n11 = ThreadLocalRandom.current().nextInt();
                                n10 = n11 & 0xF;
                                int n17 = chunkSection.getFullBlock(n10, n16 = n11 >> 16 & 0xF, n15 = n11 >> 8 & 0xF);
                                int n18 = n17 >> 4;
                                if (!randomTickBlocks[n18]) continue;
                                Block block = Block.getFast(n17, this, (n2 << 4) + n10, (n13 << 4) + n16, (n << 4) + n15);
                                block.onUpdate(2);
                            }
                        }
                        continue;
                    }
                    for (int k = 0; k < 8 && (k < 3 || n9 != 0); ++k) {
                        n9 = 0;
                        for (int i2 = 0; i2 < this.gameRules.getInteger(GameRule.RANDOM_TICK_SPEED); ++i2) {
                            int n19 = ThreadLocalRandom.current().nextInt();
                            int n20 = n19 & 0xF;
                            n13 = n19 >> 8 & 0xF;
                            n12 = n19 >> 16 & 0xF;
                            n11 = baseFullChunk.getFullBlock(n20, n12 + (k << 4), n13);
                            n10 = n11 >> 4;
                            n9 |= n11;
                            if (!randomTickBlocks[n10]) continue;
                            Block block = Block.getFast(n11, this, n20, n12 + (k << 4), n13);
                            block.onUpdate(2);
                        }
                    }
                }
            }
            if (!this.g) break block14;
            this.t.clear();
        }
    }

    public boolean save() {
        return this.save(false);
    }

    public boolean save(boolean bl) {
        block1: {
            if (!this.O && !bl) {
                return false;
            }
            this.N.getPluginManager().callEvent(new LevelSaveEvent(this));
            this.u.setTime(this.r);
            this.u.setRaining(this.c);
            this.u.setRainTime(this.C);
            this.u.setThundering(this.j);
            this.u.setThunderTime(this.k);
            this.u.setCurrentTick(this.P);
            this.u.setGameRules(this.gameRules);
            this.saveChunks();
            if (!(this.u instanceof BaseLevelProvider)) break block1;
            this.u.saveLevelData();
        }
        return true;
    }

    public void saveChunks() {
        this.u.saveChunks();
    }

    public void updateAroundRedstone(Vector3 vector3, BlockFace blockFace) {
        block2: {
            for (BlockFace blockFace2 : BlockFace.values()) {
                long l;
                if (blockFace != null && blockFace2 == blockFace) continue;
                Vector3 vector32 = vector3.getSideVec(blockFace2);
                if (this.N.unsafeRedstone) {
                    this.getBlock(vector32).onUpdate(6);
                    continue;
                }
                int n = (int)vector32.y;
                if (n < 0 || n > 255 || this.z.contains(l = Level.blockHash((int)vector32.x, n, (int)vector32.z))) continue;
                this.z.add(l);
                this.getBlock(vector32).onUpdate(6);
            }
            if (this.N.unsafeRedstone) break block2;
            this.z.clear();
        }
    }

    public void updateComparatorOutputLevel(Vector3 vector3) {
        for (BlockFace blockFace : BlockFace.Plane.HORIZONTAL) {
            Vector3 vector32 = vector3.getSideVec(blockFace);
            if (!this.isChunkLoaded((int)vector32.x >> 4, (int)vector32.z >> 4)) continue;
            Block block = this.getBlock(vector32);
            if (BlockRedstoneDiode.isDiode(block)) {
                block.onUpdate(6);
                continue;
            }
            if (!block.isNormalBlock() || !BlockRedstoneDiode.isDiode(block = this.getBlock(vector32 = vector32.getSideVec(blockFace)))) continue;
            block.onUpdate(6);
        }
    }

    public void updateAround(Vector3 vector3) {
        this.updateAround((int)vector3.x, (int)vector3.y, (int)vector3.z);
    }

    public void updateAround(int n, int n2, int n3) {
        block5: {
            BlockUpdateEvent blockUpdateEvent = new BlockUpdateEvent(this.getBlock(n, n2 - 1, n3));
            this.N.getPluginManager().callEvent(blockUpdateEvent);
            if (!blockUpdateEvent.isCancelled()) {
                this.Q.add(blockUpdateEvent.getBlock());
            }
            blockUpdateEvent = new BlockUpdateEvent(this.getBlock(n, n2 + 1, n3));
            this.N.getPluginManager().callEvent(blockUpdateEvent);
            if (!blockUpdateEvent.isCancelled()) {
                this.Q.add(blockUpdateEvent.getBlock());
            }
            blockUpdateEvent = new BlockUpdateEvent(this.getBlock(n - 1, n2, n3));
            this.N.getPluginManager().callEvent(blockUpdateEvent);
            if (!blockUpdateEvent.isCancelled()) {
                this.Q.add(blockUpdateEvent.getBlock());
            }
            blockUpdateEvent = new BlockUpdateEvent(this.getBlock(n + 1, n2, n3));
            this.N.getPluginManager().callEvent(blockUpdateEvent);
            if (!blockUpdateEvent.isCancelled()) {
                this.Q.add(blockUpdateEvent.getBlock());
            }
            blockUpdateEvent = new BlockUpdateEvent(this.getBlock(n, n2, n3 - 1));
            this.N.getPluginManager().callEvent(blockUpdateEvent);
            if (!blockUpdateEvent.isCancelled()) {
                this.Q.add(blockUpdateEvent.getBlock());
            }
            blockUpdateEvent = new BlockUpdateEvent(this.getBlock(n, n2, n3 + 1));
            this.N.getPluginManager().callEvent(blockUpdateEvent);
            if (blockUpdateEvent.isCancelled()) break block5;
            this.Q.add(blockUpdateEvent.getBlock());
        }
    }

    public void scheduleUpdate(Block block, int n) {
        this.scheduleUpdate(block, block, n, 0, true);
    }

    public void scheduleUpdate(Block block, Vector3 vector3, int n) {
        this.scheduleUpdate(block, vector3, n, 0, true);
    }

    public void scheduleUpdate(Block block, Vector3 vector3, int n, int n2) {
        this.scheduleUpdate(block, vector3, n, n2, true);
    }

    public void scheduleUpdate(Block block, Vector3 vector3, int n, int n2, boolean bl) {
        block1: {
            if (block.getId() == 0 || bl && !this.isChunkLoaded(block.getChunkX(), block.getChunkZ())) {
                return;
            }
            BlockUpdateEntry blockUpdateEntry = new BlockUpdateEntry(vector3.floor(), block, (long)n + this.P, n2);
            if (this.q.contains(blockUpdateEntry)) break block1;
            this.q.add(blockUpdateEntry);
        }
    }

    public boolean cancelSheduledUpdate(Vector3 vector3, Block block) {
        return this.q.remove(new BlockUpdateEntry(vector3, block));
    }

    public boolean isUpdateScheduled(Vector3 vector3, Block block) {
        return this.q.contains(new BlockUpdateEntry(vector3, block));
    }

    public boolean isBlockTickPending(Vector3 vector3, Block block) {
        return this.q.isBlockTickPending(vector3, block);
    }

    public Set<BlockUpdateEntry> getPendingBlockUpdates(FullChunk fullChunk) {
        int n = (fullChunk.getX() << 4) - 2;
        int n2 = n + 18;
        int n3 = (fullChunk.getZ() << 4) - 2;
        int n4 = n3 + 18;
        return this.getPendingBlockUpdates(new AxisAlignedBB(n, 0.0, n3, n2, 256.0, n4));
    }

    public Set<BlockUpdateEntry> getPendingBlockUpdates(AxisAlignedBB axisAlignedBB) {
        return this.q.getPendingBlockUpdates(axisAlignedBB);
    }

    public Block[] getCollisionBlocks(AxisAlignedBB axisAlignedBB) {
        return this.getCollisionBlocks(axisAlignedBB, false);
    }

    public Block[] getCollisionBlocks(AxisAlignedBB axisAlignedBB, boolean bl) {
        int n = NukkitMath.floorDouble(axisAlignedBB.minX);
        int n2 = NukkitMath.floorDouble(axisAlignedBB.minY);
        int n3 = NukkitMath.floorDouble(axisAlignedBB.minZ);
        int n4 = NukkitMath.ceilDouble(axisAlignedBB.maxX);
        int n5 = NukkitMath.ceilDouble(axisAlignedBB.maxY);
        int n6 = NukkitMath.ceilDouble(axisAlignedBB.maxZ);
        ArrayList<Block> arrayList = new ArrayList<Block>();
        if (bl) {
            for (int k = n3; k <= n6; ++k) {
                for (int i2 = n; i2 <= n4; ++i2) {
                    for (int i3 = n2; i3 <= n5; ++i3) {
                        Block block = this.getBlock(i2, i3, k, false);
                        if (block == null || block.getId() == 0 || !block.collidesWithBB(axisAlignedBB)) continue;
                        return new Block[]{block};
                    }
                }
            }
        } else {
            for (int k = n3; k <= n6; ++k) {
                for (int i4 = n; i4 <= n4; ++i4) {
                    for (int i5 = n2; i5 <= n5; ++i5) {
                        Block block = this.getBlock(i4, i5, k, false);
                        if (block == null || block.getId() == 0 || !block.collidesWithBB(axisAlignedBB)) continue;
                        arrayList.add(block);
                    }
                }
            }
        }
        return arrayList.toArray(new Block[0]);
    }

    public boolean hasCollisionBlocks(AxisAlignedBB axisAlignedBB) {
        int n = NukkitMath.floorDouble(axisAlignedBB.minX);
        int n2 = NukkitMath.floorDouble(axisAlignedBB.minY);
        int n3 = NukkitMath.floorDouble(axisAlignedBB.minZ);
        int n4 = NukkitMath.ceilDouble(axisAlignedBB.maxX);
        int n5 = NukkitMath.ceilDouble(axisAlignedBB.maxY);
        int n6 = NukkitMath.ceilDouble(axisAlignedBB.maxZ);
        for (int k = n3; k <= n6; ++k) {
            for (int i2 = n; i2 <= n4; ++i2) {
                for (int i3 = n2; i3 <= n5; ++i3) {
                    Block block = this.getBlock(i2, i3, k, false);
                    if (block == null || block.getId() == 0 || !block.collidesWithBB(axisAlignedBB)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFullBlock(Vector3 vector3) {
        AxisAlignedBB axisAlignedBB;
        if (vector3 instanceof Block) {
            if (((Block)vector3).isSolid()) {
                return true;
            }
            axisAlignedBB = ((Block)vector3).getBoundingBox();
        } else {
            axisAlignedBB = this.getBlock(vector3).getBoundingBox();
        }
        return axisAlignedBB != null && axisAlignedBB.getAverageEdgeLength() >= 1.0;
    }

    public AxisAlignedBB[] getCollisionCubes(Entity entity, AxisAlignedBB axisAlignedBB) {
        return this.getCollisionCubes(entity, axisAlignedBB, true);
    }

    public AxisAlignedBB[] getCollisionCubes(Entity entity, AxisAlignedBB axisAlignedBB, boolean bl) {
        return this.getCollisionCubes(entity, axisAlignedBB, bl, false);
    }

    public AxisAlignedBB[] getCollisionCubes(Entity entity, AxisAlignedBB axisAlignedBB, boolean bl, boolean bl2) {
        if (entity.noClip) {
            return new AxisAlignedBB[0];
        }
        int n = NukkitMath.floorDouble(axisAlignedBB.minX);
        int n2 = NukkitMath.floorDouble(axisAlignedBB.minY);
        int n3 = NukkitMath.floorDouble(axisAlignedBB.minZ);
        int n4 = NukkitMath.ceilDouble(axisAlignedBB.maxX);
        int n5 = NukkitMath.ceilDouble(axisAlignedBB.maxY);
        int n6 = NukkitMath.ceilDouble(axisAlignedBB.maxZ);
        ArrayList<AxisAlignedBB> arrayList = new ArrayList<AxisAlignedBB>();
        for (int k = n3; k <= n6; ++k) {
            for (int i2 = n; i2 <= n4; ++i2) {
                for (int i3 = n2; i3 <= n5; ++i3) {
                    Block position = this.getBlock(entity.chunk, i2, i3, k, false);
                    if (position.canPassThrough() || !position.collidesWithBB(axisAlignedBB)) continue;
                    arrayList.add(position.getBoundingBox());
                }
            }
        }
        if (bl || bl2) {
            for (Entity entity2 : this.getCollidingEntities(axisAlignedBB.grow(0.25, 0.25, 0.25), entity)) {
                if (!bl2 || entity2.canPassThrough()) continue;
                arrayList.add(entity2.boundingBox.clone());
            }
        }
        return arrayList.toArray(new AxisAlignedBB[0]);
    }

    public boolean hasCollision(Entity entity, AxisAlignedBB axisAlignedBB, boolean bl) {
        int n = NukkitMath.floorDouble(axisAlignedBB.minX);
        int n2 = NukkitMath.floorDouble(axisAlignedBB.minY);
        int n3 = NukkitMath.floorDouble(axisAlignedBB.minZ);
        int n4 = NukkitMath.ceilDouble(axisAlignedBB.maxX);
        int n5 = NukkitMath.ceilDouble(axisAlignedBB.maxY);
        int n6 = NukkitMath.ceilDouble(axisAlignedBB.maxZ);
        for (int k = n3; k <= n6; ++k) {
            for (int i2 = n; i2 <= n4; ++i2) {
                for (int i3 = n2; i3 <= n5; ++i3) {
                    Block block = this.getBlock(entity.chunk, i2, i3, k, false);
                    if (block.canPassThrough() || !block.collidesWithBB(axisAlignedBB)) continue;
                    return true;
                }
            }
        }
        if (bl) {
            return this.getCollidingEntities(axisAlignedBB.grow(0.25, 0.25, 0.25), entity).length > 0;
        }
        return false;
    }

    public int getFullLight(Vector3 vector3) {
        BaseFullChunk baseFullChunk = this.getChunk((int)vector3.x >> 4, (int)vector3.z >> 4, false);
        int n = 0;
        if (baseFullChunk != null) {
            n = baseFullChunk.getBlockSkyLight((int)vector3.x & 0xF, (int)vector3.y & 0xFF, (int)vector3.z & 0xF);
            if ((n = (int)((float)n - this.skyLightSubtracted)) < 15) {
                n = Math.max(baseFullChunk.getBlockLight((int)vector3.x & 0xF, (int)vector3.y & 0xFF, (int)vector3.z & 0xF), n);
            }
        }
        return n;
    }

    public int calculateSkylightSubtracted(float f2) {
        float f3 = 1.0f - (MathHelper.cos(this.calculateCelestialAngle(this.getTime(), f2) * ((float)Math.PI * 2)) * 2.0f + 0.5f);
        f3 = f3 < 0.0f ? 0.0f : (f3 > 1.0f ? 1.0f : f3);
        f3 = 1.0f - f3;
        f3 = (float)((double)f3 * ((double)(this.c ? 1 : 0) - 0.3125));
        f3 = (float)((double)f3 * ((double)(this.isThundering() ? 1 : 0) - 0.3125));
        f3 = 1.0f - f3;
        return (int)(f3 * 11.0f);
    }

    public float calculateCelestialAngle(int n, float f2) {
        float f3 = ((float)n + f2) / 24000.0f - 0.25f;
        if (f3 < 0.0f) {
            f3 += 1.0f;
        }
        if (f3 > 1.0f) {
            f3 -= 1.0f;
        }
        float f4 = 1.0f - (float)((Math.cos((double)f3 * Math.PI) + 1.0) / 2.0);
        f3 += (f4 - f3) / 3.0f;
        return f3;
    }

    public int getMoonPhase(long l) {
        return (int)(l / 24000L % 8L + 8L) % 8;
    }

    public int getFullBlock(int n, int n2, int n3) {
        return this.getChunk(n >> 4, n3 >> 4, false).getFullBlock(n & 0xF, n2 & 0xFF, n3 & 0xF);
    }

    public synchronized Block getBlock(Vector3 vector3) {
        return this.getBlock(vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ());
    }

    public synchronized Block getBlock(Vector3 vector3, boolean bl) {
        return this.getBlock(vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ(), bl);
    }

    public synchronized Block getBlock(int n, int n2, int n3) {
        return this.getBlock(n, n2, n3, true);
    }

    public synchronized Block getBlock(int n, int n2, int n3, boolean bl) {
        return this.getBlock(null, n, n2, n3, bl);
    }

    public synchronized Block getBlock(FullChunk fullChunk, int n, int n2, int n3, boolean bl) {
        int n4;
        if (n2 >= 0 && n2 < 256) {
            int n5 = n >> 4;
            int n6 = n3 >> 4;
            if (fullChunk == null || n5 != fullChunk.getX() || n6 != fullChunk.getZ()) {
                fullChunk = bl ? this.getChunk(n5, n6) : this.getChunkIfLoaded(n5, n6);
            }
            n4 = fullChunk == null ? 0 : fullChunk.getFullBlock(n & 0xF, n2, n3 & 0xF);
        } else {
            n4 = 0;
        }
        Block block = Block.fullList[n4].clone();
        block.x = n;
        block.y = n2;
        block.z = n3;
        block.level = this;
        return block;
    }

    public void updateAllLight(Vector3 vector3) {
        this.updateBlockSkyLight((int)vector3.x, (int)vector3.y, (int)vector3.z);
        this.addLightUpdate((int)vector3.x, (int)vector3.y, (int)vector3.z);
    }

    public void updateBlockSkyLight(int n, int n2, int n3) {
    }

    public void updateBlockLight(Map<Long, Map<Character, Object>> map) {
        int n;
        int n2;
        int n3;
        long l;
        Object object;
        int n4 = map.size();
        if (n4 == 0) {
            return;
        }
        ConcurrentLinkedQueue<Long> concurrentLinkedQueue = new ConcurrentLinkedQueue<Long>();
        ConcurrentLinkedQueue<Object[]> concurrentLinkedQueue2 = new ConcurrentLinkedQueue<Object[]>();
        Long2ObjectOpenHashMap<Object> long2ObjectOpenHashMap = new Long2ObjectOpenHashMap<Object>();
        Long2ObjectOpenHashMap<Object> long2ObjectOpenHashMap2 = new Long2ObjectOpenHashMap<Object>();
        Iterator<Map.Entry<Long, Map<Character, Object>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext() && n4-- > 0) {
            object = iterator.next();
            iterator.remove();
            l = (Long)object.getKey();
            Map map2 = (Map)object.getValue();
            n3 = Level.getHashX(l);
            n2 = Level.getHashZ(l);
            n = n3 << 4;
            int n5 = n2 << 4;
            Iterator iterator2 = map2.keySet().iterator();
            while (iterator2.hasNext()) {
                int n6;
                int n7;
                int n8;
                int n9;
                char c2 = ((Character)iterator2.next()).charValue();
                byte by = (byte)(c2 >>> 8);
                byte by2 = (byte)c2;
                int n10 = by2 & 0xFF;
                int n11 = (by & 0xF) + n;
                int n12 = (by >> 4 & 0xF) + n5;
                BaseFullChunk baseFullChunk = this.getChunk(n11 >> 4, n12 >> 4, false);
                if (baseFullChunk == null || (n9 = baseFullChunk.getBlockLight(n8 = n11 & 0xF, n10, n7 = n12 & 0xF)) == (n6 = Block.light[baseFullChunk.getBlockId(n8, n10, n7)])) continue;
                this.setBlockLightAt(n11, n10, n12, n6);
                long l2 = Hash.hashBlock(n11, n10, n12);
                if (n6 < n9) {
                    long2ObjectOpenHashMap2.put(l2, this.n);
                    concurrentLinkedQueue2.add(new Object[]{l2, n9});
                    continue;
                }
                long2ObjectOpenHashMap.put(l2, this.n);
                concurrentLinkedQueue.add(l2);
            }
        }
        while (!concurrentLinkedQueue2.isEmpty()) {
            object = (Object[])concurrentLinkedQueue2.poll();
            l = (Long)object[0];
            int n13 = Hash.hashBlockX(l);
            n3 = Hash.hashBlockY(l);
            n2 = Hash.hashBlockZ(l);
            n = (Integer)object[1];
            this.a(n13 - 1, n3, n2, n, concurrentLinkedQueue2, concurrentLinkedQueue, long2ObjectOpenHashMap2, long2ObjectOpenHashMap);
            this.a(n13 + 1, n3, n2, n, concurrentLinkedQueue2, concurrentLinkedQueue, long2ObjectOpenHashMap2, long2ObjectOpenHashMap);
            this.a(n13, n3 - 1, n2, n, concurrentLinkedQueue2, concurrentLinkedQueue, long2ObjectOpenHashMap2, long2ObjectOpenHashMap);
            this.a(n13, n3 + 1, n2, n, concurrentLinkedQueue2, concurrentLinkedQueue, long2ObjectOpenHashMap2, long2ObjectOpenHashMap);
            this.a(n13, n3, n2 - 1, n, concurrentLinkedQueue2, concurrentLinkedQueue, long2ObjectOpenHashMap2, long2ObjectOpenHashMap);
            this.a(n13, n3, n2 + 1, n, concurrentLinkedQueue2, concurrentLinkedQueue, long2ObjectOpenHashMap2, long2ObjectOpenHashMap);
        }
        while (!concurrentLinkedQueue.isEmpty()) {
            int n14;
            long l3 = (Long)concurrentLinkedQueue.poll();
            int n15 = Hash.hashBlockX(l3);
            n2 = this.getBlockLightAt(n15, n14 = Hash.hashBlockY(l3), n3 = Hash.hashBlockZ(l3)) - Block.lightFilter[this.getBlockIdAt(n15, n14, n3)];
            if (n2 < 1) continue;
            this.a(n15 - 1, n14, n3, n2, concurrentLinkedQueue, long2ObjectOpenHashMap);
            this.a(n15 + 1, n14, n3, n2, concurrentLinkedQueue, long2ObjectOpenHashMap);
            this.a(n15, n14 - 1, n3, n2, concurrentLinkedQueue, long2ObjectOpenHashMap);
            this.a(n15, n14 + 1, n3, n2, concurrentLinkedQueue, long2ObjectOpenHashMap);
            this.a(n15, n14, n3 - 1, n2, concurrentLinkedQueue, long2ObjectOpenHashMap);
            this.a(n15, n14, n3 + 1, n2, concurrentLinkedQueue, long2ObjectOpenHashMap);
        }
    }

    private void a(int n, int n2, int n3, int n4, Queue<Object[]> queue, Queue<Long> queue2, Map<Long, Object> map, Map<Long, Object> map2) {
        block1: {
            long l;
            int n5;
            block0: {
                long l2;
                n5 = this.getBlockLightAt(n, n2, n3);
                if (n5 == 0 || n5 >= n4) break block0;
                this.setBlockLightAt(n, n2, n3, 0);
                if (n5 <= 1 || map.containsKey(l2 = Hash.hashBlock(n, n2, n3))) break block1;
                map.put(l2, this.n);
                queue.add(new Object[]{l2, n5});
                break block1;
            }
            if (n5 < n4 || map2.containsKey(l = Hash.hashBlock(n, n2, n3))) break block1;
            map2.put(l, this.n);
            queue2.add(l);
        }
    }

    private void a(int n, int n2, int n3, int n4, Queue<Long> queue, Map<Long, Object> map) {
        block2: {
            int n5 = this.getBlockLightAt(n, n2, n3);
            if (n5 >= n4 - 1) break block2;
            this.setBlockLightAt(n, n2, n3, n4);
            long l = Hash.hashBlock(n, n2, n3);
            if (!map.containsKey(l)) {
                map.put(l, this.n);
                if (n4 > 1) {
                    queue.add(l);
                }
            }
        }
    }

    public void addLightUpdate(int n, int n2, int n3) {
        long l = Level.chunkHash(n >> 4, n3 >> 4);
        Map<Character, Object> map = this.i.get(l);
        if (map == null) {
            map = new ConcurrentHashMap<Character, Object>(8, 0.9f, 1);
            this.i.put(l, map);
        }
        map.put(Character.valueOf(Level.localBlockHash(n, n2, n3)), this.n);
    }

    @Override
    public synchronized void setBlockFullIdAt(int n, int n2, int n3, int n4) {
        this.setBlock(n, n2, n3, Block.fullList[n4], false, false);
    }

    public synchronized boolean setBlock(Vector3 vector3, Block block) {
        return this.setBlock(vector3, block, false);
    }

    public synchronized boolean setBlock(Vector3 vector3, Block block, boolean bl) {
        return this.setBlock(vector3, block, bl, true);
    }

    public synchronized boolean setBlock(Vector3 vector3, Block block, boolean bl, boolean bl2) {
        return this.setBlock(vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ(), block, bl, bl2);
    }

    public synchronized boolean setBlock(int n, int n2, int n3, Block block, boolean bl, boolean bl2) {
        return this.setBlock(n, n2, n3, block, bl, bl2, true);
    }

    public synchronized boolean setBlock(int n, int n2, int n3, Block block, boolean bl, boolean bl2, boolean bl3) {
        if (n2 < 0 || n2 >= 256) {
            return false;
        }
        BaseFullChunk baseFullChunk = this.getChunk(n >> 4, n3 >> 4, true);
        Block block2 = baseFullChunk.getAndSetBlock(n & 0xF, n2, n3 & 0xF, block);
        if (block2.getFullId() == block.getFullId()) {
            return false;
        }
        block.x = n;
        block.y = n2;
        block.z = n3;
        block.level = this;
        int n4 = n >> 4;
        int n5 = n3 >> 4;
        if (bl3) {
            if (bl) {
                this.sendBlocks(this.getChunkPlayers(n4, n5).values().toArray(new Player[0]), (Vector3[])new Block[]{block}, 11);
            } else {
                this.a(Level.chunkHash(n4, n5), n, n2, n3);
            }
        }
        for (ChunkLoader chunkLoader : this.getChunkLoaders(n4, n5)) {
            chunkLoader.onBlockChanged(block);
        }
        if (bl2) {
            if (block2.isTransparent() != block.isTransparent() || block2.getLightLevel() != block.getLightLevel()) {
                this.addLightUpdate(n, n2, n3);
            }
            BlockUpdateEvent blockUpdateEvent = new BlockUpdateEvent(block);
            this.N.getPluginManager().callEvent(blockUpdateEvent);
            if (!blockUpdateEvent.isCancelled()) {
                for (Entity entity : this.getNearbyEntities(new AxisAlignedBB(n - 1, n2 - 1, n3 - 1, n + 1, n2 + 1, n3 + 1))) {
                    entity.scheduleUpdate();
                }
                block = blockUpdateEvent.getBlock();
                block.onUpdate(1);
                this.antiXrayOnBlockChange(block);
                this.updateAround(n, n2, n3);
            }
        }
        return true;
    }

    private void a(int n, int n2, int n3) {
        long l = Level.chunkHash(n >> 4, n3 >> 4);
        this.a(l, n, n2, n3);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void a(long l2, int n, int n2, int n3) {
        Long2ObjectOpenHashMap<SoftReference<Map<Character, Object>>> long2ObjectOpenHashMap = this.w;
        synchronized (long2ObjectOpenHashMap) {
            SoftReference softReference = this.w.computeIfAbsent(l2, l -> new SoftReference(new HashMap()));
            Map map = (Map)softReference.get();
            if (map != this.S && map != null) {
                if (map.size() > 512) {
                    this.w.put(l2, new SoftReference<Map<Character, Object>>(this.S));
                } else {
                    map.put(Character.valueOf(Level.localBlockHash(n, n2, n3)), this.n);
                }
            }
        }
    }

    public void dropItem(Vector3 vector3, Item item) {
        this.dropItem(vector3, item, null);
    }

    public void dropItem(Vector3 vector3, Item item, Vector3 vector32) {
        this.dropItem(vector3, item, vector32, 10);
    }

    public void dropItem(Vector3 vector3, Item item, Vector3 vector32, int n) {
        this.dropItem(vector3, item, vector32, false, n);
    }

    public void dropItem(Vector3 vector3, Item item, Vector3 vector32, boolean bl, int n) {
        if (item.getId() != 0 && item.getCount() > 0) {
            if (vector32 == null) {
                if (bl) {
                    float f2 = ThreadLocalRandom.current().nextFloat() * 0.5f;
                    float f3 = ThreadLocalRandom.current().nextFloat() * ((float)Math.PI * 2);
                    vector32 = new Vector3(-MathHelper.sin(f3) * f2, 0.2f, MathHelper.cos(f3) * f2);
                } else {
                    vector32 = new Vector3(Utils.random.nextDouble() * 0.2 - 0.1, 0.2, Utils.random.nextDouble() * 0.2 - 0.1);
                }
            }
            CompoundTag compoundTag = NBTIO.putItemHelper(item);
            compoundTag.setName("Item");
            EntityItem entityItem = new EntityItem(this.getChunk(vector3.getChunkX(), vector3.getChunkZ(), true), new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", vector3.getX())).add(new DoubleTag("", vector3.getY())).add(new DoubleTag("", vector3.getZ()))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", vector32.x)).add(new DoubleTag("", vector32.y)).add(new DoubleTag("", vector32.z))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", ThreadLocalRandom.current().nextFloat() * 360.0f)).add(new FloatTag("", 0.0f))).putShort("Health", 5).putCompound("Item", compoundTag).putShort("PickupDelay", n));
            entityItem.spawnToAll();
        }
    }

    public EntityItem dropAndGetItem(Vector3 vector3, Item item) {
        return this.dropAndGetItem(vector3, item, null);
    }

    public EntityItem dropAndGetItem(Vector3 vector3, Item item, Vector3 vector32) {
        return this.dropAndGetItem(vector3, item, vector32, 10);
    }

    public EntityItem dropAndGetItem(Vector3 vector3, Item item, Vector3 vector32, int n) {
        return this.dropAndGetItem(vector3, item, vector32, false, n);
    }

    public EntityItem dropAndGetItem(Vector3 vector3, Item item, Vector3 vector32, boolean bl, int n) {
        if (item.getId() != 0 && item.getCount() > 0) {
            if (vector32 == null) {
                if (bl) {
                    float f2 = ThreadLocalRandom.current().nextFloat() * 0.5f;
                    float f3 = ThreadLocalRandom.current().nextFloat() * ((float)Math.PI * 2);
                    vector32 = new Vector3(-MathHelper.sin(f3) * f2, 0.2f, MathHelper.cos(f3) * f2);
                } else {
                    vector32 = new Vector3(Utils.random.nextDouble() * 0.2 - 0.1, 0.2, Utils.random.nextDouble() * 0.2 - 0.1);
                }
            }
            CompoundTag compoundTag = NBTIO.putItemHelper(item);
            compoundTag.setName("Item");
            EntityItem entityItem = new EntityItem(this.getChunk(vector3.getChunkX(), vector3.getChunkZ(), true), new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", vector3.getX())).add(new DoubleTag("", vector3.getY())).add(new DoubleTag("", vector3.getZ()))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", vector32.x)).add(new DoubleTag("", vector32.y)).add(new DoubleTag("", vector32.z))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", ThreadLocalRandom.current().nextFloat() * 360.0f)).add(new FloatTag("", 0.0f))).putShort("Health", 5).putCompound("Item", compoundTag).putShort("PickupDelay", n));
            entityItem.spawnToAll();
            return entityItem;
        }
        return null;
    }

    public Item useBreakOn(Vector3 vector3) {
        return this.useBreakOn(vector3, null);
    }

    public Item useBreakOn(Vector3 vector3, Item item) {
        return this.useBreakOn(vector3, item, null);
    }

    public Item useBreakOn(Vector3 vector3, Item item, Player player) {
        return this.useBreakOn(vector3, item, player, false);
    }

    public Item useBreakOn(Vector3 vector3, Item item, Player player, boolean bl) {
        return this.useBreakOn(vector3, null, item, player, bl);
    }

    public Item useBreakOn(Vector3 vector3, BlockFace blockFace, Item item, Player player, boolean bl) {
        Item[] itemArray;
        Object object;
        int n;
        boolean bl2;
        if (player != null && player.getGamemode() > 2) {
            return null;
        }
        Block block = this.getBlock(vector3);
        int n2 = block.getDropExp();
        if (item == null) {
            item = new ItemBlock(Block.get(0), 0, 0);
        }
        boolean bl3 = bl2 = item.isTool() && item.hasEnchantment(16);
        if (player != null) {
            if (player.getGamemode() == 2) {
                Tag tag = item.getNamedTagEntry("CanDestroy");
                n = 0;
                if (tag instanceof ListTag) {
                    for (Tag tag2 : ((ListTag)tag).getAll()) {
                        Item item2;
                        if (!(tag2 instanceof StringTag) || (item2 = Item.fromString(((StringTag)tag2).data)).getId() == 0 || item2.getBlockUnsafe() == null || item2.getBlockUnsafe().getId() != block.getId()) continue;
                        n = 1;
                        break;
                    }
                }
                if (n == 0) {
                    return null;
                }
            }
            double d2 = block.getBreakTime(item, player);
            object = !player.isSurvival() ? new Item[]{} : (bl2 && block.canSilkTouch() ? new Item[]{block.toItem()} : block.getDrops(item));
            long l = System.currentTimeMillis();
            int n3 = (!this.N.suomiCraftPEMode() || !item.isTool() || !item.hasEnchantment(15) && !player.hasEffect(3)) && player.lastBreak + (long)(d2 * 1000.0) > l ? 1 : 0;
            BlockBreakEvent object2 = new BlockBreakEvent(player, block, blockFace, item, (Item[])object, player.isCreative(), n3 != 0);
            if ((player.isSurvival() || player.isAdventure()) && !block.isBreakable(item)) {
                object2.setCancelled(true);
            } else if (!player.isOp() && this.isInSpawnRadius(block)) {
                object2.setCancelled(true);
            } else if (!object2.getInstaBreak() && object2.isFastBreak()) {
                object2.setCancelled(true);
            }
            player.lastBreak = l;
            this.N.getPluginManager().callEvent(object2);
            if (object2.isCancelled()) {
                return null;
            }
            if (block instanceof BlockBed) {
                itemArray = (block.getDamage() & 8) == 8 ? object2.getDrops() : new Item[]{};
                ((BlockBed)block).canDropItem = object2.getDrops().length != 0;
            } else {
                itemArray = object2.getDrops();
            }
            n2 = object2.getDropExp();
        } else {
            if (!block.isBreakable(item)) {
                return null;
            }
            itemArray = item.hasEnchantment(16) ? new Item[]{block.toItem()} : block.getDrops(item);
        }
        Vector3 vector32 = new Vector3(block.x, block.y + 1.0, block.z);
        n = this.getBlockIdAt((int)vector32.x, (int)vector32.y, (int)vector32.z);
        if (n == 51 || n == 492) {
            this.setBlock(vector32, Block.get(0), true);
        }
        if (bl) {
            object = this.getChunkPlayers(block.getChunkX(), block.getChunkZ());
            this.addParticle((Particle)new DestroyBlockParticle(block.add(0.5), block), object.values());
        }
        if ((object = this.getBlockEntity(block)) != null) {
            ((BlockEntity)object).onBreak();
            ((BlockEntity)object).close();
            this.updateComparatorOutputLevel(block);
        }
        block.onBreak(item, player);
        item.useOn(block);
        if (item.isTool() && item.getDamage() >= item.getMaxDurability()) {
            this.addSound((Vector3)block, Sound.RANDOM_BREAK);
            this.addParticle(new ItemBreakParticle(block, item));
            item = new ItemBlock(Block.get(0), 0, 0);
        }
        if (this.gameRules.getBoolean(GameRule.DO_TILE_DROPS)) {
            if (!bl2 && player != null && itemArray.length != 0 && (player.isSurvival() || player.isAdventure())) {
                this.dropExpOrb(vector3.add(0.5, 0.5, 0.5), n2);
            }
            if (player == null || player.isSurvival() || player.isAdventure()) {
                for (Item item2 : itemArray) {
                    if (item2.getCount() <= 0) continue;
                    this.dropItem(vector3.add(0.5, 0.5, 0.5), item2);
                }
            }
        }
        return item;
    }

    public void antiXrayOnBlockChange(Vector3 vector3) {
        if (this.antiXrayEnabled()) {
            for (Vector3 vector32 : new Vector3[]{vector3.add(1.0), vector3.add(-1.0), vector3.add(0.0, 1.0), vector3.add(0.0, -1.0), vector3.add(0.0, 0.0, 1.0), vector3.add(0.0, 0.0, -1.0)}) {
                int n;
                int n2;
                int n3;
                int n4 = vector32.getFloorY();
                if (n4 > 255 || n4 < 0 || !xrayableBlocks[n3 = this.getBlockIdAt(n2 = vector32.getFloorX(), n4, n = vector32.getFloorZ())]) continue;
                Int2ObjectMap<ObjectList<Player>> int2ObjectMap = Server.sortPlayers(this.getChunkPlayers(vector32.getChunkX(), vector32.getChunkZ()).values().toArray(new Player[0]));
                IntIterator intIterator = int2ObjectMap.keySet().iterator();
                while (intIterator.hasNext()) {
                    int n5 = (Integer)intIterator.next();
                    UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
                    updateBlockPacket.x = n2;
                    updateBlockPacket.y = n4;
                    updateBlockPacket.z = n;
                    updateBlockPacket.flags = 11;
                    try {
                        updateBlockPacket.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(n5, this.getFullBlock(n2, n4, n));
                    }
                    catch (Exception exception) {
                        try {
                            updateBlockPacket.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(n5, n3, 0);
                        }
                        catch (Exception exception2) {
                            continue;
                        }
                    }
                    ObjectList objectList = (ObjectList)int2ObjectMap.get(n5);
                    for (Player player : objectList) {
                        player.dataPacket(updateBlockPacket);
                    }
                }
            }
        }
    }

    public void dropExpOrb(Vector3 vector3, int n) {
        this.dropExpOrb(vector3, n, null);
    }

    public void dropExpOrb(Vector3 vector3, int n, Vector3 vector32) {
        this.dropExpOrb(vector3, n, vector32, 10);
    }

    public void dropExpOrb(Vector3 vector3, int n, Vector3 vector32, int n2) {
        if (n > 0) {
            ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
            if (this.N.suomiCraftPEMode()) {
                CompoundTag compoundTag = Entity.getDefaultNBT(vector3, vector32 == null ? new Vector3((((Random)threadLocalRandom).nextDouble() * 0.2 - 0.1) * 2.0, ((Random)threadLocalRandom).nextDouble() * 0.4, (((Random)threadLocalRandom).nextDouble() * 0.2 - 0.1) * 2.0) : vector32, ((Random)threadLocalRandom).nextFloat() * 360.0f, 0.0f);
                compoundTag.putShort("Value", n);
                compoundTag.putShort("PickupDelay", n2);
                Entity entity = Entity.createEntity("XpOrb", (FullChunk)this.getChunk(vector3.getChunkX(), vector3.getChunkZ()), compoundTag, new Object[0]);
                if (entity != null) {
                    entity.spawnToAll();
                }
            } else {
                for (int n3 : EntityXPOrb.splitIntoOrbSizes(n)) {
                    CompoundTag compoundTag = Entity.getDefaultNBT(vector3, vector32 == null ? new Vector3((((Random)threadLocalRandom).nextDouble() * 0.2 - 0.1) * 2.0, ((Random)threadLocalRandom).nextDouble() * 0.4, (((Random)threadLocalRandom).nextDouble() * 0.2 - 0.1) * 2.0) : vector32, ((Random)threadLocalRandom).nextFloat() * 360.0f, 0.0f);
                    compoundTag.putShort("Value", n3);
                    compoundTag.putShort("PickupDelay", n2);
                    Entity.createEntity("XpOrb", (FullChunk)this.getChunk(vector3.getChunkX(), vector3.getChunkZ()), compoundTag, new Object[0]).spawnToAll();
                }
            }
        }
    }

    public Item useItemOn(Vector3 vector3, Item item, BlockFace blockFace, float f2, float f3, float f4) {
        return this.useItemOn(vector3, item, blockFace, f2, f3, f4, null);
    }

    public Item useItemOn(Vector3 vector3, Item item, BlockFace blockFace, float f2, float f3, float f4, Player player) {
        return this.useItemOn(vector3, item, blockFace, f2, f3, f4, player, true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Item useItemOn(Vector3 vector3, Item item, BlockFace blockFace, float f2, float f3, float f4, Player player, boolean bl) {
        Object object;
        Object object22;
        Object object3;
        Object object4;
        Block block = this.getBlock(vector3);
        Block block2 = block.getSide(blockFace);
        if (block2.y > 255.0) return null;
        if (block2.y < 0.0) {
            return null;
        }
        if (block2.y > 127.0 && this.getDimension() == 1) {
            return null;
        }
        if (block.getId() == 0) {
            return null;
        }
        if (player != null) {
            object4 = new PlayerInteractEvent(player, item, block, blockFace, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK);
            if (player.getGamemode() > 2) {
                ((Event)object4).setCancelled(true);
            }
            if (!player.isOp() && this.isInSpawnRadius(block)) {
                ((Event)object4).setCancelled(true);
            }
            this.N.getPluginManager().callEvent((Event)object4);
            if (((Event)object4).isCancelled()) return null;
            block.onUpdate(5);
            if ((!player.isSneaking() || player.getInventory().getItemInHand().isNull()) && block.canBeActivated() && block.onActivate(item, player)) {
                if (!item.isTool()) return item;
                if (item.getDamage() < item.getMaxDurability()) return item;
                this.addSound((Vector3)block, Sound.RANDOM_BREAK);
                this.addParticle(new ItemBreakParticle(block, item));
                return new ItemBlock(Block.get(0), 0, 0);
            }
            if (item.canBeActivated() && item.onActivate(this, player, block2, block, blockFace, f2, f3, f4) && item.getCount() <= 0) {
                return new ItemBlock(Block.get(0), 0, 0);
            }
        } else if (block.canBeActivated() && block.onActivate(item, null)) {
            if (!item.isTool()) return item;
            if (item.getDamage() < item.getMaxDurability()) return item;
            return new ItemBlock(Block.get(0), 0, 0);
        }
        if (this.N.disableNewBlocks && item.getBlockId() > 255) {
            return null;
        }
        if (!item.canBePlaced()) return null;
        object4 = item.getBlock();
        ((Block)object4).position(block2);
        if (!block2.canBeReplaced()) {
            if (((Block)object4).getId() != 44) return null;
            if (block2.getId() != 44) {
                return null;
            }
        }
        if (block.canBeReplaced()) {
            block2 = block;
            ((Block)object4).position(block2);
        }
        if (!((Block)object4).canPassThrough() && ((Block)object4).getBoundingBox() != null) {
            for (Object object22 : object3 = this.getCollidingEntities(((Block)object4).getBoundingBox())) {
                if (object22 == player || object22 instanceof EntityArrow || object22 instanceof EntityItem || object22 instanceof Player && ((Player)object22).isSpectator()) continue;
                if (((Entity)object22).canCollide()) return null;
            }
            if (player != null) {
                object = player.getNextPosition().subtract(player.getPosition());
                if (((Block)object4).getBoundingBox().intersectsWith(player.getBoundingBox().getOffsetBoundingBox(((Vector3)object).x, ((Vector3)object).y, ((Vector3)object).z))) {
                    this.sendBlocks(player, new Vector3[]{object4, block}, 0);
                    return null;
                }
            }
        }
        if (player != null) {
            Object object5;
            object3 = new BlockPlaceEvent(player, (Block)object4, block2, block, item);
            if (player.getGamemode() == 2) {
                object = item.getNamedTagEntry("CanPlaceOn");
                int n = 0;
                if (object instanceof ListTag) {
                    for (Object object22 : ((ListTag)object).getAll()) {
                        if (!(object22 instanceof StringTag) || ((Item)(object5 = Item.fromString(((StringTag)object22).data))).getId() == 0 || ((Item)object5).getBlockUnsafe() == null || ((Item)object5).getBlockUnsafe().getId() != block.getId()) continue;
                        n = 1;
                        break;
                    }
                }
                if (n == 0) {
                    ((Event)object3).setCancelled(true);
                }
            }
            if (!player.isOp() && this.isInSpawnRadius(block)) {
                ((Event)object3).setCancelled(true);
            }
            this.N.getPluginManager().callEvent((Event)object3);
            if (((Event)object3).isCancelled()) {
                return null;
            }
            if (this.N.mobsFromBlocks) {
                if (item.getId() == 91 || item.getId() == 86 || item.getId() == -155) {
                    if (block2.getSide(BlockFace.DOWN).getId() == 80 && block2.getSide(BlockFace.DOWN, 2).getId() == 80) {
                        block2.getLevel().setBlock(block, Block.get(0));
                        block2.getLevel().setBlock(block.add(0.0, -1.0, 0.0), Block.get(0));
                        object = new CreatureSpawnEvent(21, CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN);
                        this.N.getPluginManager().callEvent((Event)object);
                        if (((Event)object).isCancelled()) {
                            return null;
                        }
                        Entity.createEntity("SnowGolem", block.add(0.5, -1.0, 0.5), new Object[0]).spawnToAll();
                        if (player.isCreative()) return null;
                        item.setCount(item.getCount() - 1);
                        player.getInventory().setItemInHand(item);
                        return null;
                    }
                    if (block2.getSide(BlockFace.DOWN).getId() == 42 && block2.getSide(BlockFace.DOWN, 2).getId() == 42) {
                        block2 = block2.getSide(BlockFace.DOWN);
                        Block block3 = null;
                        object = block2.getSide(BlockFace.EAST);
                        if (((Block)object).getId() == 42 && (block3 = block2.getSide(BlockFace.WEST)).getId() == 42) {
                            block2.getLevel().setBlock((Vector3)object, Block.get(0));
                            block2.getLevel().setBlock(block3, Block.get(0));
                        } else {
                            object = block2.getSide(BlockFace.NORTH);
                            if (((Block)object).getId() == 42 && (block3 = block2.getSide(BlockFace.SOUTH)).getId() == 42) {
                                block2.getLevel().setBlock((Vector3)object, Block.get(0));
                                block2.getLevel().setBlock(block3, Block.get(0));
                            }
                        }
                        if (block3 != null) {
                            block2.getLevel().setBlock(block2, Block.get(0));
                            block2.getLevel().setBlock(block2.add(0.0, -1.0, 0.0), Block.get(0));
                            CreatureSpawnEvent creatureSpawnEvent = new CreatureSpawnEvent(20, CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM);
                            this.N.getPluginManager().callEvent(creatureSpawnEvent);
                            if (creatureSpawnEvent.isCancelled()) {
                                return null;
                            }
                            Entity.createEntity("IronGolem", block2.add(0.5, -1.0, 0.5), new Object[0]).spawnToAll();
                            if (player.isCreative()) return null;
                            item.setCount(item.getCount() - 1);
                            player.getInventory().setItemInHand(item);
                            return null;
                        }
                    }
                } else if (item.getId() == 397 && item.getDamage() == 1 && block2.getSide(BlockFace.DOWN).getId() == 88 && block2.getSide(BlockFace.DOWN, 2).getId() == 88) {
                    Block block4;
                    Block block5;
                    object = block2.getSide(BlockFace.EAST);
                    if (((Block)object).getId() != 144 || ((Block)object).toItem().getDamage() != 1 || (block5 = block2.getSide(BlockFace.WEST)).getId() != 144 || block5.toItem().getDamage() != 1) {
                        object = block2.getSide(BlockFace.NORTH);
                        if (((Block)object).getId() != 144) return null;
                        if (((Block)object).toItem().getDamage() != 1) return null;
                        block5 = block2.getSide(BlockFace.SOUTH);
                        if (block5.getId() != 144) return null;
                        if (block5.toItem().getDamage() != 1) {
                            return null;
                        }
                    }
                    if ((block4 = (block2 = block2.getSide(BlockFace.DOWN)).getSide(BlockFace.EAST)).getId() != 88 || ((Block)(object22 = block2.getSide(BlockFace.WEST))).getId() != 88) {
                        block4 = block2.getSide(BlockFace.NORTH);
                        if (block4.getId() != 88) return null;
                        object22 = block2.getSide(BlockFace.SOUTH);
                        if (((Block)object22).getId() != 88) {
                            return null;
                        }
                    }
                    block2.getLevel().setBlock((Vector3)object, Block.get(0));
                    block2.getLevel().setBlock(block5, Block.get(0));
                    block2.getLevel().setBlock(block4, Block.get(0));
                    block2.getLevel().setBlock((Vector3)object22, Block.get(0));
                    block2.getLevel().setBlock(block2, Block.get(0));
                    block2.getLevel().setBlock(block2.add(0.0, -1.0, 0.0), Block.get(0));
                    object5 = new CreatureSpawnEvent(52, CreatureSpawnEvent.SpawnReason.BUILD_WITHER);
                    this.N.getPluginManager().callEvent((Event)object5);
                    if (((Event)object5).isCancelled()) {
                        return null;
                    }
                    if (!player.isCreative()) {
                        item.setCount(item.getCount() - 1);
                        player.getInventory().setItemInHand(item);
                    }
                    EntityWither entityWither = (EntityWither)Entity.createEntity("Wither", block2.add(0.5, -1.0, 0.5), new Object[0]);
                    entityWither.stayTime = 220;
                    entityWither.spawnToAll();
                    this.addSound((Vector3)entityWither, Sound.MOB_WITHER_SPAWN);
                    player.awardAchievement("spawnWither");
                    return null;
                }
            }
        }
        if (!((Block)object4).place(item, block2, block, blockFace, f2, f3, f4, player)) {
            return null;
        }
        if (player != null && !player.isCreative()) {
            item.setCount(item.getCount() - 1);
        }
        if (bl) {
            object3 = Server.sortPlayers(this.getChunkPlayers(((Vector3)object4).getChunkX(), ((Vector3)object4).getChunkZ()).values());
            object = object3.keySet().iterator();
            while (object.hasNext()) {
                int n = (Integer)object.next();
                int n2 = GlobalBlockPalette.getOrCreateRuntimeId(n > 201 ? n : ProtocolInfo.CURRENT_PROTOCOL, ((Block)object4).getId(), ((Block)object4).getDamage());
                object22 = new LevelSoundEventPacket();
                ((LevelSoundEventPacket)object22).sound = 6;
                ((LevelSoundEventPacket)object22).extraData = n2;
                ((LevelSoundEventPacket)object22).entityIdentifier = "";
                ((LevelSoundEventPacket)object22).x = (float)((Block)object4).x;
                ((LevelSoundEventPacket)object22).y = (float)((Block)object4).y;
                ((LevelSoundEventPacket)object22).z = (float)((Block)object4).z;
                Server.broadcastPacket(((ObjectList)object3.get(n)).toArray(new Player[0]), (DataPacket)object22);
            }
        }
        if (item.getCount() > 0) return item;
        return new ItemBlock(Block.get(0), 0, 0);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isInSpawnRadius(Vector3 vector3) {
        if (this.N.getSpawnRadius() <= -1) return false;
        Vector2 vector2 = new Vector2(vector3.x, vector3.z);
        Position position = this.getSpawnLocation();
        Vector2 vector22 = new Vector2(position.x, position.z);
        if (!(vector2.distance(vector22) <= (double)this.N.getSpawnRadius())) return false;
        return true;
    }

    public Entity getEntity(long l) {
        return this.entities.containsKey(l) ? this.entities.get(l) : null;
    }

    public Entity[] getEntities() {
        return this.entities.values().toArray(new Entity[0]);
    }

    public Entity[] getCollidingEntities(AxisAlignedBB axisAlignedBB) {
        return this.getCollidingEntities(axisAlignedBB, null);
    }

    public Entity[] getCollidingEntities(AxisAlignedBB axisAlignedBB, Entity entity) {
        ArrayList<Entity> arrayList = new ArrayList<Entity>();
        if (entity == null || entity.canCollide()) {
            int n = NukkitMath.floorDouble((axisAlignedBB.minX - 2.0) / 16.0);
            int n2 = NukkitMath.ceilDouble((axisAlignedBB.maxX + 2.0) / 16.0);
            int n3 = NukkitMath.floorDouble((axisAlignedBB.minZ - 2.0) / 16.0);
            int n4 = NukkitMath.ceilDouble((axisAlignedBB.maxZ + 2.0) / 16.0);
            for (int k = n; k <= n2; ++k) {
                for (int i2 = n3; i2 <= n4; ++i2) {
                    for (Entity entity2 : this.getChunkEntities(k, i2, false).values()) {
                        if (entity != null && (entity2 == entity || !entity.canCollideWith(entity2)) || !entity2.boundingBox.intersectsWith(axisAlignedBB)) continue;
                        arrayList.add(entity2);
                    }
                }
            }
        }
        return arrayList.toArray(new Entity[0]);
    }

    public Entity[] getNearbyEntities(AxisAlignedBB axisAlignedBB) {
        return this.getNearbyEntities(axisAlignedBB, null);
    }

    public Entity[] getNearbyEntities(AxisAlignedBB axisAlignedBB, Entity entity) {
        return this.getNearbyEntities(axisAlignedBB, entity, false);
    }

    public Entity[] getNearbyEntities(AxisAlignedBB axisAlignedBB, Entity entity, boolean bl) {
        ArrayList<Entity> arrayList = new ArrayList<Entity>();
        int n = NukkitMath.floorDouble((axisAlignedBB.minX - 2.0) * 0.0625);
        int n2 = NukkitMath.ceilDouble((axisAlignedBB.maxX + 2.0) * 0.0625);
        int n3 = NukkitMath.floorDouble((axisAlignedBB.minZ - 2.0) * 0.0625);
        int n4 = NukkitMath.ceilDouble((axisAlignedBB.maxZ + 2.0) * 0.0625);
        for (int k = n; k <= n2; ++k) {
            for (int i2 = n3; i2 <= n4; ++i2) {
                for (Entity entity2 : this.getChunkEntities(k, i2, bl).values()) {
                    if (entity2 == entity || !entity2.boundingBox.intersectsWith(axisAlignedBB)) continue;
                    arrayList.add(entity2);
                }
            }
        }
        return arrayList.toArray(new Entity[0]);
    }

    public Map<Long, BlockEntity> getBlockEntities() {
        return this.R;
    }

    public BlockEntity getBlockEntityById(long l) {
        return this.R.containsKey(l) ? this.R.get(l) : null;
    }

    public Map<Long, Player> getPlayers() {
        return this.H;
    }

    public Map<Integer, ChunkLoader> getLoaders() {
        return this.U;
    }

    public BlockEntity getBlockEntity(Vector3 vector3) {
        return this.getBlockEntity(null, vector3);
    }

    public BlockEntity getBlockEntity(FullChunk fullChunk, Vector3 vector3) {
        int n = (int)vector3.x >> 4;
        int n2 = (int)vector3.z >> 4;
        if (fullChunk == null || n != fullChunk.getX() || n2 != fullChunk.getZ()) {
            fullChunk = this.getChunk(n, n2, false);
        }
        if (fullChunk != null) {
            return fullChunk.getTile((int)vector3.x & 0xF, (int)vector3.y & 0xFF, (int)vector3.z & 0xF);
        }
        return null;
    }

    public BlockEntity getBlockEntityIfLoaded(Vector3 vector3) {
        return this.getBlockEntityIfLoaded(null, vector3);
    }

    public BlockEntity getBlockEntityIfLoaded(FullChunk fullChunk, Vector3 vector3) {
        int n = (int)vector3.x >> 4;
        int n2 = (int)vector3.z >> 4;
        if (fullChunk == null || n != fullChunk.getX() || n2 != fullChunk.getZ()) {
            fullChunk = this.getChunkIfLoaded(n, n2);
        }
        if (fullChunk != null) {
            return fullChunk.getTile((int)vector3.x & 0xF, (int)vector3.y & 0xFF, (int)vector3.z & 0xF);
        }
        return null;
    }

    public Map<Long, Entity> getChunkEntities(int n, int n2) {
        return this.getChunkEntities(n, n2, true);
    }

    public Map<Long, Entity> getChunkEntities(int n, int n2, boolean bl) {
        BaseFullChunk baseFullChunk = bl ? this.getChunk(n, n2) : this.getChunkIfLoaded(n, n2);
        return baseFullChunk != null ? baseFullChunk.getEntities() : Collections.emptyMap();
    }

    public Map<Long, BlockEntity> getChunkBlockEntities(int n, int n2) {
        BaseFullChunk baseFullChunk = this.getChunk(n, n2);
        return baseFullChunk != null ? baseFullChunk.getBlockEntities() : Collections.emptyMap();
    }

    @Override
    public synchronized int getBlockIdAt(int n, int n2, int n3) {
        if (n2 < 0 || n2 > 255) {
            return 0;
        }
        return this.getChunk(n >> 4, n3 >> 4, true).getBlockId(n & 0xF, n2 & 0xFF, n3 & 0xF);
    }

    public synchronized int getBlockIdAt(FullChunk fullChunk, int n, int n2, int n3) {
        if (n2 < 0 || n2 > 255) {
            return 0;
        }
        int n4 = n >> 4;
        int n5 = n3 >> 4;
        if (fullChunk == null || n4 != fullChunk.getX() || n5 != fullChunk.getZ()) {
            fullChunk = this.getChunk(n4, n5, true);
        }
        return fullChunk.getBlockId(n & 0xF, n2 & 0xFF, n3 & 0xF);
    }

    @Override
    public synchronized void setBlockIdAt(int n, int n2, int n3, int n4) {
        this.getChunk(n >> 4, n3 >> 4, true).setBlockId(n & 0xF, n2 & 0xFF, n3 & 0xF, n4 & 0x1FF);
        this.a(n, n2, n3);
        this.b.setComponents(n, n2, n3);
        for (ChunkLoader chunkLoader : this.getChunkLoaders(n >> 4, n3 >> 4)) {
            chunkLoader.onBlockChanged(this.b);
        }
    }

    @Override
    public synchronized void setBlockAt(int n, int n2, int n3, int n4, int n5) {
        BaseFullChunk baseFullChunk = this.getChunk(n >> 4, n3 >> 4, true);
        baseFullChunk.setBlockId(n & 0xF, n2 & 0xFF, n3 & 0xF, n4 & 0x1FF);
        baseFullChunk.setBlockData(n & 0xF, n2 & 0xFF, n3 & 0xF, n5 & 0xF);
        this.a(n, n2, n3);
        this.b.setComponents(n, n2, n3);
        for (ChunkLoader chunkLoader : this.getChunkLoaders(n >> 4, n3 >> 4)) {
            chunkLoader.onBlockChanged(this.b);
        }
    }

    public synchronized int getBlockExtraDataAt(int n, int n2, int n3) {
        return this.getChunk(n >> 4, n3 >> 4, true).getBlockExtraData(n & 0xF, n2 & 0xFF, n3 & 0xF);
    }

    public synchronized void setBlockExtraDataAt(int n, int n2, int n3, int n4, int n5) {
        this.getChunk(n >> 4, n3 >> 4, true).setBlockExtraData(n & 0xF, n2 & 0xFF, n3 & 0xF, n5 << 8 | n4);
        this.sendBlockExtraData(n, n2, n3, n4, n5);
    }

    @Override
    public synchronized int getBlockDataAt(int n, int n2, int n3) {
        return this.getChunk(n >> 4, n3 >> 4, true).getBlockData(n & 0xF, n2 & 0xFF, n3 & 0xF);
    }

    @Override
    public synchronized void setBlockDataAt(int n, int n2, int n3, int n4) {
        this.getChunk(n >> 4, n3 >> 4, true).setBlockData(n & 0xF, n2 & 0xFF, n3 & 0xF, n4 & 0xF);
        this.a(n, n2, n3);
        this.b.setComponents(n, n2, n3);
        for (ChunkLoader chunkLoader : this.getChunkLoaders(n >> 4, n3 >> 4)) {
            chunkLoader.onBlockChanged(this.b);
        }
    }

    public synchronized int getBlockSkyLightAt(int n, int n2, int n3) {
        return this.getChunk(n >> 4, n3 >> 4, true).getBlockSkyLight(n & 0xF, n2 & 0xFF, n3 & 0xF);
    }

    public synchronized void setBlockSkyLightAt(int n, int n2, int n3, int n4) {
        this.getChunk(n >> 4, n3 >> 4, true).setBlockSkyLight(n & 0xF, n2 & 0xFF, n3 & 0xF, n4 & 0xF);
    }

    public synchronized int getBlockLightAt(int n, int n2, int n3) {
        BaseFullChunk baseFullChunk = this.getChunkIfLoaded(n >> 4, n3 >> 4);
        return baseFullChunk == null ? 0 : baseFullChunk.getBlockLight(n & 0xF, n2 & 0xFF, n3 & 0xF);
    }

    public synchronized void setBlockLightAt(int n, int n2, int n3, int n4) {
        block0: {
            BaseFullChunk baseFullChunk = this.getChunkIfLoaded(n >> 4, n3 >> 4);
            if (null == baseFullChunk) break block0;
            baseFullChunk.setBlockLight(n & 0xF, n2 & 0xFF, n3 & 0xF, n4 & 0xF);
        }
    }

    public int getBiomeId(int n, int n2) {
        return this.getChunk(n >> 4, n2 >> 4, true).getBiomeId(n & 0xF, n2 & 0xF);
    }

    public void setBiomeId(int n, int n2, int n3) {
        this.getChunk(n >> 4, n2 >> 4, true).setBiomeId(n & 0xF, n2 & 0xF, n3 & 0xF);
    }

    public void setBiomeId(int n, int n2, byte by) {
        this.getChunk(n >> 4, n2 >> 4, true).setBiomeId(n & 0xF, n2 & 0xF, by & 0xF);
    }

    public int getHeightMap(int n, int n2) {
        return this.getChunk(n >> 4, n2 >> 4, true).getHeightMap(n & 0xF, n2 & 0xF);
    }

    public void setHeightMap(int n, int n2, int n3) {
        this.getChunk(n >> 4, n2 >> 4, true).setHeightMap(n & 0xF, n2 & 0xF, n3 & 0xF);
    }

    public int getBiomeColor(int n, int n2) {
        return this.getChunk(n >> 4, n2 >> 4, true).getBiomeColor(n & 0xF, n2 & 0xF);
    }

    public void setBiomeColor(int n, int n2, int n3, int n4, int n5) {
        this.getChunk(n >> 4, n2 >> 4, true).setBiomeColor(n & 0xF, n2 & 0xF, n3, n4, n5);
    }

    public Map<Long, ? extends FullChunk> getChunks() {
        return this.u.getLoadedChunks();
    }

    @Override
    public BaseFullChunk getChunk(int n, int n2) {
        return this.getChunk(n, n2, false);
    }

    public BaseFullChunk getChunk(int n, int n2, boolean bl) {
        long l = Level.chunkHash(n, n2);
        BaseFullChunk baseFullChunk = this.u.getLoadedChunk(l);
        if (baseFullChunk == null) {
            baseFullChunk = this.a(l, n, n2, bl);
        }
        return baseFullChunk;
    }

    public BaseFullChunk getChunkIfLoaded(int n, int n2) {
        return this.u.getLoadedChunk(Level.chunkHash(n, n2));
    }

    public void generateChunkCallback(int n, int n2, BaseFullChunk baseFullChunk) {
        this.generateChunkCallback(n, n2, baseFullChunk, true);
    }

    public void generateChunkCallback(int n, int n2, BaseFullChunk baseFullChunk, boolean bl) {
        block9: {
            long l;
            if (Timings.generationCallbackTimer != null) {
                Timings.generationCallbackTimer.startTiming();
            }
            if (this.l.containsKey(l = Level.chunkHash(n, n2))) {
                BaseFullChunk baseFullChunk2 = this.getChunk(n, n2, false);
                for (int k = -1; k <= 1; ++k) {
                    for (int i2 = -1; i2 <= 1; ++i2) {
                        this.e.remove(Level.chunkHash(n + k, n2 + i2));
                    }
                }
                this.l.remove(l);
                baseFullChunk.setProvider(this.u);
                this.setChunk(n, n2, baseFullChunk, false);
                baseFullChunk = this.getChunk(n, n2, false);
                if (baseFullChunk != null && (baseFullChunk2 == null || !bl) && baseFullChunk.isPopulated() && baseFullChunk.getProvider() != null) {
                    this.N.getPluginManager().callEvent(new ChunkPopulateEvent(baseFullChunk));
                    for (ChunkLoader chunkLoader : this.getChunkLoaders(n, n2)) {
                        chunkLoader.onChunkPopulated(baseFullChunk);
                    }
                }
            } else if (this.J.containsKey(l) || this.e.containsKey(l)) {
                this.J.remove(l);
                this.e.remove(l);
                baseFullChunk.setProvider(this.u);
                this.setChunk(n, n2, baseFullChunk, false);
            } else {
                baseFullChunk.setProvider(this.u);
                this.setChunk(n, n2, baseFullChunk, false);
            }
            if (Timings.generationCallbackTimer == null) break block9;
            Timings.generationCallbackTimer.stopTiming();
        }
    }

    @Override
    public void setChunk(int n, int n2) {
        this.setChunk(n, n2, null);
    }

    @Override
    public void setChunk(int n, int n2, BaseFullChunk baseFullChunk) {
        this.setChunk(n, n2, baseFullChunk, true);
    }

    public void setChunk(int n, int n2, BaseFullChunk baseFullChunk, boolean bl) {
        if (baseFullChunk == null) {
            return;
        }
        long l = Level.chunkHash(n, n2);
        BaseFullChunk baseFullChunk2 = this.getChunk(n, n2, false);
        if (baseFullChunk2 != baseFullChunk) {
            if (bl && baseFullChunk2 != null) {
                this.unloadChunk(n, n2, false, false);
            } else {
                Position position;
                Iterator<Map.Entry<Object, Object>> iterator;
                Map<Object, Object> map;
                ChunkLoader[] chunkLoaderArray = baseFullChunk2 != null ? baseFullChunk2.getEntities() : Collections.emptyMap();
                Map<Object, Object> map2 = map = baseFullChunk2 != null ? baseFullChunk2.getBlockEntities() : Collections.emptyMap();
                if (!chunkLoaderArray.isEmpty()) {
                    iterator = chunkLoaderArray.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Object, Object> entry = iterator.next();
                        position = (Entity)entry.getValue();
                        baseFullChunk.addEntity((Entity)position);
                        if (baseFullChunk2 == null) continue;
                        iterator.remove();
                        baseFullChunk2.removeEntity((Entity)position);
                        position.chunk = baseFullChunk;
                    }
                }
                if (!map.isEmpty()) {
                    iterator = map.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Object, Object> entry = iterator.next();
                        position = (BlockEntity)entry.getValue();
                        baseFullChunk.addBlockEntity((BlockEntity)position);
                        if (baseFullChunk2 == null) continue;
                        iterator.remove();
                        baseFullChunk2.removeBlockEntity((BlockEntity)position);
                        ((BlockEntity)position).chunk = baseFullChunk;
                    }
                }
            }
            this.u.setChunk(n, n2, baseFullChunk);
        }
        baseFullChunk.setChanged();
        if (!this.isChunkInUse(l)) {
            this.unloadChunkRequest(n, n2);
        } else {
            for (ChunkLoader chunkLoader : this.getChunkLoaders(n, n2)) {
                chunkLoader.onChunkChanged(baseFullChunk);
            }
        }
    }

    public int getHighestBlockAt(int n, int n2) {
        return this.getHighestBlockAt(n, n2, true);
    }

    public int getHighestBlockAt(int n, int n2, boolean bl) {
        return this.getChunk(n >> 4, n2 >> 4, true).getHighestBlockAt(n & 0xF, n2 & 0xF, bl);
    }

    public int getHighestBlockAt(FullChunk fullChunk, int n, int n2, boolean bl) {
        int n3 = n >> 4;
        int n4 = n2 >> 4;
        if (fullChunk == null || n3 != fullChunk.getX() || n4 != fullChunk.getZ()) {
            fullChunk = this.getChunk(n3, n4, true);
        }
        return fullChunk.getHighestBlockAt(n & 0xF, n2 & 0xF, bl);
    }

    public BlockColor getMapColorAt(int n, int n2) {
        return this.getMapColorAt(n, this.getHighestBlockAt(n, n2, false), n2);
    }

    public BlockColor getMapColorAt(int n, int n2, int n3) {
        BaseFullChunk baseFullChunk = this.getChunk(n >> 4, n3 >> 4);
        for (int k = n2; k > 1; --k) {
            Block block = this.getBlock(baseFullChunk, n, k, n3, true);
            if (block instanceof BlockGrass) {
                return this.a((FullChunk)baseFullChunk, n, n3);
            }
            BlockColor blockColor = block.getColor();
            if (blockColor.getAlpha() == 0) {
                continue;
            }
            return blockColor;
        }
        return BlockColor.VOID_BLOCK_COLOR;
    }

    private BlockColor a(FullChunk fullChunk, int n, int n2) {
        int n3 = fullChunk.getBiomeId(n & 0xF, n2 & 0xF);
        switch (n3) {
            case 0: 
            case 7: 
            case 9: 
            case 24: {
                return new BlockColor("#8eb971");
            }
            case 1: 
            case 16: 
            case 129: {
                return new BlockColor("#91bd59");
            }
            case 2: 
            case 8: 
            case 17: 
            case 35: 
            case 36: 
            case 130: 
            case 163: 
            case 164: {
                return new BlockColor("#bfb755");
            }
            case 3: 
            case 20: 
            case 25: 
            case 34: 
            case 131: 
            case 162: {
                return new BlockColor("#8ab689");
            }
            case 4: 
            case 132: {
                return new BlockColor("#79c05a");
            }
            case 5: 
            case 19: 
            case 32: 
            case 33: 
            case 133: 
            case 160: {
                return new BlockColor("#86b783");
            }
            case 6: 
            case 134: {
                return new BlockColor("#6A7039");
            }
            case 10: 
            case 11: 
            case 12: 
            case 30: 
            case 31: 
            case 140: 
            case 158: {
                return new BlockColor("#80b497");
            }
            case 14: 
            case 15: {
                return new BlockColor("#55c93f");
            }
            case 18: 
            case 27: 
            case 28: 
            case 155: 
            case 156: {
                return new BlockColor("#88bb67");
            }
            case 21: 
            case 22: 
            case 149: {
                return new BlockColor("#59c93c");
            }
            case 23: 
            case 151: {
                return new BlockColor("#64c73f");
            }
            case 26: {
                return new BlockColor("#83b593");
            }
            case 29: 
            case 157: {
                return new BlockColor("#507a32");
            }
            case 37: 
            case 38: 
            case 39: 
            case 165: 
            case 166: 
            case 167: {
                return new BlockColor("#90814d");
            }
        }
        return BlockColor.GRASS_BLOCK_COLOR;
    }

    public BlockColor getWaterColorAt(int n, int n2) {
        int n3 = this.getBiomeId(n, n2);
        switch (n3) {
            case 2: 
            case 130: {
                return new BlockColor("#32A598");
            }
            case 4: {
                return new BlockColor("#1E97F2");
            }
            case 132: {
                return new BlockColor("#20A3CC");
            }
            case 3: 
            case 5: 
            case 19: 
            case 20: 
            case 34: 
            case 131: 
            case 133: 
            case 162: {
                return new BlockColor("#1E6B82");
            }
            case 6: {
                return new BlockColor("#4c6559");
            }
            case 134: {
                return new BlockColor("#4c6156");
            }
            case 7: {
                return new BlockColor("#0084FF");
            }
            case 9: {
                return new BlockColor("#62529e");
            }
            case 8: {
                return new BlockColor("#905957");
            }
            case 11: {
                return new BlockColor("#185390");
            }
            case 12: 
            case 140: {
                return new BlockColor("#14559b");
            }
            case 14: {
                return new BlockColor("#8a8997");
            }
            case 15: {
                return new BlockColor("#818193");
            }
            case 16: {
                return new BlockColor("#157cab");
            }
            case 17: {
                return new BlockColor("#1a7aa1");
            }
            case 18: {
                return new BlockColor("#056bd1");
            }
            case 21: {
                return new BlockColor("#14A2C5");
            }
            case 22: 
            case 149: {
                return new BlockColor("#1B9ED8");
            }
            case 23: 
            case 151: {
                return new BlockColor("#0D8AE3");
            }
            case 25: {
                return new BlockColor("#0d67bb");
            }
            case 26: {
                return new BlockColor("#1463a5");
            }
            case 27: 
            case 155: {
                return new BlockColor("#0677ce");
            }
            case 28: 
            case 156: {
                return new BlockColor("#0a74c4");
            }
            case 29: 
            case 157: {
                return new BlockColor("#3B6CD1");
            }
            case 30: 
            case 158: {
                return new BlockColor("#205e83");
            }
            case 31: {
                return new BlockColor("#245b78");
            }
            case 32: 
            case 160: {
                return new BlockColor("#2d6d77");
            }
            case 33: {
                return new BlockColor("#286378");
            }
            case 35: 
            case 163: {
                return new BlockColor("#2C8B9C");
            }
            case 36: 
            case 164: {
                return new BlockColor("#2590A8");
            }
            case 0: 
            case 24: {
                return new BlockColor("#1787D4");
            }
            case 10: {
                return new BlockColor("#2570B5");
            }
            default: {
                return new BlockColor("#44AFF5");
            }
            case 37: {
                return new BlockColor("#4E7F81");
            }
            case 38: 
            case 39: 
            case 165: {
                return new BlockColor("#497F99");
            }
            case 166: 
            case 167: 
        }
        return new BlockColor("#55809E");
    }

    public boolean isChunkLoaded(int n, int n2) {
        return this.u.isChunkLoaded(n, n2);
    }

    private boolean a(long l) {
        return this.u.isChunkLoaded(l + 1L) && this.u.isChunkLoaded(l - 1L) && this.u.isChunkLoaded(l + 0x100000000L) && this.u.isChunkLoaded(l - 0x100000000L);
    }

    public boolean isChunkGenerated(int n, int n2) {
        BaseFullChunk baseFullChunk = this.getChunk(n, n2);
        return baseFullChunk != null && baseFullChunk.isGenerated();
    }

    public boolean isChunkPopulated(int n, int n2) {
        BaseFullChunk baseFullChunk = this.getChunk(n, n2);
        return baseFullChunk != null && baseFullChunk.isPopulated();
    }

    public Position getSpawnLocation() {
        return Position.fromObject(this.u.getSpawn(), this);
    }

    public void setSpawnLocation(Vector3 vector3) {
        Position position = this.getSpawnLocation();
        this.u.setSpawn(vector3);
        this.d = this.getSpawnLocation();
        this.N.getPluginManager().callEvent(new SpawnChangeEvent(this, position));
        SetSpawnPositionPacket setSpawnPositionPacket = new SetSpawnPositionPacket();
        setSpawnPositionPacket.spawnType = 1;
        setSpawnPositionPacket.x = vector3.getFloorX();
        setSpawnPositionPacket.y = vector3.getFloorY();
        setSpawnPositionPacket.z = vector3.getFloorZ();
        setSpawnPositionPacket.dimension = this.getDimension();
        for (Player player : this.getPlayers().values()) {
            player.dataPacket(setSpawnPositionPacket);
        }
    }

    public void requestChunk(int n, int n2, Player player) {
        Preconditions.checkState(player.getLoaderId() > 0, player.getName() + " has no chunk loader");
        long l2 = Level.chunkHash(n, n2);
        this.a(player.protocol).computeIfAbsent(l2, l -> new Int2ObjectOpenHashMap()).put(player.getLoaderId(), player);
    }

    private void a(int n, int n2, long l, DataPacket dataPacket) {
        IntIterator intIterator = this.s.keySet().iterator();
        while (intIterator.hasNext()) {
            int n3 = (Integer)intIterator.next();
            this.a(n, n2, l, dataPacket, n3);
        }
    }

    private void a(int n, int n2, long l, DataPacket dataPacket, int n3) {
        LongSet longSet = this.c(n3);
        if (!longSet.contains(l)) {
            return;
        }
        ConcurrentMap<Long, Int2ObjectMap<Player>> concurrentMap = this.a(n3);
        for (Player player : ((Int2ObjectMap)concurrentMap.get(l)).values()) {
            if (!player.isConnected() || !player.usedChunks.containsKey(l)) continue;
            player.sendChunk(n, n2, dataPacket);
        }
        concurrentMap.remove(l);
        longSet.remove(l);
    }

    private void b() {
        block3: {
            if (this.timings.syncChunkSendTimer != null) {
                this.timings.syncChunkSendTimer.startTiming();
            }
            Long2ObjectOpenHashMap<IntSet> long2ObjectOpenHashMap = new Long2ObjectOpenHashMap<IntSet>();
            IntIterator intIterator = this.K.keySet().iterator();
            while (intIterator.hasNext()) {
                int n = (Integer)intIterator.next();
                Set set = this.a(n).keySet();
                LongSet longSet = this.c(n);
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    long l2 = (Long)iterator.next();
                    if (longSet.contains(l2)) continue;
                    long2ObjectOpenHashMap.computeIfAbsent(l2, l -> new IntOpenHashSet()).add(n);
                    longSet.add(l2);
                }
            }
            this.a(long2ObjectOpenHashMap);
            if (this.timings.syncChunkSendTimer == null) break block3;
            this.timings.syncChunkSendTimer.stopTiming();
        }
    }

    private void a(Long2ObjectMap<IntSet> long2ObjectMap) {
        LongIterator longIterator = long2ObjectMap.keySet().iterator();
        while (longIterator.hasNext()) {
            long l = (Long)longIterator.next();
            IntOpenHashSet intOpenHashSet = new IntOpenHashSet((IntCollection)long2ObjectMap.get(l));
            int n = Level.getHashX(l);
            int n2 = Level.getHashZ(l);
            IntIterator intIterator = ((IntSet)long2ObjectMap.get(l)).iterator();
            while (intIterator.hasNext()) {
                BatchPacket batchPacket;
                int n3 = (Integer)intIterator.next();
                BaseFullChunk baseFullChunk = this.getChunk(n, n2);
                if (baseFullChunk == null || (batchPacket = baseFullChunk.getChunkPacket(n3)) == null) continue;
                this.a(n, n2, l, batchPacket);
                intOpenHashSet.remove(n3);
            }
            if (intOpenHashSet.isEmpty()) continue;
            if (this.timings.syncChunkSendPrepareTimer != null) {
                this.timings.syncChunkSendPrepareTimer.startTiming();
            }
            this.u.requestChunkTask(intOpenHashSet, n, n2);
            if (this.timings.syncChunkSendPrepareTimer == null) continue;
            this.timings.syncChunkSendPrepareTimer.stopTiming();
        }
    }

    public void chunkRequestCallback(int n, long l, int n2, int n3, int n4, byte[] byArray) {
        this.chunkRequestCallback(n, l, n2, n3, n4, byArray, Level.chunkHash(n2, n3));
    }

    public void chunkRequestCallback(int n, long l, int n2, int n3, int n4, byte[] byArray, long l2) {
        block6: {
            if (this.timings.syncChunkSendTimer != null) {
                this.timings.syncChunkSendTimer.startTiming();
            }
            if (this.N.cacheChunks) {
                BatchPacket batchPacket = Player.getChunkCacheFromData(n, n2, n3, n4, byArray);
                BaseFullChunk baseFullChunk = this.getChunk(n2, n3, false);
                if (baseFullChunk != null && baseFullChunk.getChanges() <= l) {
                    baseFullChunk.setChunkPacket(n, batchPacket);
                }
                this.a(n2, n3, l2, batchPacket);
                if (this.timings.syncChunkSendTimer != null) {
                    this.timings.syncChunkSendTimer.stopTiming();
                }
                return;
            }
            LongSet longSet = this.c(n);
            if (longSet.contains(l2)) {
                ConcurrentMap<Long, Int2ObjectMap<Player>> concurrentMap = this.a(n);
                for (Player player : ((Int2ObjectMap)concurrentMap.get(l2)).values()) {
                    if (!player.isConnected() || !player.usedChunks.containsKey(l2) || !Level.b(n, player.protocol)) continue;
                    player.sendChunk(n2, n3, n4, byArray);
                }
                concurrentMap.remove(l2);
                longSet.remove(l2);
            }
            if (this.timings.syncChunkSendTimer == null) break block6;
            this.timings.syncChunkSendTimer.stopTiming();
        }
    }

    public void removeEntity(Entity entity) {
        if (entity.getLevel() != this) {
            throw new LevelException("Invalid Entity level");
        }
        if (entity instanceof Player) {
            this.H.remove(entity.getId());
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
            this.H.put(entity.getId(), (Player)entity);
        }
        this.entities.put(entity.getId(), entity);
    }

    public void addBlockEntity(BlockEntity blockEntity) {
        if (blockEntity.getLevel() != this) {
            throw new LevelException("Invalid BlockEntity level");
        }
        this.R.put(blockEntity.getId(), blockEntity);
    }

    public void scheduleBlockEntityUpdate(BlockEntity blockEntity) {
        block0: {
            Preconditions.checkNotNull(blockEntity, "entity");
            Preconditions.checkArgument(blockEntity.getLevel() == this, "BlockEntity is not in this level");
            if (this.D.contains(blockEntity)) break block0;
            this.D.add(blockEntity);
        }
    }

    public void removeBlockEntity(BlockEntity blockEntity) {
        Preconditions.checkNotNull(blockEntity, "entity");
        Preconditions.checkArgument(blockEntity.getLevel() == this, "BlockEntity is not in this level");
        this.R.remove(blockEntity.getId());
        this.D.remove(blockEntity);
    }

    public boolean isChunkInUse(int n, int n2) {
        return this.isChunkInUse(Level.chunkHash(n, n2));
    }

    public boolean isChunkInUse(long l) {
        Map<Integer, ChunkLoader> map = this.m.get(l);
        return map != null && !map.isEmpty();
    }

    public boolean loadChunk(int n, int n2) {
        return this.loadChunk(n, n2, true);
    }

    public boolean loadChunk(int n, int n2, boolean bl) {
        long l = Level.chunkHash(n, n2);
        if (this.u.isChunkLoaded(l)) {
            return true;
        }
        return this.a(l, n, n2, bl) != null;
    }

    private synchronized BaseFullChunk a(long l, int n, int n2, boolean bl) {
        BaseFullChunk baseFullChunk;
        block10: {
            if (this.timings.syncChunkLoadTimer != null) {
                this.timings.syncChunkLoadTimer.startTiming();
            }
            if ((baseFullChunk = this.u.getChunk(n, n2, bl)) == null) {
                if (bl) {
                    throw new IllegalStateException("Could not create new chunk");
                }
                if (this.timings.syncChunkLoadTimer != null) {
                    this.timings.syncChunkLoadTimer.stopTiming();
                }
                return null;
            }
            if (baseFullChunk.getProvider() == null) {
                this.unloadChunk(n, n2, false);
                if (this.timings.syncChunkLoadTimer != null) {
                    this.timings.syncChunkLoadTimer.stopTiming();
                }
                return baseFullChunk;
            }
            this.N.getPluginManager().callEvent(new ChunkLoadEvent(baseFullChunk, !baseFullChunk.isGenerated()));
            baseFullChunk.initChunk();
            if (!baseFullChunk.isLightPopulated() && baseFullChunk.isPopulated() && this.N.lightUpdates) {
                this.N.getScheduler().scheduleAsyncTask(new LightPopulationTask(this, baseFullChunk));
            }
            if (this.isChunkInUse(l)) {
                this.A.remove(l);
                for (ChunkLoader chunkLoader : this.getChunkLoaders(n, n2)) {
                    chunkLoader.onChunkLoaded(baseFullChunk);
                }
            } else {
                this.A.put(l, System.currentTimeMillis());
            }
            if (this.timings.syncChunkLoadTimer == null) break block10;
            this.timings.syncChunkLoadTimer.stopTiming();
        }
        return baseFullChunk;
    }

    private void a(int n, int n2) {
        long l = Level.chunkHash(n, n2);
        this.A.put(l, System.currentTimeMillis());
    }

    public boolean unloadChunkRequest(int n, int n2) {
        return this.unloadChunkRequest(n, n2, true);
    }

    public boolean unloadChunkRequest(int n, int n2, boolean bl) {
        if (bl && this.isChunkInUse(n, n2) || this.isSpawnChunk(n, n2)) {
            return false;
        }
        this.a(n, n2);
        return true;
    }

    public void cancelUnloadChunkRequest(int n, int n2) {
        this.cancelUnloadChunkRequest(Level.chunkHash(n, n2));
    }

    public void cancelUnloadChunkRequest(long l) {
        this.A.remove(l);
    }

    public boolean unloadChunk(int n, int n2) {
        return this.unloadChunk(n, n2, true);
    }

    public boolean unloadChunk(int n, int n2, boolean bl) {
        return this.unloadChunk(n, n2, bl, true);
    }

    public synchronized boolean unloadChunk(int n, int n2, boolean bl, boolean bl2) {
        block13: {
            BaseFullChunk baseFullChunk;
            if (bl && this.isChunkInUse(n, n2)) {
                return false;
            }
            if (!this.isChunkLoaded(n, n2)) {
                return true;
            }
            if (this.timings.doChunkUnload != null) {
                this.timings.doChunkUnload.startTiming();
            }
            if ((baseFullChunk = this.getChunk(n, n2)) != null && baseFullChunk.getProvider() != null) {
                ChunkUnloadEvent chunkUnloadEvent = new ChunkUnloadEvent(baseFullChunk);
                this.N.getPluginManager().callEvent(chunkUnloadEvent);
                if (chunkUnloadEvent.isCancelled()) {
                    if (this.timings.doChunkUnload != null) {
                        this.timings.doChunkUnload.stopTiming();
                    }
                    return false;
                }
            }
            try {
                if (baseFullChunk != null) {
                    if (bl2 && this.O) {
                        int n3 = 0;
                        for (Entity entity : baseFullChunk.getEntities().values()) {
                            if (entity instanceof Player) continue;
                            ++n3;
                        }
                        if (baseFullChunk.hasChanged() || !baseFullChunk.getBlockEntities().isEmpty() || n3 > 0) {
                            this.u.setChunk(n, n2, baseFullChunk);
                            this.u.saveChunk(n, n2);
                        }
                    }
                    for (ChunkLoader chunkLoader : this.getChunkLoaders(n, n2)) {
                        chunkLoader.onChunkUnloaded(baseFullChunk);
                    }
                }
                this.u.unloadChunk(n, n2, bl);
            }
            catch (Exception exception) {
                MainLogger mainLogger = this.N.getLogger();
                mainLogger.error(this.N.getLanguage().translateString("nukkit.level.chunkUnloadError", exception.toString()), exception);
            }
            if (this.timings.doChunkUnload == null) break block13;
            this.timings.doChunkUnload.stopTiming();
        }
        return true;
    }

    public boolean isSpawnChunk(int n, int n2) {
        if (this.d == null) {
            this.d = this.getSpawnLocation();
        }
        if (this.N.suomiCraftPEMode() && !this.randomTickingEnabled()) {
            if (this.equals(this.getServer().getDefaultLevel())) {
                return Math.abs(n - this.d.getChunkX()) <= 9 && Math.abs(n2 - this.d.getChunkZ()) <= 9;
            }
            return Math.abs(n - this.d.getChunkX()) <= 5 && Math.abs(n2 - this.d.getChunkZ()) <= 5;
        }
        return Math.abs(n - this.d.getChunkX()) <= 1 && Math.abs(n2 - this.d.getChunkZ()) <= 1;
    }

    public Position getSafeSpawn() {
        return this.getSafeSpawn(null);
    }

    public Position getSafeSpawn(Vector3 vector3) {
        if (vector3 == null) {
            vector3 = this.getSpawnLocation();
        }
        Vector3 vector32 = vector3.floor();
        BaseFullChunk baseFullChunk = this.getChunk((int)vector32.x >> 4, (int)vector32.z >> 4, false);
        int n = (int)vector32.x & 0xF;
        int n2 = (int)vector32.z & 0xF;
        if (baseFullChunk != null && baseFullChunk.isGenerated()) {
            Block block;
            int n3;
            boolean bl;
            int n4 = NukkitMath.clamp((int)vector32.y, 1, 254);
            boolean bl2 = bl = baseFullChunk.getBlockId(n, n4 - 1, n2) == 0;
            while (n4 > 0) {
                n3 = baseFullChunk.getFullBlock(n, n4, n2);
                block = Block.get(n3 >> 4, n3 & 0xF);
                if (this.isFullBlock(block)) {
                    if (!bl) break;
                    ++n4;
                    break;
                }
                bl = true;
                --n4;
            }
            while (n4 >= 0 && n4 < 255) {
                n3 = baseFullChunk.getFullBlock(n, n4 + 1, n2);
                block = Block.get(n3 >> 4, n3 & 0xF);
                if (!this.isFullBlock(block)) {
                    n3 = baseFullChunk.getFullBlock(n, n4, n2);
                    block = Block.get(n3 >> 4, n3 & 0xF);
                    if (!this.isFullBlock(block)) {
                        return new Position(vector32.x + 0.5, vector32.y + 0.51, vector32.z + 0.5, this);
                    }
                } else {
                    ++n4;
                }
                ++n4;
            }
            vector32.y = n4;
        }
        return new Position(vector32.x + 0.5, vector32.y + 0.1, vector32.z + 0.5, this);
    }

    public int getTime() {
        return this.r;
    }

    public boolean isDaytime() {
        return this.skyLightSubtracted < 4.0f;
    }

    public long getCurrentTick() {
        return this.P;
    }

    public String getName() {
        return this.u.getName();
    }

    public String getFolderName() {
        return this.X;
    }

    public void setTime(int n) {
        this.r = n;
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
        return this.u.getSeed();
    }

    public void setSeed(int n) {
        this.u.setSeed(n);
    }

    public boolean populateChunk(int n, int n2) {
        return this.populateChunk(n, n2, false);
    }

    public boolean populateChunk(int n, int n2, boolean bl) {
        long l = Level.chunkHash(n, n2);
        if (this.l.containsKey(l) || this.l.size() >= this.Y && !bl) {
            return false;
        }
        BaseFullChunk baseFullChunk = this.getChunk(n, n2, true);
        if (!baseFullChunk.isPopulated()) {
            int n3;
            int n4;
            if (Timings.populationTimer != null) {
                Timings.populationTimer.startTiming();
            }
            boolean bl2 = true;
            block0: for (n4 = -1; n4 <= 1; ++n4) {
                for (n3 = -1; n3 <= 1; ++n3) {
                    if (!this.e.containsKey(Level.chunkHash(n + n4, n2 + n3))) continue;
                    bl2 = false;
                    continue block0;
                }
            }
            if (bl2 && !this.l.containsKey(l)) {
                this.l.put(l, Boolean.TRUE);
                for (n4 = -1; n4 <= 1; ++n4) {
                    for (n3 = -1; n3 <= 1; ++n3) {
                        this.e.put(Level.chunkHash(n + n4, n2 + n3), Boolean.TRUE);
                    }
                }
                this.N.getScheduler().scheduleAsyncTask(new PopulationTask(this, baseFullChunk));
            }
            if (Timings.populationTimer != null) {
                Timings.populationTimer.stopTiming();
            }
            return false;
        }
        return true;
    }

    public void generateChunk(int n, int n2) {
        this.generateChunk(n, n2, false);
    }

    public void generateChunk(int n, int n2, boolean bl) {
        block3: {
            if (this.J.size() >= this.B && !bl) {
                return;
            }
            long l = Level.chunkHash(n, n2);
            if (this.J.containsKey(l)) break block3;
            if (Timings.generationTimer != null) {
                Timings.generationTimer.startTiming();
            }
            this.J.put(l, Boolean.TRUE);
            GenerationTask generationTask = new GenerationTask(this, this.getChunk(n, n2, true));
            this.N.getScheduler().scheduleAsyncTask(generationTask);
            if (Timings.generationTimer != null) {
                Timings.generationTimer.stopTiming();
            }
        }
    }

    public void regenerateChunk(int n, int n2) {
        this.unloadChunk(n, n2, false, false);
        this.cancelUnloadChunkRequest(n, n2);
        this.u.setChunk(n, n2, this.u.getEmptyChunk(n, n2));
        this.generateChunk(n, n2);
    }

    public void doChunkGarbageCollection() {
        block5: {
            if (this.timings.doChunkGC != null) {
                this.timings.doChunkGC.startTiming();
            }
            if (!this.R.isEmpty()) {
                Iterator<Map.Entry<Long, ? extends FullChunk>> iterator = this.R.values().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Long, ? extends FullChunk> entry = (BlockEntity)((Object)iterator.next());
                    if (entry != null) {
                        if (((Position)((Object)entry)).isValid()) continue;
                        iterator.remove();
                        ((BlockEntity)((Object)entry)).close();
                        continue;
                    }
                    iterator.remove();
                }
            }
            for (Map.Entry<Long, ? extends FullChunk> entry : this.u.getLoadedChunks().entrySet()) {
                int n;
                FullChunk fullChunk;
                int n2;
                long l = (Long)entry.getKey();
                if (this.A.containsKey(l) || this.isSpawnChunk(n2 = (fullChunk = (FullChunk)entry.getValue()).getX(), n = fullChunk.getZ())) continue;
                this.unloadChunkRequest(n2, n, true);
            }
            this.u.doGarbageCollection();
            if (this.timings.doChunkGC == null) break block5;
            this.timings.doChunkGC.stopTiming();
        }
    }

    public void doGarbageCollection(long l) {
        long l2 = System.currentTimeMillis();
        if (this.a(l2, l, false)) {
            this.u.doGarbageCollection(l -= System.currentTimeMillis() - l2);
        }
    }

    public void unloadChunks() {
        this.unloadChunks(false);
    }

    public void unloadChunks(boolean bl) {
        this.unloadChunks(50, bl);
    }

    public void unloadChunks(int n, boolean bl) {
        if (!this.A.isEmpty()) {
            long l;
            long l2 = System.currentTimeMillis();
            int n2 = 0;
            List list = null;
            for (Long2LongMap.Entry entry : this.A.long2LongEntrySet()) {
                l = entry.getLongKey();
                if (this.isChunkInUse(l)) continue;
                if (!bl) {
                    long l3 = entry.getLongValue();
                    if (n2 > n) break;
                    if (l3 > l2 - 20000L) continue;
                    ++n2;
                }
                if (list == null) {
                    list = new LongArrayList();
                }
                list.add(l);
            }
            if (list != null) {
                int n3 = list.size();
                for (int k = 0; k < n3; ++k) {
                    int n4;
                    l = list.getLong(k);
                    int n5 = Level.getHashX(l);
                    if (!this.unloadChunk(n5, n4 = Level.getHashZ(l), true)) continue;
                    this.A.remove(l);
                }
            }
        }
    }

    private boolean a(long l, long l2, boolean bl) {
        if (!this.A.isEmpty()) {
            boolean bl2 = true;
            int n = this.A.size();
            if (this.I > n) {
                this.I = 0;
            }
            Iterator iterator = this.A.long2LongEntrySet().iterator();
            if (this.I != 0) {
                iterator.skip(this.I);
            }
            LongList longList = null;
            for (int k = 0; k < n; ++k) {
                long l3;
                Long2LongMap.Entry entry;
                long l4;
                if (!iterator.hasNext()) {
                    iterator = this.A.long2LongEntrySet().iterator();
                }
                if (this.isChunkInUse(l4 = (entry = (Long2LongMap.Entry)iterator.next()).getLongKey()) || !bl && (l3 = entry.getLongValue()) > l - 20000L) continue;
                if (longList == null) {
                    longList = new LongArrayList();
                }
                longList.add(l4);
            }
            if (longList != null) {
                LongListIterator longListIterator = longList.iterator();
                while (longListIterator.hasNext()) {
                    int n2;
                    long l5 = (Long)longListIterator.next();
                    int n3 = Level.getHashX(l5);
                    if (!this.unloadChunk(n3, n2 = Level.getHashZ(l5), true)) continue;
                    this.A.remove(l5);
                    if (System.currentTimeMillis() - l < l2) continue;
                    bl2 = false;
                    break;
                }
            }
            return bl2;
        }
        return true;
    }

    @Override
    public void setMetadata(String string, MetadataValue metadataValue) throws Exception {
        this.N.getLevelMetadata().setMetadata(this, string, metadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String string) throws Exception {
        return this.N.getLevelMetadata().getMetadata(this, string);
    }

    @Override
    public boolean hasMetadata(String string) throws Exception {
        return this.N.getLevelMetadata().hasMetadata(this, string);
    }

    @Override
    public void removeMetadata(String string, Plugin plugin) throws Exception {
        this.N.getLevelMetadata().removeMetadata(this, string, plugin);
    }

    public void addPlayerMovement(Entity entity, double d2, double d3, double d4, double d5, double d6, double d7) {
        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
        movePlayerPacket.eid = entity.getId();
        movePlayerPacket.x = (float)d2;
        movePlayerPacket.y = (float)d3;
        movePlayerPacket.z = (float)d4;
        movePlayerPacket.yaw = (float)d5;
        movePlayerPacket.headYaw = (float)d7;
        movePlayerPacket.pitch = (float)d6;
        movePlayerPacket.onGround = entity.onGround;
        if (entity.riding != null) {
            movePlayerPacket.ridingEid = entity.riding.getId();
            movePlayerPacket.mode = 3;
        }
        Server.broadcastPacket(entity.getViewers().values(), (DataPacket)movePlayerPacket);
    }

    public void addEntityMovement(Entity entity, double d2, double d3, double d4, double d5, double d6, double d7) {
        MoveEntityAbsolutePacket moveEntityAbsolutePacket = new MoveEntityAbsolutePacket();
        moveEntityAbsolutePacket.eid = entity.getId();
        moveEntityAbsolutePacket.x = (float)d2;
        moveEntityAbsolutePacket.y = (float)d3;
        moveEntityAbsolutePacket.z = (float)d4;
        moveEntityAbsolutePacket.yaw = (float)d5;
        moveEntityAbsolutePacket.headYaw = (float)d7;
        moveEntityAbsolutePacket.pitch = (float)d6;
        moveEntityAbsolutePacket.onGround = entity.onGround;
        for (Player player : entity.getViewers().values()) {
            player.dataPacket(moveEntityAbsolutePacket);
        }
    }

    public boolean isRaining() {
        return this.c;
    }

    public boolean setRaining(boolean bl) {
        WeatherChangeEvent weatherChangeEvent = new WeatherChangeEvent(this, bl);
        this.N.getPluginManager().callEvent(weatherChangeEvent);
        if (weatherChangeEvent.isCancelled()) {
            return false;
        }
        this.c = bl;
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        if (bl) {
            int n;
            levelEventPacket.evid = 3001;
            levelEventPacket.data = n = Utils.random.nextInt(12000) + 12000;
            this.setRainTime(n);
        } else {
            levelEventPacket.evid = 3003;
            this.setRainTime(Utils.random.nextInt(168000) + 12000);
        }
        Server.broadcastPacket(this.getPlayers().values(), (DataPacket)levelEventPacket);
        return true;
    }

    public int getRainTime() {
        return this.C;
    }

    public void setRainTime(int n) {
        this.C = n;
    }

    public boolean isThundering() {
        return this.c && this.j;
    }

    public boolean setThundering(boolean bl) {
        ThunderChangeEvent thunderChangeEvent = new ThunderChangeEvent(this, bl);
        this.N.getPluginManager().callEvent(thunderChangeEvent);
        if (thunderChangeEvent.isCancelled()) {
            return false;
        }
        if (bl && !this.c) {
            this.setRaining(true);
        }
        this.j = bl;
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        if (bl) {
            int n;
            levelEventPacket.evid = 3002;
            levelEventPacket.data = n = Utils.random.nextInt(12000) + 3600;
            this.setThunderTime(n);
        } else {
            levelEventPacket.evid = 3004;
            this.setThunderTime(Utils.random.nextInt(168000) + 12000);
        }
        Server.broadcastPacket(this.getPlayers().values(), (DataPacket)levelEventPacket);
        return true;
    }

    public int getThunderTime() {
        return this.k;
    }

    public void setThunderTime(int n) {
        this.k = n;
    }

    public void sendWeather(Player[] playerArray) {
        if (playerArray == null) {
            playerArray = this.getPlayers().values().toArray(new Player[0]);
        }
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        if (this.c) {
            levelEventPacket.evid = 3001;
            levelEventPacket.data = this.C;
        } else {
            levelEventPacket.evid = 3003;
        }
        Server.broadcastPacket(playerArray, (DataPacket)levelEventPacket);
        levelEventPacket = new LevelEventPacket();
        if (this.isThundering()) {
            levelEventPacket.evid = 3002;
            levelEventPacket.data = this.k;
        } else {
            levelEventPacket.evid = 3004;
        }
        Server.broadcastPacket(playerArray, (DataPacket)levelEventPacket);
    }

    public void sendWeather(Player player) {
        block0: {
            if (player == null) break block0;
            this.sendWeather(new Player[]{player});
        }
    }

    public void sendWeather(Collection<Player> collection) {
        if (collection == null) {
            collection = this.getPlayers().values();
        }
        this.sendWeather(collection.toArray(new Player[0]));
    }

    public DimensionData getDimensionData() {
        return this.y;
    }

    public int getDimension() {
        return this.y.getDimensionId();
    }

    public int getMinBlockY() {
        return 0;
    }

    public int getMaxBlockY() {
        return this.getDimension() == 1 ? 127 : 255;
    }

    public boolean canBlockSeeSky(Vector3 vector3) {
        return (double)this.getHighestBlockAt(vector3.getFloorX(), vector3.getFloorZ()) < vector3.getY();
    }

    public boolean canBlockSeeSky(Block block) {
        return (double)this.getHighestBlockAt((int)block.getX(), (int)block.getZ()) < block.getY();
    }

    public int getStrongPower(Vector3 vector3, BlockFace blockFace) {
        return this.b(null, vector3, blockFace);
    }

    private int b(FullChunk fullChunk, Vector3 vector3, BlockFace blockFace) {
        return this.getBlock(fullChunk, vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ(), true).getStrongPower(blockFace);
    }

    public int getStrongPower(Vector3 vector3) {
        return this.a(null, vector3);
    }

    private int a(FullChunk fullChunk, Vector3 vector3) {
        int n = 0;
        for (BlockFace blockFace : BlockFace.values()) {
            if ((n = Math.max(n, this.b(fullChunk, vector3.getSideVec(blockFace), blockFace))) < 15) continue;
            return n;
        }
        return n;
    }

    public boolean isSidePowered(Vector3 vector3, BlockFace blockFace) {
        return this.getRedstonePower(vector3, blockFace) > 0;
    }

    public int getRedstonePower(Vector3 vector3, BlockFace blockFace) {
        return this.a(null, vector3, blockFace);
    }

    private int a(FullChunk fullChunk, Vector3 vector3, BlockFace blockFace) {
        Block block = this.getBlock(fullChunk, vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ(), true);
        return block.isNormalBlock() ? this.a(fullChunk, vector3) : block.getWeakPower(blockFace);
    }

    public boolean isBlockPowered(Vector3 vector3) {
        return this.isBlockPowered(null, vector3);
    }

    public boolean isBlockPowered(FullChunk fullChunk, Vector3 vector3) {
        for (BlockFace blockFace : BlockFace.values()) {
            if (this.a(fullChunk, vector3.getSideVec(blockFace), blockFace) <= 0) continue;
            return true;
        }
        return false;
    }

    public int isBlockIndirectlyGettingPowered(Vector3 vector3) {
        int n = 0;
        for (BlockFace blockFace : BlockFace.values()) {
            int n2 = this.getRedstonePower(vector3.getSideVec(blockFace), blockFace);
            if (n2 >= 15) {
                return 15;
            }
            if (n2 <= n) continue;
            n = n2;
        }
        return n;
    }

    public boolean isAreaLoaded(AxisAlignedBB axisAlignedBB) {
        if (axisAlignedBB.maxY < 0.0 || axisAlignedBB.minY >= 256.0) {
            return false;
        }
        int n = NukkitMath.floorDouble(axisAlignedBB.minX) >> 4;
        int n2 = NukkitMath.floorDouble(axisAlignedBB.minZ) >> 4;
        int n3 = NukkitMath.floorDouble(axisAlignedBB.maxX) >> 4;
        int n4 = NukkitMath.floorDouble(axisAlignedBB.maxZ) >> 4;
        for (int k = n; k <= n3; ++k) {
            for (int i2 = n2; i2 <= n4; ++i2) {
                if (this.isChunkLoaded(k, i2)) continue;
                return false;
            }
        }
        return true;
    }

    private int c() {
        this.L = this.L * 3 ^ 0x3C6EF35F;
        return this.L;
    }

    public boolean randomTickingEnabled() {
        return this.p;
    }

    public boolean isAnimalSpawningAllowedByTime() {
        int n = this.getTime() % 24000;
        return n < 13184 || n > 22800;
    }

    public boolean isMobSpawningAllowedByTime() {
        int n = this.getTime() % 24000;
        return n > 13184 && n < 22800;
    }

    public boolean shouldMobBurn(BaseEntity baseEntity) {
        int n = this.getTime() % 24000;
        return !baseEntity.isOnFire() && !this.c && !baseEntity.isBaby() && (n < 12567 || n > 23450) && !baseEntity.isInsideOfWater() && baseEntity.canSeeSky();
    }

    public boolean isMobSpawningAllowed() {
        return !Server.disabledSpawnWorlds.contains(this.getName()) && this.gameRules.getBoolean(GameRule.DO_MOB_SPAWNING);
    }

    public boolean antiXrayEnabled() {
        return this.T;
    }

    public boolean createPortal(Block block, boolean bl) {
        int n;
        int n2;
        if (this.getDimension() == 2) {
            return false;
        }
        int n3 = 23;
        int n4 = block.getFloorX();
        int n5 = block.getFloorY();
        int n6 = block.getFloorZ();
        for (n2 = 1; n2 < 4; ++n2) {
            if (this.getBlockIdAt(n4, n5 + n2, n6) == 0) continue;
            return false;
        }
        n2 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        for (n = 1; n < 23 && this.getBlockIdAt(n4 + n, n5, n6) == 49; ++n) {
            ++n2;
        }
        for (n = 1; n < 23 && this.getBlockIdAt(n4 - n, n5, n6) == 49; ++n) {
            ++n7;
        }
        for (n = 1; n < 23 && this.getBlockIdAt(n4, n5, n6 + n) == 49; ++n) {
            ++n8;
        }
        for (n = 1; n < 23 && this.getBlockIdAt(n4, n5, n6 - n) == 49; ++n) {
            ++n9;
        }
        n = n2 + n7 + 1;
        int n10 = n8 + n9 + 1;
        if (n >= 2 && n <= 23) {
            int n11;
            int n12;
            int n13;
            int n14;
            int n15 = n4;
            int n16 = n5 + 1;
            for (n14 = 0; n14 < n2 + 1; ++n14) {
                if (this.getBlockIdAt(n15 + n14, n16, n6) != 0) {
                    return false;
                }
                if (this.getBlockIdAt(n15 + n14 + 1, n16, n6) != 49) continue;
                n15 += n14;
                break;
            }
            if (this.getBlockIdAt(n15 + 1, n16, n6) != 49) {
                return false;
            }
            n14 = 0;
            block22: for (n13 = 0; n13 < 21; ++n13) {
                n12 = this.getBlockIdAt(n15 - n13, n16, n6);
                switch (n12) {
                    case 0: {
                        ++n14;
                        continue block22;
                    }
                    case 49: {
                        break block22;
                    }
                    default: {
                        return false;
                    }
                }
            }
            n13 = 0;
            block23: for (n12 = 0; n12 < 21; ++n12) {
                n11 = this.getBlockIdAt(n15, n16 + n12, n6);
                switch (n11) {
                    case 0: {
                        ++n13;
                        continue block23;
                    }
                    case 49: {
                        break block23;
                    }
                    default: {
                        return false;
                    }
                }
            }
            if (n14 > 21 || n14 < 2 || n13 > 21 || n13 < 3) {
                return false;
            }
            for (n12 = 0; n12 < n13 + 1; ++n12) {
                if (n12 == n13) {
                    for (n11 = 0; n11 < n14; ++n11) {
                        if (this.getBlockIdAt(n15 - n11, n16 + n12, n6) == 49) continue;
                        return false;
                    }
                    continue;
                }
                if (this.getBlockIdAt(n15 + 1, n16 + n12, n6) != 49 || this.getBlockIdAt(n15 - n14, n16 + n12, n6) != 49) {
                    return false;
                }
                for (n11 = 0; n11 < n14; ++n11) {
                    if (this.getBlockIdAt(n15 - n11, n16 + n12, n6) == 0) continue;
                    return false;
                }
            }
            for (n12 = 0; n12 < n13; ++n12) {
                for (n11 = 0; n11 < n14; ++n11) {
                    this.setBlock(new Vector3(n15 - n11, n16 + n12, n6), Block.get(90));
                }
            }
            if (bl) {
                this.addSound((Vector3)block, Sound.MOB_GHAST_FIREBALL);
            } else {
                this.addLevelSoundEvent(block, 50);
            }
            return true;
        }
        if (n10 >= 2 && n10 <= 23) {
            int n17;
            int n18;
            int n19;
            int n20;
            int n21 = n5 + 1;
            int n22 = n6;
            for (n20 = 0; n20 < n8 + 1; ++n20) {
                if (this.getBlockIdAt(n4, n21, n22 + n20) != 0) {
                    return false;
                }
                if (this.getBlockIdAt(n4, n21, n22 + n20 + 1) != 49) continue;
                n22 += n20;
                break;
            }
            if (this.getBlockIdAt(n4, n21, n22 + 1) != 49) {
                return false;
            }
            n20 = 0;
            block30: for (n19 = 0; n19 < 21; ++n19) {
                n18 = this.getBlockIdAt(n4, n21, n22 - n19);
                switch (n18) {
                    case 0: {
                        ++n20;
                        continue block30;
                    }
                    case 49: {
                        break block30;
                    }
                    default: {
                        return false;
                    }
                }
            }
            n19 = 0;
            block31: for (n18 = 0; n18 < 21; ++n18) {
                n17 = this.getBlockIdAt(n4, n21 + n18, n22);
                switch (n17) {
                    case 0: {
                        ++n19;
                        continue block31;
                    }
                    case 49: {
                        break block31;
                    }
                    default: {
                        return false;
                    }
                }
            }
            if (n20 > 21 || n20 < 2 || n19 > 21 || n19 < 3) {
                return false;
            }
            for (n18 = 0; n18 < n19 + 1; ++n18) {
                if (n18 == n19) {
                    for (n17 = 0; n17 < n20; ++n17) {
                        if (this.getBlockIdAt(n4, n21 + n18, n22 - n17) == 49) continue;
                        return false;
                    }
                    continue;
                }
                if (this.getBlockIdAt(n4, n21 + n18, n22 + 1) != 49 || this.getBlockIdAt(n4, n21 + n18, n22 - n20) != 49) {
                    return false;
                }
                for (n17 = 0; n17 < n20; ++n17) {
                    if (this.getBlockIdAt(n4, n21 + n18, n22 - n17) == 0) continue;
                    return false;
                }
            }
            for (n18 = 0; n18 < n19; ++n18) {
                for (n17 = 0; n17 < n20; ++n17) {
                    this.setBlock(new Vector3(n4, n21 + n18, n22 - n17), Block.get(90));
                }
            }
            if (bl) {
                this.addSound((Vector3)block, Sound.MOB_GHAST_FIREBALL);
            } else {
                this.addLevelSoundEvent(block, 50);
            }
            return true;
        }
        return false;
    }

    public Position calculatePortalMirror(Vector3 vector3) {
        double d2;
        double d3;
        double d4;
        Level level = Server.getInstance().getNetherWorld(this.getName());
        if (level == null) {
            return null;
        }
        if (this == level) {
            d4 = Math.floor(vector3.getFloorX() << 3);
            d3 = NukkitMath.clamp(EnumLevel.a(vector3.getFloorY()), 70, 246);
            d2 = Math.floor(vector3.getFloorZ() << 3);
        } else {
            d4 = Math.floor(vector3.getFloorX() >> 3);
            d3 = NukkitMath.clamp(EnumLevel.a(vector3.getFloorY()), 70, 118);
            d2 = Math.floor(vector3.getFloorZ() >> 3);
        }
        return new Position(d4, d3, d2, this == level ? Server.getInstance().getDefaultLevel() : level);
    }

    private ConcurrentMap<Long, Int2ObjectMap<Player>> a(int n2) {
        int n3 = Level.b(n2);
        return this.K.computeIfAbsent(n3, n -> new ConcurrentHashMap());
    }

    private LongSet c(int n2) {
        int n3 = Level.b(n2);
        return this.s.computeIfAbsent(n3, n -> new LongOpenHashSet());
    }

    private static int b(int n) {
        if (n >= 560) {
            return 560;
        }
        if (n >= 544) {
            return 544;
        }
        if (n >= 524) {
            return 527;
        }
        if (n >= 503) {
            return 503;
        }
        if (n >= 485) {
            return 486;
        }
        if (n >= 474) {
            return 475;
        }
        if (n >= 471) {
            return 471;
        }
        if (n >= 465) {
            return 465;
        }
        if (n >= 448) {
            return 448;
        }
        if (n >= 440) {
            return 440;
        }
        if (n >= 428) {
            return 428;
        }
        if (n >= 419) {
            return 419;
        }
        if (n >= 407) {
            return 407;
        }
        if (n == 389 || n == 390) {
            return 389;
        }
        if (n == 388) {
            return 388;
        }
        if (n == 361) {
            return 361;
        }
        if (n < 361) {
            return 0;
        }
        throw new IllegalArgumentException("Invalid chunk protocol: " + n);
    }

    private static boolean b(int n, int n2) {
        if (n2 >= 560) {
            return n == 560;
        }
        if (n2 >= 544) {
            return n == 544;
        }
        if (n2 >= 524) {
            return n == 527;
        }
        if (n2 >= 503) {
            return n == 503;
        }
        if (n2 >= 485) {
            return n == 486;
        }
        if (n2 >= 474) {
            return n == 475;
        }
        if (n2 >= 471) {
            return n == 471;
        }
        if (n2 >= 465) {
            return n == 465;
        }
        if (n2 >= 448) {
            return n == 448;
        }
        if (n2 >= 440) {
            return n == 440;
        }
        if (n2 >= 428) {
            return n == 428;
        }
        if (n2 >= 419) {
            return n == 419;
        }
        if (n2 >= 407) {
            return n == 407;
        }
        if (n2 == 389 || n2 == 390) {
            return n == 389;
        }
        if (n2 == 388) {
            return n == 388;
        }
        if (n2 == 361) {
            return n == 361;
        }
        if (n2 < 361) {
            return n == 0;
        }
        return false;
    }

    public String toString() {
        return "Level@" + Integer.toHexString(this.hashCode()) + "[" + this.X + ']';
    }

    public void asyncChunk(IntSet intSet, cn.nukkit.level.format.anvil.Chunk chunk, long l, int n, int n2) {
        this.a.b(intSet, chunk, l, n, n2, this.T, this.y);
    }

    static /* synthetic */ LevelProvider access$100(Level level) {
        return level.u;
    }

    static /* synthetic */ Class access$200(Level level) {
        return level.generatorClass;
    }

    static {
        Level.randomTickBlocks[2] = true;
        Level.randomTickBlocks[60] = true;
        Level.randomTickBlocks[110] = true;
        Level.randomTickBlocks[6] = true;
        Level.randomTickBlocks[18] = true;
        Level.randomTickBlocks[161] = true;
        Level.randomTickBlocks[78] = true;
        Level.randomTickBlocks[79] = true;
        if (!Server.getInstance().suomiCraftPEMode()) {
            Level.randomTickBlocks[10] = true;
        }
        if (!Server.getInstance().suomiCraftPEMode()) {
            Level.randomTickBlocks[11] = true;
        }
        Level.randomTickBlocks[81] = true;
        Level.randomTickBlocks[244] = true;
        Level.randomTickBlocks[141] = true;
        Level.randomTickBlocks[142] = true;
        Level.randomTickBlocks[105] = true;
        Level.randomTickBlocks[104] = true;
        Level.randomTickBlocks[59] = true;
        Level.randomTickBlocks[83] = true;
        Level.randomTickBlocks[115] = true;
        if (!Server.getInstance().suomiCraftPEMode()) {
            Level.randomTickBlocks[51] = true;
        }
        Level.randomTickBlocks[74] = true;
        Level.randomTickBlocks[127] = true;
        Level.randomTickBlocks[207] = true;
        Level.randomTickBlocks[106] = true;
        Level.randomTickBlocks[8] = true;
        Level.randomTickBlocks[118] = true;
        Level.randomTickBlocks[200] = true;
        Level.randomTickBlocks[462] = true;
        Level.randomTickBlocks[419] = true;
        Level.randomTickBlocks[418] = true;
        Level.xrayableBlocks[15] = true;
        Level.xrayableBlocks[14] = true;
        Level.xrayableBlocks[56] = true;
        Level.xrayableBlocks[73] = true;
        Level.xrayableBlocks[21] = true;
        Level.xrayableBlocks[129] = true;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}


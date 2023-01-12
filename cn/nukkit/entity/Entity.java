/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFire;
import cn.nukkit.block.BlockSlab;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.EntityBoss;
import cn.nukkit.entity.EntityFlying;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.custom.EntityDefinition;
import cn.nukkit.entity.custom.EntityManager;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.data.EntityData;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.data.ShortEntityData;
import cn.nukkit.entity.data.StringEntityData;
import cn.nukkit.entity.data.Vector3fEntityData;
import cn.nukkit.entity.item.EntityMinecartEmpty;
import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.entity.passive.EntityPig;
import cn.nukkit.entity.passive.EntityWolf;
import cn.nukkit.event.Event;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDespawnEvent;
import cn.nukkit.event.entity.EntityInteractEvent;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.entity.EntityMotionEvent;
import cn.nukkit.event.entity.EntityPortalEnterEvent;
import cn.nukkit.event.entity.EntityRegainHealthEvent;
import cn.nukkit.event.entity.EntitySpawnEvent;
import cn.nukkit.event.entity.EntityTeleportEvent;
import cn.nukkit.event.entity.EntityVehicleEnterEvent;
import cn.nukkit.event.entity.EntityVehicleExitEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.particle.GenericParticle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.metadata.Metadatable;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.AnimatePacket;
import cn.nukkit.network.protocol.BossEventPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.MobEffectPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.network.protocol.SetEntityLinkPacket;
import cn.nukkit.network.protocol.SetEntityMotionPacket;
import cn.nukkit.network.protocol.types.EntityLink;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.ChunkException;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.Utils;
import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import co.aikar.timings.TimingsHistory;
import com.google.common.collect.Iterables;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Entity
extends Location
implements Metadatable {
    public static final int NETWORK_ID = -1;
    public static final int DATA_TYPE_BYTE = 0;
    public static final int DATA_TYPE_SHORT = 1;
    public static final int DATA_TYPE_INT = 2;
    public static final int DATA_TYPE_FLOAT = 3;
    public static final int DATA_TYPE_STRING = 4;
    public static final int DATA_TYPE_NBT = 5;
    public static final int DATA_TYPE_POS = 6;
    public static final int DATA_TYPE_LONG = 7;
    public static final int DATA_TYPE_VECTOR3F = 8;
    public static final int DATA_FLAGS = 0;
    public static final int DATA_HEALTH = 1;
    public static final int DATA_VARIANT = 2;
    public static final int DATA_COLOR = 3;
    public static final int DATA_COLOUR = 3;
    public static final int DATA_NAMETAG = 4;
    public static final int DATA_OWNER_EID = 5;
    public static final int DATA_TARGET_EID = 6;
    public static final int DATA_AIR = 7;
    public static final int DATA_POTION_COLOR = 8;
    public static final int DATA_POTION_AMBIENT = 9;
    public static final int DATA_JUMP_DURATION = 10;
    public static final int DATA_HURT_TIME = 11;
    public static final int DATA_HURT_DIRECTION = 12;
    public static final int DATA_PADDLE_TIME_LEFT = 13;
    public static final int DATA_PADDLE_TIME_RIGHT = 14;
    public static final int DATA_EXPERIENCE_VALUE = 15;
    public static final int DATA_DISPLAY_ITEM = 16;
    public static final int DATA_DISPLAY_OFFSET = 17;
    public static final int DATA_HAS_DISPLAY = 18;
    public static final int DATA_SWELL = 19;
    public static final int DATA_OLD_SWELL = 20;
    public static final int DATA_SWELL_DIR = 21;
    public static final int DATA_CHARGE_AMOUNT = 22;
    public static final int DATA_ENDERMAN_HELD_RUNTIME_ID = 23;
    public static final int DATA_ENTITY_AGE = 24;
    public static final int DATA_PLAYER_FLAGS = 26;
    public static final int DATA_PLAYER_INDEX = 27;
    public static final int DATA_PLAYER_BED_POSITION = 28;
    public static final int DATA_FIREBALL_POWER_X = 29;
    public static final int DATA_FIREBALL_POWER_Y = 30;
    public static final int DATA_FIREBALL_POWER_Z = 31;
    public static final int DATA_AUX_POWER = 32;
    public static final int DATA_FISH_X = 33;
    public static final int DATA_FISH_Z = 34;
    public static final int DATA_FISH_ANGLE = 35;
    public static final int DATA_POTION_AUX_VALUE = 36;
    public static final int DATA_LEAD_HOLDER_EID = 37;
    public static final int DATA_SCALE = 38;
    public static final int DATA_HAS_NPC_COMPONENT = 39;
    public static final int DATA_NPC_SKIN_ID = 40;
    public static final int DATA_URL_TAG = 41;
    public static final int DATA_MAX_AIR = 42;
    public static final int DATA_MARK_VARIANT = 43;
    public static final int DATA_CONTAINER_TYPE = 44;
    public static final int DATA_CONTAINER_BASE_SIZE = 45;
    public static final int DATA_CONTAINER_EXTRA_SLOTS_PER_STRENGTH = 46;
    public static final int DATA_BLOCK_TARGET = 47;
    public static final int DATA_WITHER_INVULNERABLE_TICKS = 48;
    public static final int DATA_WITHER_TARGET_1 = 49;
    public static final int DATA_WITHER_TARGET_2 = 50;
    public static final int DATA_WITHER_TARGET_3 = 51;
    public static final int DATA_AERIAL_ATTACK = 52;
    public static final int DATA_BOUNDING_BOX_WIDTH = 53;
    public static final int DATA_BOUNDING_BOX_HEIGHT = 54;
    public static final int DATA_FUSE_LENGTH = 55;
    public static final int DATA_RIDER_SEAT_POSITION = 56;
    public static final int DATA_RIDER_ROTATION_LOCKED = 57;
    public static final int DATA_RIDER_MAX_ROTATION = 58;
    public static final int DATA_RIDER_MIN_ROTATION = 59;
    public static final int DATA_RIDER_ROTATION_OFFSET = 60;
    public static final int DATA_AREA_EFFECT_CLOUD_RADIUS = 61;
    public static final int DATA_AREA_EFFECT_CLOUD_WAITING = 62;
    public static final int DATA_AREA_EFFECT_CLOUD_PARTICLE_ID = 63;
    public static final int DATA_SHULKER_PEEK_ID = 64;
    public static final int DATA_SHULKER_ATTACH_FACE = 65;
    public static final int DATA_SHULKER_ATTACHED = 66;
    public static final int DATA_SHULKER_ATTACH_POS = 67;
    public static final int DATA_TRADING_PLAYER_EID = 68;
    public static final int DATA_TRADING_CAREER = 69;
    public static final int DATA_HAS_COMMAND_BLOCK = 70;
    public static final int DATA_COMMAND_BLOCK_COMMAND = 71;
    public static final int DATA_COMMAND_BLOCK_LAST_OUTPUT = 72;
    public static final int DATA_COMMAND_BLOCK_TRACK_OUTPUT = 73;
    public static final int DATA_CONTROLLING_RIDER_SEAT_NUMBER = 74;
    public static final int DATA_STRENGTH = 75;
    public static final int DATA_MAX_STRENGTH = 76;
    public static final int DATA_SPELL_CASTING_COLOR = 77;
    public static final int DATA_LIMITED_LIFE = 78;
    public static final int DATA_ARMOR_STAND_POSE_INDEX = 79;
    public static final int DATA_ENDER_CRYSTAL_TIME_OFFSET = 80;
    public static final int DATA_ALWAYS_SHOW_NAMETAG = 81;
    public static final int DATA_COLOR_2 = 82;
    public static final int DATA_NAME_AUTHOR = 83;
    public static final int DATA_SCORE_TAG = 84;
    public static final int DATA_BALLOON_ATTACHED_ENTITY = 85;
    public static final int DATA_PUFFERFISH_SIZE = 86;
    public static final int DATA_BUBBLE_TIME = 87;
    public static final int DATA_AGENT = 88;
    public static final int DATA_SITTING_AMOUNT = 89;
    public static final int DATA_SITTING_AMOUNT_PREVIOUS = 90;
    public static final int DATA_EATING_COUNTER = 91;
    public static final int DATA_FLAGS_EXTENDED = 92;
    public static final int DATA_LAYING_AMOUNT = 93;
    public static final int DATA_LAYING_AMOUNT_PREVIOUS = 94;
    public static final int DATA_DURATION = 95;
    public static final int DATA_SPAWN_TIME = 96;
    public static final int DATA_CHANGE_RATE = 97;
    public static final int DATA_CHANGE_ON_PICKUP = 98;
    public static final int DATA_PICKUP_COUNT = 99;
    public static final int DATA_INTERACTIVE_TAG = 100;
    public static final int DATA_TRADE_TIER = 101;
    public static final int DATA_MAX_TRADE_TIER = 102;
    public static final int DATA_TRADE_EXPERIENCE = 103;
    public static final int DATA_SKIN_ID = 104;
    public static final int DATA_SPAWNING_FRAMES = 105;
    public static final int DATA_COMMAND_BLOCK_TICK_DELAY = 106;
    public static final int DATA_COMMAND_BLOCK_EXECUTE_ON_FIRST_TICK = 107;
    public static final int DATA_AMBIENT_SOUND_INTERVAL = 108;
    public static final int DATA_AMBIENT_SOUND_INTERVAL_RANGE = 109;
    public static final int DATA_AMBIENT_SOUND_EVENT_NAME = 110;
    public static final int DATA_FALL_DAMAGE_MULTIPLIER = 111;
    public static final int DATA_NAME_RAW_TEXT = 112;
    public static final int DATA_CAN_RIDE_TARGET = 113;
    public static final int DATA_LOW_TIER_CURED_DISCOUNT = 114;
    public static final int DATA_HIGH_TIER_CURED_DISCOUNT = 115;
    public static final int DATA_NEARBY_CURED_DISCOUNT = 116;
    public static final int DATA_NEARBY_CURED_DISCOUNT_TIMESTAMP = 117;
    public static final int DATA_HITBOX = 118;
    public static final int DATA_IS_BUOYANT = 119;
    public static final int DATA_FREEZING_EFFECT_STRENGTH = 120;
    public static final int DATA_BUOYANCY_DATA = 121;
    public static final int DATA_GOAT_HORN_COUNT = 122;
    public static final int DATA_BASE_RUNTIME_ID = 123;
    public static final int DATA_MOVEMENT_SOUND_DISTANCE_OFFSET = 124;
    public static final int DATA_HEARTBEAT_INTERVAL_TICKS = 125;
    public static final int DATA_HEARTBEAT_SOUND_EVENT = 126;
    public static final int DATA_PLAYER_LAST_DEATH_POS = 127;
    public static final int DATA_PLAYER_LAST_DEATH_DIMENSION = 128;
    public static final int DATA_PLAYER_HAS_DIED = 129;
    public static final int DATA_FLAG_ONFIRE = 0;
    public static final int DATA_FLAG_SNEAKING = 1;
    public static final int DATA_FLAG_RIDING = 2;
    public static final int DATA_FLAG_SPRINTING = 3;
    public static final int DATA_FLAG_ACTION = 4;
    public static final int DATA_FLAG_INVISIBLE = 5;
    public static final int DATA_FLAG_TEMPTED = 6;
    public static final int DATA_FLAG_INLOVE = 7;
    public static final int DATA_FLAG_SADDLED = 8;
    public static final int DATA_FLAG_POWERED = 9;
    public static final int DATA_FLAG_IGNITED = 10;
    public static final int DATA_FLAG_BABY = 11;
    public static final int DATA_FLAG_CONVERTING = 12;
    public static final int DATA_FLAG_CRITICAL = 13;
    public static final int DATA_FLAG_CAN_SHOW_NAMETAG = 14;
    public static final int DATA_FLAG_ALWAYS_SHOW_NAMETAG = 15;
    public static final int DATA_FLAG_IMMOBILE = 16;
    public static final int DATA_FLAG_NO_AI = 16;
    public static final int DATA_FLAG_SILENT = 17;
    public static final int DATA_FLAG_WALLCLIMBING = 18;
    public static final int DATA_FLAG_CAN_CLIMB = 19;
    public static final int DATA_FLAG_SWIMMER = 20;
    public static final int DATA_FLAG_CAN_FLY = 21;
    public static final int DATA_FLAG_WALKER = 22;
    public static final int DATA_FLAG_RESTING = 23;
    public static final int DATA_FLAG_SITTING = 24;
    public static final int DATA_FLAG_ANGRY = 25;
    public static final int DATA_FLAG_INTERESTED = 26;
    public static final int DATA_FLAG_CHARGED = 27;
    public static final int DATA_FLAG_TAMED = 28;
    public static final int DATA_FLAG_ORPHANED = 29;
    public static final int DATA_FLAG_LEASHED = 30;
    public static final int DATA_FLAG_SHEARED = 31;
    public static final int DATA_FLAG_GLIDING = 32;
    public static final int DATA_FLAG_ELDER = 33;
    public static final int DATA_FLAG_MOVING = 34;
    public static final int DATA_FLAG_BREATHING = 35;
    public static final int DATA_FLAG_CHESTED = 36;
    public static final int DATA_FLAG_STACKABLE = 37;
    public static final int DATA_FLAG_SHOWBASE = 38;
    public static final int DATA_FLAG_REARING = 39;
    public static final int DATA_FLAG_VIBRATING = 40;
    public static final int DATA_FLAG_IDLING = 41;
    public static final int DATA_FLAG_EVOKER_SPELL = 42;
    public static final int DATA_FLAG_CHARGE_ATTACK = 43;
    public static final int DATA_FLAG_WASD_CONTROLLED = 44;
    public static final int DATA_FLAG_CAN_POWER_JUMP = 45;
    public static final int DATA_FLAG_CAN_DASH = 46;
    public static final int DATA_FLAG_LINGER = 47;
    public static final int DATA_FLAG_HAS_COLLISION = 48;
    public static final int DATA_FLAG_GRAVITY = 49;
    public static final int DATA_FLAG_FIRE_IMMUNE = 50;
    public static final int DATA_FLAG_DANCING = 51;
    public static final int DATA_FLAG_ENCHANTED = 52;
    public static final int DATA_FLAG_SHOW_TRIDENT_ROPE = 53;
    public static final int DATA_FLAG_CONTAINER_PRIVATE = 54;
    public static final int DATA_FLAG_IS_TRANSFORMING = 55;
    public static final int DATA_FLAG_SPIN_ATTACK = 56;
    public static final int DATA_FLAG_SWIMMING = 57;
    public static final int DATA_FLAG_BRIBED = 58;
    public static final int DATA_FLAG_PREGNANT = 59;
    public static final int DATA_FLAG_LAYING_EGG = 60;
    public static final int DATA_FLAG_RIDER_CAN_PICK = 61;
    public static final int DATA_FLAG_TRANSITION_SETTING = 62;
    public static final int DATA_FLAG_EATING = 63;
    public static final int DATA_FLAG_LAYING_DOWN = 64;
    public static final int DATA_FLAG_SNEEZING = 65;
    public static final int DATA_FLAG_TRUSTING = 66;
    public static final int DATA_FLAG_ROLLING = 67;
    public static final int DATA_FLAG_SCARED = 68;
    public static final int DATA_FLAG_IN_SCAFFOLDING = 69;
    public static final int DATA_FLAG_OVER_SCAFFOLDING = 70;
    public static final int DATA_FLAG_FALL_THROUGH_SCAFFOLDING = 71;
    public static final int DATA_FLAG_BLOCKING = 72;
    public static final int DATA_FLAG_TRANSITION_BLOCKING = 73;
    public static final int DATA_FLAG_BLOCKED_USING_SHIELD = 74;
    public static final int DATA_FLAG_BLOCKED_USING_DAMAGED_SHIELD = 75;
    public static final int DATA_FLAG_SLEEPING = 76;
    public static final int DATA_FLAG_ENTITY_GROW_UP = 77;
    public static final int DATA_FLAG_TRADE_INTEREST = 78;
    public static final int DATA_FLAG_DOOR_BREAKER = 79;
    public static final int DATA_FLAG_BREAKING_OBSTRUCTION = 80;
    public static final int DATA_FLAG_DOOR_OPENER = 81;
    public static final int DATA_FLAG_IS_ILLAGER_CAPTAIN = 82;
    public static final int DATA_FLAG_STUNNED = 83;
    public static final int DATA_FLAG_ROARING = 84;
    public static final int DATA_FLAG_DELAYED_ATTACK = 85;
    public static final int DATA_FLAG_IS_AVOIDING_MOBS = 86;
    public static final int DATA_FLAG_IS_AVOIDING_BLOCKS = 87;
    public static final int DATA_FLAG_FACING_TARGET_TO_RANGE_ATTACK = 88;
    public static final int DATA_FLAG_HIDDEN_WHEN_INVISIBLE = 89;
    public static final int DATA_FLAG_IS_IN_UI = 90;
    public static final int DATA_FLAG_STALKING = 91;
    public static final int DATA_FLAG_EMOTING = 92;
    public static final int DATA_FLAG_CELEBRATING = 93;
    public static final int DATA_FLAG_ADMIRING = 94;
    public static final int DATA_FLAG_CELEBRATING_SPECIAL = 95;
    public static final int DATA_FLAG_OUT_OF_CONTROL = 96;
    public static final int DATA_FLAG_RAM_ATTACK = 97;
    public static final int DATA_FLAG_PLAYING_DEAD = 98;
    public static final int DATA_FLAG_IN_ASCENDABLE_BLOCK = 99;
    public static final int DATA_FLAG_OVER_DESCENDABLE_BLOCK = 100;
    public static final int DATA_FLAG_CROAKING = 101;
    public static final int DATA_FLAG_EAT_MOB = 102;
    public static final int DATA_FLAG_JUMP_GOAL_JUMP = 103;
    public static final int DATA_FLAG_EMERGING = 104;
    public static final int DATA_FLAG_SNIFFING = 105;
    public static final int DATA_FLAG_DIGGING = 106;
    public static final int DATA_FLAG_SONIC_BOOM = 107;
    public static final int DATA_FLAG_HAS_DASH_COOLDOWN = 108;
    public static final int DATA_FLAG_PUSH_TOWARDS_CLOSEST_SPACE = 109;
    public static final double STEP_CLIP_MULTIPLIER = 0.4;
    public static long entityCount = 1L;
    private static final Map<String, Class<? extends Entity>> g = new HashMap<String, Class<? extends Entity>>();
    private static final Map<String, String> f = new HashMap<String, String>();
    public final Map<Integer, Player> hasSpawned = new HashMap<Integer, Player>();
    protected final Map<Integer, Effect> effects = new ConcurrentHashMap<Integer, Effect>();
    protected long id;
    protected final EntityMetadata dataProperties = new EntityMetadata().putLong(0, 0L).putByte(3, 0).putShort(7, 400).putShort(42, 400).putString(4, "").putLong(37, -1L).putFloat(38, 1.0f);
    public final List<Entity> passengers = new ArrayList<Entity>();
    public Entity riding;
    public FullChunk chunk;
    protected EntityDamageEvent lastDamageCause;
    public List<Block> blocksAround = new ArrayList<Block>();
    public List<Block> collisionBlocks = new ArrayList<Block>();
    public double lastX;
    public double lastY;
    public double lastZ;
    public boolean firstMove = true;
    public double motionX;
    public double motionY;
    public double motionZ;
    public Vector3 temporalVector;
    public double lastMotionX;
    public double lastMotionY;
    public double lastMotionZ;
    public double lastYaw;
    public double lastPitch;
    public double lastHeadYaw;
    public double entityCollisionReduction = 0.0;
    public AxisAlignedBB boundingBox;
    public boolean onGround;
    public int deadTicks = 0;
    public int age = 0;
    public int ticksLived = 0;
    protected int airTicks = 400;
    protected boolean noFallDamage;
    protected float health = 20.0f;
    protected int maxHealth = 20;
    protected float absorption = 0.0f;
    protected float ySize = 0.0f;
    public boolean keepMovement;
    public float fallDistance = 0.0f;
    public int lastUpdate;
    public int fireTicks = 0;
    public int inPortalTicks = 0;
    public int inEndPortalTicks = 0;
    protected Position portalPos;
    public boolean noClip;
    public float scale = 1.0f;
    public CompoundTag namedTag;
    public boolean isCollided;
    public boolean isCollidedHorizontally;
    public boolean isCollidedVertically;
    public int noDamageTicks;
    public boolean justCreated;
    public boolean fireProof;
    public boolean invulnerable;
    private boolean i;
    private boolean j;
    private boolean d;
    private boolean h;
    private boolean e;
    protected Server server;
    public double highestPosition;
    public boolean closed;
    protected Timing timing;
    public final boolean isPlayer = this instanceof Player;
    private volatile boolean c;
    private volatile boolean a;
    private Vector3 b;

    public abstract int getNetworkId();

    public float getHeight() {
        return 0.0f;
    }

    public float getEyeHeight() {
        return this.getHeight() / 2.0f + 0.1f;
    }

    public float getWidth() {
        return 0.0f;
    }

    public float getLength() {
        return 0.0f;
    }

    protected double getStepHeight() {
        return 0.0;
    }

    public boolean canCollide() {
        return true;
    }

    public boolean canBeFollowed() {
        return true;
    }

    protected float getGravity() {
        return 0.0f;
    }

    protected float getDrag() {
        return 0.0f;
    }

    protected float getBaseOffset() {
        return 0.0f;
    }

    public Entity(FullChunk fullChunk, CompoundTag compoundTag) {
        if (!this.isPlayer) {
            this.init(fullChunk, compoundTag);
        }
    }

    protected void initEntity() {
        block6: {
            if (this.a) {
                throw new RuntimeException("Entity is already initialized: " + this.getName() + " (" + this.id + ')');
            }
            this.a = true;
            if (this.namedTag.contains("ActiveEffects")) {
                ListTag<CompoundTag> listTag = this.namedTag.getList("ActiveEffects", CompoundTag.class);
                for (CompoundTag compoundTag : listTag.getAll()) {
                    Effect effect = Effect.getEffect(compoundTag.getByte("Id"));
                    if (effect == null) continue;
                    effect.setAmplifier(compoundTag.getByte("Amplifier")).setDuration(compoundTag.getInt("Duration")).setVisible(compoundTag.getBoolean("ShowParticles"));
                    this.addEffect(effect);
                }
            }
            if (this.namedTag.contains("CustomName")) {
                this.setNameTag(this.namedTag.getString("CustomName"));
                if (this.namedTag.contains("CustomNameVisible")) {
                    this.setNameTagVisible(this.namedTag.getBoolean("CustomNameVisible"));
                }
                if (this.namedTag.contains("CustomNameAlwaysVisible")) {
                    this.setNameTagAlwaysVisible(this.namedTag.getBoolean("CustomNameAlwaysVisible"));
                }
            }
            this.setDataFlag(0, 48, true, false);
            this.dataProperties.putFloat(54, this.getHeight());
            this.dataProperties.putFloat(53, this.getWidth());
            this.dataProperties.putInt(1, (int)this.health);
            this.scheduleUpdate();
            if (!(this instanceof Player)) break block6;
            this.sendData((Player)this);
        }
    }

    protected final void init(FullChunk fullChunk, CompoundTag compoundTag) {
        float f2;
        if (fullChunk == null || fullChunk.getProvider() == null) {
            throw new ChunkException("Invalid garbage Chunk given to Entity");
        }
        if (this.c) {
            throw new RuntimeException("Entity is already initialized: " + this.getName() + " (" + this.id + ')');
        }
        this.c = true;
        this.timing = Timings.getEntityTiming(this);
        this.temporalVector = new Vector3();
        this.id = entityCount++;
        this.justCreated = true;
        this.namedTag = compoundTag;
        this.chunk = fullChunk;
        this.setLevel(fullChunk.getProvider().getLevel());
        this.server = fullChunk.getProvider().getLevel().getServer();
        this.boundingBox = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        ListTag<DoubleTag> listTag = this.namedTag.getList("Pos", DoubleTag.class);
        ListTag<FloatTag> listTag2 = this.namedTag.getList("Rotation", FloatTag.class);
        ListTag<DoubleTag> listTag3 = this.namedTag.getList("Motion", DoubleTag.class);
        float f3 = listTag2.get((int)0).data;
        if (!(f3 >= 0.0f) || !(f3 <= 360.0f)) {
            f3 = 0.0f;
        }
        if (!((f2 = listTag2.get((int)1).data) >= 0.0f) || !(f2 <= 360.0f)) {
            f2 = 0.0f;
        }
        this.setPositionAndRotation(this.temporalVector.setComponents(listTag.get((int)0).data, listTag.get((int)1).data, listTag.get((int)2).data), f3, f2);
        this.setMotion(this.temporalVector.setComponents(listTag3.get((int)0).data, listTag3.get((int)1).data, listTag3.get((int)2).data));
        if (!this.namedTag.contains("FallDistance")) {
            this.namedTag.putFloat("FallDistance", 0.0f);
        }
        this.fallDistance = this.namedTag.getFloat("FallDistance");
        this.highestPosition = this.y + (double)this.namedTag.getFloat("FallDistance");
        if (!this.namedTag.contains("Fire") || this.namedTag.getShort("Fire") > Short.MAX_VALUE) {
            this.namedTag.putShort("Fire", 0);
        }
        this.fireTicks = this.namedTag.getShort("Fire");
        if (!this.namedTag.contains("Air")) {
            this.namedTag.putShort("Air", 400);
        }
        this.setDataProperty(new ShortEntityData(7, this.namedTag.getShort("Air")), false);
        if (!this.namedTag.contains("OnGround")) {
            this.namedTag.putBoolean("OnGround", false);
        }
        this.onGround = this.namedTag.getBoolean("OnGround");
        if (!this.namedTag.contains("Invulnerable")) {
            this.namedTag.putBoolean("Invulnerable", false);
        }
        this.invulnerable = this.namedTag.getBoolean("Invulnerable");
        if (!this.namedTag.contains("Scale")) {
            this.namedTag.putFloat("Scale", 1.0f);
        }
        if (!(this instanceof BaseEntity)) {
            this.scale = this.namedTag.getFloat("Scale");
            this.setDataProperty(new FloatEntityData(38, this.scale), false);
        }
        this.chunk.addEntity(this);
        if (this.closed) {
            return;
        }
        this.level.addEntity(this);
        this.initEntity();
        this.lastUpdate = this.server.getTick();
        this.server.getPluginManager().callEvent(new EntitySpawnEvent(this));
        this.scheduleUpdate();
    }

    public boolean hasCustomName() {
        return !this.getNameTag().isEmpty();
    }

    public String getNameTag() {
        return this.getDataPropertyString(4);
    }

    public boolean isNameTagVisible() {
        return this.getDataFlag(0, 14);
    }

    public boolean isNameTagAlwaysVisible() {
        return this.getDataPropertyByte(81) == 1;
    }

    public void setNameTag(String string) {
        this.setDataProperty(new StringEntityData(4, string));
    }

    public void setNameTagVisible() {
        this.setNameTagVisible(true);
    }

    public void setNameTagVisible(boolean bl) {
        this.setDataFlag(0, 14, bl);
    }

    public void setNameTagAlwaysVisible() {
        this.setNameTagAlwaysVisible(true);
    }

    public void setNameTagAlwaysVisible(boolean bl) {
        this.setDataProperty(new ByteEntityData(81, bl ? 1 : 0));
    }

    public void setScoreTag(String string) {
        this.setDataProperty(new StringEntityData(84, string));
    }

    public String getScoreTag() {
        return this.getDataPropertyString(84);
    }

    public boolean isSneaking() {
        return this.e;
    }

    public void setSneaking() {
        this.setSneaking(true);
    }

    public void setSneaking(boolean bl) {
        block0: {
            if (this.e == bl) break block0;
            this.e = bl;
            this.setDataFlag(0, 1, bl);
        }
    }

    public boolean isSwimming() {
        return this.h;
    }

    public void setSwimming() {
        this.setSwimming(true);
    }

    public void setSwimming(boolean bl) {
        block0: {
            if (this.h == bl) break block0;
            this.h = bl;
            this.setDataFlag(0, 57, bl);
            this.recalculateBoundingBox(true);
        }
    }

    public boolean isSprinting() {
        return this.d;
    }

    public void setSprinting() {
        this.setSprinting(true);
    }

    public void setSprinting(boolean bl) {
        block0: {
            if (this.d == bl) break block0;
            this.d = bl;
            this.setDataFlag(0, 3, bl);
        }
    }

    public boolean isGliding() {
        return this.i;
    }

    public void setGliding() {
        this.setGliding(true);
    }

    public void setGliding(boolean bl) {
        block0: {
            if (this.i == bl) break block0;
            this.i = bl;
            this.setDataFlag(0, 32, bl);
            this.recalculateBoundingBox(true);
        }
    }

    public boolean isImmobile() {
        return this.j;
    }

    public void setImmobile() {
        this.setImmobile(true);
    }

    public void setImmobile(boolean bl) {
        this.j = bl;
        this.setDataFlag(0, 16, bl);
    }

    public boolean canClimb() {
        return this.getDataFlag(0, 19);
    }

    public void setCanClimb() {
        this.setCanClimb(true);
    }

    public void setCanClimb(boolean bl) {
        this.setDataFlag(0, 19, bl);
    }

    public boolean canClimbWalls() {
        return this.getDataFlag(0, 18);
    }

    public void setCanClimbWalls() {
        this.setCanClimbWalls(true);
    }

    public void setCanClimbWalls(boolean bl) {
        this.setDataFlag(0, 18, bl);
    }

    public void setScale(float f2) {
        block0: {
            if (this.scale == f2) break block0;
            this.scale = f2;
            this.setDataProperty(new FloatEntityData(38, this.scale));
            this.recalculateBoundingBox(true);
        }
    }

    public float getScale() {
        return this.scale;
    }

    public List<Entity> getPassengers() {
        return this.passengers;
    }

    public Entity getPassenger() {
        return Iterables.getFirst(this.passengers, null);
    }

    public boolean isPassenger(Entity entity) {
        return this.passengers.contains(entity);
    }

    public boolean isControlling(Entity entity) {
        return this.passengers.indexOf(entity) == 0;
    }

    public boolean hasControllingPassenger() {
        return !this.passengers.isEmpty() && this.isControlling(this.passengers.get(0));
    }

    public Entity getRiding() {
        return this.riding;
    }

    public Map<Integer, Effect> getEffects() {
        return this.effects;
    }

    public void removeAllEffects() {
        for (Effect effect : this.effects.values()) {
            this.removeEffect(effect.getId());
        }
    }

    public void removeEffect(int n) {
        if (this.effects.containsKey(n)) {
            Effect effect = this.effects.get(n);
            this.effects.remove(n);
            effect.remove(this);
            this.recalculateEffectColor();
        }
    }

    public Effect getEffect(int n) {
        return this.effects.getOrDefault(n, null);
    }

    public boolean hasEffect(int n) {
        return this.effects.containsKey(n);
    }

    public void addEffect(Effect effect) {
        block1: {
            if (effect == null) {
                return;
            }
            effect.add(this);
            this.effects.put(effect.getId(), effect);
            this.recalculateEffectColor();
            if (effect.getId() != 21) break block1;
            this.setHealth(this.health + (float)(effect.getAmplifier() + 1 << 2));
        }
    }

    protected static void addEffectFromTippedArrow(Entity entity, Effect effect, float f2) {
        switch (effect.getId()) {
            case 6: {
                entity.heal(new EntityRegainHealthEvent(entity, 4 * (effect.getAmplifier() + 1), 2));
                break;
            }
            case 7: {
                float f3 = (float)(effect.getAmplifier() < 1 ? 6 : 12) - f2;
                if (!(f3 > 0.0f)) break;
                EntityDamageEvent entityDamageEvent = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.MAGIC, f3);
                entity.attack(entityDamageEvent);
                break;
            }
            default: {
                effect.add(entity);
                entity.effects.put(effect.getId(), effect);
                entity.recalculateEffectColor();
            }
        }
    }

    public void recalculateBoundingBox() {
        this.recalculateBoundingBox(false);
    }

    public void recalculateBoundingBox(boolean bl) {
        float f2 = this.getHeight();
        double d2 = (double)(this.getWidth() * this.scale) / 2.0;
        this.boundingBox.setBounds(this.x - d2, this.y + (double)this.ySize, this.z - d2, this.x + d2, this.y + (double)(f2 * this.scale) + (double)this.ySize, this.z + d2);
        if (bl) {
            FloatEntityData floatEntityData = new FloatEntityData(54, f2);
            FloatEntityData floatEntityData2 = new FloatEntityData(53, this.getWidth());
            this.dataProperties.put(floatEntityData);
            this.dataProperties.put(floatEntityData2);
            this.sendData(this.hasSpawned.values().toArray(new Player[0]), new EntityMetadata().put(floatEntityData).put(floatEntityData2));
        }
    }

    protected void recalculateEffectColor() {
        int[] nArray = new int[3];
        int n = 0;
        boolean bl = true;
        for (Effect effect : this.effects.values()) {
            if (!effect.isVisible()) continue;
            int[] nArray2 = effect.getColor();
            nArray[0] = nArray[0] + nArray2[0] * (effect.getAmplifier() + 1);
            nArray[1] = nArray[1] + nArray2[1] * (effect.getAmplifier() + 1);
            nArray[2] = nArray[2] + nArray2[2] * (effect.getAmplifier() + 1);
            n += effect.getAmplifier() + 1;
            if (effect.isAmbient()) continue;
            bl = false;
        }
        if (n > 0) {
            int n2 = nArray[0] / n & 0xFF;
            int n3 = nArray[1] / n & 0xFF;
            int n4 = nArray[2] / n & 0xFF;
            this.setDataProperty(new IntEntityData(8, (n2 << 16) + (n3 << 8) + n4));
            this.setDataProperty(new ByteEntityData(9, bl ? 1 : 0));
        } else {
            this.setDataProperty(new IntEntityData(8, 0));
            this.setDataProperty(new ByteEntityData(9, 0));
        }
    }

    public static Entity createEntity(String string, Position position, Object ... objectArray) {
        return Entity.createEntity(string, position.getChunk(), Entity.getDefaultNBT(position), objectArray);
    }

    public static Entity createEntity(int n, Position position, Object ... objectArray) {
        return Entity.createEntity(String.valueOf(n), position.getChunk(), Entity.getDefaultNBT(position), objectArray);
    }

    public static Entity createEntity(String string, FullChunk fullChunk, CompoundTag compoundTag, Object ... objectArray) {
        Class<? extends Entity> clazz = g.get(string);
        if (clazz == null) {
            EntityDefinition entityDefinition = EntityManager.get().getDefinition(string);
            if (entityDefinition == null) {
                return null;
            }
            clazz = entityDefinition.getImplementation();
        }
        return Entity.a(clazz, fullChunk, compoundTag, objectArray);
    }

    public static Entity createEntity(int n, FullChunk fullChunk, CompoundTag compoundTag, Object ... objectArray) {
        String string = String.valueOf(n);
        if (g.containsKey(string)) {
            return Entity.createEntity(string, fullChunk, compoundTag, objectArray);
        }
        EntityDefinition entityDefinition = EntityManager.get().getDefinition(n);
        if (entityDefinition != null) {
            return Entity.a(entityDefinition.getImplementation(), fullChunk, compoundTag, objectArray);
        }
        return null;
    }

    private static Entity a(Class<? extends Entity> clazz, FullChunk fullChunk, CompoundTag compoundTag, Object ... objectArray) {
        Entity entity = null;
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (entity != null) break;
            if (constructor.getParameterCount() != (objectArray == null ? 2 : objectArray.length + 2)) continue;
            try {
                if (objectArray == null || objectArray.length == 0) {
                    entity = (Entity)constructor.newInstance(fullChunk, compoundTag);
                    continue;
                }
                Object[] objectArray2 = new Object[objectArray.length + 2];
                objectArray2[0] = fullChunk;
                objectArray2[1] = compoundTag;
                System.arraycopy(objectArray, 0, objectArray2, 2, objectArray.length);
                entity = (Entity)constructor.newInstance(objectArray2);
            }
            catch (Exception exception) {
                MainLogger.getLogger().logException(exception);
            }
        }
        return entity;
    }

    public static boolean registerEntity(String string, Class<? extends Entity> clazz) {
        return Entity.registerEntity(string, clazz, false);
    }

    public static boolean registerEntity(String string, Class<? extends Entity> clazz, boolean bl) {
        block3: {
            if (clazz == null) {
                return false;
            }
            try {
                int n = clazz.getField("NETWORK_ID").getInt(null);
                g.put(String.valueOf(n), clazz);
            }
            catch (Exception exception) {
                if (bl) break block3;
                return false;
            }
        }
        g.put(string, clazz);
        f.put(clazz.getSimpleName(), string);
        return true;
    }

    public static CompoundTag getDefaultNBT(Vector3 vector3) {
        return Entity.getDefaultNBT(vector3, null);
    }

    public static CompoundTag getDefaultNBT(Vector3 vector3, Vector3 vector32) {
        Location location;
        Location location2 = location = vector3 instanceof Location ? (Location)vector3 : null;
        if (location != null) {
            return Entity.getDefaultNBT(vector3, vector32, (float)location.getYaw(), (float)location.getPitch());
        }
        return Entity.getDefaultNBT(vector3, vector32, 0.0f, 0.0f);
    }

    public static CompoundTag getDefaultNBT(Vector3 vector3, Vector3 vector32, float f2, float f3) {
        return new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", vector3.x)).add(new DoubleTag("", vector3.y)).add(new DoubleTag("", vector3.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", vector32 != null ? vector32.x : 0.0)).add(new DoubleTag("", vector32 != null ? vector32.y : 0.0)).add(new DoubleTag("", vector32 != null ? vector32.z : 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", f2)).add(new FloatTag("", f3)));
    }

    public void saveNBT() {
        if (!(this instanceof Player)) {
            this.namedTag.putString("id", this.getSaveId());
            if (!this.getNameTag().isEmpty()) {
                this.namedTag.putString("CustomName", this.getNameTag());
                this.namedTag.putBoolean("CustomNameVisible", this.isNameTagVisible());
                this.namedTag.putBoolean("CustomNameAlwaysVisible", this.isNameTagAlwaysVisible());
            } else {
                this.namedTag.remove("CustomName");
                this.namedTag.remove("CustomNameVisible");
                this.namedTag.remove("CustomNameAlwaysVisible");
            }
        }
        this.namedTag.putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("0", this.x)).add(new DoubleTag("1", this.y)).add(new DoubleTag("2", this.z)));
        this.namedTag.putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("0", this.motionX)).add(new DoubleTag("1", this.motionY)).add(new DoubleTag("2", this.motionZ)));
        this.namedTag.putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("0", (float)this.yaw)).add(new FloatTag("1", (float)this.pitch)));
        this.namedTag.putFloat("FallDistance", this.fallDistance);
        this.namedTag.putShort("Fire", this.fireTicks);
        this.namedTag.putShort("Air", this.airTicks);
        this.namedTag.putBoolean("OnGround", this.onGround);
        this.namedTag.putBoolean("Invulnerable", this.invulnerable);
        this.namedTag.putFloat("Scale", this.scale);
        if (!this.effects.isEmpty()) {
            ListTag<CompoundTag> listTag = new ListTag<CompoundTag>("ActiveEffects");
            for (Effect effect : this.effects.values()) {
                listTag.add(new CompoundTag(String.valueOf(effect.getId())).putByte("Id", effect.getId()).putByte("Amplifier", effect.getAmplifier()).putInt("Duration", effect.getDuration()).putBoolean("Ambient", false).putBoolean("ShowParticles", effect.isVisible()));
            }
            this.namedTag.putList(listTag);
        } else {
            this.namedTag.remove("ActiveEffects");
        }
    }

    public String getName() {
        if (this.hasCustomName()) {
            return this.getNameTag();
        }
        return this.getSaveId();
    }

    public final String getSaveId() {
        if (this instanceof CustomEntity) {
            EntityDefinition entityDefinition = ((CustomEntity)((Object)this)).getEntityDefinition();
            return entityDefinition == null ? "" : entityDefinition.getIdentifier();
        }
        return f.getOrDefault(this.getClass().getSimpleName(), "");
    }

    public void spawnTo(Player player) {
        Boolean bl;
        if (!this.c || !this.a) {
            this.server.getLogger().warning("Spawned an entity that is not initialized yet: " + this.getClass().getName() + " (" + this.id + ')');
        }
        if (!this.hasSpawned.containsKey(player.getLoaderId()) && (bl = player.usedChunks.get(Level.chunkHash(this.chunk.getX(), this.chunk.getZ()))) != null && bl.booleanValue()) {
            DataPacket dataPacket;
            player.dataPacket(this.createAddEntityPacket());
            this.hasSpawned.put(player.getLoaderId(), player);
            if (this.riding != null) {
                this.riding.spawnTo(player);
                dataPacket = new SetEntityLinkPacket();
                dataPacket.vehicleUniqueId = this.riding.id;
                dataPacket.riderUniqueId = this.id;
                dataPacket.type = 1;
                dataPacket.immediate = 1;
                player.dataPacket(dataPacket);
            }
            if (this.server.vanillaBossBar && this instanceof EntityBoss) {
                dataPacket = new BossEventPacket();
                ((BossEventPacket)dataPacket).bossEid = this.id;
                ((BossEventPacket)dataPacket).type = 0;
                ((BossEventPacket)dataPacket).title = this.getName();
                ((BossEventPacket)dataPacket).healthPercent = player.protocol >= 361 ? this.health / 100.0f : this.health;
                player.dataPacket(dataPacket);
            }
        }
    }

    protected DataPacket createAddEntityPacket() {
        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.type = this.getNetworkId();
        addEntityPacket.entityUniqueId = this.id;
        addEntityPacket.entityRuntimeId = this.id;
        addEntityPacket.yaw = (float)this.yaw;
        addEntityPacket.headYaw = (float)this.yaw;
        addEntityPacket.pitch = (float)this.pitch;
        addEntityPacket.x = (float)this.x;
        addEntityPacket.y = (float)this.y + this.getBaseOffset();
        addEntityPacket.z = (float)this.z;
        addEntityPacket.speedX = (float)this.motionX;
        addEntityPacket.speedY = (float)this.motionY;
        addEntityPacket.speedZ = (float)this.motionZ;
        addEntityPacket.metadata = this.dataProperties.clone();
        addEntityPacket.links = new EntityLink[this.passengers.size()];
        for (int k = 0; k < addEntityPacket.links.length; ++k) {
            addEntityPacket.links[k] = new EntityLink(this.id, this.passengers.get((int)k).id, k == 0 ? (byte)1 : 2, false, false);
        }
        return addEntityPacket;
    }

    public Map<Integer, Player> getViewers() {
        return this.hasSpawned;
    }

    public void sendPotionEffects(Player player) {
        for (Effect effect : this.effects.values()) {
            MobEffectPacket mobEffectPacket = new MobEffectPacket();
            mobEffectPacket.eid = this.id;
            mobEffectPacket.effectId = effect.getId();
            mobEffectPacket.amplifier = effect.getAmplifier();
            mobEffectPacket.particles = effect.isVisible();
            mobEffectPacket.duration = effect.getDuration();
            mobEffectPacket.eventId = 1;
            player.dataPacket(mobEffectPacket);
        }
    }

    public void sendData(Player player) {
        this.sendData(player, null);
    }

    public void sendData(Player player, EntityMetadata entityMetadata) {
        SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
        setEntityDataPacket.eid = this.id;
        setEntityDataPacket.metadata = entityMetadata == null ? this.dataProperties.clone() : entityMetadata;
        player.dataPacket(setEntityDataPacket);
    }

    public void sendData(Player[] playerArray) {
        this.sendData(playerArray, null);
    }

    public void sendData(Player[] playerArray, EntityMetadata entityMetadata) {
        SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
        setEntityDataPacket.eid = this.id;
        for (Player player : playerArray) {
            if (player == this) continue;
            setEntityDataPacket.metadata = entityMetadata == null ? this.dataProperties.clone() : entityMetadata;
            player.dataPacket(setEntityDataPacket);
        }
        if (this instanceof Player) {
            setEntityDataPacket.metadata = entityMetadata == null ? this.dataProperties.clone() : entityMetadata;
            ((Player)this).dataPacket(setEntityDataPacket);
        }
    }

    public void despawnFrom(Player player) {
        if (this.hasSpawned.containsKey(player.getLoaderId())) {
            this.hasSpawned.remove(player.getLoaderId());
            RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
            removeEntityPacket.eid = this.id;
            player.dataPacket(removeEntityPacket);
        }
    }

    private static boolean a(Player player) {
        if (player.isOnGround() || player.riding != null || player.speed == null || player.speed.y <= 0.0 || player.hasEffect(15)) {
            return false;
        }
        int n = player.getLevel().getBlockIdAt(player.chunk, player.getFloorX(), player.getFloorY(), player.getFloorZ());
        return n != 65 && n != 106 && !Block.hasWater(n);
    }

    /*
     * WARNING - void declaration
     */
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        block18: {
            Entity entity;
            Enchantment[] enchantmentArray;
            if (this.hasEffect(12) && (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.LAVA)) {
                return false;
            }
            if (this instanceof EntityLiving && entityDamageEvent instanceof EntityDamageByEntityEvent && !(entityDamageEvent instanceof EntityDamageByChildEntityEvent) && (enchantmentArray = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager()) instanceof Player && Entity.a((Player)enchantmentArray)) {
                entityDamageEvent.setDamage(entityDamageEvent.getFinalDamage() * 0.5f, EntityDamageEvent.DamageModifier.CRITICAL);
            }
            this.server.getPluginManager().callEvent(entityDamageEvent);
            if (entityDamageEvent.isCancelled()) {
                return false;
            }
            if (entityDamageEvent.isApplicable(EntityDamageEvent.DamageModifier.CRITICAL)) {
                enchantmentArray = new AnimatePacket();
                enchantmentArray.action = AnimatePacket.Action.CRITICAL_HIT;
                enchantmentArray.eid = this.getId();
                this.getLevel().addChunkPacket(this.getChunkX(), this.getChunkZ(), (DataPacket)enchantmentArray);
                this.getLevel().addLevelSoundEvent(this, 43);
            }
            if (entityDamageEvent instanceof EntityDamageByEntityEvent) {
                enchantmentArray = ((EntityDamageByEntityEvent)entityDamageEvent).getWeaponEnchantments();
                if (enchantmentArray != null) {
                    for (Enchantment cloneable : enchantmentArray) {
                        cloneable.doAttack(((EntityDamageByEntityEvent)entityDamageEvent).getDamager(), this);
                    }
                }
                if (entityDamageEvent.getEntity() instanceof Player) {
                    for (Cloneable cloneable : entityDamageEvent.getEntity().getLevel().getNearbyEntities(entityDamageEvent.getEntity().getBoundingBox().grow(17.0, 17.0, 17.0), entityDamageEvent.getEntity())) {
                        if (!(cloneable instanceof EntityWolf) || !((EntityWolf)cloneable).hasOwner()) continue;
                        ((EntityWolf)cloneable).isAngryTo = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager().getId();
                        ((EntityWolf)cloneable).setAngry(true);
                    }
                } else if (((EntityDamageByEntityEvent)entityDamageEvent).getDamager() instanceof Player) {
                    for (Cloneable cloneable : ((EntityDamageByEntityEvent)entityDamageEvent).getDamager().getLevel().getNearbyEntities(((EntityDamageByEntityEvent)entityDamageEvent).getDamager().getBoundingBox().grow(17.0, 17.0, 17.0), ((EntityDamageByEntityEvent)entityDamageEvent).getDamager())) {
                        if (((Entity)cloneable).getId() == entityDamageEvent.getEntity().getId() || !(cloneable instanceof EntityWolf) || !((EntityWolf)cloneable).hasOwner() || !((EntityWolf)cloneable).getOwner().equals(((EntityDamageByEntityEvent)entityDamageEvent).getDamager())) continue;
                        ((EntityWolf)cloneable).isAngryTo = entityDamageEvent.getEntity().getId();
                        ((EntityWolf)cloneable).setAngry(true);
                    }
                }
            }
            float f2 = entityDamageEvent.getFinalDamage();
            float f3 = this.absorption;
            if (f3 > 0.0f) {
                this.setAbsorption(Math.max(0.0f, f3 - f2));
                f2 = Math.max(0.0f, f2 - f3);
            }
            this.setLastDamageCause(entityDamageEvent);
            float f4 = this.health - f2;
            if (f4 < 1.0f && this instanceof Player && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.VOID && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.SUICIDE) {
                void var6_20;
                Player player = (Player)this;
                boolean bl = false;
                if (player.getOffhandInventory().getItemFast(0).getId() == 450) {
                    player.getOffhandInventory().clear(0);
                    boolean bl2 = true;
                } else if (player.getInventory().getItemInHandFast().getId() == 450) {
                    player.getInventory().clear(player.getInventory().getHeldItemIndex());
                    boolean bl3 = true;
                }
                if (var6_20 != false) {
                    this.getLevel().addLevelEvent(this, 1052);
                    this.getLevel().addParticle(new GenericParticle(this, 50));
                    this.extinguish();
                    this.removeAllEffects();
                    this.setHealth(1.0f);
                    this.addEffect(Effect.getEffect(10).setDuration(800).setAmplifier(1));
                    this.addEffect(Effect.getEffect(12).setDuration(800));
                    this.addEffect(Effect.getEffect(22).setDuration(100).setAmplifier(1));
                    EntityEventPacket entityEventPacket = new EntityEventPacket();
                    entityEventPacket.eid = this.getId();
                    entityEventPacket.event = 65;
                    player.dataPacket(entityEventPacket);
                    entityDamageEvent.setCancelled(true);
                    return false;
                }
            }
            this.setHealth(f4);
            if (!(f2 >= 18.0f) || !(entityDamageEvent instanceof EntityDamageByEntityEvent) || entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK || !((entity = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager()) instanceof Player)) break block18;
            ((Player)entity).awardAchievement("overkill");
        }
        return true;
    }

    public boolean attack(float f2) {
        return this.attack(new EntityDamageEvent(this, EntityDamageEvent.DamageCause.CUSTOM, f2));
    }

    public void heal(EntityRegainHealthEvent entityRegainHealthEvent) {
        this.server.getPluginManager().callEvent(entityRegainHealthEvent);
        if (entityRegainHealthEvent.isCancelled()) {
            return;
        }
        this.setHealth(this.health + entityRegainHealthEvent.getAmount());
    }

    public void heal(float f2) {
        this.heal(new EntityRegainHealthEvent(this, f2, 0));
    }

    public float getHealth() {
        return this.health;
    }

    public boolean isAlive() {
        return this.health >= 1.0f;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void setHealth(float f2) {
        if (this.health == f2) {
            return;
        }
        if (f2 < 1.0f) {
            if (this.isAlive()) {
                this.kill();
            }
        } else {
            this.health = f2 <= (float)this.getMaxHealth() || f2 < this.health ? f2 : (float)this.getMaxHealth();
        }
    }

    public void setLastDamageCause(EntityDamageEvent entityDamageEvent) {
        this.lastDamageCause = entityDamageEvent;
    }

    public EntityDamageEvent getLastDamageCause() {
        return this.lastDamageCause;
    }

    public int getMaxHealth() {
        return this.maxHealth + (this.hasEffect(21) ? this.getEffect(21).getAmplifier() + 1 << 2 : 0);
    }

    public int getRealMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(int n) {
        this.maxHealth = n;
    }

    public boolean canCollideWith(Entity entity) {
        return entity != null && !this.justCreated && this != entity && !entity.noClip && !this.noClip;
    }

    protected boolean checkObstruction(double d2, double d3, double d4) {
        if (this.noClip || this.level.getCollisionCubes(this, this.boundingBox, false).length == 0) {
            return false;
        }
        int n = NukkitMath.floorDouble(d2);
        int n2 = NukkitMath.floorDouble(d3);
        int n3 = NukkitMath.floorDouble(d4);
        double d5 = d2 - (double)n;
        double d6 = d3 - (double)n2;
        double d7 = d4 - (double)n3;
        if (!Block.transparent[this.level.getBlockIdAt(n, n2, n3)]) {
            boolean bl = Block.transparent[this.level.getBlockIdAt(n - 1, n2, n3)];
            boolean bl2 = Block.transparent[this.level.getBlockIdAt(n + 1, n2, n3)];
            boolean bl3 = Block.transparent[this.level.getBlockIdAt(n, n2 - 1, n3)];
            boolean bl4 = Block.transparent[this.level.getBlockIdAt(n, n2 + 1, n3)];
            boolean bl5 = Block.transparent[this.level.getBlockIdAt(n, n2, n3 - 1)];
            boolean bl6 = Block.transparent[this.level.getBlockIdAt(n, n2, n3 + 1)];
            int n4 = -1;
            double d8 = 9999.0;
            if (bl) {
                d8 = d5;
                n4 = 0;
            }
            if (bl2 && 1.0 - d5 < d8) {
                d8 = 1.0 - d5;
                n4 = 1;
            }
            if (bl3 && d6 < d8) {
                d8 = d6;
                n4 = 2;
            }
            if (bl4 && 1.0 - d6 < d8) {
                d8 = 1.0 - d6;
                n4 = 3;
            }
            if (bl5 && d7 < d8) {
                d8 = d7;
                n4 = 4;
            }
            if (bl6 && 1.0 - d7 < d8) {
                n4 = 5;
            }
            double d9 = Utils.random.nextDouble() * 0.2 + 0.1;
            if (n4 == 0) {
                this.motionX = -d9;
                return true;
            }
            if (n4 == 1) {
                this.motionX = d9;
                return true;
            }
            if (n4 == 2) {
                this.motionY = -d9;
                return true;
            }
            if (n4 == 3) {
                this.motionY = d9;
                return true;
            }
            if (n4 == 4) {
                this.motionZ = -d9;
                return true;
            }
            if (n4 == 5) {
                this.motionZ = d9;
                return true;
            }
        }
        return false;
    }

    public boolean entityBaseTick() {
        return this.entityBaseTick(1);
    }

    public boolean entityBaseTick(int n) {
        boolean bl;
        block26: {
            if (Timings.entityBaseTickTimer != null) {
                Timings.entityBaseTickTimer.startTiming();
            }
            if (!(this instanceof Player)) {
                this.collisionBlocks = null;
            }
            this.justCreated = false;
            if (!this.isAlive()) {
                this.despawnFromAll();
                if (!(this instanceof Player)) {
                    this.close();
                }
                if (Timings.entityBaseTickTimer != null) {
                    Timings.entityBaseTickTimer.stopTiming();
                }
                return false;
            }
            this.updatePassengers();
            if (!this.effects.isEmpty()) {
                for (Effect object : this.effects.values()) {
                    if (object.canTick()) {
                        object.applyEffect(this);
                    }
                    object.setDuration(object.getDuration() - n);
                    if (object.getDuration() > 0) continue;
                    this.removeEffect(object.getId());
                }
            }
            bl = false;
            this.checkBlockCollision();
            if (this.y <= -16.0 && this.isAlive()) {
                if (this instanceof Player) {
                    if (((Player)this).getGamemode() != 1) {
                        this.attack(new EntityDamageEvent(this, EntityDamageEvent.DamageCause.VOID, 10.0f));
                    }
                } else {
                    this.attack(new EntityDamageEvent(this, EntityDamageEvent.DamageCause.VOID, 10.0f));
                    bl = true;
                }
            }
            if (this.fireTicks > 0) {
                if (this.fireProof) {
                    this.fireTicks -= n << 2;
                    if (this.fireTicks < 0) {
                        this.fireTicks = 0;
                    }
                } else {
                    if (!(this.fireTicks % 20 != 0 && n <= 20 || this.hasEffect(12))) {
                        this.attack(new EntityDamageEvent(this, EntityDamageEvent.DamageCause.FIRE_TICK, 1.0f));
                    }
                    this.fireTicks -= n;
                }
                if (this.fireTicks <= 0) {
                    this.extinguish();
                } else if (!(this.fireProof || this instanceof Player && ((Player)this).isSpectator() || this instanceof EntityBoss)) {
                    this.setDataFlag(0, 0, true);
                    bl = true;
                }
            }
            if (this.noDamageTicks > 0) {
                this.noDamageTicks -= n;
                if (this.noDamageTicks < 0) {
                    this.noDamageTicks = 0;
                }
            }
            if (this.inPortalTicks == 80 && Server.getInstance().isNetherAllowed() && this instanceof BaseEntity) {
                EntityPortalEnterEvent entityPortalEnterEvent = new EntityPortalEnterEvent(this, EntityPortalEnterEvent.PortalType.NETHER);
                this.server.getPluginManager().callEvent(entityPortalEnterEvent);
                if (!entityPortalEnterEvent.isCancelled()) {
                    if (this.getLevel().getDimension() == 1) {
                        this.switchLevel(this.server.getDefaultLevel());
                    } else {
                        this.switchLevel(this.server.getNetherWorld(this.level.getName()));
                    }
                }
            }
            this.age += n;
            this.ticksLived += n;
            ++TimingsHistory.activatedEntityTicks;
            if (Timings.entityBaseTickTimer == null) break block26;
            Timings.entityBaseTickTimer.stopTiming();
        }
        return bl;
    }

    public void updateMovement() {
        block1: {
            double d2 = (this.x - this.lastX) * (this.x - this.lastX) + (this.y - this.lastY) * (this.y - this.lastY) + (this.z - this.lastZ) * (this.z - this.lastZ);
            double d3 = (this.yaw - this.lastYaw) * (this.yaw - this.lastYaw) + (this.pitch - this.lastPitch) * (this.pitch - this.lastPitch);
            double d4 = (this.motionX - this.lastMotionX) * (this.motionX - this.lastMotionX) + (this.motionY - this.lastMotionY) * (this.motionY - this.lastMotionY) + (this.motionZ - this.lastMotionZ) * (this.motionZ - this.lastMotionZ);
            if (d2 > 1.0E-4 || d3 > 1.0) {
                this.lastX = this.x;
                this.lastY = this.y;
                this.lastZ = this.z;
                this.lastYaw = this.yaw;
                this.lastPitch = this.pitch;
                this.lastHeadYaw = this.headYaw;
                this.addMovement(this.x, this instanceof Player ? this.y : this.y + (double)this.getBaseOffset(), this.z, this.yaw, this.pitch, this.headYaw == 0.0 || this instanceof Player ? this.yaw : this.headYaw);
            }
            if (!(d4 > 0.0025) && (!(d4 > 1.0E-4) || !(this.getMotion().lengthSquared() <= 1.0E-4))) break block1;
            this.lastMotionX = this.motionX;
            this.lastMotionY = this.motionY;
            this.lastMotionZ = this.motionZ;
            this.addMotion(this.motionX, this.motionY, this.motionZ);
        }
    }

    public void addMovement(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.level.addEntityMovement(this, d2, d3, d4, d5, d6, d7);
    }

    public void addMotion(double d2, double d3, double d4) {
        SetEntityMotionPacket setEntityMotionPacket = new SetEntityMotionPacket();
        setEntityMotionPacket.eid = this.id;
        setEntityMotionPacket.motionX = (float)d2;
        setEntityMotionPacket.motionY = (float)d3;
        setEntityMotionPacket.motionZ = (float)d4;
        for (Player player : this.hasSpawned.values()) {
            player.dataPacket(setEntityMotionPacket);
        }
    }

    public Vector2 getDirectionPlane() {
        return new Vector2((float)(-Math.cos(FastMathLite.toRadians(this.yaw) - 1.5707963267948966)), (float)(-Math.sin(FastMathLite.toRadians(this.yaw) - 1.5707963267948966))).normalize();
    }

    public BlockFace getHorizontalFacing() {
        return BlockFace.fromHorizontalIndex(NukkitMath.floorDouble(this.yaw * 4.0 / 360.0 + 0.5) & 3);
    }

    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (!this.isAlive()) {
            ++this.deadTicks;
            if (this.deadTicks >= 10) {
                this.despawnFromAll();
                if (!(this instanceof Player)) {
                    this.close();
                }
            }
            return this.deadTicks < 10;
        }
        int n2 = n - this.lastUpdate;
        if (n2 <= 0) {
            return false;
        }
        this.lastUpdate = n;
        boolean bl = this.entityBaseTick(n2);
        this.updateMovement();
        return bl;
    }

    public boolean mountEntity(Entity entity) {
        return this.mountEntity(entity, (byte)1);
    }

    public boolean mountEntity(Entity entity, byte by) {
        Objects.requireNonNull(entity, "The target of the mounting entity can't be null");
        if (entity.riding != null) {
            this.dismountEntity(entity);
        } else {
            if (this.isPassenger(entity)) {
                return false;
            }
            EntityVehicleEnterEvent entityVehicleEnterEvent = new EntityVehicleEnterEvent(entity, (EntityVehicle)this);
            this.server.getPluginManager().callEvent(entityVehicleEnterEvent);
            if (entityVehicleEnterEvent.isCancelled()) {
                return false;
            }
            this.broadcastLinkPacket(entity, by);
            entity.riding = this;
            entity.setDataFlag(0, 2, true);
            this.passengers.add(entity);
            if (this instanceof EntityMinecartEmpty) {
                entity.b = new Vector3(entity.x, entity.y, entity.z);
            }
            entity.setSeatPosition(this.getMountedOffset(entity));
            this.updatePassengerPosition(entity);
        }
        return true;
    }

    public boolean dismountEntity(Entity entity) {
        if (this instanceof EntityVehicle) {
            EntityVehicleExitEvent entityVehicleExitEvent = new EntityVehicleExitEvent(entity, (EntityVehicle)this);
            this.server.getPluginManager().callEvent(entityVehicleExitEvent);
            if (entityVehicleExitEvent.isCancelled()) {
                return false;
            }
        }
        this.broadcastLinkPacket(entity, (byte)0);
        entity.riding = null;
        entity.setDataFlag(0, 2, false);
        this.passengers.remove(entity);
        entity.setSeatPosition(new Vector3f());
        this.updatePassengerPosition(entity);
        entity.resetFallDistance();
        if (this instanceof EntityMinecartEmpty) {
            if (entity instanceof Player && entity.b != null && this.distance(entity.b) >= 1000.0) {
                ((Player)entity).awardAchievement("onARail");
            }
            entity.b = null;
        }
        return true;
    }

    protected void broadcastLinkPacket(Entity entity, byte by) {
        SetEntityLinkPacket setEntityLinkPacket = new SetEntityLinkPacket();
        setEntityLinkPacket.vehicleUniqueId = this.id;
        setEntityLinkPacket.riderUniqueId = entity.id;
        setEntityLinkPacket.type = by;
        Server.broadcastPacket(this.hasSpawned.values(), (DataPacket)setEntityLinkPacket);
    }

    public void updatePassengers() {
        if (this.passengers.isEmpty()) {
            return;
        }
        for (Entity entity : new ArrayList<Entity>(this.passengers)) {
            if (!entity.isAlive()) {
                this.dismountEntity(entity);
                continue;
            }
            this.updatePassengerPosition(entity);
        }
    }

    protected void updatePassengerPosition(Entity entity) {
        entity.setPosition(this.add(entity.getSeatPosition().asVector3()));
    }

    public void setSeatPosition(Vector3f vector3f) {
        this.setDataProperty(new Vector3fEntityData(56, vector3f));
    }

    public Vector3f getSeatPosition() {
        return this.getDataPropertyVector3f(56);
    }

    public Vector3f getMountedOffset(Entity entity) {
        return new Vector3f(0.0f, this.getHeight() * 0.75f);
    }

    public final void scheduleUpdate() {
        block0: {
            if (this.closed) break block0;
            this.level.updateEntities.put(this.id, this);
        }
    }

    public boolean isOnFire() {
        return this.fireTicks > 0;
    }

    public void setOnFire(int n) {
        block0: {
            int n2 = n * 20;
            if (n2 <= this.fireTicks) break block0;
            this.fireTicks = n2;
        }
    }

    public float getAbsorption() {
        return this.absorption;
    }

    public void setAbsorption(float f2) {
        block1: {
            if (f2 == this.absorption) break block1;
            this.absorption = f2;
            if (this instanceof Player) {
                ((Player)this).setAttribute(Attribute.getAttribute(0).setValue(f2));
            }
        }
    }

    public BlockFace getDirection() {
        double d2 = this.yaw % 360.0;
        if (d2 < 0.0) {
            d2 += 360.0;
        }
        if (0.0 <= d2 && d2 < 45.0 || 315.0 <= d2 && d2 < 360.0) {
            return BlockFace.SOUTH;
        }
        if (45.0 <= d2 && d2 < 135.0) {
            return BlockFace.WEST;
        }
        if (135.0 <= d2 && d2 < 225.0) {
            return BlockFace.NORTH;
        }
        if (225.0 <= d2 && d2 < 315.0) {
            return BlockFace.EAST;
        }
        return null;
    }

    public void extinguish() {
        this.fireTicks = 0;
        this.setDataFlag(0, 0, false);
    }

    public boolean canTriggerWalking() {
        return true;
    }

    public void resetFallDistance() {
        this.highestPosition = this.y;
    }

    protected void updateFallState(boolean bl) {
        if (bl) {
            this.fallDistance = (float)(this.highestPosition - this.y);
            if (this.fallDistance > 0.0f) {
                if (this instanceof EntityLiving && !(this instanceof EntityFlying) && !Block.hasWater(this.level.getBlockIdAt(this.chunk, this.getFloorX(), this.getFloorY(), this.getFloorZ()))) {
                    this.fall(this.fallDistance);
                }
                this.resetFallDistance();
            }
        }
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void fall(float f2) {
        block7: {
            Position position;
            if (!((double)f2 > 0.75)) break block7;
            if (!this.hasEffect(27)) {
                position = this.level.getBlock(this.down());
                if (!this.noFallDamage) {
                    float f3 = (float)Math.floor(f2 - 3.0f - (float)(this.hasEffect(8) ? this.getEffect(8).getAmplifier() + 1 : 0));
                    if (((Block)position).getId() == 170) {
                        f3 -= f3 * 0.8f;
                    }
                    if (f3 > 0.0f && (!(this instanceof Player) || this.level.getGameRules().getBoolean(GameRule.FALL_DAMAGE))) {
                        this.attack(new EntityDamageEvent(this, EntityDamageEvent.DamageCause.FALL, f3));
                    }
                }
                if (((Block)position).getId() == 60) {
                    Event event = this instanceof Player ? new PlayerInteractEvent((Player)this, null, position, null, PlayerInteractEvent.Action.PHYSICAL) : new EntityInteractEvent(this, (Block)position);
                    this.server.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }
                    this.level.setBlock(position, Block.get(3), true, true);
                }
            }
            if (f2 > 5.0f && this instanceof EntityPig && ((EntityPig)this).isSaddled() && !this.passengers.isEmpty() && (position = this.passengers.get(0)) instanceof Player) {
                ((Player)position).awardAchievement("flyPig");
            }
        }
    }

    public void moveFlying(float f2, float f3, float f4) {
        float f5 = f2 * f2 + f3 * f3;
        if (f5 >= 1.0E-4f) {
            if ((f5 = MathHelper.sqrt(f5)) < 1.0f) {
                f5 = 1.0f;
            }
            f5 = f4 / f5;
            float f6 = MathHelper.sin((float)(this.yaw * 3.1415927410125732 / 180.0));
            float f7 = MathHelper.cos((float)(this.yaw * 3.1415927410125732 / 180.0));
            this.motionX += (double)((f2 *= f5) * f7 - (f3 *= f5) * f6);
            this.motionZ += (double)(f3 * f7 + f2 * f6);
        }
    }

    public void applyEntityCollision(Entity entity) {
        block2: {
            double d2;
            double d3;
            double d4;
            if (entity.riding == this || entity.passengers.contains(this) || !((d4 = NukkitMath.getDirection(d3 = entity.x - this.x, d2 = entity.z - this.z)) >= (double)0.01f)) break block2;
            d4 = MathHelper.sqrt((float)d4);
            d3 /= d4;
            d2 /= d4;
            double d5 = 1.0 / d4;
            if (d5 > 1.0) {
                d5 = 1.0;
            }
            d3 *= d5;
            d2 *= d5;
            d3 *= (double)0.05f;
            d2 *= (double)0.05f;
            d3 *= 1.0 + this.entityCollisionReduction;
            if (this.riding == null) {
                this.motionX -= d3;
                this.motionZ -= d2;
            }
        }
    }

    public void onStruckByLightning(Entity entity) {
        block0: {
            if (!this.attack(new EntityDamageByEntityEvent(entity, this, EntityDamageEvent.DamageCause.LIGHTNING, 5.0f)) || this.fireTicks >= 160) break block0;
            this.setOnFire(8);
        }
    }

    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        return this.onInteract(player, item);
    }

    public boolean onInteract(Player player, Item item) {
        return false;
    }

    protected boolean switchLevel(Level level) {
        if (this.closed) {
            return false;
        }
        if (this.isValid()) {
            EntityLevelChangeEvent entityLevelChangeEvent = new EntityLevelChangeEvent(this, this.level, level);
            this.server.getPluginManager().callEvent(entityLevelChangeEvent);
            if (entityLevelChangeEvent.isCancelled()) {
                return false;
            }
            this.level.removeEntity(this);
            if (this.chunk != null) {
                this.chunk.removeEntity(this);
            }
            this.despawnFromAll();
            this.preSwitchLevel();
        }
        this.setLevel(level);
        this.level.addEntity(this);
        this.chunk = null;
        this.afterSwitchLevel();
        return true;
    }

    protected void preSwitchLevel() {
    }

    protected void afterSwitchLevel() {
    }

    public Position getPosition() {
        return new Position(this.x, this.y, this.z, this.level);
    }

    @Override
    public Location getLocation() {
        return new Location(this.x, this.y, this.z, this.yaw, this.pitch, this.headYaw, this.level);
    }

    public boolean isSubmerged() {
        int n = this.level.getBlockIdAt(this.chunk, this.getFloorX(), NukkitMath.floorDouble(this.y + (double)this.getEyeHeight()), this.getFloorZ());
        return Block.hasWater(n) && n != 415;
    }

    public boolean isInsideOfWater() {
        int n = this.level.getBlockIdAt(this.chunk, this.getFloorX(), this.getFloorY(), this.getFloorZ());
        return Block.hasWater(n);
    }

    public boolean isInsideOfSolid() {
        double d2 = this.y + (double)this.getEyeHeight();
        Block block = this.level.getBlock(this.chunk, NukkitMath.floorDouble(this.x), NukkitMath.floorDouble(d2), NukkitMath.floorDouble(this.z), true);
        AxisAlignedBB axisAlignedBB = block.getBoundingBox();
        return axisAlignedBB != null && block.isSolid() && !block.isTransparent() && axisAlignedBB.intersectsWith(this.boundingBox) && !(block instanceof BlockSlab);
    }

    public boolean isInsideOfFire() {
        for (Block block : this.getCollisionBlocks()) {
            if (!(block instanceof BlockFire)) continue;
            return true;
        }
        return false;
    }

    public boolean fastMove(double d2, double d3, double d4) {
        block5: {
            if (!(this instanceof Player)) {
                this.blocksAround = null;
            }
            if (d2 == 0.0 && d3 == 0.0 && d4 == 0.0) {
                return true;
            }
            if (Timings.entityMoveTimer != null) {
                Timings.entityMoveTimer.startTiming();
            }
            AxisAlignedBB axisAlignedBB = this.boundingBox.getOffsetBoundingBox(d2, d3, d4);
            if (this.server.getAllowFlight() || this instanceof Player && ((Player)this).isSpectator() || !this.level.hasCollision(this, this.getStepHeight() == 0.0 ? axisAlignedBB : axisAlignedBB.shrink(0.0, this.getStepHeight(), 0.0), false)) {
                this.boundingBox = axisAlignedBB;
            }
            this.x = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0;
            this.y = this.boundingBox.minY - (double)this.ySize;
            this.z = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0;
            this.checkChunks();
            if (!(this.noClip || this.onGround && d3 == 0.0 || this instanceof Player && ((Player)this).isSpectator())) {
                AxisAlignedBB axisAlignedBB2 = this.boundingBox.clone();
                axisAlignedBB2.minY -= 0.75;
                this.onGround = this.level.hasCollisionBlocks(axisAlignedBB2);
            }
            this.isCollided = this.onGround;
            this.updateFallState(this.onGround);
            if (Timings.entityMoveTimer == null) break block5;
            Timings.entityMoveTimer.stopTiming();
        }
        return true;
    }

    public boolean move(double d2, double d3, double d4) {
        block17: {
            AxisAlignedBB[] axisAlignedBBArray;
            if (d2 == 0.0 && d4 == 0.0 && d3 == 0.0) {
                return false;
            }
            if (!(this instanceof Player)) {
                this.blocksAround = null;
            }
            if (this.keepMovement) {
                this.boundingBox.offset(d2, d3, d4);
                this.setPosition(this.temporalVector.setComponents((this.boundingBox.minX + this.boundingBox.maxX) / 2.0, this.boundingBox.minY, (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0));
                this.onGround = this instanceof Player;
                return true;
            }
            if (Timings.entityMoveTimer != null) {
                Timings.entityMoveTimer.startTiming();
            }
            this.ySize = (float)((double)this.ySize * 0.4);
            double d5 = d2;
            double d6 = d3;
            double d7 = d4;
            AxisAlignedBB axisAlignedBB = this.boundingBox.clone();
            for (AxisAlignedBB axisAlignedBB2 : axisAlignedBBArray = this.noClip ? new AxisAlignedBB[]{} : this.level.getCollisionCubes(this, this.boundingBox.addCoord(d2, d3, d4), false)) {
                d3 = axisAlignedBB2.calculateYOffset(this.boundingBox, d3);
            }
            this.boundingBox.offset(0.0, d3, 0.0);
            boolean bl = this.onGround || d3 != d6 && d6 < 0.0;
            for (AxisAlignedBB axisAlignedBB3 : axisAlignedBBArray) {
                d2 = axisAlignedBB3.calculateXOffset(this.boundingBox, d2);
            }
            this.boundingBox.offset(d2, 0.0, 0.0);
            for (AxisAlignedBB axisAlignedBB3 : axisAlignedBBArray) {
                d4 = axisAlignedBB3.calculateZOffset(this.boundingBox, d4);
            }
            this.boundingBox.offset(0.0, 0.0, d4);
            if (this.getStepHeight() > 0.0 && bl && (d5 != d2 || d7 != d4)) {
                double d8 = d2;
                double d9 = d3;
                double d10 = d4;
                d2 = d5;
                d3 = this.getStepHeight();
                d4 = d7;
                AxisAlignedBB axisAlignedBB4 = this.boundingBox.clone();
                this.boundingBox.setBB(axisAlignedBB);
                for (AxisAlignedBB axisAlignedBB5 : axisAlignedBBArray = this.level.getCollisionCubes(this, this.boundingBox.addCoord(d2, d3, d4), false)) {
                    d3 = axisAlignedBB5.calculateYOffset(this.boundingBox, d3);
                }
                this.boundingBox.offset(0.0, d3, 0.0);
                for (AxisAlignedBB axisAlignedBB5 : axisAlignedBBArray) {
                    d2 = axisAlignedBB5.calculateXOffset(this.boundingBox, d2);
                }
                this.boundingBox.offset(d2, 0.0, 0.0);
                for (AxisAlignedBB axisAlignedBB5 : axisAlignedBBArray) {
                    d4 = axisAlignedBB5.calculateZOffset(this.boundingBox, d4);
                }
                this.boundingBox.offset(0.0, 0.0, d4);
                double d11 = -d3;
                for (AxisAlignedBB axisAlignedBB6 : axisAlignedBBArray) {
                    d11 = axisAlignedBB6.calculateYOffset(this.boundingBox, d11);
                }
                d3 += d11;
                this.boundingBox.offset(0.0, d11, 0.0);
                if (d8 * d8 + d10 * d10 >= d2 * d2 + d4 * d4) {
                    d2 = d8;
                    d3 = d9;
                    d4 = d10;
                    this.boundingBox.setBB(axisAlignedBB4);
                } else {
                    this.ySize = (float)((double)this.ySize + d3);
                }
            }
            this.x = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0;
            this.y = this.boundingBox.minY - (double)this.ySize;
            this.z = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0;
            this.checkChunks();
            this.checkGroundState(d5, d6, d7, d2, d3, d4);
            this.updateFallState(this.onGround);
            if (d5 != d2) {
                this.motionX = 0.0;
            }
            if (d6 != d3) {
                this.motionY = 0.0;
            }
            if (d7 != d4) {
                this.motionZ = 0.0;
            }
            if (Timings.entityMoveTimer == null) break block17;
            Timings.entityMoveTimer.stopTiming();
        }
        return true;
    }

    protected void checkGroundState(double d2, double d3, double d4, double d5, double d6, double d7) {
        if (this.noClip) {
            this.isCollidedVertically = false;
            this.isCollidedHorizontally = false;
            this.isCollided = false;
            this.onGround = false;
        } else {
            this.isCollidedVertically = d3 != d6;
            this.isCollidedHorizontally = d2 != d5 || d4 != d7;
            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
            this.onGround = d3 != d6 && d3 < 0.0;
        }
    }

    public List<Block> getBlocksAround() {
        if (this.blocksAround == null) {
            int n = NukkitMath.floorDouble(this.boundingBox.minX);
            int n2 = NukkitMath.floorDouble(this.boundingBox.minY);
            int n3 = NukkitMath.floorDouble(this.boundingBox.minZ);
            int n4 = NukkitMath.ceilDouble(this.boundingBox.maxX);
            int n5 = NukkitMath.ceilDouble(this.boundingBox.maxY);
            int n6 = NukkitMath.ceilDouble(this.boundingBox.maxZ);
            this.blocksAround = new ArrayList<Block>();
            for (int k = n3; k <= n6; ++k) {
                for (int i2 = n; i2 <= n4; ++i2) {
                    for (int i3 = n2; i3 <= n5; ++i3) {
                        Block block = this.level.getBlock(this.chunk, i2, i3, k, false);
                        this.blocksAround.add(block);
                    }
                }
            }
        }
        return this.blocksAround;
    }

    public List<Block> getCollisionBlocks() {
        if (this.collisionBlocks == null) {
            this.collisionBlocks = new ArrayList<Block>();
            List<Block> list = this.getBlocksAround();
            for (Block block : list) {
                if (!block.collidesWithBB(this.boundingBox, true)) continue;
                this.collisionBlocks.add(block);
            }
        }
        return this.collisionBlocks;
    }

    public boolean canBeMovedByCurrents() {
        return true;
    }

    protected void checkBlockCollision() {
        if (this.noClip) {
            return;
        }
        Vector3 vector3 = new Vector3(0.0, 0.0, 0.0);
        boolean bl = false;
        for (Block block : this.getCollisionBlocks()) {
            if (block.getId() == 90) {
                bl = true;
                continue;
            }
            block.onEntityCollide(this);
            block.addVelocityToEntity(this, vector3);
        }
        this.inPortalTicks = bl ? ++this.inPortalTicks : 0;
        if (vector3.lengthSquared() > 0.0) {
            vector3 = vector3.normalize();
            double d2 = 0.014;
            this.motionX += vector3.x * d2;
            this.motionY += vector3.y * d2;
            this.motionZ += vector3.z * d2;
        }
    }

    public boolean setPositionAndRotation(Vector3 vector3, double d2, double d3) {
        return this.setPositionAndRotation(vector3, d2, d3, d2);
    }

    public boolean setPositionAndRotation(Vector3 vector3, double d2, double d3, double d4) {
        if (this.setPosition(vector3)) {
            this.setRotation(d2, d3, d4);
            return true;
        }
        return false;
    }

    public void setRotation(double d2, double d3) {
        this.setRotation(d2, d3, d2);
    }

    public void setRotation(double d2, double d3, double d4) {
        this.yaw = d2;
        this.pitch = d3;
        this.headYaw = d4;
        this.scheduleUpdate();
    }

    public boolean doesTriggerPressurePlate() {
        return true;
    }

    public boolean canPassThrough() {
        return true;
    }

    protected void checkChunks() {
        int n = this.getChunkX();
        int n2 = this.getChunkZ();
        if (this.chunk == null || this.chunk.getX() != n || this.chunk.getZ() != n2) {
            if (this.chunk != null) {
                this.chunk.removeEntity(this);
            }
            this.chunk = this.level.getChunk(n, n2, true);
            if (!this.justCreated) {
                Map<Integer, Player> map = this.level.getChunkPlayers(n, n2);
                for (Player player : new ArrayList<Player>(this.hasSpawned.values())) {
                    if (!map.containsKey(player.getLoaderId())) {
                        this.despawnFrom(player);
                        continue;
                    }
                    map.remove(player.getLoaderId());
                }
                for (Player player : map.values()) {
                    this.spawnTo(player);
                }
            }
            if (this.chunk == null) {
                return;
            }
            if (this.closed) {
                return;
            }
            this.chunk.addEntity(this);
        }
    }

    public boolean setPosition(Vector3 vector3) {
        if (this.closed) {
            return false;
        }
        if (vector3 instanceof Position) {
            Level level = this.level;
            Level level2 = ((Position)vector3).level;
            if (level2 != null && level2 != level) {
                if (!this.switchLevel(level2)) {
                    return false;
                }
                this.x = vector3.x;
                this.y = vector3.y;
                this.z = vector3.z;
                if (this instanceof Player && this.server.dimensionsEnabled && level2.getDimension() != level.getDimension()) {
                    ((Player)this).setDimension(level2.getDimension());
                }
            } else {
                this.x = vector3.x;
                this.y = vector3.y;
                this.z = vector3.z;
            }
        } else {
            this.x = vector3.x;
            this.y = vector3.y;
            this.z = vector3.z;
        }
        this.recalculateBoundingBox();
        if (!(this instanceof Player)) {
            this.blocksAround = null;
        }
        this.checkChunks();
        return true;
    }

    public Vector3 getMotion() {
        return new Vector3(this.motionX, this.motionY, this.motionZ);
    }

    public boolean setMotion(Vector3 vector3) {
        block2: {
            if (this.server.callEntityMotionEv && !this.justCreated) {
                EntityMotionEvent entityMotionEvent = new EntityMotionEvent(this, vector3);
                this.server.getPluginManager().callEvent(entityMotionEvent);
                if (entityMotionEvent.isCancelled()) {
                    return false;
                }
            }
            this.motionX = vector3.x;
            this.motionY = vector3.y;
            this.motionZ = vector3.z;
            if (this.justCreated) break block2;
            this.updateMovement();
        }
        return true;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void kill() {
        this.health = 0.0f;
        this.scheduleUpdate();
        if (!this.passengers.isEmpty()) {
            for (Entity entity : new ArrayList<Entity>(this.passengers)) {
                this.dismountEntity(entity);
                entity.riding = null;
            }
        }
    }

    public boolean teleport(Vector3 vector3) {
        return this.teleport(vector3, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public boolean teleport(Vector3 vector3, PlayerTeleportEvent.TeleportCause teleportCause) {
        return this.teleport(Location.fromObject(vector3, this.level, this.yaw, this.pitch, this.headYaw), teleportCause);
    }

    public boolean teleport(Position position) {
        return this.teleport(position, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public boolean teleport(Position position, PlayerTeleportEvent.TeleportCause teleportCause) {
        return this.teleport(Location.fromObject(position, position.level, this.yaw, this.pitch, this.headYaw), teleportCause);
    }

    public boolean teleport(Location location) {
        return this.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        double d2 = location.yaw;
        double d3 = location.pitch;
        Location location2 = this.getLocation();
        Location location3 = location;
        if (teleportCause != null) {
            EntityTeleportEvent entityTeleportEvent = new EntityTeleportEvent(this, location2, location3);
            this.server.getPluginManager().callEvent(entityTeleportEvent);
            if (entityTeleportEvent.isCancelled()) {
                return false;
            }
            location3 = entityTeleportEvent.getTo();
        }
        if (this.riding != null) {
            this.riding.dismountEntity(this);
        }
        this.ySize = 0.0f;
        if (teleportCause != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            this.setMotion(this.temporalVector.setComponents(0.0, 0.0, 0.0));
        }
        if (this.setPositionAndRotation(location3, d2, d3)) {
            this.resetFallDistance();
            this.onGround = !this.noClip;
            this.updateMovement();
            return true;
        }
        return false;
    }

    public long getId() {
        return this.id;
    }

    public void respawnToAll() {
        ArrayList<Player> arrayList = new ArrayList<Player>(this.hasSpawned.values());
        this.hasSpawned.clear();
        for (Player player : arrayList) {
            this.spawnTo(player);
        }
    }

    public void spawnToAll() {
        if (this.chunk == null || this.closed) {
            return;
        }
        for (Player player : this.level.getChunkPlayers(this.chunk.getX(), this.chunk.getZ()).values()) {
            if (!player.isOnline()) continue;
            this.spawnTo(player);
        }
    }

    public void despawnFromAll() {
        for (Player player : new ArrayList<Player>(this.hasSpawned.values())) {
            this.despawnFrom(player);
        }
    }

    public void close() {
        block2: {
            if (this.closed) break block2;
            this.closed = true;
            this.server.getPluginManager().callEvent(new EntityDespawnEvent(this));
            this.despawnFromAll();
            if (this.chunk != null) {
                this.chunk.removeEntity(this);
            }
            if (this.level != null) {
                this.level.removeEntity(this);
            }
        }
    }

    public boolean setDataProperty(EntityData entityData) {
        return this.setDataProperty(entityData, true);
    }

    public boolean setDataProperty(EntityData entityData, boolean bl) {
        if (!Objects.equals(entityData, this.dataProperties.get(entityData.getId()))) {
            this.dataProperties.put(entityData);
            if (bl) {
                EntityMetadata entityMetadata = new EntityMetadata();
                entityMetadata.put(this.dataProperties.get(entityData.getId()));
                if (entityData.getId() == 92) {
                    entityMetadata.put(this.dataProperties.get(0));
                }
                this.sendData(this.hasSpawned.values().toArray(new Player[0]), entityMetadata);
            }
            return true;
        }
        return false;
    }

    public boolean setDataPropertyAndSendOnlyToSelf(EntityData entityData) {
        if (entityData == null) {
            throw new IllegalArgumentException("setDataPropertyAndSendOnlyToSelf: EntityData must not be null");
        }
        if (!Objects.equals(entityData, this.dataProperties.get(entityData.getId()))) {
            this.dataProperties.put(entityData);
            if (this instanceof Player) {
                EntityMetadata entityMetadata = new EntityMetadata();
                entityMetadata.put(this.dataProperties.get(entityData.getId()));
                SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
                setEntityDataPacket.eid = this.id;
                setEntityDataPacket.metadata = entityMetadata;
                ((Player)this).dataPacket(setEntityDataPacket);
            }
            return true;
        }
        return false;
    }

    public EntityMetadata getDataProperties() {
        return this.dataProperties;
    }

    public EntityData getDataProperty(int n) {
        return this.dataProperties.get(n);
    }

    public int getDataPropertyInt(int n) {
        return this.dataProperties.getInt(n);
    }

    public int getDataPropertyShort(int n) {
        return this.dataProperties.getShort(n);
    }

    public int getDataPropertyByte(int n) {
        return this.dataProperties.getByte(n);
    }

    public boolean getDataPropertyBoolean(int n) {
        return this.dataProperties.getBoolean(n);
    }

    public long getDataPropertyLong(int n) {
        return this.dataProperties.getLong(n);
    }

    public String getDataPropertyString(int n) {
        return this.dataProperties.getString(n);
    }

    public float getDataPropertyFloat(int n) {
        return this.dataProperties.getFloat(n);
    }

    public CompoundTag getDataPropertyNBT(int n) {
        return this.dataProperties.getNBT(n);
    }

    public Vector3 getDataPropertyPos(int n) {
        return this.dataProperties.getPosition(n);
    }

    public Vector3f getDataPropertyVector3f(int n) {
        return this.dataProperties.getFloatPosition(n);
    }

    public int getDataPropertyType(int n) {
        return this.dataProperties.exists(n) ? this.getDataProperty(n).getType() : -1;
    }

    public void setDataFlag(int n, int n2) {
        this.setDataFlag(n, n2, true);
    }

    public void setDataFlag(int n, int n2, boolean bl) {
        this.setDataFlag(n, n2, bl, true);
    }

    public void setDataFlag(int n, int n2, boolean bl, boolean bl2) {
        if (this.getDataFlag(n, n2) != bl) {
            if (n == 26) {
                byte by = (byte)this.getDataPropertyByte(n);
                by = (byte)(by ^ 1 << n2);
                this.setDataProperty(new ByteEntityData(n, by), bl2);
            } else {
                LongEntityData longEntityData = (LongEntityData)this.dataProperties.getOrDefault(n, new LongEntityData(n, 0L));
                long l = longEntityData.getData() ^ 1L << n2;
                LongEntityData longEntityData2 = new LongEntityData(n, l);
                if (n == 0) {
                    long l2;
                    long l3;
                    long l4;
                    int n3;
                    int n4 = n2 > 46 ? n2 - 1 : n2;
                    int n5 = n4 > 30 ? n4 - 1 : n4;
                    int n6 = n3 = n5 >= 23 && n5 < 43 || n5 >= 46 ? n5 - 1 : n5;
                    if (longEntityData.dataVersions != null && longEntityData.dataVersions.length == 3) {
                        l4 = longEntityData.dataVersions[0];
                        l3 = longEntityData.dataVersions[1];
                        l2 = longEntityData.dataVersions[2];
                    } else {
                        l4 = 0L;
                        l3 = 0L;
                        l2 = 0L;
                    }
                    longEntityData2.dataVersions = new long[]{l4 ^ 1L << n4, l3 ^ 1L << n5, l2 ^ 1L << n3};
                } else if (n == 92) {
                    int n7 = n2 > 46 ? n2 - 1 : n2;
                    long l5 = longEntityData.dataVersions != null && longEntityData.dataVersions.length == 1 ? longEntityData.dataVersions[0] : 0L;
                    longEntityData2.dataVersions = new long[]{l5 ^ 1L << n7};
                }
                this.setDataProperty(longEntityData2, bl2);
            }
        }
    }

    public void setDataFlagSelfOnly(int n, int n2, boolean bl) {
        if (this.getDataFlag(n, n2) != bl) {
            if (n == 26) {
                byte by = (byte)this.getDataPropertyByte(n);
                by = (byte)(by ^ 1 << n2);
                this.setDataPropertyAndSendOnlyToSelf(new ByteEntityData(n, by));
            } else {
                LongEntityData longEntityData = (LongEntityData)this.dataProperties.getOrDefault(n, new LongEntityData(n, 0L));
                long l = longEntityData.getData() ^ 1L << n2;
                LongEntityData longEntityData2 = new LongEntityData(n, l);
                if (n == 0) {
                    long l2;
                    long l3;
                    long l4;
                    int n3;
                    int n4 = n2 > 46 ? n2 - 1 : n2;
                    int n5 = n4 > 30 ? n4 - 1 : n4;
                    int n6 = n3 = n5 >= 23 && n5 < 43 || n5 >= 46 ? n5 - 1 : n5;
                    if (longEntityData.dataVersions != null && longEntityData.dataVersions.length == 3) {
                        l4 = longEntityData.dataVersions[0];
                        l3 = longEntityData.dataVersions[1];
                        l2 = longEntityData.dataVersions[2];
                    } else {
                        l4 = 0L;
                        l3 = 0L;
                        l2 = 0L;
                    }
                    longEntityData2.dataVersions = new long[]{l4 ^ 1L << n4, l3 ^ 1L << n5, l2 ^ 1L << n3};
                } else if (n == 92) {
                    int n7 = n2 > 46 ? n2 - 1 : n2;
                    long l5 = longEntityData.dataVersions != null && longEntityData.dataVersions.length == 1 ? longEntityData.dataVersions[0] : 0L;
                    longEntityData2.dataVersions = new long[]{l5 ^ 1L << n7};
                }
                this.setDataPropertyAndSendOnlyToSelf(longEntityData2);
            }
        }
    }

    public boolean getDataFlag(int n, int n2) {
        return ((n == 26 ? (long)(this.getDataPropertyByte(n) & 0xFF) : this.getDataPropertyLong(n)) & 1L << n2) > 0L;
    }

    public void setGenericFlag(int n, boolean bl) {
        this.setDataFlag(n >= 64 ? 92 : 0, n % 64, bl);
    }

    public boolean getGenericFlag(int n) {
        return this.getDataFlag(n >= 64 ? 92 : 0, n % 64);
    }

    @Override
    public void setMetadata(String string, MetadataValue metadataValue) {
        this.server.getEntityMetadata().setMetadata(this, string, metadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String string) {
        return this.server.getEntityMetadata().getMetadata(this, string);
    }

    @Override
    public boolean hasMetadata(String string) {
        return this.server.getEntityMetadata().hasMetadata(this, string);
    }

    @Override
    public void removeMetadata(String string, Plugin plugin) {
        this.server.getEntityMetadata().removeMetadata(this, string, plugin);
    }

    public Server getServer() {
        return this.server;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        Entity entity = (Entity)object;
        return this.id == entity.id;
    }

    @Override
    public int hashCode() {
        return (int)(203L + this.id);
    }

    public static Entity create(Object object, Position position, Object ... objectArray) {
        BaseFullChunk baseFullChunk = position.getLevel().getChunk((int)position.x >> 4, (int)position.z >> 4, true);
        if (!baseFullChunk.isGenerated()) {
            baseFullChunk.setGenerated();
        }
        if (!baseFullChunk.isPopulated()) {
            baseFullChunk.setPopulated();
        }
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", position.x)).add(new DoubleTag("", position.y)).add(new DoubleTag("", position.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", position instanceof Location ? (float)((Location)position).yaw : 0.0f)).add(new FloatTag("", position instanceof Location ? (float)((Location)position).pitch : 0.0f)));
        return Entity.createEntity(object.toString(), (FullChunk)baseFullChunk, compoundTag, objectArray);
    }

    public boolean isOnLadder() {
        int n = this.level.getBlockIdAt(this.chunk, this.getFloorX(), this.getFloorY(), this.getFloorZ());
        return n == 65 || n == 106 || n == 30 || n == 420;
    }

    public float getMountedYOffset() {
        return this.getHeight() * 0.75f;
    }

    public boolean goToNewChunk(FullChunk fullChunk) {
        return true;
    }

    public boolean canSeeSky() {
        int n = this.getFloorX();
        int n2 = this.getFloorY();
        int n3 = this.getFloorZ();
        for (int k = 255; k >= n2; --k) {
            if (this.level.getBlockIdAt(this.chunk, n, k, n3) == 0) continue;
            return false;
        }
        return true;
    }

    private static Exception c(Exception exception) {
        return exception;
    }
}


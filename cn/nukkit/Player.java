/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.Achievement;
import cn.nukkit.AdventureSettings;
import cn.nukkit.IPlayer;
import cn.nukkit.NOBF;
import cn.nukkit.Nukkit;
import cn.nukkit.PlayerFood;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDoor;
import cn.nukkit.block.BlockDragonEgg;
import cn.nukkit.block.BlockEnderChest;
import cn.nukkit.block.BlockFallable;
import cn.nukkit.block.BlockLectern;
import cn.nukkit.block.BlockMeta;
import cn.nukkit.block.BlockNetherPortal;
import cn.nukkit.block.BlockNoteblock;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityCampfire;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.blockentity.BlockEntityLectern;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.c;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandDataVersions;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityControllable;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.EntityInteractable;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.EntityRideable;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.data.IntPositionEntityData;
import cn.nukkit.entity.data.ShortEntityData;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.entity.data.StringEntityData;
import cn.nukkit.entity.item.EntityBoat;
import cn.nukkit.entity.item.EntityChestBoat;
import cn.nukkit.entity.item.EntityFishingHook;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.item.EntityXPOrb;
import cn.nukkit.entity.mob.EntityEnderman;
import cn.nukkit.entity.mob.EntityWalkingMob;
import cn.nukkit.entity.passive.EntityWolf;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.entity.projectile.EntityThrownTrident;
import cn.nukkit.event.Event;
import cn.nukkit.event.block.WaterFrostEvent;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityPortalEnterEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.event.inventory.InventoryCloseEvent;
import cn.nukkit.event.inventory.InventoryPickupArrowEvent;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.inventory.InventoryPickupTridentEvent;
import cn.nukkit.event.player.PlayerAchievementAwardedEvent;
import cn.nukkit.event.player.PlayerAnimationEvent;
import cn.nukkit.event.player.PlayerBedEnterEvent;
import cn.nukkit.event.player.PlayerBedLeaveEvent;
import cn.nukkit.event.player.PlayerBlockPickEvent;
import cn.nukkit.event.player.PlayerChangeSkinEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerChunkRequestEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerEditBookEvent;
import cn.nukkit.event.player.PlayerExperienceChangeEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerGameModeChangeEvent;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInvalidMoveEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerJumpEvent;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerMapInfoRequestEvent;
import cn.nukkit.event.player.PlayerMouseOverEntityEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.event.player.PlayerServerSettingsRequestEvent;
import cn.nukkit.event.player.PlayerSettingsRespondedEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.event.player.PlayerToggleFlightEvent;
import cn.nukkit.event.player.PlayerToggleGlideEvent;
import cn.nukkit.event.player.PlayerToggleSneakEvent;
import cn.nukkit.event.player.PlayerToggleSpinAttackEvent;
import cn.nukkit.event.player.PlayerToggleSprintEvent;
import cn.nukkit.event.player.PlayerToggleSwimEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.form.handler.FormResponseHandler;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.inventory.AnvilInventory;
import cn.nukkit.inventory.BigCraftingGrid;
import cn.nukkit.inventory.CraftingGrid;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.PlayerCursorInventory;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.PlayerUIInventory;
import cn.nukkit.inventory.transaction.CraftingTransaction;
import cn.nukkit.inventory.transaction.EnchantTransaction;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.inventory.transaction.LoomTransaction;
import cn.nukkit.inventory.transaction.RepairItemTransaction;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.data.ReleaseItemData;
import cn.nukkit.inventory.transaction.data.UseItemData;
import cn.nukkit.inventory.transaction.data.UseItemOnEntityData;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.item.ItemArrow;
import cn.nukkit.item.ItemBookAndQuill;
import cn.nukkit.item.ItemBookWritten;
import cn.nukkit.item.ItemCrossbow;
import cn.nukkit.item.ItemMap;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.food.Food;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.ChunkLoader;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.level.particle.PunchBlockParticle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.ByteTag;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.CompressionProvider;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.network.protocol.AdventureSettingsPacket;
import cn.nukkit.network.protocol.AnimatePacket;
import cn.nukkit.network.protocol.AvailableCommandsPacket;
import cn.nukkit.network.protocol.AvailableEntityIdentifiersPacket;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.BiomeDefinitionListPacket;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.BlockPickRequestPacket;
import cn.nukkit.network.protocol.BookEditPacket;
import cn.nukkit.network.protocol.ChangeDimensionPacket;
import cn.nukkit.network.protocol.ChunkRadiusUpdatedPacket;
import cn.nukkit.network.protocol.CommandRequestPacket;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.DeathInfoPacket;
import cn.nukkit.network.protocol.DisconnectPacket;
import cn.nukkit.network.protocol.EmotePacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.FilterTextPacket;
import cn.nukkit.network.protocol.GameRulesChangedPacket;
import cn.nukkit.network.protocol.InteractPacket;
import cn.nukkit.network.protocol.InventoryContentPacket;
import cn.nukkit.network.protocol.InventorySlotPacket;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import cn.nukkit.network.protocol.ItemFrameDropItemPacket;
import cn.nukkit.network.protocol.LecternUpdatePacket;
import cn.nukkit.network.protocol.LevelChunkPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.network.protocol.MapInfoRequestPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.network.protocol.ModalFormRequestPacket;
import cn.nukkit.network.protocol.ModalFormResponsePacket;
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.NetworkChunkPublisherUpdatePacket;
import cn.nukkit.network.protocol.NetworkSettingsPacket;
import cn.nukkit.network.protocol.PacketViolationWarningPacket;
import cn.nukkit.network.protocol.PlayStatusPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import cn.nukkit.network.protocol.PlayerHotbarPacket;
import cn.nukkit.network.protocol.PlayerInputPacket;
import cn.nukkit.network.protocol.PlayerSkinPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.RequestAbilityPacket;
import cn.nukkit.network.protocol.RequestChunkRadiusPacket;
import cn.nukkit.network.protocol.RequestNetworkSettingsPacket;
import cn.nukkit.network.protocol.ResourcePackChunkDataPacket;
import cn.nukkit.network.protocol.ResourcePackChunkRequestPacket;
import cn.nukkit.network.protocol.ResourcePackClientResponsePacket;
import cn.nukkit.network.protocol.ResourcePackDataInfoPacket;
import cn.nukkit.network.protocol.ResourcePackStackPacket;
import cn.nukkit.network.protocol.ResourcePacksInfoPacket;
import cn.nukkit.network.protocol.RespawnPacket;
import cn.nukkit.network.protocol.ServerSettingsResponsePacket;
import cn.nukkit.network.protocol.SetCommandsEnabledPacket;
import cn.nukkit.network.protocol.SetDifficultyPacket;
import cn.nukkit.network.protocol.SetEntityMotionPacket;
import cn.nukkit.network.protocol.SetPlayerGameTypePacket;
import cn.nukkit.network.protocol.SetSpawnPositionPacket;
import cn.nukkit.network.protocol.SetTitlePacket;
import cn.nukkit.network.protocol.ShowProfilePacket;
import cn.nukkit.network.protocol.StartGamePacket;
import cn.nukkit.network.protocol.TakeItemEntityPacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.network.protocol.ToastRequestPacket;
import cn.nukkit.network.protocol.TransferPacket;
import cn.nukkit.network.protocol.UpdateAttributesPacket;
import cn.nukkit.network.protocol.types.AuthInputAction;
import cn.nukkit.network.protocol.types.PacketCompressionAlgorithm;
import cn.nukkit.network.protocol.types.PlayerAbility;
import cn.nukkit.network.protocol.types.PlayerActionType;
import cn.nukkit.network.protocol.types.PlayerBlockActionData;
import cn.nukkit.network.session.NetworkPlayerSession;
import cn.nukkit.permission.PermissibleBase;
import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.potion.Potion;
import cn.nukkit.resourcepacks.ResourcePack;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.BlockIterator;
import cn.nukkit.utils.ClientChainData;
import cn.nukkit.utils.DummyBossBar;
import cn.nukkit.utils.LoginChainData;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;
import cn.nukkit.utils.Zlib;
import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.netty.util.internal.PlatformDependent;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player
extends EntityHuman
implements CommandSender,
InventoryHolder,
ChunkLoader,
IPlayer {
    private static final Logger aj;
    public static final int SURVIVAL = 0;
    public static final int CREATIVE = 1;
    public static final int ADVENTURE = 2;
    public static final int SPECTATOR = 3;
    public static final int VIEW = 3;
    public static final int CRAFTING_SMALL = 0;
    public static final int CRAFTING_BIG = 1;
    public static final int CRAFTING_ANVIL = 2;
    public static final int CRAFTING_ENCHANT = 3;
    public static final int CRAFTING_BEACON = 4;
    public static final int CRAFTING_LOOM = 1004;
    public static final float DEFAULT_SPEED = 0.1f;
    public static final float MAXIMUM_SPEED = 0.5f;
    public static final float DEFAULT_FLY_SPEED = 0.05f;
    public static final int PERMISSION_CUSTOM = 3;
    public static final int PERMISSION_OPERATOR = 2;
    public static final int PERMISSION_MEMBER = 1;
    public static final int PERMISSION_VISITOR = 0;
    public static final int ANVIL_WINDOW_ID = 2;
    public static final int ENCHANT_WINDOW_ID = 3;
    public static final int BEACON_WINDOW_ID = 4;
    public static final int LOOM_WINDOW_ID = 2;
    protected static final int RESOURCE_PACK_CHUNK_SIZE = 8192;
    protected final SourceInterface interfaz;
    protected final NetworkPlayerSession networkSession;
    public boolean playedBefore;
    public boolean spawned;
    public boolean loggedIn;
    private boolean I;
    private int p;
    public int gamemode;
    protected long randomClientId;
    protected final BiMap<Inventory, Integer> windows = HashBiMap.create();
    protected final BiMap<Integer, Inventory> windowIndex = this.windows.inverse();
    protected final Set<Integer> permanentWindows = new IntOpenHashSet();
    private boolean G;
    protected int windowCnt = 4;
    protected int closingWindowId = Integer.MIN_VALUE;
    public final HashSet<String> achievements = new HashSet();
    public int craftingType = 0;
    protected PlayerUIInventory playerUIInventory;
    protected CraftingGrid craftingGrid;
    protected CraftingTransaction craftingTransaction;
    protected EnchantTransaction enchantTransaction;
    protected RepairItemTransaction repairItemTransaction;
    protected LoomTransaction loomTransaction;
    public Vector3 speed;
    protected Vector3 forceMovement;
    protected Vector3 teleportPosition;
    protected Vector3 newPosition;
    protected Vector3 sleeping;
    private Vector3 Y;
    private final Queue<Vector3> u = PlatformDependent.newMpscQueue(4);
    protected boolean connected = true;
    protected final InetSocketAddress socketAddress;
    protected boolean removeFormat = true;
    protected String username;
    protected String iusername;
    protected String displayName;
    public int protocol = Integer.MAX_VALUE;
    public int raknetProtocol = Integer.MAX_VALUE;
    private boolean T;
    private final int P;
    private int U = 0;
    protected int nextChunkOrderRun = 1;
    protected int chunkRadius;
    protected int viewDistance;
    public final Map<Long, Boolean> usedChunks = new Long2ObjectOpenHashMap<Boolean>();
    protected final Long2ObjectLinkedOpenHashMap<Boolean> loadQueue = new Long2ObjectLinkedOpenHashMap();
    protected final Map<UUID, Player> hiddenPlayers = new HashMap<UUID, Player>();
    protected Position spawnPosition;
    protected int inAirTicks = 0;
    protected int startAirTicks = 10;
    protected AdventureSettings adventureSettings;
    @NOBF
    private PermissibleBase perm;
    private boolean ai = true;
    private boolean M = true;
    private boolean n = true;
    private int aa = 0;
    private int S = 0;
    protected PlayerFood foodData;
    private Entity ae;
    private final AtomicReference<Locale> O = new AtomicReference<Object>(null);
    private int q;
    private String o = "";
    protected boolean enableClientCommand = true;
    private BlockEnderChest E;
    private LoginChainData C;
    public int pickedXPOrb = 0;
    private boolean v = true;
    protected int formWindowCount = 0;
    public Map<Integer, FormWindow> formWindows = new Int2ObjectOpenHashMap<FormWindow>();
    protected Map<Integer, FormWindow> serverSettings = new Int2ObjectOpenHashMap<FormWindow>();
    protected Map<Long, DummyBossBar> dummyBossBars = new Long2ObjectLinkedOpenHashMap<DummyBossBar>();
    private AsyncTask ag;
    protected boolean shouldLogin;
    private static Stream<Field> ah;
    protected int startAction = -1;
    private int X;
    protected int lastEnderPearl = 20;
    protected int lastChorusFruitTeleport = 20;
    protected int lastFireworkBoost = 20;
    public long lastSkinChange = -1L;
    private double D = 0.0;
    public long lastBreak = -1L;
    private BlockVector3 H = new BlockVector3();
    public Block breakingBlock;
    private PlayerBlockActionData V;
    public EntityFishingHook fishing;
    private boolean Q;
    public boolean locallyInitialized;
    private boolean w = true;
    protected boolean checkMovement = true;
    private int L;
    private boolean ac;
    private boolean K;
    private boolean s;
    private int t;
    @NOBF
    private int riptideTicks;
    private boolean R;
    private boolean r;
    private boolean Z;
    private boolean ad;
    private boolean A;
    private int af;
    private int J;
    private final int[] F = new int[256];
    private static final ByteArrayList N;
    private static final IntSet W;
    private final Long2IntOpenHashMap ab = new Long2IntOpenHashMap();
    private static final String B;

    public int getStartActionTick() {
        return this.startAction;
    }

    public void startAction() {
        this.startAction = this.server.getTick();
    }

    public void stopAction() {
        this.startAction = -1;
    }

    public int getLastEnderPearlThrowingTick() {
        return this.lastEnderPearl;
    }

    public void onThrowEnderPearl() {
        this.lastEnderPearl = this.server.getTick();
    }

    public int getLastChorusFruitTeleport() {
        return this.lastChorusFruitTeleport;
    }

    public void onChorusFruitTeleport() {
        this.lastChorusFruitTeleport = this.server.getTick();
    }

    public int getLastFireworkBoostTick() {
        return this.lastFireworkBoost;
    }

    public void onFireworkBoost() {
        this.lastFireworkBoost = this.server.getTick();
    }

    public BlockEnderChest getViewingEnderChest() {
        return this.E;
    }

    public void setViewingEnderChest(BlockEnderChest blockEnderChest) {
        if (blockEnderChest == null && this.E != null) {
            this.E.getViewers().remove(this);
        } else if (blockEnderChest != null) {
            blockEnderChest.getViewers().add(this);
        }
        this.E = blockEnderChest;
    }

    public TranslationContainer getLeaveMessage() {
        return new TranslationContainer((Object)((Object)TextFormat.YELLOW) + "%multiplayer.player.left", this.displayName);
    }

    public Long getClientId() {
        return this.randomClientId;
    }

    @Override
    public boolean isBanned() {
        return this.server.getNameBans().isBanned(this.username);
    }

    @Override
    public void setBanned(boolean bl) {
        if (bl) {
            this.server.getNameBans().addBan(this.username, null, null, null);
            this.kick(PlayerKickEvent.Reason.NAME_BANNED, "You are banned!");
        } else {
            this.server.getNameBans().remove(this.username);
        }
    }

    @Override
    public boolean isWhitelisted() {
        return this.server.isWhitelisted(this.username.toLowerCase());
    }

    @Override
    public void setWhitelisted(boolean bl) {
        if (bl) {
            this.server.addWhitelist(this.username.toLowerCase());
        } else {
            this.server.removeWhitelist(this.username.toLowerCase());
        }
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    @Override
    public Long getFirstPlayed() {
        return this.namedTag != null ? Long.valueOf(this.namedTag.getLong("firstPlayed")) : null;
    }

    @Override
    public Long getLastPlayed() {
        return this.namedTag != null ? Long.valueOf(this.namedTag.getLong("lastPlayed")) : null;
    }

    @Override
    public boolean hasPlayedBefore() {
        return this.playedBefore;
    }

    public AdventureSettings getAdventureSettings() {
        return this.adventureSettings;
    }

    public void setAdventureSettings(AdventureSettings adventureSettings) {
        this.adventureSettings = adventureSettings.clone(this);
        this.adventureSettings.update();
    }

    public void resetInAirTicks() {
        if (this.inAirTicks != 0) {
            this.startAirTicks = 10;
        }
        this.inAirTicks = 0;
    }

    public void setAllowFlight(boolean bl) {
        this.adventureSettings.set(AdventureSettings.Type.ALLOW_FLIGHT, bl);
        this.adventureSettings.update();
    }

    public boolean getAllowFlight() {
        return this.adventureSettings.get(AdventureSettings.Type.ALLOW_FLIGHT);
    }

    public void setAllowModifyWorld(boolean bl) {
        this.adventureSettings.set(AdventureSettings.Type.WORLD_IMMUTABLE, !bl);
        this.adventureSettings.set(AdventureSettings.Type.MINE, bl);
        this.adventureSettings.set(AdventureSettings.Type.BUILD, bl);
        this.adventureSettings.update();
    }

    public void setAllowInteract(boolean bl) {
        this.setAllowInteract(bl, bl);
    }

    public void setAllowInteract(boolean bl, boolean bl2) {
        this.adventureSettings.set(AdventureSettings.Type.WORLD_IMMUTABLE, !bl);
        this.adventureSettings.set(AdventureSettings.Type.DOORS_AND_SWITCHED, bl);
        this.adventureSettings.set(AdventureSettings.Type.OPEN_CONTAINERS, bl2);
        this.adventureSettings.update();
    }

    public void setAutoJump(boolean bl) {
        this.adventureSettings.set(AdventureSettings.Type.AUTO_JUMP, bl);
        this.adventureSettings.update();
    }

    public boolean hasAutoJump() {
        return this.adventureSettings.get(AdventureSettings.Type.AUTO_JUMP);
    }

    @Override
    public void spawnTo(Player player) {
        block0: {
            if (!this.spawned || !player.spawned || !this.isAlive() || !player.isAlive() || player.getLevel() != this.level || !player.canSee(this) || this.isSpectator() || !this.M) break block0;
            super.spawnTo(player);
        }
    }

    public boolean getRemoveFormat() {
        return this.removeFormat;
    }

    public void setRemoveFormat() {
        this.setRemoveFormat(true);
    }

    public void setRemoveFormat(boolean bl) {
        this.removeFormat = bl;
    }

    public boolean canSee(Player player) {
        return !this.hiddenPlayers.containsKey(player.getUniqueId());
    }

    public void hidePlayer(Player player) {
        if (this == player) {
            return;
        }
        this.hiddenPlayers.put(player.getUniqueId(), player);
        player.despawnFrom(this);
    }

    public void showPlayer(Player player) {
        block1: {
            if (this == player) {
                return;
            }
            this.hiddenPlayers.remove(player.getUniqueId());
            if (!player.isOnline()) break block1;
            player.spawnTo(this);
        }
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    public boolean canPickupXP() {
        return this.v;
    }

    public void setCanPickupXP(boolean bl) {
        this.v = bl;
    }

    @Override
    public void resetFallDistance() {
        super.resetFallDistance();
        this.resetInAirTicks();
    }

    @Override
    public boolean isOnline() {
        return this.connected && this.loggedIn;
    }

    @Override
    public boolean isOp() {
        return this.server.isOp(this.username);
    }

    @Override
    public void setOp(boolean bl) {
        if (bl == this.isOp()) {
            return;
        }
        if (bl) {
            this.server.addOp(this.username);
        } else {
            this.server.removeOp(this.username);
        }
        this.recalculatePermissions();
        this.adventureSettings.update();
        this.sendCommandData();
    }

    public void setShowAdmin(boolean bl) {
        this.ai = bl;
    }

    public boolean showAdmin() {
        return this.ai;
    }

    @Override
    public boolean isPermissionSet(String string) {
        return this.perm.isPermissionSet(string);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return this.perm.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(String string) {
        return this.perm != null && this.perm.hasPermission(string);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return this.perm.hasPermission(permission);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.addAttachment(plugin, null);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String string) {
        return this.addAttachment(plugin, string, null);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String string, Boolean bl) {
        return this.perm.addAttachment(plugin, string, bl);
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment) {
        this.perm.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        block3: {
            this.server.getPluginManager().unsubscribeFromPermission("nukkit.broadcast.user", this);
            this.server.getPluginManager().unsubscribeFromPermission("nukkit.broadcast.admin", this);
            if (this.perm == null) {
                return;
            }
            this.perm.recalculatePermissions();
            if (this.hasPermission("nukkit.broadcast.user")) {
                this.server.getPluginManager().subscribeToPermission("nukkit.broadcast.user", this);
            }
            if (this.hasPermission("nukkit.broadcast.admin")) {
                this.server.getPluginManager().subscribeToPermission("nukkit.broadcast.admin", this);
            }
            if (!this.enableClientCommand || !this.spawned) break block3;
            this.sendCommandData();
        }
    }

    public boolean isEnableClientCommand() {
        return this.enableClientCommand;
    }

    public void setEnableClientCommand(boolean bl) {
        block0: {
            this.enableClientCommand = bl;
            SetCommandsEnabledPacket setCommandsEnabledPacket = new SetCommandsEnabledPacket();
            setCommandsEnabledPacket.enabled = bl;
            this.dataPacket(setCommandsEnabledPacket);
            if (!bl) break block0;
            this.sendCommandData();
        }
    }

    public void sendCommandData() {
        block1: {
            AvailableCommandsPacket availableCommandsPacket = new AvailableCommandsPacket();
            HashMap<String, CommandDataVersions> hashMap = new HashMap<String, CommandDataVersions>();
            for (Command command : this.server.getCommandMap().getCommands().values()) {
                CommandDataVersions commandDataVersions;
                if (!command.isRegistered() || (commandDataVersions = command.generateCustomCommandData(this)) == null) continue;
                hashMap.put(command.getName(), commandDataVersions);
            }
            if (hashMap.isEmpty()) break block1;
            availableCommandsPacket.commands = hashMap;
            this.dataPacket(availableCommandsPacket);
        }
    }

    @Override
    public Map<String, PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }

    public Player(SourceInterface sourceInterface, Long l, InetSocketAddress inetSocketAddress) {
        super(null, new CompoundTag());
        this.interfaz = sourceInterface;
        this.networkSession = sourceInterface.getSession(inetSocketAddress);
        this.perm = new PermissibleBase(this);
        this.server = Server.getInstance();
        this.socketAddress = inetSocketAddress;
        this.P = Level.generateChunkLoaderId(this);
        this.gamemode = this.server.getGamemode();
        this.setLevel(this.server.getDefaultLevel());
        this.chunkRadius = this.viewDistance = this.server.getViewDistance();
        this.boundingBox = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.addDefaultWindows();
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    public void removeAchievement(String string) {
        this.achievements.remove(string);
    }

    public boolean hasAchievement(String string) {
        return this.achievements.contains(string);
    }

    public boolean isConnected() {
        return this.connected;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String string) {
        block1: {
            if (string == null) {
                string = "";
                this.server.getLogger().debug("Warning: setDisplayName: argument is null", new Throwable());
            }
            this.displayName = string;
            if (!this.spawned) break block1;
            this.server.updatePlayerListData(this.getUniqueId(), this.getId(), this.displayName, this.getSkin(), this.C.getXUID());
        }
    }

    @Override
    public void setSkin(Skin skin) {
        block0: {
            super.setSkin(skin);
            if (!this.spawned) break block0;
            this.server.updatePlayerListData(this.getUniqueId(), this.getId(), this.displayName, skin, this.C.getXUID());
        }
    }

    public String getAddress() {
        return this.socketAddress.getAddress().getHostAddress();
    }

    public int getPort() {
        return this.socketAddress.getPort();
    }

    public InetSocketAddress getSocketAddress() {
        return this.socketAddress;
    }

    public Position getNextPosition() {
        return this.newPosition != null ? new Position(this.newPosition.x, this.newPosition.y, this.newPosition.z, this.level) : this.getPosition();
    }

    public boolean isSleeping() {
        return this.sleeping != null;
    }

    public int getInAirTicks() {
        return this.inAirTicks;
    }

    public boolean isUsingItem() {
        return this.startAction > -1 && this.getDataFlag(0, 4);
    }

    public void setUsingItem(boolean bl) {
        this.startAction = bl ? this.server.getTick() : -1;
        this.setDataFlag(0, 4, bl);
    }

    public String getButtonText() {
        return this.o;
    }

    public void setButtonText(String string) {
        block1: {
            if (string == null) {
                string = "";
                this.server.getLogger().debug("Warning: setButtonText: argument is null", new Throwable());
            }
            if (string.equals(this.o)) break block1;
            this.o = string;
            this.setDataPropertyAndSendOnlyToSelf(new StringEntityData(100, this.o));
        }
    }

    public void unloadChunk(int n, int n2) {
        this.unloadChunk(n, n2, null);
    }

    public void unloadChunk(int n, int n2, Level level) {
        level = level == null ? this.level : level;
        long l = Level.chunkHash(n, n2);
        if (this.usedChunks.containsKey(l)) {
            for (Entity entity : level.getChunkEntities(n, n2).values()) {
                if (entity == this) continue;
                entity.despawnFrom(this);
            }
            this.usedChunks.remove(l);
        }
        level.unregisterChunkLoader(this, n, n2);
        this.loadQueue.remove(l);
    }

    private void a(boolean bl) {
        for (long l : this.usedChunks.keySet()) {
            int n = Level.getHashX(l);
            int n2 = Level.getHashZ(l);
            this.level.unregisterChunkLoader(this, n, n2);
            for (Entity entity : this.level.getChunkEntities(n, n2).values()) {
                if (entity == this) continue;
                if (bl) {
                    entity.despawnFrom(this);
                    continue;
                }
                entity.hasSpawned.remove(this.P);
            }
        }
        this.usedChunks.clear();
        this.loadQueue.clear();
    }

    public Position getSpawn() {
        if (this.spawnPosition != null && this.spawnPosition.getLevel() != null) {
            return this.spawnPosition;
        }
        return this.server.getDefaultLevel().getSafeSpawn();
    }

    public void sendChunk(int n, int n2, DataPacket dataPacket) {
        if (!this.connected) {
            return;
        }
        this.usedChunks.put(Level.chunkHash(n, n2), true);
        this.dataPacket(dataPacket);
        ++this.U;
        if (this.spawned) {
            for (Entity position : this.level.getChunkEntities(n, n2).values()) {
                if (this == position || position.closed || !position.isAlive()) continue;
                position.spawnTo(this);
            }
        }
        if (this.protocol >= 534) {
            for (BlockEntity blockEntity : this.level.getChunkBlockEntities(n, n2).values()) {
                if (!(blockEntity instanceof BlockEntityItemFrame) && !(blockEntity instanceof BlockEntityCampfire)) continue;
                ((BlockEntitySpawnable)blockEntity).spawnTo(this);
            }
        }
        if (this.A) {
            this.A = false;
            PlayerActionPacket playerActionPacket = new PlayerActionPacket();
            playerActionPacket.action = 14;
            playerActionPacket.entityId = this.getId();
            this.dataPacket(playerActionPacket);
        }
    }

    public void sendChunk(int n, int n2, int n3, byte[] byArray) {
        if (this.connected) {
            LevelChunkPacket levelChunkPacket = new LevelChunkPacket();
            levelChunkPacket.chunkX = n;
            levelChunkPacket.chunkZ = n2;
            levelChunkPacket.subChunkCount = n3;
            levelChunkPacket.data = byArray;
            this.sendChunk(n, n2, levelChunkPacket);
        }
    }

    protected void sendNextChunk() {
        block9: {
            if (!this.connected) {
                return;
            }
            if (Timings.playerChunkSendTimer != null) {
                Timings.playerChunkSendTimer.startTiming();
            }
            if (!this.loadQueue.isEmpty()) {
                ObjectIterator objectIterator = this.loadQueue.long2ObjectEntrySet().fastIterator();
                for (int k = 0; objectIterator.hasNext() && k < this.server.chunksPerTick; ++k) {
                    Long2ObjectMap.Entry entry = (Long2ObjectMap.Entry)objectIterator.next();
                    long l = entry.getLongKey();
                    int n = Level.getHashX(l);
                    int n2 = Level.getHashZ(l);
                    try {
                        this.usedChunks.put(l, false);
                        this.level.registerChunkLoader(this, n, n2, false);
                        if (!this.level.populateChunk(n, n2)) {
                            if (!this.spawned || this.teleportPosition != null) break;
                            continue;
                        }
                        objectIterator.remove();
                    }
                    catch (Exception exception) {
                        this.server.getLogger().logException(exception);
                        return;
                    }
                    PlayerChunkRequestEvent playerChunkRequestEvent = new PlayerChunkRequestEvent(this, n, n2);
                    this.server.getPluginManager().callEvent(playerChunkRequestEvent);
                    if (playerChunkRequestEvent.isCancelled()) continue;
                    this.level.requestChunk(n, n2, this);
                }
            }
            if (!this.T && this.U >= this.server.spawnThreshold) {
                this.T = true;
                if (this.protocol <= 274) {
                    this.doFirstSpawn();
                }
                this.sendPlayStatus(3);
                this.server.getPluginManager().callEvent(new PlayerLocallyInitializedEvent(this));
            }
            if (Timings.playerChunkSendTimer == null) break block9;
            Timings.playerChunkSendTimer.stopTiming();
        }
    }

    protected void doFirstSpawn() {
        block12: {
            Object object;
            this.locallyInitialized = true;
            if (this.spawned) {
                return;
            }
            this.noDamageTicks = 60;
            this.setAirTicks(400);
            if (this.hasPermission("nukkit.broadcast.user")) {
                this.server.getPluginManager().subscribeToPermission("nukkit.broadcast.user", this);
            }
            if (this.hasPermission("nukkit.broadcast.admin")) {
                this.server.getPluginManager().subscribeToPermission("nukkit.broadcast.admin", this);
            }
            boolean bl = this.getHealth() < 1.0f;
            PlayerRespawnEvent playerRespawnEvent = new PlayerRespawnEvent(this, this.level.getSafeSpawn(bl ? this.getSpawn() : this), true);
            this.server.getPluginManager().callEvent(playerRespawnEvent);
            this.spawned = true;
            if (bl) {
                if (this.server.isHardcore()) {
                    this.setBanned(true);
                    return;
                }
                this.teleport(playerRespawnEvent.getRespawnPosition(), null);
                if (this.protocol < 388) {
                    object = new RespawnPacket();
                    ((RespawnPacket)object).x = (float)playerRespawnEvent.getRespawnPosition().x;
                    ((RespawnPacket)object).y = (float)playerRespawnEvent.getRespawnPosition().y;
                    ((RespawnPacket)object).z = (float)playerRespawnEvent.getRespawnPosition().z;
                    this.dataPacket((DataPacket)object);
                }
                this.setHealth(this.getMaxHealth());
                this.foodData.setLevel(20, 20.0f);
                this.sendData(this);
            } else {
                this.setPosition(playerRespawnEvent.getRespawnPosition());
                this.sendPosition(playerRespawnEvent.getRespawnPosition(), this.yaw, this.pitch, 1);
                if (this.protocol < 274) {
                    object = new RespawnPacket();
                    ((RespawnPacket)object).x = (float)playerRespawnEvent.getRespawnPosition().x;
                    ((RespawnPacket)object).y = (float)playerRespawnEvent.getRespawnPosition().y;
                    ((RespawnPacket)object).z = (float)playerRespawnEvent.getRespawnPosition().z;
                    this.dataPacket((DataPacket)object);
                }
                this.getLevel().sendTime(this);
                this.getLevel().sendWeather(this);
            }
            object = new PlayerJoinEvent(this, new TranslationContainer((Object)((Object)TextFormat.YELLOW) + "%multiplayer.player.joined", new String[]{this.displayName}));
            this.server.getPluginManager().callEvent((Event)object);
            if (((PlayerJoinEvent)object).getJoinMessage().toString().trim().length() > 0) {
                this.server.broadcastMessage(((PlayerJoinEvent)object).getJoinMessage());
            }
            for (long l : this.usedChunks.keySet()) {
                int n = Level.getHashX(l);
                int n2 = Level.getHashZ(l);
                for (Entity entity : this.level.getChunkEntities(n, n2).values()) {
                    if (this == entity || entity.closed || !entity.isAlive()) continue;
                    entity.spawnTo(this);
                }
            }
            if (!this.isSpectator()) {
                this.spawnToAll();
            }
            if (!this.server.updateChecks || !this.isOp() || !Nukkit.getBranch().equals("private")) break block12;
            this.getServer().updateNotification(this, false);
        }
    }

    protected boolean orderChunks() {
        block14: {
            long l;
            if (!this.connected) {
                return false;
            }
            if (Timings.playerChunkOrderTimer != null) {
                Timings.playerChunkOrderTimer.startTiming();
            }
            this.nextChunkOrderRun = 200;
            this.loadQueue.clear();
            Long2ObjectOpenHashMap<Boolean> long2ObjectOpenHashMap = new Long2ObjectOpenHashMap<Boolean>(this.usedChunks);
            int n = this.getChunkX();
            int n2 = this.getChunkZ();
            int n3 = this.spawned ? this.chunkRadius : this.server.af;
            int n4 = n3 * n3;
            for (int k = 0; k <= n3; ++k) {
                int n5 = k * k;
                for (int i2 = 0; i2 <= k; ++i2) {
                    int n6 = n5 + i2 * i2;
                    if (n6 > n4) continue;
                    l = Level.chunkHash(n + k, n2 + i2);
                    if (this.usedChunks.get(l) != Boolean.TRUE) {
                        this.loadQueue.put(l, Boolean.TRUE);
                    }
                    long2ObjectOpenHashMap.remove(l);
                    l = Level.chunkHash(n - k - 1, n2 + i2);
                    if (this.usedChunks.get(l) != Boolean.TRUE) {
                        this.loadQueue.put(l, Boolean.TRUE);
                    }
                    long2ObjectOpenHashMap.remove(l);
                    l = Level.chunkHash(n + k, n2 - i2 - 1);
                    if (this.usedChunks.get(l) != Boolean.TRUE) {
                        this.loadQueue.put(l, Boolean.TRUE);
                    }
                    long2ObjectOpenHashMap.remove(l);
                    l = Level.chunkHash(n - k - 1, n2 - i2 - 1);
                    if (this.usedChunks.get(l) != Boolean.TRUE) {
                        this.loadQueue.put(l, Boolean.TRUE);
                    }
                    long2ObjectOpenHashMap.remove(l);
                    if (k == i2) continue;
                    l = Level.chunkHash(n + i2, n2 + k);
                    if (this.usedChunks.get(l) != Boolean.TRUE) {
                        this.loadQueue.put(l, Boolean.TRUE);
                    }
                    long2ObjectOpenHashMap.remove(l);
                    l = Level.chunkHash(n - i2 - 1, n2 + k);
                    if (this.usedChunks.get(l) != Boolean.TRUE) {
                        this.loadQueue.put(l, Boolean.TRUE);
                    }
                    long2ObjectOpenHashMap.remove(l);
                    l = Level.chunkHash(n + i2, n2 - k - 1);
                    if (this.usedChunks.get(l) != Boolean.TRUE) {
                        this.loadQueue.put(l, Boolean.TRUE);
                    }
                    long2ObjectOpenHashMap.remove(l);
                    l = Level.chunkHash(n - i2 - 1, n2 - k - 1);
                    if (this.usedChunks.get(l) != Boolean.TRUE) {
                        this.loadQueue.put(l, Boolean.TRUE);
                    }
                    long2ObjectOpenHashMap.remove(l);
                }
            }
            LongIterator longIterator = long2ObjectOpenHashMap.keySet().iterator();
            while (longIterator.hasNext()) {
                l = longIterator.nextLong();
                this.unloadChunk(Level.getHashX(l), Level.getHashZ(l));
            }
            if (this.protocol >= 313 && !this.loadQueue.isEmpty()) {
                NetworkChunkPublisherUpdatePacket networkChunkPublisherUpdatePacket = new NetworkChunkPublisherUpdatePacket();
                networkChunkPublisherUpdatePacket.position = this.asBlockVector3();
                networkChunkPublisherUpdatePacket.radius = this.chunkRadius << 4;
                this.dataPacket(networkChunkPublisherUpdatePacket);
            }
            if (Timings.playerChunkOrderTimer == null) break block14;
            Timings.playerChunkOrderTimer.stopTiming();
        }
        return true;
    }

    public boolean batchDataPacket(DataPacket dataPacket) {
        return this.dataPacket(dataPacket);
    }

    public boolean dataPacket(DataPacket dataPacket) {
        if (!this.connected) {
            return false;
        }
        DataPacket dataPacket2 = dataPacket.clone();
        dataPacket2.protocol = this.protocol;
        try (Timing timing = Timings.getSendDataPacketTiming(dataPacket2);){
            if (this.server.callDataPkSendEv) {
                DataPacketSendEvent dataPacketSendEvent = new DataPacketSendEvent(this, dataPacket2);
                this.server.getPluginManager().callEvent(dataPacketSendEvent);
                if (dataPacketSendEvent.isCancelled()) {
                    boolean bl = false;
                    return bl;
                }
            }
            if (Nukkit.DEBUG > 2) {
                aj.trace("Outbound {}: {}", (Object)this.getName(), (Object)dataPacket2);
            }
            if (dataPacket2 instanceof BatchPacket) {
                this.networkSession.sendPacket(dataPacket2);
            } else {
                this.server.batchPackets(new Player[]{this}, new DataPacket[]{dataPacket2}, true);
            }
        }
        return true;
    }

    public int dataPacket(DataPacket dataPacket, boolean bl) {
        return this.dataPacket(dataPacket) ? 1 : 0;
    }

    public boolean directDataPacket(DataPacket dataPacket) {
        return this.dataPacket(dataPacket);
    }

    public int directDataPacket(DataPacket dataPacket, boolean bl) {
        return this.directDataPacket(dataPacket) ? 1 : 0;
    }

    public void forceDataPacket(DataPacket dataPacket, Runnable runnable) {
        dataPacket.protocol = this.protocol;
        this.networkSession.sendImmediatePacket(dataPacket, runnable == null ? () -> {} : runnable);
    }

    public int getPing() {
        return this.interfaz.getNetworkLatency(this);
    }

    public boolean sleepOn(Vector3 vector3) {
        if (!this.isOnline()) {
            return false;
        }
        Entity[] entityArray = this.level.getNearbyEntities(this.boundingBox.grow(2.0, 1.0, 2.0), this);
        for (Entity entity : entityArray) {
            if (!(entity instanceof Player) || ((Player)entity).sleeping == null || !(vector3.distance(((Player)entity).sleeping) <= 0.1)) continue;
            return false;
        }
        PlayerBedEnterEvent playerBedEnterEvent = new PlayerBedEnterEvent(this, this.level.getBlock(vector3));
        this.server.getPluginManager().callEvent(playerBedEnterEvent);
        if (playerBedEnterEvent.isCancelled()) {
            return false;
        }
        this.sleeping = vector3.clone();
        this.teleport(new Location(vector3.x + 0.5, vector3.y + 0.5, vector3.z + 0.5, this.yaw, this.pitch, this.level), null);
        this.setDataProperty(new IntPositionEntityData(28, (int)vector3.x, (int)vector3.y, (int)vector3.z));
        this.setDataFlag(26, 1, true);
        if (this.getServer().bedSpawnpoints && !this.getSpawn().equals(vector3)) {
            this.setSpawn(vector3);
            this.sendMessage("\u00a77%tile.bed.respawnSet", true);
        }
        this.level.sleepTicks = 60;
        this.L = 0;
        return true;
    }

    public void setSpawn(Vector3 vector3) {
        Level level = !(vector3 instanceof Position) ? this.level : ((Position)vector3).getLevel();
        this.spawnPosition = new Position(vector3.x, vector3.y, vector3.z, level);
        this.a((int)vector3.x, (int)vector3.y, (int)vector3.z, level.getDimension());
    }

    private void a(int n, int n2, int n3, int n4) {
        SetSpawnPositionPacket setSpawnPositionPacket = new SetSpawnPositionPacket();
        setSpawnPositionPacket.spawnType = 0;
        setSpawnPositionPacket.x = n;
        setSpawnPositionPacket.y = n2;
        setSpawnPositionPacket.z = n3;
        setSpawnPositionPacket.dimension = n4;
        this.dataPacket(setSpawnPositionPacket);
    }

    public void stopSleep() {
        if (this.sleeping != null) {
            this.server.getPluginManager().callEvent(new PlayerBedLeaveEvent(this, this.level.getBlock(this.sleeping)));
            this.sleeping = null;
            this.setDataProperty(new IntPositionEntityData(28, 0, 0, 0));
            this.setDataFlag(26, 1, false);
            this.level.sleepTicks = 0;
            AnimatePacket animatePacket = new AnimatePacket();
            animatePacket.eid = this.id;
            animatePacket.action = AnimatePacket.Action.WAKE_UP;
            this.dataPacket(animatePacket);
        }
    }

    public Vector3 getSleepingPos() {
        return this.sleeping;
    }

    public boolean awardAchievement(String string) {
        if (!this.server.achievementsEnabled) {
            return false;
        }
        Achievement achievement = Achievement.achievements.get(string);
        if (achievement == null || this.hasAchievement(string)) {
            return false;
        }
        for (String string2 : achievement.requires) {
            if (this.hasAchievement(string2)) continue;
            return false;
        }
        PlayerAchievementAwardedEvent playerAchievementAwardedEvent = new PlayerAchievementAwardedEvent(this, string);
        this.server.getPluginManager().callEvent(playerAchievementAwardedEvent);
        if (playerAchievementAwardedEvent.isCancelled()) {
            return false;
        }
        this.achievements.add(string);
        achievement.broadcast(this);
        return true;
    }

    public int getGamemode() {
        return this.gamemode;
    }

    private static int a(int n) {
        if ((n &= 3) == 3) {
            return 1;
        }
        return n;
    }

    public boolean setGamemode(int n) {
        return this.setGamemode(n, false, null);
    }

    public boolean setGamemode(int n, boolean bl) {
        return this.setGamemode(n, bl, null);
    }

    public boolean setGamemode(int n, boolean bl, AdventureSettings adventureSettings) {
        DataPacket dataPacket;
        if (n < 0 || n > 3 || this.gamemode == n) {
            return false;
        }
        if (adventureSettings == null) {
            adventureSettings = this.adventureSettings.clone(this);
            adventureSettings.set(AdventureSettings.Type.WORLD_IMMUTABLE, (n & 2) > 0);
            adventureSettings.set(AdventureSettings.Type.MINE, (n & 2) <= 0);
            adventureSettings.set(AdventureSettings.Type.BUILD, (n & 2) <= 0);
            adventureSettings.set(AdventureSettings.Type.NO_PVM, n == 3);
            adventureSettings.set(AdventureSettings.Type.ALLOW_FLIGHT, (n & 1) > 0);
            adventureSettings.set(AdventureSettings.Type.NO_CLIP, n == 3);
            if (n == 3) {
                adventureSettings.set(AdventureSettings.Type.FLYING, true);
            } else if ((n & 1) == 0) {
                adventureSettings.set(AdventureSettings.Type.FLYING, false);
            }
        }
        PlayerGameModeChangeEvent playerGameModeChangeEvent = new PlayerGameModeChangeEvent(this, n, adventureSettings);
        this.server.getPluginManager().callEvent(playerGameModeChangeEvent);
        if (playerGameModeChangeEvent.isCancelled()) {
            return false;
        }
        this.gamemode = n;
        if (this.isSpectator()) {
            this.keepMovement = true;
            this.onGround = false;
            this.despawnFromAll();
        } else {
            this.keepMovement = false;
            this.spawnToAll();
        }
        this.namedTag.putInt("playerGameType", this.gamemode);
        if (!bl) {
            dataPacket = new SetPlayerGameTypePacket();
            dataPacket.gamemode = Player.a(n);
            this.dataPacket(dataPacket);
        }
        this.setAdventureSettings(playerGameModeChangeEvent.getNewAdventureSettings());
        if (this.isSpectator()) {
            this.teleport(this, null);
            this.setDataFlag(0, 17, true, false);
            this.setDataFlag(0, 48, false);
            if (this.protocol < 407) {
                dataPacket = new InventoryContentPacket();
                ((InventoryContentPacket)dataPacket).inventoryId = 121;
                this.dataPacket(dataPacket);
            }
        } else {
            this.setDataFlag(0, 17, false, false);
            this.setDataFlag(0, 48, true);
            if (this.protocol < 407) {
                dataPacket = new InventoryContentPacket();
                ((InventoryContentPacket)dataPacket).inventoryId = 121;
                ((InventoryContentPacket)dataPacket).slots = Item.getCreativeItems(this.protocol).toArray(new Item[0]);
                this.dataPacket(dataPacket);
            }
        }
        this.resetFallDistance();
        this.inventory.sendContents(this);
        this.inventory.sendHeldItem(this.hasSpawned.values());
        this.offhandInventory.sendContents(this);
        this.offhandInventory.sendContents(this.getViewers().values());
        this.inventory.sendCreativeContents();
        return true;
    }

    public void sendSettings() {
        this.adventureSettings.update();
    }

    public boolean isSurvival() {
        return this.gamemode == 0;
    }

    public boolean isCreative() {
        return this.gamemode == 1;
    }

    public boolean isSpectator() {
        return this.gamemode == 3;
    }

    public boolean isAdventure() {
        return this.gamemode == 2;
    }

    @Override
    public Item[] getDrops() {
        if (!this.isCreative() && !this.isSpectator()) {
            if (this.inventory != null) {
                ArrayList<Item> arrayList = new ArrayList<Item>(this.inventory.getContents().values());
                arrayList.addAll(this.offhandInventory.getContents().values());
                arrayList.addAll(this.playerUIInventory.getContents().values());
                return arrayList.toArray(new Item[0]);
            }
            return new Item[0];
        }
        return new Item[0];
    }

    @Override
    protected void checkGroundState(double d2, double d3, double d4, double d5, double d6, double d7) {
        if (!this.onGround || d2 != 0.0 || d3 != 0.0 || d4 != 0.0) {
            boolean bl = false;
            AxisAlignedBB axisAlignedBB = this.boundingBox.clone();
            axisAlignedBB.maxY = axisAlignedBB.minY + 0.5;
            axisAlignedBB.minY -= 1.0;
            AxisAlignedBB axisAlignedBB2 = this.boundingBox.clone();
            axisAlignedBB2.maxY = axisAlignedBB2.minY + 0.1;
            axisAlignedBB2.minY -= 0.2;
            int n = NukkitMath.floorDouble(axisAlignedBB.minX);
            int n2 = NukkitMath.floorDouble(axisAlignedBB.minY);
            int n3 = NukkitMath.floorDouble(axisAlignedBB.minZ);
            int n4 = NukkitMath.ceilDouble(axisAlignedBB.maxX);
            int n5 = NukkitMath.ceilDouble(axisAlignedBB.maxY);
            int n6 = NukkitMath.ceilDouble(axisAlignedBB.maxZ);
            for (int k = n3; k <= n6; ++k) {
                block1: for (int i2 = n; i2 <= n4; ++i2) {
                    for (int i3 = n2; i3 <= n5; ++i3) {
                        Block block = this.level.getBlock(this.chunk, i2, i3, k, true);
                        if (block.canPassThrough() || !block.collidesWithBB(axisAlignedBB2)) continue;
                        bl = true;
                        continue block1;
                    }
                }
            }
            this.onGround = bl;
        }
        this.isCollided = this.onGround;
    }

    @Override
    protected void checkBlockCollision() {
        block24: {
            int n;
            Object object;
            block26: {
                block25: {
                    Object object2;
                    if (this.isSpectator()) {
                        return;
                    }
                    boolean bl = false;
                    boolean bl2 = false;
                    for (Block block : this.getCollisionBlocks()) {
                        if (block.getId() == 90) {
                            bl = true;
                            continue;
                        }
                        if (block.getId() == 119) {
                            bl2 = true;
                            continue;
                        }
                        block.onEntityCollide(this);
                    }
                    this.inEndPortalTicks = bl2 ? ++this.inEndPortalTicks : 0;
                    if (this.server.endEnabled && this.inEndPortalTicks == 1) {
                        object2 = new EntityPortalEnterEvent(this, EntityPortalEnterEvent.PortalType.END);
                        this.getServer().getPluginManager().callEvent((Event)object2);
                        if (!((Event)object2).isCancelled()) {
                            int n2 = this.getLevel().getDimension();
                            if (n2 == 2) {
                                if (this.server.vanillaPortals && this.getSpawn().getLevel().getDimension() == 0) {
                                    if (this.teleport(this.getSpawn(), PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
                                        this.awardAchievement("theEnd2");
                                    }
                                } else if (this.teleport(this.getServer().getDefaultLevel().getSafeSpawn(), PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
                                    this.awardAchievement("theEnd2");
                                }
                            } else {
                                object = this.getServer().getLevelByName("the_end");
                                if (object != null && this.teleport(((Level)object).getSafeSpawn(), PlayerTeleportEvent.TeleportCause.END_PORTAL) && n2 == 0) {
                                    this.awardAchievement("theEnd");
                                }
                            }
                        }
                    }
                    if (bl) {
                        ++this.inPortalTicks;
                    } else {
                        this.inPortalTicks = 0;
                        this.portalPos = null;
                    }
                    if (!this.server.isNetherAllowed()) break block24;
                    if (this.server.vanillaPortals && this.inPortalTicks == (this.gamemode == 1 ? 1 : 40) && this.portalPos == null) {
                        object2 = this.level.calculatePortalMirror(this);
                        if (object2 == null) {
                            return;
                        }
                        for (int k = -1; k < 2; ++k) {
                            for (int i2 = -1; i2 < 2; ++i2) {
                                int n3;
                                int n4 = ((Vector3)object2).getChunkX() + k;
                                BaseFullChunk baseFullChunk = ((Position)object2).level.getChunk(n4, n3 = ((Vector3)object2).getChunkZ() + i2, false);
                                if (baseFullChunk != null && (baseFullChunk.isGenerated() || baseFullChunk.isPopulated())) continue;
                                ((Position)object2).level.generateChunk(n4, n3, true);
                            }
                        }
                        this.portalPos = object2;
                    }
                    if (this.inPortalTicks != (this.gamemode == 1 ? 1 : 80)) break block24;
                    object2 = new EntityPortalEnterEvent(this, EntityPortalEnterEvent.PortalType.NETHER);
                    this.getServer().getPluginManager().callEvent((Event)object2);
                    if (((Event)object2).isCancelled()) {
                        this.portalPos = null;
                        return;
                    }
                    n = this.getLevel().getDimension();
                    if (!this.server.vanillaPortals) break block25;
                    object = BlockNetherPortal.findNearestPortal(this.portalPos, n != 1);
                    if (object == null) {
                        BlockNetherPortal.spawnPortal(this.portalPos);
                        if (this.teleport(this.portalPos.add(1.5, 1.0, 0.5), PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) && n == 0) {
                            this.awardAchievement("portal");
                        }
                    } else if (this.teleport(BlockNetherPortal.getSafePortal((Position)object), PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) && n == 0) {
                        this.awardAchievement("portal");
                    }
                    this.portalPos = null;
                    break block24;
                }
                if (n != 1) break block26;
                this.teleport(this.getServer().getDefaultLevel().getSafeSpawn(), PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
                break block24;
            }
            object = this.getServer().getNetherWorld(this.level.getName());
            if (object == null || !this.teleport(((Level)object).getSafeSpawn(), PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) || n != 0) break block24;
            this.awardAchievement("portal");
        }
    }

    protected void checkNearEntities() {
        Entity[] entityArray;
        for (Entity entity : entityArray = this.level.getNearbyEntities(this.boundingBox.grow(1.0, 0.5, 1.0), this)) {
            if (!entity.isAlive()) continue;
            this.pickupEntity(entity, true);
        }
    }

    protected void handleMovement(Vector3 vector3) {
        Entity[] entityArray;
        Cloneable cloneable;
        if (!this.isAlive() || !this.spawned || this.teleportPosition != null || this.isSleeping()) {
            return;
        }
        double d2 = vector3.distanceSquared(this);
        if (d2 == 0.0) {
            if (this.lastYaw != this.yaw || this.lastPitch != this.pitch) {
                this.lastYaw = this.yaw;
                this.lastPitch = this.pitch;
                this.ad = true;
            }
            return;
        }
        int n = 9;
        if (this.riptideTicks > 95 || vector3.y - this.y < 2.0) {
            n = 49;
        }
        if (d2 > (double)n) {
            this.revertClientMotion(this);
            this.server.getLogger().debug(this.username + ": distanceSquared=" + d2 + " > maxDist=" + n);
            return;
        }
        if (this.chunk == null || !this.chunk.isGenerated()) {
            BaseFullChunk baseFullChunk = this.level.getChunk(vector3.getChunkX(), vector3.getChunkZ(), false);
            if (baseFullChunk == null || !baseFullChunk.isGenerated()) {
                this.nextChunkOrderRun = 0;
                this.revertClientMotion(this);
                return;
            }
            if (this.chunk != null) {
                this.chunk.removeEntity(this);
            }
            this.chunk = baseFullChunk;
        }
        double d3 = vector3.x - this.x;
        double d4 = vector3.y - this.y;
        double d5 = vector3.z - this.z;
        this.fastMove(d3, d4 += (double)this.ySize * 0.6, d5);
        double d6 = this.x - vector3.x;
        double d7 = this.y - vector3.y;
        double d8 = this.z - vector3.z;
        double d9 = this.getStepHeight() + (double)this.ySize;
        if (d7 >= -d9 || d7 <= d9) {
            d7 = 0.0;
        }
        boolean bl = false;
        if (d6 != 0.0 || d7 != 0.0 || d8 != 0.0) {
            double d10;
            if (this.checkMovement && this.riptideTicks < 1 && !this.server.getAllowFlight() && (this.isSurvival() || this.isAdventure()) && !this.isSleeping() && this.riding == null && (d10 = d6 * d6 + d8 * d8) > 0.5) {
                PlayerInvalidMoveEvent playerInvalidMoveEvent = new PlayerInvalidMoveEvent(this, true);
                this.getServer().getPluginManager().callEvent(playerInvalidMoveEvent);
                if (!playerInvalidMoveEvent.isCancelled()) {
                    bl = playerInvalidMoveEvent.isRevert();
                    this.server.getLogger().debug(this.username + ": hSpeed=" + d10 + " > MAXIMUM_SPEED=" + 0.5f);
                }
            }
            if (bl) {
                this.revertClientMotion(this);
            } else {
                this.x = vector3.x;
                this.y = vector3.y;
                this.z = vector3.z;
                d10 = this.getWidth() / 2.0f;
                this.boundingBox.setBounds(this.x - d10, this.y, this.z - d10, this.x + d10, this.y + (double)this.getHeight(), this.z + d10);
            }
        }
        Location location = new Location(this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch, this.level);
        Location location2 = this.getLocation();
        double d11 = Math.pow(this.lastX - location2.x, 2.0) + Math.pow(this.lastY - location2.y, 2.0) + Math.pow(this.z - location2.z, 2.0);
        double d12 = Math.abs(this.lastYaw - location2.yaw) + Math.abs(this.lastPitch - location2.pitch);
        if (!bl && (d11 > 1.0E-4 || d12 > 1.0)) {
            boolean bl2 = this.firstMove;
            this.firstMove = false;
            this.lastX = location2.x;
            this.lastY = location2.y;
            this.lastZ = location2.z;
            this.lastYaw = location2.yaw;
            this.lastPitch = location2.pitch;
            if (!bl2) {
                cloneable = null;
                ArrayList arrayList = null;
                if (!this.server.suomiCraftPEMode()) {
                    if (this.blocksAround != null) {
                        cloneable = new ArrayList(this.blocksAround);
                        this.blocksAround = null;
                    }
                    if (this.collisionBlocks != null) {
                        arrayList = new ArrayList(this.collisionBlocks);
                        this.collisionBlocks = null;
                    }
                }
                PlayerMoveEvent playerMoveEvent = new PlayerMoveEvent(this, location, location2);
                this.server.getPluginManager().callEvent(playerMoveEvent);
                bl = playerMoveEvent.isCancelled();
                if (!bl) {
                    if (this.server.suomiCraftPEMode()) {
                        this.blocksAround = null;
                        this.collisionBlocks = null;
                    }
                    if (!location2.equals(playerMoveEvent.getTo())) {
                        this.teleport(playerMoveEvent.getTo(), null);
                    } else {
                        this.addMovement(this.x, this.y, this.z, this.yaw, this.pitch, this.yaw);
                    }
                    if (this.server.getMobAiEnabled() && this.age % 20 == 0 && this.inventory != null && this.inventory.getHelmetFast().getId() != -155) {
                        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.getX() - (double)0.6f, this.getY() + (double)1.45f, this.getZ() - (double)0.6f, this.getX() + (double)0.6f, this.getY() + (double)2.9f, this.getZ() + (double)0.6f);
                        for (int k = 0; k < 8; ++k) {
                            for (Entity entity : entityArray = this.level.getCollidingEntities(axisAlignedBB.offset(-Math.sin(this.getYaw() * Math.PI / 180.0) * (double)k, (double)k * Math.tan(this.getPitch() * -1.0 * Math.PI / 180.0), Math.cos(this.getYaw() * Math.PI / 180.0) * (double)k))) {
                                if (!(entity instanceof EntityEnderman)) continue;
                                ((EntityEnderman)entity).stareToAngry();
                            }
                        }
                    }
                } else if (!this.server.suomiCraftPEMode()) {
                    this.blocksAround = cloneable;
                    this.collisionBlocks = arrayList;
                }
            }
            if (this.speed == null) {
                this.speed = new Vector3(location.x - location2.x, location.y - location2.y, location.z - location2.z);
            } else {
                this.speed.setComponents(location.x - location2.x, location.y - location2.y, location.z - location2.z);
            }
        } else if (this.speed == null) {
            this.speed = new Vector3(0.0, 0.0, 0.0);
        } else {
            this.speed.setComponents(0.0, 0.0, 0.0);
        }
        if (!bl && this.isFoodEnabled() && this.getServer().getDifficulty() > 0 && d2 >= 0.05) {
            double d13 = 0.0;
            double d14 = this.isInsideOfWater() ? 0.01 * d2 : 0.0;
            double d15 = d2;
            if (d14 != 0.0) {
                d15 = 0.0;
            }
            if (this.isSprinting()) {
                if (this.inAirTicks == 3 && d14 == 0.0) {
                    d13 = 0.2;
                }
                this.foodData.updateFoodExpLevel(0.1 * d15 + d13 + d14);
            } else {
                if (this.inAirTicks == 3 && d14 == 0.0) {
                    d13 = 0.05;
                }
                this.foodData.updateFoodExpLevel(d13 + d14);
            }
        }
        if (!bl && d11 > 1.0E-4 && this.riding == null) {
            Enchantment enchantment;
            Item item = this.inventory.getBootsFast();
            cloneable = item.getEnchantment(25);
            if (cloneable != null && ((Enchantment)cloneable).getLevel() > 0 && !this.isSpectator() && this.y >= 1.0 && this.y <= 255.0) {
                int n2 = 2 + ((Enchantment)cloneable).getLevel();
                for (int k = this.getFloorX() - n2; k < this.getFloorX() + n2 + 1; ++k) {
                    for (int i2 = this.getFloorZ() - n2; i2 < this.getFloorZ() + n2 + 1; ++i2) {
                        Block block = this.level.getBlock(this.chunk, k, this.getFloorY() - 1, i2, true);
                        if (block.getId() != 9 && (block.getId() != 8 || block.getDamage() != 0) || block.up().getId() != 0) continue;
                        entityArray = new WaterFrostEvent(block);
                        this.server.getPluginManager().callEvent((Event)entityArray);
                        if (entityArray.isCancelled()) continue;
                        this.level.setBlockAt((int)block.x, (int)block.y, (int)block.z, 207, 0);
                        this.level.scheduleUpdate(this.level.getBlock(block), Utils.random.nextInt(20, 40));
                    }
                }
            }
            if ((enchantment = item.getEnchantment(36)) != null && enchantment.getLevel() > 0) {
                int n3 = this.getLevel().getBlockIdAt(this.chunk, this.getFloorX(), this.getFloorY() - 1, this.getFloorZ());
                if (this.ac && n3 != 88) {
                    this.ac = false;
                    this.setMovementSpeed(0.1f, true);
                } else if (!this.ac && n3 == 88) {
                    this.ac = true;
                    float f2 = (float)enchantment.getLevel() * 0.105f + 1.3f;
                    this.setMovementSpeed(0.1f * f2, true);
                }
            }
        }
        if (!bl) {
            this.forceMovement = null;
            if (d2 != 0.0 && this.nextChunkOrderRun > 20) {
                this.nextChunkOrderRun = 20;
            }
            this.ad = false;
        }
        this.resetClientMovement();
    }

    protected void resetClientMovement() {
        this.newPosition = null;
    }

    protected void revertClientMotion(Location location) {
        this.lastX = location.getX();
        this.lastY = location.getY();
        this.lastZ = location.getZ();
        this.lastYaw = location.getYaw();
        this.lastPitch = location.getPitch();
        Location location2 = location.add(0.0, 1.0E-5, 0.0);
        this.sendPosition(location2, location.getYaw(), location.getPitch(), 1);
        this.forceMovement = location2;
        if (this.speed == null) {
            this.speed = new Vector3(0.0, 0.0, 0.0);
        } else {
            this.speed.setComponents(0.0, 0.0, 0.0);
        }
    }

    @Override
    public double getStepHeight() {
        return 0.6f;
    }

    @Override
    public void addMovement(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.a(d2, d3, d4, d5, d6, d7);
    }

    @Override
    public boolean setMotion(Vector3 vector3) {
        if (super.setMotion(vector3)) {
            if (this.chunk != null && this.spawned) {
                this.addMotion(this.motionX, this.motionY, this.motionZ);
                SetEntityMotionPacket setEntityMotionPacket = new SetEntityMotionPacket();
                setEntityMotionPacket.eid = this.id;
                setEntityMotionPacket.motionX = (float)vector3.x;
                setEntityMotionPacket.motionY = (float)vector3.y;
                setEntityMotionPacket.motionZ = (float)vector3.z;
                this.dataPacket(setEntityMotionPacket);
            }
            if (this.motionY > 0.0) {
                this.startAirTicks = (int)(-Math.log((double)this.getGravity() / ((double)this.getGravity() + (double)this.getDrag() * this.motionY)) / (double)this.getDrag() * 2.0 + 5.0);
            }
            return true;
        }
        return false;
    }

    public void sendAttributes() {
        int n = this.getMaxHealth();
        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
        updateAttributesPacket.entityId = this.getId();
        Attribute[] attributeArray = new Attribute[5];
        attributeArray[0] = Attribute.getAttribute(4).setMaxValue(n).setValue(this.health > 0.0f ? (this.health < (float)n ? this.health : (float)n) : 0.0f);
        attributeArray[1] = Attribute.getAttribute(7).setValue(this.foodData.getLevel()).setDefaultValue(this.foodData.getMaxLevel());
        attributeArray[2] = Attribute.getAttribute(5).setValue(this.getMovementSpeed()).setDefaultValue(this.getMovementSpeed());
        attributeArray[3] = Attribute.getAttribute(9).setValue(this.S);
        attributeArray[4] = Attribute.getAttribute(10).setValue((float)this.aa / (float)Player.calculateRequireExperience(this.S));
        updateAttributesPacket.entries = attributeArray;
        this.dataPacket(updateAttributesPacket);
    }

    @Override
    public boolean onUpdate(int n) {
        block34: {
            if (!this.loggedIn) {
                return false;
            }
            int n2 = n - this.lastUpdate;
            if (n2 <= 0) {
                return true;
            }
            this.lastUpdate = n;
            if (this.riptideTicks > 0) {
                --this.riptideTicks;
            }
            if (this.fishing != null && this.age % 20 == 0 && this.distanceSquared(this.fishing) > 1089.0) {
                this.stopFishing(false);
            }
            if (!this.isAlive() && this.spawned) {
                this.despawnFromAll();
                return true;
            }
            if (this.spawned) {
                while (!this.u.isEmpty()) {
                    this.handleMovement(this.u.poll());
                }
                if (this.ad) {
                    this.addMovement(this.x, this.y, this.z, this.yaw, this.pitch, this.yaw);
                    this.ad = false;
                }
                this.motionZ = 0.0;
                this.motionY = 0.0;
                this.motionX = 0.0;
                if (!this.isSpectator() && this.isAlive()) {
                    this.checkNearEntities();
                }
                this.entityBaseTick(n2);
                if (this.getServer().getDifficulty() == 0 && this.level.getGameRules().getBoolean(GameRule.NATURAL_REGENERATION)) {
                    if (this.getHealth() < (float)this.getRealMaxHealth() && this.age % 20 == 0) {
                        this.heal(1.0f);
                    }
                    if (this.foodData.getLevel() < 20 && this.age % 10 == 0) {
                        this.foodData.addFoodLevel(1, 0.0f);
                    }
                }
                if (this.isOnFire() && this.lastUpdate % 10 == 0) {
                    if (this.isCreative() && !this.isInsideOfFire()) {
                        this.extinguish();
                    } else if (this.getLevel().isRaining() && this.canSeeSky()) {
                        this.extinguish();
                    }
                }
                if (!this.isSpectator() && this.speed != null) {
                    if (this.onGround) {
                        this.resetFallDistance();
                    } else {
                        if (!(!this.checkMovement || this.riptideTicks >= 1 || this.isGliding() || this.server.getAllowFlight() || this.inAirTicks <= 20 || this.getAllowFlight() || this.isSleeping() || this.isImmobile() || this.isSwimming() || this.riding != null || this.hasEffect(24) || this.hasEffect(27))) {
                            double d2 = (double)(-this.getGravity()) / (double)this.getDrag() - (double)(-this.getGravity()) / (double)this.getDrag() * Math.exp(-((double)this.getDrag()) * (double)(this.inAirTicks - this.startAirTicks));
                            double d3 = (this.speed.y - d2) * (this.speed.y - d2);
                            if (this.isOnLadder()) {
                                this.resetFallDistance();
                            } else if (d3 > 2.0 && d2 < this.speed.y && this.speed.y != 0.0) {
                                if (this.inAirTicks < 150) {
                                    PlayerInvalidMoveEvent playerInvalidMoveEvent = new PlayerInvalidMoveEvent(this, true);
                                    this.getServer().getPluginManager().callEvent(playerInvalidMoveEvent);
                                    if (!playerInvalidMoveEvent.isCancelled()) {
                                        this.setMotion(new Vector3(0.0, d2, 0.0));
                                    }
                                } else if (this.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server", true, "type=MOVE, expectedVelocity=" + d2 + ", diff=" + d3 + ", speed.y=" + this.speed.y)) {
                                    return false;
                                }
                            }
                        }
                        if (this.y > this.highestPosition) {
                            this.highestPosition = this.y;
                        }
                        if (this.isSwimming() || this.isGliding() && Math.abs(this.speed.y) < 0.5 && this.getPitch() <= 40.0) {
                            this.resetFallDistance();
                        } else if (this.isGliding()) {
                            this.resetInAirTicks();
                        } else {
                            ++this.inAirTicks;
                        }
                    }
                    if (this.foodData != null) {
                        this.foodData.update(n2);
                    }
                }
            }
            this.checkTeleportPosition();
            if (this.spawned && !this.dummyBossBars.isEmpty() && n % 100 == 0) {
                this.dummyBossBars.values().forEach(DummyBossBar::updateBossEntityPosition);
            }
            this.d();
            if (this.isSleeping()) break block34;
            ++this.L;
        }
        return true;
    }

    private void d() {
        block2: {
            boolean bl;
            boolean bl2;
            block1: {
                if (this.protocol < 340 || !this.n) {
                    return;
                }
                bl2 = this.getInventory().getItemInHandFast().getId() == 513;
                boolean bl3 = bl = this.getOffhandInventory().getItemFast(0).getId() == 513;
                if (!this.isBlocking()) break block1;
                if (this.isSneaking() && (bl2 || bl)) break block2;
                this.setBlocking(false);
                break block2;
            }
            if (!this.isSneaking() || !bl2 && !bl) break block2;
            this.setBlocking(true);
        }
    }

    public void checkInteractNearby() {
        int n;
        int n2 = n = this.isCreative() ? 5 : 3;
        if (this.canInteract(this, n)) {
            EntityInteractable entityInteractable = this.getEntityPlayerLookingAt(n);
            if (entityInteractable != null) {
                String string = entityInteractable.getInteractButtonText(this);
                if (string == null) {
                    string = "";
                }
                this.setButtonText(string);
            } else {
                this.setButtonText("");
            }
        } else {
            this.setButtonText("");
        }
    }

    public EntityInteractable getEntityPlayerLookingAt(int n) {
        EntityInteractable entityInteractable;
        block6: {
            if (this.timing != null) {
                this.timing.startTiming();
            }
            entityInteractable = null;
            if (this.temporalVector != null) {
                Entity[] entityArray = this.level.getNearbyEntities(this.boundingBox.grow(n, n, n), this);
                try {
                    BlockIterator blockIterator = new BlockIterator(this.level, this.getPosition(), this.getDirectionVector(), this.getEyeHeight(), n);
                    if (blockIterator.hasNext()) {
                        Block block;
                        while (blockIterator.hasNext() && (entityInteractable = Player.a(entityArray, (block = blockIterator.next()).getFloorX(), block.getFloorY(), block.getFloorZ())) == null) {
                        }
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (this.timing == null) break block6;
            this.timing.stopTiming();
        }
        return entityInteractable;
    }

    private static EntityInteractable a(Entity[] entityArray, int n, int n2, int n3) {
        for (Entity entity : entityArray) {
            if (entity.getFloorX() != n || entity.getFloorY() != n2 || entity.getFloorZ() != n3 || !(entity instanceof EntityInteractable) || !((EntityInteractable)((Object)entity)).canDoInteraction()) continue;
            return (EntityInteractable)((Object)entity);
        }
        return null;
    }

    public void checkNetwork() {
        block3: {
            if (this.protocol < 419 && !this.isOnline()) {
                return;
            }
            if (!this.isOnline()) {
                return;
            }
            if (this.nextChunkOrderRun-- <= 0 || this.chunk == null) {
                this.orderChunks();
            }
            if (this.loadQueue.isEmpty() && this.spawned) break block3;
            this.sendNextChunk();
        }
    }

    public boolean canInteract(Vector3 vector3, double d2) {
        return this.canInteract(vector3, d2, 6.0);
    }

    public boolean canInteract(Vector3 vector3, double d2, double d3) {
        if (this.distanceSquared(vector3) > d2 * d2) {
            return false;
        }
        Vector2 vector2 = this.getDirectionPlane();
        return vector2.dot(new Vector2(vector3.x, vector3.z)) - vector2.dot(new Vector2(this.x, this.z)) >= -d3;
    }

    private boolean a(Vector3 vector3, double d2) {
        if (this.distanceSquared(vector3) > Math.pow(d2, 2.0)) {
            return false;
        }
        Vector2 vector2 = this.getDirectionPlane();
        return vector2.dot(new Vector2(vector3.x, vector3.z)) - vector2.dot(new Vector2(this.x, this.z)) >= -0.87;
    }

    protected void processLogin() {
        Object object;
        Object object2;
        boolean level;
        String string = this.username.toLowerCase();
        if (!this.server.isWhitelisted(string)) {
            this.kick(PlayerKickEvent.Reason.NOT_WHITELISTED, this.server.whitelistReason);
            return;
        }
        if (this.isBanned()) {
            String string2 = this.server.getNameBans().getEntires().get(string).getReason();
            this.kick(PlayerKickEvent.Reason.NAME_BANNED, "You are banned!" + (string2.isEmpty() ? "" : " Reason: " + string2));
            return;
        }
        if (!this.server.strongIPBans && this.server.getIPBans().isBanned(this.getAddress())) {
            this.kick(PlayerKickEvent.Reason.IP_BANNED, "Your IP is banned!");
            return;
        }
        for (Player object32 : new ArrayList<Player>(this.server.Y.values())) {
            if (object32 == this || object32.username == null || !object32.username.equalsIgnoreCase(this.username) && !this.getUniqueId().equals(object32.getUniqueId())) continue;
            object32.close("", "disconnectionScreen.loggedinOtherLocation");
            break;
        }
        File file = new File(this.server.getDataPath() + "players/" + string + ".dat");
        File file2 = new File(this.server.getDataPath() + "players/" + this.uuid.toString() + ".dat");
        if (this.server.savePlayerDataByUuid) {
            level = file2.exists();
            if (!level && file.exists()) {
                object2 = this.server.getOfflinePlayerData(string, false);
                if (!file.delete()) {
                    this.server.getLogger().warning("Could not delete legacy player data for " + this.username);
                }
            } else {
                object2 = this.server.getOfflinePlayerData(this.uuid, !level);
            }
        } else {
            level = !file.exists();
            object2 = level && file2.exists() ? this.server.getOfflinePlayerData(this.uuid, false) : this.server.getOfflinePlayerData(string, level);
        }
        if (object2 == null) {
            this.close(this.getLeaveMessage(), "Invalid data");
            return;
        }
        if (this.C.isXboxAuthed() || !this.server.xboxAuth) {
            this.server.updateName(this.uuid, this.username);
        }
        this.playedBefore = ((CompoundTag)object2).getLong("lastPlayed") - ((CompoundTag)object2).getLong("firstPlayed") > 1L;
        ((CompoundTag)object2).putString("NameTag", this.username);
        this.setExperience(((CompoundTag)object2).getInt("EXP"), ((CompoundTag)object2).getInt("expLevel"));
        if (this.server.getForceGamemode()) {
            this.gamemode = this.server.getGamemode();
            ((CompoundTag)object2).putInt("playerGameType", this.gamemode);
        } else {
            this.gamemode = ((CompoundTag)object2).getInt("playerGameType") & 3;
        }
        this.adventureSettings = new AdventureSettings(this).set(AdventureSettings.Type.WORLD_IMMUTABLE, this.isAdventure() || this.isSpectator()).set(AdventureSettings.Type.MINE, !this.isAdventure() && !this.isSpectator()).set(AdventureSettings.Type.BUILD, !this.isAdventure() && !this.isSpectator()).set(AdventureSettings.Type.NO_PVM, this.isSpectator()).set(AdventureSettings.Type.AUTO_JUMP, true).set(AdventureSettings.Type.ALLOW_FLIGHT, this.isCreative() || this.isSpectator()).set(AdventureSettings.Type.NO_CLIP, this.isSpectator()).set(AdventureSettings.Type.FLYING, this.isSpectator());
        Level level2 = this.server.getLevelByName(((CompoundTag)object2).getString("Level"));
        if (level2 == null || ((CompoundTag)object2).getShort("Health") < 1) {
            this.setLevel(this.server.getDefaultLevel());
            ((CompoundTag)object2).putString("Level", this.level.getName());
            object = this.level.getSpawnLocation();
            ((CompoundTag)object2).getList("Pos", DoubleTag.class).add(new DoubleTag("0", ((Position)object).x)).add(new DoubleTag("1", ((Position)object).y)).add(new DoubleTag("2", ((Position)object).z));
        } else {
            this.setLevel(level2);
        }
        if (((CompoundTag)object2).contains("SpawnLevel") && (object = this.server.getLevelByName(((CompoundTag)object2).getString("SpawnLevel"))) != null) {
            this.spawnPosition = new Position(((CompoundTag)object2).getInt("SpawnX"), ((CompoundTag)object2).getInt("SpawnY"), ((CompoundTag)object2).getInt("SpawnZ"), level2);
        }
        this.L = ((CompoundTag)object2).getInt("TimeSinceRest");
        for (Tag tag : ((CompoundTag)object2).getCompound("Achievements").getAllTags()) {
            if (!(tag instanceof ByteTag) || ((ByteTag)tag).getData() <= 0) continue;
            this.achievements.add(tag.getName());
        }
        ((CompoundTag)object2).putLong("lastPlayed", System.currentTimeMillis() / 1000L);
        object = this.getUniqueId();
        ((CompoundTag)object2).putLong("UUIDLeast", ((UUID)object).getLeastSignificantBits());
        ((CompoundTag)object2).putLong("UUIDMost", ((UUID)object).getMostSignificantBits());
        if (this.server.getAutoSave()) {
            if (this.server.savePlayerDataByUuid) {
                this.server.saveOfflinePlayerData(this.uuid, (CompoundTag)object2, true);
            } else {
                this.server.saveOfflinePlayerData(this.username, (CompoundTag)object2, true);
            }
        }
        this.sendPlayStatus(0);
        ListTag<DoubleTag> listTag = ((CompoundTag)object2).getList("Pos", DoubleTag.class);
        super.init(this.level.getChunk(NukkitMath.floorDouble(listTag.get((int)0).data) >> 4, NukkitMath.floorDouble(listTag.get((int)2).data) >> 4, true), (CompoundTag)object2);
        if (!this.namedTag.contains("foodLevel")) {
            this.namedTag.putInt("foodLevel", 20);
        }
        if (!this.namedTag.contains("FoodSaturationLevel")) {
            this.namedTag.putFloat("FoodSaturationLevel", 20.0f);
        }
        this.foodData = new PlayerFood(this, this.namedTag.getInt("foodLevel"), this.namedTag.getFloat("foodSaturationLevel"));
        if (this.isSpectator()) {
            this.keepMovement = true;
            this.onGround = false;
        }
        this.forceMovement = this.teleportPosition = this.getPosition();
        ResourcePacksInfoPacket resourcePacksInfoPacket = new ResourcePacksInfoPacket();
        resourcePacksInfoPacket.resourcePackEntries = this.server.getResourcePackManager().getResourceStack();
        resourcePacksInfoPacket.mustAccept = this.server.getForceResources();
        this.dataPacket(resourcePacksInfoPacket);
    }

    protected void completeLoginSequence() {
        Object object;
        if (this.loggedIn) {
            this.server.getLogger().warning("Tried to call completeLoginSequence but player is already logged in: " + this.username);
            return;
        }
        PlayerLoginEvent playerLoginEvent = new PlayerLoginEvent(this, "Plugin reason");
        this.server.getPluginManager().callEvent(playerLoginEvent);
        if (playerLoginEvent.isCancelled()) {
            this.close(this.getLeaveMessage(), playerLoginEvent.getKickMessage());
            return;
        }
        StartGamePacket startGamePacket = new StartGamePacket();
        startGamePacket.entityUniqueId = this.id;
        startGamePacket.entityRuntimeId = this.id;
        startGamePacket.playerGamemode = Player.a(this.gamemode);
        startGamePacket.x = (float)this.x;
        startGamePacket.y = (float)this.y;
        startGamePacket.z = (float)this.z;
        startGamePacket.yaw = (float)this.yaw;
        startGamePacket.pitch = (float)this.pitch;
        startGamePacket.dimension = this.getServer().dimensionsEnabled ? (byte)(this.level.getDimension() & 0xFF) : (byte)0;
        startGamePacket.worldGamemode = Player.a(this.gamemode);
        startGamePacket.difficulty = this.server.getDifficulty();
        if (this.level.getProvider() == null || this.level.getProvider().getSpawn() == null) {
            startGamePacket.spawnX = (int)this.x;
            startGamePacket.spawnY = (int)this.y;
            startGamePacket.spawnZ = (int)this.z;
        } else {
            object = this.level.getProvider().getSpawn();
            startGamePacket.spawnX = (int)((Vector3)object).x;
            startGamePacket.spawnY = (int)((Vector3)object).y;
            startGamePacket.spawnZ = (int)((Vector3)object).z;
        }
        startGamePacket.commandsEnabled = this.enableClientCommand;
        startGamePacket.gameRules = this.getLevel().getGameRules();
        startGamePacket.worldName = this.getServer().getNetwork().getName();
        startGamePacket.version = this.getLoginChainData().getGameVersion();
        if (this.getLevel().isRaining()) {
            startGamePacket.rainLevel = this.getLevel().getRainTime();
            if (this.getLevel().isThundering()) {
                startGamePacket.lightningLevel = this.getLevel().getThunderTime();
            }
        }
        startGamePacket.isMovementServerAuthoritative = this.isMovementServerAuthoritative();
        startGamePacket.forceNoServerAuthBlockBreaking = !this.isMovementServerAuthoritative() && this.protocol >= 440;
        this.forceDataPacket(startGamePacket, null);
        this.loggedIn = true;
        object = this.getServer().getLanguage().translateString("nukkit.player.logIn", (Object)((Object)TextFormat.AQUA) + this.username + (Object)((Object)TextFormat.WHITE), this.getAddress(), String.valueOf(this.getPort()));
        if (this.server.logJoinLocation) {
            object = (String)object + " (" + this.level.getName() + ", " + this.getFloorX() + ", " + this.getFloorY() + ", " + this.getFloorZ() + ')';
        }
        this.server.getLogger().info((String)object);
        this.setDataFlag(0, 19, true, false);
        this.setDataFlag(0, 14, true, false);
        this.setDataProperty(new ByteEntityData(81, 1), false);
        if (this.isSpectator()) {
            this.setDataFlag(0, 17, true, false);
            this.setDataFlag(0, 48, false, false);
        }
        try {
            if (this.protocol >= 313) {
                if (this.protocol >= 361) {
                    this.dataPacket(new BiomeDefinitionListPacket());
                }
                this.dataPacket(new AvailableEntityIdentifiersPacket());
            }
            if (this.protocol >= 419) {
                this.a((int)this.x, (int)this.y, (int)this.z, this.level.getDimension());
            }
            this.getLevel().sendTime(this);
            SetDifficultyPacket setDifficultyPacket = new SetDifficultyPacket();
            setDifficultyPacket.difficulty = this.server.getDifficulty();
            this.dataPacket(setDifficultyPacket);
            SetCommandsEnabledPacket setCommandsEnabledPacket = new SetCommandsEnabledPacket();
            setCommandsEnabledPacket.enabled = this.isEnableClientCommand();
            this.dataPacket(setCommandsEnabledPacket);
            this.adventureSettings.update();
            GameRulesChangedPacket gameRulesChangedPacket = new GameRulesChangedPacket();
            gameRulesChangedPacket.gameRulesMap = this.level.getGameRules().getGameRules();
            this.dataPacket(gameRulesChangedPacket);
            this.server.sendFullPlayerListData(this);
            this.sendAttributes();
            if (this.protocol < 407 && this.gamemode == 3) {
                InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
                inventoryContentPacket.inventoryId = 121;
                this.dataPacket(inventoryContentPacket);
            } else {
                this.inventory.sendCreativeContents();
            }
            this.sendAllInventories();
            this.inventory.sendHeldItemIfNotAir(this);
            this.server.sendRecipeList(this);
            if (this.isEnableClientCommand()) {
                this.sendCommandData();
            }
            this.sendPotionEffects(this);
            this.sendData(this);
            if (!this.server.checkOpMovement && this.isOp()) {
                this.setCheckMovement(false);
            }
            if (this.isOp() || this.hasPermission("nukkit.textcolor") || this.server.suomiCraftPEMode()) {
                this.setRemoveFormat(false);
            }
            this.server.onPlayerCompleteLoginSequence(this);
        }
        catch (Exception exception) {
            this.close("", "Internal Server Error");
            this.server.getLogger().logException(exception);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void handleDataPacket(DataPacket dataPacket) {
        if (!this.connected) {
            return;
        }
        if (!this.I && dataPacket.pid() != 1 && dataPacket.pid() != -63 && dataPacket.pid() != -1) {
            this.server.getLogger().warning("Ignoring " + dataPacket.getClass().getSimpleName() + " from " + this.getAddress() + " due to player not verified yet");
            if (this.p++ <= 100) return;
            this.close("", "Too many failed login attempts");
            return;
        }
        if (this.a(dataPacket, 1200, 1200)) {
            return;
        }
        if (!this.loggedIn && !N.contains(dataPacket.pid())) {
            this.server.getLogger().warning("Ignoring " + dataPacket.getClass().getSimpleName() + " from " + this.username + " due to player not logged in yet");
            return;
        }
        try (Timing timing = Timings.getReceiveDataPacketTiming(dataPacket);){
            Object object;
            Item item;
            DataPacketReceiveEvent dataPacketReceiveEvent = new DataPacketReceiveEvent(this, dataPacket);
            this.server.getPluginManager().callEvent(dataPacketReceiveEvent);
            if (dataPacketReceiveEvent.isCancelled()) {
                return;
            }
            if (dataPacket.pid() == -1) {
                this.server.getNetwork().processBatch((BatchPacket)dataPacket, this);
                return;
            }
            if (dataPacket.protocol != this.protocol && this.server.minimumProtocol != ProtocolInfo.CURRENT_PROTOCOL && dataPacket.pid() != 1 && dataPacket.pid() != -63) {
                this.server.getLogger().warning("handleDataPacket: packet protocol != player protocol", new Throwable());
            }
            if (Nukkit.DEBUG > 2) {
                aj.trace("Inbound {}: {}", (Object)this.getName(), (Object)dataPacket);
            }
            switch (dataPacket.pid()) {
                case -63: {
                    if (this.raknetProtocol < 11) {
                        return;
                    }
                    if (this.loggedIn) {
                        this.getServer().getLogger().debug(this.username + ": got a RequestNetworkSettingsPacket but player is already logged in");
                        return;
                    }
                    if (this.getNetworkSession().getCompression() != CompressionProvider.NONE) {
                        this.getServer().getLogger().debug(this.username + ": got a RequestNetworkSettingsPacket but network settings are already updated");
                        return;
                    }
                    RequestNetworkSettingsPacket requestNetworkSettingsPacket = (RequestNetworkSettingsPacket)dataPacket;
                    this.protocol = requestNetworkSettingsPacket.protocolVersion;
                    if (!ProtocolInfo.SUPPORTED_PROTOCOLS.contains(this.protocol)) {
                        this.close("", "You are running unsupported Minecraft version");
                        this.server.getLogger().debug(this.getAddress() + " disconnected with unsupported protocol " + this.protocol);
                        return;
                    }
                    if (this.protocol < this.server.minimumProtocol) {
                        this.close("", "Support for this Minecraft version is not enabled");
                        this.server.getLogger().debug(this.getAddress() + " disconnected with unsupported protocol " + this.protocol);
                        return;
                    }
                    NetworkSettingsPacket networkSettingsPacket = new NetworkSettingsPacket();
                    networkSettingsPacket.compressionAlgorithm = PacketCompressionAlgorithm.ZLIB;
                    networkSettingsPacket.compressionThreshold = 1;
                    this.forceDataPacket(networkSettingsPacket, () -> this.networkSession.setCompression(CompressionProvider.ZLIB_RAW));
                    return;
                }
                case 1: {
                    if (this.loggedIn) {
                        this.close("", "Invalid login packet");
                        return;
                    }
                    LoginPacket loginPacket = (LoginPacket)dataPacket;
                    this.protocol = loginPacket.getProtocol();
                    if (!ProtocolInfo.SUPPORTED_PROTOCOLS.contains(this.protocol)) {
                        this.close("", "You are running unsupported Minecraft version");
                        this.server.getLogger().debug(TextFormat.clean(loginPacket.username) + " disconnected with unsupported protocol " + this.protocol);
                        return;
                    }
                    if (this.protocol < this.server.minimumProtocol) {
                        this.close("", "Support for this Minecraft version is not enabled");
                        this.server.getLogger().debug(TextFormat.clean(loginPacket.username) + " disconnected with unsupported protocol " + this.protocol);
                        return;
                    }
                    if (loginPacket.invalidData) {
                        this.close("", "Received invalid login data");
                        return;
                    }
                    this.displayName = this.username = TextFormat.clean(loginPacket.username);
                    this.iusername = this.username.toLowerCase();
                    this.setDataProperty(new StringEntityData(4, this.username), false);
                    this.C = ClientChainData.read(loginPacket);
                    String[] stringArray = this.C.getServerAddress().toLowerCase().split(":");
                    if (!this.C.isXboxAuthed() && this.server.xboxAuth || stringArray.length > 0 && (stringArray[0].endsWith(".ru") || stringArray[0].endsWith(".ru."))) {
                        this.close("", "disconnectionScreen.notAuthenticated");
                        if (!this.server.banXBAuthFailed) return;
                        this.server.getNetwork().blockAddress(this.socketAddress.getAddress(), 5);
                        this.server.getLogger().notice("Blocked " + this.getAddress() + " for 5 seconds due to failed Xbox auth");
                        return;
                    }
                    if (this.server.getOnlinePlayersCount() >= this.server.getMaxPlayers() && this.kick(PlayerKickEvent.Reason.SERVER_FULL, "disconnectionScreen.serverFull", false)) {
                        return;
                    }
                    this.getServer().getLogger().debug("Name: " + this.username + " Protocol: " + this.protocol + " Version: " + this.C.getGameVersion());
                    this.randomClientId = loginPacket.clientId;
                    this.uuid = loginPacket.clientUUID;
                    this.rawUUID = Binary.writeUUID(this.uuid);
                    boolean bl = true;
                    int n2 = loginPacket.username.length();
                    if (n2 > 16 || n2 < 3) {
                        bl = false;
                    }
                    if (bl) {
                        for (int k = 0; k < n2; ++k) {
                            char c2 = loginPacket.username.charAt(k);
                            if (c2 >= 'a' && c2 <= 'z' || c2 >= 'A' && c2 <= 'Z' || c2 >= '0' && c2 <= '9' || c2 == '_' || c2 == ' ') continue;
                            bl = false;
                            break;
                        }
                    }
                    if (!bl || Objects.equals(this.iusername, "rcon") || Objects.equals(this.iusername, "console")) {
                        this.close("", "disconnectionScreen.invalidName");
                        return;
                    }
                    Skin skin = loginPacket.skin;
                    if (!skin.isValid(this, this.server.doNotLimitSkinGeometry)) {
                        this.close("", "disconnectionScreen.invalidSkin");
                        return;
                    }
                    this.setSkin(skin.isPersona() && !this.server.personaSkins ? Skin.NO_PERSONA_SKIN : skin);
                    PlayerPreLoginEvent playerPreLoginEvent = new PlayerPreLoginEvent(this, "Plugin reason");
                    this.server.getPluginManager().callEvent(playerPreLoginEvent);
                    if (playerPreLoginEvent.isCancelled()) {
                        this.close("", playerPreLoginEvent.getKickMessage());
                        return;
                    }
                    Player player = this;
                    this.I = true;
                    this.ag = new c(this, skin, player);
                    this.server.getScheduler().scheduleAsyncTask(this.ag);
                    this.processLogin();
                    return;
                }
                case 8: {
                    if (this.spawned) {
                        this.getServer().getLogger().debug(this.username + ": ResourcePackClientResponsePacket after player spawned");
                        return;
                    }
                    ResourcePackClientResponsePacket resourcePackClientResponsePacket = (ResourcePackClientResponsePacket)dataPacket;
                    switch (resourcePackClientResponsePacket.responseStatus) {
                        case 1: {
                            this.close("", "disconnectionScreen.noReason");
                            return;
                        }
                        case 2: {
                            ResourcePackClientResponsePacket.Entry[] entryArray = resourcePackClientResponsePacket.packEntries;
                            int n3 = entryArray.length;
                            int n4 = 0;
                            while (n4 < n3) {
                                ResourcePackClientResponsePacket.Entry entry = entryArray[n4];
                                ResourcePack resourcePack = this.server.getResourcePackManager().getPackById(entry.uuid);
                                if (resourcePack == null) {
                                    this.close("", "disconnectionScreen.resourcePack");
                                    return;
                                }
                                ResourcePackDataInfoPacket resourcePackDataInfoPacket = new ResourcePackDataInfoPacket();
                                resourcePackDataInfoPacket.packId = resourcePack.getPackId();
                                resourcePackDataInfoPacket.maxChunkSize = 8192;
                                resourcePackDataInfoPacket.chunkCount = MathHelper.ceil((float)resourcePack.getPackSize() / 8192.0f);
                                resourcePackDataInfoPacket.compressedPackSize = resourcePack.getPackSize();
                                resourcePackDataInfoPacket.sha256 = resourcePack.getSha256();
                                this.dataPacket(resourcePackDataInfoPacket);
                                ++n4;
                            }
                            return;
                        }
                        case 3: {
                            ResourcePackStackPacket resourcePackStackPacket = new ResourcePackStackPacket();
                            resourcePackStackPacket.mustAccept = this.server.getForceResources() && !this.server.forceResourcesAllowOwnPacks;
                            resourcePackStackPacket.resourcePackStack = this.server.getResourcePackManager().getResourceStack();
                            this.dataPacket(resourcePackStackPacket);
                            return;
                        }
                        case 4: {
                            this.shouldLogin = true;
                            if (!this.ag.isFinished()) return;
                            this.ag.onCompletion(this.server);
                            return;
                        }
                    }
                    return;
                }
                case 84: {
                    ResourcePackChunkRequestPacket resourcePackChunkRequestPacket = (ResourcePackChunkRequestPacket)dataPacket;
                    ResourcePack resourcePack = this.server.getResourcePackManager().getPackById(resourcePackChunkRequestPacket.packId);
                    if (resourcePack == null) {
                        this.close("", "disconnectionScreen.resourcePack");
                        return;
                    }
                    ResourcePackChunkDataPacket resourcePackChunkDataPacket = new ResourcePackChunkDataPacket();
                    resourcePackChunkDataPacket.packId = resourcePack.getPackId();
                    resourcePackChunkDataPacket.chunkIndex = resourcePackChunkRequestPacket.chunkIndex;
                    resourcePackChunkDataPacket.data = resourcePack.getPackChunk(8192 * resourcePackChunkRequestPacket.chunkIndex, 8192);
                    resourcePackChunkDataPacket.progress = 8192L * (long)resourcePackChunkRequestPacket.chunkIndex;
                    this.dataPacket(resourcePackChunkDataPacket);
                    return;
                }
                case 93: {
                    PlayerSkinPacket playerSkinPacket = (PlayerSkinPacket)dataPacket;
                    Skin skin = playerSkinPacket.skin;
                    if (this.a(dataPacket, 20, 100)) {
                        return;
                    }
                    if (!skin.isValid(this, this.server.doNotLimitSkinGeometry)) {
                        this.close("", "disconnectionScreen.invalidSkin");
                        return;
                    }
                    PlayerChangeSkinEvent playerChangeSkinEvent = new PlayerChangeSkinEvent(this, skin);
                    playerChangeSkinEvent.setCancelled(TimeUnit.SECONDS.toMillis(this.server.getPlayerSkinChangeCooldown()) > System.currentTimeMillis() - this.lastSkinChange);
                    this.server.getPluginManager().callEvent(playerChangeSkinEvent);
                    if (playerChangeSkinEvent.isCancelled()) return;
                    this.lastSkinChange = System.currentTimeMillis();
                    this.setSkin(skin.isPersona() && !this.server.personaSkins ? Skin.NO_PERSONA_SKIN : skin);
                    return;
                }
                case 57: {
                    if (!this.spawned) return;
                    if (!this.isAlive()) return;
                    if (this.isMovementServerAuthoritative()) {
                        return;
                    }
                    if (!(this.riding instanceof EntityControllable)) return;
                    PlayerInputPacket playerInputPacket = (PlayerInputPacket)dataPacket;
                    if (playerInputPacket.jumping && this.t <= 0) {
                        this.t = this.server.getTick();
                    } else if (!playerInputPacket.jumping && this.t > 0) {
                        ((EntityControllable)((Object)this.riding)).onJump(this, this.server.getTick() - this.t);
                        this.t = 0;
                    }
                    if (!((double)playerInputPacket.motionX >= -1.0)) return;
                    if (!((double)playerInputPacket.motionX <= 1.0)) return;
                    if (!((double)playerInputPacket.motionY >= -1.0)) return;
                    if (!((double)playerInputPacket.motionY <= 1.0)) return;
                    ((EntityControllable)((Object)this.riding)).onPlayerInput(this, playerInputPacket.motionX, playerInputPacket.motionY);
                    return;
                }
                case 19: {
                    Block block;
                    if (this.teleportPosition != null) return;
                    if (!this.spawned) {
                        return;
                    }
                    MovePlayerPacket movePlayerPacket = (MovePlayerPacket)dataPacket;
                    Vector3 vector3 = new Vector3(movePlayerPacket.x, movePlayerPacket.y - this.getBaseOffset(), movePlayerPacket.z);
                    double d2 = vector3.distanceSquared(this);
                    if (d2 > 100.0) {
                        this.sendPosition(this, movePlayerPacket.yaw, movePlayerPacket.pitch, 1);
                        return;
                    }
                    if (!(!this.server.suomiCraftPEMode() || this.getAdventureSettings().get(AdventureSettings.Type.NO_CLIP) || !(block = this.level.getBlock(this.chunk, vector3.getFloorX(), NukkitMath.floorDouble(vector3.getY() + 0.2), vector3.getFloorZ(), false)).isSolid() || block.isTransparent() && !Player.a(block) || block instanceof BlockFallable && !this.isInsideOfSolid())) {
                        this.sendPosition(this.onGround && vector3.y < this.y ? this.add(0.0, 0.1, 0.0) : this, movePlayerPacket.yaw, movePlayerPacket.pitch, 1);
                        return;
                    }
                    boolean bl = false;
                    if (!this.isAlive() || !this.spawned) {
                        bl = true;
                        this.forceMovement = this;
                    }
                    if (this.forceMovement != null && (bl || vector3.distanceSquared(this.forceMovement) > 0.1)) {
                        this.sendPosition(this.forceMovement, movePlayerPacket.yaw, movePlayerPacket.pitch, 1);
                        return;
                    }
                    movePlayerPacket.yaw %= 360.0f;
                    movePlayerPacket.pitch %= 360.0f;
                    if (movePlayerPacket.yaw < 0.0f) {
                        movePlayerPacket.yaw += 360.0f;
                    }
                    this.setRotation(movePlayerPacket.yaw, movePlayerPacket.pitch);
                    this.newPosition = vector3;
                    this.u.offer(vector3);
                    this.forceMovement = null;
                    return;
                }
                case -112: {
                    Block block;
                    Vector3 vector3;
                    double d3;
                    Object object2;
                    if (!this.spawned) return;
                    if (!this.isMovementServerAuthoritative()) {
                        return;
                    }
                    PlayerAuthInputPacket playerAuthInputPacket = (PlayerAuthInputPacket)dataPacket;
                    if (!playerAuthInputPacket.getBlockActionData().isEmpty()) {
                        for (PlayerBlockActionData playerBlockActionData : playerAuthInputPacket.getBlockActionData().values()) {
                            BlockVector3 blockVector3;
                            object2 = playerBlockActionData.getPosition();
                            BlockFace blockFace = BlockFace.fromIndex(playerBlockActionData.getFacing());
                            if (this.V != null && this.V.getAction() == PlayerActionType.PREDICT_DESTROY_BLOCK && playerBlockActionData.getAction() == PlayerActionType.CONTINUE_DESTROY_BLOCK) {
                                this.d((BlockVector3)object2, blockFace);
                            }
                            BlockVector3 blockVector32 = blockVector3 = this.V == null ? null : this.V.getPosition();
                            if (blockVector3 != null && (blockVector3.getX() != ((BlockVector3)object2).getX() || blockVector3.getY() != ((BlockVector3)object2).getY() || blockVector3.getZ() != ((BlockVector3)object2).getZ())) {
                                this.a(blockVector3, BlockFace.DOWN);
                                this.d((BlockVector3)object2, blockFace);
                            }
                            switch (playerBlockActionData.getAction()) {
                                case START_DESTROY_BLOCK: {
                                    this.d((BlockVector3)object2, blockFace);
                                    break;
                                }
                                case ABORT_DESTROY_BLOCK: 
                                case STOP_DESTROY_BLOCK: {
                                    this.a((BlockVector3)object2, blockFace);
                                    break;
                                }
                                case CONTINUE_DESTROY_BLOCK: {
                                    this.c((BlockVector3)object2, blockFace);
                                    break;
                                }
                                case PREDICT_DESTROY_BLOCK: {
                                    this.a((BlockVector3)object2, blockFace);
                                    this.b((BlockVector3)object2, blockFace);
                                    break;
                                }
                            }
                            this.V = playerBlockActionData;
                        }
                    }
                    if (this.teleportPosition != null) {
                        return;
                    }
                    if (this.riding instanceof EntityControllable && this.riding.isControlling(this)) {
                        boolean bl = playerAuthInputPacket.getInputData().contains((Object)AuthInputAction.JUMPING);
                        if (bl && this.t <= 0) {
                            this.t = this.server.getTick();
                        } else if (!bl && this.t > 0) {
                            ((EntityControllable)((Object)this.riding)).onJump(this, this.server.getTick() - this.t);
                            this.t = 0;
                        }
                        double d4 = playerAuthInputPacket.getMotion().getX();
                        double d5 = playerAuthInputPacket.getMotion().getY();
                        if (d4 >= -1.0 && d4 <= 1.0 && d5 >= -1.0 && d5 <= 1.0) {
                            ((EntityControllable)((Object)this.riding)).onPlayerInput(this, d4, d5);
                        }
                    }
                    if (playerAuthInputPacket.getInputData().contains((Object)AuthInputAction.START_SPRINTING)) {
                        PlayerToggleSprintEvent playerToggleSprintEvent = new PlayerToggleSprintEvent(this, true);
                        if (this.foodData.getLevel() <= 6 && !this.getAdventureSettings().get(AdventureSettings.Type.FLYING) || this.hasEffect(15)) {
                            playerToggleSprintEvent.setCancelled(true);
                        }
                        this.server.getPluginManager().callEvent(playerToggleSprintEvent);
                        if (playerToggleSprintEvent.isCancelled()) {
                            this.R = true;
                        } else {
                            this.setSprinting(true, false);
                        }
                        this.setUsingItem(false);
                    }
                    if (playerAuthInputPacket.getInputData().contains((Object)AuthInputAction.STOP_SPRINTING)) {
                        PlayerToggleSprintEvent playerToggleSprintEvent = new PlayerToggleSprintEvent(this, false);
                        this.server.getPluginManager().callEvent(playerToggleSprintEvent);
                        if (playerToggleSprintEvent.isCancelled()) {
                            this.R = true;
                        } else {
                            this.setSprinting(false, false);
                        }
                    }
                    if (playerAuthInputPacket.getInputData().contains((Object)AuthInputAction.START_SNEAKING)) {
                        PlayerToggleSneakEvent playerToggleSneakEvent = new PlayerToggleSneakEvent(this, true);
                        this.server.getPluginManager().callEvent(playerToggleSneakEvent);
                        if (playerToggleSneakEvent.isCancelled()) {
                            this.R = true;
                        } else {
                            this.setSneaking(true);
                        }
                    }
                    if (playerAuthInputPacket.getInputData().contains((Object)AuthInputAction.STOP_SNEAKING)) {
                        PlayerToggleSneakEvent playerToggleSneakEvent = new PlayerToggleSneakEvent(this, false);
                        this.server.getPluginManager().callEvent(playerToggleSneakEvent);
                        if (playerToggleSneakEvent.isCancelled()) {
                            this.R = true;
                        } else {
                            this.setSneaking(false);
                        }
                    }
                    if (playerAuthInputPacket.getInputData().contains((Object)AuthInputAction.START_JUMPING)) {
                        this.server.getPluginManager().callEvent(new PlayerJumpEvent(this));
                    }
                    if (playerAuthInputPacket.getInputData().contains((Object)AuthInputAction.START_GLIDING)) {
                        boolean bl = false;
                        Item item2 = this.getInventory().getChestplateFast();
                        if (item2 == null || item2.getId() != 444) {
                            bl = true;
                        }
                        if (bl && !this.server.getAllowFlight()) {
                            this.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server", true, "type=ACTION_START_GLIDE");
                            return;
                        }
                        object2 = new PlayerToggleGlideEvent(this, true);
                        if (bl) {
                            ((Event)object2).setCancelled(true);
                        }
                        this.server.getPluginManager().callEvent((Event)object2);
                        if (((Event)object2).isCancelled()) {
                            this.R = true;
                        } else {
                            this.setGliding(true);
                        }
                        this.setUsingItem(false);
                    }
                    if (playerAuthInputPacket.getInputData().contains((Object)AuthInputAction.STOP_GLIDING)) {
                        PlayerToggleGlideEvent playerToggleGlideEvent = new PlayerToggleGlideEvent(this, false);
                        this.server.getPluginManager().callEvent(playerToggleGlideEvent);
                        if (playerToggleGlideEvent.isCancelled()) {
                            this.R = true;
                        } else {
                            this.setGliding(false);
                        }
                    }
                    if (playerAuthInputPacket.getInputData().contains((Object)AuthInputAction.START_SWIMMING)) {
                        PlayerToggleSwimEvent playerToggleSwimEvent = new PlayerToggleSwimEvent(this, true);
                        if (!this.isInsideOfWater()) {
                            playerToggleSwimEvent.setCancelled(true);
                        }
                        this.server.getPluginManager().callEvent(playerToggleSwimEvent);
                        if (playerToggleSwimEvent.isCancelled()) {
                            this.R = true;
                        } else {
                            this.setSwimming(true);
                        }
                        this.setUsingItem(false);
                    }
                    if (playerAuthInputPacket.getInputData().contains((Object)AuthInputAction.STOP_SWIMMING)) {
                        PlayerToggleSwimEvent playerToggleSwimEvent = new PlayerToggleSwimEvent(this, false);
                        this.server.getPluginManager().callEvent(playerToggleSwimEvent);
                        if (playerToggleSwimEvent.isCancelled()) {
                            this.R = true;
                        } else {
                            this.setSwimming(false);
                        }
                    }
                    if ((d3 = (vector3 = playerAuthInputPacket.getPosition().asVector3().subtract(0.0, this.getBaseOffset(), 0.0)).distanceSquared(this)) > 100.0) {
                        this.sendPosition(this, playerAuthInputPacket.getYaw(), playerAuthInputPacket.getPitch(), 1);
                        return;
                    }
                    if (!(!this.server.suomiCraftPEMode() || this.getAdventureSettings().get(AdventureSettings.Type.NO_CLIP) || !(block = this.level.getBlock(this.chunk, vector3.getFloorX(), NukkitMath.floorDouble(vector3.getY() + 0.2), vector3.getFloorZ(), false)).isSolid() || block.isTransparent() && !Player.a(block) || block instanceof BlockFallable && !this.isInsideOfSolid())) {
                        this.sendPosition(this.onGround && vector3.y < this.y ? this.add(0.0, 0.1, 0.0) : this, playerAuthInputPacket.getYaw(), playerAuthInputPacket.getPitch(), 1);
                        return;
                    }
                    boolean bl = false;
                    if (!this.isAlive() || !this.spawned) {
                        bl = true;
                        this.forceMovement = this;
                    }
                    if (this.forceMovement != null && (bl || vector3.distanceSquared(this.forceMovement) > 0.1)) {
                        this.sendPosition(this.forceMovement, playerAuthInputPacket.getYaw(), playerAuthInputPacket.getPitch(), 1);
                        return;
                    }
                    float f2 = playerAuthInputPacket.getYaw() % 360.0f;
                    float f3 = playerAuthInputPacket.getPitch() % 360.0f;
                    if (f2 < 0.0f) {
                        f2 += 360.0f;
                    }
                    this.setRotation(f2, f3);
                    this.newPosition = vector3;
                    this.u.offer(vector3);
                    this.forceMovement = null;
                    return;
                }
                case 18: {
                    MoveEntityAbsolutePacket moveEntityAbsolutePacket = (MoveEntityAbsolutePacket)dataPacket;
                    if (!this.spawned) return;
                    if (this.riding == null) return;
                    if (this.riding.getId() != moveEntityAbsolutePacket.eid) return;
                    if (!this.riding.isControlling(this)) {
                        return;
                    }
                    if (!(this.riding instanceof EntityBoat)) return;
                    if (this.temporalVector.setComponents(moveEntityAbsolutePacket.x, moveEntityAbsolutePacket.y, moveEntityAbsolutePacket.z).distanceSquared(this.riding) < 9.0) {
                        ((EntityBoat)this.riding).onInput(moveEntityAbsolutePacket.x, moveEntityAbsolutePacket.y, moveEntityAbsolutePacket.z, moveEntityAbsolutePacket.headYaw);
                        return;
                    }
                    if (!this.server.suomiCraftPEMode()) return;
                    this.riding.dismountEntity(this);
                    return;
                }
                case 55: {
                    if (this.protocol >= 553) {
                        return;
                    }
                    AdventureSettingsPacket adventureSettingsPacket = (AdventureSettingsPacket)dataPacket;
                    if (adventureSettingsPacket.entityUniqueId != this.getId()) {
                        this.getServer().getLogger().debug(this.username + ": adventure settings eid mismatch");
                        return;
                    }
                    if (adventureSettingsPacket.getFlag(64) && !this.adventureSettings.get(AdventureSettings.Type.ALLOW_FLIGHT)) {
                        if (!this.server.getAllowFlight()) {
                            this.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server", true, "type=AdventureSettingsPacket, flag=ALLOW_FLIGHT");
                            return;
                        }
                        this.r = true;
                        return;
                    }
                    if (adventureSettingsPacket.getFlag(128) && !this.adventureSettings.get(AdventureSettings.Type.NO_CLIP)) {
                        if (!this.server.getAllowFlight()) {
                            this.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server", true, "type=AdventureSettingsPacket, flag=NO_CLIP");
                            return;
                        }
                        this.r = true;
                        return;
                    }
                    boolean bl = false;
                    if (adventureSettingsPacket.getFlag(512) && !this.adventureSettings.get(AdventureSettings.Type.ALLOW_FLIGHT)) {
                        if (!this.server.getAllowFlight()) {
                            this.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server", true, "type=AdventureSettingsPacket, flag=FLYING");
                            return;
                        }
                        bl = true;
                    }
                    PlayerToggleFlightEvent playerToggleFlightEvent = new PlayerToggleFlightEvent(this, adventureSettingsPacket.getFlag(512));
                    if (bl || this.isSpectator()) {
                        playerToggleFlightEvent.setCancelled(true);
                    }
                    this.server.getPluginManager().callEvent(playerToggleFlightEvent);
                    if (playerToggleFlightEvent.isCancelled()) {
                        this.r = true;
                        return;
                    }
                    this.adventureSettings.set(AdventureSettings.Type.FLYING, playerToggleFlightEvent.isFlying());
                    return;
                }
                case -72: {
                    boolean bl;
                    if (this.protocol < 553) {
                        return;
                    }
                    RequestAbilityPacket requestAbilityPacket = (RequestAbilityPacket)dataPacket;
                    PlayerAbility playerAbility = requestAbilityPacket.getAbility();
                    if (requestAbilityPacket.isBoolValue()) {
                        if (playerAbility == PlayerAbility.MAY_FLY && !this.getAdventureSettings().get(AdventureSettings.Type.ALLOW_FLIGHT)) {
                            if (!this.server.getAllowFlight()) {
                                this.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server", true, "type=RequestAbilityPacket, ability=MAY_FLY");
                                return;
                            }
                            this.r = true;
                            return;
                        }
                        if (playerAbility == PlayerAbility.NO_CLIP && !this.getAdventureSettings().get(AdventureSettings.Type.NO_CLIP)) {
                            if (!this.server.getAllowFlight()) {
                                this.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server", true, "type=RequestAbilityPacket, ability=NO_CLIP");
                                return;
                            }
                            this.r = true;
                            return;
                        }
                    }
                    if (playerAbility != PlayerAbility.FLYING) {
                        this.server.getLogger().debug(this.username + ": tried to trigger ability " + (Object)((Object)playerAbility) + (requestAbilityPacket.isBoolValue() ? " on" : " off"));
                        return;
                    }
                    boolean bl2 = bl = requestAbilityPacket.isBoolValue() && !this.getAdventureSettings().get(AdventureSettings.Type.ALLOW_FLIGHT);
                    if (bl && !this.server.getAllowFlight()) {
                        this.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server", true, "type=RequestAbilityPacket, ability=FLYING");
                        return;
                    }
                    PlayerToggleFlightEvent playerToggleFlightEvent = new PlayerToggleFlightEvent(this, requestAbilityPacket.isBoolValue());
                    if (bl || this.isSpectator()) {
                        playerToggleFlightEvent.setCancelled(true);
                    }
                    this.server.getPluginManager().callEvent(playerToggleFlightEvent);
                    if (playerToggleFlightEvent.isCancelled()) {
                        this.r = true;
                        return;
                    }
                    this.adventureSettings.set(AdventureSettings.Type.FLYING, playerToggleFlightEvent.isFlying());
                    return;
                }
                case 31: {
                    if (!this.spawned) return;
                    if (!this.isAlive()) {
                        return;
                    }
                    if (this.a(dataPacket, 40, -1)) {
                        return;
                    }
                    MobEquipmentPacket mobEquipmentPacket = (MobEquipmentPacket)dataPacket;
                    Inventory inventory = this.getWindowById(mobEquipmentPacket.windowId);
                    if (inventory == null) {
                        this.server.getLogger().debug("Player " + this.getName() + " has no open container with window ID " + mobEquipmentPacket.windowId);
                        return;
                    }
                    if (inventory instanceof PlayerInventory) {
                        ((PlayerInventory)inventory).equipItem(mobEquipmentPacket.hotbarSlot);
                    }
                    this.setUsingItem(false);
                    return;
                }
                case 36: {
                    PlayerActionPacket playerActionPacket = (PlayerActionPacket)dataPacket;
                    if (!this.spawned) return;
                    if (!this.isAlive() && playerActionPacket.action != 7) {
                        return;
                    }
                    playerActionPacket.entityId = this.id;
                    switch (playerActionPacket.action) {
                        case 0: {
                            if (this.isMovementServerAuthoritative()) break;
                            BlockVector3 blockVector3 = new BlockVector3(playerActionPacket.x, playerActionPacket.y, playerActionPacket.z);
                            this.d(blockVector3, BlockFace.fromIndex(playerActionPacket.face));
                            break;
                        }
                        case 1: 
                        case 2: {
                            if (this.isMovementServerAuthoritative()) return;
                            this.a(null, null);
                            return;
                        }
                        case 6: {
                            this.stopSleep();
                            return;
                        }
                        case 7: {
                            if (!this.spawned) return;
                            if (this.isAlive()) return;
                            if (!this.isOnline()) {
                                return;
                            }
                            this.respawn();
                            break;
                        }
                        case 8: {
                            if (this.isMovementServerAuthoritative()) return;
                            this.server.getPluginManager().callEvent(new PlayerJumpEvent(this));
                            return;
                        }
                        case 9: {
                            if (this.isMovementServerAuthoritative()) break;
                            PlayerToggleSprintEvent playerToggleSprintEvent = new PlayerToggleSprintEvent(this, true);
                            if (this.foodData.getLevel() <= 6 && !this.getAdventureSettings().get(AdventureSettings.Type.FLYING) || this.hasEffect(15)) {
                                playerToggleSprintEvent.setCancelled(true);
                            }
                            this.server.getPluginManager().callEvent(playerToggleSprintEvent);
                            if (playerToggleSprintEvent.isCancelled()) {
                                this.R = true;
                                break;
                            }
                            this.setSprinting(true, false);
                            break;
                        }
                        case 10: {
                            if (this.isMovementServerAuthoritative()) return;
                            PlayerToggleSprintEvent playerToggleSprintEvent = new PlayerToggleSprintEvent(this, false);
                            this.server.getPluginManager().callEvent(playerToggleSprintEvent);
                            if (playerToggleSprintEvent.isCancelled()) {
                                this.R = true;
                                return;
                            }
                            this.setSprinting(false, false);
                            return;
                        }
                        case 11: {
                            if (this.isMovementServerAuthoritative()) return;
                            PlayerToggleSneakEvent playerToggleSneakEvent = new PlayerToggleSneakEvent(this, true);
                            this.server.getPluginManager().callEvent(playerToggleSneakEvent);
                            if (playerToggleSneakEvent.isCancelled()) {
                                this.R = true;
                                return;
                            }
                            this.setSneaking(true);
                            return;
                        }
                        case 12: {
                            if (this.isMovementServerAuthoritative()) return;
                            PlayerToggleSneakEvent playerToggleSneakEvent = new PlayerToggleSneakEvent(this, false);
                            this.server.getPluginManager().callEvent(playerToggleSneakEvent);
                            if (playerToggleSneakEvent.isCancelled()) {
                                this.R = true;
                                return;
                            }
                            this.setSneaking(false);
                            return;
                        }
                        case 14: {
                            if (this.s) {
                                this.sendPosition(this, this.yaw, this.pitch, 1);
                                this.dummyBossBars.values().forEach(DummyBossBar::reshow);
                                this.s = false;
                                return;
                            }
                            this.getServer().getLogger().debug(this.username + ": got a dimension change ack but no dimension change is in progress");
                            return;
                        }
                        case 15: {
                            if (this.isMovementServerAuthoritative()) break;
                            boolean bl = false;
                            Item item3 = this.getInventory().getChestplateFast();
                            if (item3 == null || item3.getId() != 444) {
                                bl = true;
                            }
                            if (bl && !this.server.getAllowFlight()) {
                                this.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server", true, "type=ACTION_START_GLIDE");
                                return;
                            }
                            PlayerToggleGlideEvent playerToggleGlideEvent = new PlayerToggleGlideEvent(this, true);
                            if (bl) {
                                playerToggleGlideEvent.setCancelled(true);
                            }
                            this.server.getPluginManager().callEvent(playerToggleGlideEvent);
                            if (playerToggleGlideEvent.isCancelled()) {
                                this.R = true;
                                break;
                            }
                            this.setGliding(true);
                            break;
                        }
                        case 16: {
                            if (this.isMovementServerAuthoritative()) return;
                            PlayerToggleGlideEvent playerToggleGlideEvent = new PlayerToggleGlideEvent(this, false);
                            this.server.getPluginManager().callEvent(playerToggleGlideEvent);
                            if (playerToggleGlideEvent.isCancelled()) {
                                this.R = true;
                                return;
                            }
                            this.setGliding(false);
                            return;
                        }
                        case 18: {
                            if (this.isMovementServerAuthoritative()) return;
                            this.c(null, BlockFace.fromIndex(playerActionPacket.face));
                            return;
                        }
                        case 21: {
                            if (this.isMovementServerAuthoritative()) break;
                            PlayerToggleSwimEvent playerToggleSwimEvent = new PlayerToggleSwimEvent(this, true);
                            if (!this.isInsideOfWater()) {
                                playerToggleSwimEvent.setCancelled(true);
                            }
                            this.server.getPluginManager().callEvent(playerToggleSwimEvent);
                            if (playerToggleSwimEvent.isCancelled()) {
                                this.R = true;
                                break;
                            }
                            this.setSwimming(true);
                            break;
                        }
                        case 22: {
                            if (this.isMovementServerAuthoritative()) return;
                            PlayerToggleSwimEvent playerToggleSwimEvent = new PlayerToggleSwimEvent(this, false);
                            this.server.getPluginManager().callEvent(playerToggleSwimEvent);
                            if (playerToggleSwimEvent.isCancelled()) {
                                this.R = true;
                                return;
                            }
                            this.setSwimming(false);
                            return;
                        }
                        case 23: {
                            if (this.inventory == null) {
                                this.getServer().getLogger().debug(this.username + ": got ACTION_START_SPIN_ATTACK but inventory was null");
                                break;
                            }
                            PlayerToggleSpinAttackEvent playerToggleSpinAttackEvent = new PlayerToggleSpinAttackEvent(this, true);
                            int n5 = 0;
                            Item item4 = this.inventory.getItemInHandFast();
                            if (item4.getId() != 455) {
                                playerToggleSpinAttackEvent.setCancelled(true);
                            } else {
                                Enchantment enchantment = item4.getEnchantment(30);
                                if (enchantment == null) {
                                    playerToggleSpinAttackEvent.setCancelled(true);
                                } else {
                                    n5 = enchantment.getLevel();
                                    if (n5 < 1) {
                                        playerToggleSpinAttackEvent.setCancelled(true);
                                    } else if (!(this.isInsideOfWater() || this.getLevel().isRaining() && this.canSeeSky())) {
                                        playerToggleSpinAttackEvent.setCancelled(true);
                                    }
                                }
                            }
                            this.server.getPluginManager().callEvent(playerToggleSpinAttackEvent);
                            if (playerToggleSpinAttackEvent.isCancelled()) {
                                this.R = true;
                                this.sendPosition(this, this.yaw, this.pitch, 1);
                                break;
                            }
                            this.setSpinAttack(true);
                            this.resetFallDistance();
                            this.riptideTicks = 100;
                            int n6 = n5 >= 3 ? 182 : (n5 == 2 ? 181 : 180);
                            this.level.addLevelSoundEvent(this, n6);
                            break;
                        }
                        case 24: {
                            PlayerToggleSpinAttackEvent playerToggleSpinAttackEvent = new PlayerToggleSpinAttackEvent(this, false);
                            this.server.getPluginManager().callEvent(playerToggleSpinAttackEvent);
                            if (playerToggleSpinAttackEvent.isCancelled()) {
                                this.R = true;
                                return;
                            }
                            this.setSpinAttack(false);
                            return;
                        }
                    }
                    this.setUsingItem(false);
                    return;
                }
                case 101: {
                    FormWindow formWindow2;
                    this.Q = false;
                    if (!this.spawned) return;
                    if (!this.isAlive()) {
                        return;
                    }
                    ModalFormResponsePacket modalFormResponsePacket = (ModalFormResponsePacket)dataPacket;
                    if (this.formWindows.containsKey(modalFormResponsePacket.formId)) {
                        formWindow2 = this.formWindows.remove(modalFormResponsePacket.formId);
                        formWindow2.setResponse(modalFormResponsePacket.data.trim());
                        Object object3 = formWindow2.getHandlers().iterator();
                        while (true) {
                            if (!object3.hasNext()) {
                                object3 = new PlayerFormRespondedEvent(this, modalFormResponsePacket.formId, formWindow2);
                                this.getServer().getPluginManager().callEvent((Event)object3);
                                return;
                            }
                            FormResponseHandler formResponseHandler = object3.next();
                            formResponseHandler.handle(this, modalFormResponsePacket.formId);
                        }
                    }
                    if (!this.serverSettings.containsKey(modalFormResponsePacket.formId)) return;
                    formWindow2 = this.serverSettings.get(modalFormResponsePacket.formId);
                    formWindow2.setResponse(modalFormResponsePacket.data.trim());
                    Object object4 = formWindow2.getHandlers().iterator();
                    while (true) {
                        if (!object4.hasNext()) {
                            object4 = new PlayerSettingsRespondedEvent(this, modalFormResponsePacket.formId, formWindow2);
                            this.getServer().getPluginManager().callEvent((Event)object4);
                            if (((Event)object4).isCancelled()) return;
                            if (!(formWindow2 instanceof FormWindowCustom)) return;
                            ((FormWindowCustom)formWindow2).setElementsFromResponse();
                            return;
                        }
                        FormResponseHandler formResponseHandler = object4.next();
                        formResponseHandler.handle(this, modalFormResponsePacket.formId);
                    }
                }
                case 33: {
                    Player player;
                    if (!this.spawned) return;
                    if (!this.isAlive()) {
                        return;
                    }
                    InteractPacket interactPacket = (InteractPacket)dataPacket;
                    if (interactPacket.target == 0L && interactPacket.action == 4) {
                        this.setButtonText("");
                        return;
                    }
                    Entity entity = player = interactPacket.target == this.getId() ? this : this.level.getEntity(interactPacket.target);
                    if (player == null || !this.isAlive() || !player.isAlive()) {
                        if (player != null) return;
                        if (interactPacket.action != 6) {
                            return;
                        }
                    }
                    if (player instanceof EntityItem || player instanceof EntityArrow || player instanceof EntityXPOrb) {
                        this.kick(PlayerKickEvent.Reason.INVALID_PVE, "Attempting to interact with an invalid entity", true, "targetEntity=" + player.getClass().getSimpleName());
                        return;
                    }
                    switch (interactPacket.action) {
                        case 6: {
                            if (this.protocol < 407) {
                                this.awardAchievement("openInventory");
                                return;
                            }
                            if (this.G) return;
                            if (this.riding instanceof EntityChestBoat && this.riding == player) {
                                this.addWindow(((EntityChestBoat)((Object)player)).getInventory());
                                return;
                            }
                            if (!this.inventory.open(this)) return;
                            this.G = true;
                            this.awardAchievement("openInventory");
                            return;
                        }
                        case 4: {
                            String string2 = "";
                            if (player instanceof EntityInteractable && (string2 = ((EntityInteractable)((Object)player)).getInteractButtonText(this)) == null) {
                                string2 = "";
                            }
                            this.setButtonText(string2);
                            this.getServer().getPluginManager().callEvent(new PlayerMouseOverEntityEvent(this, player));
                            return;
                        }
                        case 3: {
                            if (!(player instanceof EntityRideable)) return;
                            if (this.riding != player) {
                                return;
                            }
                            this.t = 0;
                            ((EntityRideable)((Object)this.riding)).dismountEntity(this);
                            return;
                        }
                    }
                    return;
                }
                case 34: {
                    int n7;
                    CompoundTag compoundTag;
                    Object object5;
                    if (!this.spawned) return;
                    if (!this.isAlive()) {
                        return;
                    }
                    if (this.a(dataPacket, 20, -1)) {
                        return;
                    }
                    if (this.inventory == null) {
                        this.getServer().getLogger().debug(this.username + ": got block pick request but inventory was null");
                        return;
                    }
                    BlockPickRequestPacket blockPickRequestPacket = (BlockPickRequestPacket)dataPacket;
                    Block block = this.level.getBlock(this.chunk, blockPickRequestPacket.x, blockPickRequestPacket.y, blockPickRequestPacket.z, false);
                    if (block.distanceSquared(this) > 1000.0) {
                        this.getServer().getLogger().debug(this.username + ": block pick request for a block too far away");
                        return;
                    }
                    Item item5 = block.toItem();
                    if (blockPickRequestPacket.addUserData && (object5 = this.getLevel().getBlockEntityIfLoaded(this.chunk, this.temporalVector.setComponents(blockPickRequestPacket.x, blockPickRequestPacket.y, blockPickRequestPacket.z))) != null && (compoundTag = ((BlockEntity)object5).getCleanedNBT()) != null) {
                        item5.setCustomBlockData(compoundTag);
                        item5.setLore("+(DATA)");
                    }
                    object5 = new PlayerBlockPickEvent(this, block, item5);
                    if (this.isSpectator()) {
                        ((Event)object5).setCancelled();
                    }
                    this.server.getPluginManager().callEvent((Event)object5);
                    if (((Event)object5).isCancelled()) return;
                    boolean bl = false;
                    int n8 = -1;
                    for (n7 = 0; n7 < this.inventory.getSize(); ++n7) {
                        if (!this.inventory.getItem(n7).equals(((PlayerBlockPickEvent)object5).getItem())) continue;
                        if (n7 < this.inventory.getHotbarSize()) {
                            this.inventory.setHeldItemSlot(n7);
                        } else {
                            n8 = n7;
                        }
                        bl = true;
                        break;
                    }
                    for (n7 = 0; n7 < this.inventory.getHotbarSize(); ++n7) {
                        if (!this.inventory.getItem(n7).isNull()) continue;
                        if (!bl && this.isCreative()) {
                            this.inventory.setHeldItemSlot(n7);
                            this.inventory.setItemInHand(((PlayerBlockPickEvent)object5).getItem());
                            return;
                        }
                        if (n8 <= -1) continue;
                        this.inventory.setHeldItemSlot(n7);
                        this.inventory.setItemInHand(this.inventory.getItem(n8));
                        this.inventory.clear(n8, true);
                        return;
                    }
                    if (!bl && this.isCreative()) {
                        Item item6 = this.inventory.getItemInHand();
                        this.inventory.setItemInHand(((PlayerBlockPickEvent)object5).getItem());
                        if (this.inventory.isFull()) return;
                        int n9 = 0;
                        while (n9 < this.inventory.getSize()) {
                            if (this.inventory.getItem(n9).isNull()) {
                                this.inventory.setItem(n9, item6);
                                return;
                            }
                            ++n9;
                        }
                        return;
                    }
                    if (n8 <= -1) return;
                    Item item7 = this.inventory.getItemInHand();
                    this.inventory.setItemInHand(this.inventory.getItem(n8));
                    this.inventory.setItem(n8, item7);
                    return;
                }
                case 44: {
                    if (!this.spawned) return;
                    if (!this.isAlive()) {
                        return;
                    }
                    if (this.a(dataPacket, 40, -1)) {
                        return;
                    }
                    AnimatePacket animatePacket = (AnimatePacket)dataPacket;
                    PlayerAnimationEvent playerAnimationEvent = new PlayerAnimationEvent(this, animatePacket.action);
                    if (animatePacket.action == null) return;
                    if (animatePacket.action == AnimatePacket.Action.WAKE_UP) return;
                    if (animatePacket.action == AnimatePacket.Action.CRITICAL_HIT) return;
                    if (animatePacket.action == AnimatePacket.Action.MAGIC_CRITICAL_HIT) {
                        return;
                    }
                    this.server.getPluginManager().callEvent(playerAnimationEvent);
                    if (playerAnimationEvent.isCancelled()) {
                        return;
                    }
                    AnimatePacket.Action action = playerAnimationEvent.getAnimationType();
                    switch (action) {
                        case ROW_RIGHT: 
                        case ROW_LEFT: {
                            if (!(this.riding instanceof EntityBoat)) break;
                            ((EntityBoat)this.riding).onPaddle(action, animatePacket.rowingTime);
                            break;
                        }
                    }
                    animatePacket = new AnimatePacket();
                    animatePacket.eid = this.getId();
                    animatePacket.action = playerAnimationEvent.getAnimationType();
                    Server.broadcastPacket(this.getViewers().values(), (DataPacket)animatePacket);
                    return;
                }
                case 27: {
                    if (!this.spawned) return;
                    if (!this.isAlive()) {
                        return;
                    }
                    EntityEventPacket entityEventPacket = (EntityEventPacket)dataPacket;
                    if (entityEventPacket.event != 34) {
                        this.craftingType = 0;
                    }
                    switch (entityEventPacket.event) {
                        case 57: {
                            if (entityEventPacket.data == 0 || entityEventPacket.eid != this.id) {
                                this.getServer().getLogger().debug(this.username + ": entity event eid mismatch");
                                return;
                            }
                            if (this.a(dataPacket, 40, -1)) {
                                return;
                            }
                            entityEventPacket.eid = this.id;
                            entityEventPacket.isEncoded = false;
                            this.dataPacket(entityEventPacket);
                            Server.broadcastPacket(this.getViewers().values(), (DataPacket)entityEventPacket);
                            return;
                        }
                        case 34: {
                            if (entityEventPacket.eid != this.id) {
                                this.getServer().getLogger().debug(this.username + ": entity event eid mismatch");
                                return;
                            }
                            if (this.protocol >= 407) {
                                Inventory inventory = this.getWindowById(2);
                                if (!(inventory instanceof AnvilInventory)) return;
                                ((AnvilInventory)inventory).setCost(-entityEventPacket.data);
                                return;
                            }
                            int n10 = entityEventPacket.data;
                            if (n10 >= 0) return;
                            this.setExperience(this.aa, this.S + n10);
                            return;
                        }
                    }
                    return;
                }
                case 77: {
                    if (!this.spawned) return;
                    if (!this.isAlive()) {
                        return;
                    }
                    if (this.a(dataPacket, 20, -1)) {
                        return;
                    }
                    this.resetCraftingGridType();
                    CommandRequestPacket commandRequestPacket = (CommandRequestPacket)dataPacket;
                    PlayerCommandPreprocessEvent playerCommandPreprocessEvent = new PlayerCommandPreprocessEvent(this, commandRequestPacket.command + ' ');
                    this.server.getPluginManager().callEvent(playerCommandPreprocessEvent);
                    if (playerCommandPreprocessEvent.isCancelled()) {
                        return;
                    }
                    if (Timings.playerCommandTimer != null) {
                        Timings.playerCommandTimer.startTiming();
                    }
                    this.server.dispatchCommand(playerCommandPreprocessEvent.getPlayer(), playerCommandPreprocessEvent.getMessage().substring(1));
                    if (Timings.playerCommandTimer == null) return;
                    Timings.playerCommandTimer.stopTiming();
                    return;
                }
                case 9: {
                    if (!this.spawned) return;
                    if (!this.isAlive()) {
                        return;
                    }
                    if (this.a(dataPacket, 20, -1)) {
                        return;
                    }
                    TextPacket textPacket = (TextPacket)dataPacket;
                    if (textPacket.type != 1) return;
                    String string3 = textPacket.message;
                    int n11 = string3.indexOf(10);
                    if (n11 != -1) {
                        string3 = string3.substring(0, n11);
                    }
                    this.chat(string3);
                    return;
                }
                case 47: {
                    ContainerClosePacket containerClosePacket = (ContainerClosePacket)dataPacket;
                    if (!this.spawned) return;
                    if (containerClosePacket.windowId == 0 && !this.G && this.protocol >= 407) {
                        return;
                    }
                    if (this.a(dataPacket, 40, -1)) {
                        return;
                    }
                    if (this.windowIndex.containsKey(containerClosePacket.windowId)) {
                        this.server.getPluginManager().callEvent(new InventoryCloseEvent((Inventory)this.windowIndex.get(containerClosePacket.windowId), this));
                        if (containerClosePacket.windowId == 0) {
                            this.G = false;
                        }
                        this.closingWindowId = containerClosePacket.windowId;
                        this.removeWindow((Inventory)this.windowIndex.get(containerClosePacket.windowId), true);
                        this.closingWindowId = Integer.MIN_VALUE;
                    }
                    if (containerClosePacket.windowId != -1) return;
                    this.resetCraftingGridType();
                    this.addWindow(this.craftingGrid, -1);
                    if (this.protocol < 407) return;
                    ContainerClosePacket containerClosePacket2 = new ContainerClosePacket();
                    containerClosePacket2.windowId = -1;
                    containerClosePacket2.wasServerInitiated = false;
                    this.dataPacket(containerClosePacket2);
                    return;
                }
                case 56: {
                    CompoundTag compoundTag;
                    if (!this.spawned) return;
                    if (!this.isAlive()) {
                        return;
                    }
                    BlockEntityDataPacket blockEntityDataPacket = (BlockEntityDataPacket)dataPacket;
                    this.resetCraftingGridType();
                    Vector3 vector3 = this.temporalVector.setComponents(blockEntityDataPacket.x, blockEntityDataPacket.y, blockEntityDataPacket.z);
                    if (vector3.distanceSquared(this) > 10000.0) {
                        return;
                    }
                    BlockEntity blockEntity = this.level.getBlockEntity(vector3);
                    if (!(blockEntity instanceof BlockEntitySpawnable)) return;
                    try {
                        compoundTag = NBTIO.read(blockEntityDataPacket.namedTag, ByteOrder.LITTLE_ENDIAN, true);
                    }
                    catch (IOException iOException) {
                        throw new RuntimeException(iOException);
                    }
                    if (((BlockEntitySpawnable)blockEntity).updateCompoundTag(compoundTag, this)) return;
                    ((BlockEntitySpawnable)blockEntity).spawnTo(this);
                    return;
                }
                case 69: {
                    if (this.a(dataPacket, 20, -1)) {
                        return;
                    }
                    RequestChunkRadiusPacket requestChunkRadiusPacket = (RequestChunkRadiusPacket)dataPacket;
                    ChunkRadiusUpdatedPacket chunkRadiusUpdatedPacket = new ChunkRadiusUpdatedPacket();
                    chunkRadiusUpdatedPacket.radius = this.chunkRadius = Math.max(3, Math.min(requestChunkRadiusPacket.radius, this.viewDistance));
                    this.dataPacket(chunkRadiusUpdatedPacket);
                    return;
                }
                case 62: {
                    if (!this.spawned) {
                        return;
                    }
                    if (this.a(dataPacket, 20, 100)) {
                        return;
                    }
                    SetPlayerGameTypePacket setPlayerGameTypePacket = (SetPlayerGameTypePacket)dataPacket;
                    if (setPlayerGameTypePacket.gamemode == this.gamemode) return;
                    if (!this.hasPermission("nukkit.command.gamemode")) {
                        this.kick(PlayerKickEvent.Reason.INVALID_PACKET, "Invalid SetPlayerGameTypePacket", true, "type=SetPlayerGameTypePacket, reason=NO_PERMISSION");
                        return;
                    }
                    this.setGamemode(setPlayerGameTypePacket.gamemode, true);
                    Command.broadcastCommandMessage((CommandSender)this, new TranslationContainer("commands.gamemode.success.self", Server.getGamemodeString(this.gamemode)));
                    return;
                }
                case 71: {
                    if (!this.spawned) {
                        return;
                    }
                    if (this.a(dataPacket, 20, -1)) {
                        return;
                    }
                    ItemFrameDropItemPacket itemFrameDropItemPacket = (ItemFrameDropItemPacket)dataPacket;
                    Vector3 vector3 = this.temporalVector.setComponents(itemFrameDropItemPacket.x, itemFrameDropItemPacket.y, itemFrameDropItemPacket.z);
                    if (!(vector3.distanceSquared(this) < 1000.0)) return;
                    BlockEntity blockEntity = this.level.getBlockEntityIfLoaded(this.chunk, vector3);
                    if (!(blockEntity instanceof BlockEntityItemFrame)) return;
                    ((BlockEntityItemFrame)blockEntity).dropItem(this);
                    return;
                }
                case 68: {
                    if (this.inventory == null) {
                        this.getServer().getLogger().debug(this.username + ": got map info request but inventory was null");
                        return;
                    }
                    MapInfoRequestPacket mapInfoRequestPacket = (MapInfoRequestPacket)dataPacket;
                    int n12 = this.ab.getOrDefault(mapInfoRequestPacket.mapId, -20);
                    this.ab.put(mapInfoRequestPacket.mapId, this.server.getTick());
                    if (this.server.getTick() - n12 < 20) {
                        this.getServer().getLogger().debug(this.username + ": got next map info request too soon for map " + mapInfoRequestPacket.mapId);
                        return;
                    }
                    item = null;
                    for (Item item8 : this.offhandInventory.getContents().values()) {
                        if (!(item8 instanceof ItemMap) || ((ItemMap)item8).getMapId() != mapInfoRequestPacket.mapId) continue;
                        item = item8;
                    }
                    if (item == null) {
                        for (Item item9 : this.inventory.getContents().values()) {
                            if (!(item9 instanceof ItemMap) || ((ItemMap)item9).getMapId() != mapInfoRequestPacket.mapId) continue;
                            item = item9;
                        }
                    }
                    if (item == null) {
                        BlockEntityItemFrame blockEntityItemFrame;
                        BlockEntity blockEntity;
                        object = this.level.getBlockEntities().values().iterator();
                        do {
                            if (!object.hasNext()) return;
                        } while (!((blockEntity = (BlockEntity)object.next()) instanceof BlockEntityItemFrame) || !((blockEntityItemFrame = (BlockEntityItemFrame)blockEntity).getItem() instanceof ItemMap) || ((ItemMap)blockEntityItemFrame.getItem()).getMapId() != mapInfoRequestPacket.mapId);
                        ((ItemMap)blockEntityItemFrame.getItem()).sendImage(this);
                        return;
                    }
                }
                object = new PlayerMapInfoRequestEvent(this, item);
                this.getServer().getPluginManager().callEvent((Event)object);
                if (((Event)object).isCancelled()) return;
                ItemMap itemMap = (ItemMap)item;
                if (itemMap.trySendImage(this)) {
                    return;
                }
                try {
                    BufferedImage bufferedImage = new BufferedImage(128, 128, 1);
                    Graphics2D graphics2D = bufferedImage.createGraphics();
                    int n13 = this.getFloorX() / 128 << 7;
                    int n14 = this.getFloorZ() / 128 << 7;
                    int n15 = 0;
                    while (true) {
                        if (n15 >= 128) {
                            itemMap.setImage(bufferedImage);
                            itemMap.sendImage(this);
                            return;
                        }
                        int n16 = 0;
                        for (int k = -1; k < 128; ++k) {
                            if (k == -1) {
                                n16 = this.getLevel().getHighestBlockAt(n13 + n15, n14, false);
                                continue;
                            }
                            int n17 = this.getLevel().getHighestBlockAt(n13 + n15, n14 + k, false);
                            double d6 = (double)((n17 - n16) * 4 / 5) + ((double)(n15 + k & 1) - 0.5) * 0.4;
                            int n18 = 1;
                            if (d6 > 0.6) {
                                n18 = 2;
                            }
                            if (d6 < -0.6) {
                                n18 = 0;
                            }
                            n16 = n17;
                            graphics2D.setColor(Player.a(this.getLevel().getMapColorAt(n13 + n15, n17, n14 + k), n18));
                            graphics2D.fillRect(n15, k, n15 + 1, k + 1);
                        }
                        ++n15;
                    }
                }
                catch (Exception exception) {
                    this.getServer().getLogger().debug(this.username + ": there was an error while generating map image", exception);
                    return;
                }
                case 24: 
                case 120: 
                case 123: {
                    if (!this.spawned) return;
                    if (this.isSpectator()) {
                        return;
                    }
                    if (this.a(dataPacket, 40, 400)) {
                        return;
                    }
                    LevelSoundEventPacket levelSoundEventPacket = (LevelSoundEventPacket)dataPacket;
                    if (!W.contains(levelSoundEventPacket.sound)) {
                        this.getServer().getLogger().debug(this.username + ": prohibited level sound event: " + ((LevelSoundEventPacket)dataPacket).sound);
                        return;
                    }
                    if (this.distanceSquared(this.temporalVector.setComponents(levelSoundEventPacket.x, levelSoundEventPacket.y, levelSoundEventPacket.z)) > 10000.0) {
                        this.getServer().getLogger().debug(this.username + ": level sound event too far away");
                        return;
                    }
                    this.level.addChunkPacket(this.getChunkX(), this.getChunkZ(), dataPacket);
                    return;
                }
                case 30: {
                    Object object6;
                    if (this.isSpectator()) {
                        this.sendAllInventories();
                        return;
                    }
                    InventoryTransactionPacket inventoryTransactionPacket = (InventoryTransactionPacket)dataPacket;
                    ArrayList<InventoryAction> arrayList = new ArrayList<InventoryAction>();
                    for (Object object7 : inventoryTransactionPacket.actions) {
                        InventoryAction inventoryAction = object7.createInventoryAction(this);
                        if (inventoryAction == null) {
                            this.getServer().getLogger().debug("Unmatched inventory action from " + this.username + ": " + object7);
                            this.getCursorInventory().sendContents(this);
                            this.sendAllInventories();
                            if (this.af++ <= 10) return;
                            this.close("", "Too many failed inventory transactions");
                            return;
                        }
                        arrayList.add(inventoryAction);
                    }
                    if (inventoryTransactionPacket.isCraftingPart) {
                        if (LoomTransaction.checkForItemPart(arrayList)) {
                            if (this.loomTransaction == null) {
                                this.loomTransaction = new LoomTransaction(this, arrayList);
                            } else {
                                for (InventoryAction inventoryAction : arrayList) {
                                    this.loomTransaction.addAction(inventoryAction);
                                }
                            }
                            if (this.loomTransaction.canExecute() && this.loomTransaction.execute()) {
                                this.level.addLevelSoundEvent(this, 273);
                            }
                            this.loomTransaction = null;
                            return;
                        }
                        if (this.craftingTransaction == null) {
                            this.craftingTransaction = new CraftingTransaction(this, arrayList);
                        } else {
                            for (InventoryAction inventoryAction : arrayList) {
                                this.craftingTransaction.addAction(inventoryAction);
                            }
                        }
                        if (this.craftingTransaction.getPrimaryOutput() == null) return;
                        if (!this.craftingTransaction.canExecute()) return;
                        try {
                            this.craftingTransaction.execute();
                        }
                        catch (Exception exception) {
                            this.server.getLogger().debug(this.username + ": executing crafting transaction failed");
                        }
                        this.craftingTransaction = null;
                        return;
                    }
                    if (this.protocol >= 407 && inventoryTransactionPacket.isEnchantingPart) {
                        if (this.enchantTransaction == null) {
                            this.enchantTransaction = new EnchantTransaction(this, arrayList);
                        } else {
                            for (InventoryAction inventoryAction : arrayList) {
                                this.enchantTransaction.addAction(inventoryAction);
                            }
                        }
                        if (!this.enchantTransaction.canExecute()) return;
                        this.enchantTransaction.execute();
                        this.enchantTransaction = null;
                        return;
                    }
                    if (this.protocol >= 407 && inventoryTransactionPacket.isRepairItemPart) {
                        if (this.repairItemTransaction == null) {
                            this.repairItemTransaction = new RepairItemTransaction(this, arrayList);
                        } else {
                            for (InventoryAction inventoryAction : arrayList) {
                                this.repairItemTransaction.addAction(inventoryAction);
                            }
                        }
                        if (!this.repairItemTransaction.canExecute()) return;
                        this.repairItemTransaction.execute();
                        this.repairItemTransaction = null;
                        return;
                    }
                    if (this.craftingTransaction != null) {
                        if (this.craftingTransaction.checkForCraftingPart(arrayList)) {
                            if (this.craftingType == 1004) {
                                this.craftingTransaction = null;
                                return;
                            }
                            object6 = arrayList.iterator();
                            while (object6.hasNext()) {
                                InventoryAction inventoryAction = (InventoryAction)object6.next();
                                this.craftingTransaction.addAction(inventoryAction);
                            }
                            return;
                        }
                        this.server.getLogger().debug("Got unexpected normal inventory action with incomplete crafting transaction from " + this.username + ", refusing to execute crafting");
                        if (this.protocol >= 407) {
                            this.removeAllWindows(false);
                            this.getCursorInventory().sendContents(this);
                            this.sendAllInventories();
                            if (this.af++ > 10) {
                                this.close("", "Too many failed inventory transactions");
                            }
                        }
                        this.craftingTransaction = null;
                    } else if (this.protocol >= 407 && this.enchantTransaction != null) {
                        if (this.enchantTransaction.checkForEnchantPart(arrayList)) {
                            object6 = arrayList.iterator();
                            while (object6.hasNext()) {
                                InventoryAction inventoryAction = (InventoryAction)object6.next();
                                this.enchantTransaction.addAction(inventoryAction);
                            }
                            return;
                        }
                        this.server.getLogger().debug("Got unexpected normal inventory action with incomplete enchanting transaction from " + this.username + ", refusing to execute enchant " + inventoryTransactionPacket.toString());
                        this.removeAllWindows(false);
                        this.getCursorInventory().sendContents(this);
                        this.sendAllInventories();
                        this.enchantTransaction = null;
                        if (this.af++ > 10) {
                            this.close("", "Too many failed inventory transactions");
                        }
                    } else if (this.protocol >= 407 && this.repairItemTransaction != null) {
                        if (RepairItemTransaction.checkForRepairItemPart(arrayList)) {
                            object6 = arrayList.iterator();
                            while (object6.hasNext()) {
                                InventoryAction inventoryAction = (InventoryAction)object6.next();
                                this.repairItemTransaction.addAction(inventoryAction);
                            }
                            return;
                        }
                        this.server.getLogger().debug("Got unexpected normal inventory action with incomplete repair item transaction from " + this.username + ", refusing to execute repair item " + inventoryTransactionPacket.toString());
                        this.removeAllWindows(false);
                        this.getCursorInventory().sendContents(this);
                        this.sendAllInventories();
                        this.repairItemTransaction = null;
                        if (this.af++ > 10) {
                            this.close("", "Too many failed inventory transactions");
                        }
                    }
                    switch (inventoryTransactionPacket.transactionType) {
                        case 0: {
                            object6 = new InventoryTransaction(this, arrayList);
                            if (((InventoryTransaction)object6).execute()) return;
                            this.server.getLogger().debug("Failed to execute inventory transaction from " + this.username + " with actions: " + Arrays.toString(inventoryTransactionPacket.actions));
                            if (this.af++ <= 10) return;
                            this.close("", "Too many failed inventory transactions");
                            return;
                        }
                        case 1: {
                            if (inventoryTransactionPacket.actions.length > 0) {
                                this.server.getLogger().debug("Expected 0 actions for mismatch, got " + inventoryTransactionPacket.actions.length + ", " + Arrays.toString(inventoryTransactionPacket.actions));
                            }
                            this.getCursorInventory().sendContents(this);
                            this.sendAllInventories();
                            if (this.af++ <= 10) return;
                            this.close("", "Too many failed inventory transactions");
                            return;
                        }
                        case 2: {
                            Object object7;
                            UseItemData useItemData = (UseItemData)inventoryTransactionPacket.transactionData;
                            BlockVector3 blockVector3 = useItemData.blockPos;
                            object7 = useItemData.face;
                            int n19 = useItemData.actionType;
                            if (this.isBlocking()) {
                                this.setBlocking(false);
                            }
                            if (this.inventory.getHeldItemIndex() != useItemData.hotbarSlot) {
                                this.inventory.equipItem(useItemData.hotbarSlot);
                            }
                            switch (n19) {
                                case 0: {
                                    Cloneable cloneable;
                                    Cloneable cloneable2;
                                    if (!this.server.doNotLimitInteractions && this.Y != null && this.getInventory().getItemInHandFast().getBlockId() == 0 && (double)System.currentTimeMillis() - this.D < 200.0 && blockVector3.distanceSquared(this.Y) < 1.0E-5) {
                                        return;
                                    }
                                    this.Y = blockVector3.asVector3();
                                    this.D = System.currentTimeMillis();
                                    this.setUsingItem(false);
                                    if (!(this.distance(blockVector3.asVector3()) > (double)(this.isCreative() ? 13 : 7))) {
                                        if (this.isCreative()) {
                                            if (this.level.useItemOn(blockVector3.asVector3(), this.inventory.getItemInHand(), (BlockFace)((Object)object7), useItemData.clickPos.x, useItemData.clickPos.y, useItemData.clickPos.z, this) != null) {
                                                return;
                                            }
                                        } else if (this.inventory.getItemInHand().equals(useItemData.itemInHand)) {
                                            cloneable2 = this.inventory.getItemInHand();
                                            cloneable = ((Item)cloneable2).clone();
                                            cloneable2 = this.level.useItemOn(blockVector3.asVector3(), (Item)cloneable2, (BlockFace)((Object)object7), useItemData.clickPos.x, useItemData.clickPos.y, useItemData.clickPos.z, this);
                                            if (cloneable2 != null) {
                                                if (((Item)cloneable2).equals(cloneable)) {
                                                    if (((Item)cloneable2).getCount() == ((Item)cloneable).getCount()) return;
                                                }
                                                if (((Item)cloneable).getId() == ((Item)cloneable2).getId() || ((Item)cloneable2).getId() == 0) {
                                                    this.inventory.setItemInHand((Item)cloneable2);
                                                } else {
                                                    this.server.getLogger().debug("Tried to set item " + ((Item)cloneable2).getId() + " but " + this.username + " had item " + ((Item)cloneable).getId() + " in their hand slot");
                                                }
                                                this.inventory.sendHeldItem(this.getViewers().values());
                                                return;
                                            }
                                        }
                                    }
                                    this.b();
                                    if (blockVector3.distanceSquared(this) > 10000.0) {
                                        return;
                                    }
                                    cloneable2 = this.level.getBlock(blockVector3.asVector3());
                                    Block block = ((Block)cloneable2).getSide((BlockFace)((Object)object7));
                                    this.level.sendBlocks(this, (Vector3[])new Block[]{cloneable2, block}, 11);
                                    if (!(cloneable2 instanceof BlockDoor)) return;
                                    cloneable = (BlockDoor)cloneable2;
                                    if ((((BlockMeta)cloneable).getDamage() & 8) <= 0) return;
                                    Block block2 = ((Block)cloneable2).down();
                                    if (block2.getId() != ((Block)cloneable2).getId()) return;
                                    cloneable2 = block2;
                                    this.level.sendBlocks(this, (Vector3[])new Block[]{cloneable2}, 11);
                                    return;
                                }
                                case 2: {
                                    if (!this.spawned) return;
                                    if (!this.isAlive()) {
                                        return;
                                    }
                                    this.resetCraftingGridType();
                                    Item item10 = this.getInventory().getItemInHand();
                                    Item item11 = item10.clone();
                                    if (this.canInteract(blockVector3.add(0.5, 0.5, 0.5), this.isCreative() ? 13.0 : 7.0) && (item10 = this.level.useBreakOn(blockVector3.asVector3(), (BlockFace)((Object)object7), item10, this, true)) != null) {
                                        if (!this.isSurvival()) {
                                            if (!this.isAdventure()) return;
                                        }
                                        this.foodData.updateFoodExpLevel(0.005);
                                        if (item10.equals(item11)) {
                                            if (item10.getCount() == item11.getCount()) return;
                                        }
                                        if (item11.getId() == item10.getId() || item10.getId() == 0) {
                                            this.inventory.setItemInHand(item10);
                                        } else {
                                            this.server.getLogger().debug("Tried to set item " + item10.getId() + " but " + this.username + " had item " + item11.getId() + " in their hand slot");
                                        }
                                        this.inventory.sendHeldItem(this.getViewers().values());
                                        return;
                                    }
                                    this.b();
                                    if (!(blockVector3.distanceSquared(this) < 10000.0)) return;
                                    this.level.sendBlocks(this, (Vector3[])new Block[]{this.level.getBlock(blockVector3.asVector3(), false)}, 11);
                                    BlockEntity blockEntity = this.level.getBlockEntityIfLoaded(this.chunk, blockVector3.asVector3());
                                    if (!(blockEntity instanceof BlockEntitySpawnable)) return;
                                    ((BlockEntitySpawnable)blockEntity).spawnTo(this);
                                    return;
                                }
                                case 1: {
                                    if (!this.spawned) return;
                                    if (!this.isAlive()) {
                                        return;
                                    }
                                    if (this.inventory.getHeldItemIndex() != useItemData.hotbarSlot) {
                                        this.inventory.equipItem(useItemData.hotbarSlot);
                                    }
                                    Item item12 = this.inventory.getItemInHand();
                                    Vector3 vector3 = this.getDirectionVector();
                                    PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(this, item12, vector3, (BlockFace)((Object)object7), PlayerInteractEvent.Action.RIGHT_CLICK_AIR);
                                    this.server.getPluginManager().callEvent(playerInteractEvent);
                                    if (playerInteractEvent.isCancelled()) {
                                        this.b();
                                        return;
                                    }
                                    if (item12 instanceof ItemCrossbow) {
                                        ItemCrossbow itemCrossbow = (ItemCrossbow)item12;
                                        if (itemCrossbow.isLoaded()) {
                                            if (this.J >= 3) return;
                                            if (!itemCrossbow.launchArrow(this)) return;
                                            ++this.J;
                                            return;
                                        }
                                        if (this.isUsingItem()) {
                                            int n20 = this.server.getTick() - this.startAction;
                                            this.setUsingItem(false);
                                            item12.onUse(this, n20);
                                            return;
                                        }
                                        this.setUsingItem(true);
                                        this.getLevel().addLevelSoundEvent(this, 245);
                                        return;
                                    }
                                    if (!item12.onClickAir(this, vector3)) return;
                                    if (this.isSurvival() || this.isAdventure()) {
                                        if (item12.getId() == 0 || this.inventory.getItemInHandFast().getId() == item12.getId()) {
                                            this.inventory.setItemInHand(item12);
                                        } else if (Nukkit.DEBUG > 1) {
                                            this.server.getLogger().debug("Tried to set item " + item12.getId() + " but " + this.username + " had item " + this.inventory.getItemInHandFast().getId() + " in their hand slot");
                                        }
                                    }
                                    if (this.isUsingItem()) {
                                        int n21 = this.server.getTick() - this.startAction;
                                        this.setUsingItem(false);
                                        if (item12.onUse(this, n21)) return;
                                        this.b();
                                        return;
                                    }
                                    this.setUsingItem(true);
                                    return;
                                }
                            }
                            return;
                        }
                        case 3: {
                            UseItemOnEntityData useItemOnEntityData = (UseItemOnEntityData)inventoryTransactionPacket.transactionData;
                            Entity entity = this.level.getEntity(useItemOnEntityData.entityRuntimeId);
                            if (entity == null) {
                                return;
                            }
                            int n22 = useItemOnEntityData.actionType;
                            if (this.inventory.getHeldItemIndex() != useItemOnEntityData.hotbarSlot) {
                                this.inventory.equipItem(useItemOnEntityData.hotbarSlot);
                            }
                            Item item13 = this.inventory.getItemInHand();
                            switch (n22) {
                                case 0: {
                                    if (this.distanceSquared(entity) > 1000.0) {
                                        this.getServer().getLogger().debug(this.username + ": target entity is too far away");
                                        return;
                                    }
                                    this.setUsingItem(false);
                                    PlayerInteractEntityEvent playerInteractEntityEvent = new PlayerInteractEntityEvent(this, entity, item13, useItemOnEntityData.clickPos);
                                    if (this.isSpectator()) {
                                        playerInteractEntityEvent.setCancelled();
                                    }
                                    this.getServer().getPluginManager().callEvent(playerInteractEntityEvent);
                                    if (playerInteractEntityEvent.isCancelled()) {
                                        return;
                                    }
                                    if (!entity.onInteract(this, item13, useItemOnEntityData.clickPos)) return;
                                    if (!this.isSurvival()) {
                                        if (!this.isAdventure()) return;
                                    }
                                    if (item13.isTool()) {
                                        if (item13.useOn(entity) && item13.getDamage() >= item13.getMaxDurability()) {
                                            this.level.addSound((Vector3)this, Sound.RANDOM_BREAK);
                                            this.level.addParticle(new ItemBreakParticle(this, item13));
                                            item13 = Item.get(0);
                                        }
                                    } else if (item13.count > 1) {
                                        --item13.count;
                                    } else {
                                        item13 = Item.get(0);
                                    }
                                    if (item13.getId() != 0 && this.inventory.getItemInHandFast().getId() != item13.getId()) {
                                        if (Nukkit.DEBUG <= 1) return;
                                        this.server.getLogger().debug("Tried to set item " + item13.getId() + " but " + this.username + " had item " + this.inventory.getItemInHandFast().getId() + " in their hand slot");
                                        return;
                                    }
                                    this.inventory.setItemInHand(item13);
                                    return;
                                }
                                case 1: {
                                    if (entity.getId() == this.getId()) {
                                        this.kick(PlayerKickEvent.Reason.INVALID_PVP, "Tried to attack invalid player");
                                        return;
                                    }
                                    this.setUsingItem(false);
                                    if (!this.a(entity, this.isCreative() ? 8.0 : 5.0)) {
                                        return;
                                    }
                                    if (entity instanceof Player) {
                                        if ((((Player)entity).gamemode & 1) > 0) {
                                            return;
                                        }
                                        if (!this.server.pvpEnabled) {
                                            return;
                                        }
                                    }
                                    Enchantment[] enchantmentArray = item13.getEnchantments();
                                    float f4 = item13.getAttackDamage();
                                    for (Enchantment enchantment : enchantmentArray) {
                                        f4 = (float)((double)f4 + enchantment.getDamageBonus(entity));
                                    }
                                    EnumMap enumMap = new EnumMap(EntityDamageEvent.DamageModifier.class);
                                    enumMap.put(EntityDamageEvent.DamageModifier.BASE, Float.valueOf(f4));
                                    float f5 = 0.3f;
                                    Enchantment enchantment = item13.getEnchantment(12);
                                    if (enchantment != null) {
                                        f5 += (float)enchantment.getLevel() * 0.1f;
                                    }
                                    EntityDamageByEntityEvent entityDamageByEntityEvent = new EntityDamageByEntityEvent(this, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, enumMap, f5, enchantmentArray);
                                    if (this.isSpectator()) {
                                        entityDamageByEntityEvent.setCancelled();
                                    }
                                    if (entity instanceof Player && !this.level.getGameRules().getBoolean(GameRule.PVP)) {
                                        entityDamageByEntityEvent.setCancelled();
                                    }
                                    if (!entity.attack(entityDamageByEntityEvent)) {
                                        if (!item13.isTool()) return;
                                        if (this.isCreative()) return;
                                        this.b();
                                        return;
                                    }
                                    if (this.server.attackStopSprint) {
                                        this.setSprinting(false);
                                    }
                                    for (Enchantment enchantment2 : item13.getEnchantments()) {
                                        enchantment2.doPostAttack(this, entity);
                                    }
                                    if (!item13.isTool()) return;
                                    if (this.isCreative()) return;
                                    if (item13.useOn(entity) && item13.getDamage() >= item13.getMaxDurability()) {
                                        this.level.addSound((Vector3)this, Sound.RANDOM_BREAK);
                                        this.level.addParticle(new ItemBreakParticle(this, item13));
                                        this.inventory.setItemInHand(Item.get(0));
                                        return;
                                    }
                                    if (item13.getId() != 0 && this.inventory.getItemInHandFast().getId() != item13.getId()) {
                                        if (Nukkit.DEBUG <= 1) return;
                                        this.server.getLogger().debug("Tried to set item " + item13.getId() + " but " + this.username + " had item " + this.inventory.getItemInHandFast().getId() + " in their hand slot");
                                        return;
                                    }
                                    this.inventory.setItemInHand(item13);
                                    return;
                                }
                            }
                            return;
                        }
                        case 4: {
                            if (this.isSpectator()) {
                                this.sendAllInventories();
                                return;
                            }
                            ReleaseItemData releaseItemData = (ReleaseItemData)inventoryTransactionPacket.transactionData;
                            try {
                                int n23 = releaseItemData.actionType;
                                switch (n23) {
                                    case 0: {
                                        if (!this.isUsingItem()) {
                                            ++this.af;
                                            this.b();
                                            return;
                                        }
                                        int n24 = this.server.getTick() - this.startAction;
                                        if (!this.inventory.getItemInHand().onRelease(this, n24)) {
                                            this.b();
                                        }
                                        this.setUsingItem(false);
                                        return;
                                    }
                                    case 1: {
                                        Object object8;
                                        if (this.protocol >= 388) {
                                            return;
                                        }
                                        Item item14 = this.inventory.getItemInHand();
                                        PlayerItemConsumeEvent playerItemConsumeEvent = new PlayerItemConsumeEvent(this, item14);
                                        if (item14.getId() == 373) {
                                            this.server.getPluginManager().callEvent(playerItemConsumeEvent);
                                            if (playerItemConsumeEvent.isCancelled()) {
                                                this.b();
                                                return;
                                            }
                                            object8 = Potion.getPotion(item14.getDamage());
                                            if (this.gamemode == 0 || this.gamemode == 2) {
                                                this.getInventory().decreaseCount(this.getInventory().getHeldItemIndex());
                                                this.inventory.addItem(Item.get(374));
                                            }
                                            if (object8 == null) return;
                                            ((Potion)object8).applyPotion(this);
                                            return;
                                        }
                                        this.server.getPluginManager().callEvent(playerItemConsumeEvent);
                                        if (playerItemConsumeEvent.isCancelled()) {
                                            this.b();
                                            return;
                                        }
                                        object8 = Food.getByRelative(item14);
                                        if (object8 == null) return;
                                        if (!((Food)object8).eatenBy(this)) return;
                                        this.getInventory().decreaseCount(this.getInventory().getHeldItemIndex());
                                        return;
                                    }
                                }
                                this.getServer().getLogger().debug(this.username + ": unknown release item action type: " + n23);
                                return;
                            }
                            finally {
                                this.setUsingItem(false);
                            }
                        }
                        default: {
                            this.b();
                            return;
                        }
                    }
                }
                case 48: {
                    if (this.a(dataPacket, 40, -1)) {
                        return;
                    }
                    if (this.inventory == null) {
                        this.getServer().getLogger().debug(this.username + ": got PlayerHotbarPacket but inventory was null");
                        return;
                    }
                    PlayerHotbarPacket playerHotbarPacket = (PlayerHotbarPacket)dataPacket;
                    if (playerHotbarPacket.windowId != 0) {
                        return;
                    }
                    this.inventory.equipItem(playerHotbarPacket.selectedHotbarSlot);
                    this.setUsingItem(false);
                    return;
                }
                case 102: {
                    if (this.a(dataPacket, 20, 100)) {
                        return;
                    }
                    PlayerServerSettingsRequestEvent playerServerSettingsRequestEvent = new PlayerServerSettingsRequestEvent(this, new HashMap<Integer, FormWindow>(this.serverSettings));
                    this.getServer().getPluginManager().callEvent(playerServerSettingsRequestEvent);
                    if (playerServerSettingsRequestEvent.isCancelled()) return;
                    playerServerSettingsRequestEvent.getSettings().forEach((n, formWindow) -> {
                        ServerSettingsResponsePacket serverSettingsResponsePacket = new ServerSettingsResponsePacket();
                        serverSettingsResponsePacket.formId = n;
                        serverSettingsResponsePacket.data = formWindow.getJSONData();
                        this.dataPacket(serverSettingsResponsePacket);
                    });
                    return;
                }
                case 113: {
                    if (this.locallyInitialized) return;
                    if (this.protocol <= 274) {
                        return;
                    }
                    this.doFirstSpawn();
                    return;
                }
                case 45: {
                    if (this.isAlive()) return;
                    if (this.protocol < 388) {
                        return;
                    }
                    if (this.a(dataPacket, 20, -1)) {
                        return;
                    }
                    RespawnPacket respawnPacket = (RespawnPacket)dataPacket;
                    if (respawnPacket.respawnState != 2) return;
                    RespawnPacket respawnPacket2 = new RespawnPacket();
                    respawnPacket2.x = (float)this.getX();
                    respawnPacket2.y = (float)this.getY();
                    respawnPacket2.z = (float)this.getZ();
                    respawnPacket2.respawnState = 1;
                    this.dataPacket(respawnPacket2);
                    return;
                }
                case 97: {
                    boolean bl;
                    if (!this.spawned) {
                        return;
                    }
                    if (this.a(dataPacket, 20, -1)) {
                        return;
                    }
                    if (this.inventory == null) {
                        this.getServer().getLogger().debug(this.username + ": got BookEditPacket but inventory was null");
                        return;
                    }
                    BookEditPacket bookEditPacket = (BookEditPacket)dataPacket;
                    Item item15 = this.inventory.getItem(bookEditPacket.inventorySlot);
                    if (item15.getId() != 386) {
                        this.getServer().getLogger().debug(this.username + ": BookEditPacket for invalid item: expected Book & Quill (386), got " + item15.getId());
                        return;
                    }
                    if (bookEditPacket.text != null && bookEditPacket.text.length() > 256) {
                        this.getServer().getLogger().debug(this.username + ": BookEditPacket with too long text");
                        return;
                    }
                    Item item16 = item15.clone();
                    switch (bookEditPacket.action) {
                        case REPLACE_PAGE: {
                            bl = ((ItemBookAndQuill)item16).setPageText(bookEditPacket.pageNumber, bookEditPacket.text);
                            break;
                        }
                        case ADD_PAGE: {
                            bl = ((ItemBookAndQuill)item16).insertPage(bookEditPacket.pageNumber, bookEditPacket.text);
                            break;
                        }
                        case DELETE_PAGE: {
                            bl = ((ItemBookAndQuill)item16).deletePage(bookEditPacket.pageNumber);
                            break;
                        }
                        case SWAP_PAGES: {
                            bl = ((ItemBookAndQuill)item16).swapPages(bookEditPacket.pageNumber, bookEditPacket.secondaryPageNumber);
                            break;
                        }
                        case SIGN_BOOK: {
                            item16 = Item.get(387, 0, 1, item15.getCompoundTag());
                            if (bookEditPacket.title == null || bookEditPacket.author == null || bookEditPacket.xuid == null || bookEditPacket.title.length() > 64 || bookEditPacket.author.length() > 64 || bookEditPacket.xuid.length() > 64) {
                                this.getServer().getLogger().debug(this.username + ": invalid BookEditPacket action SIGN_BOOK: title/author/xuid is too long");
                                return;
                            }
                            bl = ((ItemBookWritten)item16).signBook(bookEditPacket.title, bookEditPacket.author, bookEditPacket.xuid, 0);
                            break;
                        }
                        default: {
                            this.getServer().getLogger().debug(this.username + ": BookEditPacket unknown action: " + (Object)((Object)bookEditPacket.action));
                            return;
                        }
                    }
                    if (!bl) return;
                    PlayerEditBookEvent playerEditBookEvent = new PlayerEditBookEvent(this, item15, item16, bookEditPacket.action);
                    this.server.getPluginManager().callEvent(playerEditBookEvent);
                    if (playerEditBookEvent.isCancelled()) return;
                    this.inventory.setItem(bookEditPacket.inventorySlot, playerEditBookEvent.getNewBook());
                    return;
                }
                case -93: {
                    if (!this.spawned) {
                        return;
                    }
                    if (this.a(dataPacket, 20, -1)) {
                        return;
                    }
                    FilterTextPacket filterTextPacket = (FilterTextPacket)dataPacket;
                    if (filterTextPacket.text != null && filterTextPacket.text.length() <= 64) {
                        FilterTextPacket filterTextPacket2 = new FilterTextPacket();
                        filterTextPacket2.text = filterTextPacket.text;
                        filterTextPacket2.fromServer = true;
                        this.dataPacket(filterTextPacket2);
                        return;
                    }
                    this.getServer().getLogger().debug(this.username + ": FilterTextPacket with too long text");
                    return;
                }
                case -100: {
                    if (this.a(dataPacket, 20, 100)) {
                        return;
                    }
                    PacketViolationWarningPacket packetViolationWarningPacket = (PacketViolationWarningPacket)dataPacket;
                    if (ah == null) {
                        ah = Arrays.stream(ProtocolInfo.class.getDeclaredFields()).filter(field -> field.getType() == Byte.TYPE);
                    }
                    Optional<String> optional = ah.filter(field -> {
                        try {
                            return field.getByte(null) == ((PacketViolationWarningPacket)dataPacket).packetId;
                        }
                        catch (IllegalAccessException illegalAccessException) {
                            return false;
                        }
                    }).map(Field::getName).findFirst();
                    this.getServer().getLogger().warning("PacketViolationWarningPacket" + optional.map(string -> " for " + string).orElse(" UNKNOWN") + " from " + this.username + " (Protocol " + this.protocol + "): " + packetViolationWarningPacket.toString());
                    return;
                }
                case -118: {
                    if (!this.spawned) return;
                    if (this.server.getTick() - this.X < 20) return;
                    if (this.isSpectator()) {
                        return;
                    }
                    this.X = this.server.getTick();
                    EmotePacket emotePacket = (EmotePacket)dataPacket;
                    if (emotePacket.runtimeId != this.id) {
                        this.getServer().getLogger().debug(this.username + ": EmotePacket eid mismatch");
                        return;
                    }
                    if (emotePacket.emoteID == null || emotePacket.emoteID.isEmpty() || emotePacket.emoteID.length() > 100) {
                        this.getServer().getLogger().debug(this.username + " EmotePacket invalid emote id: " + emotePacket.emoteID);
                        return;
                    }
                    Iterator<Player> iterator = this.getViewers().values().iterator();
                    while (iterator.hasNext()) {
                        Player player = iterator.next();
                        if (player.protocol < 407) continue;
                        player.dataPacket(emotePacket);
                    }
                    return;
                }
                case 125: {
                    Position position;
                    if (!this.spawned) {
                        return;
                    }
                    if (this.a(dataPacket, 20, -1)) {
                        return;
                    }
                    LecternUpdatePacket lecternUpdatePacket = (LecternUpdatePacket)dataPacket;
                    Vector3 vector3 = lecternUpdatePacket.blockPosition.asVector3();
                    if (vector3.distanceSquared(this) > 4096.0) {
                        return;
                    }
                    if (lecternUpdatePacket.dropBook) {
                        position = this.getLevel().getBlock(this.chunk, vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ(), false);
                        if (!(position instanceof BlockLectern)) return;
                        ((BlockLectern)position).dropBook(this);
                        return;
                    }
                    position = this.level.getBlockEntityIfLoaded(this.chunk, vector3);
                    if (!(position instanceof BlockEntityLectern)) return;
                    BlockEntityLectern blockEntityLectern = (BlockEntityLectern)position;
                    if (blockEntityLectern.getRawPage() == lecternUpdatePacket.page) return;
                    blockEntityLectern.setRawPage(lecternUpdatePacket.page);
                    blockEntityLectern.spawnToAll();
                    return;
                }
                case 60: {
                    if (!this.spawned) return;
                    if (!this.hasPermission("nukkit.command.difficulty")) {
                        return;
                    }
                    if (this.a(dataPacket, 20, 100)) {
                        return;
                    }
                    this.server.setDifficulty(((SetDifficultyPacket)dataPacket).difficulty);
                    SetDifficultyPacket setDifficultyPacket = new SetDifficultyPacket();
                    setDifficultyPacket.difficulty = this.server.getDifficulty();
                    Server.broadcastPacket(this.server.getOnlinePlayers().values(), (DataPacket)setDifficultyPacket);
                    Command.broadcastCommandMessage((CommandSender)this, new TranslationContainer("commands.difficulty.success", String.valueOf(this.server.getDifficulty())));
                    return;
                }
                default: {
                    return;
                }
            }
        }
    }

    private void a(BlockVector3 blockVector3, BlockFace blockFace) {
        if (this.isBreakingBlock()) {
            LevelEventPacket levelEventPacket = new LevelEventPacket();
            levelEventPacket.evid = 3601;
            levelEventPacket.x = (float)this.breakingBlock.x;
            levelEventPacket.y = (float)this.breakingBlock.y;
            levelEventPacket.z = (float)this.breakingBlock.z;
            levelEventPacket.data = 0;
            this.getLevel().addChunkPacket((int)this.breakingBlock.x >> 4, (int)this.breakingBlock.z >> 4, levelEventPacket);
        }
        this.breakingBlock = null;
    }

    private void c(BlockVector3 blockVector3, BlockFace blockFace) {
        block0: {
            if (!this.isBreakingBlock()) break block0;
            this.level.addParticle(new PunchBlockParticle(this.breakingBlock, this.breakingBlock, blockFace));
        }
    }

    private void d(BlockVector3 blockVector3, BlockFace blockFace) {
        double d2;
        boolean bl = this.H.equals(blockVector3);
        long l = System.currentTimeMillis();
        if (bl && l - this.lastBreak < 10L) {
            return;
        }
        if (blockVector3.distanceSquared(this) > 100.0) {
            this.breakingBlock = null;
            return;
        }
        Block block = this.level.getBlock(this.chunk, blockVector3.x, blockVector3.y, blockVector3.z, false);
        PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(this, this.inventory.getItemInHand(), block, blockFace, block.getId() == 0 ? PlayerInteractEvent.Action.LEFT_CLICK_AIR : PlayerInteractEvent.Action.LEFT_CLICK_BLOCK);
        this.getServer().getPluginManager().callEvent(playerInteractEvent);
        if (playerInteractEvent.isCancelled()) {
            this.b();
            return;
        }
        switch (block.getId()) {
            case 0: {
                this.breakingBlock = null;
                if (!bl) {
                    this.lastBreak = l;
                }
                this.H = blockVector3;
                return;
            }
            case 25: {
                ((BlockNoteblock)block).emitSound();
                return;
            }
            case 122: {
                if (!this.isCreative()) {
                    ((BlockDragonEgg)block).teleport();
                    return;
                }
            }
            case 199: {
                BlockEntity blockEntity = this.level.getBlockEntityIfLoaded(this.chunk, this.temporalVector.setComponents(blockVector3.x, blockVector3.y, blockVector3.z));
                if (!(blockEntity instanceof BlockEntityItemFrame) || !((BlockEntityItemFrame)blockEntity).dropItem(this)) break;
                return;
            }
        }
        int n = this.level.getBlockIdAt(blockVector3.x + blockFace.getXOffset(), blockVector3.y + blockFace.getYOffset(), blockVector3.z + blockFace.getZOffset());
        if (n == 51 || n == 492) {
            Vector3 vector3 = this.temporalVector.setComponents(blockVector3.x + blockFace.getXOffset(), blockVector3.y + blockFace.getYOffset(), blockVector3.z + blockFace.getZOffset());
            this.level.setBlock(vector3, Block.get(0), true);
            this.level.addLevelSoundEvent(vector3, 65);
            return;
        }
        if (!this.isCreative() && (d2 = Math.ceil(block.getBreakTime(this.inventory.getItemInHandFast(), this) * 20.0)) > 0.0) {
            LevelEventPacket levelEventPacket = new LevelEventPacket();
            levelEventPacket.evid = 3600;
            levelEventPacket.x = blockVector3.x;
            levelEventPacket.y = blockVector3.y;
            levelEventPacket.z = blockVector3.z;
            levelEventPacket.data = (int)(65535.0 / d2);
            this.getLevel().addChunkPacket(blockVector3.x >> 4, blockVector3.z >> 4, levelEventPacket);
        }
        this.breakingBlock = block;
        if (!bl) {
            this.lastBreak = l;
        }
        this.H = blockVector3;
    }

    private void b(BlockVector3 blockVector3, BlockFace blockFace) {
        block7: {
            if (!this.spawned || !this.isAlive()) {
                return;
            }
            this.resetCraftingGridType();
            Item item = this.getInventory().getItemInHand();
            Item item2 = item.clone();
            if (this.canInteract(blockVector3.add(0.5, 0.5, 0.5), this.isCreative() ? 13.0 : 7.0) && (item = this.level.useBreakOn(blockVector3.asVector3(), blockFace, item, this, true)) != null) {
                if (this.isSurvival() || this.isAdventure()) {
                    this.foodData.updateFoodExpLevel(0.005);
                    if (!item.equals(item2) || item.getCount() != item2.getCount()) {
                        if (item2.getId() == item.getId() || item.getId() == 0) {
                            this.inventory.setItemInHand(item);
                        } else {
                            this.server.getLogger().debug("Tried to set item " + item.getId() + " but " + this.username + " had item " + item2.getId() + " in their hand slot");
                        }
                        this.inventory.sendHeldItem(this.getViewers().values());
                    }
                }
                return;
            }
            this.b();
            if (!(blockVector3.distanceSquared(this) < 10000.0)) break block7;
            this.level.sendBlocks(this, (Vector3[])new Block[]{this.level.getBlock(blockVector3.asVector3(), false)}, 11);
            BlockEntity blockEntity = this.level.getBlockEntityIfLoaded(this.chunk, blockVector3.asVector3());
            if (blockEntity instanceof BlockEntitySpawnable) {
                ((BlockEntitySpawnable)blockEntity).spawnTo(this);
            }
        }
    }

    private static boolean a(Block block) {
        switch (block.getId()) {
            case 20: 
            case 79: 
            case 89: 
            case 138: 
            case 169: 
            case 241: 
            case 253: 
            case 254: 
            case 416: {
                return true;
            }
        }
        return false;
    }

    private static Color a(BlockColor blockColor, int n) {
        int n2;
        if (n == 2) {
            n2 = 255;
        } else if (n == 1) {
            n2 = 220;
        } else if (n == 0) {
            n2 = 180;
        } else {
            throw new IllegalArgumentException("Invalid colorLevel: " + n);
        }
        int n3 = blockColor.getRed() * n2 / 255;
        int n4 = blockColor.getGreen() * n2 / 255;
        int n5 = blockColor.getBlue() * n2 / 255;
        return new Color(n3, n4, n5);
    }

    public boolean chat(String string) {
        this.resetCraftingGridType();
        if (this.removeFormat) {
            string = TextFormat.clean(string, true);
        }
        for (String string2 : string.split("\n")) {
            if (string2.trim().isEmpty() || string2.length() >= 512) continue;
            PlayerChatEvent playerChatEvent = new PlayerChatEvent(this, string2);
            this.server.getPluginManager().callEvent(playerChatEvent);
            if (playerChatEvent.isCancelled()) continue;
            this.server.broadcastMessage(this.getServer().getLanguage().translateString(playerChatEvent.getFormat(), new String[]{playerChatEvent.getPlayer().displayName, playerChatEvent.getMessage()}), playerChatEvent.getRecipients());
        }
        return true;
    }

    public boolean kick() {
        return this.kick("");
    }

    public boolean kick(String string, boolean bl) {
        return this.kick(PlayerKickEvent.Reason.UNKNOWN, string, bl);
    }

    public boolean kick(String string) {
        return this.kick(PlayerKickEvent.Reason.UNKNOWN, string);
    }

    public boolean kick(PlayerKickEvent.Reason reason) {
        return this.kick(reason, true);
    }

    public boolean kick(PlayerKickEvent.Reason reason, String string) {
        return this.kick(reason, string, true);
    }

    public boolean kick(PlayerKickEvent.Reason reason, boolean bl) {
        return this.kick(reason, reason.toString(), bl);
    }

    public boolean kick(PlayerKickEvent.Reason reason, String string, boolean bl) {
        return this.kick(reason, string, bl, "");
    }

    public boolean kick(PlayerKickEvent.Reason reason, String string, boolean bl, String string2) {
        PlayerKickEvent playerKickEvent = new PlayerKickEvent(this, reason, string, this.getLeaveMessage(), string2);
        this.server.getPluginManager().callEvent(playerKickEvent);
        if (!playerKickEvent.isCancelled()) {
            String string3 = bl ? (!this.isBanned() ? "Kicked!" + (!string.isEmpty() ? " Reason: " + string : "") : string) : (string.isEmpty() ? "disconnectionScreen.noReason" : string);
            this.close(playerKickEvent.getQuitMessage(), string3);
            return true;
        }
        return false;
    }

    public void setViewDistance(int n) {
        this.viewDistance = n;
        this.chunkRadius = n;
        ChunkRadiusUpdatedPacket chunkRadiusUpdatedPacket = new ChunkRadiusUpdatedPacket();
        chunkRadiusUpdatedPacket.radius = n;
        this.dataPacket(chunkRadiusUpdatedPacket);
    }

    public int getViewDistance() {
        return this.chunkRadius;
    }

    public int getMaximumViewDistance() {
        return this.viewDistance;
    }

    @Override
    public void sendMessage(String string) {
        this.sendMessage(string, false);
    }

    public void sendMessage(String string, boolean bl) {
        TextPacket textPacket = new TextPacket();
        textPacket.type = 0;
        textPacket.message = this.server.getLanguage().translateString(string);
        textPacket.isLocalized = bl;
        this.dataPacket(textPacket);
    }

    @Override
    public void sendMessage(TextContainer textContainer) {
        if (textContainer instanceof TranslationContainer) {
            this.sendTranslation(textContainer.getText(), ((TranslationContainer)textContainer).getParameters());
            return;
        }
        this.sendMessage(textContainer.getText(), false);
    }

    public void sendTranslation(String string) {
        this.sendTranslation(string, new String[0]);
    }

    public void sendTranslation(String string, String[] stringArray) {
        TextPacket textPacket = new TextPacket();
        if (this.server.isLanguageForced()) {
            textPacket.type = 0;
            textPacket.message = this.server.getLanguage().translateString(string, stringArray);
        } else {
            textPacket.type = (byte)2;
            textPacket.message = this.server.getLanguage().translateString(string, stringArray, "nukkit.");
            for (int k = 0; k < stringArray.length; ++k) {
                stringArray[k] = this.server.getLanguage().translateString(stringArray[k], stringArray, "nukkit.");
            }
            textPacket.parameters = stringArray;
        }
        this.dataPacket(textPacket);
    }

    public void sendChat(String string) {
        this.sendChat("", string);
    }

    public void sendChat(String string, String string2) {
        TextPacket textPacket = new TextPacket();
        textPacket.type = 1;
        textPacket.source = string;
        textPacket.message = this.server.getLanguage().translateString(string2);
        this.dataPacket(textPacket);
    }

    public void sendPopup(String string) {
        TextPacket textPacket = new TextPacket();
        textPacket.type = (byte)3;
        textPacket.message = string;
        this.dataPacket(textPacket);
    }

    public void sendPopup(String string, String string2) {
        this.sendPopup(string);
    }

    public void sendTip(String string) {
        TextPacket textPacket = new TextPacket();
        textPacket.type = (byte)5;
        textPacket.message = string;
        this.dataPacket(textPacket);
    }

    public void clearTitle() {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.type = 0;
        this.dataPacket(setTitlePacket);
    }

    public void resetTitleSettings() {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.type = 1;
        this.dataPacket(setTitlePacket);
    }

    public void setSubtitle(String string) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.type = 3;
        setTitlePacket.text = string;
        this.dataPacket(setTitlePacket);
    }

    public void setTitleAnimationTimes(int n, int n2, int n3) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.type = 5;
        setTitlePacket.fadeInTime = n;
        setTitlePacket.stayTime = n2;
        setTitlePacket.fadeOutTime = n3;
        this.dataPacket(setTitlePacket);
    }

    private void a(String string) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.text = string;
        setTitlePacket.type = 2;
        this.dataPacket(setTitlePacket);
    }

    public void sendTitle(String string) {
        this.sendTitle(string, null, 20, 20, 5);
    }

    public void sendTitle(String string, String string2) {
        this.sendTitle(string, string2, 20, 20, 5);
    }

    public void sendTitle(String string, String string2, int n, int n2, int n3) {
        this.setTitleAnimationTimes(n, n2, n3);
        if (!Strings.isNullOrEmpty(string2)) {
            this.setSubtitle(string2);
        }
        this.a(Strings.isNullOrEmpty(string) ? " " : string);
    }

    public void sendActionBar(String string) {
        this.sendActionBar(string, 1, 0, 1);
    }

    public void sendActionBar(String string, int n, int n2, int n3) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.type = 4;
        setTitlePacket.text = string;
        setTitlePacket.fadeInTime = n;
        setTitlePacket.stayTime = n2;
        setTitlePacket.fadeOutTime = n3;
        this.dataPacket(setTitlePacket);
    }

    public void sendToast(String string, String string2) {
        if (this.protocol >= 526) {
            ToastRequestPacket toastRequestPacket = new ToastRequestPacket();
            toastRequestPacket.title = string;
            toastRequestPacket.content = string2;
            this.dataPacket(toastRequestPacket);
        } else {
            this.getServer().getLogger().debug(this.username + ": unable to send toast notification due to too old client version");
        }
    }

    @Override
    public void close() {
        this.close("");
    }

    public void close(String string) {
        this.close(string, "generic");
    }

    public void close(String string, String string2) {
        this.close(string, string2, true);
    }

    public void close(String string, String string2, boolean bl) {
        this.close(new TextContainer(string), string2, bl);
    }

    public void close(TextContainer textContainer) {
        this.close(textContainer, "generic");
    }

    public void close(TextContainer textContainer, String string) {
        this.close(textContainer, string, true);
    }

    public void close(TextContainer textContainer, String string, boolean bl) {
        if (this.connected && !this.closed) {
            Object object;
            if (bl && !string.isEmpty()) {
                object = new DisconnectPacket();
                ((DisconnectPacket)object).message = string;
                this.forceDataPacket((DataPacket)object, null);
            }
            this.connected = false;
            this.resetCraftingGridType();
            this.removeAllWindows(true);
            if (this.fishing != null) {
                this.stopFishing(false);
            }
            object = null;
            if (this.username != null && !this.username.isEmpty()) {
                object = new PlayerQuitEvent(this, textContainer, true, string);
                this.server.getPluginManager().callEvent((Event)object);
                if (this.loggedIn && ((PlayerQuitEvent)object).getAutoSave()) {
                    this.save();
                }
            }
            for (Player player : new ArrayList<Player>(this.server.Y.values())) {
                if (player.canSee(this)) continue;
                player.showPlayer(this);
            }
            this.hiddenPlayers.clear();
            this.a(false);
            super.close();
            this.interfaz.close(this, bl ? string : "");
            if (this.loggedIn) {
                this.loggedIn = false;
                this.server.removeOnlinePlayer(this);
            }
            if (object != null && !Objects.equals(this.username, "") && this.spawned && !Objects.equals(((PlayerQuitEvent)object).getQuitMessage().toString(), "")) {
                this.server.broadcastMessage(((PlayerQuitEvent)object).getQuitMessage());
            }
            this.server.getPluginManager().unsubscribeFromPermission("nukkit.broadcast.user", this);
            this.spawned = false;
            this.server.getLogger().info(this.getServer().getLanguage().translateString("nukkit.player.logOut", (Object)((Object)TextFormat.AQUA) + (this.username == null ? "" : this.username) + (Object)((Object)TextFormat.WHITE), this.getAddress(), String.valueOf(this.getPort()), this.getServer().getLanguage().translateString(string)));
            this.windows.clear();
            this.hasSpawned.clear();
            if (this.riding instanceof EntityRideable) {
                this.riding.passengers.remove(this);
            }
            this.riding = null;
        }
        if (this.perm != null) {
            this.perm.clearPermissions();
            this.perm = null;
        }
        this.inventory = null;
        this.chunk = null;
        this.server.removePlayer(this);
        if (this.loggedIn) {
            this.server.getLogger().warning("Player is still logged in: " + this.username);
            this.interfaz.close(this, bl ? string : "");
            this.server.removeOnlinePlayer(this);
            this.loggedIn = false;
        }
        if (!this.server.isPrimaryThread() && !this.server.suomiCraftPEMode()) {
            this.server.getLogger().warning("Player closed asynchronously: " + this.username);
        }
        this.ab.clear();
        this.u.clear();
    }

    public void save() {
        this.save(false);
    }

    public void save(boolean bl) {
        if (this.closed) {
            throw new IllegalStateException("Tried to save closed player");
        }
        super.saveNBT();
        if (this.level != null) {
            this.namedTag.putString("Level", this.level.getFolderName());
            if (this.spawnPosition != null && this.spawnPosition.getLevel() != null) {
                this.namedTag.putString("SpawnLevel", this.spawnPosition.getLevel().getFolderName());
                this.namedTag.putInt("SpawnX", (int)this.spawnPosition.x);
                this.namedTag.putInt("SpawnY", (int)this.spawnPosition.y);
                this.namedTag.putInt("SpawnZ", (int)this.spawnPosition.z);
            }
            CompoundTag compoundTag = new CompoundTag();
            for (String string : this.achievements) {
                compoundTag.putByte(string, 1);
            }
            this.namedTag.putCompound("Achievements", compoundTag);
            this.namedTag.putInt("playerGameType", this.gamemode);
            this.namedTag.putLong("lastPlayed", System.currentTimeMillis() / 1000L);
            this.namedTag.putString("lastIP", this.getAddress());
            this.namedTag.putInt("EXP", this.aa);
            this.namedTag.putInt("expLevel", this.S);
            this.namedTag.putInt("foodLevel", this.foodData.getLevel());
            this.namedTag.putFloat("foodSaturationLevel", this.foodData.getFoodSaturationLevel());
            this.namedTag.putInt("TimeSinceRest", this.L);
            if (!this.username.isEmpty() && this.namedTag != null) {
                if (this.server.savePlayerDataByUuid) {
                    this.server.saveOfflinePlayerData(this.uuid, this.namedTag, bl);
                } else {
                    this.server.saveOfflinePlayerData(this.username, this.namedTag, bl);
                }
            }
        }
    }

    @Override
    public String getName() {
        return this.username;
    }

    @Override
    public void kill() {
        if (!this.spawned) {
            return;
        }
        boolean bl = this.level.getGameRules().getBoolean(GameRule.SHOW_DEATH_MESSAGES);
        String string = "";
        ArrayList<String> arrayList = new ArrayList<String>();
        EntityDamageEvent entityDamageEvent = this.getLastDamageCause();
        if (bl) {
            arrayList.add(this.displayName);
            switch (entityDamageEvent == null ? EntityDamageEvent.DamageCause.CUSTOM : entityDamageEvent.getCause()) {
                case ENTITY_ATTACK: 
                case THORNS: {
                    Entity entity;
                    if (!(entityDamageEvent instanceof EntityDamageByEntityEvent)) break;
                    this.ae = entity = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager();
                    if (entity instanceof Player) {
                        string = "death.attack.player";
                        arrayList.add(((Player)entity).displayName);
                        break;
                    }
                    if (entity instanceof EntityLiving) {
                        string = "death.attack.mob";
                        arrayList.add(!Objects.equals(entity.getNameTag(), "") ? entity.getNameTag() : entity.getName());
                        break;
                    }
                    arrayList.add("Unknown");
                    break;
                }
                case PROJECTILE: {
                    Entity entity;
                    if (!(entityDamageEvent instanceof EntityDamageByEntityEvent)) break;
                    this.ae = entity = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager();
                    if (entity instanceof Player) {
                        string = "death.attack.arrow";
                        arrayList.add(((Player)entity).displayName);
                        break;
                    }
                    if (entity instanceof EntityLiving) {
                        string = "death.attack.arrow";
                        arrayList.add(!Objects.equals(entity.getNameTag(), "") ? entity.getNameTag() : entity.getName());
                        break;
                    }
                    arrayList.add("Unknown");
                    break;
                }
                case VOID: {
                    string = "death.attack.outOfWorld";
                    break;
                }
                case FALL: {
                    if (entityDamageEvent.getFinalDamage() > 2.0f) {
                        string = "death.fell.accident.generic";
                        break;
                    }
                    string = "death.attack.fall";
                    break;
                }
                case SUFFOCATION: {
                    string = "death.attack.inWall";
                    break;
                }
                case LAVA: {
                    string = "death.attack.lava";
                    break;
                }
                case MAGMA: {
                    string = "death.attack.magma";
                    break;
                }
                case FIRE: {
                    string = "death.attack.onFire";
                    break;
                }
                case FIRE_TICK: {
                    string = "death.attack.inFire";
                    break;
                }
                case DROWNING: {
                    string = "death.attack.drown";
                    break;
                }
                case CONTACT: {
                    if (!(entityDamageEvent instanceof EntityDamageByBlockEvent)) break;
                    int n = ((EntityDamageByBlockEvent)entityDamageEvent).getDamager().getId();
                    if (n == 81) {
                        string = "death.attack.cactus";
                        break;
                    }
                    if (n == 145) {
                        string = "death.attack.anvil";
                        break;
                    }
                    if (n == 462) {
                        string = "death.attack.sweetBerry";
                        break;
                    }
                    this.server.getLogger().debug("Missing death message for DamageCause.CONTACT " + n);
                    break;
                }
                case BLOCK_EXPLOSION: 
                case ENTITY_EXPLOSION: {
                    if (entityDamageEvent instanceof EntityDamageByEntityEvent) {
                        Entity entity;
                        this.ae = entity = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager();
                        if (entity instanceof Player) {
                            string = "death.attack.explosion.player";
                            arrayList.add(((Player)entity).displayName);
                            break;
                        }
                        if (entity instanceof EntityLiving) {
                            string = "death.attack.explosion.player";
                            arrayList.add(!Objects.equals(entity.getNameTag(), "") ? entity.getNameTag() : entity.getName());
                            break;
                        }
                        string = "death.attack.explosion";
                        break;
                    }
                    string = "death.attack.explosion";
                    break;
                }
                case MAGIC: {
                    string = "death.attack.magic";
                    break;
                }
                case LIGHTNING: {
                    string = "death.attack.lightningBolt";
                    break;
                }
                case HUNGER: {
                    string = "death.attack.starve";
                    break;
                }
                default: {
                    string = "death.attack.generic";
                }
            }
        }
        PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(this, this.getDrops(), new TranslationContainer(string, arrayList.toArray(new String[0])), this.S);
        playerDeathEvent.setKeepInventory(this.level.gameRules.getBoolean(GameRule.KEEP_INVENTORY));
        playerDeathEvent.setKeepExperience(playerDeathEvent.getKeepInventory());
        this.server.getPluginManager().callEvent(playerDeathEvent);
        if (!playerDeathEvent.isCancelled()) {
            if (this.fishing != null) {
                this.stopFishing(false);
            }
            this.health = 0.0f;
            this.scheduleUpdate();
            if (this.getKiller() != null && this.getKiller() instanceof EntityWalkingMob && ((EntityWalkingMob)this.getKiller()).isAngryTo == this.getId()) {
                ((EntityWalkingMob)this.getKiller()).isAngryTo = -1L;
                if (this.getKiller() instanceof EntityWolf) {
                    ((EntityWolf)this.getKiller()).setAngry(false);
                }
            }
            this.resetCraftingGridType();
            if (!playerDeathEvent.getKeepInventory() && this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS)) {
                for (Item item : playerDeathEvent.getDrops()) {
                    if (item.hasEnchantment(28)) continue;
                    this.level.dropItem(this, item, null, true, 40);
                }
                if (this.inventory != null) {
                    this.inventory.clearAll();
                }
            }
            if (!playerDeathEvent.getKeepExperience() && this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS)) {
                if (this.isSurvival() || this.isAdventure()) {
                    int n;
                    int n2 = playerDeathEvent.getExperience() * 7;
                    if (n2 > 100) {
                        n = 100;
                    }
                    this.getLevel().dropExpOrb(this, n);
                }
                this.setExperience(0, 0);
            }
            if (this.level.getGameRules().getBoolean(GameRule.DO_IMMEDIATE_RESPAWN)) {
                this.respawn();
            } else {
                if (bl && !playerDeathEvent.getDeathMessage().toString().isEmpty()) {
                    this.server.broadcast(playerDeathEvent.getDeathMessage(), "nukkit.broadcast.user");
                    if (this.protocol >= 534) {
                        DeathInfoPacket deathInfoPacket = new DeathInfoPacket();
                        deathInfoPacket.messageTranslationKey = playerDeathEvent.getDeathMessage() instanceof TranslationContainer ? this.server.getLanguage().translateString(playerDeathEvent.getDeathMessage().getText(), ((TranslationContainer)playerDeathEvent.getDeathMessage()).getParameters(), (String)null) : playerDeathEvent.getDeathMessage().getText();
                        this.dataPacket(deathInfoPacket);
                    }
                }
                RespawnPacket respawnPacket = new RespawnPacket();
                Position position = this.getSpawn();
                respawnPacket.x = (float)position.x;
                respawnPacket.y = (float)position.y;
                respawnPacket.z = (float)position.z;
                respawnPacket.respawnState = 0;
                this.dataPacket(respawnPacket);
            }
        }
    }

    protected void respawn() {
        if (this.server.isHardcore()) {
            this.setBanned(true);
            return;
        }
        this.resetCraftingGridType();
        PlayerRespawnEvent playerRespawnEvent = new PlayerRespawnEvent(this, this.getSpawn());
        this.server.getPluginManager().callEvent(playerRespawnEvent);
        Position position = playerRespawnEvent.getRespawnPosition();
        this.teleport(position, null);
        if (this.protocol < 388) {
            RespawnPacket respawnPacket = new RespawnPacket();
            respawnPacket.x = (float)position.x;
            respawnPacket.y = (float)position.y;
            respawnPacket.z = (float)position.z;
            this.dataPacket(respawnPacket);
        }
        this.a(this.aa, this.S);
        this.setSprinting(false, false);
        this.setSneaking(false);
        this.extinguish();
        this.setDataProperty(new ShortEntityData(7, 400), false);
        this.airTicks = 400;
        this.deadTicks = 0;
        this.noDamageTicks = 60;
        this.L = 0;
        this.removeAllEffects();
        this.setHealth(this.getMaxHealth());
        this.foodData.setLevel(20, 20.0f);
        this.sendData(this);
        this.setMovementSpeed(0.1f);
        this.adventureSettings.update();
        this.inventory.sendContents(this);
        this.inventory.sendArmorContents(this);
        this.offhandInventory.sendContents(this);
        this.spawnToAll();
        this.scheduleUpdate();
    }

    @Override
    public void setHealth(float f2) {
        if (f2 < 1.0f) {
            f2 = 0.0f;
        }
        super.setHealth(f2);
        if (this.spawned) {
            UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
            int n = this.getMaxHealth();
            if (this.protocol >= 440) {
                Attribute[] attributeArray = new Attribute[1];
                attributeArray[0] = Attribute.getAttribute(4).setMaxValue(n).setValue(this.health > 0.0f ? (this.health < (float)n ? this.health : (float)n) : 0.0f);
                updateAttributesPacket.entries = attributeArray;
            } else {
                Attribute[] attributeArray = new Attribute[1];
                attributeArray[0] = Attribute.getAttribute(4).setMaxValue(this.getAbsorption() % 2.0f != 0.0f ? (float)(n + 1) : (float)n).setValue(f2 > 0.0f ? (f2 < (float)n ? f2 : (float)n) : 0.0f);
                updateAttributesPacket.entries = attributeArray;
            }
            updateAttributesPacket.entityId = this.id;
            this.dataPacket(updateAttributesPacket);
        }
    }

    @Override
    public void setMaxHealth(int n) {
        super.setMaxHealth(n);
        if (this.spawned) {
            UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
            int n2 = this.getMaxHealth();
            if (this.protocol >= 440) {
                Attribute[] attributeArray = new Attribute[1];
                attributeArray[0] = Attribute.getAttribute(4).setMaxValue(n2).setValue(this.health > 0.0f ? (this.health < (float)n2 ? this.health : (float)n2) : 0.0f);
                updateAttributesPacket.entries = attributeArray;
            } else {
                Attribute[] attributeArray = new Attribute[1];
                attributeArray[0] = Attribute.getAttribute(4).setMaxValue(this.getAbsorption() % 2.0f != 0.0f ? (float)(n2 + 1) : (float)n2).setValue(this.health > 0.0f ? (this.health < (float)n2 ? this.health : (float)n2) : 0.0f);
                updateAttributesPacket.entries = attributeArray;
            }
            updateAttributesPacket.entityId = this.id;
            this.dataPacket(updateAttributesPacket);
        }
    }

    public int getExperience() {
        return this.aa;
    }

    public int getExperienceLevel() {
        return this.S;
    }

    public void addExperience(int n) {
        int n2;
        if (n == 0) {
            return;
        }
        int n3 = this.S;
        int n4 = Player.calculateRequireExperience(n3);
        for (n2 = this.aa + n; n2 >= n4; n2 -= n4) {
            n4 = Player.calculateRequireExperience(++n3);
        }
        this.setExperience(n2, n3);
    }

    public static int calculateRequireExperience(int n) {
        if (n >= 30) {
            return 112 + (n - 30) * 9;
        }
        if (n >= 15) {
            return 37 + (n - 15) * 5;
        }
        return 7 + (n << 1);
    }

    public void setExperience(int n) {
        this.setExperience(n, this.S);
    }

    public void setExperience(int n, int n2) {
        PlayerExperienceChangeEvent playerExperienceChangeEvent = new PlayerExperienceChangeEvent(this, this.aa, this.S, n, n2);
        this.server.getPluginManager().callEvent(playerExperienceChangeEvent);
        if (playerExperienceChangeEvent.isCancelled()) {
            return;
        }
        this.aa = playerExperienceChangeEvent.getNewExperience();
        this.S = playerExperienceChangeEvent.getNewExperienceLevel();
        this.a(this.aa, this.S);
    }

    public void sendExperience() {
        this.sendExperience(this.aa);
    }

    public void sendExperience(int n) {
        block0: {
            if (!this.spawned) break block0;
            this.setAttribute(Attribute.getAttribute(10).setValue(Math.max(0.0f, Math.min(1.0f, (float)n / (float)Player.calculateRequireExperience(this.S)))));
        }
    }

    public void sendExperienceLevel() {
        this.sendExperienceLevel(this.S);
    }

    public void sendExperienceLevel(int n) {
        block0: {
            if (!this.spawned) break block0;
            this.setAttribute(Attribute.getAttribute(9).setValue(n));
        }
    }

    private void a(int n, int n2) {
        if (this.spawned) {
            UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
            updateAttributesPacket.entries = new Attribute[]{Attribute.getAttribute(9).setValue(n2), Attribute.getAttribute(10).setValue(Math.max(0.0f, Math.min(1.0f, (float)n / (float)Player.calculateRequireExperience(this.S))))};
            updateAttributesPacket.entityId = this.id;
            this.dataPacket(updateAttributesPacket);
        }
    }

    public void setAttribute(Attribute attribute) {
        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
        updateAttributesPacket.entries = new Attribute[]{attribute};
        updateAttributesPacket.entityId = this.id;
        this.dataPacket(updateAttributesPacket);
    }

    @Override
    public void setMovementSpeed(float f2) {
        this.setMovementSpeed(f2, true);
    }

    public void setMovementSpeed(float f2, boolean bl) {
        block0: {
            super.setMovementSpeed(f2);
            if (!this.spawned || !bl) break block0;
            this.setAttribute(Attribute.getAttribute(5).setValue(f2).setDefaultValue(f2));
        }
    }

    public void sendMovementSpeed(float f2) {
        Attribute attribute = Attribute.getAttribute(5).setValue(f2);
        this.setAttribute(attribute);
    }

    public Entity getKiller() {
        return this.ae;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        Cloneable cloneable;
        if (!this.isAlive()) {
            return false;
        }
        if (this.isSpectator() || this.isCreative() && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.SUICIDE) {
            entityDamageEvent.setCancelled();
            return false;
        }
        if (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FALL && this.getAllowFlight()) {
            entityDamageEvent.setCancelled();
            return false;
        }
        if (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FALL) {
            cloneable = this.getPosition().floor().add(0.5, -1.0, 0.5);
            int n = this.getLevel().getBlockIdAt(this.chunk, (int)cloneable.x, (int)cloneable.y, (int)cloneable.z);
            if (!(n != 165 && n != 30 || this.isSneaking())) {
                entityDamageEvent.setCancelled();
                this.resetFallDistance();
                return false;
            }
        }
        if (super.attack(entityDamageEvent)) {
            if (this.getLastDamageCause() == entityDamageEvent && this.spawned) {
                if (entityDamageEvent instanceof EntityDamageByEntityEvent && (cloneable = ((EntityDamageByEntityEvent)entityDamageEvent).getDamager()) instanceof Player) {
                    ((Player)cloneable).foodData.updateFoodExpLevel(0.1);
                }
                cloneable = new EntityEventPacket();
                ((EntityEventPacket)cloneable).eid = this.id;
                ((EntityEventPacket)cloneable).event = 2;
                this.dataPacket((DataPacket)cloneable);
            }
            return true;
        }
        return false;
    }

    public boolean dropItem(Item item) {
        block2: {
            if (!this.spawned || !this.isAlive()) {
                return false;
            }
            if (item.isNull()) {
                this.server.getLogger().debug(this.username + " attempted to drop a null item (" + item + ')');
                return true;
            }
            this.setUsingItem(false);
            Vector3 vector3 = this.getDirectionVector().multiply(0.4);
            EntityItem entityItem = this.level.dropAndGetItem(this.add(0.0, 1.3, 0.0), item, vector3, 40);
            if (entityItem == null) break block2;
            entityItem.droppedBy = this;
        }
        return true;
    }

    public EntityItem dropAndGetItem(Item item) {
        EntityItem entityItem;
        block2: {
            if (!this.spawned || !this.isAlive()) {
                return null;
            }
            if (item.isNull()) {
                this.server.getLogger().debug(this.getName() + " attempted to drop a null item (" + item + ')');
                return null;
            }
            this.setUsingItem(false);
            Vector3 vector3 = this.getDirectionVector().multiply(0.4);
            entityItem = this.level.dropAndGetItem(this.add(0.0, 1.3, 0.0), item, vector3, 40);
            if (entityItem == null) break block2;
            entityItem.droppedBy = this;
        }
        return entityItem;
    }

    public void sendPosition(Vector3 vector3) {
        this.sendPosition(vector3, this.yaw);
    }

    public void sendPosition(Vector3 vector3, double d2) {
        this.sendPosition(vector3, d2, this.pitch);
    }

    public void sendPosition(Vector3 vector3, double d2, double d3) {
        this.sendPosition(vector3, d2, d3, 0);
    }

    public void sendPosition(Vector3 vector3, double d2, double d3, int n) {
        this.sendPosition(vector3, d2, d3, n, null);
    }

    public void sendPosition(Vector3 vector3, double d2, double d3, int n, Player[] playerArray) {
        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
        movePlayerPacket.eid = this.getId();
        movePlayerPacket.x = (float)vector3.x;
        movePlayerPacket.y = (float)(vector3.y + (double)this.getBaseOffset());
        movePlayerPacket.z = (float)vector3.z;
        movePlayerPacket.headYaw = (float)d2;
        movePlayerPacket.pitch = (float)d3;
        movePlayerPacket.yaw = (float)d2;
        movePlayerPacket.mode = n;
        movePlayerPacket.onGround = this.onGround;
        if (this.riding != null) {
            movePlayerPacket.ridingEid = this.riding.getId();
            movePlayerPacket.mode = 3;
        }
        this.ySize = 0.0f;
        if (playerArray != null) {
            Server.broadcastPacket(playerArray, (DataPacket)movePlayerPacket);
        } else {
            this.dataPacket(movePlayerPacket);
        }
    }

    private void a(double d2, double d3, double d4, double d5, double d6, double d7) {
        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
        movePlayerPacket.eid = this.getId();
        movePlayerPacket.x = (float)d2;
        movePlayerPacket.y = (float)(d3 + (double)this.getBaseOffset());
        movePlayerPacket.z = (float)d4;
        movePlayerPacket.headYaw = (float)d7;
        movePlayerPacket.pitch = (float)d6;
        movePlayerPacket.yaw = (float)d5;
        movePlayerPacket.mode = 0;
        movePlayerPacket.onGround = this.onGround;
        if (this.riding != null) {
            movePlayerPacket.ridingEid = this.riding.getId();
            movePlayerPacket.mode = 3;
        }
        this.ySize = 0.0f;
        Server.broadcastPacket(this.getViewers().values(), (DataPacket)movePlayerPacket);
    }

    @Override
    protected void checkChunks() {
        if (this.chunk == null || this.chunk.getX() != this.getChunkX() || this.chunk.getZ() != this.getChunkZ()) {
            if (this.chunk != null) {
                this.chunk.removeEntity(this);
            }
            this.chunk = this.level.getChunk(this.getChunkX(), this.getChunkZ(), true);
            if (!this.justCreated) {
                Map<Integer, Player> map = this.level.getChunkPlayers(this.getChunkX(), this.getChunkZ());
                map.remove(this.P);
                for (Player player : new ArrayList(this.hasSpawned.values())) {
                    if (!map.containsKey(player.P)) {
                        this.despawnFrom(player);
                        continue;
                    }
                    map.remove(player.P);
                }
                for (Player player : map.values()) {
                    this.spawnTo(player);
                }
            }
            if (this.chunk == null) {
                return;
            }
            this.chunk.addEntity(this);
        }
    }

    protected boolean checkTeleportPosition() {
        return this.checkTeleportPosition(false);
    }

    protected boolean checkTeleportPosition(boolean bl) {
        if (this.teleportPosition != null) {
            int n = this.teleportPosition.getChunkX();
            int n2 = this.teleportPosition.getChunkZ();
            for (int k = -1; k <= 1; ++k) {
                for (int i2 = -1; i2 <= 1; ++i2) {
                    long l = Level.chunkHash(n + k, n2 + i2);
                    if (this.usedChunks.containsKey(l) && this.usedChunks.get(l).booleanValue()) continue;
                    return false;
                }
            }
            this.spawnToAll();
            if (!bl) {
                this.forceMovement = this.teleportPosition;
            }
            this.teleportPosition = null;
            return true;
        }
        return false;
    }

    protected void sendPlayStatus(int n) {
        this.sendPlayStatus(n, false);
    }

    protected void sendPlayStatus(int n, boolean bl) {
        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.status = n;
        if (bl) {
            this.directDataPacket(playStatusPacket);
        } else {
            this.dataPacket(playStatusPacket);
        }
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        if (!this.isOnline()) {
            return false;
        }
        Location location2 = this.getLocation();
        Location location3 = location;
        if (teleportCause != null) {
            PlayerTeleportEvent playerTeleportEvent = new PlayerTeleportEvent(this, location2, location3, teleportCause);
            this.server.getPluginManager().callEvent(playerTeleportEvent);
            if (playerTeleportEvent.isCancelled()) {
                return false;
            }
            location3 = playerTeleportEvent.getTo();
        }
        if (super.teleport(location3.getY() == (double)location3.getFloorY() ? location3.add(0.0, 1.0E-5, 0.0) : location3, null)) {
            this.removeAllWindows();
            this.Q = false;
            this.teleportPosition = this;
            if (teleportCause != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                this.forceMovement = this.teleportPosition;
            }
            if (this.K) {
                this.K = false;
            } else {
                this.sendPosition(this, this.yaw, this.pitch, 2);
                this.checkTeleportPosition(teleportCause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                this.dummyBossBars.values().forEach(DummyBossBar::reshow);
            }
            this.resetFallDistance();
            this.nextChunkOrderRun = 0;
            this.resetClientMovement();
            this.stopFishing(false);
            return true;
        }
        return false;
    }

    protected void forceSendEmptyChunks() {
        int n = this.getChunkX();
        int n2 = this.getChunkZ();
        for (int k = -this.chunkRadius; k < this.chunkRadius; ++k) {
            for (int i2 = -this.chunkRadius; i2 < this.chunkRadius; ++i2) {
                LevelChunkPacket levelChunkPacket = new LevelChunkPacket();
                levelChunkPacket.chunkX = n + k;
                levelChunkPacket.chunkZ = n2 + i2;
                levelChunkPacket.data = new byte[0];
                this.dataPacket(levelChunkPacket);
            }
        }
    }

    public void teleportImmediate(Location location) {
        this.teleportImmediate(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public void teleportImmediate(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        block0: {
            if (!super.teleport(location.getY() == (double)location.getFloorY() ? location.add(0.0, 1.0E-5, 0.0) : location, teleportCause)) break block0;
            this.removeAllWindows();
            this.Q = false;
            this.forceMovement = this;
            this.sendPosition(this, this.yaw, this.pitch, 1);
            this.resetFallDistance();
            this.orderChunks();
            this.nextChunkOrderRun = 0;
            this.resetClientMovement();
            this.stopFishing(false);
        }
    }

    public int showFormWindow(FormWindow formWindow) {
        return this.showFormWindow(formWindow, this.formWindowCount++);
    }

    public int showFormWindow(FormWindow formWindow, int n) {
        if (this.Q) {
            return 0;
        }
        ModalFormRequestPacket modalFormRequestPacket = new ModalFormRequestPacket();
        modalFormRequestPacket.formId = n;
        modalFormRequestPacket.data = formWindow.getJSONData();
        this.formWindows.put(modalFormRequestPacket.formId, formWindow);
        this.dataPacket(modalFormRequestPacket);
        this.Q = true;
        return n;
    }

    public int addServerSettings(FormWindow formWindow) {
        int n = this.formWindowCount++;
        this.serverSettings.put(n, formWindow);
        return n;
    }

    public long createBossBar(String string, int n) {
        return this.createBossBar(new DummyBossBar.Builder(this).text(string).length(n).build());
    }

    public long createBossBar(DummyBossBar dummyBossBar) {
        this.dummyBossBars.put(dummyBossBar.getBossBarId(), dummyBossBar);
        dummyBossBar.create();
        return dummyBossBar.getBossBarId();
    }

    public DummyBossBar getDummyBossBar(long l) {
        return this.dummyBossBars.getOrDefault(l, null);
    }

    public Map<Long, DummyBossBar> getDummyBossBars() {
        return this.dummyBossBars;
    }

    public void updateBossBar(String string, int n, long l) {
        if (this.dummyBossBars.containsKey(l)) {
            DummyBossBar dummyBossBar = this.dummyBossBars.get(l);
            dummyBossBar.setText(string);
            dummyBossBar.setLength(n);
        }
    }

    public void removeBossBar(long l) {
        block0: {
            if (!this.dummyBossBars.containsKey(l)) break block0;
            this.dummyBossBars.get(l).destroy();
            this.dummyBossBars.remove(l);
        }
    }

    public int getWindowId(Inventory inventory) {
        if (this.windows.containsKey(inventory)) {
            return (Integer)this.windows.get(inventory);
        }
        return -1;
    }

    public Inventory getWindowById(int n) {
        return (Inventory)this.windowIndex.get(n);
    }

    public int addWindow(Inventory inventory) {
        return this.addWindow(inventory, null);
    }

    public int addWindow(Inventory inventory, Integer n) {
        return this.addWindow(inventory, n, false);
    }

    public int addWindow(Inventory inventory, Integer n, boolean bl) {
        return this.addWindow(inventory, n, bl, false);
    }

    public int addWindow(Inventory inventory, Integer n, boolean bl, boolean bl2) {
        int n2;
        if (this.windows.containsKey(inventory)) {
            return (Integer)this.windows.get(inventory);
        }
        if (n == null) {
            this.windowCnt = n2 = Math.max(4, ++this.windowCnt % 99);
        } else {
            n2 = n;
        }
        this.windows.forcePut(inventory, n2);
        if (bl) {
            this.permanentWindows.add(n2);
        }
        if (this.spawned && inventory.open(this)) {
            return n2;
        }
        if (!bl2) {
            this.removeWindow(inventory);
            return -1;
        }
        inventory.getViewers().add(this);
        return n2;
    }

    public Optional<Inventory> getTopWindow() {
        for (Map.Entry entry : this.windows.entrySet()) {
            if (this.permanentWindows.contains(entry.getValue())) continue;
            return Optional.of(entry.getKey());
        }
        return Optional.empty();
    }

    public void removeWindow(Inventory inventory) {
        this.removeWindow(inventory, false);
    }

    protected void removeWindow(Inventory inventory, boolean bl) {
        block0: {
            inventory.close(this);
            if (this.permanentWindows.contains(this.getWindowId(inventory))) break block0;
            this.windows.remove(inventory);
        }
    }

    public void sendAllInventories() {
        for (Inventory inventory : this.windows.keySet()) {
            inventory.sendContents(this);
            if (!(inventory instanceof PlayerInventory)) continue;
            ((PlayerInventory)inventory).sendArmorContents(this);
        }
    }

    protected void addDefaultWindows() {
        this.addWindow(this.getInventory(), 0, true, true);
        this.playerUIInventory = new PlayerUIInventory(this);
        this.addWindow(this.playerUIInventory, 124, true);
        this.addWindow(this.offhandInventory, 119, true, true);
        this.craftingGrid = this.playerUIInventory.getCraftingGrid();
        this.addWindow(this.craftingGrid, -1);
    }

    public PlayerUIInventory getUIInventory() {
        return this.playerUIInventory;
    }

    public PlayerCursorInventory getCursorInventory() {
        return this.playerUIInventory.getCursorInventory();
    }

    public CraftingGrid getCraftingGrid() {
        return this.craftingGrid;
    }

    public void setCraftingGrid(CraftingGrid craftingGrid) {
        this.craftingGrid = craftingGrid;
        this.addWindow(craftingGrid, -1);
    }

    public void resetCraftingGridType() {
        if (this.playerUIInventory != null) {
            Item[] itemArray;
            if (this.craftingGrid != null) {
                itemArray = this.inventory.addItem(this.craftingGrid.getContents().values().toArray(new Item[0]));
                this.craftingGrid.clearAll();
                for (Item item : itemArray) {
                    this.level.dropItem(this, item);
                }
            }
            itemArray = this.inventory.addItem(this.playerUIInventory.getCursorInventory().getItemFast(0));
            this.playerUIInventory.getCursorInventory().clear(0);
            for (Item item : itemArray) {
                this.level.dropItem(this, item);
            }
            this.b(2);
            this.b(3);
            this.b(4);
            this.playerUIInventory.clearAll();
            if (this.craftingGrid instanceof BigCraftingGrid && this.connected) {
                this.craftingGrid = this.playerUIInventory.getCraftingGrid();
                this.addWindow(this.craftingGrid, -1);
            }
        }
        this.craftingType = 0;
    }

    private void b(int n) {
        Inventory inventory = this.getWindowById(n);
        if (inventory != null) {
            Item[] itemArray = this.inventory.addItem(inventory.getContents().values().toArray(new Item[0]));
            inventory.clearAll();
            for (Item item : itemArray) {
                this.level.dropItem(this, item);
            }
        }
    }

    public void removeAllWindows() {
        this.removeAllWindows(false);
    }

    public void removeAllWindows(boolean bl) {
        for (Map.Entry entry : new ArrayList(this.windowIndex.entrySet())) {
            if (!bl && this.permanentWindows.contains(entry.getKey())) continue;
            this.removeWindow((Inventory)entry.getValue());
        }
    }

    public int getClosingWindowId() {
        return this.closingWindowId;
    }

    @Override
    public void setMetadata(String string, MetadataValue metadataValue) {
        this.server.getPlayerMetadata().setMetadata(this, string, metadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String string) {
        return this.server.getPlayerMetadata().getMetadata(this, string);
    }

    @Override
    public boolean hasMetadata(String string) {
        return this.server.getPlayerMetadata().hasMetadata(this, string);
    }

    @Override
    public void removeMetadata(String string, Plugin plugin) {
        this.server.getPlayerMetadata().removeMetadata(this, string, plugin);
    }

    @Override
    public void onChunkChanged(FullChunk fullChunk) {
        this.usedChunks.remove(Level.chunkHash(fullChunk.getX(), fullChunk.getZ()));
    }

    @Override
    public void onChunkLoaded(FullChunk fullChunk) {
    }

    @Override
    public void onChunkPopulated(FullChunk fullChunk) {
    }

    @Override
    public void onChunkUnloaded(FullChunk fullChunk) {
    }

    @Override
    public void onBlockChanged(Vector3 vector3) {
    }

    @Override
    public int getLoaderId() {
        return this.P;
    }

    @Override
    public boolean isLoaderActive() {
        return this.connected;
    }

    public static BatchPacket getChunkCacheFromData(int n, int n2, int n3, int n4, byte[] byArray) {
        LevelChunkPacket levelChunkPacket = new LevelChunkPacket();
        levelChunkPacket.chunkX = n2;
        levelChunkPacket.chunkZ = n3;
        levelChunkPacket.subChunkCount = n4;
        levelChunkPacket.data = byArray;
        levelChunkPacket.protocol = n;
        levelChunkPacket.tryEncode();
        BatchPacket batchPacket = new BatchPacket();
        byte[][] byArrayArray = new byte[2][];
        byte[] byArray2 = levelChunkPacket.getBuffer();
        byArrayArray[0] = Binary.writeUnsignedVarInt(byArray2.length);
        byArrayArray[1] = byArray2;
        try {
            batchPacket.payload = n >= 407 ? Zlib.deflateRaw(Binary.appendBytes(byArrayArray), Server.getInstance().networkCompressionLevel) : Zlib.deflate(Binary.appendBytes(byArrayArray), Server.getInstance().networkCompressionLevel);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return batchPacket;
    }

    public boolean isFoodEnabled() {
        return !this.isCreative() && !this.isSpectator() && this.w;
    }

    public void setFoodEnabled(boolean bl) {
        this.w = bl;
    }

    public PlayerFood getFoodData() {
        return this.foodData;
    }

    public void setDimension(int n) {
        block1: {
            this.K = true;
            this.s = true;
            ChangeDimensionPacket changeDimensionPacket = new ChangeDimensionPacket();
            changeDimensionPacket.dimension = n;
            changeDimensionPacket.x = (float)this.x;
            changeDimensionPacket.y = (float)this.y;
            changeDimensionPacket.z = (float)this.z;
            changeDimensionPacket.respawn = !this.isAlive();
            this.dataPacket(changeDimensionPacket);
            if (this.protocol >= 313) {
                NetworkChunkPublisherUpdatePacket networkChunkPublisherUpdatePacket = new NetworkChunkPublisherUpdatePacket();
                networkChunkPublisherUpdatePacket.position = this.asBlockVector3();
                networkChunkPublisherUpdatePacket.radius = this.chunkRadius << 4;
                this.dataPacket(networkChunkPublisherUpdatePacket);
            }
            if (this.protocol < 560) break block1;
            this.A = true;
        }
    }

    @Override
    protected void preSwitchLevel() {
        this.networkSession.flush();
        this.a(true);
    }

    @Override
    protected void afterSwitchLevel() {
        SetSpawnPositionPacket setSpawnPositionPacket = new SetSpawnPositionPacket();
        setSpawnPositionPacket.spawnType = 1;
        Position position = this.level.getSpawnLocation();
        setSpawnPositionPacket.x = position.getFloorX();
        setSpawnPositionPacket.y = position.getFloorY();
        setSpawnPositionPacket.z = position.getFloorZ();
        setSpawnPositionPacket.dimension = this.level.getDimension();
        this.dataPacket(setSpawnPositionPacket);
        this.level.sendTime(this);
        this.level.sendWeather(this);
        GameRulesChangedPacket gameRulesChangedPacket = new GameRulesChangedPacket();
        gameRulesChangedPacket.gameRulesMap = this.level.getGameRules().getGameRules();
        this.dataPacket(gameRulesChangedPacket);
        this.L = 0;
    }

    public void setCheckMovement(boolean bl) {
        this.checkMovement = bl;
    }

    public boolean isCheckingMovement() {
        return this.checkMovement;
    }

    public synchronized void setLocale(Locale locale) {
        this.O.set(locale);
    }

    public synchronized Locale getLocale() {
        return this.O.get();
    }

    @Override
    public void setSprinting(boolean bl) {
        this.setSprinting(bl, true);
    }

    public void setSprinting(boolean bl, boolean bl2) {
        if (this.isSprinting() != bl) {
            super.setSprinting(bl);
            this.setMovementSpeed(bl ? this.getMovementSpeed() * 1.3f : this.getMovementSpeed() / 1.3f, bl2);
        }
    }

    public void transfer(InetSocketAddress inetSocketAddress) {
        this.transfer(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
    }

    public void transfer(String string, int n) {
        TransferPacket transferPacket = new TransferPacket();
        transferPacket.address = string;
        transferPacket.port = n;
        this.dataPacket(transferPacket);
    }

    public LoginChainData getLoginChainData() {
        return this.C;
    }

    public boolean pickupEntity(Entity entity, boolean bl) {
        Entity entity2;
        if (!this.spawned || !this.isAlive() || !this.isOnline() || this.isSpectator() || entity.isClosed()) {
            return false;
        }
        if (bl) {
            Item item;
            if (entity instanceof EntityArrow && ((EntityArrow)entity).hadCollision) {
                EntityArrow entityArrow = (EntityArrow)entity;
                ItemArrow itemArrow = (ItemArrow)Item.get(262, entityArrow.getData());
                if (!this.isCreative() && !this.inventory.canAddItem(itemArrow)) {
                    return false;
                }
                InventoryPickupArrowEvent inventoryPickupArrowEvent = new InventoryPickupArrowEvent(this.inventory, entityArrow);
                int n = entityArrow.getPickupMode();
                if (n == -1 || n == 0 || n == 2 && !this.isCreative()) {
                    inventoryPickupArrowEvent.setCancelled();
                }
                this.server.getPluginManager().callEvent(inventoryPickupArrowEvent);
                if (n == -1) {
                    entity.close();
                }
                if (inventoryPickupArrowEvent.isCancelled()) {
                    return false;
                }
                TakeItemEntityPacket takeItemEntityPacket = new TakeItemEntityPacket();
                takeItemEntityPacket.entityId = this.getId();
                takeItemEntityPacket.target = entity.getId();
                Server.broadcastPacket(entity.getViewers().values(), (DataPacket)takeItemEntityPacket);
                this.dataPacket(takeItemEntityPacket);
                if (!this.isCreative()) {
                    this.inventory.addItem(itemArrow);
                }
                entity.close();
                return true;
            }
            if (entity instanceof EntityThrownTrident) {
                if (!((EntityThrownTrident)entity).hadCollision) {
                    if (entity.noClip) {
                        if (!this.equals(((EntityProjectile)entity).shootingEntity)) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
                if (!((EntityThrownTrident)entity).shotByPlayer()) {
                    return false;
                }
                Item item2 = ((EntityThrownTrident)entity).getItem();
                if (!this.isCreative() && !this.inventory.canAddItem(item2)) {
                    return false;
                }
                InventoryPickupTridentEvent inventoryPickupTridentEvent = new InventoryPickupTridentEvent(this.inventory, (EntityThrownTrident)entity);
                int n = ((EntityThrownTrident)entity).getPickupMode();
                if (n == -1 || n == 0 || n == 2 && !this.isCreative()) {
                    inventoryPickupTridentEvent.setCancelled();
                }
                this.server.getPluginManager().callEvent(inventoryPickupTridentEvent);
                if (n == -1) {
                    entity.close();
                }
                if (inventoryPickupTridentEvent.isCancelled()) {
                    return false;
                }
                TakeItemEntityPacket takeItemEntityPacket = new TakeItemEntityPacket();
                takeItemEntityPacket.entityId = this.getId();
                takeItemEntityPacket.target = entity.getId();
                Server.broadcastPacket(entity.getViewers().values(), (DataPacket)takeItemEntityPacket);
                this.dataPacket(takeItemEntityPacket);
                int n2 = ((EntityThrownTrident)entity).getFavoredSlot();
                if (n2 != -1 && !this.isCreative() && this.inventory.getItemFast(n2).getId() == 0) {
                    this.inventory.setItem(n2, item2.clone());
                } else {
                    this.inventory.addItem(item2);
                }
                entity.close();
                return true;
            }
            if (entity instanceof EntityItem && ((EntityItem)(entity2 = (EntityItem)entity)).getPickupDelay() <= 0 && (item = ((EntityItem)entity2).getItem()) != null) {
                if (!this.isCreative() && !this.inventory.canAddItem(item)) {
                    return false;
                }
                InventoryPickupItemEvent inventoryPickupItemEvent = new InventoryPickupItemEvent(this.inventory, (EntityItem)entity2);
                this.server.getPluginManager().callEvent(inventoryPickupItemEvent);
                if (inventoryPickupItemEvent.isCancelled()) {
                    return false;
                }
                switch (item.getId()) {
                    case 17: 
                    case 162: {
                        this.awardAchievement("mineWood");
                        break;
                    }
                    case 264: {
                        this.awardAchievement("diamonds");
                        if (((EntityItem)entity2).droppedBy == null || ((EntityItem)entity2).droppedBy == this) break;
                        ((EntityItem)entity2).droppedBy.awardAchievement("diamondsToYou");
                        break;
                    }
                    case 334: {
                        this.awardAchievement("killCow");
                        break;
                    }
                    case 369: {
                        this.awardAchievement("blazeRod");
                    }
                }
                TakeItemEntityPacket takeItemEntityPacket = new TakeItemEntityPacket();
                takeItemEntityPacket.entityId = this.getId();
                takeItemEntityPacket.target = entity.getId();
                Server.broadcastPacket(entity.getViewers().values(), (DataPacket)takeItemEntityPacket);
                this.dataPacket(takeItemEntityPacket);
                this.inventory.addItem(item);
                entity.close();
                return true;
            }
        }
        if (this.pickedXPOrb < this.server.getTick() && entity instanceof EntityXPOrb && this.boundingBox.isVectorInside(entity) && ((EntityXPOrb)(entity2 = (EntityXPOrb)entity)).getPickupDelay() <= 0) {
            Item item;
            int n;
            int n3 = ((EntityXPOrb)entity2).getExp();
            entity.close();
            this.getLevel().addLevelEvent(this, 1051);
            this.pickedXPOrb = this.server.getTick();
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            for (n = 0; n < 4; ++n) {
                if (!this.inventory.getArmorItem(n).hasEnchantment(26)) continue;
                arrayList.add(this.inventory.getSize() + n);
            }
            if (this.inventory.getItemInHandFast().hasEnchantment(26)) {
                arrayList.add(this.inventory.getHeldItemIndex());
            }
            if (!arrayList.isEmpty() && ((item = this.inventory.getItem(n = ((Integer)arrayList.get(Utils.random.nextInt(arrayList.size()))).intValue())) instanceof ItemTool || item instanceof ItemArmor) && item.getDamage() > 0) {
                int n4 = item.getDamage() - 2;
                if (n4 < 0) {
                    n4 = 0;
                }
                item.setDamage(n4);
                this.inventory.setItem(n, item);
                return true;
            }
            this.addExperience(n3);
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this.q == 0 || this.q == 485) {
            this.q = 485 + (this.getUniqueId() != null ? this.getUniqueId().hashCode() : 0);
        }
        return this.q;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Player)) {
            return false;
        }
        Player player = (Player)object;
        return Objects.equals(this.getUniqueId(), player.getUniqueId()) && this.getId() == player.getId();
    }

    public boolean isBreakingBlock() {
        return this.breakingBlock != null;
    }

    public void showXboxProfile(String string) {
        ShowProfilePacket showProfilePacket = new ShowProfilePacket();
        showProfilePacket.xuid = string;
        this.dataPacket(showProfilePacket);
    }

    public void startFishing(Item item) {
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", this.x)).add(new DoubleTag("", this.y + (double)this.getEyeHeight())).add(new DoubleTag("", this.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", -Math.sin(this.yaw / 180.0 + Math.PI) * Math.cos(this.pitch / 180.0 * Math.PI))).add(new DoubleTag("", -Math.sin(this.pitch / 180.0 * Math.PI))).add(new DoubleTag("", Math.cos(this.yaw / 180.0 * Math.PI) * Math.cos(this.pitch / 180.0 * Math.PI)))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", (float)this.yaw)).add(new FloatTag("", (float)this.pitch)));
        double d2 = 1.1;
        EntityFishingHook entityFishingHook = new EntityFishingHook(this.chunk, compoundTag, this);
        entityFishingHook.setMotion(new Vector3(-Math.sin(FastMathLite.toRadians(this.yaw)) * Math.cos(FastMathLite.toRadians(this.pitch)) * d2 * d2, -Math.sin(FastMathLite.toRadians(this.pitch)) * d2 * d2, Math.cos(FastMathLite.toRadians(this.yaw)) * Math.cos(FastMathLite.toRadians(this.pitch)) * d2 * d2));
        ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(entityFishingHook);
        this.getServer().getPluginManager().callEvent(projectileLaunchEvent);
        if (projectileLaunchEvent.isCancelled()) {
            entityFishingHook.close();
        } else {
            this.fishing = entityFishingHook;
            entityFishingHook.rod = item;
            entityFishingHook.checkLure();
            entityFishingHook.spawnToAll();
        }
    }

    public void stopFishing(boolean bl) {
        if (this.fishing != null && bl) {
            this.fishing.reelLine();
        } else if (this.fishing != null) {
            this.fishing.close();
        }
        this.fishing = null;
    }

    @Override
    public boolean doesTriggerPressurePlate() {
        return this.gamemode != 3;
    }

    public int getTimeSinceRest() {
        return this.L;
    }

    public void setTimeSinceRest(int n) {
        this.L = n;
    }

    @Override
    public String toString() {
        return "Player(name='" + this.getName() + "', location=" + super.toString() + ')';
    }

    @Override
    public void setAirTicks(int n) {
        block0: {
            if (this.airTicks == n || !this.spawned && n <= this.airTicks) break block0;
            this.airTicks = n;
            this.setDataPropertyAndSendOnlyToSelf(new ShortEntityData(7, n));
        }
    }

    private void b() {
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.slot = this.inventory.getHeldItemIndex();
        inventorySlotPacket.item = this.inventory.getItem(inventorySlotPacket.slot);
        inventorySlotPacket.inventoryId = 0;
        this.dataPacket(inventorySlotPacket);
    }

    void c() {
        if (this.r) {
            this.r = false;
            this.adventureSettings.a(false);
        }
        if (this.R) {
            this.R = false;
            this.sendData(this);
        }
        if (this.Z) {
            this.Z = false;
            this.foodData.sendFoodLevel();
        }
        this.af = 0;
        this.J = 0;
        for (int k = 0; k < 256; ++k) {
            this.F[k] = 0;
        }
    }

    public boolean canEat(boolean bl) {
        block1: {
            if (this.foodData.getLevel() < this.foodData.getMaxLevel() || this.isCreative() || this.server.getDifficulty() == 0) {
                return true;
            }
            if (!bl || this.protocol <= 361) break block1;
            this.Z = true;
        }
        return false;
    }

    public boolean isMovementServerAuthoritative() {
        return this.protocol >= 440;
    }

    private boolean a(DataPacket dataPacket, int n, int n2) {
        int n3 = dataPacket.pid() & 0xFF;
        int n4 = this.F[n3];
        this.F[n3] = n4 + 1;
        if (n4 > n) {
            if (Nukkit.DEBUG > 1) {
                this.server.getLogger().debug(this.username + ": too many " + dataPacket.getClass().getSimpleName() + " (" + n4 + " > " + n + ")");
            }
            if (n2 > 0 && n4 > n2) {
                this.kick(PlayerKickEvent.Reason.INVALID_PACKET, "Too many packets", true, "pid=" + dataPacket.pid() + ", count=" + n4);
            }
            return true;
        }
        return false;
    }

    public NetworkPlayerSession getNetworkSession() {
        return this.networkSession;
    }

    public boolean isShowToOthers() {
        return this.M;
    }

    public void setShowToOthers(boolean bl) {
        this.M = bl;
    }

    public boolean isCanTickShield() {
        return this.n;
    }

    public void setCanTickShield(boolean bl) {
        this.n = bl;
    }

    public boolean isFormOpen() {
        return this.Q;
    }

    public int getRiptideTicks() {
        return this.riptideTicks;
    }

    static /* synthetic */ UUID access$000(Player player) {
        return player.uuid;
    }

    static /* synthetic */ LoginChainData access$100(Player player) {
        return player.C;
    }

    static /* synthetic */ Server access$200(Player player) {
        return player.server;
    }

    static {
        B = "Flying is not enabled on this server";
        aj = LogManager.getLogger(Player.class);
        N = new ByteArrayList(new byte[]{-1, 1, -63, 69, 113, 84, 8, -127, -100});
        W = new IntOpenHashSet(new int[]{1, 9, 24, 25, 26, 35, 40, 41, 42, 43, 57});
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}


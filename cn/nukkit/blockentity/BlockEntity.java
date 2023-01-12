/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.ChunkException;
import cn.nukkit.utils.LevelException;
import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.lang.reflect.Constructor;

public abstract class BlockEntity
extends Position {
    public static final String CHEST;
    public static final String ENDER_CHEST;
    public static final String FURNACE;
    public static final String SIGN;
    public static final String MOB_SPAWNER;
    public static final String ENCHANT_TABLE;
    public static final String SKULL;
    public static final String FLOWER_POT;
    public static final String BREWING_STAND;
    public static final String DAYLIGHT_DETECTOR;
    public static final String MUSIC;
    public static final String ITEM_FRAME;
    public static final String CAULDRON;
    public static final String BEACON;
    public static final String PISTON_ARM;
    public static final String MOVING_BLOCK;
    public static final String COMPARATOR;
    public static final String HOPPER;
    public static final String BED;
    public static final String JUKEBOX;
    public static final String SHULKER_BOX;
    public static final String BANNER;
    public static final String DROPPER;
    public static final String DISPENSER;
    public static final String BARREL;
    public static final String CAMPFIRE;
    public static final String BELL;
    public static final String LECTERN;
    public static final String BLAST_FURNACE;
    public static final String SMOKER;
    public static long count;
    private static final BiMap<String, Class<? extends BlockEntity>> a;
    public FullChunk chunk;
    public String name;
    public long id;
    public boolean movable;
    public boolean closed = false;
    public CompoundTag namedTag;
    protected Server server;
    protected Timing timing;

    public BlockEntity(FullChunk fullChunk, CompoundTag compoundTag) {
        if (fullChunk == null || fullChunk.getProvider() == null) {
            throw new ChunkException("Invalid garbage chunk given to BlockEntity");
        }
        this.timing = Timings.getBlockEntityTiming(this);
        this.server = fullChunk.getProvider().getLevel().getServer();
        this.chunk = fullChunk;
        this.setLevel(fullChunk.getProvider().getLevel());
        this.namedTag = compoundTag;
        this.name = "";
        this.id = count++;
        this.x = this.namedTag.getInt("x");
        this.y = this.namedTag.getInt("y");
        this.z = this.namedTag.getInt("z");
        this.movable = this.namedTag.getBoolean("isMovable", true);
        this.initBlockEntity();
        this.chunk.addBlockEntity(this);
        this.getLevel().addBlockEntity(this);
    }

    protected void initBlockEntity() {
    }

    public static BlockEntity createBlockEntity(String string, FullChunk fullChunk, CompoundTag compoundTag, Object ... objectArray) {
        BlockEntity blockEntity = null;
        if (a.containsKey(string)) {
            Class clazz = (Class)a.get(string);
            if (clazz == null) {
                return null;
            }
            for (Constructor<?> constructor : clazz.getConstructors()) {
                if (blockEntity == null) {
                    if (constructor.getParameterCount() != (objectArray == null ? 2 : objectArray.length + 2)) continue;
                    try {
                        if (objectArray == null || objectArray.length == 0) {
                            blockEntity = (BlockEntity)constructor.newInstance(fullChunk, compoundTag);
                            continue;
                        }
                        Object[] objectArray2 = new Object[objectArray.length + 2];
                        objectArray2[0] = fullChunk;
                        objectArray2[1] = compoundTag;
                        System.arraycopy(objectArray, 0, objectArray2, 2, objectArray.length);
                        blockEntity = (BlockEntity)constructor.newInstance(objectArray2);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    continue;
                }
                break;
            }
        } else {
            Server.getInstance().getLogger().warning("Tried to create block entity that doesn't exists: " + string);
        }
        return blockEntity;
    }

    public static boolean registerBlockEntity(String string, Class<? extends BlockEntity> clazz) {
        if (clazz == null) {
            return false;
        }
        a.put(string, clazz);
        return true;
    }

    public final String getSaveId() {
        return (String)a.inverse().get(this.getClass());
    }

    public long getId() {
        return this.id;
    }

    public void saveNBT() {
        this.namedTag.putString("id", this.getSaveId());
        this.namedTag.putInt("x", (int)this.getX());
        this.namedTag.putInt("y", (int)this.getY());
        this.namedTag.putInt("z", (int)this.getZ());
        this.namedTag.putBoolean("isMovable", this.movable);
    }

    public CompoundTag getCleanedNBT() {
        this.saveNBT();
        CompoundTag compoundTag = this.namedTag.clone();
        compoundTag.remove("x").remove("y").remove("z").remove("id");
        if (!compoundTag.getTags().isEmpty()) {
            return compoundTag;
        }
        return null;
    }

    public Block getBlock() {
        return this.getLevelBlock();
    }

    @Override
    public Block getLevelBlock() {
        if (this.isValid()) {
            return this.level.getBlock(this.chunk, this.getFloorX(), this.getFloorY(), this.getFloorZ(), true);
        }
        throw new LevelException("Undefined Level reference");
    }

    public abstract boolean isBlockEntityValid();

    public boolean onUpdate() {
        return false;
    }

    public final void scheduleUpdate() {
        this.level.scheduleBlockEntityUpdate(this);
    }

    public void close() {
        if (!this.closed) {
            this.closed = true;
            if (this.chunk != null) {
                this.chunk.removeBlockEntity(this);
            }
            if (this.level != null) {
                this.level.removeBlockEntity(this);
            }
            this.level = null;
        }
    }

    public void onBreak() {
    }

    public void setDirty() {
        this.chunk.setChanged();
    }

    public String getName() {
        return this.name;
    }

    public boolean isMovable() {
        return this.movable;
    }

    public static CompoundTag getDefaultCompound(Vector3 vector3, String string) {
        return new CompoundTag().putString("id", string).putInt("x", vector3.getFloorX()).putInt("y", vector3.getFloorY()).putInt("z", vector3.getFloorZ());
    }

    static {
        DAYLIGHT_DETECTOR = "DaylightDetector";
        FLOWER_POT = "FlowerPot";
        JUKEBOX = "Jukebox";
        CAMPFIRE = "Campfire";
        SMOKER = "Smoker";
        DROPPER = "Dropper";
        ITEM_FRAME = "ItemFrame";
        ENCHANT_TABLE = "EnchantTable";
        DISPENSER = "Dispenser";
        BEACON = "Beacon";
        PISTON_ARM = "PistonArm";
        BLAST_FURNACE = "BlastFurnace";
        FURNACE = "Furnace";
        CAULDRON = "Cauldron";
        SKULL = "Skull";
        COMPARATOR = "Comparator";
        MUSIC = "Music";
        MOVING_BLOCK = "MovingBlock";
        SIGN = "Sign";
        BELL = "Bell";
        BARREL = "Barrel";
        BED = "Bed";
        MOB_SPAWNER = "MobSpawner";
        BREWING_STAND = "BrewingStand";
        CHEST = "Chest";
        HOPPER = "Hopper";
        BANNER = "Banner";
        ENDER_CHEST = "EnderChest";
        SHULKER_BOX = "ShulkerBox";
        LECTERN = "Lectern";
        count = 1L;
        a = HashBiMap.create(30);
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}


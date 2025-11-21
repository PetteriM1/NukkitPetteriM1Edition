package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.mob.EntityBlaze;
import cn.nukkit.entity.mob.EntityMob;
import cn.nukkit.entity.mob.EntitySilverfish;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.utils.Utils;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BlockEntitySpawner extends BlockEntitySpawnable {

    private int entityId;
    private int spawnRange;
    private int maxNearbyEntities;
    private int requiredPlayerRange;
    private int requiredPlayerRangeSquared;

    private int delay = 0;

    private int minSpawnDelay;
    private int maxSpawnDelay;

    private int minSpawnCount;
    private int maxSpawnCount;

    public static final String TAG_ID = "id";
    public static final String TAG_X = "x";
    public static final String TAG_Y = "y";
    public static final String TAG_Z = "z";
    public static final String TAG_ENTITY_ID = "EntityId";
    public static final String TAG_SPAWN_RANGE = "SpawnRange";
    public static final String TAG_MIN_SPAWN_DELAY = "MinSpawnDelay";
    public static final String TAG_MAX_SPAWN_DELAY = "MaxSpawnDelay";
    public static final String TAG_MAX_NEARBY_ENTITIES = "MaxNearbyEntities";
    public static final String TAG_REQUIRED_PLAYER_RANGE = "RequiredPlayerRange";
    public static final String TAG_MINIMUM_SPAWN_COUNT = "MinimumSpawnerCount";
    public static final String TAG_MAXIMUM_SPAWN_COUNT = "MaximumSpawnerCount";

    public static final short SPAWN_RANGE = 4;
    public static final short MIN_SPAWN_DELAY = 200;
    public static final short MAX_SPAWN_DELAY = 5000;
    public static final short MAX_NEARBY_ENTITIES = 16;
    public static final short REQUIRED_PLAYER_RANGE = 16;
    public static final short MINIMUM_SPAWN_COUNT = 1;
    public static final short MAXIMUM_SPAWN_COUNT = 4;

    public BlockEntitySpawner(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag tag = new CompoundTag()
                .putString(TAG_ID, BlockEntity.MOB_SPAWNER)
                .putInt(TAG_X, (int) this.x)
                .putInt(TAG_Y, (int) this.y)
                .putInt(TAG_Z, (int) this.z);
        if (this.entityId != 0) {
            tag.putInt(TAG_ENTITY_ID, this.entityId);
        }
        return tag;
    }

    public int getSpawnEntityType() {
        return this.entityId;
    }

    public void setSpawnEntityType(int entityId) {
        this.entityId = entityId;
        setDirty();
        this.spawnToAll();
    }

    @Override
    protected void initBlockEntity() {
        Tag vanillaId = this.namedTag.get("EntityIdentifier");
        if (vanillaId instanceof StringTag) {
            for (Map.Entry<Integer, String> entry : AddEntityPacket.LEGACY_IDS.entrySet()) {
                if (entry.getValue().equals(vanillaId.parseValue())) {
                    this.namedTag.putInt(TAG_ENTITY_ID, entry.getKey());
                    this.namedTag.remove("EntityIdentifier");
                    vanillaId = null; // Found
                    break;
                }
            }
            if (vanillaId != null) {
                server.getLogger().warning("BlockEntitySpawner: Couldn't find entity id for " + vanillaId.parseValue());
            }
        }

        this.entityId = this.namedTag.getInt(TAG_ENTITY_ID);

        if (!this.namedTag.contains(TAG_SPAWN_RANGE) || !(this.namedTag.get(TAG_SPAWN_RANGE) instanceof ShortTag)) {
            this.namedTag.putShort(TAG_SPAWN_RANGE, SPAWN_RANGE);
        }

        if (!this.namedTag.contains(TAG_MIN_SPAWN_DELAY) || !(this.namedTag.get(TAG_MIN_SPAWN_DELAY) instanceof ShortTag)) {
            this.namedTag.putShort(TAG_MIN_SPAWN_DELAY, MIN_SPAWN_DELAY);
        }

        if (!this.namedTag.contains(TAG_MAX_SPAWN_DELAY) || !(this.namedTag.get(TAG_MAX_SPAWN_DELAY) instanceof ShortTag)) {
            this.namedTag.putShort(TAG_MAX_SPAWN_DELAY, MAX_SPAWN_DELAY);
        }

        if (!this.namedTag.contains(TAG_MAX_NEARBY_ENTITIES) || !(this.namedTag.get(TAG_MAX_NEARBY_ENTITIES) instanceof ShortTag)) {
            this.namedTag.putShort(TAG_MAX_NEARBY_ENTITIES, MAX_NEARBY_ENTITIES);
        }

        if (!this.namedTag.contains(TAG_REQUIRED_PLAYER_RANGE) || !(this.namedTag.get(TAG_REQUIRED_PLAYER_RANGE) instanceof ShortTag)) {
            this.namedTag.putShort(TAG_REQUIRED_PLAYER_RANGE, REQUIRED_PLAYER_RANGE);
        }

        if (!this.namedTag.contains(TAG_MINIMUM_SPAWN_COUNT) || !(this.namedTag.get(TAG_MINIMUM_SPAWN_COUNT) instanceof ShortTag)) {
            this.namedTag.putShort(TAG_MINIMUM_SPAWN_COUNT, MINIMUM_SPAWN_COUNT);
        }

        if (!this.namedTag.contains(TAG_MAXIMUM_SPAWN_COUNT) || !(this.namedTag.get(TAG_MAXIMUM_SPAWN_COUNT) instanceof ShortTag)) {
            this.namedTag.putShort(TAG_MAXIMUM_SPAWN_COUNT, MAXIMUM_SPAWN_COUNT);
        }

        this.spawnRange = this.namedTag.getShort(TAG_SPAWN_RANGE);
        this.minSpawnDelay = this.namedTag.getShort(TAG_MIN_SPAWN_DELAY);
        this.maxSpawnDelay = this.namedTag.getShort(TAG_MAX_SPAWN_DELAY);
        this.maxNearbyEntities = this.namedTag.getShort(TAG_MAX_NEARBY_ENTITIES);
        this.requiredPlayerRange = this.namedTag.getShort(TAG_REQUIRED_PLAYER_RANGE);
        this.requiredPlayerRangeSquared = (int) Math.pow(this.requiredPlayerRange, 2);
        this.minSpawnCount = this.namedTag.getShort(TAG_MINIMUM_SPAWN_COUNT);
        this.maxSpawnCount = this.namedTag.getShort(TAG_MAXIMUM_SPAWN_COUNT);

        this.scheduleUpdate();
        super.initBlockEntity();
    }

    @Override
    public boolean isBlockEntityValid() {
        return level.getBlockIdAt(chunk, (int) x, (int) y, (int) z) == Block.MONSTER_SPAWNER;
    }

    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }

        if (this.delay++ >= Utils.rand(this.minSpawnDelay, this.maxSpawnDelay)) {
            this.delay = 0;

            int nearbyEntities = 0;
            boolean playerInRange = false;
            for (Entity entity : this.level.getEntitiesList()) {
                if (!playerInRange && entity instanceof Player && !((Player) entity).isSpectator()) {
                    if (entity.distanceSquared(this) <= this.requiredPlayerRangeSquared) {
                        playerInRange = true;
                    }
                } else if (entity instanceof BaseEntity || (server.suomiCraftPEMode() && entity instanceof EntityItem)) { //SCPE: dropped items included
                    if (entity.distanceSquared(this) <= this.requiredPlayerRangeSquared) {
                        nearbyEntities++;
                    }
                }
            }

            int amountToSpawn = minSpawnCount + ThreadLocalRandom.current().nextInt(maxSpawnCount);
            for (int i = 0; i < amountToSpawn; i++) {
                if (playerInRange && nearbyEntities <= this.maxNearbyEntities) {
                    Position pos = new Position
                            (
                                    this.x + Utils.rand(-this.spawnRange, this.spawnRange),
                                    this.y,
                                    this.z + Utils.rand(-this.spawnRange, this.spawnRange),
                                    this.level
                            );
                    Block block = level.getBlock(pos);
                    // Mobs shouldn't spawn in walls, and they shouldn't retry to
                    if (
                            block.getId() != 0 && block.getId() != BlockID.SIGN_POST && block.getId() != BlockID.WALL_SIGN &&
                                    block.getId() != BlockID.STILL_WATER && block.getId() != BlockID.WATER &&
                                    block.getId() != BlockID.LAVA && block.getId() != BlockID.STILL_LAVA
                    ) {
                        continue;
                    }

                    CreatureSpawnEvent ev = new CreatureSpawnEvent(this.entityId, pos, Entity.getDefaultNBT(pos), CreatureSpawnEvent.SpawnReason.SPAWNER);
                    level.getServer().getPluginManager().callEvent(ev);

                    if (ev.isCancelled()) {
                        continue;
                    }

                    CompoundTag nbt = ev.getCompoundTag();
                    if (!pos.equals(ev.getPosition())) {
                        nbt.putList(new ListTag<DoubleTag>("Pos")
                                .add(new DoubleTag("", ev.getPosition().x))
                                .add(new DoubleTag("", ev.getPosition().y))
                                .add(new DoubleTag("", ev.getPosition().z)));
                    }

                    Entity entity = Entity.createEntity(this.entityId, ev.getPosition().getChunk(), nbt);
                    if (entity != null) {
                        if (entity instanceof EntityMob &&
                                (this.level.getBlockLightAt((int) x, (int) y, (int) z) > ((entity instanceof EntitySilverfish || entity instanceof EntityBlaze) ? 12 : 0) ||
                                        (!level.isMobSpawningAllowedByTime() && level.canBlockSeeSky(this.getSideVec(BlockFace.UP))))) {
                            entity.close();
                            break;
                        }
                        entity.spawnToAll();
                        nearbyEntities++;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putInt(TAG_ENTITY_ID, this.entityId);
        this.namedTag.putString(TAG_ID, "MobSpawner");
        this.namedTag.putShort(TAG_SPAWN_RANGE, this.spawnRange);
        this.namedTag.putShort(TAG_MIN_SPAWN_DELAY, this.minSpawnDelay);
        this.namedTag.putShort(TAG_MAX_SPAWN_DELAY, this.maxSpawnDelay);
        this.namedTag.putShort(TAG_MAX_NEARBY_ENTITIES, this.maxNearbyEntities);
        this.namedTag.putShort(TAG_REQUIRED_PLAYER_RANGE, this.requiredPlayerRange);
        this.namedTag.putShort(TAG_MINIMUM_SPAWN_COUNT, this.minSpawnCount);
        this.namedTag.putShort(TAG_MAXIMUM_SPAWN_COUNT, this.maxSpawnCount);
    }

    public void setMaxNearbyEntities(int count) {
        this.maxNearbyEntities = count;
        setDirty();
    }

    public void setMaxSpawnDelay(int maxDelay) {
        if (this.minSpawnDelay > maxDelay) {
            return;
        }

        this.maxSpawnDelay = maxDelay;
        setDirty();
    }

    public void setMinSpawnDelay(int minDelay) {
        if (minDelay > this.maxSpawnDelay) {
            return;
        }

        this.minSpawnDelay = minDelay;
        setDirty();
    }

    public void setRequiredPlayerRange(int range) {
        this.requiredPlayerRange = range;
        this.requiredPlayerRangeSquared = (int) Math.pow(this.requiredPlayerRange, 2);
        setDirty();
    }

    public void setSpawnDelay(int minDelay, int maxDelay) {
        if (minDelay > maxDelay) {
            return;
        }

        this.minSpawnDelay = minDelay;
        this.maxSpawnDelay = maxDelay;
        setDirty();
    }
}

/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.mob.EntityMob;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ShortTag;
import cn.nukkit.utils.Utils;

public class BlockEntitySpawner
extends BlockEntitySpawnable {
    private int b = this.namedTag.getInt("EntityId");
    private int g;
    private int c;
    private int e;
    private int i;
    private int k = 0;
    private int f;
    private int j;
    private int d;
    private int h;
    public static final String TAG_ID;
    public static final String TAG_X = "x";
    public static final String TAG_Y = "y";
    public static final String TAG_Z = "z";
    public static final String TAG_ENTITY_ID;
    public static final String TAG_SPAWN_RANGE;
    public static final String TAG_MIN_SPAWN_DELAY;
    public static final String TAG_MAX_SPAWN_DELAY;
    public static final String TAG_MAX_NEARBY_ENTITIES;
    public static final String TAG_REQUIRED_PLAYER_RANGE;
    public static final String TAG_MINIMUM_SPAWN_COUNT;
    public static final String TAG_MAXIMUM_SPAWN_COUNT;
    public static final short SPAWN_RANGE = 4;
    public static final short MIN_SPAWN_DELAY = 200;
    public static final short MAX_SPAWN_DELAY = 5000;
    public static final short MAX_NEARBY_ENTITIES = 16;
    public static final short REQUIRED_PLAYER_RANGE = 16;
    public static final short MINIMUM_SPAWN_COUNT = 1;
    public static final short MAXIMUM_SPAWN_COUNT = 4;

    public BlockEntitySpawner(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        if (!this.namedTag.contains("SpawnRange") || !(this.namedTag.get("SpawnRange") instanceof ShortTag)) {
            this.namedTag.putShort("SpawnRange", 4);
        }
        if (!this.namedTag.contains("MinSpawnDelay") || !(this.namedTag.get("MinSpawnDelay") instanceof ShortTag)) {
            this.namedTag.putShort("MinSpawnDelay", 200);
        }
        if (!this.namedTag.contains("MaxSpawnDelay") || !(this.namedTag.get("MaxSpawnDelay") instanceof ShortTag)) {
            this.namedTag.putShort("MaxSpawnDelay", 5000);
        }
        if (!this.namedTag.contains("MaxNearbyEntities") || !(this.namedTag.get("MaxNearbyEntities") instanceof ShortTag)) {
            this.namedTag.putShort("MaxNearbyEntities", 16);
        }
        if (!this.namedTag.contains("RequiredPlayerRange") || !(this.namedTag.get("RequiredPlayerRange") instanceof ShortTag)) {
            this.namedTag.putShort("RequiredPlayerRange", 16);
        }
        if (!this.namedTag.contains("MinimumSpawnerCount") || !(this.namedTag.get("MinimumSpawnerCount") instanceof ShortTag)) {
            this.namedTag.putShort("MinimumSpawnerCount", 1);
        }
        if (!this.namedTag.contains("MaximumSpawnerCount") || !(this.namedTag.get("MaximumSpawnerCount") instanceof ShortTag)) {
            this.namedTag.putShort("MaximumSpawnerCount", 4);
        }
        this.g = this.namedTag.getShort("SpawnRange");
        this.f = this.namedTag.getShort("MinSpawnDelay");
        this.j = this.namedTag.getShort("MaxSpawnDelay");
        this.c = this.namedTag.getShort("MaxNearbyEntities");
        this.e = this.namedTag.getShort("RequiredPlayerRange");
        this.i = (int)Math.pow(this.e, 2.0);
        this.d = this.namedTag.getShort("MinimumSpawnerCount");
        this.h = this.namedTag.getShort("MaximumSpawnerCount");
        this.scheduleUpdate();
        super.initBlockEntity();
    }

    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }
        if (this.k++ >= Utils.rand(this.f, this.j)) {
            Position position;
            this.k = 0;
            int n = 0;
            boolean bl = false;
            Entity[] entityArray = this.level.getEntities();
            int n2 = entityArray.length;
            for (int k = 0; k < n2; ++k) {
                position = entityArray[k];
                if (!bl && position instanceof Player) {
                    if (!(position.distanceSquared(this) <= (double)this.i)) continue;
                    bl = true;
                    continue;
                }
                if (!(position instanceof BaseEntity) && (!this.server.suomiCraftPEMode() || !(position instanceof EntityItem)) || !(position.distanceSquared(this) <= (double)this.i)) continue;
                ++n;
            }
            int n3 = this.d + Utils.nukkitRandom.nextBoundedInt(this.h);
            for (n2 = 0; n2 < n3; ++n2) {
                Entity entity;
                Position position2;
                if (!bl || n > this.c || ((Block)(position = this.level.getBlock(position2 = new Position(this.x + (double)Utils.rand(-this.g, this.g), this.y, this.z + (double)Utils.rand(-this.g, this.g), this.level)))).getId() != 0 && ((Block)position).getId() != 63 && ((Block)position).getId() != 68 && ((Block)position).getId() != 9 && ((Block)position).getId() != 8 && ((Block)position).getId() != 10 && ((Block)position).getId() != 11) continue;
                CreatureSpawnEvent creatureSpawnEvent = new CreatureSpawnEvent(this.b, position2, CreatureSpawnEvent.SpawnReason.SPAWNER);
                this.level.getServer().getPluginManager().callEvent(creatureSpawnEvent);
                if (creatureSpawnEvent.isCancelled() || (entity = Entity.createEntity(this.b, position2, new Object[0])) == null) continue;
                if (entity instanceof EntityMob && this.level.getBlockLightAt((int)this.x, (int)this.y, (int)this.z) > 3) {
                    entity.close();
                    continue;
                }
                entity.spawnToAll();
                ++n;
            }
        }
        return true;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("EntityId", this.b);
        this.namedTag.putString("id", "MobSpawner");
        this.namedTag.putShort("SpawnRange", this.g);
        this.namedTag.putShort("MinSpawnDelay", this.f);
        this.namedTag.putShort("MaxSpawnDelay", this.j);
        this.namedTag.putShort("MaxNearbyEntities", this.c);
        this.namedTag.putShort("RequiredPlayerRange", this.e);
        this.namedTag.putShort("MinimumSpawnerCount", this.d);
        this.namedTag.putShort("MaximumSpawnerCount", this.h);
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return new CompoundTag().putString("id", "MobSpawner").putInt("EntityId", this.b).putInt(TAG_X, (int)this.x).putInt(TAG_Y, (int)this.y).putInt(TAG_Z, (int)this.z);
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 52;
    }

    public void setSpawnEntityType(int n) {
        this.b = n;
        this.spawnToAll();
    }

    public int getSpawnEntityType() {
        return this.b;
    }

    public void setMinSpawnDelay(int n) {
        if (n > this.j) {
            return;
        }
        this.f = n;
    }

    public void setMaxSpawnDelay(int n) {
        if (this.f > n) {
            return;
        }
        this.j = n;
    }

    public void setSpawnDelay(int n, int n2) {
        if (n > n2) {
            return;
        }
        this.f = n;
        this.j = n2;
    }

    public void setRequiredPlayerRange(int n) {
        this.e = n;
        this.i = (int)Math.pow(this.e, 2.0);
    }

    public void setMaxNearbyEntities(int n) {
        this.c = n;
    }

    static {
        TAG_MIN_SPAWN_DELAY = "MinSpawnDelay";
        TAG_MINIMUM_SPAWN_COUNT = "MinimumSpawnerCount";
        TAG_SPAWN_RANGE = "SpawnRange";
        TAG_MAX_SPAWN_DELAY = "MaxSpawnDelay";
        TAG_MAX_NEARBY_ENTITIES = "MaxNearbyEntities";
        TAG_REQUIRED_PLAYER_RANGE = "RequiredPlayerRange";
        TAG_MAXIMUM_SPAWN_COUNT = "MaximumSpawnerCount";
        TAG_ID = "id";
        TAG_ENTITY_ID = "EntityId";
    }
}


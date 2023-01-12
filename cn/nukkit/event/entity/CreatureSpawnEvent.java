/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.level.Position;
import cn.nukkit.nbt.tag.CompoundTag;

public class CreatureSpawnEvent
extends Event
implements Cancellable {
    private static final HandlerList f = new HandlerList();
    private final SpawnReason c;
    private final int b;
    private final Position d;
    private final CompoundTag e;

    public static HandlerList getHandlers() {
        return f;
    }

    public CreatureSpawnEvent(int n, SpawnReason spawnReason) {
        this(n, new Position(), new CompoundTag(), spawnReason);
    }

    public CreatureSpawnEvent(int n, Position position, SpawnReason spawnReason) {
        this(n, position, new CompoundTag(), spawnReason);
    }

    public CreatureSpawnEvent(int n, Position position, CompoundTag compoundTag, SpawnReason spawnReason) {
        this.c = spawnReason;
        this.b = n;
        this.d = position;
        this.e = compoundTag;
    }

    public SpawnReason getReason() {
        return this.c;
    }

    public int getEntityNetworkId() {
        return this.b;
    }

    public CompoundTag getCompoundTag() {
        return this.e;
    }

    public Position getPosition() {
        return this.d;
    }

    public static enum SpawnReason {
        NATURAL,
        JOCKEY,
        SPAWNER,
        EGG,
        SPAWN_EGG,
        LIGHTNING,
        BUILD_SNOWMAN,
        BUILD_IRONGOLEM,
        BUILD_WITHER,
        VILLAGE_DEFENSE,
        VILLAGE_INVASION,
        BREEDING,
        SLIME_SPLIT,
        REINFORCEMENTS,
        NETHER_PORTAL,
        DISPENSE_EGG,
        INFECTION,
        CURED,
        OCELOT_BABY,
        SILVERFISH_BLOCK,
        MOUNT,
        TRAP,
        ENDER_PEARL,
        SHOULDER_ENTITY,
        DROWNED,
        SHEARED,
        CUSTOM,
        DEFAULT;

    }
}


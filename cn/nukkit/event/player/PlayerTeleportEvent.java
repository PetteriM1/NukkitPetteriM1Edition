/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;

public class PlayerTeleportEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private TeleportCause e;
    private Location d;
    private Location b;

    public static HandlerList getHandlers() {
        return c;
    }

    private PlayerTeleportEvent(Player player) {
        this.player = player;
    }

    public PlayerTeleportEvent(Player player, Location location, Location location2, TeleportCause teleportCause) {
        this(player);
        this.d = location;
        this.b = location2;
        this.e = teleportCause;
    }

    public PlayerTeleportEvent(Player player, Vector3 vector3, Vector3 vector32, TeleportCause teleportCause) {
        this(player);
        this.d = PlayerTeleportEvent.a(player.getLevel(), vector3);
        this.d = PlayerTeleportEvent.a(player.getLevel(), vector32);
        this.e = teleportCause;
    }

    public Location getFrom() {
        return this.d;
    }

    public Location getTo() {
        return this.b;
    }

    public TeleportCause getCause() {
        return this.e;
    }

    private static Location a(Level level, Vector3 vector3) {
        if (vector3 instanceof Location) {
            return (Location)vector3;
        }
        if (vector3 instanceof Position) {
            return ((Position)vector3).getLocation();
        }
        return new Location(vector3.getX(), vector3.getY(), vector3.getZ(), 0.0, 0.0, level);
    }

    public static enum TeleportCause {
        COMMAND,
        PLUGIN,
        NETHER_PORTAL,
        END_PORTAL,
        ENDER_PEARL,
        CHORUS_FRUIT,
        UNKNOWN;

    }
}


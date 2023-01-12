/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.level.Location;

public class PlayerMoveEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private Location e;
    private Location b;
    private boolean d;

    public static HandlerList getHandlers() {
        return c;
    }

    public PlayerMoveEvent(Player player, Location location, Location location2) {
        this(player, location, location2, true);
    }

    public PlayerMoveEvent(Player player, Location location, Location location2, boolean bl) {
        this.player = player;
        this.e = location;
        this.b = location2;
        this.d = bl;
    }

    public Location getFrom() {
        return this.e;
    }

    public void setFrom(Location location) {
        this.e = location;
    }

    public Location getTo() {
        return this.b;
    }

    public void setTo(Location location) {
        this.b = location;
    }

    public boolean isResetBlocksAround() {
        return this.d;
    }

    public void setResetBlocksAround(boolean bl) {
        this.d = bl;
    }
}


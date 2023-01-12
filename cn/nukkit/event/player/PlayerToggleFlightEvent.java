/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerToggleFlightEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected final boolean isFlying;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerToggleFlightEvent(Player player, boolean bl) {
        this.player = player;
        this.isFlying = bl;
    }

    public boolean isFlying() {
        return this.isFlying;
    }
}


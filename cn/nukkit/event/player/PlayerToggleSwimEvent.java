/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerToggleSwimEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final boolean b;

    public static HandlerList getHandlers() {
        return c;
    }

    public PlayerToggleSwimEvent(Player player, boolean bl) {
        this.player = player;
        this.b = bl;
    }

    public boolean isSwimming() {
        return this.b;
    }
}


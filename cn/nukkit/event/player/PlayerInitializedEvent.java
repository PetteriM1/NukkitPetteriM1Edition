/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerInitializedEvent
extends PlayerEvent {
    private static final HandlerList b = new HandlerList();

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerInitializedEvent(Player player) {
        this.player = player;
    }
}


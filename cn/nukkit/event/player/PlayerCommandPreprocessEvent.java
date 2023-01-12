/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerMessageEvent;

public class PlayerCommandPreprocessEvent
extends PlayerMessageEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerCommandPreprocessEvent(Player player, String string) {
        this.player = player;
        this.message = string;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}


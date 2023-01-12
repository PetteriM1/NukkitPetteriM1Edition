/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerInvalidMoveEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private boolean c;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerInvalidMoveEvent(Player player, boolean bl) {
        this.player = player;
        this.c = bl;
    }

    public boolean isRevert() {
        return this.c;
    }

    public void setRevert(boolean bl) {
        this.c = bl;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerChunkRequestEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final int d;
    private final int c;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerChunkRequestEvent(Player player, int n, int n2) {
        this.player = player;
        this.d = n;
        this.c = n2;
    }

    public int getChunkX() {
        return this.d;
    }

    public int getChunkZ() {
        return this.c;
    }
}


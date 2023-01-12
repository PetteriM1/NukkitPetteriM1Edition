/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.level.Position;

public class PlayerRespawnEvent
extends PlayerEvent {
    private static final HandlerList d = new HandlerList();
    private Position c;
    private final boolean b;

    public static HandlerList getHandlers() {
        return d;
    }

    public PlayerRespawnEvent(Player player, Position position) {
        this(player, position, false);
    }

    public PlayerRespawnEvent(Player player, Position position, boolean bl) {
        this.player = player;
        this.c = position;
        this.b = bl;
    }

    public Position getRespawnPosition() {
        return this.c;
    }

    public void setRespawnPosition(Position position) {
        this.c = position;
    }

    public boolean isFirstSpawn() {
        return this.b;
    }
}


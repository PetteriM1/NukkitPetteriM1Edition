/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerMouseOverEntityEvent
extends PlayerEvent {
    private static final HandlerList c = new HandlerList();
    private final Entity b;

    public static HandlerList getHandlers() {
        return c;
    }

    public PlayerMouseOverEntityEvent(Player player, Entity entity) {
        this.player = player;
        this.b = entity;
    }

    public Entity getEntity() {
        return this.b;
    }
}


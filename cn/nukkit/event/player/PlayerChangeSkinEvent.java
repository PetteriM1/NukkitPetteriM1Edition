/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerChangeSkinEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final Skin b;

    public static HandlerList getHandlers() {
        return c;
    }

    public PlayerChangeSkinEvent(Player player, Skin skin) {
        this.player = player;
        this.b = skin;
    }

    public Skin getSkin() {
        return this.b;
    }
}


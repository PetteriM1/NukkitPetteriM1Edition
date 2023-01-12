/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.network.protocol.AnimatePacket;

public class PlayerAnimationEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final AnimatePacket.Action c;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerAnimationEvent(Player player) {
        this(player, AnimatePacket.Action.SWING_ARM);
    }

    public PlayerAnimationEvent(Player player, AnimatePacket.Action action) {
        this.player = player;
        this.c = action;
    }

    public AnimatePacket.Action getAnimationType() {
        return this.c;
    }
}


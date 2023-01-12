/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerToggleSpinAttackEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final boolean c;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerToggleSpinAttackEvent(Player player, boolean bl) {
        this.player = player;
        this.c = bl;
    }

    public boolean isSpinAttacking() {
        return this.c;
    }
}


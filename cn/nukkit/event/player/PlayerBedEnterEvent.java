/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerBedEnterEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final Block d;
    private final boolean b;

    public static HandlerList getHandlers() {
        return c;
    }

    public PlayerBedEnterEvent(Player player, Block block) {
        this(player, block, false);
    }

    public PlayerBedEnterEvent(Player player, Block block, boolean bl) {
        this.player = player;
        this.d = block;
        this.b = bl;
    }

    public Block getBed() {
        return this.d;
    }

    public boolean isOnlySetSpawn() {
        return this.b;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;

public class SignGlowEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private final Player b;
    private final boolean c;

    public static HandlerList getHandlers() {
        return d;
    }

    public SignGlowEvent(Block block, Player player, boolean bl) {
        super(block);
        this.b = player;
        this.c = bl;
    }

    public Player getPlayer() {
        return this.b;
    }

    public boolean isGlowing() {
        return this.c;
    }
}


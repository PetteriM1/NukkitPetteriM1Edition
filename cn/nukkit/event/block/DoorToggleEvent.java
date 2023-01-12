/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockUpdateEvent;

public class DoorToggleEvent
extends BlockUpdateEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private Player d;

    public static HandlerList getHandlers() {
        return c;
    }

    public DoorToggleEvent(Block block, Player player) {
        super(block);
        this.d = player;
    }

    public void setPlayer(Player player) {
        this.d = player;
    }

    public Player getPlayer() {
        return this.d;
    }
}


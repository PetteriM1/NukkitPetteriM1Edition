/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;
import cn.nukkit.utils.BlockColor;

public class SignColorChangeEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final Player c;
    private final BlockColor d;

    public static HandlerList getHandlers() {
        return b;
    }

    public SignColorChangeEvent(Block block, Player player, BlockColor blockColor) {
        super(block);
        this.c = player;
        this.d = blockColor;
    }

    public Player getPlayer() {
        return this.c;
    }

    public BlockColor getColor() {
        return this.d;
    }
}


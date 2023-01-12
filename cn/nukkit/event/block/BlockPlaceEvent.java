/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;
import cn.nukkit.item.Item;

public class BlockPlaceEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected final Player player;
    protected final Item item;
    protected final Block blockReplace;
    protected final Block blockAgainst;

    public static HandlerList getHandlers() {
        return b;
    }

    public BlockPlaceEvent(Player player, Block block, Block block2, Block block3, Item item) {
        super(block);
        this.blockReplace = block2;
        this.blockAgainst = block3;
        this.item = item;
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Item getItem() {
        return this.item;
    }

    public Block getBlockReplace() {
        return this.blockReplace;
    }

    public Block getBlockAgainst() {
        return this.blockAgainst;
    }
}


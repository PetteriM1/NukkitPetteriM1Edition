/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;
import cn.nukkit.item.Item;

public class ItemFrameDropItemEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private final Player c;
    private final Item e;
    private final BlockEntityItemFrame b;

    public ItemFrameDropItemEvent(Player player, Block block, BlockEntityItemFrame blockEntityItemFrame, Item item) {
        super(block);
        this.c = player;
        this.b = blockEntityItemFrame;
        this.e = item;
    }

    public static HandlerList getHandlers() {
        return d;
    }

    public Player getPlayer() {
        return this.c;
    }

    public BlockEntityItemFrame getItemFrame() {
        return this.b;
    }

    public Item getItem() {
        return this.e;
    }
}


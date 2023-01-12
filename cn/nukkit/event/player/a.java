/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

abstract class a
extends PlayerEvent
implements Cancellable {
    private final Block c;
    private final BlockFace b;
    private final Item d;
    private Item e;

    public a(Player player, Block block, BlockFace blockFace, Item item, Item item2) {
        this.player = player;
        this.c = block;
        this.b = blockFace;
        this.e = item2;
        this.d = item;
    }

    public Item getBucket() {
        return this.d;
    }

    public Item getItem() {
        return this.e;
    }

    public void setItem(Item item) {
        this.e = item;
    }

    public Block getBlockClicked() {
        return this.c;
    }

    public BlockFace getBlockFace() {
        return this.b;
    }
}


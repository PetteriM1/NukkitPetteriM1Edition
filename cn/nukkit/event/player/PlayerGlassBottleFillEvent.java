/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;

public class PlayerGlassBottleFillEvent
extends PlayerEvent
implements Cancellable {
    protected final Item item;
    protected final Block target;

    public PlayerGlassBottleFillEvent(Player player, Block block, Item item) {
        this.player = player;
        this.target = block;
        this.item = item.clone();
    }

    public Item getItem() {
        return this.item;
    }

    public Block getBlock() {
        return this.target;
    }
}


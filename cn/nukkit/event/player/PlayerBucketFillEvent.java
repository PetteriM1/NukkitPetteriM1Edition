/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.a;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

public class PlayerBucketFillEvent
extends a {
    private static final HandlerList f = new HandlerList();

    public static HandlerList getHandlers() {
        return f;
    }

    public PlayerBucketFillEvent(Player player, Block block, BlockFace blockFace, Item item, Item item2) {
        super(player, block, blockFace, item, item2);
    }
}


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

public class PlayerBucketEmptyEvent
extends a {
    private static final HandlerList g = new HandlerList();
    private boolean f;

    public static HandlerList getHandlers() {
        return g;
    }

    public PlayerBucketEmptyEvent(Player player, Block block, BlockFace blockFace, Item item, Item item2) {
        this(player, block, blockFace, item, item2, true);
    }

    public PlayerBucketEmptyEvent(Player player, Block block, BlockFace blockFace, Item item, Item item2, boolean bl) {
        super(player, block, blockFace, item, item2);
        this.f = bl;
    }

    public boolean isMobSpawningAllowed() {
        return this.f;
    }

    public void setMobSpawningAllowed(boolean bl) {
        this.f = bl;
    }
}


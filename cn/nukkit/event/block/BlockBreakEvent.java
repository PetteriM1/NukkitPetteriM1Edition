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
import cn.nukkit.math.BlockFace;

public class BlockBreakEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected final Player player;
    protected final Item item;
    protected final BlockFace face;
    protected boolean instaBreak;
    protected Item[] blockDrops;
    protected int blockXP;
    protected boolean fastBreak;

    public static HandlerList getHandlers() {
        return b;
    }

    public BlockBreakEvent(Player player, Block block, Item item, Item[] itemArray) {
        this(player, block, item, itemArray, false, false);
    }

    public BlockBreakEvent(Player player, Block block, Item item, Item[] itemArray, boolean bl) {
        this(player, block, item, itemArray, bl, false);
    }

    public BlockBreakEvent(Player player, Block block, Item item, Item[] itemArray, boolean bl, boolean bl2) {
        this(player, block, null, item, itemArray, bl, bl2);
    }

    public BlockBreakEvent(Player player, Block block, BlockFace blockFace, Item item, Item[] itemArray, boolean bl, boolean bl2) {
        super(block);
        this.face = blockFace;
        this.item = item;
        this.player = player;
        this.instaBreak = bl;
        this.blockDrops = itemArray;
        this.fastBreak = bl2;
        this.blockXP = block.getDropExp();
    }

    public Player getPlayer() {
        return this.player;
    }

    public BlockFace getFace() {
        return this.face;
    }

    public Item getItem() {
        return this.item;
    }

    public boolean getInstaBreak() {
        return this.instaBreak;
    }

    public Item[] getDrops() {
        return this.blockDrops;
    }

    public void setDrops(Item[] itemArray) {
        if (itemArray == null) {
            itemArray = new Item[]{};
        }
        this.blockDrops = itemArray;
    }

    public int getDropExp() {
        return this.blockXP;
    }

    public void setDropExp(int n) {
        this.blockXP = n;
    }

    public void setInstaBreak(boolean bl) {
        this.instaBreak = bl;
    }

    public boolean isFastBreak() {
        return this.fastBreak;
    }
}


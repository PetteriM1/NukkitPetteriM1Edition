package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

public class CraftingTableOpenEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Block craftingTable;

    public CraftingTableOpenEvent(Player player, Block craftingTable) {
        this.player = player;
        this.craftingTable = craftingTable;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Block getCraftingTable() {
        return this.craftingTable;
    }
}

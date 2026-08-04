package cn.nukkit.event.inventory;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.inventory.SmithingInventory;
import cn.nukkit.item.Item;

public class SmithItemEvent extends InventoryEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Item oldItem;
    private final Item newItem;
    private final Item materialItem;
    private final Player player;
    public SmithItemEvent(SmithingInventory inventory, Item oldItem, Item newItem, Item materialItem, Player player) {
        super(inventory);
        this.oldItem = oldItem;
        this.newItem = newItem;
        this.materialItem = materialItem;
        this.player = player;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Item getMaterialItem() {
        return this.materialItem;
    }

    public Item getNewItem() {
        return this.newItem;
    }

    public Item getOldItem() {
        return this.oldItem;
    }

    public Player getPlayer() {
        return this.player;
    }
}

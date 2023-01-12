/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction.action;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;

public class DropItemAction
extends InventoryAction {
    public DropItemAction(Item item, Item item2) {
        super(item, item2);
    }

    @Override
    public boolean isValid(Player player) {
        return this.sourceItem.isNull();
    }

    @Override
    public boolean onPreExecute(Player player) {
        PlayerDropItemEvent playerDropItemEvent = new PlayerDropItemEvent(player, this.targetItem);
        player.getServer().getPluginManager().callEvent(playerDropItemEvent);
        if (playerDropItemEvent.isCancelled()) {
            player.stopAction();
        }
        return !playerDropItemEvent.isCancelled();
    }

    @Override
    public boolean execute(Player player) {
        return player.dropItem(this.targetItem);
    }

    @Override
    public void onExecuteSuccess(Player player) {
    }

    @Override
    public void onExecuteFail(Player player) {
    }
}


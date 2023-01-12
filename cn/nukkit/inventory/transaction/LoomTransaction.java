/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.LoomInventory;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.LoomItemAction;
import cn.nukkit.item.Item;
import java.util.List;

public class LoomTransaction
extends InventoryTransaction {
    private Item b;

    public LoomTransaction(Player player, List<InventoryAction> list) {
        super(player, list);
    }

    @Override
    public void addAction(InventoryAction inventoryAction) {
        super.addAction(inventoryAction);
        if (inventoryAction instanceof LoomItemAction) {
            this.b = inventoryAction.getSourceItem();
        }
    }

    @Override
    public boolean canExecute() {
        Inventory inventory = this.getSource().getWindowById(2);
        if (inventory == null) {
            return false;
        }
        LoomInventory loomInventory = (LoomInventory)inventory;
        if (this.b == null) {
            return false;
        }
        Item item = loomInventory.getFirstItem();
        Item item2 = loomInventory.getSecondItem();
        if (item.getId() != 446 || item2.getId() != 351 || item.getDamage() != this.b.getDamage()) {
            return false;
        }
        if (!this.b.hasCompoundTag()) {
            return false;
        }
        int n = this.b.getNamedTag().getList("Patterns").size();
        if (item.getNamedTag() == null) {
            return n == 1;
        }
        if (n > 6) {
            return false;
        }
        return item.getNamedTag().getList("Patterns").size() + 1 == n;
    }

    @Override
    public boolean execute() {
        if (this.hasExecuted() || !this.canExecute()) {
            this.source.removeAllWindows(false);
            this.sendInventories();
            return false;
        }
        for (InventoryAction inventoryAction : this.actions) {
            if (inventoryAction.execute(this.source)) {
                inventoryAction.onExecuteSuccess(this.source);
                continue;
            }
            inventoryAction.onExecuteFail(this.source);
        }
        return true;
    }

    public static boolean checkForItemPart(List<InventoryAction> list) {
        return list.stream().anyMatch(inventoryAction -> inventoryAction instanceof LoomItemAction);
    }
}


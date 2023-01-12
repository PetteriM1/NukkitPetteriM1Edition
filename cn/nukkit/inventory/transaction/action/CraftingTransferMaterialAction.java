/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction.action;

import cn.nukkit.Player;
import cn.nukkit.inventory.transaction.CraftingTransaction;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;

public class CraftingTransferMaterialAction
extends InventoryAction {
    public CraftingTransferMaterialAction(Item item, Item item2, int n) {
        super(item, item2);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void onAddToTransaction(InventoryTransaction inventoryTransaction) {
        if (!(inventoryTransaction instanceof CraftingTransaction)) throw new RuntimeException(this.getClass().getName() + " can only be added to CraftingTransactions");
        if (this.sourceItem.isNull()) {
            ((CraftingTransaction)inventoryTransaction).setInput(this.targetItem);
            return;
        } else {
            if (!this.targetItem.isNull()) throw new RuntimeException("Invalid " + this.getClass().getName() + ", either source or target item must be air, got source: " + this.sourceItem + ", target: " + this.targetItem);
            ((CraftingTransaction)inventoryTransaction).setExtraOutput(this.sourceItem);
        }
    }

    @Override
    public boolean isValid(Player player) {
        return true;
    }

    @Override
    public boolean execute(Player player) {
        return true;
    }

    @Override
    public void onExecuteSuccess(Player player) {
    }

    @Override
    public void onExecuteFail(Player player) {
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction.action;

import cn.nukkit.Player;
import cn.nukkit.inventory.transaction.CraftingTransaction;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;

public class CraftingTakeResultAction
extends InventoryAction {
    public CraftingTakeResultAction(Item item, Item item2) {
        super(item, item2);
    }

    @Override
    public void onAddToTransaction(InventoryTransaction inventoryTransaction) {
        if (!(inventoryTransaction instanceof CraftingTransaction)) {
            throw new RuntimeException(this.getClass().getName() + " can only be added to CraftingTransactions");
        }
        ((CraftingTransaction)inventoryTransaction).setPrimaryOutput(this.getSourceItem());
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


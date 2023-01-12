/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction.action;

import cn.nukkit.Player;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.item.Item;

public abstract class InventoryAction {
    protected Item sourceItem;
    protected Item targetItem;

    public InventoryAction(Item item, Item item2) {
        this.sourceItem = item;
        this.targetItem = item2;
    }

    public long getCreationTime() {
        return 0L;
    }

    public Item getSourceItem() {
        return this.sourceItem.clone();
    }

    public Item getSourceItemUnsafe() {
        return this.sourceItem;
    }

    public Item getTargetItem() {
        return this.targetItem.clone();
    }

    public Item getTargetItemUnsafe() {
        return this.targetItem;
    }

    public boolean onPreExecute(Player player) {
        return true;
    }

    public abstract boolean isValid(Player var1);

    public void onAddToTransaction(InventoryTransaction inventoryTransaction) {
    }

    public abstract boolean execute(Player var1);

    public abstract void onExecuteSuccess(Player var1);

    public abstract void onExecuteFail(Player var1);
}


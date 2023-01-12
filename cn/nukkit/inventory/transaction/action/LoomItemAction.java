/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction.action;

import cn.nukkit.Player;
import cn.nukkit.inventory.LoomInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;

public class LoomItemAction
extends InventoryAction {
    private final LoomInventory a;

    public LoomItemAction(Item item, Item item2, LoomInventory loomInventory) {
        super(item, item2);
        this.a = loomInventory;
    }

    @Override
    public boolean isValid(Player player) {
        return player.getWindowById(2) != null;
    }

    @Override
    public boolean execute(Player player) {
        return true;
    }

    @Override
    public void onExecuteSuccess(Player player) {
        Item item = this.a.getFirstItem();
        Item item2 = this.a.getSecondItem();
        if (item != null && !item.isNull()) {
            --item.count;
            this.a.setFirstItem(item);
        }
        if (item2 != null && !item2.isNull()) {
            --item2.count;
            this.a.setSecondItem(item2);
        }
    }

    @Override
    public void onExecuteFail(Player player) {
        this.a.sendContents(player);
    }
}


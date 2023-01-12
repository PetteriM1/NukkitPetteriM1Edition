/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction.action;

import cn.nukkit.Player;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;

public class RepairItemAction
extends InventoryAction {
    private final int a;

    public RepairItemAction(Item item, Item item2, int n) {
        super(item, item2);
        this.a = n;
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
    }

    @Override
    public void onExecuteFail(Player player) {
    }

    public int getType() {
        return this.a;
    }
}


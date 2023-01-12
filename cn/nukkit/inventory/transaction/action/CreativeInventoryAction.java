/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction.action;

import cn.nukkit.Player;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;

public class CreativeInventoryAction
extends InventoryAction {
    public static final int TYPE_DELETE_ITEM = 0;
    public static final int TYPE_CREATE_ITEM = 1;
    protected int actionType;

    public CreativeInventoryAction(Item item, Item item2, int n) {
        super(item, item2);
    }

    @Override
    public boolean isValid(Player player) {
        return player.isCreative() && (this.actionType == 0 || Item.getCreativeItemIndex(player.protocol, this.sourceItem) != -1);
    }

    public int getActionType() {
        return this.actionType;
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
}


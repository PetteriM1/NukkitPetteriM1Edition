/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;
import cn.nukkit.event.inventory.EnchantItemEvent;
import cn.nukkit.inventory.EnchantInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.inventory.transaction.action.EnchantingAction;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;
import java.util.List;

public class EnchantTransaction
extends InventoryTransaction {
    private Item d;
    private Item c;
    private int b = -1;

    public EnchantTransaction(Player player, List<InventoryAction> list) {
        super(player, list);
    }

    @Override
    public boolean canExecute() {
        Inventory inventory = this.getSource().getWindowById(3);
        if (inventory == null) {
            return false;
        }
        EnchantInventory enchantInventory = (EnchantInventory)inventory;
        if (!(this.getSource().isCreative() || this.b != -1 && enchantInventory.getReagentSlot().equals(Item.get(351, 4), true, false) && enchantInventory.getReagentSlot().count >= this.b)) {
            return false;
        }
        return this.d != null && this.c != null && this.d.equals(enchantInventory.getInputSlot(), true, true);
    }

    @Override
    public boolean execute() {
        if (this.hasExecuted || !this.canExecute()) {
            this.source.removeAllWindows(false);
            this.sendInventories();
            return false;
        }
        EnchantInventory enchantInventory = (EnchantInventory)this.getSource().getWindowById(3);
        EnchantItemEvent enchantItemEvent = new EnchantItemEvent(enchantInventory, this.d, this.c, this.b, this.source);
        this.source.getServer().getPluginManager().callEvent(enchantItemEvent);
        if (enchantItemEvent.isCancelled()) {
            this.source.removeAllWindows(false);
            this.sendInventories();
            return true;
        }
        for (InventoryAction inventoryAction : this.actions) {
            if (inventoryAction.execute(this.source)) {
                inventoryAction.onExecuteSuccess(this.source);
                continue;
            }
            inventoryAction.onExecuteFail(this.source);
        }
        if (!enchantItemEvent.getNewItem().equals(this.c, true, true)) {
            enchantInventory.setItem(0, enchantItemEvent.getNewItem(), true);
        }
        if (!this.source.isCreative()) {
            this.source.setExperience(this.source.getExperience(), this.source.getExperienceLevel() - enchantItemEvent.getXpCost());
        }
        return true;
    }

    @Override
    public void addAction(InventoryAction inventoryAction) {
        super.addAction(inventoryAction);
        if (inventoryAction instanceof EnchantingAction) {
            switch (((EnchantingAction)inventoryAction).getType()) {
                case -15: {
                    this.d = inventoryAction.getTargetItem();
                    break;
                }
                case -17: {
                    this.c = inventoryAction.getSourceItem();
                    break;
                }
                case -16: {
                    this.b = inventoryAction.getTargetItem().equals(Item.get(0), false, false) ? inventoryAction.getSourceItemUnsafe().count : inventoryAction.getSourceItemUnsafe().count - inventoryAction.getTargetItemUnsafe().count;
                }
            }
        }
    }

    public boolean checkForEnchantPart(List<InventoryAction> list) {
        for (InventoryAction inventoryAction : list) {
            if (!(inventoryAction instanceof EnchantingAction)) continue;
            return true;
        }
        return false;
    }

    public Item getInputItem() {
        return this.d;
    }

    public Item getOutputItem() {
        return this.c;
    }

    public int getCost() {
        return this.b;
    }

    public void setInputItem(Item item) {
        this.d = item;
    }

    public void setOutputItem(Item item) {
        this.c = item;
    }

    public void setCost(int n) {
        this.b = n;
    }
}


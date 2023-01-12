/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class InventoryTransaction {
    private boolean a;
    protected boolean hasExecuted;
    protected Player source;
    protected Set<Inventory> inventories = new HashSet<Inventory>();
    protected List<InventoryAction> actions = new ArrayList<InventoryAction>();

    public InventoryTransaction(Player player, List<InventoryAction> list) {
        this(player, list, true);
    }

    public InventoryTransaction(Player player, List<InventoryAction> list, boolean bl) {
        if (bl) {
            this.init(player, list);
        }
    }

    protected void init(Player player, List<InventoryAction> list) {
        this.source = player;
        for (InventoryAction inventoryAction : list) {
            this.addAction(inventoryAction);
        }
    }

    public Player getSource() {
        return this.source;
    }

    public long getCreationTime() {
        return 0L;
    }

    public Set<Inventory> getInventories() {
        return this.inventories;
    }

    public List<InventoryAction> getActionList() {
        return this.actions;
    }

    public Set<InventoryAction> getActions() {
        return new HashSet<InventoryAction>(this.actions);
    }

    public void addAction(InventoryAction inventoryAction) {
        if (this.a) {
            Server.getInstance().getLogger().debug("Failed to add InventoryAction for " + this.source.getName() + ": previous run was marked as invalid");
            return;
        }
        if (inventoryAction instanceof SlotChangeAction) {
            int n;
            SlotChangeAction slotChangeAction = (SlotChangeAction)inventoryAction;
            Item item = slotChangeAction.getTargetItemUnsafe();
            Item item2 = slotChangeAction.getSourceItemUnsafe();
            if (item.getCount() > item.getMaxStackSize() || item2.getCount() > item2.getMaxStackSize()) {
                this.a = true;
                Server.getInstance().getLogger().debug("Failed to add SlotChangeAction for " + this.source.getName() + ": illegal item stack size");
                return;
            }
            if (!slotChangeAction.getInventory().allowedToAdd(item.getId())) {
                this.a = true;
                Server.getInstance().getLogger().debug("Failed to add SlotChangeAction for " + this.source.getName() + ": " + slotChangeAction.getInventory().getTitle() + " inventory doesn't allow item " + item.getId());
                return;
            }
            if (!this.source.isCreative() && ((n = slotChangeAction.getSlot()) == 36 || n == 37 || n == 38 || n == 39) && item2.hasEnchantment(27)) {
                this.a = true;
                Server.getInstance().getLogger().debug("Failed to add SlotChangeAction for " + this.source.getName() + ": armor has binding curse");
                return;
            }
            ListIterator<InventoryAction> listIterator = this.actions.listIterator();
            while (listIterator.hasNext()) {
                SlotChangeAction slotChangeAction2;
                InventoryAction inventoryAction2 = listIterator.next();
                if (!(inventoryAction2 instanceof SlotChangeAction) || !(slotChangeAction2 = (SlotChangeAction)inventoryAction2).getInventory().equals(slotChangeAction.getInventory())) continue;
                Item item3 = slotChangeAction2.getSourceItem();
                Item item4 = slotChangeAction2.getTargetItem();
                if (slotChangeAction2.getSlot() == slotChangeAction.getSlot() && slotChangeAction.getSourceItem().equals(item4, item4.hasMeta(), item4.hasCompoundTag())) {
                    listIterator.set(new SlotChangeAction(slotChangeAction2.getInventory(), slotChangeAction2.getSlot(), slotChangeAction2.getSourceItem(), slotChangeAction.getTargetItem()));
                    inventoryAction.onAddToTransaction(this);
                    return;
                }
                if (slotChangeAction2.getSlot() != slotChangeAction.getSlot() || !slotChangeAction.getSourceItem().equals(item3, item3.hasMeta(), item3.hasCompoundTag()) || !slotChangeAction.getTargetItem().equals(item4, item4.hasMeta(), item4.hasCompoundTag())) continue;
                item3.setCount(item3.getCount() + slotChangeAction.getSourceItemUnsafe().getCount());
                item4.setCount(item4.getCount() + slotChangeAction.getTargetItemUnsafe().getCount());
                listIterator.set(new SlotChangeAction(slotChangeAction2.getInventory(), slotChangeAction2.getSlot(), item3, item4));
                return;
            }
        }
        this.actions.add(inventoryAction);
        inventoryAction.onAddToTransaction(this);
    }

    public void addInventory(Inventory inventory) {
        this.inventories.add(inventory);
    }

    protected boolean matchItems(List<Item> list, List<Item> list2) {
        for (InventoryAction object : this.actions) {
            if (object.getTargetItemUnsafe().getId() != 0) {
                list.add(object.getTargetItem());
            }
            if (!object.isValid(this.source)) {
                this.a = true;
                return false;
            }
            if (object.getSourceItemUnsafe().getId() == 0) continue;
            list2.add(object.getSourceItem());
        }
        block1: for (Item item : new ArrayList<Item>(list)) {
            for (Item item2 : new ArrayList<Item>(list2)) {
                if (!item.equals(item2)) continue;
                int n = Math.min(item2.getCount(), item.getCount());
                item.setCount(item.getCount() - n);
                item2.setCount(item2.getCount() - n);
                if (item2.getCount() == 0) {
                    list2.remove(item2);
                }
                if (item.getCount() != 0) continue;
                list.remove(item);
                continue block1;
            }
        }
        return list2.isEmpty() && list.isEmpty();
    }

    protected void sendInventories() {
        if (this.getSource().protocol >= 407) {
            for (InventoryAction inventoryAction : this.actions) {
                if (!(inventoryAction instanceof SlotChangeAction)) continue;
                SlotChangeAction slotChangeAction = (SlotChangeAction)inventoryAction;
                slotChangeAction.getInventory().sendSlot(slotChangeAction.getSlot(), this.source);
            }
        } else {
            for (Inventory inventory : this.inventories) {
                inventory.sendContents(this.source);
                if (!(inventory instanceof PlayerInventory)) continue;
                ((PlayerInventory)inventory).sendArmorContents(this.source);
            }
        }
    }

    public boolean canExecute() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        ArrayList<Item> arrayList2 = new ArrayList<Item>();
        return this.matchItems(arrayList, arrayList2) && !this.actions.isEmpty() && arrayList2.isEmpty() && arrayList.isEmpty();
    }

    protected boolean callExecuteEvent() {
        InventoryTransactionEvent inventoryTransactionEvent = new InventoryTransactionEvent(this);
        this.source.getServer().getPluginManager().callEvent(inventoryTransactionEvent);
        InventoryAction inventoryAction = null;
        SlotChangeAction slotChangeAction = null;
        Player player = null;
        for (InventoryAction inventoryAction2 : this.actions) {
            if (!(inventoryAction2 instanceof SlotChangeAction)) continue;
            SlotChangeAction slotChangeAction2 = (SlotChangeAction)inventoryAction2;
            if (slotChangeAction2.getInventory().getHolder() instanceof Player) {
                player = (Player)slotChangeAction2.getInventory().getHolder();
            }
            if (inventoryAction == null) {
                inventoryAction = slotChangeAction2;
                continue;
            }
            slotChangeAction = slotChangeAction2;
        }
        if (player != null && slotChangeAction != null) {
            if (inventoryAction.getTargetItemUnsafe().getCount() > inventoryAction.getSourceItemUnsafe().getCount()) {
                inventoryAction = slotChangeAction;
            }
            InventoryClickEvent inventoryClickEvent = new InventoryClickEvent(player, ((SlotChangeAction)inventoryAction).getInventory(), ((SlotChangeAction)inventoryAction).getSlot(), inventoryAction.getSourceItem(), inventoryAction.getTargetItem());
            this.source.getServer().getPluginManager().callEvent(inventoryClickEvent);
            if (inventoryClickEvent.isCancelled()) {
                return false;
            }
        }
        return !inventoryTransactionEvent.isCancelled();
    }

    public boolean execute() {
        if (this.a || this.hasExecuted() || !this.canExecute()) {
            this.sendInventories();
            return false;
        }
        if (!this.callExecuteEvent()) {
            this.sendInventories();
            return true;
        }
        for (InventoryAction inventoryAction : this.actions) {
            if (inventoryAction.onPreExecute(this.source)) continue;
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
        this.hasExecuted = true;
        return true;
    }

    public boolean hasExecuted() {
        return this.hasExecuted;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.inventory.CraftItemEvent;
import cn.nukkit.inventory.BigCraftingGrid;
import cn.nukkit.inventory.CraftingRecipe;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.LoomInventory;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.inventory.transaction.b;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ContainerClosePacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CraftingTransaction
extends InventoryTransaction {
    protected int gridSize;
    protected List<Item> inputs;
    protected List<Item> secondaryOutputs;
    protected Item primaryOutput;
    protected CraftingRecipe recipe;

    public CraftingTransaction(Player player, List<InventoryAction> list) {
        super(player, list, false);
        this.gridSize = player.getCraftingGrid() instanceof BigCraftingGrid ? 3 : 2;
        this.inputs = new ArrayList<Item>();
        this.secondaryOutputs = new ArrayList<Item>();
        this.init(player, list);
    }

    public void setInput(Item item) {
        if (this.inputs.size() < this.gridSize * this.gridSize) {
            for (Item item2 : this.inputs) {
                if (!item2.equals(item, item.hasMeta(), item.hasCompoundTag())) continue;
                item2.setCount(item2.getCount() + item.getCount());
                return;
            }
            this.inputs.add(item.clone());
        } else if (!Server.getInstance().suomiCraftPEMode()) {
            throw new RuntimeException("Input list is full can't add " + item);
        }
    }

    public List<Item> getInputList() {
        return this.inputs;
    }

    public void setExtraOutput(Item item) {
        if (this.secondaryOutputs.size() < this.gridSize * this.gridSize) {
            this.secondaryOutputs.add(item.clone());
        } else if (!Server.getInstance().suomiCraftPEMode()) {
            throw new RuntimeException("Output list is full can't add " + item);
        }
    }

    public Item getPrimaryOutput() {
        return this.primaryOutput;
    }

    public void setPrimaryOutput(Item item) {
        if (this.primaryOutput == null) {
            this.primaryOutput = item.clone();
        } else if (!this.primaryOutput.equals(item) && !Server.getInstance().suomiCraftPEMode()) {
            throw new RuntimeException("Primary result item has already been set and does not match the current item (expected " + this.primaryOutput + ", got " + item + ')');
        }
    }

    public CraftingRecipe getRecipe() {
        return this.recipe;
    }

    @Override
    public boolean canExecute() {
        if (this.source.craftingType == 1004) {
            Inventory inventory = this.source.getWindowById(2);
            if (inventory instanceof LoomInventory) {
                LoomInventory loomInventory = (LoomInventory)inventory;
                this.addInventory(loomInventory);
            }
        } else {
            this.recipe = this.source.getServer().getCraftingManager().matchRecipe(this.source.protocol, this.inputs, this.primaryOutput, this.secondaryOutputs);
        }
        return this.recipe != null && super.canExecute();
    }

    @Override
    protected boolean callExecuteEvent() {
        CraftItemEvent craftItemEvent = new CraftItemEvent(this);
        this.source.getServer().getPluginManager().callEvent(craftItemEvent);
        return !craftItemEvent.isCancelled();
    }

    @Override
    protected void sendInventories() {
        super.sendInventories();
        ContainerClosePacket containerClosePacket = new ContainerClosePacket();
        containerClosePacket.windowId = -1;
        containerClosePacket.wasServerInitiated = true;
        this.source.getServer().getScheduler().scheduleDelayedTask(new b(this, containerClosePacket), 10);
        this.source.resetCraftingGridType();
    }

    @Override
    public boolean execute() {
        if (super.execute()) {
            switch (this.primaryOutput.getId()) {
                case 58: {
                    this.source.awardAchievement("buildWorkBench");
                    break;
                }
                case 270: {
                    this.source.awardAchievement("buildPickaxe");
                    break;
                }
                case 61: {
                    this.source.awardAchievement("buildFurnace");
                    break;
                }
                case 290: {
                    this.source.awardAchievement("buildHoe");
                    break;
                }
                case 297: {
                    this.source.awardAchievement("makeBread");
                    break;
                }
                case 354: {
                    this.source.awardAchievement("bakeCake");
                    break;
                }
                case 257: 
                case 274: 
                case 278: 
                case 285: 
                case 745: {
                    this.source.awardAchievement("buildBetterPickaxe");
                    break;
                }
                case 267: 
                case 268: 
                case 272: 
                case 276: 
                case 283: 
                case 743: {
                    this.source.awardAchievement("buildSword");
                    break;
                }
                case 116: {
                    this.source.awardAchievement("enchantments");
                    break;
                }
                case 47: {
                    this.source.awardAchievement("bookcase");
                }
            }
            return true;
        }
        return false;
    }

    public boolean checkForCraftingPart(List<InventoryAction> list) {
        Iterator<InventoryAction> iterator = list.iterator();
        if (iterator.hasNext()) {
            InventoryAction inventoryAction = iterator.next();
            if (inventoryAction instanceof SlotChangeAction) {
                SlotChangeAction slotChangeAction = (SlotChangeAction)inventoryAction;
                if (slotChangeAction.getInventory().getType() == InventoryType.UI) {
                    if (slotChangeAction.getSlot() == 50) {
                        if (!slotChangeAction.getSourceItem().equals(slotChangeAction.getTargetItem())) {
                            return true;
                        }
                        Server.getInstance().getLogger().debug("Source equals target");
                        return false;
                    }
                    Server.getInstance().getLogger().debug("Invalid slot: " + slotChangeAction.getSlot());
                    return false;
                }
                Server.getInstance().getLogger().debug("Invalid action type: " + (Object)((Object)slotChangeAction.getInventory().getType()));
                return false;
            }
            Server.getInstance().getLogger().debug("SlotChangeAction expected, got " + inventoryAction);
            return false;
        }
        Server.getInstance().getLogger().debug("No actions on the list");
        return false;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}


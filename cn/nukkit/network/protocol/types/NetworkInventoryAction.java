/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol.types;

import cn.nukkit.Player;
import cn.nukkit.inventory.AnvilInventory;
import cn.nukkit.inventory.BeaconInventory;
import cn.nukkit.inventory.EnchantInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.LoomInventory;
import cn.nukkit.inventory.transaction.action.CraftingTakeResultAction;
import cn.nukkit.inventory.transaction.action.CraftingTransferMaterialAction;
import cn.nukkit.inventory.transaction.action.CreativeInventoryAction;
import cn.nukkit.inventory.transaction.action.DropItemAction;
import cn.nukkit.inventory.transaction.action.EnchantingAction;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.LoomItemAction;
import cn.nukkit.inventory.transaction.action.RepairItemAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import java.util.Optional;

public class NetworkInventoryAction {
    public static final int SOURCE_CONTAINER = 0;
    public static final int SOURCE_WORLD = 2;
    public static final int SOURCE_CREATIVE = 3;
    public static final int SOURCE_TODO = 99999;
    public static final int SOURCE_CRAFT_SLOT = 100;
    public static final int SOURCE_TYPE_CRAFTING_ADD_INGREDIENT = -2;
    public static final int SOURCE_TYPE_CRAFTING_REMOVE_INGREDIENT = -3;
    public static final int SOURCE_TYPE_CRAFTING_RESULT = -4;
    public static final int SOURCE_TYPE_CRAFTING_USE_INGREDIENT = -5;
    public static final int SOURCE_TYPE_ANVIL_INPUT = -10;
    public static final int SOURCE_TYPE_ANVIL_MATERIAL = -11;
    public static final int SOURCE_TYPE_ANVIL_RESULT = -12;
    public static final int SOURCE_TYPE_ANVIL_OUTPUT = -13;
    public static final int SOURCE_TYPE_ENCHANT_INPUT = -15;
    public static final int SOURCE_TYPE_ENCHANT_MATERIAL = -16;
    public static final int SOURCE_TYPE_ENCHANT_OUTPUT = -17;
    public static final int SOURCE_TYPE_TRADING_INPUT_1 = -20;
    public static final int SOURCE_TYPE_TRADING_INPUT_2 = -21;
    public static final int SOURCE_TYPE_TRADING_USE_INPUTS = -22;
    public static final int SOURCE_TYPE_TRADING_OUTPUT = -23;
    public static final int SOURCE_TYPE_BEACON = -24;
    public static final int SOURCE_TYPE_CONTAINER_DROP_CONTENTS = -100;
    public int sourceType;
    public int windowId;
    public long unknown;
    public int inventorySlot;
    public Item oldItem;
    public Item newItem;
    public int stackNetworkId;

    public NetworkInventoryAction read(InventoryTransactionPacket inventoryTransactionPacket) {
        this.sourceType = (int)inventoryTransactionPacket.getUnsignedVarInt();
        block0 : switch (this.sourceType) {
            case 0: {
                this.windowId = inventoryTransactionPacket.getVarInt();
                break;
            }
            case 2: {
                this.unknown = inventoryTransactionPacket.getUnsignedVarInt();
                break;
            }
            case 3: {
                break;
            }
            case 100: 
            case 99999: {
                this.windowId = inventoryTransactionPacket.getVarInt();
                switch (this.windowId) {
                    case -5: 
                    case -4: {
                        inventoryTransactionPacket.isCraftingPart = true;
                        break block0;
                    }
                    case -17: 
                    case -16: 
                    case -15: {
                        inventoryTransactionPacket.isEnchantingPart = true;
                        break block0;
                    }
                    case -12: 
                    case -11: 
                    case -10: {
                        inventoryTransactionPacket.isRepairItemPart = true;
                    }
                }
            }
        }
        this.inventorySlot = (int)inventoryTransactionPacket.getUnsignedVarInt();
        this.oldItem = inventoryTransactionPacket.getSlot(inventoryTransactionPacket.protocol);
        this.newItem = inventoryTransactionPacket.getSlot(inventoryTransactionPacket.protocol);
        if (inventoryTransactionPacket.hasNetworkIds && inventoryTransactionPacket.protocol >= 407 && inventoryTransactionPacket.protocol < 431) {
            this.stackNetworkId = inventoryTransactionPacket.getVarInt();
        }
        return this;
    }

    public void write(InventoryTransactionPacket inventoryTransactionPacket) {
        inventoryTransactionPacket.putUnsignedVarInt(this.sourceType);
        switch (this.sourceType) {
            case 0: {
                inventoryTransactionPacket.putVarInt(this.windowId);
                break;
            }
            case 2: {
                inventoryTransactionPacket.putUnsignedVarInt(this.unknown);
                break;
            }
            case 3: {
                break;
            }
            case 100: 
            case 99999: {
                inventoryTransactionPacket.putVarInt(this.windowId);
            }
        }
        inventoryTransactionPacket.putUnsignedVarInt(this.inventorySlot);
        inventoryTransactionPacket.putSlot(inventoryTransactionPacket.protocol, this.oldItem);
        inventoryTransactionPacket.putSlot(inventoryTransactionPacket.protocol, this.newItem);
        if (inventoryTransactionPacket.hasNetworkIds && inventoryTransactionPacket.protocol >= 407 && inventoryTransactionPacket.protocol < 431) {
            inventoryTransactionPacket.putVarInt(this.stackNetworkId);
        }
    }

    public InventoryAction createInventoryAction(Player player) {
        switch (this.sourceType) {
            case 0: {
                Inventory inventory;
                if (this.windowId == 120) {
                    this.inventorySlot += 36;
                    this.windowId = 0;
                    if (this.newItem == null || this.inventorySlot == 36 && !this.newItem.canBePutInHelmetSlot() && this.newItem.getId() != -155 && !this.oldItem.canBePutInHelmetSlot() && this.oldItem.getId() != -155 || this.inventorySlot == 37 && !this.newItem.isChestplate() && !this.oldItem.isChestplate() || this.inventorySlot == 38 && !this.newItem.isLeggings() && !this.oldItem.isLeggings() || this.inventorySlot == 39 && !this.newItem.isBoots() && !this.oldItem.isBoots()) {
                        player.getServer().getLogger().warning("Player " + player.getName() + " tried to set an invalid armor item");
                        return null;
                    }
                }
                if (this.windowId == 124 && player.protocol >= 407) {
                    switch (this.inventorySlot) {
                        case 50: {
                            if (player.getWindowById(2) == null) break;
                            this.windowId = 2;
                            this.inventorySlot = 2;
                            break;
                        }
                        case 14: {
                            if (player.getWindowById(3) == null) {
                                player.getServer().getLogger().debug("Player " + player.getName() + " does not have enchant window open");
                                return null;
                            }
                            this.windowId = 3;
                            this.inventorySlot = 0;
                            break;
                        }
                        case 15: {
                            if (player.getWindowById(3) == null) {
                                player.getServer().getLogger().debug("Player " + player.getName() + " does not have enchant window open");
                                return null;
                            }
                            this.windowId = 3;
                            this.inventorySlot = 1;
                            break;
                        }
                        case 1: {
                            if (player.getWindowById(2) == null) {
                                player.getServer().getLogger().debug("Player " + player.getName() + " does not have anvil window open");
                                return null;
                            }
                            this.windowId = 2;
                            this.inventorySlot = 0;
                            break;
                        }
                        case 2: {
                            if (player.getWindowById(2) == null) {
                                player.getServer().getLogger().debug("Player " + player.getName() + " does not have anvil window open");
                                return null;
                            }
                            this.windowId = 2;
                            this.inventorySlot = 1;
                        }
                    }
                }
                if ((inventory = player.getWindowById(this.windowId)) != null) {
                    return new SlotChangeAction(inventory, this.inventorySlot, this.oldItem, this.newItem);
                }
                player.getServer().getLogger().debug("Player " + player.getName() + " has no open container with window ID " + this.windowId);
                return null;
            }
            case 2: {
                if (this.inventorySlot != 0) {
                    player.getServer().getLogger().debug("Only expecting drop-item world actions from the client!");
                    return null;
                }
                return new DropItemAction(this.oldItem, this.newItem);
            }
            case 3: {
                int n;
                switch (this.inventorySlot) {
                    case 0: {
                        n = 0;
                        break;
                    }
                    case 1: {
                        n = 1;
                        break;
                    }
                    default: {
                        player.getServer().getLogger().debug("Unexpected creative action type " + this.inventorySlot);
                        return null;
                    }
                }
                return new CreativeInventoryAction(this.oldItem, this.newItem, n);
            }
            case 100: 
            case 99999: {
                switch (this.windowId) {
                    case -3: 
                    case -2: {
                        return new SlotChangeAction(player.getCraftingGrid(), this.inventorySlot, this.oldItem, this.newItem);
                    }
                    case -100: {
                        Optional<Inventory> optional = player.getTopWindow();
                        if (!optional.isPresent()) {
                            return null;
                        }
                        return new SlotChangeAction(optional.get(), this.inventorySlot, this.oldItem, this.newItem);
                    }
                    case -4: {
                        return new CraftingTakeResultAction(this.oldItem, this.newItem);
                    }
                    case -5: {
                        Inventory inventory = player.getWindowById(2);
                        if (inventory != null) {
                            LoomInventory loomInventory = (LoomInventory)inventory;
                            return new LoomItemAction(this.oldItem, this.newItem, loomInventory);
                        }
                        return new CraftingTransferMaterialAction(this.oldItem, this.newItem, this.inventorySlot);
                    }
                }
                if (this.windowId >= -13 && this.windowId <= -10) {
                    Inventory inventory = player.getWindowById(2);
                    if (!(inventory instanceof AnvilInventory)) {
                        inventory = player.getWindowById(4);
                        if (inventory instanceof BeaconInventory) {
                            inventory.setItem(0, Item.get(0));
                            return null;
                        }
                        player.getServer().getLogger().debug("Player " + player.getName() + " has no open anvil inventory");
                        return null;
                    }
                    AnvilInventory anvilInventory = (AnvilInventory)inventory;
                    switch (this.windowId) {
                        case -12: 
                        case -11: 
                        case -10: {
                            return new RepairItemAction(this.oldItem, this.newItem, this.windowId);
                        }
                    }
                    return new SlotChangeAction(anvilInventory, this.inventorySlot, this.oldItem, this.newItem);
                }
                if (this.windowId >= -17 && this.windowId <= -15) {
                    Inventory inventory = player.getWindowById(3);
                    if (!(inventory instanceof EnchantInventory)) {
                        player.getServer().getLogger().debug("Player " + player.getName() + " has no open enchant inventory");
                        return null;
                    }
                    EnchantInventory enchantInventory = (EnchantInventory)inventory;
                    switch (this.windowId) {
                        case -15: {
                            if (player.protocol < 407) {
                                if (this.inventorySlot == 0) break;
                                return null;
                            }
                            return new EnchantingAction(this.oldItem, this.newItem, -15);
                        }
                        case -16: {
                            if (player.protocol < 407) {
                                if (this.inventorySlot == 1) break;
                                return null;
                            }
                            return new EnchantingAction(this.newItem, this.oldItem, -16);
                        }
                        case -17: {
                            if (player.protocol < 407) {
                                if (this.inventorySlot != 0) {
                                    return null;
                                }
                                if (Item.get(351, 4).equals(this.newItem, true, false)) {
                                    this.inventorySlot = 2;
                                    if (this.newItem.getCount() < 1 || this.newItem.getCount() > 3) {
                                        return null;
                                    }
                                    Item item = enchantInventory.getItem(1);
                                    int n = this.newItem.getCount();
                                    if (item.getId() == 351 || item.getDamage() == 4 || item.getCount() >= n) break;
                                    return null;
                                }
                                Item item = enchantInventory.getItem(0);
                                Item item2 = enchantInventory.getItem(1);
                                if (item.equals(this.newItem, true, true) && (item2.getId() == 351 && item2.getDamage() == 4 || player.isCreative())) {
                                    this.inventorySlot = 3;
                                    enchantInventory.setItem(3, this.oldItem, false);
                                    break;
                                }
                                return null;
                            }
                            return new EnchantingAction(this.oldItem, this.newItem, -17);
                        }
                    }
                    return new SlotChangeAction(enchantInventory, this.inventorySlot, this.oldItem, this.newItem);
                }
                if (this.windowId == -24) {
                    Inventory inventory = player.getWindowById(4);
                    if (!(inventory instanceof BeaconInventory)) {
                        player.getServer().getLogger().debug("Player " + player.getName() + " has no open beacon inventory");
                        return null;
                    }
                    BeaconInventory beaconInventory = (BeaconInventory)inventory;
                    this.inventorySlot = 0;
                    return new SlotChangeAction(beaconInventory, this.inventorySlot, this.oldItem, this.newItem);
                }
                player.getServer().getLogger().debug("Player " + player.getName() + " has no open container with window ID " + this.windowId);
                return null;
            }
        }
        player.getServer().getLogger().debug("Unknown inventory source type " + this.sourceType);
        return null;
    }

    public String toString() {
        return "NetworkInventoryAction(sourceType=" + this.sourceType + ", windowId=" + this.windowId + ", unknown=" + this.unknown + ", inventorySlot=" + this.inventorySlot + ", oldItem=" + this.oldItem + ", newItem=" + this.newItem + ", stackNetworkId=" + this.stackNetworkId + ")";
    }
}


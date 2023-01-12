/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.inventory.BigCraftingGrid;
import cn.nukkit.inventory.CraftingGrid;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.PlayerCursorInventory;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.InventoryContentPacket;
import cn.nukkit.network.protocol.InventorySlotPacket;
import java.util.HashMap;

public class PlayerUIInventory
extends BaseInventory {
    private final Player c;
    private final PlayerCursorInventory d;
    private final CraftingGrid e;
    private final BigCraftingGrid b;

    public PlayerUIInventory(Player player) {
        super(player, InventoryType.UI, new HashMap<Integer, Item>(), 51);
        this.c = player;
        this.d = new PlayerCursorInventory(this);
        this.e = new CraftingGrid(this);
        this.b = new BigCraftingGrid(this);
    }

    public PlayerCursorInventory getCursorInventory() {
        return this.d;
    }

    public CraftingGrid getCraftingGrid() {
        return this.e;
    }

    public BigCraftingGrid getBigCraftingGrid() {
        return this.b;
    }

    @Override
    public void setSize(int n) {
        throw new UnsupportedOperationException("UI size is immutable");
    }

    @Override
    public void sendSlot(int n, Player ... playerArray) {
        Item item = this.getItem(n);
        for (Player player : playerArray) {
            InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
            inventorySlotPacket.slot = n;
            inventorySlotPacket.item = item;
            if (player == this.getHolder()) {
                inventorySlotPacket.inventoryId = 124;
                if (player.protocol < 407) {
                    player.dataPacket(inventorySlotPacket);
                }
            } else {
                int n2 = player.getWindowId(this);
                if (n2 == -1) {
                    this.close(player);
                    continue;
                }
                inventorySlotPacket.inventoryId = n2;
                if (player.protocol < 407) {
                    player.dataPacket(inventorySlotPacket);
                }
            }
            if (player.protocol < 407) continue;
            player.dataPacket(inventorySlotPacket);
        }
    }

    @Override
    public void sendContents(Player ... playerArray) {
        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.slots = new Item[this.getSize()];
        for (int k = 0; k < this.getSize(); ++k) {
            inventoryContentPacket.slots[k] = this.getItem(k);
        }
        for (Player player : playerArray) {
            if (player == this.getHolder()) {
                inventoryContentPacket.inventoryId = 124;
                if (player.protocol >= 407) continue;
                player.dataPacket(inventoryContentPacket);
                continue;
            }
            int n = player.getWindowId(this);
            if (n == -1) {
                this.close(player);
                continue;
            }
            inventoryContentPacket.inventoryId = n;
            if (player.protocol >= 407) continue;
            player.dataPacket(inventoryContentPacket);
        }
    }

    @Override
    public int getSize() {
        return 51;
    }

    @Override
    public Player getHolder() {
        return this.c;
    }

    private static UnsupportedOperationException a(UnsupportedOperationException unsupportedOperationException) {
        return unsupportedOperationException;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import java.util.Map;

public abstract class ContainerInventory
extends BaseInventory {
    public ContainerInventory(InventoryHolder inventoryHolder, InventoryType inventoryType) {
        super(inventoryHolder, inventoryType);
    }

    public ContainerInventory(InventoryHolder inventoryHolder, InventoryType inventoryType, Map<Integer, Item> map) {
        super(inventoryHolder, inventoryType, map);
    }

    public ContainerInventory(InventoryHolder inventoryHolder, InventoryType inventoryType, Map<Integer, Item> map, Integer n) {
        super(inventoryHolder, inventoryType, map, n);
    }

    public ContainerInventory(InventoryHolder inventoryHolder, InventoryType inventoryType, Map<Integer, Item> map, Integer n, String string) {
        super(inventoryHolder, inventoryType, map, n, string);
    }

    @Override
    public void onOpen(Player player) {
        super.onOpen(player);
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.windowId = player.getWindowId(this);
        containerOpenPacket.type = this.getType().getNetworkType();
        InventoryHolder inventoryHolder = this.getHolder();
        if (inventoryHolder instanceof Vector3) {
            containerOpenPacket.x = (int)((Vector3)((Object)inventoryHolder)).getX();
            containerOpenPacket.y = (int)((Vector3)((Object)inventoryHolder)).getY();
            containerOpenPacket.z = (int)((Vector3)((Object)inventoryHolder)).getZ();
        } else {
            containerOpenPacket.z = 0;
            containerOpenPacket.y = 0;
            containerOpenPacket.x = 0;
        }
        if (inventoryHolder instanceof Entity) {
            containerOpenPacket.entityId = ((Entity)((Object)inventoryHolder)).getId();
        }
        player.dataPacket(containerOpenPacket);
        this.sendContents(player);
    }

    @Override
    public void onClose(Player player) {
        ContainerClosePacket containerClosePacket = new ContainerClosePacket();
        containerClosePacket.windowId = player.getWindowId(this);
        containerClosePacket.wasServerInitiated = player.getClosingWindowId() != containerClosePacket.windowId;
        player.dataPacket(containerClosePacket);
        super.onClose(player);
    }

    public static int calculateRedstone(Inventory inventory) {
        if (inventory == null) {
            return 0;
        }
        int n = 0;
        float f2 = 0.0f;
        for (int k = 0; k < inventory.getSize(); ++k) {
            Item item = inventory.getItem(k);
            if (item.getId() == 0) continue;
            f2 += (float)item.getCount() / (float)Math.min(inventory.getMaxStackSize(), item.getMaxStackSize());
            ++n;
        }
        return NukkitMath.floorFloat((f2 /= (float)inventory.getSize()) * 14.0f) + (n > 0 ? 1 : 0);
    }
}


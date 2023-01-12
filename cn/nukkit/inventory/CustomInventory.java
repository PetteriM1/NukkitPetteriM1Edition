/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import java.util.Map;

public abstract class CustomInventory
extends ContainerInventory {
    public CustomInventory(InventoryHolder inventoryHolder, InventoryType inventoryType) {
        super(inventoryHolder, inventoryType);
    }

    public CustomInventory(InventoryHolder inventoryHolder, InventoryType inventoryType, Map<Integer, Item> map) {
        super(inventoryHolder, inventoryType, map);
    }

    public CustomInventory(InventoryHolder inventoryHolder, InventoryType inventoryType, Map<Integer, Item> map, Integer n) {
        super(inventoryHolder, inventoryType, map, n);
    }

    public CustomInventory(InventoryHolder inventoryHolder, InventoryType inventoryType, Map<Integer, Item> map, Integer n, String string) {
        super(inventoryHolder, inventoryType, map, n, string);
    }
}


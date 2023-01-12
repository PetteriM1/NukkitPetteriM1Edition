/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.entity.item.EntityMinecartChest;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;

public class MinecartChestInventory
extends ContainerInventory {
    public MinecartChestInventory(EntityMinecartChest entityMinecartChest) {
        super(entityMinecartChest, InventoryType.MINECART_CHEST);
    }

    @Override
    public EntityMinecartChest getHolder() {
        return (EntityMinecartChest)this.holder;
    }
}


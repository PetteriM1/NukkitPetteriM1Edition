/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.entity.item.EntityMinecartHopper;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;

public class MinecartHopperInventory
extends ContainerInventory {
    public MinecartHopperInventory(EntityMinecartHopper entityMinecartHopper) {
        super(entityMinecartHopper, InventoryType.MINECART_HOPPER);
    }

    @Override
    public EntityMinecartHopper getHolder() {
        return (EntityMinecartHopper)super.getHolder();
    }
}


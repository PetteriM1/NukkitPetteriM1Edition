/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityChestBoat;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.ContainerOpenPacket;

public class ChestBoatInventory
extends ContainerInventory {
    public ChestBoatInventory(EntityChestBoat entityChestBoat) {
        super(entityChestBoat, InventoryType.CHEST_BOAT);
    }

    @Override
    public EntityChestBoat getHolder() {
        return (EntityChestBoat)super.getHolder();
    }

    @Override
    public void onOpen(Player player) {
        super.onOpen(player);
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.windowId = player.getWindowId(this);
        containerOpenPacket.type = player.protocol >= 524 ? InventoryType.CHEST_BOAT.getNetworkType() : InventoryType.CHEST.getNetworkType();
        EntityChestBoat entityChestBoat = this.getHolder();
        if (entityChestBoat != null) {
            containerOpenPacket.x = (int)((Vector3)entityChestBoat).getX();
            containerOpenPacket.y = (int)((Vector3)entityChestBoat).getY();
            containerOpenPacket.z = (int)((Vector3)entityChestBoat).getZ();
        } else {
            containerOpenPacket.z = 0;
            containerOpenPacket.y = 0;
            containerOpenPacket.x = 0;
        }
        if (entityChestBoat != null) {
            containerOpenPacket.entityId = ((Entity)entityChestBoat).getId();
        }
        player.dataPacket(containerOpenPacket);
        this.sendContents(player);
    }
}


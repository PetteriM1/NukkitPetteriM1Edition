/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.event.inventory.InventoryCloseEvent;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.inventory.FakeBlockMenu;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.PlayerUIComponent;
import cn.nukkit.inventory.PlayerUIInventory;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.ContainerOpenPacket;

public class FakeBlockUIComponent
extends PlayerUIComponent {
    private final InventoryType e;

    FakeBlockUIComponent(PlayerUIInventory playerUIInventory, InventoryType inventoryType, int n, Position position) {
        super(playerUIInventory, n, inventoryType.getDefaultSize());
        this.e = inventoryType;
        this.holder = new FakeBlockMenu(this, position);
    }

    @Override
    public FakeBlockMenu getHolder() {
        return (FakeBlockMenu)this.holder;
    }

    @Override
    public boolean open(Player player) {
        InventoryOpenEvent inventoryOpenEvent = new InventoryOpenEvent(this, player);
        player.getServer().getPluginManager().callEvent(inventoryOpenEvent);
        if (inventoryOpenEvent.isCancelled()) {
            return false;
        }
        this.onOpen(player);
        return true;
    }

    @Override
    public void onOpen(Player player) {
        super.onOpen(player);
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.windowId = player.getWindowId(this);
        containerOpenPacket.type = this.e.getNetworkType();
        FakeBlockMenu fakeBlockMenu = this.getHolder();
        if (fakeBlockMenu != null) {
            containerOpenPacket.x = (int)((Vector3)fakeBlockMenu).getX();
            containerOpenPacket.y = (int)((Vector3)fakeBlockMenu).getY();
            containerOpenPacket.z = (int)((Vector3)fakeBlockMenu).getZ();
        } else {
            containerOpenPacket.z = 0;
            containerOpenPacket.y = 0;
            containerOpenPacket.x = 0;
        }
        player.dataPacket(containerOpenPacket);
        this.sendContents(player);
    }

    @Override
    public void onClose(Player player) {
        player.resetCraftingGridType();
        ContainerClosePacket containerClosePacket = new ContainerClosePacket();
        containerClosePacket.windowId = player.getWindowId(this);
        containerClosePacket.wasServerInitiated = player.getClosingWindowId() != containerClosePacket.windowId;
        player.dataPacket(containerClosePacket);
        super.onClose(player);
    }

    @Override
    public void close(Player player) {
        InventoryCloseEvent inventoryCloseEvent = new InventoryCloseEvent(this, player);
        player.getServer().getPluginManager().callEvent(inventoryCloseEvent);
        this.onClose(player);
    }
}


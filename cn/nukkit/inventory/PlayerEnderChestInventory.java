/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.block.BlockEnderChest;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.EntityHumanType;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.network.protocol.BlockEventPacket;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.ContainerOpenPacket;

public class PlayerEnderChestInventory
extends BaseInventory {
    public PlayerEnderChestInventory(EntityHumanType entityHumanType) {
        super(entityHumanType, InventoryType.ENDER_CHEST);
    }

    @Override
    public EntityHuman getHolder() {
        return (EntityHuman)this.holder;
    }

    @Override
    public void onOpen(Player player) {
        if (player != this.getHolder()) {
            return;
        }
        super.onOpen(player);
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.windowId = player.getWindowId(this);
        containerOpenPacket.type = this.getType().getNetworkType();
        BlockEnderChest blockEnderChest = player.getViewingEnderChest();
        if (blockEnderChest != null) {
            containerOpenPacket.x = (int)blockEnderChest.getX();
            containerOpenPacket.y = (int)blockEnderChest.getY();
            containerOpenPacket.z = (int)blockEnderChest.getZ();
        } else {
            containerOpenPacket.z = 0;
            containerOpenPacket.y = 0;
            containerOpenPacket.x = 0;
        }
        player.dataPacket(containerOpenPacket);
        this.sendContents(player);
        if (blockEnderChest != null && blockEnderChest.getViewers().size() == 1) {
            BlockEventPacket blockEventPacket = new BlockEventPacket();
            blockEventPacket.x = (int)blockEnderChest.getX();
            blockEventPacket.y = (int)blockEnderChest.getY();
            blockEventPacket.z = (int)blockEnderChest.getZ();
            blockEventPacket.case1 = 1;
            blockEventPacket.case2 = 2;
            if (blockEnderChest.level != null && blockEnderChest.level == player.level) {
                blockEnderChest.level.addLevelSoundEvent(blockEnderChest.add(0.5, 0.5, 0.5), 71);
                blockEnderChest.level.addChunkPacket((int)blockEnderChest.getX() >> 4, (int)blockEnderChest.getZ() >> 4, blockEventPacket);
            }
        }
    }

    @Override
    public void onClose(Player player) {
        ContainerClosePacket containerClosePacket = new ContainerClosePacket();
        containerClosePacket.windowId = player.getWindowId(this);
        containerClosePacket.wasServerInitiated = player.getClosingWindowId() != containerClosePacket.windowId;
        player.dataPacket(containerClosePacket);
        super.onClose(player);
        BlockEnderChest blockEnderChest = player.getViewingEnderChest();
        if (blockEnderChest != null && blockEnderChest.getViewers().size() == 1) {
            BlockEventPacket blockEventPacket = new BlockEventPacket();
            blockEventPacket.x = (int)blockEnderChest.getX();
            blockEventPacket.y = (int)blockEnderChest.getY();
            blockEventPacket.z = (int)blockEnderChest.getZ();
            blockEventPacket.case1 = 1;
            blockEventPacket.case2 = 0;
            if (blockEnderChest.level != null && blockEnderChest.level == player.level) {
                blockEnderChest.level.addLevelSoundEvent(blockEnderChest.add(0.5, 0.5, 0.5), 72);
                blockEnderChest.level.addChunkPacket((int)blockEnderChest.getX() >> 4, (int)blockEnderChest.getZ() >> 4, blockEventPacket);
            }
            player.setViewingEnderChest(null);
        }
        super.onClose(player);
    }
}


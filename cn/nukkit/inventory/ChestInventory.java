/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.DoubleChestInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.BlockEventPacket;

public class ChestInventory
extends ContainerInventory {
    protected DoubleChestInventory doubleInventory;

    public ChestInventory(BlockEntityChest blockEntityChest) {
        super(blockEntityChest, InventoryType.CHEST);
    }

    @Override
    public BlockEntityChest getHolder() {
        return (BlockEntityChest)this.holder;
    }

    @Override
    public void onOpen(Player player) {
        super.onOpen(player);
        if (this.getViewers().size() == 1) {
            BlockEventPacket blockEventPacket = new BlockEventPacket();
            blockEventPacket.x = (int)this.getHolder().getX();
            blockEventPacket.y = (int)this.getHolder().getY();
            blockEventPacket.z = (int)this.getHolder().getZ();
            blockEventPacket.case1 = 1;
            blockEventPacket.case2 = 2;
            Level level = this.getHolder().getLevel();
            if (level != null) {
                level.addLevelSoundEvent(this.getHolder().add(0.5, 0.5, 0.5), 67);
                level.addChunkPacket((int)this.getHolder().getX() >> 4, (int)this.getHolder().getZ() >> 4, blockEventPacket);
            }
        }
    }

    @Override
    public void onClose(Player player) {
        if (this.getViewers().size() == 1) {
            BlockEventPacket blockEventPacket = new BlockEventPacket();
            blockEventPacket.x = (int)this.getHolder().getX();
            blockEventPacket.y = (int)this.getHolder().getY();
            blockEventPacket.z = (int)this.getHolder().getZ();
            blockEventPacket.case1 = 1;
            blockEventPacket.case2 = 0;
            Level level = this.getHolder().getLevel();
            if (level != null) {
                level.addLevelSoundEvent(this.getHolder().add(0.5, 0.5, 0.5), 68);
                level.addChunkPacket((int)this.getHolder().getX() >> 4, (int)this.getHolder().getZ() >> 4, blockEventPacket);
            }
        }
        super.onClose(player);
    }

    public void setDoubleInventory(DoubleChestInventory doubleChestInventory) {
        this.doubleInventory = doubleChestInventory;
    }

    public DoubleChestInventory getDoubleInventory() {
        return this.doubleInventory;
    }

    @Override
    public void sendSlot(int n, Player ... playerArray) {
        if (this.doubleInventory != null) {
            this.doubleInventory.sendSlot(this, n, playerArray);
        } else {
            super.sendSlot(n, playerArray);
        }
    }
}


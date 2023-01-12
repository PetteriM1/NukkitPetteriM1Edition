/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntityShulkerBox;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.BlockEventPacket;

public class ShulkerBoxInventory
extends ContainerInventory {
    public ShulkerBoxInventory(BlockEntityShulkerBox blockEntityShulkerBox) {
        super(blockEntityShulkerBox, InventoryType.SHULKER_BOX);
    }

    @Override
    public BlockEntityShulkerBox getHolder() {
        return (BlockEntityShulkerBox)this.holder;
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
                level.addLevelSoundEvent(this.getHolder().add(0.5, 0.5, 0.5), 69);
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
                level.addLevelSoundEvent(this.getHolder().add(0.5, 0.5, 0.5), 70);
                level.addChunkPacket((int)this.getHolder().getX() >> 4, (int)this.getHolder().getZ() >> 4, blockEventPacket);
            }
        }
        super.onClose(player);
    }

    @Override
    public boolean canAddItem(Item item) {
        return this.allowedToAdd(item.getId()) && super.canAddItem(item);
    }

    @Override
    public boolean allowedToAdd(int n) {
        return n != 218 && n != 205;
    }
}


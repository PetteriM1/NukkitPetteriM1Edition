/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockBarrel;
import cn.nukkit.blockentity.BlockEntityBarrel;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;

public class BarrelInventory
extends ContainerInventory {
    public BarrelInventory(BlockEntityBarrel blockEntityBarrel) {
        super(blockEntityBarrel, InventoryType.BARREL);
    }

    @Override
    public BlockEntityBarrel getHolder() {
        return (BlockEntityBarrel)this.holder;
    }

    @Override
    public void onOpen(Player player) {
        BlockBarrel blockBarrel;
        super.onOpen(player);
        if (this.getViewers().size() != 1) {
            return;
        }
        BlockEntityBarrel blockEntityBarrel = this.getHolder();
        Level level = blockEntityBarrel.getLevel();
        if (level == null) {
            return;
        }
        Block block = blockEntityBarrel.getBlock();
        if (block instanceof BlockBarrel && !(blockBarrel = (BlockBarrel)block).isOpen()) {
            blockBarrel.setOpen(true);
            level.setBlock(blockBarrel, blockBarrel, true, true);
            level.addSound((Vector3)blockBarrel, Sound.BLOCK_BARREL_OPEN);
        }
    }

    @Override
    public void onClose(Player player) {
        BlockBarrel blockBarrel;
        super.onClose(player);
        if (!this.getViewers().isEmpty()) {
            return;
        }
        BlockEntityBarrel blockEntityBarrel = this.getHolder();
        Level level = blockEntityBarrel.getLevel();
        if (level == null) {
            return;
        }
        Block block = blockEntityBarrel.getBlock();
        if (block instanceof BlockBarrel && (blockBarrel = (BlockBarrel)block).isOpen()) {
            blockBarrel.setOpen(false);
            level.setBlock(blockBarrel, blockBarrel, true, true);
            level.addSound((Vector3)blockBarrel, Sound.BLOCK_BARREL_CLOSE);
        }
    }
}


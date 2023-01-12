/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparent;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBeacon;
import cn.nukkit.inventory.BeaconInventory;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.BlockColor;

public class BlockBeacon
extends BlockTransparent {
    @Override
    public int getId() {
        return 138;
    }

    @Override
    public double getHardness() {
        return 3.0;
    }

    @Override
    public double getResistance() {
        return 15.0;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public String getName() {
        return "Beacon";
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
            if (!(blockEntity instanceof BlockEntityBeacon)) {
                return false;
            }
            player.addWindow(new BeaconInventory(player.getUIInventory(), this), 4);
        }
        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        boolean bl = super.place(item, block, block2, blockFace, d2, d3, d4, player);
        if (bl) {
            CompoundTag compoundTag = new CompoundTag("").putString("id", "Beacon").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
            BlockEntity.createBlockEntity("Beacon", this.getChunk(), compoundTag, new Object[0]);
        }
        return bl;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIAMOND_BLOCK_COLOR;
    }
}


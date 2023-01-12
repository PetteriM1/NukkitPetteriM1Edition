/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityHopper;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Faceable;

public class BlockHopper
extends BlockTransparentMeta
implements Faceable {
    public BlockHopper() {
        this(0);
    }

    public BlockHopper(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 154;
    }

    @Override
    public String getName() {
        return "Hopper Block";
    }

    @Override
    public double getHardness() {
        return 3.0;
    }

    @Override
    public double getResistance() {
        return 24.0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        BlockFace blockFace2 = blockFace.getOpposite();
        if (blockFace2 == BlockFace.UP) {
            blockFace2 = BlockFace.DOWN;
        }
        this.setDamage(blockFace2.getIndex());
        boolean bl = this.level.isBlockPowered(this);
        if (bl == this.isEnabled()) {
            this.setEnabled(!bl);
        }
        this.level.setBlock(this, this);
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag("Items")).putString("id", "Hopper").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        BlockEntity.createBlockEntity("Hopper", this.getChunk(), compoundTag, new Object[0]);
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (blockEntity instanceof BlockEntityHopper) {
            return player.addWindow(((BlockEntityHopper)blockEntity).getInventory()) != -1;
        }
        return false;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (blockEntity instanceof BlockEntityHopper) {
            return ContainerInventory.calculateRedstone(((BlockEntityHopper)blockEntity).getInventory());
        }
        return super.getComparatorInputOverride();
    }

    public BlockFace getFacing() {
        return BlockFace.fromIndex(this.getDamage() & 7);
    }

    public boolean isEnabled() {
        return (this.getDamage() & 8) != 8;
    }

    public void setEnabled(boolean bl) {
        if (this.isEnabled() != bl) {
            this.setDamage(this.getDamage() ^ 8);
        }
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            boolean bl = this.level.isBlockPowered(this);
            if (bl == this.isEnabled()) {
                this.setEnabled(!bl);
                this.level.setBlock(this, this, true, false);
            }
            return n;
        }
        return 0;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.getTier() >= 1) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public Item toItem() {
        return Item.get(410);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    @Override
    public boolean canBePushed() {
        return false;
    }
}


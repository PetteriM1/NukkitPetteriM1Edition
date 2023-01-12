/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSkull;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockSkull
extends BlockTransparentMeta
implements Faceable {
    public BlockSkull() {
        this(0);
    }

    public BlockSkull(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 144;
    }

    @Override
    public double getHardness() {
        return 1.0;
    }

    @Override
    public double getResistance() {
        return 5.0;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public String getName() {
        BlockEntity blockEntity;
        int n = 0;
        if (this.level != null && (blockEntity = this.getLevel().getBlockEntity(this)) != null) {
            n = blockEntity.namedTag.getByte("SkullType");
        }
        return ItemSkull.getItemSkullName(n);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        switch (blockFace) {
            case NORTH: 
            case SOUTH: 
            case EAST: 
            case WEST: 
            case UP: {
                this.setDamage(blockFace.getIndex());
                break;
            }
            default: {
                return false;
            }
        }
        this.getLevel().setBlock(block, this, true, true);
        CompoundTag compoundTag = new CompoundTag().putString("id", "Skull").putByte("SkullType", item.getDamage()).putInt("x", block.getFloorX()).putInt("y", block.getFloorY()).putInt("z", block.getFloorZ()).putByte("Rot", (int)Math.floor(player.yaw * 16.0 / 360.0 + 0.5) & 0xF);
        if (item.hasCustomBlockData()) {
            for (Tag tag : item.getCustomBlockData().getAllTags()) {
                compoundTag.put(tag.getName(), tag);
            }
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(() -> BlockEntity.createBlockEntity("Skull", this.getChunk(), compoundTag, new Object[0]), 2);
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        int n = 0;
        if (blockEntity != null) {
            n = blockEntity.namedTag.getByte("SkullType");
        }
        return new Item[]{Item.get(397, n)};
    }

    @Override
    public Item toItem() {
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        int n = 0;
        if (blockEntity != null) {
            n = blockEntity.namedTag.getByte("SkullType");
        }
        return Item.get(397, n);
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromIndex(this.getDamage() & 7);
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.x + 0.25, this.y, this.z + 0.25, this.x + 1.0 - 0.25, this.y + 0.5, this.z + 1.0 - 0.25);
        switch (this.getBlockFace()) {
            case NORTH: {
                return axisAlignedBB.offset(0.0, 0.25, 0.25);
            }
            case SOUTH: {
                return axisAlignedBB.offset(0.0, 0.25, -0.25);
            }
            case WEST: {
                return axisAlignedBB.offset(0.25, 0.25, 0.0);
            }
            case EAST: {
                return axisAlignedBB.offset(-0.25, 0.25, 0.0);
            }
        }
        return axisAlignedBB;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBanner;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.Faceable;

public class BlockBanner
extends BlockTransparentMeta
implements Faceable {
    public BlockBanner() {
        this(0);
    }

    public BlockBanner(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 176;
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
    public int getToolType() {
        return 4;
    }

    @Override
    public String getName() {
        return "Banner";
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return null;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (blockFace != BlockFace.DOWN) {
            Tag tag;
            if (blockFace == BlockFace.UP) {
                this.setDamage(NukkitMath.floorDouble((player.yaw + 180.0) * 16.0 / 360.0 + 0.5) & 0xF);
                this.getLevel().setBlock(block, this, true);
            } else {
                this.setDamage(blockFace.getIndex());
                this.getLevel().setBlock(block, Block.get(177, this.getDamage()), true);
            }
            CompoundTag compoundTag = BlockEntity.getDefaultCompound(this, "Banner").putInt("Base", item.getDamage() & 0xF);
            Tag tag2 = item.getNamedTagEntry("Type");
            if (tag2 instanceof IntTag) {
                compoundTag.put("Type", tag2);
            }
            if ((tag = item.getNamedTagEntry("Patterns")) instanceof ListTag) {
                compoundTag.put("Patterns", tag);
            }
            BlockEntity.createBlockEntity("Banner", this.getChunk(), compoundTag, new Object[0]);
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && this.down().getId() == 0) {
            this.getLevel().useBreakOn(this);
            return 1;
        }
        return 0;
    }

    @Override
    public Item toItem() {
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        Item item = Item.get(446);
        if (blockEntity instanceof BlockEntityBanner) {
            ListTag<CompoundTag> listTag;
            BlockEntityBanner blockEntityBanner = (BlockEntityBanner)blockEntity;
            item.setDamage(blockEntityBanner.getBaseColor() & 0xF);
            int n = blockEntityBanner.namedTag.getInt("Type");
            if (n > 0) {
                item.setNamedTag((item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag()).putInt("Type", n));
            }
            if ((listTag = blockEntityBanner.namedTag.getList("Patterns", CompoundTag.class)).size() > 0) {
                item.setNamedTag((item.hasCompoundTag() ? item.getNamedTag() : new CompoundTag()).putList(listTag));
            }
        }
        return item;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    @Override
    public BlockColor getColor() {
        return this.getDyeColor().getColor();
    }

    public DyeColor getDyeColor() {
        BlockEntity blockEntity;
        if (this.level != null && (blockEntity = this.level.getBlockEntity(this)) instanceof BlockEntityBanner) {
            return ((BlockEntityBanner)blockEntity).getDyeColor();
        }
        return DyeColor.WHITE;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


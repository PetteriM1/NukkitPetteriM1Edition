/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBarrel;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityPiglin;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;
import java.util.Map;

public class BlockBarrel
extends BlockSolidMeta
implements Faceable {
    public BlockBarrel() {
        this(0);
    }

    public BlockBarrel(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Barrel";
    }

    @Override
    public int getId() {
        return 458;
    }

    @Override
    public double getHardness() {
        return 2.5;
    }

    @Override
    public double getResistance() {
        return 12.5;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(458));
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromIndex(this.getDamage() & 7);
    }

    public void setBlockFace(BlockFace blockFace) {
        this.setDamage(this.getDamage() & 8 | blockFace.getIndex() & 7);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (Math.abs(player.x - this.x) < 2.0 && Math.abs(player.z - this.z) < 2.0) {
            double d5 = player.y + (double)player.getEyeHeight();
            if (d5 - this.y > 2.0) {
                this.setDamage(BlockFace.UP.getIndex());
            } else if (this.y - d5 > 0.0) {
                this.setDamage(BlockFace.DOWN.getIndex());
            } else {
                this.setDamage(player.getHorizontalFacing().getOpposite().getIndex());
            }
        } else {
            this.setDamage(player.getHorizontalFacing().getOpposite().getIndex());
        }
        this.level.setBlock(block, this, true, true);
        CompoundTag compoundTag = new CompoundTag("").putList(new ListTag("Items")).putString("id", "Barrel").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        if (item.hasCustomName()) {
            compoundTag.putString("CustomName", item.getCustomName());
        }
        if (item.hasCustomBlockData()) {
            Map<String, Tag> map = item.getCustomBlockData().getTags();
            for (Map.Entry<String, Tag> entry : map.entrySet()) {
                compoundTag.put(entry.getKey(), entry.getValue());
            }
        }
        BlockEntity.createBlockEntity("Barrel", this.getChunk(), compoundTag, new Object[0]);
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player == null) {
            return false;
        }
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (!(blockEntity instanceof BlockEntityBarrel)) {
            return false;
        }
        BlockEntityBarrel blockEntityBarrel = (BlockEntityBarrel)blockEntity;
        if (blockEntityBarrel.namedTag.contains("Lock") && blockEntityBarrel.namedTag.get("Lock") instanceof StringTag && !blockEntityBarrel.namedTag.getString("Lock").equals(item.getCustomName())) {
            return true;
        }
        player.addWindow(blockEntityBarrel.getInventory());
        for (Entity entity : this.getChunk().getEntities().values()) {
            if (!(entity instanceof EntityPiglin)) continue;
            ((EntityPiglin)entity).setAngry(600);
        }
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    public boolean isOpen() {
        return (this.getDamage() & 8) == 8;
    }

    public void setOpen(boolean bl) {
        this.setDamage(this.getDamage() & 7 | (bl ? 8 : 0));
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (blockEntity instanceof BlockEntityBarrel) {
            return ContainerInventory.calculateRedstone(((BlockEntityBarrel)blockEntity).getInventory());
        }
        return super.getComparatorInputOverride();
    }

    @Override
    public boolean onBreak(Item item, Player player) {
        boolean bl = this.onBreak(item);
        if (bl && player != null) {
            for (Entity entity : this.getChunk().getEntities().values()) {
                if (!(entity instanceof EntityPiglin)) continue;
                ((EntityPiglin)entity).setAngry(600);
            }
        }
        return bl;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }
}


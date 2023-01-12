/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSlab;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityPiglin;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;
import cn.nukkit.utils.Utils;
import java.util.Map;

public class BlockChest
extends BlockTransparentMeta
implements Faceable {
    public BlockChest() {
        this(0);
    }

    public BlockChest(int n) {
        super(n);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public int getId() {
        return 54;
    }

    @Override
    public String getName() {
        return "Chest";
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
    public AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x + 0.0625, this.y, this.z + 0.0625, this.x + 0.9375, this.y + 0.9475, this.z + 0.9375);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Object object;
        BlockEntityChest blockEntityChest = null;
        this.setDamage(Utils.faces2534[player != null ? player.getDirection().getHorizontalIndex() : 0]);
        for (int k = 2; k <= 5; ++k) {
            Object object2;
            if ((this.getDamage() == 4 || this.getDamage() == 5) && (k == 4 || k == 5) || (this.getDamage() == 3 || this.getDamage() == 2) && (k == 2 || k == 3) || !((object = this.getSide(BlockFace.fromIndex(k))) instanceof BlockChest) || ((Block)object).getDamage() != this.getDamage() || !((object2 = this.getLevel().getBlockEntity((Vector3)object)) instanceof BlockEntityChest) || ((BlockEntityChest)object2).isPaired()) continue;
            blockEntityChest = (BlockEntityChest)object2;
            break;
        }
        this.getLevel().setBlock(block, this, true, true);
        CompoundTag compoundTag = new CompoundTag("").putList(new ListTag("Items")).putString("id", "Chest").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        if (item.hasCustomName()) {
            compoundTag.putString("CustomName", item.getCustomName());
        }
        if (item.hasCustomBlockData()) {
            object = item.getCustomBlockData().getTags();
            for (Map.Entry entry : object.entrySet()) {
                compoundTag.put((String)entry.getKey(), (Tag)entry.getValue());
            }
        }
        object = (BlockEntityChest)BlockEntity.createBlockEntity("Chest", this.getChunk(), compoundTag, new Object[0]);
        if (blockEntityChest != null) {
            blockEntityChest.pairWith((BlockEntityChest)object);
            ((BlockEntityChest)object).pairWith(blockEntityChest);
        }
        return true;
    }

    @Override
    public boolean onBreak(Item item) {
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        if (blockEntity instanceof BlockEntityChest) {
            ((BlockEntityChest)blockEntity).unpair();
        }
        this.getLevel().setBlock(this, Block.get(0), true, true);
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            Block block = this.up();
            if (!(block instanceof BlockSlab) && !block.isTransparent() || block instanceof BlockSlab && block.isTransparent()) {
                return true;
            }
            BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
            if (!(blockEntity instanceof BlockEntityChest)) {
                return false;
            }
            BlockEntityChest blockEntityChest = (BlockEntityChest)blockEntity;
            if (blockEntityChest.namedTag.contains("Lock") && blockEntityChest.namedTag.get("Lock") instanceof StringTag && !blockEntityChest.namedTag.getString("Lock").equals(item.getCustomName())) {
                return true;
            }
            player.addWindow(blockEntityChest.getInventory());
            for (Entity entity : this.getChunk().getEntities().values()) {
                if (!(entity instanceof EntityPiglin)) continue;
                ((EntityPiglin)entity).setAngry(600);
            }
        }
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (blockEntity instanceof BlockEntityChest) {
            return ContainerInventory.calculateRedstone(((BlockEntityChest)blockEntity).getInventory());
        }
        return super.getComparatorInputOverride();
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
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


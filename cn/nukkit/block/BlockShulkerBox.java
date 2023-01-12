/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityShulkerBox;
import cn.nukkit.inventory.ShulkerBoxInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;

public class BlockShulkerBox
extends BlockTransparentMeta {
    public BlockShulkerBox() {
        this(0);
    }

    public BlockShulkerBox(int n) {
        super(n);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public int getId() {
        return 218;
    }

    @Override
    public String getName() {
        return this.getDyeColor().getName() + " Shulker Box";
    }

    @Override
    public double getHardness() {
        return 2.5;
    }

    @Override
    public double getResistance() {
        return 30.0;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public Item toItem() {
        ItemBlock itemBlock = new ItemBlock((Block)this, this.getDamage(), 1);
        BlockEntityShulkerBox blockEntityShulkerBox = (BlockEntityShulkerBox)this.getLevel().getBlockEntity(this);
        if (blockEntityShulkerBox != null) {
            ShulkerBoxInventory shulkerBoxInventory = blockEntityShulkerBox.getRealInventory();
            if (!shulkerBoxInventory.slots.isEmpty()) {
                CompoundTag compoundTag = itemBlock.getNamedTag();
                if (compoundTag == null) {
                    compoundTag = new CompoundTag("");
                }
                ListTag<CompoundTag> listTag = new ListTag<CompoundTag>();
                for (int k = 0; k < shulkerBoxInventory.getSize(); ++k) {
                    if (shulkerBoxInventory.getItem(k).getId() == 0) continue;
                    CompoundTag compoundTag2 = NBTIO.putItemHelper(shulkerBoxInventory.getItem(k), k);
                    listTag.add(compoundTag2);
                }
                compoundTag.put("Items", listTag);
                itemBlock.setCompoundTag(compoundTag);
            }
            if (blockEntityShulkerBox.hasName()) {
                itemBlock.setCustomName(blockEntityShulkerBox.getName());
            }
        }
        return itemBlock;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        CompoundTag compoundTag;
        this.getLevel().setBlock(block, this, true, true);
        CompoundTag compoundTag2 = BlockEntity.getDefaultCompound(this, "ShulkerBox").putByte("facing", blockFace.getIndex());
        if (item.hasCustomName()) {
            compoundTag2.putString("CustomName", item.getCustomName());
        }
        if ((compoundTag = item.getNamedTag()) != null && compoundTag.contains("Items")) {
            compoundTag2.putList(compoundTag.getList("Items"));
        }
        BlockEntity.createBlockEntity("ShulkerBox", this.getChunk(), compoundTag2, new Object[0]);
        return true;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null && player.protocol > 471) {
            BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
            if (!(blockEntity instanceof BlockEntityShulkerBox)) {
                return false;
            }
            BlockEntityShulkerBox blockEntityShulkerBox = (BlockEntityShulkerBox)blockEntity;
            Block block = this.getSide(BlockFace.fromIndex(blockEntityShulkerBox.namedTag.getByte("facing")));
            if (!(block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockFlowable)) {
                return true;
            }
            player.addWindow(blockEntityShulkerBox.getInventory());
        }
        return true;
    }

    @Override
    public BlockColor getColor() {
        return this.getDyeColor().getColor();
    }

    public DyeColor getDyeColor() {
        return DyeColor.getByWoolData(this.getDamage());
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


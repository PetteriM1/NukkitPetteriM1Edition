/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBrewingStand;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.BlockColor;
import java.util.Map;

public class BlockBrewingStand
extends BlockTransparentMeta {
    public BlockBrewingStand() {
        this(0);
    }

    public BlockBrewingStand(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Brewing Stand";
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public int getId() {
        return 117;
    }

    @Override
    public int getLightLevel() {
        return 1;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Object object;
        this.getLevel().setBlock(block, this, true, true);
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag("Items")).putString("id", "BrewingStand").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        if (item.hasCustomName()) {
            compoundTag.putString("CustomName", item.getCustomName());
        }
        if (item.hasCustomBlockData()) {
            object = item.getCustomBlockData().getTags();
            for (Map.Entry<String, Tag> entry : object.entrySet()) {
                compoundTag.put(entry.getKey(), entry.getValue());
            }
        }
        return (object = (BlockEntityBrewingStand)BlockEntity.createBlockEntity("BrewingStand", this.getChunk(), compoundTag, new Object[0])) != null;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
            if (!(blockEntity instanceof BlockEntityBrewingStand)) {
                return false;
            }
            BlockEntityBrewingStand blockEntityBrewingStand = (BlockEntityBrewingStand)blockEntity;
            if (blockEntityBrewingStand.namedTag.contains("Lock") && blockEntityBrewingStand.namedTag.get("Lock") instanceof StringTag && !blockEntityBrewingStand.namedTag.getString("Lock").equals(item.getCustomName())) {
                return false;
            }
            player.addWindow(blockEntityBrewingStand.getInventory());
        }
        return true;
    }

    @Override
    public Item toItem() {
        return Item.get(379);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= 1) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public double getMinX() {
        return this.x + 0.4375;
    }

    @Override
    public double getMinZ() {
        return this.z + 0.4375;
    }

    @Override
    public double getMaxX() {
        return this.x + 1.0 - 0.4375;
    }

    @Override
    public double getMaxY() {
        return this.y + 1.0 - 0.125;
    }

    @Override
    public double getMaxZ() {
        return this.z + 1.0 - 0.4375;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (blockEntity instanceof BlockEntityBrewingStand) {
            return ContainerInventory.calculateRedstone(((BlockEntityBrewingStand)blockEntity).getInventory());
        }
        return super.getComparatorInputOverride();
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparent;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityEnchantTable;
import cn.nukkit.inventory.EnchantInventory;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.BlockColor;
import java.util.Map;

public class BlockEnchantingTable
extends BlockTransparent {
    @Override
    public int getId() {
        return 116;
    }

    @Override
    public String getName() {
        return "Enchanting Table";
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public double getHardness() {
        return 5.0;
    }

    @Override
    public double getResistance() {
        return 6000.0;
    }

    @Override
    public int getLightLevel() {
        return 7;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.getLevel().setBlock(block, this, true, true);
        CompoundTag compoundTag = new CompoundTag().putString("id", "EnchantTable").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        if (item.hasCustomName()) {
            compoundTag.putString("CustomName", item.getCustomName());
        }
        if (item.hasCustomBlockData()) {
            Map<String, Tag> map = item.getCustomBlockData().getTags();
            for (Map.Entry<String, Tag> entry : map.entrySet()) {
                compoundTag.put(entry.getKey(), entry.getValue());
            }
        }
        BlockEntity.createBlockEntity("EnchantTable", this.getChunk(), compoundTag, new Object[0]);
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
            if (!(blockEntity instanceof BlockEntityEnchantTable)) {
                return false;
            }
            BlockEntityEnchantTable blockEntityEnchantTable = (BlockEntityEnchantTable)blockEntity;
            if (blockEntityEnchantTable.namedTag.contains("Lock") && blockEntityEnchantTable.namedTag.get("Lock") instanceof StringTag && !blockEntityEnchantTable.namedTag.getString("Lock").equals(item.getCustomName())) {
                return true;
            }
            player.addWindow(new EnchantInventory(player.getUIInventory(), this.getLocation()), 3);
        }
        return true;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.RED_BLOCK_COLOR;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }
}


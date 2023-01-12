/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityFurnace;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.Utils;
import java.util.Map;

public class BlockFurnaceBurning
extends BlockSolidMeta {
    public BlockFurnaceBurning() {
        this(0);
    }

    public BlockFurnaceBurning(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 62;
    }

    @Override
    public String getName() {
        return "Burning Furnace";
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 3.5;
    }

    @Override
    public double getResistance() {
        return 17.5;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public int getLightLevel() {
        return 13;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.setDamage(Utils.faces2534[player != null ? player.getDirection().getHorizontalIndex() : 0]);
        this.getLevel().setBlock(block, this, true, true);
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag("Items")).putString("id", "Furnace").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        if (item.hasCustomName()) {
            compoundTag.putString("CustomName", item.getCustomName());
        }
        if (item.hasCustomBlockData()) {
            Map<String, Tag> map = item.getCustomBlockData().getTags();
            for (Map.Entry<String, Tag> entry : map.entrySet()) {
                compoundTag.put(entry.getKey(), entry.getValue());
            }
        }
        BlockEntity.createBlockEntity("Furnace", this.getChunk(), compoundTag, new Object[0]);
        return true;
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, Block.get(0), true, true);
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
            if (!(blockEntity instanceof BlockEntityFurnace)) {
                return false;
            }
            BlockEntityFurnace blockEntityFurnace = (BlockEntityFurnace)blockEntity;
            if (blockEntityFurnace.namedTag.contains("Lock") && blockEntityFurnace.namedTag.get("Lock") instanceof StringTag && !blockEntityFurnace.namedTag.getString("Lock").equals(item.getCustomName())) {
                return true;
            }
            player.addWindow(blockEntityFurnace.getInventory());
        }
        return true;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(61));
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (blockEntity instanceof BlockEntityFurnace) {
            return ContainerInventory.calculateRedstone(((BlockEntityFurnace)blockEntity).getInventory());
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


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSlab;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityEnderChest;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityPiglin;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;
import cn.nukkit.utils.Utils;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlockEnderChest
extends BlockTransparentMeta
implements Faceable {
    private final Set<Player> d = new HashSet<Player>();

    public BlockEnderChest() {
        this(0);
    }

    public BlockEnderChest(int n) {
        super(n);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public int getId() {
        return 130;
    }

    @Override
    public int getLightLevel() {
        return 7;
    }

    @Override
    public String getName() {
        return "Ender Chest";
    }

    @Override
    public double getHardness() {
        return 22.5;
    }

    @Override
    public double getResistance() {
        return 3000.0;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x + 0.0625, this.y, this.z + 0.0625, this.x + 0.9375, this.y + 0.9475, this.z + 0.9375);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.setDamage(Utils.faces2534[player != null ? player.getDirection().getHorizontalIndex() : 0]);
        this.getLevel().setBlock(block, this, true, true);
        CompoundTag compoundTag = new CompoundTag("").putString("id", "EnderChest").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        if (item.hasCustomName()) {
            compoundTag.putString("CustomName", item.getCustomName());
        }
        if (item.hasCustomBlockData()) {
            Map<String, Tag> map = item.getCustomBlockData().getTags();
            for (Map.Entry<String, Tag> entry : map.entrySet()) {
                compoundTag.put(entry.getKey(), entry.getValue());
            }
        }
        BlockEntity.createBlockEntity("EnderChest", this.getChunk(), compoundTag, new Object[0]);
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            Block block = this.up();
            if (!(block.isTransparent() || block instanceof BlockSlab && (block.getDamage() & 7) <= 0)) {
                return true;
            }
            BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
            if (!(blockEntity instanceof BlockEntityEnderChest)) {
                return false;
            }
            BlockEntityEnderChest blockEntityEnderChest = (BlockEntityEnderChest)blockEntity;
            if (blockEntityEnderChest.namedTag.contains("Lock") && blockEntityEnderChest.namedTag.get("Lock") instanceof StringTag && !blockEntityEnderChest.namedTag.getString("Lock").equals(item.getCustomName())) {
                return true;
            }
            player.setViewingEnderChest(this);
            player.addWindow(player.getEnderChestInventory());
            for (Entity entity : this.getChunk().getEntities().values()) {
                if (!(entity instanceof EntityPiglin)) continue;
                ((EntityPiglin)entity).setAngry(600);
            }
        }
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            if (item.hasEnchantment(16)) {
                return new Item[]{this.toItem()};
            }
            return new Item[]{Item.get(49, 0, 8)};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.OBSIDIAN_BLOCK_COLOR;
    }

    public Set<Player> getViewers() {
        return this.d;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }
}


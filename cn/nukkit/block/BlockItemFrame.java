/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.Faceable;
import java.util.concurrent.ThreadLocalRandom;

public class BlockItemFrame
extends BlockTransparentMeta
implements Faceable {
    private static final int[] e = new int[]{4, 5, 3, 2, 1, 0};
    private static final int d = 7;

    public BlockItemFrame() {
        this(0);
    }

    public BlockItemFrame(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 199;
    }

    @Override
    public String getName() {
        return "Item Frame";
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && !this.getSide(this.getFacing()).isSolid()) {
            this.level.useBreakOn(this);
            return n;
        }
        return 0;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        BlockEntityItemFrame blockEntityItemFrame = (BlockEntityItemFrame)blockEntity;
        if (blockEntityItemFrame.getItem().getId() == 0) {
            Item item2 = item.clone();
            if (player != null && player.isSurvival()) {
                item.setCount(item.getCount() - 1);
                player.getInventory().setItemInHand(item);
            }
            item2.setCount(1);
            blockEntityItemFrame.setItem(item2);
            this.getLevel().addSound((Vector3)this, Sound.BLOCK_ITEMFRAME_REMOVE_ITEM);
        } else {
            blockEntityItemFrame.setItemRotation((blockEntityItemFrame.getItemRotation() + 1) % 8);
            this.getLevel().addSound((Vector3)this, Sound.BLOCK_ITEMFRAME_ROTATE_ITEM);
        }
        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (blockFace.getIndex() > 1 && block2.isSolid() && (!block.isSolid() || block.canBeReplaced())) {
            this.setDamage(e[blockFace.getIndex()]);
            this.getLevel().setBlock(block, this, true, true);
            CompoundTag compoundTag = new CompoundTag().putString("id", "ItemFrame").putInt("x", (int)block.x).putInt("y", (int)block.y).putInt("z", (int)block.z).putByte("ItemRotation", 0).putFloat("ItemDropChance", 1.0f);
            if (item.hasCustomBlockData()) {
                for (Tag tag : item.getCustomBlockData().getAllTags()) {
                    compoundTag.put(tag.getName(), tag);
                }
            }
            BlockEntity.createBlockEntity("ItemFrame", this.getChunk(), compoundTag, new Object[0]);
            this.getLevel().addSound((Vector3)this, Sound.BLOCK_ITEMFRAME_PLACE);
            return true;
        }
        return false;
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, Block.get(0), true, true);
        this.getLevel().addSound((Vector3)this, Sound.BLOCK_ITEMFRAME_BREAK);
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        BlockEntityItemFrame blockEntityItemFrame = (BlockEntityItemFrame)blockEntity;
        if (blockEntityItemFrame != null && ThreadLocalRandom.current().nextFloat() <= blockEntityItemFrame.getItemDropChance()) {
            return new Item[]{this.toItem(), blockEntityItemFrame.getItem().clone()};
        }
        return new Item[]{this.toItem()};
    }

    @Override
    public Item toItem() {
        return Item.get(389);
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (blockEntity instanceof BlockEntityItemFrame) {
            return ((BlockEntityItemFrame)blockEntity).getAnalogOutput();
        }
        return super.getComparatorInputOverride();
    }

    public BlockFace getFacing() {
        switch (this.getDamage() & 7) {
            case 0: {
                return BlockFace.WEST;
            }
            case 1: {
                return BlockFace.EAST;
            }
            case 2: {
                return BlockFace.NORTH;
            }
            case 3: {
                return BlockFace.SOUTH;
            }
        }
        return null;
    }

    @Override
    public double getHardness() {
        return 0.25;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }

    @Override
    public BlockFace getBlockFace() {
        return this.getFacing().getOpposite();
    }
}


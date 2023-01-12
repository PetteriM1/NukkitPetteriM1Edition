/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFence;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockHopper;
import cn.nukkit.block.BlockWall;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityFlowerPot;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;

public class BlockFlowerPot
extends BlockFlowable {
    public BlockFlowerPot() {
        this(0);
    }

    public BlockFlowerPot(int n) {
        super(n);
    }

    protected static boolean canPlaceIntoFlowerPot(int n, int n2) {
        switch (n) {
            case 6: 
            case 32: 
            case 37: 
            case 38: 
            case 39: 
            case 40: 
            case 81: {
                return true;
            }
            case 31: {
                if (n2 != 2 && n2 != 3) break;
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Flower Pot";
    }

    @Override
    public int getId() {
        return 140;
    }

    private boolean a(Block block) {
        return block.isSolid() || block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockHopper;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && !this.a(this.down())) {
            this.level.useBreakOn(this);
            return n;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (!this.a(this.down())) {
            return false;
        }
        CompoundTag compoundTag = new CompoundTag().putString("id", "FlowerPot").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putShort("item", 0).putInt("data", 0);
        if (item.hasCustomBlockData()) {
            for (Tag tag : item.getCustomBlockData().getAllTags()) {
                compoundTag.put(tag.getName(), tag);
            }
        }
        BlockEntity.createBlockEntity("FlowerPot", this.getChunk(), compoundTag, new Object[0]);
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        int n;
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        if (!(blockEntity instanceof BlockEntityFlowerPot)) {
            return false;
        }
        if (blockEntity.namedTag.getShort("item") != 0 || blockEntity.namedTag.getInt("mData") != 0) {
            if (!BlockFlowerPot.canPlaceIntoFlowerPot(item.getId(), item.getDamage())) {
                int n2 = blockEntity.namedTag.getShort("item");
                if (n2 == 0) {
                    n2 = blockEntity.namedTag.getInt("mData");
                }
                for (Item item2 : player.getInventory().addItem(Item.get(n2, blockEntity.namedTag.getInt("data")))) {
                    player.dropItem(item2);
                }
                blockEntity.namedTag.putShort("item", 0);
                blockEntity.namedTag.putInt("data", 0);
                this.setDamage(0);
                this.level.setBlock(this, this, true);
                ((BlockEntityFlowerPot)blockEntity).spawnToAll();
                return true;
            }
            return false;
        }
        if (!BlockFlowerPot.canPlaceIntoFlowerPot(item.getId(), item.getDamage())) {
            Block block = item.getBlock();
            if (block == null || !BlockFlowerPot.canPlaceIntoFlowerPot(block.getId(), block.getDamage())) {
                return true;
            }
            n = block.getId();
        } else {
            n = item.getId();
        }
        blockEntity.namedTag.putShort("item", n);
        blockEntity.namedTag.putInt("data", item.getDamage());
        this.setDamage(1);
        this.getLevel().setBlock(this, this, true);
        ((BlockEntityFlowerPot)blockEntity).spawnToAll();
        if (!player.isCreative()) {
            item.setCount(item.getCount() - 1);
            player.getInventory().setItemInHand(item.getCount() > 0 ? item : Item.get(0));
        }
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        boolean bl = false;
        int n = 0;
        int n2 = 0;
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        if (blockEntity instanceof BlockEntityFlowerPot) {
            bl = true;
            n = blockEntity.namedTag.getShort("item");
            n2 = blockEntity.namedTag.getInt("data");
        }
        if (bl) {
            return new Item[]{Item.get(390), Item.get(n, n2, 1)};
        }
        return new Item[]{Item.get(390)};
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x + 0.3125, this.y, this.z + 0.3125, this.x + 0.6875, this.y + 0.375, this.z + 0.6875);
    }

    @Override
    public boolean canPassThrough() {
        return false;
    }

    @Override
    public Item toItem() {
        return Item.get(390);
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


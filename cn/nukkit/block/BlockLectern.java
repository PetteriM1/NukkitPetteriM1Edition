/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityLectern;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockLectern
extends BlockSolidMeta
implements Faceable {
    public BlockLectern() {
        this(0);
    }

    public BlockLectern(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Lectern";
    }

    @Override
    public int getId() {
        return 449;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public double getResistance() {
        return 12.5;
    }

    @Override
    public int getBurnChance() {
        return 30;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        int n = (player != null ? player.getDirection().getOpposite() : BlockFace.SOUTH).getHorizontalIndex();
        if (n >= 0) {
            this.setDamage(this.getDamage() & 0xC | n & 3);
        }
        CompoundTag compoundTag = new CompoundTag().putString("id", "Lectern").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        BlockEntityLectern blockEntityLectern = (BlockEntityLectern)BlockEntity.createBlockEntity("Lectern", this.getChunk(), compoundTag, new Object[0]);
        if (blockEntityLectern == null) {
            return false;
        }
        return super.place(item, block, block2, blockFace, d2, d3, d4, player);
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 3);
    }

    public void dropBook(Player player) {
        BlockEntityLectern blockEntityLectern;
        Item item;
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        if (blockEntity instanceof BlockEntityLectern && (item = (blockEntityLectern = (BlockEntityLectern)blockEntity).getBook()).getId() != 0) {
            blockEntityLectern.setBook(Item.get(0));
            blockEntityLectern.spawnToAll();
            this.level.dropItem(blockEntityLectern.add(0.5, 1.0, 0.5), item);
        }
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    public boolean isActivated() {
        return (this.getDamage() & 4) == 4;
    }

    public void setActivated(boolean bl) {
        if (bl) {
            this.setDamage(this.getDamage() | 4);
        } else {
            this.setDamage(this.getDamage() ^ 4);
        }
    }

    @Override
    public int getWeakPower(BlockFace blockFace) {
        return this.isActivated() ? 15 : 0;
    }

    @Override
    public int getStrongPower(BlockFace blockFace) {
        return 0;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
            if (!(blockEntity instanceof BlockEntityLectern)) {
                return false;
            }
            BlockEntityLectern blockEntityLectern = (BlockEntityLectern)blockEntity;
            Item item2 = blockEntityLectern.getBook();
            if (item2.getId() == 0 && (item.getId() == 387 || item.getId() == 386)) {
                Item item3 = item.clone();
                if (player.isSurvival()) {
                    item3.setCount(item3.getCount() - 1);
                    player.getInventory().setItemInHand(item3);
                }
                item3.setCount(1);
                blockEntityLectern.setBook(item3);
                blockEntityLectern.spawnToAll();
                this.getLevel().addSound((Vector3)this.add(0.5, 0.5, 0.5), Sound.ITEM_BOOK_PUT);
            }
        }
        return true;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }
}


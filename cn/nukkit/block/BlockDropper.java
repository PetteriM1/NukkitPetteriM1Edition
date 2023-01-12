/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityDropper;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.DropperInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Faceable;
import cn.nukkit.utils.Utils;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BlockDropper
extends BlockSolidMeta
implements Faceable {
    private boolean d;

    public BlockDropper() {
        this(0);
    }

    public BlockDropper(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 125;
    }

    @Override
    public String getName() {
        return "Dropper";
    }

    @Override
    public double getHardness() {
        return 0.5;
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
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (player != null) {
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
        }
        this.getLevel().setBlock(block, this, true);
        BlockEntity.createBlockEntity("Dropper", this.getChunk(), BlockEntity.getDefaultCompound(this, "Dropper"), new Object[0]);
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player == null) {
            return false;
        }
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (!(blockEntity instanceof BlockEntityDropper)) {
            return false;
        }
        player.addWindow(((BlockEntityDropper)blockEntity).getInventory());
        return true;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromIndex(this.getDamage() & 7);
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(125));
    }

    public Vector3 getDispensePosition() {
        BlockFace blockFace = this.getBlockFace();
        return this.add(0.5 + 0.7 * (double)blockFace.getXOffset(), 0.5 + 0.7 * (double)blockFace.getYOffset(), 0.5 + 0.7 * (double)blockFace.getZOffset());
    }

    public void dispense() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (!(blockEntity instanceof BlockEntityDropper)) {
            return;
        }
        this.level.addSound((Vector3)this, Sound.BLOCK_CLICK);
        int n = 1;
        int n2 = -1;
        Item item = null;
        DropperInventory dropperInventory = ((BlockEntityDropper)blockEntity).getInventory();
        for (Map.Entry<Integer, Item> entry : dropperInventory.getContents().entrySet()) {
            Item item2 = entry.getValue();
            if (item2.isNull() || Utils.random.nextInt(n++) != 0) continue;
            item = item2;
            n2 = entry.getKey();
        }
        if (item != null) {
            item = item.clone();
            this.drop(item);
            --item.count;
            dropperInventory.setItem(n2, item);
        }
    }

    public void drop(Item item) {
        BlockFace blockFace = this.getBlockFace();
        Vector3 vector3 = this.getDispensePosition();
        vector3.y = blockFace.getAxis() == BlockFace.Axis.Y ? (vector3.y -= 0.125) : (vector3.y -= 0.15625);
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        Vector3 vector32 = new Vector3();
        double d2 = threadLocalRandom.nextDouble() * 0.1 + 0.2;
        vector32.x = (double)blockFace.getXOffset() * d2;
        vector32.y = 0.1;
        vector32.z = (double)blockFace.getZOffset() * d2;
        vector32.x += threadLocalRandom.nextGaussian() * (double)0.0075f * 6.0;
        vector32.y += threadLocalRandom.nextGaussian() * (double)0.0075f * 6.0;
        vector32.z += threadLocalRandom.nextGaussian() * (double)0.0075f * 6.0;
        Item item2 = item.clone();
        item2.setCount(1);
        this.level.dropItem(vector3, item2, vector32);
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (blockEntity instanceof BlockEntityDropper) {
            return ContainerInventory.calculateRedstone(((BlockEntityDropper)blockEntity).getInventory());
        }
        return 0;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 3) {
            this.d = false;
            this.dispense();
            return n;
        }
        if (n == 6) {
            if ((this.level.isBlockPowered(this) || this.level.isBlockPowered(this.up())) && !this.d) {
                this.d = true;
                this.level.scheduleUpdate(this, this, 4);
            }
            return n;
        }
        return 0;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }
}


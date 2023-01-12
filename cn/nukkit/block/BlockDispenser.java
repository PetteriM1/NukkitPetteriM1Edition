/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityDispenser;
import cn.nukkit.dispenser.DispenseBehavior;
import cn.nukkit.dispenser.DispenseBehaviorRegister;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.DispenserInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Faceable;
import cn.nukkit.utils.Utils;
import java.util.Map;

public class BlockDispenser
extends BlockSolidMeta
implements Faceable {
    public BlockDispenser() {
        this(0);
    }

    public BlockDispenser(int n) {
        super(n);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public String getName() {
        return "Dispenser";
    }

    @Override
    public int getId() {
        return 23;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(23));
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (blockEntity instanceof BlockEntityDispenser) {
            return ContainerInventory.calculateRedstone(((BlockEntityDispenser)blockEntity).getInventory());
        }
        return 0;
    }

    public boolean isTriggered() {
        return (this.getDamage() & 8) > 0;
    }

    public void setTriggered(boolean bl) {
        int n = 0;
        n |= this.getBlockFace().getIndex();
        if (bl) {
            n |= 8;
        }
        this.setDamage(n);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player == null) {
            return false;
        }
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (!(blockEntity instanceof BlockEntityDispenser)) {
            return false;
        }
        player.addWindow(((BlockEntityDispenser)blockEntity).getInventory());
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
        BlockEntity.createBlockEntity("Dispenser", this.getChunk(), BlockEntity.getDefaultCompound(this, "Dispenser"), new Object[0]);
        return true;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 3) {
            this.setTriggered(false);
            this.level.setBlock(this, this, false, false);
            this.dispense();
            return n;
        }
        if (n == 6) {
            if ((this.level.isBlockPowered(this) || this.level.isBlockPowered(this.up())) && !this.isTriggered()) {
                this.setTriggered(true);
                this.level.setBlock(this, this, false, false);
                this.level.scheduleUpdate(this, this, 4);
            }
            return n;
        }
        return 0;
    }

    public void dispense() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (!(blockEntity instanceof BlockEntityDispenser)) {
            return;
        }
        this.level.addSound((Vector3)this, Sound.BLOCK_CLICK);
        int n = 1;
        int n2 = -1;
        Item item = null;
        DispenserInventory dispenserInventory = ((BlockEntityDispenser)blockEntity).getInventory();
        for (Map.Entry<Integer, Item> object2 : dispenserInventory.getContents().entrySet()) {
            Item item2 = object2.getValue();
            if (item2.isNull() || Utils.random.nextInt(n++) != 0) continue;
            item = item2;
            n2 = object2.getKey();
        }
        if (item == null) {
            return;
        }
        DispenseBehavior dispenseBehavior = DispenseBehaviorRegister.getBehavior((item = item.clone()).getId());
        Item item3 = dispenseBehavior.dispense(this, this.getBlockFace(), item);
        if (item3 == null) {
            --item.count;
            dispenserInventory.setItem(n2, item);
        } else if (!item3.equals(item)) {
            dispenserInventory.setItem(n2, item3);
        }
    }

    public Vector3 getDispensePosition() {
        BlockFace blockFace = this.getBlockFace();
        return this.add(0.5 + 0.7 * (double)blockFace.getXOffset(), 0.5 + 0.7 * (double)blockFace.getYOffset(), 0.5 + 0.7 * (double)blockFace.getZOffset());
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromIndex(this.getDamage() & 7);
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }
}


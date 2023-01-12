/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.event.redstone.RedstoneUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Faceable;

public class BlockObserver
extends BlockSolidMeta
implements Faceable {
    public BlockObserver() {
        this(0);
    }

    public BlockObserver(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 251;
    }

    @Override
    public String getName() {
        return "Observer";
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
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{Item.get(251, 0, 1)};
        }
        return new Item[0];
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (player != null) {
            if (Math.abs((double)player.getFloorX() - this.x) <= 1.0 && Math.abs((double)player.getFloorZ() - this.z) <= 1.0) {
                double d5 = player.y + (double)player.getEyeHeight();
                if (d5 - this.y > 2.0) {
                    this.setDamage(BlockFace.DOWN.getIndex());
                } else if (this.y - d5 > 0.0) {
                    this.setDamage(BlockFace.UP.getIndex());
                } else {
                    this.setDamage(player.getHorizontalFacing().getIndex());
                }
            } else {
                this.setDamage(player.getHorizontalFacing().getIndex());
            }
        } else {
            this.setDamage(0);
        }
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromIndex(this.getDamage() & 7);
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && !this.isPowered()) {
            RedstoneUpdateEvent redstoneUpdateEvent = new RedstoneUpdateEvent(this);
            this.level.getServer().getPluginManager().callEvent(redstoneUpdateEvent);
            if (redstoneUpdateEvent.isCancelled()) {
                return 0;
            }
            this.setPowered(true);
            this.level.setBlock(this, this, false, false);
            this.level.updateAroundRedstone(this, this.getBlockFace());
            this.level.scheduleUpdate(this, 5);
            return 1;
        }
        if (n == 3 && this.isPowered()) {
            RedstoneUpdateEvent redstoneUpdateEvent = new RedstoneUpdateEvent(this);
            this.level.getServer().getPluginManager().callEvent(redstoneUpdateEvent);
            if (redstoneUpdateEvent.isCancelled()) {
                return 0;
            }
            this.setPowered(false);
            this.level.setBlock(this, this, false, false);
            this.level.updateAroundRedstone(this, this.getBlockFace());
        }
        return n;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(251));
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    @Override
    public int getStrongPower(BlockFace blockFace) {
        return this.isPowered() && blockFace == this.getBlockFace() ? 15 : 0;
    }

    @Override
    public int getWeakPower(BlockFace blockFace) {
        return this.getStrongPower(blockFace);
    }

    public boolean isPowered() {
        return (this.getDamage() & 8) == 8;
    }

    public void setPowered(boolean bl) {
        this.setDamage(this.getDamage() & 7 | (bl ? 8 : 0));
    }
}


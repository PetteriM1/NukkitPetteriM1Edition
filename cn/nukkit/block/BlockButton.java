/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.event.block.BlockRedstoneEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Faceable;

public abstract class BlockButton
extends BlockFlowable
implements Faceable {
    public BlockButton() {
        this(0);
    }

    public BlockButton(int n) {
        super(n);
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (block2.isTransparent()) {
            return false;
        }
        this.setDamage(blockFace.getIndex());
        this.level.setBlock(block, this, true, true);
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (this.isActivated()) {
            return false;
        }
        this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, 0, 15));
        this.setDamage(this.getDamage() ^ 8);
        this.level.setBlock(this, this, true, false);
        this.level.addLevelSoundEvent(this, 73);
        this.level.scheduleUpdate(this, 30);
        this.level.updateAroundRedstone(this.getLocation(), null);
        this.level.updateAroundRedstone(this.getLocation().getSide(this.getFacing().getOpposite()), null);
        return true;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if (this.getSide(this.getFacing().getOpposite()).isTransparent()) {
                this.level.useBreakOn(this, Item.get(270));
                return 1;
            }
        } else if (n == 3) {
            if (this.isActivated()) {
                this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, 15, 0));
                this.setDamage(this.getDamage() ^ 8);
                this.level.setBlock(this, this, true, false);
                this.level.addLevelSoundEvent(this, 74);
                this.level.updateAroundRedstone(this.getLocation(), null);
                this.level.updateAroundRedstone(this.getLocation().getSide(this.getFacing().getOpposite()), null);
            }
            return 3;
        }
        return 0;
    }

    public boolean isActivated() {
        return (this.getDamage() & 8) == 8;
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    @Override
    public int getWeakPower(BlockFace blockFace) {
        return this.isActivated() ? 15 : 0;
    }

    @Override
    public int getStrongPower(BlockFace blockFace) {
        return !this.isActivated() ? 0 : (this.getFacing() == blockFace ? 15 : 0);
    }

    public BlockFace getFacing() {
        int n = this.isActivated() ? this.getDamage() ^ 8 : this.getDamage();
        return BlockFace.fromIndex(n);
    }

    @Override
    public boolean onBreak(Item item) {
        if (this.isActivated()) {
            this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, 15, 0));
        }
        return super.onBreak(item);
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


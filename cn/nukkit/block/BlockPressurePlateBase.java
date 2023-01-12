/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFence;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Event;
import cn.nukkit.event.block.BlockRedstoneEvent;
import cn.nukkit.event.entity.EntityInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;

public abstract class BlockPressurePlateBase
extends BlockFlowable {
    protected float onPitch;
    protected float offPitch;

    protected BlockPressurePlateBase() {
        this(0);
    }

    protected BlockPressurePlateBase(int n) {
        super(n);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        if (this.isActivated()) {
            return new AxisAlignedBB(this.x + 0.0625, this.y, this.z + 0.0625, this.x + 0.9375, this.y + 0.03125, this.z + 0.9375);
        }
        return new AxisAlignedBB(this.x + 0.0625, this.y, this.z + 0.0625, this.x + 0.9375, this.y + 0.0625, this.z + 0.9375);
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    public boolean isActivated() {
        return this.getDamage() == 0;
    }

    @Override
    public int onUpdate(int n) {
        int n2;
        if (n == 1) {
            Block block = this.down();
            if (block.isTransparent() && !(block instanceof BlockFence)) {
                this.level.useBreakOn(this, Item.get(270));
            }
        } else if (n == 3 && (n2 = this.getRedstonePower()) > 0) {
            this.updateState(n2);
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Block block3 = block.down();
        if (block3.isTransparent() && !(block3 instanceof BlockFence)) {
            return false;
        }
        this.level.setBlock(block, this, true, true);
        return true;
    }

    @Override
    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return new AxisAlignedBB(this.x + 0.125, this.y, this.z + 0.125, this.x + 0.875, this.y + 0.25, this.z + 0.875);
    }

    @Override
    public void onEntityCollide(Entity entity) {
        int n = this.getRedstonePower();
        if (n == 0) {
            Event event = entity instanceof Player ? new PlayerInteractEvent((Player)entity, null, this, null, PlayerInteractEvent.Action.PHYSICAL) : new EntityInteractEvent(entity, this);
            this.level.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.updateState(n);
            }
        }
    }

    protected void updateState(int n) {
        boolean bl;
        int n2 = this.computeRedstoneStrength();
        boolean bl2 = n > 0;
        boolean bl3 = bl = n2 > 0;
        if (n != n2) {
            this.setRedstonePower(n2);
            this.level.setBlock(this, this, false, false);
            this.level.updateAroundRedstone(this, null);
            this.level.updateAroundRedstone(this.getLocation().down(), null);
            if (!bl && bl2) {
                this.playOffSound();
                this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, 15, 0));
            } else if (bl && !bl2) {
                this.playOnSound();
                this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, 0, 15));
            }
        }
        if (bl) {
            this.level.scheduleUpdate(this, 20);
        }
    }

    @Override
    public boolean onBreak(Item item) {
        this.level.setBlock(this, Block.get(0), true, true);
        if (this.getRedstonePower() > 0) {
            this.level.updateAroundRedstone(this, null);
            this.level.updateAroundRedstone(this.getLocation().down(), null);
        }
        return true;
    }

    @Override
    public int getWeakPower(BlockFace blockFace) {
        return this.getRedstonePower();
    }

    @Override
    public int getStrongPower(BlockFace blockFace) {
        return blockFace == BlockFace.UP ? this.getRedstonePower() : 0;
    }

    public int getRedstonePower() {
        return this.getDamage();
    }

    public void setRedstonePower(int n) {
        this.setDamage(n);
    }

    protected void playOnSound() {
        this.level.addLevelSoundEvent(this, 73);
    }

    protected void playOffSound() {
        this.level.addLevelSoundEvent(this, 74);
    }

    protected abstract int computeRedstoneStrength();

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0, 1);
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


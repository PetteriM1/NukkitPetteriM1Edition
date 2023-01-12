/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockTripWireHook;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;

public class BlockTripWire
extends BlockFlowable {
    public BlockTripWire(int n) {
        super(n);
    }

    public BlockTripWire() {
        this(0);
    }

    @Override
    public int getId() {
        return 132;
    }

    @Override
    public String getName() {
        return "Tripwire";
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    @Override
    public Item toItem() {
        return Item.get(287);
    }

    public boolean isPowered() {
        return (this.getDamage() & 1) > 0;
    }

    public boolean isAttached() {
        return (this.getDamage() & 4) > 0;
    }

    public boolean isDisarmed() {
        return (this.getDamage() & 8) > 0;
    }

    public void setPowered(boolean bl) {
        if (bl ^ this.isPowered()) {
            this.setDamage(this.getDamage() ^ 1);
        }
    }

    public void setAttached(boolean bl) {
        if (bl ^ this.isAttached()) {
            this.setDamage(this.getDamage() ^ 4);
        }
    }

    public void setDisarmed(boolean bl) {
        if (bl ^ this.isDisarmed()) {
            this.setDamage(this.getDamage() ^ 8);
        }
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (!entity.doesTriggerPressurePlate()) {
            return;
        }
        boolean bl = this.isPowered();
        if (!bl) {
            this.setPowered(true);
            this.level.setBlock(this, this, true, false);
            this.updateHook(false);
            this.level.scheduleUpdate(this, 10);
        }
    }

    public void updateHook(boolean bl) {
        block0: for (BlockFace blockFace : new BlockFace[]{BlockFace.SOUTH, BlockFace.WEST}) {
            for (int k = 1; k < 42; ++k) {
                Block block = this.getSide(blockFace, k);
                if (block instanceof BlockTripWireHook) {
                    BlockTripWireHook blockTripWireHook = (BlockTripWireHook)block;
                    if (blockTripWireHook.getFacing() != blockFace.getOpposite()) continue block0;
                    blockTripWireHook.calculateState(false, true, k, this);
                    continue block0;
                }
                if (block.getId() != 132) continue block0;
            }
        }
    }

    @Override
    public int onUpdate(int n) {
        if (n == 3) {
            Entity[] entityArray;
            if (!this.isPowered()) {
                return n;
            }
            boolean bl = false;
            for (Entity entity : entityArray = this.level.getCollidingEntities(this.getCollisionBoundingBox())) {
                if (!entity.doesTriggerPressurePlate()) continue;
                bl = true;
            }
            if (bl) {
                this.level.scheduleUpdate(this, 10);
            } else {
                this.setPowered(false);
                this.level.setBlock(this, this, true, false);
                this.updateHook(false);
            }
            return n;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.getLevel().setBlock(this, this, true, true);
        this.updateHook(false);
        return true;
    }

    @Override
    public boolean onBreak(Item item) {
        if (item.getId() == 359) {
            this.setDisarmed(true);
            this.level.setBlock(this, this, true, false);
            this.updateHook(false);
            this.getLevel().setBlock(this, Block.get(0), true, true);
        } else {
            this.setPowered(true);
            this.getLevel().setBlock(this, Block.get(0), true, true);
            this.updateHook(true);
        }
        return true;
    }

    @Override
    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return new AxisAlignedBB(this.x, this.y, this.z, this.x + 1.0, this.y + 0.5, this.z + 1.0);
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockRail;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityMinecartAbstract;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;

public class BlockRailDetector
extends BlockRail {
    public BlockRailDetector() {
        this(0);
        this.canBePowered = true;
    }

    public BlockRailDetector(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 28;
    }

    @Override
    public String getName() {
        return "Detector Rail";
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    @Override
    public int getWeakPower(BlockFace blockFace) {
        return this.isActive() ? 15 : 0;
    }

    @Override
    public int getStrongPower(BlockFace blockFace) {
        return !this.isActive() ? 0 : (blockFace == BlockFace.UP ? 15 : 0);
    }

    @Override
    public int onUpdate(int n) {
        if (n == 3) {
            this.updateState();
            return n;
        }
        return super.onUpdate(n);
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        this.updateState();
    }

    protected void updateState() {
        boolean bl = this.isActive();
        boolean bl2 = false;
        boolean bl3 = false;
        for (Entity entity : this.level.getCollidingEntities(new AxisAlignedBB((double)this.getFloorX() + 0.125, this.getFloorY(), (double)this.getFloorZ() + 0.125, (double)this.getFloorX() + 0.875, (double)this.getFloorY() + 0.75, (double)this.getFloorZ() + 0.875))) {
            if (!(entity instanceof EntityMinecartAbstract)) continue;
            bl2 = true;
            break;
        }
        if (bl2 && !bl) {
            this.setActive(true);
            this.level.scheduleUpdate(this, this, 0);
            this.level.scheduleUpdate(this, this.down(), 0);
            bl3 = true;
        }
        if (!bl2 && bl) {
            this.setActive(false);
            this.level.scheduleUpdate(this, this, 0);
            this.level.scheduleUpdate(this, this.down(), 0);
            bl3 = true;
        }
        if (bl3) {
            this.level.updateComparatorOutputLevel(this);
        }
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{this.toItem()};
    }
}


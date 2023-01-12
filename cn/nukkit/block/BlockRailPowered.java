/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockRail;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Rail;

public class BlockRailPowered
extends BlockRail {
    public BlockRailPowered() {
        this(0);
        this.canBePowered = true;
    }

    public BlockRailPowered(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 27;
    }

    @Override
    public String getName() {
        return "Powered Rail";
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 || n == 6 || n == 3) {
            boolean bl;
            if (super.onUpdate(n) == 1) {
                return 0;
            }
            boolean bl2 = bl = this.level.isBlockPowered(this.getLocation()) || this.checkSurrounding(this, true, 0) || this.checkSurrounding(this, false, 0);
            if (this.isActive() != bl) {
                this.setActive(bl);
                this.level.updateAround(this.down());
                if (this.getOrientation().isAscending()) {
                    this.level.updateAround(this.up());
                }
            }
            return n;
        }
        return 0;
    }

    protected boolean checkSurrounding(Vector3 vector3, boolean bl, int n) {
        int n2;
        int n3;
        if (n >= 8) {
            return false;
        }
        int n4 = vector3.getFloorX();
        Block block = this.level.getBlock(n4, n3 = vector3.getFloorY(), n2 = vector3.getFloorZ());
        if (!Rail.isRailBlock(block)) {
            return false;
        }
        BlockRail blockRail = (BlockRail)block;
        Rail.Orientation orientation = null;
        boolean bl2 = true;
        switch (blockRail.getOrientation()) {
            case STRAIGHT_NORTH_SOUTH: {
                if (bl) {
                    ++n2;
                    break;
                }
                --n2;
                break;
            }
            case STRAIGHT_EAST_WEST: {
                if (bl) {
                    --n4;
                    break;
                }
                ++n4;
                break;
            }
            case ASCENDING_EAST: {
                if (bl) {
                    --n4;
                } else {
                    ++n4;
                    ++n3;
                    bl2 = false;
                }
                orientation = Rail.Orientation.STRAIGHT_EAST_WEST;
                break;
            }
            case ASCENDING_WEST: {
                if (bl) {
                    --n4;
                    ++n3;
                    bl2 = false;
                } else {
                    ++n4;
                }
                orientation = Rail.Orientation.STRAIGHT_EAST_WEST;
                break;
            }
            case ASCENDING_NORTH: {
                if (bl) {
                    ++n2;
                } else {
                    --n2;
                    ++n3;
                    bl2 = false;
                }
                orientation = Rail.Orientation.STRAIGHT_NORTH_SOUTH;
                break;
            }
            case ASCENDING_SOUTH: {
                if (bl) {
                    ++n2;
                    ++n3;
                    bl2 = false;
                } else {
                    --n2;
                }
                orientation = Rail.Orientation.STRAIGHT_NORTH_SOUTH;
                break;
            }
            default: {
                return false;
            }
        }
        return this.canPowered(new Vector3(n4, n3, n2), orientation, n, bl) || bl2 && this.canPowered(new Vector3(n4, n3 - 1, n2), orientation, n, bl);
    }

    protected boolean canPowered(Vector3 vector3, Rail.Orientation orientation, int n, boolean bl) {
        Block block = this.level.getBlock(vector3);
        if (!(block instanceof BlockRailPowered)) {
            return false;
        }
        Rail.Orientation orientation2 = ((BlockRailPowered)block).getOrientation();
        return (orientation != Rail.Orientation.STRAIGHT_EAST_WEST || orientation2 != Rail.Orientation.STRAIGHT_NORTH_SOUTH && orientation2 != Rail.Orientation.ASCENDING_NORTH && orientation2 != Rail.Orientation.ASCENDING_SOUTH) && (orientation != Rail.Orientation.STRAIGHT_NORTH_SOUTH || orientation2 != Rail.Orientation.STRAIGHT_EAST_WEST && orientation2 != Rail.Orientation.ASCENDING_EAST && orientation2 != Rail.Orientation.ASCENDING_WEST) && (this.level.isBlockPowered(vector3) || this.checkSurrounding(vector3, bl, n + 1));
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


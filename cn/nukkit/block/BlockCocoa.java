/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.Faceable;
import cn.nukkit.utils.Utils;

public class BlockCocoa
extends BlockTransparentMeta
implements Faceable {
    protected static final AxisAlignedBB[] EAST = new AxisAlignedBB[]{new AxisAlignedBB(0.6875, 0.4375, 0.375, 0.9375, 0.75, 0.625), new AxisAlignedBB(0.5625, 0.3125, 0.3125, 0.9375, 0.75, 0.6875), new AxisAlignedBB(0.5625, 0.3125, 0.3125, 0.9375, 0.75, 0.6875)};
    protected static final AxisAlignedBB[] WEST = new AxisAlignedBB[]{new AxisAlignedBB(0.0625, 0.4375, 0.375, 0.3125, 0.75, 0.625), new AxisAlignedBB(0.0625, 0.3125, 0.3125, 0.4375, 0.75, 0.6875), new AxisAlignedBB(0.0625, 0.3125, 0.3125, 0.4375, 0.75, 0.6875)};
    protected static final AxisAlignedBB[] NORTH = new AxisAlignedBB[]{new AxisAlignedBB(0.375, 0.4375, 0.0625, 0.625, 0.75, 0.3125), new AxisAlignedBB(0.3125, 0.3125, 0.0625, 0.6875, 0.75, 0.4375), new AxisAlignedBB(0.3125, 0.3125, 0.0625, 0.6875, 0.75, 0.4375)};
    protected static final AxisAlignedBB[] SOUTH = new AxisAlignedBB[]{new AxisAlignedBB(0.375, 0.4375, 0.6875, 0.625, 0.75, 0.9375), new AxisAlignedBB(0.3125, 0.3125, 0.5625, 0.6875, 0.75, 0.9375), new AxisAlignedBB(0.3125, 0.3125, 0.5625, 0.6875, 0.75, 0.9375)};
    private static final short[] e = new short[]{0, 0, 0, 2, 3, 1};
    private static final short[] d = new short[]{3, 4, 2, 5, 3, 4, 2, 5, 3, 4, 2, 5};

    public BlockCocoa() {
        this(0);
    }

    public BlockCocoa(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 127;
    }

    @Override
    public String getName() {
        return "Cocoa";
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        if (this.boundingBox == null) {
            this.boundingBox = this.recalculateBoundingBox();
        }
        return this.boundingBox;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        AxisAlignedBB[] axisAlignedBBArray;
        int n = this.getDamage();
        if (n > 11) {
            this.setDamage(11);
        }
        switch (this.getDamage()) {
            case 1: 
            case 5: 
            case 9: {
                axisAlignedBBArray = EAST;
                break;
            }
            case 2: 
            case 6: 
            case 10: {
                axisAlignedBBArray = SOUTH;
                break;
            }
            case 3: 
            case 7: 
            case 11: {
                axisAlignedBBArray = WEST;
                break;
            }
            default: {
                axisAlignedBBArray = NORTH;
            }
        }
        return axisAlignedBBArray[this.getDamage() >> 2].getOffsetBoundingBox(this.x, this.y, this.z);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (block2.getId() == 17 && (block2.getDamage() & 3) == 3 && blockFace != BlockFace.DOWN && blockFace != BlockFace.UP) {
            this.setDamage(e[blockFace.getIndex()]);
            this.level.setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            Block block = this.getSide(BlockFace.fromIndex(d[this.getDamage()]));
            if (block.getId() == 17 || (block.getDamage() & 3) == 3) return 0;
            this.getLevel().useBreakOn(this);
            return 1;
        }
        if (n != 2) return 0;
        if (Utils.random.nextInt(2) != 1) return 2;
        if (this.getDamage() >> 2 >= 2) return 0;
        BlockCocoa blockCocoa = (BlockCocoa)this.clone();
        blockCocoa.setDamage(blockCocoa.getDamage() + 4);
        BlockGrowEvent blockGrowEvent = new BlockGrowEvent(this, blockCocoa);
        Server.getInstance().getPluginManager().callEvent(blockGrowEvent);
        if (blockGrowEvent.isCancelled()) return 2;
        this.getLevel().setBlock(this, blockGrowEvent.getNewState(), true, true);
        return 0;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == 351 && item.getDamage() == 15) {
            Block block = this.clone();
            if (this.getDamage() >> 2 < 2) {
                block.setDamage(block.getDamage() + 4);
                BlockGrowEvent blockGrowEvent = new BlockGrowEvent(this, block);
                Server.getInstance().getPluginManager().callEvent(blockGrowEvent);
                if (blockGrowEvent.isCancelled()) {
                    return false;
                }
                this.getLevel().setBlock(this, blockGrowEvent.getNewState(), true, true);
                this.level.addParticle(new BoneMealParticle(this));
                if (player != null && !player.isCreative()) {
                    --item.count;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public double getResistance() {
        return 15.0;
    }

    @Override
    public double getHardness() {
        return 0.2;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public Item toItem() {
        return Item.get(351, DyeColor.BROWN.getDyeData());
    }

    @Override
    public Item[] getDrops(Item item) {
        if (this.getDamage() >= 8) {
            return new Item[]{Item.get(351, 3, Utils.rand(2, 3))};
        }
        return new Item[]{Item.get(351, 3, 1)};
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


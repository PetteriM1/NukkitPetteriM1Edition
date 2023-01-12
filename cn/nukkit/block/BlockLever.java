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
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockLever
extends BlockFlowable
implements Faceable {
    public BlockLever() {
        this(0);
    }

    public BlockLever(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Lever";
    }

    @Override
    public int getId() {
        return 69;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{this.toItem()};
    }

    public boolean isPowerOn() {
        return (this.getDamage() & 8) > 0;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, this.isPowerOn() ? 15 : 0, this.isPowerOn() ? 0 : 15));
        this.setDamage(this.getDamage() ^ 8);
        this.getLevel().setBlock(this, this, false, true);
        if (this.isPowerOn()) {
            this.level.addLevelSoundEvent(this, 73);
        } else {
            this.level.addLevelSoundEvent(this, 74);
        }
        LeverOrientation leverOrientation = LeverOrientation.byMetadata(this.isPowerOn() ? this.getDamage() ^ 8 : this.getDamage());
        BlockFace blockFace = leverOrientation.getFacing();
        this.level.updateAroundRedstone(this.getLocation(), null);
        this.level.updateAroundRedstone(this.getLocation().getSide(blockFace.getOpposite()), this.isPowerOn() ? blockFace : null);
        return true;
    }

    @Override
    public int onUpdate(int n) {
        int n2;
        BlockFace blockFace;
        if (n == 1 && !this.getSide(blockFace = LeverOrientation.byMetadata(n2 = this.isPowerOn() ? this.getDamage() ^ 8 : this.getDamage()).getFacing().getOpposite()).isSolid()) {
            this.level.useBreakOn(this);
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (block2.isNormalBlock()) {
            this.setDamage(LeverOrientation.forFacings(blockFace, player.getHorizontalFacing()).getMetadata());
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, Block.get(0), true, true);
        if (this.isPowerOn()) {
            BlockFace blockFace = LeverOrientation.byMetadata(this.getDamage() ^ 8).getFacing();
            this.level.updateAround(this.getLocation().getSide(blockFace.getOpposite()));
        }
        return true;
    }

    @Override
    public int getWeakPower(BlockFace blockFace) {
        return this.isPowerOn() ? 15 : 0;
    }

    @Override
    public int getStrongPower(BlockFace blockFace) {
        if (!this.isPowerOn()) {
            return 0;
        }
        return LeverOrientation.byMetadata(this.getDamage() ^ 8).getFacing() == blockFace ? 15 : 0;
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }

    public static enum LeverOrientation {
        DOWN_X(0, "down_x", BlockFace.DOWN),
        EAST(1, "east", BlockFace.EAST),
        WEST(2, "west", BlockFace.WEST),
        SOUTH(3, "south", BlockFace.SOUTH),
        NORTH(4, "north", BlockFace.NORTH),
        UP_Z(5, "up_z", BlockFace.UP),
        UP_X(6, "up_x", BlockFace.UP),
        DOWN_Z(7, "down_z", BlockFace.DOWN);

        private static final LeverOrientation[] d;
        private final int b;
        private final String c;
        private final BlockFace a;

        private LeverOrientation(int n2, String string2, BlockFace blockFace) {
            this.b = n2;
            this.c = string2;
            this.a = blockFace;
        }

        public int getMetadata() {
            return this.b;
        }

        public BlockFace getFacing() {
            return this.a;
        }

        public String toString() {
            return this.c;
        }

        public static LeverOrientation byMetadata(int n) {
            if (n < 0 || n >= d.length) {
                n = 0;
            }
            return d[n];
        }

        public static LeverOrientation forFacings(BlockFace blockFace, BlockFace blockFace2) {
            switch (blockFace) {
                case DOWN: {
                    switch (blockFace2.getAxis()) {
                        case X: {
                            return DOWN_X;
                        }
                        case Z: {
                            return DOWN_Z;
                        }
                    }
                    throw new IllegalArgumentException("Invalid entityFacing " + (Object)((Object)blockFace2) + " for facing " + (Object)((Object)blockFace));
                }
                case UP: {
                    switch (blockFace2.getAxis()) {
                        case X: {
                            return UP_X;
                        }
                        case Z: {
                            return UP_Z;
                        }
                    }
                    throw new IllegalArgumentException("Invalid entityFacing " + (Object)((Object)blockFace2) + " for facing " + (Object)((Object)blockFace));
                }
                case NORTH: {
                    return NORTH;
                }
                case SOUTH: {
                    return SOUTH;
                }
                case WEST: {
                    return WEST;
                }
                case EAST: {
                    return EAST;
                }
            }
            throw new IllegalArgumentException("Invalid facing: " + (Object)((Object)blockFace));
        }

        public String getName() {
            return this.c;
        }

        static {
            d = new LeverOrientation[LeverOrientation.values().length];
            LeverOrientation[] leverOrientationArray = LeverOrientation.values();
            int n = leverOrientationArray.length;
            for (int k = 0; k < n; ++k) {
                LeverOrientation leverOrientation;
                LeverOrientation.d[leverOrientation.b] = leverOrientation = leverOrientationArray[k];
            }
        }

        private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
            return illegalArgumentException;
        }
    }
}


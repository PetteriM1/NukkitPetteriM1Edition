/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFenceGate;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.utils.BlockColor;

public class BlockFence
extends BlockTransparentMeta {
    public static final int FENCE_OAK = 0;
    public static final int FENCE_SPRUCE = 1;
    public static final int FENCE_BIRCH = 2;
    public static final int FENCE_JUNGLE = 3;
    public static final int FENCE_ACACIA = 4;
    public static final int FENCE_DARK_OAK = 5;

    public BlockFence() {
        this(0);
    }

    public BlockFence(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 85;
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public double getResistance() {
        return 15.0;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Oak Fence", "Spruce Fence", "Birch Fence", "Jungle Fence", "Acacia Fence", "Dark Oak Fence", "", ""};
        return stringArray[this.getDamage() & 7];
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        boolean bl = this.canConnect(this.north());
        boolean bl2 = this.canConnect(this.south());
        boolean bl3 = this.canConnect(this.west());
        boolean bl4 = this.canConnect(this.east());
        double d2 = bl ? 0.0 : 0.375;
        double d3 = bl2 ? 1.0 : 0.625;
        double d4 = bl3 ? 0.0 : 0.375;
        double d5 = bl4 ? 1.0 : 0.625;
        return new AxisAlignedBB(this.x + d4, this.y, this.z + d2, this.x + d5, this.y + 1.5, this.z + d3);
    }

    @Override
    public int getBurnChance() {
        return 5;
    }

    @Override
    public int getBurnAbility() {
        return 20;
    }

    public boolean canConnect(Block block) {
        return block instanceof BlockFence || block instanceof BlockFenceGate || block.isSolid() && !block.isTransparent();
    }

    @Override
    public BlockColor getColor() {
        switch (this.getDamage() & 7) {
            default: {
                return BlockColor.WOOD_BLOCK_COLOR;
            }
            case 1: {
                return BlockColor.SPRUCE_BLOCK_COLOR;
            }
            case 2: {
                return BlockColor.SAND_BLOCK_COLOR;
            }
            case 3: {
                return BlockColor.DIRT_BLOCK_COLOR;
            }
            case 4: {
                return BlockColor.ORANGE_BLOCK_COLOR;
            }
            case 5: 
        }
        return BlockColor.BROWN_BLOCK_COLOR;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, this.getDamage());
    }
}


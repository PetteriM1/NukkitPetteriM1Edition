/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;

public class BlockTurtleEgg
extends BlockTransparentMeta {
    public BlockTurtleEgg() {
        this(0);
    }

    public BlockTurtleEgg(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Turtle Egg";
    }

    @Override
    public int getId() {
        return 414;
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
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x + 0.2, this.y, this.z + 0.2, this.x + 0.8, this.y + 0.45, this.z + 0.8);
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && !BlockTurtleEgg.canStayOnFullSolid(this.down())) {
            this.getLevel().useBreakOn(this);
            return 1;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (!BlockTurtleEgg.canStayOnFullSolid(this.down())) {
            return false;
        }
        return this.getLevel().setBlock(this, this, true, true);
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


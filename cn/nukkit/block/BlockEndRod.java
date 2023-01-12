/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Faceable;

public class BlockEndRod
extends BlockTransparentMeta
implements Faceable {
    private static final int[] d = new int[]{0, 1, 3, 2, 5, 4};

    public BlockEndRod() {
        this(0);
    }

    public BlockEndRod(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "End Rod";
    }

    @Override
    public int getId() {
        return 208;
    }

    @Override
    public double getHardness() {
        return 0.0;
    }

    @Override
    public double getResistance() {
        return 0.0;
    }

    @Override
    public int getLightLevel() {
        return 14;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x + 0.4, this.y, this.z + 0.4, this.x + 0.6, this.y + 1.0, this.z + 0.6);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.setDamage(d[player != null ? blockFace.getIndex() : 0]);
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }
}


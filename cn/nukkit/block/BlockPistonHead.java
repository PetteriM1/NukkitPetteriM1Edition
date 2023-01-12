/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockPistonBase;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Faceable;

public class BlockPistonHead
extends BlockTransparentMeta
implements Faceable {
    public BlockPistonHead() {
        this(0);
    }

    public BlockPistonHead(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 34;
    }

    @Override
    public String getName() {
        return "Piston Head";
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
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    public boolean onBreak(Item item) {
        this.level.setBlock(this, Block.get(0), true, true);
        Block block = this.getSide(this.getBlockFace().getOpposite());
        if (block instanceof BlockPistonBase && ((BlockPistonBase)block).getFacing() == this.getBlockFace()) {
            block.onBreak(item);
        }
        return true;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromIndex(this.getDamage()).getOpposite();
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(0));
    }
}


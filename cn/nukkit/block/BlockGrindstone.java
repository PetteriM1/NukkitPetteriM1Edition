/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockGrindstone
extends BlockTransparentMeta
implements Faceable {
    public BlockGrindstone() {
        this(0);
    }

    public BlockGrindstone(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Grindstone";
    }

    @Override
    public int getId() {
        return 450;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public double getResistance() {
        return 6.0;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 3);
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(450));
    }

    @Override
    public boolean canBePushed() {
        return false;
    }
}


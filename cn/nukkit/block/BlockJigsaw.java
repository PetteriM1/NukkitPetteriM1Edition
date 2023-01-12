/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Faceable;

public class BlockJigsaw
extends BlockSolidMeta
implements Faceable {
    public BlockJigsaw() {
        this(0);
    }

    public BlockJigsaw(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Jigsaw";
    }

    @Override
    public int getId() {
        return 466;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public double getResistance() {
        return 1.8E7;
    }

    @Override
    public double getHardness() {
        return -1.0;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromIndex(this.getDamage());
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (Math.abs(player.x - this.x) < 2.0 && Math.abs(player.z - this.z) < 2.0) {
            double d5 = player.y + (double)player.getEyeHeight();
            if (d5 - this.y > 2.0) {
                this.setDamage(BlockFace.UP.getIndex());
            } else if (this.y - d5 > 0.0) {
                this.setDamage(BlockFace.DOWN.getIndex());
            } else {
                this.setDamage(player.getHorizontalFacing().getOpposite().getIndex());
            }
        } else {
            this.setDamage(player.getHorizontalFacing().getOpposite().getIndex());
        }
        return super.place(item, block, block2, blockFace, d2, d3, d4, player);
    }
}


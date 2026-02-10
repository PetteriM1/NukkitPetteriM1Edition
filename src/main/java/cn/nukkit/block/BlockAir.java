package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.utils.BlockColor;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class BlockAir extends BlockTransparent {

    @Override
    public boolean canBeFlowedInto() {
        return true;
    }

    @Override
    public boolean canBePlaced() {
        return false;
    }

    @Override
    public boolean canBeReplaced() {
        return true;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 0;
    }

    @Override
    public int getId() {
        return AIR;
    }

    @Override
    public String getName() {
        return "Air";
    }

    @Override
    public double getResistance() {
        return 0;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}

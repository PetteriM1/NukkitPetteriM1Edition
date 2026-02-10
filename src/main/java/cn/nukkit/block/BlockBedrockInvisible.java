package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

/**
 * Created by Pub4Game on 03.01.2016.
 */
public class BlockBedrockInvisible extends BlockSolid {

    @Override
    public BlockColor getColor() {
        return BlockColor.TRANSPARENT_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public int getId() {
        return INVISIBLE_BEDROCK;
    }

    @Override
    public String getName() {
        return "Invisible Bedrock";
    }

    @Override
    public double getResistance() {
        return 18000000;
    }

    @Override
    public WaterloggingType getWaterloggingType() {
        return WaterloggingType.FLOW_INTO_BLOCK;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public Item toItem() {
        return Item.get(0);
    }
}

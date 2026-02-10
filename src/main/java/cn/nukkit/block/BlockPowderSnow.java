package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockPowderSnow extends BlockTransparent {

    public BlockPowderSnow() {
        super();
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.SNOW_BLOCK;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SNOW_BLOCK_COLOR;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    public double getHardness() {
        return 0; //0.25
    }

    @Override
    public int getId() {
        return POWDER_SNOW;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Powder Snow";
    }

    @Override
    public double getResistance() {
        return 1.25;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }
}

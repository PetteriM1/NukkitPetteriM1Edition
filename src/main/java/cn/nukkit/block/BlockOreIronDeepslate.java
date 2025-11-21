package cn.nukkit.block;

import cn.nukkit.item.ItemID;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockOreIronDeepslate extends BlockOreIron {

    public BlockOreIronDeepslate() {
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.IRON_ORE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DEEPSLATE_GRAY_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 4.5;
    }

    @Override
    public int getId() {
        return DEEPSLATE_IRON_ORE;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Deepslate Iron Ore";
    }

    @Override
    protected int getRawMaterial() {
        return ItemID.RAW_IRON;
    }
}

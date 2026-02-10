package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockOreEmeraldDeepslate extends BlockOreEmerald {

    public BlockOreEmeraldDeepslate() {
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.EMERALD_ORE;
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
        return DEEPSLATE_EMERALD_ORE;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Deepslate Emerald Ore";
    }
}

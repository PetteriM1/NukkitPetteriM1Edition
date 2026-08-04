package cn.nukkit.block;

import cn.nukkit.item.ItemDye;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockOreLapisDeepslate extends BlockOreLapis {

    public BlockOreLapisDeepslate() {
    }

    @Override
    protected int getRawMaterialMeta() {
        return ItemDye.LAPIS_LAZULI;
    }

    @Override
    public int getId() {
        return DEEPSLATE_LAPIS_ORE;
    }

    @Override
    public double getHardness() {
        return 4.5;
    }

    @Override
    public String getName() {
        return "Deepslate Lapis Lazuli Ore";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DEEPSLATE_GRAY_BLOCK_COLOR;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.LAPIS_ORE;
    }
}

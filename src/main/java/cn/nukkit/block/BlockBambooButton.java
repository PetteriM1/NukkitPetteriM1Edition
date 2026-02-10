package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockBambooButton extends BlockButtonWooden {

    public BlockBambooButton() {
        this(0);
    }

    public BlockBambooButton(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BAMBOO_BUTTON;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public String getName() {
        return "Bamboo Button";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.WOODEN_BUTTON;
    }
}

package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockCherryButton extends BlockButtonWooden {

    public BlockCherryButton() {
        this(0);
    }

    public BlockCherryButton(int meta) {
        super(meta);
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.WOODEN_BUTTON;
    }

    @Override
    public int getId() {
        return CHERRY_BUTTON;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public String getName() {
        return "Cherry Button";
    }
}

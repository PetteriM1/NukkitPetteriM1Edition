package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockCherryTrapdoor extends BlockTrapdoor {

    public BlockCherryTrapdoor() {
        this(0);
    }

    public BlockCherryTrapdoor(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WHITE_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return CHERRY_TRAPDOOR;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public String getName() {
        return "Cherry Trapdoor";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.TRAPDOOR;
    }
}

package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockPackedMud extends BlockSolid {

    public BlockPackedMud() {
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 1;
    }

    @Override
    public int getId() {
        return PACKED_MUD;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_19_0;
    }

    @Override
    public String getName() {
        return "Packed Mud";
    }

    @Override
    public double getResistance() {
        return 3;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.DIRT;
    }
}

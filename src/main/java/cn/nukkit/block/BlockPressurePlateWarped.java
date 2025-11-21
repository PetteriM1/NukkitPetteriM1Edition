package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockPressurePlateWarped extends BlockPressurePlateWood {

    public BlockPressurePlateWarped() {
        this(0);
    }

    public BlockPressurePlateWarped(int meta) {
        super(meta);
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.WOODEN_PRESSURE_PLATE;
    }

    @Override
    public int getId() {
        return WARPED_PRESSURE_PLATE;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_16_0;
    }

    @Override
    public String getName() {
        return "Warped Pressure Plate";
    }
}


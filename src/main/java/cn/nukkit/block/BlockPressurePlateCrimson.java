package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockPressurePlateCrimson extends BlockPressurePlateWood {

    public BlockPressurePlateCrimson() {
        this(0);
    }

    public BlockPressurePlateCrimson(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return CRIMSON_PRESSURE_PLATE;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_16_0;
    }

    @Override
    public String getName() {
        return "Crimson Pressure Plate";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.WOODEN_PRESSURE_PLATE;
    }
}

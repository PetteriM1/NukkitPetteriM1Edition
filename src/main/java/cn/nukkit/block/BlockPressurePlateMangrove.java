package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockPressurePlateMangrove extends BlockPressurePlateWood {

    public BlockPressurePlateMangrove() {
        this(0);
    }

    public BlockPressurePlateMangrove(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.RED_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return MANGROVE_PRESSURE_PLATE;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_19_0;
    }

    @Override
    public String getName() {
        return "Mangrove Pressure Plate";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.WOODEN_PRESSURE_PLATE;
    }
}

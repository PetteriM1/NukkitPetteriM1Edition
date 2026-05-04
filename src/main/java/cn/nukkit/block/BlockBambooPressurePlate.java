package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockBambooPressurePlate extends BlockPressurePlateWood {

    public BlockBambooPressurePlate() {
        this(0);
    }

    public BlockBambooPressurePlate(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.YELLOW_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return BAMBOO_PRESSURE_PLATE;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public String getName() {
        return "Bamboo Pressure Plate";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.WOODEN_PRESSURE_PLATE;
    }
}

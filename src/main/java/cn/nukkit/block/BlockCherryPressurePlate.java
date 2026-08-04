package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockCherryPressurePlate extends BlockPressurePlateWood {

    public BlockCherryPressurePlate() {
        this(0);
    }

    public BlockCherryPressurePlate(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Cherry Pressure Plate";
    }

    @Override
    public int getId() {
        return CHERRY_PRESSURE_PLATE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WHITE_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.WOODEN_PRESSURE_PLATE;
    }
}

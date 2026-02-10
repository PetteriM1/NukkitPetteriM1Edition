package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

public class BlockPressurePlateJungle extends BlockPressurePlateWood {

    public BlockPressurePlateJungle() {
        this(0);
    }

    public BlockPressurePlateJungle(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return JUNGLE_PRESSURE_PLATE;
    }

    @Override
    public String getName() {
        return "Jungle Pressure Plate";
    }
}

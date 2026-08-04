package cn.nukkit.block;

public class BlockPressurePlatePolishedBlackstone extends BlockPressurePlateStone {

    public BlockPressurePlatePolishedBlackstone() {
        this(0);
    }

    public BlockPressurePlatePolishedBlackstone(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return POLISHED_BLACKSTONE_PRESSURE_PLATE;
    }

    @Override
    public String getName() {
        return "Polished Blackstone Pressure Plate";
    }
}

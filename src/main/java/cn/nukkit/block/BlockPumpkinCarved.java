package cn.nukkit.block;

public class BlockPumpkinCarved extends BlockPumpkin {

    public BlockPumpkinCarved() {
    }

    public BlockPumpkinCarved(int meta) {
        super(meta);
    }

    @Override
    public boolean canBeActivated() {
        return false;
    }

    @Override
    public int getId() {
        return CARVED_PUMPKIN;
    }

    @Override
    public String getName() {
        return "Carved Pumpkin";
    }
}

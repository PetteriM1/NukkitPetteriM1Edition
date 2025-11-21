package cn.nukkit.block;

public class BlockSmoker extends BlockSmokerLit {

    public BlockSmoker() {
        this(0);
    }

    public BlockSmoker(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return SMOKER;
    }

    @Override
    public int getLightLevel() {
        return 0;
    }

    @Override
    public String getName() {
        return "Smoker";
    }
}

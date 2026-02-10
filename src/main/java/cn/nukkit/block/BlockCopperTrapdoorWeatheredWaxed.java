package cn.nukkit.block;

public class BlockCopperTrapdoorWeatheredWaxed extends BlockCopperTrapdoorWeathered {

    public BlockCopperTrapdoorWeatheredWaxed() {
        this(0);
    }

    public BlockCopperTrapdoorWeatheredWaxed(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return WAXED_WEATHERED_COPPER_TRAPDOOR;
    }

    @Override
    public String getName() {
        return "Waxed Weathered Copper Trapdoor";
    }

    @Override
    public boolean isWaxed() {
        return true;
    }
}

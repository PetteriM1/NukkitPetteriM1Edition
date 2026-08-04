package cn.nukkit.block;

public class BlockBlackstonePolished extends BlockBlackstone {

    @Override
    public int getId() {
        return POLISHED_BLACKSTONE;
    }

    @Override
    public String getName() {
        return "Polished Blackstone";
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}

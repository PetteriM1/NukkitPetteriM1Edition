package cn.nukkit.block;

public class BlockButtonPolishedBlackstone extends BlockButtonStone {

    public BlockButtonPolishedBlackstone() {
        this(0);
    }

    public BlockButtonPolishedBlackstone(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return POLISHED_BLACKSTONE_BUTTON;
    }

    @Override
    public String getName() {
        return "Polished Blackstone Button";
    }
}

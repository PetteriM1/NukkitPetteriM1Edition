package cn.nukkit.block;

/**
 * Created by PetteriM1
 */
public class BlockColoredTorchBP extends BlockTorch {

    public BlockColoredTorchBP() {
        this(0);
    }

    public BlockColoredTorchBP(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Blue Torch";
    }

    @Override
    public int getId() {
        return COLORED_TORCH_BP;
    }
}

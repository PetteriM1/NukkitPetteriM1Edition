package cn.nukkit.block;

/**
 * Created by PetteriM1
 */
public class BlockColoredTorchRG extends BlockTorch {

    public BlockColoredTorchRG() {
        this(0);
    }

    public BlockColoredTorchRG(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Red Torch";
    }

    @Override
    public int getId() {
        return COLORED_TORCH_RG;
    }
}

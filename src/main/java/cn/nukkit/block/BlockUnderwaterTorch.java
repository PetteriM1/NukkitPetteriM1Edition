package cn.nukkit.block;

/**
 * Created by PetteriM1
 */
public class BlockUnderwaterTorch extends BlockTorch {

    public BlockUnderwaterTorch() {
        this(0);
    }

    public BlockUnderwaterTorch(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Underwater Torch";
    }

    @Override
    public int getId() {
        return UNDERWATER_TORCH;
    }

    @Override
    public int onUpdate(int type) {
        return 0;
    }

    @Override
    public boolean canBeFlowedInto() {
        return false;
    }
}

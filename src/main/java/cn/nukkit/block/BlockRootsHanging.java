package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockRootsHanging extends BlockRoots {

    public BlockRootsHanging() {
        this(0);
    }

    public BlockRootsHanging(int meta) {
        super(0); // hanging roots have no variants
    }

    @Override
    public int getId() {
        return HANGING_ROOTS;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Hanging Roots";
    }

    @Override
    protected boolean isSupportValid() {
        Block up = this.up();
        return up.isSolid() && !up.isTransparent();
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.AIR;
    }
}

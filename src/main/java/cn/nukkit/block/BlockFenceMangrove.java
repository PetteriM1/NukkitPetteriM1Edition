package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockFenceMangrove extends BlockFence {

    public BlockFenceMangrove() {
        this(0);
    }

    public BlockFenceMangrove(int meta) {
        super(meta);
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.FENCE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.RED_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return MANGROVE_FENCE;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Mangrove Fence";
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(this.getId(), 0), 0);
    }
}

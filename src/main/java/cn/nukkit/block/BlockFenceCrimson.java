package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockFenceCrimson extends BlockFence {

    public BlockFenceCrimson() {
        this(0);
    }

    public BlockFenceCrimson(int meta) {
        super(meta);
    }

    @Override
    public int getBurnAbility() {
        return 0;
    }

    @Override
    public int getBurnChance() {
        return 0;
    }

    @Override
    public int getId() {
        return CRIMSON_FENCE;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_16_0;
    }

    @Override
    public String getName() {
        return "Crimson Fence";
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.FENCE;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(this.getId(), 0), 0);
    }
}

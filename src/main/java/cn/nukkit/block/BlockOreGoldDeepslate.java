package cn.nukkit.block;

import cn.nukkit.item.ItemID;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockOreGoldDeepslate extends BlockOreGold {

    public BlockOreGoldDeepslate() {
    }

    @Override
    protected int getRawMaterial() {
        return ItemID.RAW_GOLD;
    }

    @Override
    public int getId() {
        return DEEPSLATE_GOLD_ORE;
    }

    @Override
    public double getHardness() {
        return 4.5;
    }

    @Override
    public String getName() {
        return "Deepslate Gold Ore";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DEEPSLATE_GRAY_BLOCK_COLOR;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.GOLD_ORE;
    }
}

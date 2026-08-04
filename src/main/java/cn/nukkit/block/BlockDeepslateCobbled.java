package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockDeepslateCobbled extends BlockSolid {

    public BlockDeepslateCobbled() {
    }

    @Override
    public String getName() {
        return "Cobbled Deepslate";
    }

    @Override
    public int getId() {
        return COBBLED_DEEPSLATE;
    }

    @Override
    public double getHardness() {
        return 3.5;
    }

    @Override
    public double getResistance() {
        return 6.0;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }


    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.STONE;
    }
}

package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockReinforcedDeeplsate extends BlockSolid {

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.OBSIDIAN;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DEEPSLATE_GRAY_BLOCK_COLOR;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    public double getHardness() {
        return 55;
    }

    @Override
    public int getId() {
        return REINFORCED_DEEPSLATE;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_19_0;
    }

    @Override
    public String getName() {
        return "Reinforced Deeplsate";
    }

    @Override
    public double getResistance() {
        return 1200;
    }
}

package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.material.BlockType;

public class BlockGlassTinted extends BlockGlass {

    public BlockGlassTinted() {
        // Does nothing
    }

    @Override
    public String getName() {
        return "Tinted Glass";
    }

    @Override
    public int getId() {
        return TINTED_GLASS;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{this.toItem()};
    }

    @Override
    public boolean canSilkTouch() {
        return false;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.GLASS;
    }

    @Override
    public int getAlternateMeta(int protocol) {
        return DyeColor.GRAY.getWoolData();
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GRAY_BLOCK_COLOR;
    }
}

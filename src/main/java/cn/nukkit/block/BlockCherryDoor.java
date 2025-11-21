package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockCherryDoor extends BlockDoorWood {

    public BlockCherryDoor() {
        this(0);
    }

    public BlockCherryDoor(int meta) {
        super(meta);
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.WOODEN_DOOR_BLOCK;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WHITE_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return CHERRY_DOOR_BLOCK;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public String getName() {
        return "Cherry Door Block";
    }

    @Override
    public Item toItem() {
        return Item.get(Item.CHERRY_DOOR);
    }
}

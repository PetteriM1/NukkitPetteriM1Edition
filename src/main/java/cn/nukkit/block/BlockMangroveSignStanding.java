package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockMangroveSignStanding extends BlockSignPost {

    public BlockMangroveSignStanding() {
        this(0);
    }

    public BlockMangroveSignStanding(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return MANGROVE_STANDING_SIGN;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Mangrove Sign Post";
    }

    @Override
    protected int getPostId() {
        return MANGROVE_STANDING_SIGN;
    }

    @Override
    protected int getWallId() {
        return MANGROVE_WALL_SIGN;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.SIGN_POST;
    }

    @Override
    public Item toItem() {
        return Item.get(ItemID.MANGROVE_SIGN);
    }
}

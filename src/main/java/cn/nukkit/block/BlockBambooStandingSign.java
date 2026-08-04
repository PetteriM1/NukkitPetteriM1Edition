package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockBambooStandingSign extends BlockSignPost {

    public BlockBambooStandingSign() {
        this(0);
    }

    public BlockBambooStandingSign(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BAMBOO_STANDING_SIGN;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public String getName() {
        return "Bamboo Sign Post";
    }

    @Override
    protected int getPostId() {
        return BAMBOO_STANDING_SIGN;
    }

    @Override
    protected int getWallId() {
        return BAMBOO_WALL_SIGN;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.SIGN_POST;
    }

    @Override
    public Item toItem() {
        return Item.get(ItemID.BAMBOO_SIGN);
    }
}

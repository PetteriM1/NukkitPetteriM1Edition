package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;

public class BlockDarkOakSignStanding extends BlockSignPost {

    public BlockDarkOakSignStanding() {
        this(0);
    }

    public BlockDarkOakSignStanding(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return DARK_OAK_STANDING_SIGN;
    }

    @Override
    public String getName() {
        return "Dark Oak Sign Post";
    }

    @Override
    protected int getPostId() {
        return DARK_OAK_STANDING_SIGN;
    }

    @Override
    protected int getWallId() {
        return DARK_OAK_WALL_SIGN;
    }

    @Override
    public Item toItem() {
        return Item.get(ItemID.DARKOAK_SIGN);
    }
}

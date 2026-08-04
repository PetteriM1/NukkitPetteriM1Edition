package cn.nukkit.block;

import cn.nukkit.item.Item;

public class BlockCrimsonWallSign extends BlockWallSign {

    public BlockCrimsonWallSign() {
        this(0);
    }

    public BlockCrimsonWallSign(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return CRIMSON_WALL_SIGN;
    }

    @Override
    public String getName() {
        return "Crimson WallSign";
    }

    @Override
    public Item toItem() {
        return Item.get(Item.CRIMSON_SIGN);
    }
}

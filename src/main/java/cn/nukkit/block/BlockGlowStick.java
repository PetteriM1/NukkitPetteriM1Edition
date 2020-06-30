package cn.nukkit.block;

import cn.nukkit.item.Item;

/**
 * Created by PetteriM1
 */
public class BlockGlowStick extends BlockTransparentMeta {

    public BlockGlowStick() {
        this(0);
    }

    public BlockGlowStick(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return GLOW_STICK;
    }

    @Override
    public String getName() {
        return "Glow Stick";
    }

    @Override
    public Item toItem() {
        return Item.get(AIR);
    }
}

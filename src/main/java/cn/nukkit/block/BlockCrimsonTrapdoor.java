package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;

public class BlockCrimsonTrapdoor extends BlockTrapdoor {

    public BlockCrimsonTrapdoor() {
        this(0);
    }

    public BlockCrimsonTrapdoor(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return CRIMSON_TRAPDOOR;
    }

    @Override
    public String getName() {
        return "Crimson Trapdoor";
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(this.getId(), 0), 0);
    }
}

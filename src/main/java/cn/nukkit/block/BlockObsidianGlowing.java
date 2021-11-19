package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;

/**
 * Created on 2015/11/22 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockObsidianGlowing extends BlockObsidian {

    @Override
    public int getId() {
        return GLOWING_OBSIDIAN;
    }

    @Override
    public String getName() {
        return "Glowing Obsidian";
    }

    @Override
    public int getLightLevel() {
        return 12;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(GLOWING_OBSIDIAN));
    }

    @Override
    public boolean onBreak(Item item) {
        return this.getLevel().setBlock(this, Block.get(BlockID.AIR), true, true);
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBePulled() {
        return false;
    }
}

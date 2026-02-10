package cn.nukkit.block;

import cn.nukkit.entity.Entity;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

public class BlockSoulSoil extends BlockSolid {

    public BlockSoulSoil() {
        super();
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BROWN_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public int getId() {
        return SOUL_SOIL;
    }

    @Override
    public String getName() {
        return "Soul Soil";
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHOVEL;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.motionX *= 0.4d;
        entity.motionZ *= 0.4d;
    }
}

package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;

public class BlockLightBlock extends BlockTransparentMeta {

    public BlockLightBlock() {
        this(0);
    }

    public BlockLightBlock(int meta) {
        super(meta);
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    @Override
    public double getFrictionFactor() {
        return 0.9;
    }

    @Override
    public double getHardness() {
        return 0;
    }

    @Override
    public int getId() {
        return LIGHT_BLOCK;
    }

    @Override
    public int getLightLevel() {
        return getDamage() & 0xF;
    }

    @Override
    public String getName() {
        return "Light Block";
    }

    @Override
    public double getResistance() {
        return 3600000.8;
    }

    @Override
    public boolean canBeFlowedInto() {
        return true;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeReplaced() {
        return true;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public Item toItem() {
        return Item.get(Item.AIR);
    }
}

/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;

public class BlockLightBlock
extends BlockTransparentMeta {
    public BlockLightBlock() {
        this(0);
    }

    public BlockLightBlock(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Light Block";
    }

    @Override
    public int getId() {
        return 470;
    }

    @Override
    public int getLightLevel() {
        return this.getDamage() & 0xF;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    @Override
    public boolean canBeFlowedInto() {
        return true;
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
    public double getHardness() {
        return 0.0;
    }

    @Override
    public double getResistance() {
        return 0.0;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public Item toItem() {
        return Item.get(0);
    }

    @Override
    public boolean canBePushed() {
        return false;
    }
}


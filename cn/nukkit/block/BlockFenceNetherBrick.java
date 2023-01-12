/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFence;
import cn.nukkit.block.BlockFenceGate;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockFenceNetherBrick
extends BlockFence {
    public BlockFenceNetherBrick() {
        this(0);
    }

    public BlockFenceNetherBrick(int n) {
        super(n);
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public String getName() {
        return "Nether Brick Fence";
    }

    @Override
    public int getId() {
        return 113;
    }

    @Override
    public double getResistance() {
        return 10.0;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public boolean canConnect(Block block) {
        return block instanceof BlockFenceNetherBrick || block instanceof BlockFenceGate || block.isSolid() && !block.isTransparent();
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.NETHERRACK_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public int getBurnChance() {
        return 0;
    }

    @Override
    public int getBurnAbility() {
        return 0;
    }
}


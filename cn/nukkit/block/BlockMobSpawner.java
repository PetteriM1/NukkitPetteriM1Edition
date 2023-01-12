/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

public class BlockMobSpawner
extends BlockSolid {
    @Override
    public String getName() {
        return "Monster Spawner";
    }

    @Override
    public int getId() {
        return 52;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public double getHardness() {
        return 5.0;
    }

    @Override
    public double getResistance() {
        return 25.0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (!super.place(item, block, block2, blockFace, d2, d3, d4, player)) {
            return false;
        }
        BlockEntity.createBlockEntity("MobSpawner", this.getChunk(), BlockEntity.getDefaultCompound(this, "MobSpawner"), new Object[0]);
        return true;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    public int getLightLevel() {
        return 3;
    }
}


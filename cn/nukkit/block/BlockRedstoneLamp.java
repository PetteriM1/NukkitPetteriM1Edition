/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.event.redstone.RedstoneUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

public class BlockRedstoneLamp
extends BlockSolid {
    @Override
    public String getName() {
        return "Redstone Lamp";
    }

    @Override
    public int getId() {
        return 123;
    }

    @Override
    public double getHardness() {
        return 0.3;
    }

    @Override
    public double getResistance() {
        return 1.5;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (this.level.isBlockPowered(this.getLocation())) {
            this.level.setBlock(this, Block.get(124), false, true);
        } else {
            this.level.setBlock(this, this, false, true);
        }
        return true;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 || n == 6) {
            RedstoneUpdateEvent redstoneUpdateEvent = new RedstoneUpdateEvent(this);
            this.getLevel().getServer().getPluginManager().callEvent(redstoneUpdateEvent);
            if (redstoneUpdateEvent.isCancelled()) {
                return 0;
            }
            if (this.level.isBlockPowered(this.getLocation())) {
                this.level.setBlock(this, Block.get(124), false, false);
                return 1;
            }
        }
        return 0;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{new ItemBlock(Block.get(123))};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}


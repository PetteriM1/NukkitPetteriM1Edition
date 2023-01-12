/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparent;
import cn.nukkit.event.block.BlockFadeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockIce
extends BlockTransparent {
    @Override
    public int getId() {
        return 79;
    }

    @Override
    public String getName() {
        return "Ice";
    }

    @Override
    public double getResistance() {
        return 0.5;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getFrictionFactor() {
        return 0.98;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public boolean onBreak(Item item) {
        if (this.getLevel().getDimension() == 1 || item.hasEnchantment(16) || this.down().getId() == 0) {
            return super.onBreak(item);
        }
        return this.getLevel().setBlock(this, Block.get(8), true);
    }

    @Override
    public int onUpdate(int n) {
        if (n == 2 && this.level.getBlockLightAt((int)this.x, (int)this.y, (int)this.z) >= 12) {
            BlockFadeEvent blockFadeEvent = new BlockFadeEvent(this, this.level.getDimension() == 1 ? BlockIce.get(0) : BlockIce.get(8));
            this.level.getServer().getPluginManager().callEvent(blockFadeEvent);
            if (!blockFadeEvent.isCancelled()) {
                this.level.setBlock(this, blockFadeEvent.getNewState(), true);
            }
            return 2;
        }
        return 0;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.hasEnchantment(16)) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ICE_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}


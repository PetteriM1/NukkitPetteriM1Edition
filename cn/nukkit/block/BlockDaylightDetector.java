/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

public class BlockDaylightDetector
extends BlockTransparent {
    @Override
    public int getId() {
        return 151;
    }

    @Override
    public String getName() {
        return "Daylight Detector";
    }

    @Override
    public double getHardness() {
        return 0.2;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        this.getLevel().setBlock(this, Block.get(178));
        return true;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public boolean isPowerSource() {
        return this.level.isAnimalSpawningAllowedByTime();
    }

    @Override
    public int getWeakPower(BlockFace blockFace) {
        return this.level.isAnimalSpawningAllowedByTime() ? 15 : 0;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public double getMaxY() {
        return this.y + 0.625;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 || n == 3) {
            if (n == 3) {
                this.level.updateAroundRedstone(this, null);
            }
            this.level.scheduleUpdate(this, 40);
        }
        return 0;
    }
}


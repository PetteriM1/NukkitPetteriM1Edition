/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDaylightDetector;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;

public class BlockDaylightDetectorInverted
extends BlockDaylightDetector {
    @Override
    public int getId() {
        return 178;
    }

    @Override
    public String getName() {
        return "Daylight Detector Inverted";
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        this.getLevel().setBlock(this, Block.get(151));
        return true;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(151), 0);
    }

    @Override
    public boolean isPowerSource() {
        return !this.level.isAnimalSpawningAllowedByTime();
    }

    @Override
    public int getWeakPower(BlockFace blockFace) {
        return this.level.isAnimalSpawningAllowedByTime() ? 0 : 15;
    }
}


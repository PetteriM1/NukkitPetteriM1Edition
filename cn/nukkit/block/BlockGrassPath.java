/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockGrass;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;

public class BlockGrassPath
extends BlockGrass {
    @Override
    public int getId() {
        return 198;
    }

    @Override
    public String getName() {
        return "Grass Path";
    }

    @Override
    public double getResistance() {
        return 3.25;
    }

    @Override
    public int onUpdate(int n) {
        return 0;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        Block block;
        if (item.isHoe() && ((block = this.up()) instanceof BlockAir || block instanceof BlockFlowable)) {
            item.useOn(this);
            this.getLevel().setBlock(this, BlockGrassPath.get(60), true);
            if (player != null) {
                player.getLevel().addSound((Vector3)player, Sound.STEP_GRASS);
            }
            return true;
        }
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}


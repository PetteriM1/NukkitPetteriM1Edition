/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockDirt;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;

public class BlockPodzol
extends BlockDirt {
    public BlockPodzol() {
        this(0);
    }

    public BlockPodzol(int n) {
        super(0);
    }

    @Override
    public int getId() {
        return 243;
    }

    @Override
    public String getName() {
        return "Podzol";
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        Block block;
        if (item.isShovel() && ((block = this.up()) instanceof BlockAir || block instanceof BlockFlowable)) {
            item.useOn(this);
            this.getLevel().setBlock(this, Block.get(198));
            if (player != null) {
                player.getLevel().addSound((Vector3)player, Sound.STEP_GRASS);
            }
            return true;
        }
        return false;
    }

    @Override
    public int getFullId() {
        return 3888;
    }

    @Override
    public void setDamage(int n) {
    }
}


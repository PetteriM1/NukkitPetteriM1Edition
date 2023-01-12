/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.item.Item;
import cn.nukkit.level.generator.object.mushroom.BigMushroom;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BlockMushroom
extends BlockFlowable {
    public BlockMushroom() {
        this(0);
    }

    public BlockMushroom(int n) {
        super(0);
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && !this.canStay()) {
            this.getLevel().useBreakOn(this);
            return 1;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (this.canStay()) {
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == 351 && item.getDamage() == DyeColor.WHITE.getDyeData()) {
            if (player != null && !player.isCreative()) {
                --item.count;
            }
            if ((double)ThreadLocalRandom.current().nextFloat() < 0.4) {
                this.grow();
            }
            this.level.addParticle(new BoneMealParticle(this));
            return true;
        }
        return false;
    }

    public boolean grow() {
        this.level.setBlock(this, Block.get(0), true, false);
        BigMushroom bigMushroom = new BigMushroom(this.getType());
        if (bigMushroom.generate(this.level, new NukkitRandom(), this)) {
            return true;
        }
        this.level.setBlock(this, this, true, false);
        return false;
    }

    public boolean canStay() {
        Block block = this.down();
        return block.getId() == 110 || block.getId() == 243 || !block.isTransparent() && this.level.getFullLight(this) < 13;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    protected abstract int getType();

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


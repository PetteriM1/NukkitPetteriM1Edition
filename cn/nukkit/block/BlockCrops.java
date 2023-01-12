/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public abstract class BlockCrops
extends BlockFlowable {
    protected BlockCrops(int n) {
        super(n);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (block.down().getId() == 60) {
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == 351 && item.getDamage() == 15) {
            if (this.getDamage() < 7) {
                BlockCrops blockCrops = (BlockCrops)this.clone();
                blockCrops.setDamage(blockCrops.getDamage() + Utils.random.nextInt(3) + 2);
                if (blockCrops.getDamage() > 7) {
                    blockCrops.setDamage(7);
                }
                BlockGrowEvent blockGrowEvent = new BlockGrowEvent(this, blockCrops);
                Server.getInstance().getPluginManager().callEvent(blockGrowEvent);
                if (blockGrowEvent.isCancelled()) {
                    return false;
                }
                this.getLevel().setBlock(this, blockGrowEvent.getNewState(), false, true);
                this.level.addParticle(new BoneMealParticle(this));
                if (player != null && !player.isCreative()) {
                    --item.count;
                }
            }
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if (this.down().getId() == 60) return 0;
            this.getLevel().useBreakOn(this);
            return 1;
        }
        if (n != 2) return 0;
        if (Utils.random.nextInt(2) != 1) return 2;
        if (this.getDamage() >= 7) return 0;
        BlockCrops blockCrops = (BlockCrops)this.clone();
        blockCrops.setDamage(blockCrops.getDamage() + 1);
        BlockGrowEvent blockGrowEvent = new BlockGrowEvent(this, blockCrops);
        Server.getInstance().getPluginManager().callEvent(blockGrowEvent);
        if (blockGrowEvent.isCancelled()) return 2;
        this.getLevel().setBlock(this, blockGrowEvent.getNewState(), false, true);
        return 0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }
}


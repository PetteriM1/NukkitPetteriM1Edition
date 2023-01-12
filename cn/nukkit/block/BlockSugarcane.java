/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockIceFrosted;
import cn.nukkit.block.BlockWater;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

public class BlockSugarcane
extends BlockFlowable {
    public BlockSugarcane() {
        this(0);
    }

    public BlockSugarcane(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Sugar Cane";
    }

    @Override
    public int getId() {
        return 83;
    }

    @Override
    public Item toItem() {
        return Item.get(338);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == 351 && item.getDamage() == 15) {
            int n;
            int n2;
            int n3 = 1;
            for (n2 = 1; n2 <= 2; ++n2) {
                n = this.level.getBlockIdAt(this.getFloorX(), this.getFloorY() - n2, this.getFloorZ());
                if (n != 83) continue;
                ++n3;
            }
            if (n3 < 3) {
                n2 = 0;
                n = 3 - n3;
                for (int k = 1; k <= n; ++k) {
                    Block block = this.up(k);
                    if (block.getId() == 0) {
                        BlockGrowEvent blockGrowEvent = new BlockGrowEvent(block, Block.get(83));
                        Server.getInstance().getPluginManager().callEvent(blockGrowEvent);
                        if (blockGrowEvent.isCancelled()) continue;
                        this.getLevel().setBlock(block, blockGrowEvent.getNewState(), true);
                        n2 = 1;
                        continue;
                    }
                    if (block.getId() != 83) break;
                }
                if (n2 != 0) {
                    if (player != null && !player.isCreative()) {
                        --item.count;
                    }
                    this.level.addParticle(new BoneMealParticle(this));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            Block block = this.down();
            if (block.isTransparent() && block.getId() != 83) {
                this.getLevel().useBreakOn(this);
                return 1;
            }
        } else if (n == 2 && this.down().getId() != 83) {
            if (this.getDamage() == 15) {
                for (int k = 1; k < 3; ++k) {
                    Block block = this.getLevel().getBlock((int)this.x, (int)this.y + k, (int)this.z);
                    if (block.getId() != 0) continue;
                    BlockGrowEvent blockGrowEvent = new BlockGrowEvent(block, Block.get(83));
                    Server.getInstance().getPluginManager().callEvent(blockGrowEvent);
                    if (blockGrowEvent.isCancelled()) break;
                    this.getLevel().setBlock(block, blockGrowEvent.getNewState(), false);
                    break;
                }
                this.setDamage(0);
                this.getLevel().setBlock(this, this, false);
            } else {
                this.setDamage(this.getDamage() + 1);
                this.getLevel().setBlock(this, this, false);
            }
            return 2;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (block.getId() != 0) {
            return false;
        }
        Block block3 = this.down();
        int n = block3.getId();
        if (n == 83) {
            this.getLevel().setBlock(block, Block.get(83), true);
            return true;
        }
        if (n == 2 || n == 3 || n == 12 || n == 243 || n == 110) {
            Block block4 = block3.north();
            Block block5 = block3.south();
            Block block6 = block3.west();
            Block block7 = block3.east();
            if (block4 instanceof BlockWater || block5 instanceof BlockWater || block6 instanceof BlockWater || block7 instanceof BlockWater || block4 instanceof BlockIceFrosted || block5 instanceof BlockIceFrosted || block6 instanceof BlockIceFrosted || block7 instanceof BlockIceFrosted) {
                this.getLevel().setBlock(block, Block.get(83), true);
                return true;
            }
        }
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

public class BlockCactus
extends BlockTransparentMeta {
    public BlockCactus(int n) {
        super(n);
    }

    public BlockCactus() {
        this(0);
    }

    @Override
    public int getId() {
        return 81;
    }

    @Override
    public double getHardness() {
        return 0.4;
    }

    @Override
    public double getResistance() {
        return 2.0;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x + 0.0625, this.y + 0.0625, this.z + 0.0625, this.x + 0.9375, this.y + 0.9375, this.z + 0.9375);
    }

    @Override
    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return new AxisAlignedBB(this.x, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + 1.0);
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.attack(new EntityDamageByBlockEvent(this, entity, EntityDamageEvent.DamageCause.CONTACT, 1.0f));
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            Block block = this.down();
            if (block.getId() != 12 && block.getId() != 81) {
                this.getLevel().useBreakOn(this);
            } else {
                for (int k = 2; k <= 5; ++k) {
                    Block block2 = this.getSide(BlockFace.fromIndex(k));
                    if (block2.canBeFlowedInto()) continue;
                    this.getLevel().useBreakOn(this);
                }
            }
        } else if (n == 2 && this.down().getId() != 81) {
            if (this.getDamage() == 15) {
                BaseFullChunk baseFullChunk = this.level.getChunk((int)this.x >> 4, (int)this.z >> 4);
                for (int k = 1; k < 3; ++k) {
                    Block block = this.getLevel().getBlock(baseFullChunk, (int)this.x, (int)this.y + k, (int)this.z, true);
                    if (block.getId() != 0) continue;
                    BlockGrowEvent blockGrowEvent = new BlockGrowEvent(block, Block.get(81));
                    Server.getInstance().getPluginManager().callEvent(blockGrowEvent);
                    if (blockGrowEvent.isCancelled()) continue;
                    this.getLevel().setBlock(block, blockGrowEvent.getNewState(), true, true);
                }
                this.setDamage(0);
                this.getLevel().setBlock(this, this);
            } else {
                this.setDamage(this.getDamage() + 1);
                this.getLevel().setBlock(this, this);
            }
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Block block3 = this.down();
        if (block3.getId() == 12 || block3.getId() == 81) {
            Block block4 = this.north();
            Block block5 = this.south();
            Block block6 = this.west();
            Block block7 = this.east();
            if (block4.canBeFlowedInto() && block5.canBeFlowedInto() && block6.canBeFlowedInto() && block7.canBeFlowedInto()) {
                this.getLevel().setBlock(this, this, true);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Cactus";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{Item.get(81, 0, 1)};
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


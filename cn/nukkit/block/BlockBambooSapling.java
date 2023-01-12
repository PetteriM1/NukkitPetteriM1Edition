/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockBamboo;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.MathHelper;
import cn.nukkit.utils.BlockColor;
import java.util.concurrent.ThreadLocalRandom;

public class BlockBambooSapling
extends BlockFlowable {
    public BlockBambooSapling() {
        this(0);
    }

    public BlockBambooSapling(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 419;
    }

    @Override
    public String getName() {
        return "Bamboo Sapling";
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if (this.a()) {
                this.level.useBreakOn(this, null, null, true);
            } else {
                Block block = this.up();
                if (block.getId() == 418) {
                    BlockBamboo blockBamboo = (BlockBamboo)block;
                    BlockBamboo blockBamboo2 = (BlockBamboo)Block.get(418);
                    blockBamboo2.setThick(blockBamboo.isThick());
                    this.level.setBlock(this, blockBamboo2, true, true);
                }
            }
            return n;
        }
        if (n == 2) {
            Block block = this.up();
            if (this.getAge() == 0 && block.getId() == 0 && this.level.isAnimalSpawningAllowedByTime() && ThreadLocalRandom.current().nextInt(3) == 0) {
                BlockBamboo blockBamboo = (BlockBamboo)Block.get(418);
                blockBamboo.setLeafSize(1);
                BlockGrowEvent blockGrowEvent = new BlockGrowEvent(block, blockBamboo);
                this.level.getServer().getPluginManager().callEvent(blockGrowEvent);
                if (!blockGrowEvent.isCancelled()) {
                    Block block2 = blockGrowEvent.getNewState();
                    block2.y = block.y;
                    block2.x = this.x;
                    block2.z = this.z;
                    block2.level = this.level;
                    block2.place(this.toItem(), block, this, BlockFace.DOWN, 0.5, 0.5, 0.5, null);
                }
            }
            return n;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (this.a()) {
            return false;
        }
        if (this.getLevelBlock() instanceof BlockLiquid) {
            return false;
        }
        this.level.setBlock(this, this, true, true);
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        boolean bl;
        boolean bl2 = bl = item.getId() == 351 && item.getDamage() == 15;
        if (bl || item.getBlockUnsafe() != null && item.getBlockUnsafe().getId() == 418) {
            boolean bl3 = false;
            Block block = this.up();
            if (block.getId() == 0) {
                bl3 = this.grow(block);
            }
            if (bl3) {
                if (player != null && (player.gamemode & 1) == 0) {
                    --item.count;
                }
                this.level.addParticle(new BoneMealParticle(this));
            }
            return true;
        }
        return false;
    }

    public boolean grow(Block block) {
        BlockBamboo blockBamboo = (BlockBamboo)Block.get(418);
        blockBamboo.x = this.x;
        blockBamboo.y = this.y;
        blockBamboo.z = this.z;
        blockBamboo.level = this.level;
        return blockBamboo.grow(block);
    }

    private boolean a() {
        int n = this.down().getId();
        return n != 3 && n != 2 && n != 12 && n != 13 && n != 243;
    }

    @Override
    public double getResistance() {
        return 5.0;
    }

    public int getAge() {
        return this.getDamage() & 1;
    }

    public void setAge(int n) {
        n = MathHelper.clamp(n, 0, 1) & 1;
        this.setDamage(this.getDamage() & 0xE | n);
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(418), 0);
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x + 0.125, this.y, this.z + 0.125, this.x + 0.875, this.y + 0.875, this.z + 0.875);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntitySnowball;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;
import java.util.concurrent.ThreadLocalRandom;

public class BlockChorusFlower
extends BlockTransparentMeta {
    public BlockChorusFlower() {
        super(0);
    }

    public BlockChorusFlower(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 200;
    }

    @Override
    public String getName() {
        return "Chorus Flower";
    }

    @Override
    public double getHardness() {
        return 0.4;
    }

    @Override
    public double getResistance() {
        return 0.4;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    private boolean a() {
        Block block = this.down();
        if (block.getId() == 240 || block.getId() == 121) {
            return true;
        }
        if (block.getId() != 0) {
            return false;
        }
        boolean bl = false;
        for (BlockFace blockFace : BlockFace.Plane.HORIZONTAL) {
            Block block2 = this.getSide(blockFace);
            if (block2.getId() != 240) continue;
            if (bl) {
                return false;
            }
            bl = true;
        }
        return bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int onUpdate(int n) {
        Block block;
        int n2;
        if (n == 1) {
            if (this.a()) return 0;
            this.getLevel().scheduleUpdate(this, 1);
            return n;
        }
        if (n == 3) {
            this.getLevel().useBreakOn(this, null, null, true);
            return n;
        }
        if (n != 2) return 0;
        Block block2 = this.up();
        if (block2.getId() != 0 || !(block2.getY() < 256.0)) return 2;
        if (this.isFullyAged()) return 0;
        boolean bl = false;
        boolean bl2 = false;
        Block block3 = this.down();
        if (block3.getId() == 0 || block3.getId() == 121) {
            bl = true;
        } else if (block3.getId() == 240) {
            n2 = 1;
            for (int k = 2; k < 6; ++k) {
                block = this.down(k);
                if (block.getId() == 240) {
                    ++n2;
                    continue;
                }
                if (block.getId() != 121) break;
                bl2 = true;
                break;
            }
            if (n2 < 2 || n2 <= ThreadLocalRandom.current().nextInt(bl2 ? 5 : 4)) {
                bl = true;
            }
        }
        if (bl && block2.up().getId() == 0 && this.a(block2)) {
            BlockChorusFlower blockChorusFlower = (BlockChorusFlower)this.clone();
            blockChorusFlower.y = this.y + 1.0;
            BlockGrowEvent blockGrowEvent = new BlockGrowEvent(this, blockChorusFlower);
            Server.getInstance().getPluginManager().callEvent(blockGrowEvent);
            if (blockGrowEvent.isCancelled()) return 2;
            this.getLevel().setBlock(this, Block.get(240));
            this.getLevel().setBlock(blockChorusFlower, blockGrowEvent.getNewState());
            this.getLevel().addSound((Vector3)this, Sound.BLOCK_CHORUSFLOWER_GROW);
            return 0;
        } else if (!this.isFullyAged()) {
            for (n2 = 0; n2 < ThreadLocalRandom.current().nextInt(bl2 ? 5 : 4); ++n2) {
                BlockFace blockFace = BlockFace.Plane.HORIZONTAL.random(Utils.nukkitRandom);
                block = this.getSide(blockFace);
                if (block.getId() != 0 || block.down().getId() != 0 || !this.a(block, blockFace.getOpposite())) continue;
                BlockChorusFlower blockChorusFlower = (BlockChorusFlower)this.clone();
                blockChorusFlower.x = block.x;
                blockChorusFlower.y = block.y;
                blockChorusFlower.z = block.z;
                blockChorusFlower.setAge(this.getAge() + 1);
                BlockGrowEvent blockGrowEvent = new BlockGrowEvent(this, blockChorusFlower);
                Server.getInstance().getPluginManager().callEvent(blockGrowEvent);
                if (blockGrowEvent.isCancelled()) return 2;
                this.getLevel().setBlock(this, Block.get(240));
                this.getLevel().setBlock(blockChorusFlower, blockGrowEvent.getNewState());
                this.getLevel().addSound((Vector3)this, Sound.BLOCK_CHORUSFLOWER_GROW);
            }
            return 0;
        } else {
            BlockChorusFlower blockChorusFlower = (BlockChorusFlower)this.clone();
            blockChorusFlower.setAge(this.getMaxAge());
            BlockGrowEvent blockGrowEvent = new BlockGrowEvent(this, blockChorusFlower);
            Server.getInstance().getPluginManager().callEvent(blockGrowEvent);
            if (blockGrowEvent.isCancelled()) return 2;
            this.getLevel().setBlock(blockChorusFlower, blockGrowEvent.getNewState());
            this.getLevel().addSound((Vector3)this, Sound.BLOCK_CHORUSFLOWER_DEATH);
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (!this.a()) {
            return false;
        }
        return super.place(item, block, block2, blockFace, d2, d3, d4, player);
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{this.toItem()};
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (entity instanceof EntityArrow || entity instanceof EntitySnowball) {
            entity.close();
            this.getLevel().useBreakOn(this);
        }
    }

    public int getMaxAge() {
        return 5;
    }

    public int getAge() {
        return this.getDamage();
    }

    public void setAge(int n) {
        this.setDamage(n);
    }

    public boolean isFullyAged() {
        return this.getAge() >= this.getMaxAge();
    }

    private boolean a(Block block) {
        for (BlockFace blockFace : BlockFace.Plane.HORIZONTAL) {
            if (block.getSide(blockFace).getId() == 0) continue;
            return false;
        }
        return true;
    }

    private boolean a(Block block, BlockFace blockFace) {
        for (BlockFace blockFace2 : BlockFace.Plane.HORIZONTAL) {
            if (blockFace2 == blockFace || block.getSide(blockFace2).getId() == 0) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.PURPLE_BLOCK_COLOR;
    }
}


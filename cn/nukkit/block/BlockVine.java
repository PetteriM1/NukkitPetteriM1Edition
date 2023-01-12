/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.event.block.BlockSpreadEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BlockVine
extends BlockTransparentMeta {
    public BlockVine(int n) {
        super(n);
    }

    public BlockVine() {
        this(0);
    }

    @Override
    public String getName() {
        return "Vines";
    }

    @Override
    public int getId() {
        return 106;
    }

    @Override
    public double getHardness() {
        return 0.2;
    }

    @Override
    public double getResistance() {
        return 1.0;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public boolean canBeReplaced() {
        return true;
    }

    @Override
    public boolean canBeClimbed() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.resetFallDistance();
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        boolean bl;
        double d2 = 1.0;
        double d3 = 1.0;
        double d4 = 1.0;
        double d5 = 0.0;
        double d6 = 0.0;
        double d7 = 0.0;
        boolean bl2 = bl = this.getDamage() > 0;
        if ((this.getDamage() & 2) > 0) {
            d5 = Math.max(d5, 0.0625);
            d2 = 0.0;
            d3 = 0.0;
            d6 = 1.0;
            d4 = 0.0;
            d7 = 1.0;
            bl = true;
        }
        if ((this.getDamage() & 8) > 0) {
            d2 = Math.min(d2, 0.9375);
            d5 = 1.0;
            d3 = 0.0;
            d6 = 1.0;
            d4 = 0.0;
            d7 = 1.0;
            bl = true;
        }
        if ((this.getDamage() & 1) > 0) {
            d4 = Math.min(d4, 0.9375);
            d7 = 1.0;
            d2 = 0.0;
            d5 = 1.0;
            d3 = 0.0;
            d6 = 1.0;
            bl = true;
        }
        if (!bl && this.up().isSolid()) {
            d3 = Math.min(d3, 0.9375);
            d6 = 1.0;
            d2 = 0.0;
            d5 = 1.0;
            d4 = 0.0;
            d7 = 1.0;
        }
        return new AxisAlignedBB(this.x + d2, this.y + d3, this.z + d4, this.x + d5, this.y + d6, this.z + d7);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (block.getId() != 106 && block2.isSolid() && blockFace.getHorizontalIndex() != -1) {
            this.setDamage(BlockVine.a(blockFace.getOpposite()));
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isShears()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public int onUpdate(int n) {
        ThreadLocalRandom threadLocalRandom;
        if (n == 1) {
            int n2 = this.getDamage();
            Block block = this.up();
            for (BlockFace blockFace : BlockFace.Plane.HORIZONTAL) {
                int n3 = BlockVine.a(blockFace);
                if (this.getSide(blockFace).isSolid() || block.getId() == 106 && (block.getDamage() & n3) == n3) continue;
                n2 &= ~n3;
            }
            if (n2 == 0 && !block.isSolid()) {
                this.getLevel().useBreakOn(this, null, null, true);
                return 1;
            }
            if (n2 != this.getDamage()) {
                this.level.setBlock(this, Block.get(106, n2), true);
                return 1;
            }
        } else if (n == 2 && ((Random)(threadLocalRandom = ThreadLocalRandom.current())).nextInt(4) == 0) {
            Block block;
            int n4;
            BlockFace blockFace = BlockFace.random(threadLocalRandom);
            Block block2 = this.getSide(blockFace);
            int n5 = BlockVine.a(blockFace);
            int n6 = this.getDamage();
            if (this.y < 255.0 && blockFace == BlockFace.UP && block2.getId() == 0) {
                if (this.a()) {
                    for (BlockFace blockFace2 : BlockFace.Plane.HORIZONTAL) {
                        if (!((Random)threadLocalRandom).nextBoolean() && this.getSide(blockFace2).getSide(blockFace).isSolid()) continue;
                        n6 &= ~BlockVine.a(blockFace2);
                    }
                    this.a(block2, n6, this);
                }
            } else if (blockFace.getHorizontalIndex() != -1 && (n6 & n5) != n5) {
                if (this.a()) {
                    if (block2.getId() == 0) {
                        boolean bl;
                        BlockFace blockFace3 = blockFace.rotateY();
                        BlockFace blockFace4 = blockFace.rotateYCCW();
                        Block block3 = block2.getSide(blockFace3);
                        Block block4 = block2.getSide(blockFace4);
                        int n7 = BlockVine.a(blockFace3);
                        int n8 = BlockVine.a(blockFace4);
                        boolean bl2 = (n6 & n7) == n7;
                        boolean bl3 = bl = (n6 & n8) == n8;
                        if (bl2 && block3.isSolid()) {
                            this.b(block2, BlockVine.a(blockFace3), this);
                        } else if (bl && block4.isSolid()) {
                            this.b(block2, BlockVine.a(blockFace4), this);
                        } else if (bl2 && block3.getId() == 0 && this.getSide(blockFace3).isSolid()) {
                            this.b(block3, BlockVine.a(blockFace.getOpposite()), this);
                        } else if (bl && block4.getId() == 0 && this.getSide(blockFace4).isSolid()) {
                            this.b(block4, BlockVine.a(blockFace.getOpposite()), this);
                        } else if (block2.up().isSolid()) {
                            this.b(block2, 0, this);
                        }
                    } else if (!block2.isTransparent()) {
                        this.b(this, n6 |= BlockVine.a(blockFace), null);
                    }
                }
            } else if (this.y > 0.0 && ((n4 = (block = this.down()).getId()) == 0 || n4 == 106)) {
                for (BlockFace blockFace5 : BlockFace.Plane.HORIZONTAL) {
                    if (!((Random)threadLocalRandom).nextBoolean()) continue;
                    n6 &= ~BlockVine.a(blockFace5);
                }
                this.a(block, block.getDamage() | n6, n4 == 0 ? this : null);
            }
            return 2;
        }
        return 0;
    }

    private boolean a() {
        int n = this.getFloorX();
        int n2 = this.getFloorY();
        int n3 = this.getFloorZ();
        int n4 = 0;
        for (int k = n - 4; k <= n + 4; ++k) {
            for (int i2 = n3 - 4; i2 <= n3 + 4; ++i2) {
                for (int i3 = n2 - 1; i3 <= n2 + 1; ++i3) {
                    if (this.level.getBlockIdAt(k, i3, i2) != 106 || ++n4 < 5) continue;
                    return false;
                }
            }
        }
        return true;
    }

    private void b(Block block, int n, Block block2) {
        if (block.getId() == 106 && block.getDamage() == n) {
            return;
        }
        Block block3 = BlockVine.get(106, n);
        BlockGrowEvent blockGrowEvent = block2 != null ? new BlockSpreadEvent(block, block2, block3) : new BlockGrowEvent(block, block3);
        this.level.getServer().getPluginManager().callEvent(blockGrowEvent);
        if (!blockGrowEvent.isCancelled()) {
            this.level.setBlock(block, block3, true);
        }
    }

    private void a(Block block, int n, Block block2) {
        if (block.getId() == 106 && block.getDamage() == n) {
            return;
        }
        boolean bl = false;
        for (BlockFace blockFace : BlockFace.Plane.HORIZONTAL) {
            int n2 = BlockVine.a(blockFace);
            if ((n & n2) != n2) continue;
            bl = true;
            break;
        }
        if (bl) {
            this.b(block, n, block2);
        }
    }

    private static int a(BlockFace blockFace) {
        switch (blockFace) {
            default: {
                return 1;
            }
            case WEST: {
                return 2;
            }
            case NORTH: {
                return 4;
            }
            case EAST: 
        }
        return 8;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


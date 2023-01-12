/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockDoublePlant
extends BlockFlowable {
    public static final int SUNFLOWER = 0;
    public static final int LILAC = 1;
    public static final int TALL_GRASS = 2;
    public static final int LARGE_FERN = 3;
    public static final int ROSE_BUSH = 4;
    public static final int PEONY = 5;
    public static final int TOP_HALF_BITMASK = 8;
    private static final String[] d = new String[]{"Sunflower", "Lilac", "Double Tallgrass", "Large Fern", "Rose Bush", "Peony"};

    public BlockDoublePlant() {
        this(0);
    }

    public BlockDoublePlant(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 175;
    }

    @Override
    public boolean canBeReplaced() {
        int n = this.getDamage() & 7;
        return n == 2 || n == 3;
    }

    @Override
    public String getName() {
        return d[this.getDamage() > 5 ? 0 : this.getDamage()];
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if ((this.getDamage() & 8) == 8) {
                if (this.down().getId() != 175) {
                    this.getLevel().setBlock(this, Block.get(0), false, true);
                    return 1;
                }
            } else {
                Block block = this.down();
                if (block.isTransparent() && block.getId() != 60 || this.up().getId() != 175) {
                    this.getLevel().useBreakOn(this);
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Block block3 = this.down();
        Block block4 = this.up();
        int n = block3.getId();
        if (block4.getId() == 0 && (n == 2 || n == 3 || n == 243 || n == 60 || n == 110)) {
            this.getLevel().setBlock(block4, Block.get(175, this.getDamage() ^ 8), true, false);
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean onBreak(Item item) {
        Block block = this.down();
        if ((this.getDamage() & 8) == 8) {
            this.getLevel().useBreakOn(block);
        } else {
            this.getLevel().setBlock(this, Block.get(0), true, true);
        }
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        if ((this.getDamage() & 8) != 8) {
            switch (this.getDamage() & 7) {
                case 2: 
                case 3: {
                    boolean bl;
                    boolean bl2 = bl = Utils.random.nextInt(10) == 0;
                    if (item.isShears()) {
                        if (bl) {
                            return new Item[]{Item.get(295), this.toItem()};
                        }
                        return new Item[]{this.toItem()};
                    }
                    if (bl) {
                        return new Item[]{Item.get(295)};
                    }
                    return new Item[0];
                }
            }
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == 351 && item.getDamage() == 15) {
            int n = this.getDamage() & 7;
            if (n == 0 || n == 1 || n == 4 || n == 5) {
                if (player != null && !player.isCreative()) {
                    --item.count;
                }
                this.level.addParticle(new BoneMealParticle(this));
                this.level.dropItem(this, this.toItem());
            }
            return true;
        }
        return false;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, this.getDamage() & 7, 1);
    }
}


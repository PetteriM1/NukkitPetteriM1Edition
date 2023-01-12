/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.item.Item;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockTallGrass
extends BlockFlowable {
    public BlockTallGrass() {
        this(1);
    }

    public BlockTallGrass(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 31;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Grass", "Grass", "Fern", "Fern"};
        return stringArray[this.getDamage() & 3];
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean canBeReplaced() {
        return true;
    }

    @Override
    public int getBurnChance() {
        return 60;
    }

    @Override
    public int getBurnAbility() {
        return 100;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Block block3 = this.down();
        int n = block3.getId();
        if (n == 2 || n == 3 || n == 243 || n == 60 || n == 110) {
            this.getLevel().setBlock(block, this, true);
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && this.down().isTransparent()) {
            this.getLevel().useBreakOn(this);
            return 1;
        }
        return 0;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == 351 && item.getDamage() == 15) {
            Block block = this.up();
            if (block.getId() == 0) {
                int n;
                switch (this.getDamage()) {
                    case 0: 
                    case 1: {
                        n = 2;
                        break;
                    }
                    case 2: 
                    case 3: {
                        n = 3;
                        break;
                    }
                    default: {
                        n = -1;
                    }
                }
                if (n != -1) {
                    if (player != null && !player.isCreative()) {
                        --item.count;
                    }
                    this.level.addParticle(new BoneMealParticle(this));
                    this.level.setBlock(this, BlockTallGrass.get(175, n), true, false);
                    this.level.setBlock(block, BlockTallGrass.get(175, n ^ 8), true);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Item[] getDrops(Item item) {
        boolean bl;
        boolean bl2 = bl = Utils.random.nextInt(10) == 0;
        if (item.isShears()) {
            if (bl) {
                return new Item[]{Item.get(295), Item.get(31, this.getDamage(), 1)};
            }
            return new Item[]{Item.get(31, this.getDamage(), 1)};
        }
        if (bl) {
            return new Item[]{Item.get(295)};
        }
        return new Item[0];
    }

    @Override
    public int getToolType() {
        return 5;
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


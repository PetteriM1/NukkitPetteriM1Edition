/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockFlower
extends BlockFlowable {
    public static final int TYPE_POPPY = 0;
    public static final int TYPE_BLUE_ORCHID = 1;
    public static final int TYPE_ALLIUM = 2;
    public static final int TYPE_AZURE_BLUET = 3;
    public static final int TYPE_RED_TULIP = 4;
    public static final int TYPE_ORANGE_TULIP = 5;
    public static final int TYPE_WHITE_TULIP = 6;
    public static final int TYPE_PINK_TULIP = 7;
    public static final int TYPE_OXEYE_DAISY = 8;
    public static final int TYPE_CORNFLOWER = 9;
    public static final int TYPE_LILY_OF_THE_VALLEY = 10;
    private static final String[] d = new String[]{"Poppy", "Blue Orchid", "Allium", "Azure Bluet", "Red Tulip", "Orange Tulip", "White Tulip", "Pink Tulip", "Oxeye Daisy", "Cornflower", "Lily of the Valley", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown"};

    public BlockFlower() {
        this(0);
    }

    public BlockFlower(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 38;
    }

    @Override
    public String getName() {
        return d[this.getDamage() & 0xF];
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Block block3 = this.down();
        int n = block3.getId();
        if (n == 2 || n == 3 || n == 60 || n == 243 || n == 110) {
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
            if (player != null && !player.isCreative()) {
                --item.count;
            }
            this.level.addParticle(new BoneMealParticle(this));
            for (int k = 0; k < 8; ++k) {
                Position position = this.add(Utils.random.nextInt(-3, 4), Utils.random.nextInt(-1, 2), Utils.random.nextInt(-3, 4));
                if (this.level.getBlock(position).getId() != 0 || this.level.getBlock(position.down()).getId() != 2 || !(position.getY() >= 0.0) || !(position.getY() < 256.0)) continue;
                if ((this.getDamage() == 38 || this.getDamage() == 37) && Utils.random.nextInt(10) == 0) {
                    this.level.setBlock(position, this.getUncommonFlower(), true);
                    continue;
                }
                this.level.setBlock(position, BlockFlower.get(this.getId(), this.getDamage()), true);
            }
            return true;
        }
        return false;
    }

    protected Block getUncommonFlower() {
        return BlockFlower.get(37);
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


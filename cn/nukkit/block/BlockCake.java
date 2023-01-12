/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.food.Food;
import cn.nukkit.level.Sound;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;

public class BlockCake
extends BlockTransparentMeta {
    public BlockCake(int n) {
        super(n);
    }

    public BlockCake() {
        this(0);
    }

    @Override
    public String getName() {
        return "Cake Block";
    }

    @Override
    public int getId() {
        return 92;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 0.5;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x + (double)(1 + (this.getDamage() << 1) >> 4), this.y, this.z + 0.0625, this.x - 0.0625 + 1.0, this.y + 0.5, this.z - 0.0625 + 1.0);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (this.down().getId() != 0) {
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 && this.down().getId() == 0) {
            this.getLevel().setBlock(this, Block.get(0), true);
            return 1;
        }
        return 0;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    public Item toItem() {
        return Item.get(354);
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null && player.canEat(false)) {
            if (this.getDamage() <= 6) {
                this.setDamage(this.getDamage() + 1);
            }
            if (this.getDamage() >= 6) {
                this.getLevel().setBlock(this, Block.get(0), true);
            } else {
                Food.getByRelative(this).eatenBy(player);
                this.getLevel().setBlock(this, this, true);
            }
            player.getLevel().addSound((Vector3)player, Sound.RANDOM_BURP);
            return true;
        }
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public int getComparatorInputOverride() {
        return 7 - this.getDamage() << 1;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}


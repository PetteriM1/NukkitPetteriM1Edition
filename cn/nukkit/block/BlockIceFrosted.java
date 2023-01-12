/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockIceFrosted
extends BlockTransparentMeta {
    public BlockIceFrosted() {
        this(0);
    }

    public BlockIceFrosted(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 207;
    }

    @Override
    public String getName() {
        return "Frosted Ice";
    }

    @Override
    public double getResistance() {
        return 0.5;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getFrictionFactor() {
        return 0.98;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        boolean bl = super.place(item, block, block2, blockFace, d2, d3, d4, player);
        if (bl) {
            this.level.scheduleUpdate(this, Utils.random.nextInt(20, 40));
        }
        return bl;
    }

    @Override
    public boolean onBreak(Item item) {
        this.level.setBlock(this, BlockIceFrosted.get(8), true);
        return true;
    }

    @Override
    public int onUpdate(int n) {
        int n2;
        if (n == 3) {
            int n3 = this.level.getTime() % 24000;
            if (!(n3 >= 13184 && n3 <= 22800 && this.getLevel().getBlockLightAt((int)this.x, (int)this.y, (int)this.z) < 12 || Utils.random.nextInt(3) != 0 && this.a() >= 4)) {
                this.slightlyMelt(true);
            } else {
                this.level.scheduleUpdate(this, Utils.random.nextInt(20, 40));
            }
        } else if (n == 1) {
            if (this.a() < 2) {
                this.level.setBlock(this, BlockIceFrosted.get(8), true);
            }
        } else if (!(n != 2 || (n2 = this.level.getTime() % 24000) >= 13184 && n2 <= 22800 && this.getLevel().getBlockLightAt((int)this.x, (int)this.y, (int)this.z) < 12 || Utils.random.nextInt(3) != 0 && this.a() >= 4)) {
            this.slightlyMelt(true);
        }
        return super.onUpdate(n);
    }

    @Override
    public Item toItem() {
        return Item.get(0);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ICE_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    protected void slightlyMelt(boolean bl) {
        int n = this.getDamage();
        if (n < 3) {
            this.setDamage(n + 1);
            this.level.setBlock(this, this, true);
            this.level.scheduleUpdate(this.level.getBlock(this), Utils.random.nextInt(20, 40));
        } else {
            this.level.setBlock(this, BlockIceFrosted.get(8), true);
            if (bl) {
                for (BlockFace blockFace : BlockFace.values()) {
                    Block block = this.getSide(blockFace);
                    if (!(block instanceof BlockIceFrosted)) continue;
                    ((BlockIceFrosted)block).slightlyMelt(false);
                }
            }
        }
    }

    private int a() {
        int n = 0;
        for (BlockFace blockFace : BlockFace.values()) {
            if (this.getSide(blockFace).getId() != 207 || ++n < 4) continue;
            return n;
        }
        return n;
    }
}

